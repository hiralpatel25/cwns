package gov.epa.owm.mtb.cwns.common;

import org.apache.struts.action.ActionServlet;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This Class Extends the Struts Action Servlet and is used to initialize Log4j. 
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class CWNSActionServlet extends ActionServlet {

    public void init() throws ServletException {
        super.init();

        String propFileName = "";
        String propertyFileLocation = "";
        ServletContext servletContext = getServletContext();
        String realPath = servletContext.getRealPath("/");
        FileInputStream f =null;
        try {            
            /* Configure Log4j */
            propertyFileLocation = servletContext.getInitParameter(
                    "log4j.property.file.location");
            propFileName = realPath + propertyFileLocation;
            Properties log4jProperties = new Properties();
            f = new FileInputStream(propFileName);
            log4jProperties.load(f);
            PropertyConfigurator.configure(log4jProperties);
        } catch (Exception e) {
            throw new ServletException(
                "Unable to configure the Log4j Property Configurator");
        }finally{
        	try {
				if(f!=null)f.close();
			} catch (IOException e) {
				System.out.println("unable to close the lo4j properties file");
			}
        }

    }
}
