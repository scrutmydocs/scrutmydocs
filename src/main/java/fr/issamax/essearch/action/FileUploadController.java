package fr.issamax.essearch.action;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.*;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
					.prepareIndex(INDEX_NAME,INDEX_TYPE_DOC)
					.setSource(
						jsonBuilder()
							.startObject()
								.field("name", event.getFile().getFileName())
								.field("postDate", new Date())
								.startObject("file")
									.field("_content_type", event.getFile().getContentType())
									.field("_name", event.getFile().getFileName())
									.field("content", Base64
											.encodeBytes(event.getFile().getContents()))
								.endObject()
							.endObject())
					.execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
