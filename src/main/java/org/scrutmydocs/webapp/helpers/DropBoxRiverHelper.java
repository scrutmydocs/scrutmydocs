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

package org.scrutmydocs.webapp.helpers;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.scrutmydocs.webapp.data.admin.river.DropBoxRiver;

public class DropBoxRiverHelper {
	
	/**
	 * Build a river definition for DROPBOX
	 * @param river The river definition
	 * @return An ES xcontent
	 */
	public static XContentBuilder toXContent(DropBoxRiver river) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.field("type", river.getType())
						.startObject(river.getType())
							.field("name", river.getId())
							.field("token", river.getToken())
							.field("secret", river.getSecret())
							.field("url", river.getUrl())
							.field("update_rate", river.getUpdateRate() * 1000)
							.field("includes", river.getIncludes())
							.field("excludes", river.getExcludes())
							.field("analyzer", river.getAnalyzer())
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
	 * Build a DropBox river from a JSON definiton content such as :<pre>
{
  "type" : "dropbox",
  "dropbox" : {
	  "update_rate" : 30000,
	  "name" : "tmp",
	  "token" : "XXXXXXXXXXXXXX",
	  "secret" : "ZZZZZZZZZZZZZZ",
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
	 * @return A DropBox River
	 */
	public static DropBoxRiver toRiver(Map<String, Object> content) {
		DropBoxRiver river = new DropBoxRiver();
		try {
			// First we check that it's a dropbox type
			if (!content.containsKey("type")) 
				throw new RuntimeException("Your River object should be a river and contain \"type\":\"rivertype\"");
			if (!(XContentMapValues.nodeStringValue(content.get("type"), "")).equalsIgnoreCase("dropbox")) 
				throw new RuntimeException("Your DropBox River object should be a river and contain \"type\":\"dropbox\"");
			
			// Then we dig into fs
			if (!content.containsKey("dropbox")) 
				throw new RuntimeException("A FSRiver must contain \"dropbox\":{...}");

			river.setId(getSingleStringValue("dropbox.name", content));
			river.setName(getSingleStringValue("dropbox.name", content));
			river.setToken(getSingleStringValue("dropbox.token", content));
			river.setSecret(getSingleStringValue("dropbox.secret", content));
			river.setUrl(getSingleStringValue("dropbox.url", content));
			river.setUpdateRate(getSingleLongValue("dropbox.update_rate", content) / 1000);

			// TODO Manage includes/excludes when arrays
			river.setIncludes(getSingleStringValue("dropbox.includes", content));
			river.setExcludes(getSingleStringValue("dropbox.excludes", content));
			
			river.setAnalyzer(getSingleStringValue("dropbox.analyzer", content));
			
			// Then we dig into fs
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
	
	/**
	 * Build a river mapping for DropBox
	 * @param river DropBoxRiver used to generate mapping
	 * @return An ES xcontent
	 */
	public static XContentBuilder toRiverMapping(DropBoxRiver river) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.startObject(river.getTypename())
							.startObject("properties")
								.startObject("file")
									.field("type", "attachment")
									.field("path", "full")
									.startObject("fields")
										.startObject("file")
											.field("type", "string")
											.field("store", "yes")
											.field("term_vector", "with_positions_offsets")
											.field("analyzer", river.getAnalyzer())
										.endObject()
										.startObject("author").field("type", "string").endObject()
										.startObject("title").field("type", "string").field("store", "yes").endObject()
										.startObject("name").field("type", "string").endObject()
										.startObject("date").field("type", "date").field("format", "dateOptionalTime").endObject()
										.startObject("keywords").field("type", "string").endObject()
										.startObject("content_type").field("type", "string").endObject()
									.endObject()
								.endObject()
								.startObject("name").field("type", "string").field("analyzer", "keyword").endObject()
								.startObject("pathEncoded").field("type", "string").field("analyzer", "keyword").endObject()
								.startObject("postDate").field("type", "date").field("format", "dateOptionalTime").endObject()
								.startObject("rootpath").field("type", "string").field("analyzer", "keyword").endObject()
								.startObject("virtualpath").field("type", "string").field("analyzer", "keyword").endObject()
							.endObject()
						.endObject()
					.endObject();
		} catch (IOException e) {
			// TODO Log when error
		}		
		return xb;
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
}
