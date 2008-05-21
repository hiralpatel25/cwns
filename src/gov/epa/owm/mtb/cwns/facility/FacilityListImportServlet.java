package gov.epa.owm.mtb.cwns.facility;

import gov.epa.owm.mtb.cwns.common.JDBCUtility;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;

import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This Servlet is responsible for importing a list of facility numbers that 
 * will be displayed in the FacilityList Portlet. This Servlet runs outside 
 * of the Portal environment - in other words it is acessed via a web app.
 * 
 * The Servlet takes an HTTP multipart request form and processes it. It expects 
 * the form to have two fields - an uploaded file and a url to forward the request to.
 * Processing of the file consists of extracting a list of facility numbers and 
 * storing them into the MY_FACILITIES table. Once the file is processed the request is 
 * forwarded on to the FacilityList Portlet so it's list of facilities to display 
 * will be refreshed.
 *  
 * @author mnconnor
 *
 */
public class FacilityListImportServlet extends HttpServlet 
{
	
	protected Logger log;
	private ApplicationContext ac;
	/**
	 * Initialization of the servlet. <br>
	 */
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		ServletContext sc = Config.getServletContext();
		ac = WebApplicationContextUtils.getWebApplicationContext(sc);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log = Logger.getLogger(this.getClass());

		String forwardUrl = "";
		String userAndRole = "";
		FileItem importFile = null;

		Set facilityNbrs =  new HashSet();
		try {		
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());		
		
	 		// Parse the multipart request
			List items = upload.parseRequest(request);
			
			// Extract the fields from the form
			Iterator iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					if (FacilityListDisplayAction.IMPORT_EXPORT_KEY.equals(item.getFieldName())) {
						// Identifies records in the table for this user
						userAndRole = item.getString();
					}else { 
						// forward url
						forwardUrl = item.getString();
					}
				} else {
					importFile = item;
				}
			}
			facilityNbrs = processImportFile(importFile);

		}
		catch (FileUploadException fue) {
			log.error("FileUploadException thrown : ", fue);
			throw new ApplicationException (fue);
		}
		
		if (facilityNbrs.size() > 0) {
			FacilityService facilityservice = (FacilityService) ac.getBean("facilityService");
			// Update the MY_FACILITIES table
			facilityservice.updateImportFacilitiesInDatabase(facilityNbrs,userAndRole);

			// If an "action" parameter exists on the request URL remove it 
			int idx1 = forwardUrl.indexOf("action");
			if (idx1 > -1 ) { 
				int idx2 = forwardUrl.indexOf("&", idx1);
				String pre = forwardUrl.substring(0, idx1 -1);
				if (idx2 > -1) {
					String post = forwardUrl.substring(idx2, forwardUrl.length());
					forwardUrl = pre+post; 
				} else {
					forwardUrl = pre; 
				}
			}

		}

		forwardUrl += "&action="+FacilityListDisplayAction.ACTION_IMPORT; 

		// Forward the HTTP Request to the FacilityList Portlet
		response.sendRedirect(forwardUrl);
	}
	
/**
 * Parse the uploaded file and extract a Set of  
 * facility numbers (CWNS Numbers). 
 * @param item
 * 			A handle to the uploaded file that needs to be processed
 * @return
 * 			A Set of facility numbers 
 */	
	protected Set processImportFile(FileItem item) {
		Set facilityNbrs = new HashSet();

		// Split the import file into lines of text
		String fileStr = new String(item.get());
		String []lines = fileStr.split("\n");

		String facilityNbr = "";
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.length()> 10) { 	// if < 10 it can't be a facilityNbr
				String eof  = line.substring(0,3).toUpperCase(); 
				if (!line.startsWith("#") && !eof.equals("EOF")) {
					String []fields = line.split(",");
					facilityNbr = fields[0];
					facilityNbrs.add(facilityNbr);
				}
			}
		}
		return facilityNbrs;
		
	}

}
