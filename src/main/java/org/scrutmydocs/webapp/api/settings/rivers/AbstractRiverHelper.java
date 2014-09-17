/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
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

package org.scrutmydocs.webapp.api.settings.rivers;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public abstract class AbstractRiverHelper<T extends BasicRiver> {
	
	/**
	 * Implement this to add your specific river metadata
	 * @param xcb
	 * @param river
	 * @return xcb if you don't have specific metadata to add
	 * @throws IOException 
	 */
	public abstract XContentBuilder addMeta(XContentBuilder xcb, T river) throws IOException;
	
	/**
	 * @return Type managed by this helper, fs, ...
	 */
	public abstract String type();
	
	/**
	 * Parse the json content to fill your specific metadata
	 * @param river Your river object
	 * @param content JSON Content
	 * @return river if you don't have any specific metadata
	 */
	public abstract T parseMeta(T river, Map<String, Object> content);
	
	/**
	 * Build a river definition
	 * @param river The river definition
	 * @return An ES xcontent
	 */
	public XContentBuilder toXContent(T river) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.field("type", river.getType())
						.startObject(river.getType());

			
			// We add specific metadata here
			xb = addMeta(xb, river);
			
			xb
						.endObject()
						.startObject("index")
							.field("index", river.getIndexname())
							.field("type", river.getTypename())
						.endObject()
					.endObject();
		} catch (IOException e) {
			// TODO Log when error
		}		
		return xb;
	}
	
	
	/**
	 * Build a river from a JSON definiton content such as :<pre>
{
  "type" : "fs",
  "fs" : {
	  "update_rate" : 30000,
	  "name" : "tmp",
	  "url" : "/tmp_es",
	  "includes" : "*.doc,*.pdf",
	  "excludes" : "resume.*",
  },
  "index" : {
	  "index" : "docs",
	  "type" : "doc",
	  "analyzer" : "standard"
  }
}
</pre>
	 * @param content The JSON form
	 * @return A River
	 */
	public T toRiver(T river, Map<String, Object> content) {
		try {
			// First we check that it's a type()
			if (!content.containsKey("type")) 
				throw new RuntimeException("Your River object should be a river and contain \"type\":\"rivertype\"");
			
			String type = XContentMapValues.nodeStringValue(content.get("type"), "");
			
			// Then we dig into type()
			if (!content.containsKey(type)) 
				throw new RuntimeException("A River must contain \""+type+"\":{...}");

			river.setId(getSingleStringValue(type+".name", content));
			river.setName(getSingleStringValue(type+".name", content));
			
			// We parse specific metadata depending on the river type
			river = parseMeta(river, content);
			
			// Then we dig into index
			if (content.containsKey("index")) {
				river.setIndexname(getSingleStringValue("index.index", content));
				river.setTypename(getSingleStringValue("index.type", content));
				// TODO Add support for fancy river name ???
			}
		} catch (Exception e) {
			// TODO Log when error
		}		
		return river;
	}
	
	public static String getSingleStringValue(String path, Map<String, Object> content) {
		List<Object> obj = XContentMapValues.extractRawValues(path, content);
		if(obj.isEmpty()) 
			return null;
		else 
			return ((String) obj.get(0));
	}
	
	public static Long getSingleLongValue(String path, Map<String, Object> content) {
		List<Object> obj = XContentMapValues.extractRawValues(path, content);
		return ((Integer) obj.get(0)).longValue();
	}

	public static Boolean getSingleBooleanValue(String path, Map<String, Object> content) {
		List<Object> obj = XContentMapValues.extractRawValues(path, content);
		if(obj.isEmpty()) 
			return null;
		else 
			return ((Boolean) obj.get(0));
	}
	
}
