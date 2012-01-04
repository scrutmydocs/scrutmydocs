package fr.issamax.essearch.api;

import static fr.issamax.dao.elastic.factory.ESSearchProperties.*;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/download")
public class DownloaderDefault {
 
	@Autowired
	protected Client esClient;


	
	@GET
	@Path("/{id}")
	public Response get(@PathParam(value = "id") final String id) throws IOException {
		
		GetResponse response = esClient.prepareGet(INDEX_NAME, INDEX_TYPE_DOC, id)
        .setOperationThreaded(false)
        .execute()
        .actionGet();
		
		Map<String, Object> attachment = (Map<String, Object>) response.getSource().get("file");
		
		byte[] file = Base64.decode((String) attachment.get("content"));
		String name = (String) attachment.get("_name");
		String contentType = (String) attachment.get("_content_type");

		return Response
			.ok(file,contentType)
			.header("content-disposition","attachment; filename = " + name)
			.build();

	}
}