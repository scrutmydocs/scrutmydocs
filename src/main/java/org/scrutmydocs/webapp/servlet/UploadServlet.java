/**
 * 
 */
package org.scrutmydocs.webapp.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.elasticsearch.common.Base64;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.document.DocumentService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

	/** The Document service. */
	private DocumentService documentService;

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
		} catch (RestAPIException e) {
            throw new ServletException(e);
        }
    }

	/**
	 * Index document.
	 */
	private String indexDocument(String fileName, String contentType, byte[] content) throws RestAPIException {
        String base64Content = Base64.encodeBytes(content);
        Document document = new Document(null, SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC, fileName, contentType, base64Content);
		return getDocumentService().push(document).getId();
	}

	/**
	 * Gets the client.
	 */
	private synchronized DocumentService getDocumentService() {
		if(this.documentService == null) {
			this.documentService = this.springContext.getBean(DocumentService.class);
		}
		return this.documentService;
	}
}
