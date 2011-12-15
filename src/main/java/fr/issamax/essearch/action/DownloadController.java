package fr.issamax.essearch.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.elasticsearch.search.SearchHit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("downloadController")
@Scope("request")
public class DownloadController {

	@Autowired Client esClient;

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
}
