/**
 * 
 */
package org.scrutmydocs.webapp.servlet;

import fr.pilato.elasticsearch.river.fs.util.FsRiverUtil;
import org.apache.lucene.util.IOUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesArray;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

		GetResponse responseEs = client.prepareGet()
                .setIndex(index).setId(id)
                .setFields(
                        FsRiverUtil.Doc.ATTACHMENT,
                        FsRiverUtil.Doc.META + "." + FsRiverUtil.Doc.Meta.TITLE)
                .get();
		if(responseEs.isExists()) {
			// Write into stream...
			ServletOutputStream out = resp.getOutputStream();
			try {
				String name = (String) responseEs.getField(FsRiverUtil.Doc.META + "." + FsRiverUtil.Doc.Meta.TITLE).getValue();
				BytesArray content = (BytesArray) responseEs.getField(FsRiverUtil.Doc.ATTACHMENT).getValue();

				resp.setContentType(contentType);
				resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
				out.write(content.toBytes());
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
