package fr.issamax.essearch.api;

import java.io.IOException;
import java.util.Map;
import static fr.issamax.essearch.constant.ESSearchProperties.*;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/download")
public class DownloaderDefault {

	@Autowired
	protected Client esClient;

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> get(@PathVariable final String id)
			throws IOException {

		GetResponse response = esClient
				.prepareGet(INDEX_NAME, INDEX_TYPE_DOC, id)
				.execute().actionGet();
		if(!response.isExists() )  {
			//TODO return a standard page who show a message like : this document is not available
			return null;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> attachment = (Map<String, Object>) response
				.getSource().get("file");

		byte[] file = Base64.decode((String) attachment.get("content"));
		String name = (String) attachment.get("_name");
//		String contentType = (String) attachment.get("_content_type");

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<byte[]>(file, headers, HttpStatus.CREATED);

	}
}