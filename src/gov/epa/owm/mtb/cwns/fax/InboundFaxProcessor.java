package gov.epa.owm.mtb.cwns.fax;

import gov.epa.owm.mtb.cwns.service.NeedsService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.efaxdeveloper.util.inbound.Barcode;
import com.efaxdeveloper.util.inbound.InboundClient;

public class InboundFaxProcessor extends HttpServlet {
	protected Logger log;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8743808379714677684L;
	private ApplicationContext ac;
	
	/**
	 * Constructor of the object.
	 */
	public InboundFaxProcessor() {
		super();
		log = Logger.getLogger(this.getClass());
	}

	/**
	 * Initialization of the servlet. <br>
	 */
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		ServletContext sc = Config.getServletContext();
		ac = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		boolean error = false;
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter();
		String cwnsNbr =null;
		
		String xml = request.getParameter("xml");
		log.debug(xml);
		//xml=getFile("c:\\fax1.xml");
		if(xml!=null){
			
		    InboundClient ic = null;
		    try{
		    	  ic = new InboundClient(xml);
			      // When barcode fields have been passed (optional setting)
			      if (ic.hasBarcodes()) {
			        // Begin processing barcode fields
			        // Establish a new Vector object to hold all Barcode objects. Use
			        // the getBarcodesByPage(int) method to return all Barcode objects
			        // from a specified page.
			        Vector v = new Vector(ic.getBarcodes());
			        // Establish an iterator Object
			        Iterator iterator = v.iterator();
			        // Process each Barcode object
			        while (iterator.hasNext()) {
			          Barcode b = (Barcode)iterator.next();
			          cwnsNbr = b.getKey();
			          break;
			        }
			      }
			      if(cwnsNbr!=null && !"".equals(cwnsNbr)){
				      //get the file byte array
					  byte[] fileByteArray = ic.getDocumentAsBytes();
					  
					  /* Create a document and upload */
					  NeedsService needsservice = (NeedsService) ac.getBean("needsService");
					  String faxUser = (ic.getFaxName()!=null && "".equals(ic.getFaxName()))?ic.getFaxName():"fax";
					  boolean success = needsservice.uploadFaxDocument(cwnsNbr, fileByteArray, faxUser, ic.getDateReceivedAsString());
					  if(!success)error = true;
			      }else{
			    	  error = true;
			      }
		    }catch (Exception e) {
			      log.error("An exception has occured while processing the fax request" + e.getMessage());
			      error = true;
			}		      
		}else{
			log.error("Fax inbound process called with xml");
			error=true;
		}
		
		//send the output
	    out.println("<html><head><title>InboundPostServlet</title></head><body>");
		if(!error){
		      out.println("<p>Post Successful</p>");
		}else{
		      out.println("<p>An error was encountered!</p>");
		}	
	    out.println("</body></html>");
	}
	
	private static String getFile(String fileName) throws IOException{
	    File file = new File( fileName ); // create a File object
        FileInputStream fileinputstream = new FileInputStream(file);

        int numberBytes = fileinputstream.available();
        byte bytearray[] = new byte[numberBytes];

        fileinputstream.read(bytearray);
        String xml = new String(bytearray);
        fileinputstream.close();
        return xml;		
	}
	
}
