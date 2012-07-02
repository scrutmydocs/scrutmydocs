package fr.issamax.essearch.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;

import fr.issamax.essearch.constant.ESSearchProperties;
import fr.issamax.essearch.data.admin.river.FSRiverHelper;

public class ESHelper {
	private static ESLogger logger = Loggers.getLogger("ESHelper");

	/**
	 * Define a type for a given index and if exists with its mapping definition (loaded in classloader)
	 * @param client Elasticsearch client
	 * @param index Index name
	 * @param type Type name
	 * @param xcontent If you give an xcontent, it will be used to define the mapping
	 * @throws Exception
	 */
	public static void pushMapping(Client client, String index, String type, XContentBuilder xcontent) throws Exception {
		if (logger.isTraceEnabled()) logger.trace("pushMapping("+index+","+type+")");

		// If type does not exist, we create it
		boolean mappingExist = isMappingExist(client, index, type);
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
	 * Check if a mapping (aka a type) already exists in an index
	 * @param client Elasticsearch client
	 * @param index Index name
	 * @param type Mapping name
	 * @return true if mapping exists
	 */
	public static boolean isMappingExist(Client client, String index, String type) {
		ClusterState cs = client.admin().cluster().prepareState().setFilterIndices(index).execute().actionGet().getState();
		IndexMetaData imd = cs.getMetaData().index(index);
		
		if (imd == null) return false;

		MappingMetaData mdd = imd.mapping(type);

		if (mdd != null) return true;
		return false;
	}
	
	/**
	 * Create a default index with our default settings (shortcut to {@link #createIndexIfNeeded(Client, String, String, String)})
	 * @param client Elasticsearch client
	 */
	public static void createIndexIfNeeded(Client client) {
		createIndexIfNeeded(client, null, null, null);
	}

	/**
	 * Create an index with our default settings
	 * @param client Elasticsearch client
	 * @param index Index name : default to ESSearchProperties.INDEX_NAME
	 * @param type Type name : ESSearchProperties.INDEX_TYPE_DOC
	 * @param analyzer Analyzer to apply : default to "default"
	 */
	public static void createIndexIfNeeded(Client client, String index, String type, String analyzer) {
		if (logger.isDebugEnabled()) logger.debug("createIndexIfNeeded({}, {}, {})", index, type, analyzer);
		
		String indexName = index == null ? ESSearchProperties.INDEX_NAME : index;
		String typeName = type == null ? ESSearchProperties.INDEX_TYPE_DOC : type;
		String analyzerName = analyzer == null ? "default" : analyzer;
		
		try {
			// We check first if index already exists
			if (!isIndexExist(client, indexName)) {
				if (logger.isDebugEnabled()) logger.debug("Index {} doesn't exist. Creating it.", indexName);
				
				CreateIndexRequestBuilder cirb = client.admin().indices().prepareCreate(indexName);
				
				String source = readJsonDefinition("_settings");
				if (source !=  null) {
					if (logger.isTraceEnabled()) logger.trace("Mapping for [{}]={}", indexName, source);
					cirb.setSettings(source);
				}
				
				CreateIndexResponse createIndexResponse = cirb.execute().actionGet();
				if (!createIndexResponse.acknowledged()) throw new Exception("Could not create index ["+indexName+"].");
			}
			
			// We create the mapping for the folder type
			pushMapping(client, indexName, ESSearchProperties.INDEX_TYPE_FOLDER, null);
			
			// We create the mapping for the doc type
			pushMapping(client, indexName, typeName, FSRiverHelper.toRiverMapping(typeName, analyzerName));
		} catch (Exception e) {
			logger.warn("createIndexIfNeeded() : Exception raised : {}", e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		
		if (logger.isDebugEnabled()) logger.debug("/createIndexIfNeeded()");
	}

	/**
	 * Check if an index already exists
	 * @param client Elasticsearch client
	 * @param index Index name
	 * @return true if index already exists
	 * @throws Exception
	 */
    public static boolean isIndexExist(Client client, String index) throws Exception {
		return client.admin().indices().prepareExists(index).execute().actionGet().isExists();
	}
    
	/**
	 * Read the mapping for a type.<br>
	 * Shortcut to readFileInClasspath("/estemplate/" + type + ".json");
	 * @param type Type name
	 * @return Mapping if exists. Null otherwise.
	 * @throws Exception
	 */
	private static String readJsonDefinition(String type) throws Exception {
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
			InputStream ips= ESHelper.class.getResourceAsStream(url); 
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
