package fr.issamax.essearch.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.common.Base64;
import org.elasticsearch.search.SearchHit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("downloadController")
@Scope("request")
public class DownloadController implements Serializable {

	private SearchHit hit = null;
	
	private StreamedContent file;

	@SuppressWarnings("unchecked")
	public void setHit(SearchHit hit) {
		this.hit = hit;
		if (hit != null) {
			try {
				Map<String, Object> file = (Map<String, Object>) hit.getSource().get("file");
				String contentType = file.get("_content_type").toString();
				String name = file.get("_name").toString();
				String source64 = file.get("content").toString();
				InputStream is = new ByteArrayInputStream(Base64.decode(source64));
				this.file = new DefaultStreamedContent(is, contentType, name);
			} catch (Throwable e) {
				// Something goes wrong but we don't care ;-)
			}
		}
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public SearchHit getHit() {
		return hit;
	}
	
	public String download(SearchHit hit) throws IOException {
		if (hit != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> fileMap = (Map<String, Object>) hit.getSource()
					.get("file");
			String contentType = fileMap.get("_content_type").toString();
			String name = fileMap.get("_name").toString();
			String source64 = fileMap.get("content").toString();

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc
					.getExternalContext().getResponse();
			byte[] file = Base64.decode(source64);

			response.setContentType(contentType);
			response.setContentLength(file.length);
			response.setHeader("Content-Disposition=", "attachment" + ";filename=\"" + name + "\"");

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(file, 0, file.length);
			outputStream.flush();

			response.setStatus(200);
			
			facesContext.responseComplete();
		}
		
		return null;
	}

}
