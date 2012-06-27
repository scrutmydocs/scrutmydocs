package fr.issamax.essearch.api.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.api.data.Document;
import fr.issamax.essearch.constant.ESSearchProperties;

@Component
public class RestDocumentService {

	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired Client client;

	@SuppressWarnings("unchecked")
	public Document get(String index, String type, String id) {
		GetResponse responseEs = client.prepareGet(index, type, id).execute().actionGet();
		if(!responseEs.isExists() )  {
			return null;
		}
		
		Document response = new Document();
		
		Map<String, Object> attachment = (Map<String, Object>) responseEs
				.getSource().get("file");

		response.setContent((String) attachment.get("content"));
		response.setName((String) attachment.get("_name"));
		response.setContentType((String) attachment.get("_content_type"));
		response.setId(responseEs.getId());
		response.setIndex(responseEs.getIndex());
		response.setType(responseEs.getType());
		
		return response;
	}

	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	public Document push(Document document) {
		if (document == null)
			return null;

		if (document.getIndex() == null || document.getIndex().isEmpty()) {
			document.setIndex(ESSearchProperties.INDEX_NAME);
		}
		if (document.getType() == null || document.getType().isEmpty()) {
			document.setType(ESSearchProperties.INDEX_TYPE_DOC);
		}
		try {
			IndexResponse response = client.prepareIndex(document.getIndex(), document.getType(),
					document.getId())
					.setSource(
							jsonBuilder()
									.startObject()
									.field("name", document.getName())
									.field("postDate", new Date())
									.startObject("file")
									.field("_content_type",
											document.getContentType())
									.field("_name", document.getName())
									.field("content", document.getContent())
									.endObject().endObject()).execute()
					.actionGet();
			
			document.setId(response.getId());
		} catch (Exception e) {
			logger.warn("Can not index document {}", document);
		}
		
		return document;
	}

}
