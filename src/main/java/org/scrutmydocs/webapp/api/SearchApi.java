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

package org.scrutmydocs.webapp.api;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.INDEX_NAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.scrutmydocs.webapp.data.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/search")
public class SearchApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	Client esClient;

	@RequestMapping(value = "{term}", method = RequestMethod.GET)
	public Results search(@PathVariable final String term) throws Exception {

		try {
			QueryBuilder qb;
			if (term == null || term.trim().length() <= 0) {
				qb = matchAllQuery();
			} else {
				qb = queryString(term);
			}

			SearchResponse searchHits = esClient.prepareSearch()
					.setIndices(INDEX_NAME)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(qb).setFrom(0).setSize(10)
					.addHighlightedField("name").addHighlightedField("file")
					.setHighlighterPreTags("<b>")
					.setHighlighterPostTags("</b>").execute().actionGet();

			Results results = new Results(searchHits);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// throw new APINotFoundException("Problem executing search...");
		throw new Exception("Problem executing search...");
	}
}
