/**
 * 
 */
package org.scrutmydocs.webapp.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Download Document Servlet
 * 
 * @author laborie
 */
public class UploadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6107374244490863440L;

	/** The spring context. */
	private ApplicationContext springContext;

	/** The ES client. */
	private Client client;

	/** The factory. */
	private DiskFileItemFactory factory;

	/** The upload. */
	private ServletFileUpload upload;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Retrieve the SearchService
		this.springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		this.factory = new DiskFileItemFactory();
		this.upload = new ServletFileUpload(this.factory);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Multipart content expected!");
		}
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> files = this.upload.parseRequest(req);

			String fileName;
			String contentType;
			byte[] content;
			for(FileItem item : files) {
				fileName = item.getName();
				contentType = item.getContentType();
				content = item.get();
				this.indexDocument(fileName, contentType, content);
			}
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Index document.
	 * 
	 * @param fileName
	 *        the file name
	 * @param contentType
	 *        the content type
	 * @param content
	 *        the file content
	 * @return the id of indexed document
	 * @throws ElasticSearchException
	 *         the elastic search exception
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 */
	private String indexDocument(String fileName, String contentType, byte[] content) throws ElasticSearchException, IOException {
		Client client = this.getClient();
		IndexResponse response = client.prepareIndex(SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC).setSource(jsonBuilder().startObject().field("name", fileName).field("postDate", Calendar.getInstance().getTime()).startObject("file").field("_content_type", contentType).field("_name", fileName).field("content", Base64.encodeBytes(content)).endObject().endObject()).execute().actionGet();
		return response.getId();
	}

	/**
	 * Gets the client.
	 * 
	 * @return the client
	 */
	private synchronized Client getClient() {
		if(this.client == null) {
			this.client = this.springContext.getBean(Client.class);
		}
		return this.client;
	}
}
