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

package org.scrutmydocs.webapp.api.settings.rivers.dropbox.helper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.DropBoxRiver;

public class DropBoxRiverHelper extends AbstractRiverHelper<DropBoxRiver> {

	
	/**
	 * We manage :
	 * <ul>
	 * <li>token
	 * <li>secret
	 * <li>url
	 * <li>update_rate
	 * <li>includes
	 * <li>excludes
	 * <li>analyzer
	 * </ul>
	 */
	@Override
	public XContentBuilder addMeta(XContentBuilder xcb, DropBoxRiver river) throws IOException {
		xcb	
			.field("token", river.getToken())
			.field("secret", river.getSecret())
			.field("url", river.getUrl())
			.field("update_rate", river.getUpdateRate() * 1000)
			.field("includes", river.getIncludes())
			.field("excludes", river.getExcludes())
			.field("analyzer", river.getAnalyzer());

		return xcb;
	}


	/**
	 * We build "dropbox" rivers.
	 */
	@Override
	public String type() {
		return "dropbox";
	}


	/**
	 * We manage :
	 * <ul>
	 * <li>token
	 * <li>secret
	 * <li>url
	 * <li>update_rate
	 * <li>includes
	 * <li>excludes
	 * <li>analyzer
	 * </ul>
	 * JSON definiton :<pre>
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
	 */
	@Override
	public DropBoxRiver parseMeta(DropBoxRiver river, Map<String, Object> content) {
		river.setUrl(getSingleStringValue("dropbox.url", content));
		river.setUpdateRate(getSingleLongValue("dropbox.update_rate", content) / 1000);

		river.setToken(getSingleStringValue("dropbox.token", content));
		river.setSecret(getSingleStringValue("dropbox.secret", content));

		// TODO Manage includes/excludes when arrays
		river.setIncludes(getSingleStringValue("dropbox.includes", content));
		river.setExcludes(getSingleStringValue("dropbox.excludes", content));
		
		river.setAnalyzer(getSingleStringValue("dropbox.analyzer", content));
		
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
