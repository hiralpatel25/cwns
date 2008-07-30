package gov.epa.owm.mtb.cwns.needs;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.epa.owm.mtb.cwns.needs.upload.MonitoredDiskFileItemFactory;
import gov.epa.owm.mtb.cwns.needs.upload.UploadListener;
import gov.epa.owm.mtb.cwns.service.NeedsService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class NeedsDocumentUploadServlet extends HttpServlet {

	public static final long serialVersionUID = 1;

	private ApplicationContext ac;

	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		ServletContext sc = Config.getServletContext();

		ac = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String url = "/jsp/needs/DocumentUploadResult.jsp";

		UploadListener listener = new UploadListener(request, 1);

		// Create a factory for disk-based file items
		FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		boolean result = false;

		try {
			// process uploads ..
			List list = upload.parseRequest(request);

			String docid = "";

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {

				FileItem fi = (FileItem) iterator.next();
				if ("rowID".equals(fi.getFieldName()))
					docid = fi.getString();

				if ("vurl".equals(fi.getFieldName()))
					request.setAttribute("vurl", fi.getString());

			}

			iterator = list.iterator();
			while (iterator.hasNext()) {

				FileItem fi = (FileItem) iterator.next();

				if ("file1".equals(fi.getFieldName())) {

					if (fi instanceof gov.epa.owm.mtb.cwns.needs.upload.MonitoredDiskFileItem) {

						DiskFileItem file = (DiskFileItem) fi;

						if (file.getSize() > 0) {							
							File fullFile  = new File(file.getName());
							NeedsService needsservice = (NeedsService) ac.getBean("needsService");
							result = needsservice.uploadDocument(docid, fullFile.getName(), file.get());
							
							if(!result)
								request.setAttribute("errormsg", "Document failed to upload.  Please try again or contact the CWNS helpdesk.");
						}
						else
							request.setAttribute("errormsg", "Uploaded file contained no data.  Please use the browse button."); 
					}
				}

			}

		} catch (FileUploadException e) {
			e.printStackTrace(); // To change body of catch statement use
									// File | Settings | File Templates.
		}
		
		request.setAttribute("result", new Boolean(result));

		gotoPage(url, request, response);

	}

	private void gotoPage(String forwardPage, HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {

		getServletConfig().getServletContext().getRequestDispatcher(
				"/" + forwardPage).forward(req, res);

	}
}
