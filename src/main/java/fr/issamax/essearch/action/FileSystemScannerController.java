package fr.issamax.essearch.action;

import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_NAME;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_TYPE_FS;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.issamax.essearch.data.FileSystemScanner;

@Component("fileSystemScannerController")
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class FileSystemScannerController {

	@Autowired Client esClient;

	private Collection<FileSystemScanner> scanners;
	
	public Collection<FileSystemScanner> getScanners() {
		return scanners;
	}
	
	public void setScanners(Collection<FileSystemScanner> scanners) {
		this.scanners = scanners;
	}
	
	// TODO Migrate in a DAO
	private Collection<FileSystemScanner> getFSScanners() {
		Collection<FileSystemScanner> fsScanners = new ArrayList<FileSystemScanner>();
		
		GetResponse response = esClient
				.prepareGet(INDEX_NAME,
						INDEX_TYPE_FS, "fsScan")
				.setOperationThreaded(false).execute().actionGet();

		Map<String, Object> source = response.getSource();
		if (source != null) {
			boolean array = XContentMapValues.isArray(source.get("folders"));
			if (array) {
				ArrayList<Map<String, Object>> folders = (ArrayList<Map<String, Object>>) source.get("folders");
				fsScanners = new ArrayList<FileSystemScanner>(folders.size());
				for (Map<String, Object> folder : folders) {
					String url = XContentMapValues.nodeStringValue(folder.get("url"), null);
					fsScanners.add(new FileSystemScanner(url));
				}
			}
		}
		
		return fsScanners;
	}

	// TODO Migrate in a DAO
	private long getScanDateFsRiver() {
		GetResponse response = esClient
				.prepareGet(INDEX_NAME,
						INDEX_TYPE_FS, "fsScan")
				.setOperationThreaded(false).execute().actionGet();

		if (response.getSource() != null) {
			Long scanDate = (Long) response.getSource().get("scanDate");
			return scanDate.longValue();
		} else
			return 0;
	}

	/**
	 * Save scanners in ES database
	 * @throws IOException 
	 * @todo Move to a DAO
	 */
	private void saveScanners(Collection<FileSystemScanner> scanners) throws Exception {
		XContentBuilder xcb = jsonBuilder()
				.startObject()
				.field("scanDate", getScanDateFsRiver()).endObject()
				.startArray("folders");
		
		for (FileSystemScanner scanner : scanners) {
			xcb = xcb.startObject()
					.field("url", scanner.getUrl())
				.endObject();
		}
		xcb = xcb.endArray().endObject();
		
		esClient
				.prepareIndex(INDEX_NAME, INDEX_TYPE_FS)
				.setSource(xcb)
				.execute().actionGet();
	}
	
	/**
	 * Save scanners in ES database
	 */
	public void saveScanners() {
		FacesMessage msg = null;
		
		try {
			saveScanners(this.scanners);
			msg = new FacesMessage("Succesful", "Scanners updated...");
		} catch (Exception e) {
			msg = new FacesMessage("Error", "Something went wrong while saving scanners... " + e.getMessage());
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	/**
	 * Load ES scanners definitions from ES database
	 */
	public void loadScanners() {
		scanners = getFSScanners();
	}
}
