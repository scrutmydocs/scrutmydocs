package fr.issamax.essearch.action;

import java.io.IOException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

@Component("fileUploadController")
@Scope("request")
public class FileUploadController {

	@Autowired
	Client esClient;

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");

		try {
			esClient
					.prepareIndex("index","document")
					.setSource(
							jsonBuilder().startObject()
									.field("name", event.getFile().getFileName())
									.field("postDate", new Date())
									.field("file", Base64
											.encodeBytes(event.getFile().getContents()))
									.endObject())
					.execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
