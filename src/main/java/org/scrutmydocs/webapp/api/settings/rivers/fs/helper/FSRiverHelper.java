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

package org.scrutmydocs.webapp.api.settings.rivers.fs.helper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;

public class FSRiverHelper extends AbstractRiverHelper<FSRiver> {

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
	public XContentBuilder addMeta(XContentBuilder xcb, FSRiver river) throws IOException {
		xcb	
			.field("url", river.getUrl())
			.field("update_rate", river.getUpdateRate() * 1000)
			.field("includes", river.getIncludes())
			.field("excludes", river.getExcludes())
			.field("analyzer", river.getAnalyzer());

		return xcb;
	}


	/**
	 * We build "fs" rivers.
	 */
	@Override
	public String type() {
		return "fs";
	}


	/**
	 * We manage :
	 * <ul>
	 * <li>url
	 * <li>update_rate
	 * <li>includes
	 * <li>excludes
	 * <li>analyzer
	 * </ul>
	 * JSON definiton :<pre>
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
	 */
	@Override
	public FSRiver parseMeta(FSRiver river, Map<String, Object> content) {
		river.setUrl(getSingleStringValue("fs.url", content));
		river.setUpdateRate(getSingleLongValue("fs.update_rate", content) / 1000);

		// TODO Manage includes/excludes when arrays
		river.setIncludes(getSingleStringValue("fs.includes", content));
		river.setExcludes(getSingleStringValue("fs.excludes", content));
		
		river.setAnalyzer(getSingleStringValue("fs.analyzer", content));
		
		return river;
	}	

	
	/**
	 * Build a river mapping for FS
	 * @param fsriver FSRiver used to generate mapping
	 * @return An ES xcontent
	 */
	public static XContentBuilder toRiverMapping(FSRiver fsriver) {
		return toRiverMapping(fsriver.getIndexname(), fsriver.getAnalyzer());
	}

	/**
	 * Build a river mapping for FS
	 * @param fsriver FSRiver used to generate mapping
	 * @return An ES xcontent
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
}
