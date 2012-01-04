package fr.issamax.essearch.api;

import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_NAME;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Results;

@Component
@Path("/search")
public class SearchApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	Client esClient;

	@GET
	@Produces("application/json")
	public Results search(@DefaultValue("") @QueryParam(value = "term") String term)
			throws Exception {

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
		
		throw new APINotFoundException("Problem executing search...");
	}

}
