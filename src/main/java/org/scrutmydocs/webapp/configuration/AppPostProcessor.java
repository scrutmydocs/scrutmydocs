package org.scrutmydocs.webapp.configuration;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.document.DocumentService;
import org.scrutmydocs.webapp.service.settings.rivers.fs.AdminFSRiverService;
import org.scrutmydocs.webapp.util.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppPostProcessor implements
		ApplicationListener<ContextRefreshedEvent> {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired DocumentService documentService;
	@Autowired AdminFSRiverService fsRiverService;
	@Autowired Client client;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// We only inject beans when we start the Root applicationContext, aka there's no parent!
		if (event.getApplicationContext().getParent() == null) {
			try {
				logger.warn("TODO : remove automatic river creation. Just here for example purpose! See org.scrutmydocs.webapp.configuration.AppPostProcessor.");

				// We create the default mapping
				ESHelper.createIndexIfNeeded(client);
				
				// We are going to create two filesystem rivers
				fsRiverService.start(new FSRiver("myfirstriver", SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC, "Scan tmp dir", "/tmp_es", 30L, "standard", true));
				fsRiverService.start(new FSRiver("mysecondriver", SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC, "Scan second dir", "/tmp_es_second", 30L, "standard", false));

				// Add some sample files
				pushSampleDoc("LICENSE");
				pushSampleDoc("NOTICE");
			} catch (ElasticSearchException e) {
				logger.error("Error while creating rivers", e);
			}
		}
	}
	
	private void pushSampleDoc(String docname) {
		String content;
		try {
			content = ESHelper.readFileInClasspath("/demo/" + docname);
			String base64Content = Base64.encodeBytes(content.getBytes());
			Document document = new Document(docname, base64Content);
			documentService.push(document);
		} catch (Exception e) {
			logger.error("Error while pushing sample doc {}", e, docname);
		}
		
	}
	
}
