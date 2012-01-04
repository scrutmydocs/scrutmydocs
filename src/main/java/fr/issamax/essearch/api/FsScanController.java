package fr.issamax.essearch.api;

import static fr.issamax.dao.elastic.factory.ESSearchProperties.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.dao.elastic.factory.ElasticsearchClientFactoryBean;

@Component
@Path("/scan")
public class FsScanController {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	Client esClient;

	public float nbDocScan = 0;

	@GET
	public Response scanDirectory() throws Exception {
		return scanDirectoryWithEs("D:\\TEST_ES_FS");
	}

	@GET
	@Path("/{path}")
	public Response scanDirectory(@PathParam(value = "path") String path)
			throws Exception {
		return scanDirectoryWithEs(path);
	}

	private Response scanDirectoryWithEs(String path) throws Exception {

		nbDocScan = 0;

		File directory = new File(path);
		long scanDatenew = new Date().getTime();
		long scanDate = getScanDateFsRiver();
		addFilesRecursively(directory, scanDate);

		updateFsRiver(scanDatenew);

		return Response.status(200)
				.entity(nbDocScan + " documents Scan done for path :" + path)
				.build();
	}

	private void addFilesRecursively(File path, long lastScanDate)
			throws Exception {

		final File[] children = path.listFiles();
		Collection<String> fsFiles = new ArrayList<String>();
		Collection<String> fsFolders = new ArrayList<String>();

		if (children != null) {

			for (File child : children) {

				if (child.isFile()) {
					fsFiles.add(child.getName());
					if ((lastScanDate == 0 || child.lastModified() > lastScanDate)
							&& child.isFile()) {
						indexFile(child);
						nbDocScan++;
					}
				} else if (child.isDirectory()) {
					fsFolders.add(child.getName());
					indexDirectory(child);
					addFilesRecursively(child, lastScanDate);
				}
			}
		}

		// TODO Optimize
		// if (path.isDirectory() && path.lastModified() > lastScanDate
		// && lastScanDate != 0) {

		if (path.isDirectory()) {
			Collection<String> esFiles = getFileDirectory(path
					.getAbsolutePath());

			// for the delete files
			for (String esfile : esFiles) {

				if (!fsFiles.contains(esfile)) {
					File file = new File(path.getAbsolutePath()
							.concat(File.separator).concat(esfile));
					esDelete(INDEX_NAME,
							INDEX_TYPE_DOC,
							ElasticsearchClientFactoryBean.sign(file
									.getAbsolutePath()));
					nbDocScan++;
				}
			}

			Collection<String> esFolders = getFolderDirectory(path
					.getAbsolutePath());

			// for the delete folder
			for (String esfolder : esFolders) {

				if (!fsFolders.contains(esfolder)) {

					removeEsDirectoryRecursively(path.getAbsolutePath(),
							esfolder);
				}
			}

			// for the older files
			for (String fsFile : fsFiles) {

				File file = new File(path.getAbsolutePath()
						.concat(File.separator).concat(fsFile));
				if (!esFiles.contains(fsFile)
						&& file.lastModified() < lastScanDate) {
					indexFile(file);
					nbDocScan++;
				}
			}
		}
	}

	public Collection<String> getFileDirectory(String path) throws Exception {
		Collection<String> files = new ArrayList<String>();

		SearchResponse response = esClient
				.prepareSearch(INDEX_NAME)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setTypes(INDEX_TYPE_DOC)
				.setQuery(
						QueryBuilders.termQuery(DOC_FIELD_PATH_ENCODED,
								ElasticsearchClientFactoryBean.sign(path)))
				.setFrom(0).setSize(50000).execute().actionGet();

		if (response.getHits() != null && response.getHits().getHits() != null) {
			for (SearchHit hit : response.getHits().getHits()) {
				String name = hit.getSource().get(DOC_FIELD_NAME).toString();
				files.add(name);
			}
		}

		return files;

	}

	public Collection<String> getFolderDirectory(String path) throws Exception {
		Collection<String> files = new ArrayList<String>();

		SearchResponse response = esClient
				.prepareSearch(INDEX_NAME)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setTypes(INDEX_TYPE_FOLDER)
				.setQuery(
						QueryBuilders.termQuery(DIR_FIELD_PATH_ENCODED,
								ElasticsearchClientFactoryBean.sign(path)))
				.setFrom(0).setSize(50000).execute().actionGet();

		if (response.getHits() != null && response.getHits().getHits() != null) {
			for (SearchHit hit : response.getHits().getHits()) {
				String name = hit.getSource().get(DIR_FIELD_NAME).toString();
				files.add(name);
			}
		}

		return files;

	}

	public void indexFile(File file) throws Exception {

		FileInputStream fileReader = new FileInputStream(file);

		// write it to a byte[] using a buffer since we don't know the exact
		// image size
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int i = 0;
		while (-1 != (i = fileReader.read(buffer))) {
			bos.write(buffer, 0, i);
		}
		byte[] data = bos.toByteArray();

		fileReader.close();
		bos.close();

		esIndex(INDEX_NAME,
				INDEX_TYPE_DOC,
				ElasticsearchClientFactoryBean.sign(file.getAbsolutePath()),
				jsonBuilder()
					.startObject()
						.field(DOC_FIELD_NAME, file.getName())
						.field(DOC_FIELD_DATE, file.lastModified())
						.field(DOC_FIELD_PATH_ENCODED, ElasticsearchClientFactoryBean.sign(file.getParent()))
						.field(DOC_FIELD_PATH, file.getParent())
						.startObject("file")
							.field("_name", file.getName())
							.field("content", Base64.encodeBytes(data))
						.endObject()
					.endObject());
	}

	public void indexDirectory(File file) throws Exception {
		esIndex(INDEX_NAME,
				INDEX_TYPE_FOLDER,
				ElasticsearchClientFactoryBean.sign(file.getAbsolutePath()),
				jsonBuilder()
					.startObject()
						.field(DIR_FIELD_NAME, file.getName())
						.field(DIR_FIELD_PATH, file.getParent())
						.field(DIR_FIELD_PATH_ENCODED, ElasticsearchClientFactoryBean.sign(file.getParent()))
					.endObject());
	}

	public void removeEsDirectoryRecursively(String path, String name)
			throws Exception {

		String fullPath = path.concat(File.separator).concat(name);

		logger.debug("Delete folder " + fullPath);
		Collection<String> listFile = getFileDirectory(fullPath);

		for (String esfile : listFile) {
			esDelete(
					INDEX_NAME,
					INDEX_TYPE_DOC,
					ElasticsearchClientFactoryBean.sign(fullPath.concat(
							File.separator).concat(esfile)));
		}

		Collection<String> listFolder = getFolderDirectory(fullPath);

		for (String esfolder : listFolder) {
			removeEsDirectoryRecursively(fullPath, esfolder);
		}

		esDelete(INDEX_NAME,
				INDEX_TYPE_FOLDER,
				ElasticsearchClientFactoryBean.sign(fullPath));

	}

	public void updateFsRiver(long scanDate) throws IOException,
			InterruptedException, ElasticSearchException, ExecutionException {
		esClient.prepareIndex(INDEX_NAME,
				INDEX_TYPE_FS, "fsScan")
				.setSource(
						jsonBuilder().startObject().field("scanDate", scanDate)
								.endObject()).execute().actionGet();
	}

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

	private void esIndex(String index, String type, String id,
			XContentBuilder xb) {
		logger.debug("Indexing in ES " + index + ", " + type + ", " + id);
		esClient.prepareIndex(index, type, id).setSource(xb).execute()
				.actionGet();
	}

	private void esDelete(String index, String type, String id) {
		logger.debug("Deleting from ES " + index + ", " + type + ", " + id);
		esClient.prepareDelete(index, type, id).execute().actionGet();
	}
}
