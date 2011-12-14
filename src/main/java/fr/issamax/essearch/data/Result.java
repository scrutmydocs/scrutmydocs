package fr.issamax.essearch.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.elasticsearch.common.Base64;
import org.elasticsearch.search.SearchHit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class Result {
	protected String title;
	protected Collection<String> fragments=new ArrayList<String>();
	protected SearchHit searchHit;
    private StreamedContent file;  
	
	public Result(SearchHit searchHit) {
		this.searchHit = searchHit;
		try {
			Map<String, Object> file =  (Map<String, Object>) searchHit.getSource().get("file");
			String contentType = file.get("_content_type").toString();
			String name = file.get("_name").toString();
			String source64 = file.get("content").toString();
			InputStream is = new ByteArrayInputStream(Base64.decode(source64));
			this.file = new DefaultStreamedContent(is, contentType, name); 
		} catch (Throwable e) {
			// Something goes wrong but we don't care ;-)
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<String> getFragments() {
		return fragments;
	}

	public void setFragments(Collection<String> fragments) {
		this.fragments = fragments;
	}

	public SearchHit getSearchHit() {
		return searchHit;
	}
	
	public void setSearchHit(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
}
