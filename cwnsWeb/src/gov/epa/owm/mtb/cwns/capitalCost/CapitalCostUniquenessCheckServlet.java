package gov.epa.owm.mtb.cwns.capitalCost;

import gov.epa.owm.mtb.cwns.service.CapitalCostService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CapitalCostUniquenessCheckServlet extends HttpServlet {

	public static final long serialVersionUID = 1;

	private ApplicationContext ac;

	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		ServletContext sc = Config.getServletContext();

		ac = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
           doGet(request, response);
    }	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pr = response.getWriter();
		
		String combinedId = request.getParameter("combinedId");
		
		System.out.println(combinedId);

		CapitalCostService capitalCostService = (CapitalCostService) ac.getBean("capitalCostService");
		
		if(capitalCostService.checkUniqueCombination(combinedId))
			pr.print("Y");
		else
			pr.print("N");
	
	}	

}
