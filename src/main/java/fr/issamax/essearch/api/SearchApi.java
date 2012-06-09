package fr.issamax.essearch.api;

import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_NAME;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.issamax.essearch.data.Results;

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
