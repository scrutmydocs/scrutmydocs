/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.issamax.essearch.admin.river.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;

@Component
public class RiverService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void add(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("add({})", river);
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		createIndexIfNeeded(river);
		
		XContentBuilder xb = FSRiverHelper.toXContent(river);		
		
		try {
			client.prepareIndex("_river", river.getId(), "_meta").setSource(xb)
					.execute().actionGet();
		} catch (Exception e) {
			logger.warn("add({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/add({})", river);
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void delete(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("delete({})", river);
		if (river == null) return;
		
		try {
			client.admin().indices().prepareDeleteMapping("_river").setType(river.getId()).execute().actionGet();
		} catch (Exception e) {
			logger.warn("delete({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/delete({})", river);
	}

	/**
	 * Stop all rivers
	 */
	public void stop() {
		if (logger.isDebugEnabled()) logger.debug("stop()");
		CloseIndexRequestBuilder irb = new CloseIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		CloseIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("stop() : Pb when closing rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/stop()");
	}
	
	/**
	 * (Re)Start all rivers
	 */
	public void start() {
		if (logger.isDebugEnabled()) logger.debug("start()");
		OpenIndexRequestBuilder irb = new OpenIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		OpenIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("start() : Pb when starting rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/start()");
	}
	

	/**
	 * Create an index for the river if needed.
	 * <br>It helps to manage language analyzers
	 * @param river
	 */
	public void createIndexIfNeeded(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("createIndexIfNeeded({})", river);
		
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		try {
			// We check first if index already exists
			if (!isIndexExist(river.getIndexname())) {
				if (logger.isDebugEnabled()) logger.debug("Index {} doesn't exist. Creating it.", river.getIndexname());
				
				CreateIndexRequestBuilder cirb = client.admin().indices().prepareCreate(river.getIndexname());
				
				String source = readJsonDefinition("_settings");
				if (source !=  null) {
					if (logger.isTraceEnabled()) logger.trace("Mapping for [{}]={}", river.getIndexname(), source);
					cirb.setSettings(source);
				}
				
				CreateIndexResponse createIndexResponse = cirb.execute().actionGet();
				if (!createIndexResponse.acknowledged()) throw new Exception("Could not create index ["+river.getIndexname()+"].");
			}
			
			// We create the mapping for the folder type
			pushMapping(river.getIndexname(), "folder", null);
			
			// We create the mapping for the doc type
			pushMapping(river.getIndexname(), river.getTypename(), FSRiverHelper.toRiverMapping(river));

			
			XContentBuilder xb = FSRiverHelper.toXContent(river);		
			
			client.prepareIndex("_river", river.getId(), "_meta").setSource(xb)
					.execute().actionGet();
		} catch (Exception e) {
			logger.warn("add({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		
		if (logger.isDebugEnabled()) logger.debug("/createIndexIfNeeded({})", river);
	}

	/**
	 * Check if an index already exists
	 * @param index Index name
	 * @return true if index already exists
	 * @throws Exception
	 */
    private boolean isIndexExist(String index) throws Exception {
		return client.admin().indices().prepareExists(index).execute().actionGet().isExists();
	}
    
	/**
	 * Define a type for a given index and if exists with its mapping definition (loaded in classloader)
	 * @param index Index name
	 * @param type Type name
	 * @param xcontent If you give an xcontent, it will be used to define the mapping
	 * @throws Exception
	 */
	private void pushMapping(String index, String type, XContentBuilder xcontent) throws Exception {
		if (logger.isTraceEnabled()) logger.trace("pushMapping("+index+","+type+")");

		// If type does not exist, we create it
		boolean mappingExist = isMappingExist(index, type);
		if (!mappingExist) {
			if (logger.isDebugEnabled()) logger.debug("Mapping ["+index+"]/["+type+"] doesn't exist. Creating it.");
			
			String source = null;
			
			// Read the mapping json file if exists and use it
			if (xcontent == null) source = readJsonDefinition(type);
			
			if (source != null || xcontent != null) {
				PutMappingRequestBuilder pmrb = client.admin().indices()
						.preparePutMapping(index)
						.setType(type);
				
				if (source != null) {
					if (logger.isTraceEnabled()) logger.trace("Mapping for ["+index+"]/["+type+"]="+source);
					pmrb.setSource(source);
				}
				
				if (xcontent != null) {
					if (logger.isTraceEnabled()) logger.trace("Mapping for ["+index+"]/["+type+"]="+xcontent.string());
					pmrb.setSource(xcontent);
				}
				
				// Create type and mapping
				PutMappingResponse response = pmrb.execute().actionGet();			
				if (!response.acknowledged()) {
					throw new Exception("Could not define mapping for type ["+index+"]/["+type+"].");
				} else {
					if (logger.isDebugEnabled()) logger.debug("Mapping definition for ["+index+"]/["+type+"] succesfully created.");
				}
			} else {
				if (logger.isDebugEnabled()) logger.debug("No mapping definition for ["+index+"]/["+type+"]. Ignoring.");
			}
		} else {
			if (logger.isDebugEnabled()) logger.debug("Mapping ["+index+"]/["+type+"] already exists.");
		}
		if (logger.isTraceEnabled()) logger.trace("/pushMapping("+index+","+type+")");
	}
	
    /**
	 * Check if a mapping already exists in an index
	 * @param index Index name
	 * @param type Mapping name
	 * @return true if mapping exists
	 */
	private boolean isMappingExist(String index, String type) {
		ClusterState cs = client.admin().cluster().prepareState().setFilterIndices(index).execute().actionGet().getState();
		IndexMetaData imd = cs.getMetaData().index(index);
		
		if (imd == null) return false;

		MappingMetaData mdd = imd.mapping(type);

		if (mdd != null) return true;
		return false;
	}
	
	/**
	 * Read the mapping for a type.<br>
	 * Shortcut to readFileInClasspath("/estemplate/" + type + ".json");
	 * @param type Type name
	 * @return Mapping if exists. Null otherwise.
	 * @throws Exception
	 */
	private String readJsonDefinition(String type) throws Exception {
		return readFileInClasspath("/estemplate/" + type + ".json");
	}	

	/**
	 * Read a file in classpath and return its content
	 * @param url File URL Example : /es/twitter/_settings.json
	 * @return File content or null if file doesn't exist
	 * @throws Exception
	 */
	private static String readFileInClasspath(String url) throws Exception {
		StringBuffer bufferJSON = new StringBuffer();
		
		try {
			InputStream ips= RiverService.class.getResourceAsStream(url); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			
			while ((line=br.readLine())!=null){
				bufferJSON.append(line);
			}
			br.close();
		} catch (Exception e){
			return null;
		}

		return bufferJSON.toString();
	}	

}
