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

package org.scrutmydocs.webapp.service.search;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.webapp.api.search.data.Hit;
import org.scrutmydocs.webapp.api.search.data.SearchResponse;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.prefixFilter;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.search.facet.FacetBuilders.termsFacet;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.INDEX_NAME;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.INDEX_TYPE_DOC;

@Component
public class SearchService {

	@Autowired
	protected Client esClient;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	public SearchResponse google(String search, int first, int pageSize) {
		if (logger.isDebugEnabled())
			logger.debug("google('{}', {}, {})", search, first, pageSize);

		long totalHits = -1;
		long took = -1;

		SearchResponse searchResponse = null;

		QueryBuilder qb;
		if (search == null || search.trim().length() <= 0) {
			qb = matchAllQuery();
		} else {
			qb = queryString(search);
		}

		org.elasticsearch.action.search.SearchResponse searchHits = esClient
				.prepareSearch()
                .setIndices(getSearchableIndexes())
                .setTypes(getSearchableTypes())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(first).setSize(pageSize)
                .addHighlightedField("file.filename")
				.addHighlightedField("content")
                .addHighlightedField("meta.title")
				.setHighlighterPreTags("<span class='badge badge-info'>")
				.setHighlighterPostTags("</span>")
                .addFields("*", "_source")
                .execute().actionGet();

		totalHits = searchHits.getHits().totalHits();
		took = searchHits.getTookInMillis();

		List<Hit> hits = new ArrayList();
		for (SearchHit searchHit : searchHits.getHits()) {
			Hit hit = new Hit();

			hit.setIndex(searchHit.getIndex());
			hit.setType(searchHit.getType());
			hit.setId(searchHit.getId());
			// hit.setSource(searchHit.getSourceAsString());

            if (searchHit.getFields() != null) {
                if (searchHit.getFields().get("file.content_type") != null) {
                    hit.setContentType((String) searchHit.getFields().get("file.content_type").getValue());
                }
            }

            if (searchHit.getSource() != null) {
                hit.setTitle(AbstractRiverHelper.getSingleStringValue(
                        "meta.title",
                        searchHit.getSource()));
            }


            if (searchHit.getHighlightFields() != null) {
                for (HighlightField highlightField : searchHit.getHighlightFields().values()) {
                    Text[] fragmentsBuilder = highlightField.getFragments();
                    for (Text fragment : fragmentsBuilder) {
                        hit.getHighlights().add(fragment.string());
                    }
                }
            }

			hits.add(hit);
		}

		searchResponse = new SearchResponse(took, totalHits, hits);

		if (logger.isDebugEnabled())
			logger.debug("/google({}) : {}", search, totalHits);

		return searchResponse;

	}

	/**
	 * Autocomplete TODO Create multifield mapping with edge n gram TODO Search
	 * and create facets
	 * 
	 * @param query
	 * @return
	 */
	public List<String> complete(String query) {
		List<String> results = new ArrayList<String>();

		try {
			QueryBuilder qb = matchAllQuery();
			FilterBuilder fb = prefixFilter("file", query);

			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch()
					.setIndices(INDEX_NAME)
					.setTypes(INDEX_TYPE_DOC)
					.addFacet(
							termsFacet("autocomplete").field("file")
									.facetFilter(fb))

					.execute().actionGet();

			TermsFacet terms = searchHits.getFacets().facet("autocomplete");
			for (Entry entry : terms.getEntries()) {
				results.add(entry.getTerm().string());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

    public String[] getSearchableIndexes(){
        List<String> indexList = new ArrayList<String>();
        indexList.add(INDEX_NAME);
        String[] indexArr = new String[indexList.size()];
        indexArr = indexList.toArray(indexArr);
        return indexArr;
    }

    public String[] getSearchableTypes(){
        List<String> typeList = new ArrayList<String>();
        typeList.add(INDEX_TYPE_DOC);
        String[] typeArr = new String[typeList.size()];
        typeArr = typeList.toArray(typeArr);
        return typeArr;
    }

}
