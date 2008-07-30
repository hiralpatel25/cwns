package gov.epa.owm.mtb.cwns.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

/**
 * This class is used to obtain the JDBC connection string. It looks in web.xml for
 * the locationId of the hibernate properties file and then uses this file to 
 * determine the connection string.
 * 
 * @author mnconnor
 *
 */
public class JDBCUtility {

	protected String connectionUrl = null;
	protected String username = null;
	protected String password = null;
	protected String datasource = null;

	// Declare the default constructor as private because the class must
	// be initialized using a ServletContex.
	private JDBCUtility() {};
	
	public JDBCUtility(ServletContext servletContext ) {
		getConnectionStringInfo(servletContext);
	}
	
	public String getConnectionUrl() {
		return connectionUrl;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Initialize JDBC Connection String information.
	 * @param servletContext
	 */
	private void getConnectionStringInfo(ServletContext servletContext) {

		Logger log = Logger.getLogger(this.getClass());
		FileInputStream f = null;
        try {
            // Locate the Hibernate properties file
            String propertyFilePath = servletContext.getRealPath("/") + 
            		servletContext.getInitParameter("hibernate.property.file.location");;

            // Load the properties file
            Properties hibernateProperties = new Properties();
            f= new FileInputStream(propertyFilePath);
            hibernateProperties.load(f);
            
            // Set the connection string values
            connectionUrl = hibernateProperties.getProperty("hibernate.connection.url");
            username 	  = hibernateProperties.getProperty("hibernate.connection.username");
            password 	  = hibernateProperties.getProperty("hibernate.connection.password");
            datasource 	  = hibernateProperties.getProperty("cwns.jndiDataSourceName");
            
        } catch (Exception e) {
        	log.error("Unable to obtain the JDBC Connection String",e);
        }finally{
        	try {
				if(f!=null) f.close();
			} catch (IOException e) {
				log.error("Unable to close the property file",e);
			}
        }
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
}

