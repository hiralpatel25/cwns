package gov.epa.owm.mtb.cwns.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class AjaxRequestProcessor extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AjaxRequestProcessor() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
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
		doGet(request, response);
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

		//setup response
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");		
		
		String urlStr 	 = request.getParameter("url");		
		if(urlStr==null || "".equals(urlStr)){
			out.println("<error>url not found</error>");
		}else{
			//construct the url and get the response
			URL url = new URL(urlStr); 
			URLConnection c = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                c.getInputStream()));
	        StringBuffer sb = new StringBuffer();
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) 
	            sb.append(inputLine+"\n");
	        in.close();
			out.println(sb.toString());
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}