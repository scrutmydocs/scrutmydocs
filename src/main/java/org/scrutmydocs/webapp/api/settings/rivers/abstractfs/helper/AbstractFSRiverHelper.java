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

package org.scrutmydocs.webapp.api.settings.rivers.abstractfs.helper;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public abstract class AbstractFSRiverHelper<T extends AbstractFSRiver> extends AbstractRiverHelper<T> {

	/**
	 * Implement this to add your specific river metadata
	 * @param xcb
	 * @param river
	 * @return xcb if you don't have specific metadata to add
	 * @throws IOException 
	 */
	public abstract XContentBuilder addFSMeta(XContentBuilder xcb, T river) throws IOException;
	
	/**
	 * Parse the json content to fill your specific metadata
	 * @param river Your river object
	 * @param content JSON Content
	 * @return river if you don't have any specific metadata
	 */
	public abstract T parseFSMeta(T river, Map<String, Object> content);
	
	/**
	 * We manage :
	 * <ul>
	 * <li>url
	 * <li>update_rate
	 * <li>includes
	 * <li>excludes
	 * <li>analyzer
	 * </ul>
	 */
	@Override
	public XContentBuilder addMeta(XContentBuilder xcb, T river) throws IOException {
		// We add specific metadata here
		xcb = addFSMeta(xcb, river);
		
		xcb	
			.field("url", river.getUrl())
			.field("update_rate", river.getUpdateRate() * 1000)
			.field("includes", river.getIncludes())
			.field("excludes", river.getExcludes())
			.field("analyzer", river.getAnalyzer());

		return xcb;
	}


	/**
	 * We build "abstractfs" rivers.
	 */
	@Override abstract public String type(); 

	/**
	 * We manage :
	 * <ul>
	 * <li>url
	 * <li>update_rate
	 * <li>includes
	 * <li>excludes
	 * <li>analyzer
	 * </ul>
	 * JSON definiton is like:<pre>
{
  "type" : "abstractfs",
  "abstractfs" : {
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

abstractfs will be replaced by your {@link #type()} content.
	 */
	@Override
	public T parseMeta(T river, Map<String, Object> content) {
		// We parse specific FS metadata depending on the FS river type
		river = parseFSMeta(river, content);
				
		river.setUrl(getSingleStringValue(type() + ".url", content));
		river.setUpdateRate(getSingleLongValue(type() + ".update_rate", content) / 1000);

		// TODO Manage includes/excludes when arrays
		river.setIncludes(getSingleStringValue(type() + ".includes", content));
		river.setExcludes(getSingleStringValue(type() + ".excludes", content));
		
		river.setAnalyzer(getSingleStringValue(type() + ".analyzer", content));
		
		return river;
	}	

	
	/**
	 * Build a river mapping for FS
	 * @param river FSRiver used to generate mapping
	 * @return An ES xcontent
	 */
	public static XContentBuilder toRiverMapping(AbstractFSRiver river) {
		return toRiverMapping(river.getIndexname(), river.getAnalyzer());
	}

	/**
	 * Build a river mapping for FS
	 * @param type The type you are building
	 * @param analyzer Analyzer to use for analyzing file content
	 * @return An ES xcontent (JSON)
	 */
	public static XContentBuilder toRiverMapping(String type, String analyzer) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.startObject(type)
							.startObject("properties")
								.startObject("file")
									.field("type", "attachment")
									.field("path", "full")
									.startObject("fields")
										.startObject("file")
											.field("type", "string")
											.field("store", "yes")
											.field("term_vector", "with_positions_offsets")
											.field("analyzer", analyzer)
										.endObject()
										.startObject("author").field("type", "string").field("store", "yes").endObject()
										.startObject("title").field("type", "string").field("store", "yes").endObject()
										.startObject("name").field("type", "string").field("store", "yes").endObject()
										.startObject("date").field("type", "date").field("store", "yes").field("format", "dateOptionalTime").endObject()
										.startObject("keywords").field("type", "string").field("store", "yes").endObject()
										.startObject("content_type").field("type", "string").field("store", "yes").endObject()
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
}
