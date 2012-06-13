package fr.issamax.essearch.action;

import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_TYPE_DOC;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("fileUploadController")
@Scope("request")
public class FileUploadController implements Serializable {

	private static final long serialVersionUID = 1610009986295677939L;


	@Autowired
	Client esClient;
	
	
	String idDoc;

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");

		try {
			esClient.prepareIndex(INDEX_NAME, INDEX_TYPE_DOC)
					.setSource(
							jsonBuilder()
									.startObject()
									.field("name",
											event.getFile().getFileName())
									.field("postDate", new Date())
									.startObject("file")
									.field("_content_type",
											event.getFile().getContentType())
									.field("_name",
											event.getFile().getFileName())
									.field("content",
											Base64.encodeBytes(event.getFile()
													.getContents()))
									.endObject().endObject()).execute()
					.actionGet();
		} catch (Exception e) {
			msg = new FacesMessage("Error", "Something went wrong with "
					+ event.getFile().getFileName() + ". " + e.getMessage());
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void exportFile() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();

		GetResponse esResponse = esClient
				.prepareGet(INDEX_NAME, INDEX_TYPE_DOC, this.idDoc).execute()
				.actionGet();
		// if(!esResponse.isExists() ) {
		// //TODO return a standard page who show a message like : this document
		// is not available
		// return null;
		// }

		@SuppressWarnings("unchecked")
		Map<String, Object> attachment = (Map<String, Object>) esResponse
				.getSource().get("file");

		String contentType = (String) attachment.get("_content_type");

		byte[] file;
		try {
			file = Base64.decode((String) attachment.get("content"));

			response.setContentType(contentType);
			// response.setHeader("Content-disposition",
			// "inline=filename=file.pdf");

			response.getOutputStream().write(file);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setIdDoc(String idDoc) {
		this.idDoc = idDoc;
	}
	
	public String getIdDoc() {
		return idDoc;
	}
}
