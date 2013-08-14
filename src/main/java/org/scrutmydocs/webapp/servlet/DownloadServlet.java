/**
 * 
 */
package org.scrutmydocs.webapp.servlet;

import org.apache.lucene.util.IOUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Download Document Servlet
 * 
 * @author laborie
 */
public class DownloadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3093542409787735469L;

	/** The spring context. */
	private ApplicationContext springContext;

	/** The ES client. */
	private Client client;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Retrieve the SearchService
		this.springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String index = req.getParameter("index");
        String contentType = req.getParameter("content_type");
		Client client = this.getClient();

		GetResponse responseEs = client.prepareGet().setIndex(index).setId(id).execute().actionGet();
		if(responseEs.isExists()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> attachment = (Map<String, Object>)responseEs.getSource().get("file");

			// Write into stream...
			ServletOutputStream out = resp.getOutputStream();
			try {
				String name = (String)attachment.get("_name");
				String content = (String)attachment.get("content");

				resp.setContentType(contentType);
				resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
				out.write(Base64.decode(content));
			} finally {
                IOUtils.closeWhileHandlingException(out);
			}
		} else {
			resp.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
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
