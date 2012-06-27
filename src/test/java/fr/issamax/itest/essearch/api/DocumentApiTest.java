package fr.issamax.itest.essearch.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import fr.issamax.essearch.api.data.Document;
import fr.issamax.essearch.constant.ESSearchProperties;

public class DocumentApiTest extends AbstractConfigurationIntegrationTest {
	private static final String BASE_URL = "http://localhost:9090/essearch/api/docs/doc/";

	private ESLogger logger = ESLoggerFactory.getLogger(DocumentApiTest.class
			.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void save() throws Exception {

		Document input = new Document("nom.pdf", "BASE64CODE");

		Document output1 = restTemplate.postForObject(BASE_URL, input,
				Document.class, new Object[] {});
		assertNotNull(output1);
		assertNotNull(output1.getId());
		assertEquals(ESSearchProperties.INDEX_NAME, output1.getIndex());
		assertEquals(ESSearchProperties.INDEX_TYPE_DOC, output1.getType());
		input.setId(output1.getId());
		assertEquals(input, output1);
	}

	@Test
	public void saveAndGet() throws Exception {
		Document input = new Document("nom2.pdf", "BASE64CODEO");
		input = restTemplate.postForObject(BASE_URL, input,
				Document.class, new Object[] {});
		assertNotNull(input);

		String url = BASE_URL + input.getIndex() + "/" + input.getType() + "/" + input.getId();
		
		Document output = restTemplate.getForObject(url, Document.class);

		assertNotNull(output);
		assertEquals(input, output);
	}

}
