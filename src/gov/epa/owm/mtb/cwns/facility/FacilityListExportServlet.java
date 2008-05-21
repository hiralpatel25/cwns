package gov.epa.owm.mtb.cwns.facility;

import gov.epa.owm.mtb.cwns.common.JDBCUtility;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * This Servlet is responsible for exporting a list of facility numbers.
 * This Servlet runs outside the Portal environment - in other words 
 * it is acessed via a web app.
 * 
 * This Servlet is called from a link on the FacilityList Portlet. It extracts 
 * two fields from that form - Facilities and Selected Facilities. Facilities is the list 
 * of facility numbers that was displayed on the Portlet when 
 * the request was for an export was made. Selected Facilities is the list of 
 * selected facility numbers that was displayed on the Portlet when the request
 * was made for an export.  
 * 
 * The Servlet takes those two lists of facility numbers , along with the 
 * facility numbers identified in the MY_FACILITIES table, and creates 
 * a download file that gets sent to the user. In addition the MY_FACILITIES table is 
 * updated so it contains the complete list of user selected facilites. 
 *  
 * @author mnconnor
 *
 */
public class FacilityListExportServlet extends HttpServlet {
	
	protected Logger log;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log = Logger.getLogger(this.getClass());
		
		String userAndRole 	      = request.getParameter(FacilityListDisplayAction.IMPORT_EXPORT_KEY);

		// The facility IDs that were being displayed on the FacilityList Portlet 
		// when the user requested the export.
		String facilities 		  = request.getParameter("facilities");

		// The facility IDs that were being selected on the FacilityList Portlet 
		// when the user requested the export.
		String selectedFacilities = request.getParameter("selectedFacilities");

		// Make sure we have what we need
		if (userAndRole == null || facilities == null || selectedFacilities == null) {
			throw new ApplicationException("One or more Export parameters are null");
		}else {
			userAndRole = userAndRole.trim();
		}
		
		Connection connection = null;
		try {
			JDBCUtility jdbcu = new JDBCUtility(getServletContext());
			InitialContext ctx = new InitialContext();
			String ctxLookupStr = jdbcu.getDatasource(); 
			DataSource ds = (DataSource) ctx.lookup(ctxLookupStr);
			connection = ds.getConnection();
			// Create the JDBC Connection
			/*Class.forName("oracle.jdbc.driver.OracleDriver");

			
			connection = DriverManager.getConnection(jdbcu.getConnectionUrl(), 
													 jdbcu.getUsername(), 
													 jdbcu.getPassword());
			*/
			// Get facilityIds from the cwnsUserSetting table
			Set masterList = getCwnsUserSetting(connection, userAndRole);
		
			// Update the Master List of Facilities
			masterList = updateMasterList(masterList, facilities, selectedFacilities);

			// Persist the masterList to the database
			saveCwnsUserSetting(connection, userAndRole, masterList);
			
			// Set the HTTP Response header
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment;filename=" + "FacilityList.txt");

			// Write to the output stream
			writeToOutputStream(connection, response.getWriter(),masterList);

			// Close the database connection
			connection.close();

		}  catch (SQLException e) {
			log.error("SQLException Thown: ", e);
			throw new ServletException("Unable to connect to the database");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) connection.close();
			} catch (SQLException e){
				// due nothing
			}
		}
	}
	
	
    protected void writeToOutputStream(Connection connection, PrintWriter out, Set masterList) {
       
    	int totalFacilities = 0;
        String date =  DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        
        out.println("#  USEPA Clean Watersheds Needs Survey--Facilities List");
        out.println(" ");
     	out.println("#  Run Date: "+date);                                                                                                                               
        out.println(" ");
//        out.println("#  Sort Order:");                                                                                                                         
//        out.println("#     Facility Number");
        out.println(" ");
        out.println("#  A/F");
        out.println(" ");


        Iterator iter = masterList.iterator();
        
        String query = "";
		Statement statement = null;
		ResultSet resultSet = null;
		try {

			String previousFacilityNbr = "";
			statement = connection.createStatement();
			
	    	//TODO: A CWNS number can be asssociated with more than one facilityId 
			//      because of local assigned records. Should we filter them? 
		    while (iter.hasNext()) {

				String facilityId = (String) iter.next();
				
				if (facilityId.length() > 0) {
					query = "SELECT f.cwns_nbr, f.name, f.location_id, VF.AUTHORITY,"
							+ "rst.name rst_name "
							+ "FROM facility f, review_status_ref rst, V_FACILITY VF "
							+ "WHERE f.facility_id = "
							+ facilityId
							+ " and "
							+ "f.review_status_id_fk = rst.review_status_id"
							+ " and "
							+ "VF.FACILITY_ID = f.facility_id";
					
					// execute the query
					resultSet = statement.executeQuery(query);
					if(resultSet.next()){
						String facilityNbr = resultSet.getString("cwns_nbr");
						String facilityName = resultSet.getString("name");
						String facilityReviewStatus = resultSet
								.getString("rst_name");
						String facilityLocation = resultSet
								.getString("location_id");
						String facilityAuthority = resultSet.getString("AUTHORITY");

						if (!facilityNbr.equals(previousFacilityNbr)) {

							// Write data to the output stream
							out.println("" + facilityNbr + ",'" + facilityName
									+ "', " + facilityReviewStatus + ", "
									+ facilityLocation + ", "
									+ facilityAuthority);
							totalFacilities++;
						}
						previousFacilityNbr = facilityNbr;						
					}
					
					resultSet.close();
				}			
			}

		} catch (SQLException e) {
			
			log.error("SQLException Thown for query :"+query, e);
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
			} catch (SQLException e){
				// due nothing
			}
		}
		out.println(" ");
        out.println(" ");
        out.println(" ");
        if (totalFacilities > 0) {
        	out.println("#  Total number of facilities: "+totalFacilities);
        } else {
            out.println("#  NO FACILITIES WERE SELECTED FOR EXPORT!!! ");
        }

    }
	/**
	 * Retrieve the "selected" facility IDs from the MY_FACILITIES table.
	 * @param connection
	 * 			Open JDBC connection
	 * @param userAndRole
	 * 			
	 * @return
	 */
 	protected Set getCwnsUserSetting(Connection connection, String userAndRole) {
		
		Set facilityIds = new HashSet();
		//String query  = "SELECT * FROM cwns_user_setting "+
		//				"WHERE user_and_role='"+userAndRole+"' and "+
		//					  "list_type='"+CwnsUserSettingDAO.SELECTED_LIST_TYPE+"'";
		//Statement statement = null;
		
		String query ="SELECT * FROM cwns_user_setting WHERE user_and_role=? and list_type=?";
		
		
		PreparedStatement userSetting=null;
		ResultSet resultSet = null;
		try {
			userSetting = connection.prepareStatement(query);
			userSetting.setString(1, userAndRole);
			userSetting.setString(2, CwnsUserSettingDAO.SELECTED_LIST_TYPE);
			
		    //statement = connection.createStatement();			
			// execute the query
	         //resultSet = statement.executeQuery(query);
	        resultSet = userSetting.executeQuery();

	        // Retrieve the facilityIds
	        while (resultSet.next()) {
	        	int facId = resultSet.getInt("FACILITY_ID");
	        	facilityIds.add(String.valueOf(facId));
	        }

		} catch (SQLException e) {
			log.error("SQLException Thown for query :"+query,e);
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (userSetting != null) userSetting.close();
			} catch (SQLException e){
				// due nothing
			}
		}

		return facilityIds;
	}

	/**
	 * Update the MY_FACILITIES table
	 * @param connection
	 * @param cwnsUserSettingId
	 * @param facilities
	 */
	protected void saveCwnsUserSetting(Connection connection, String userAndRole, Set masterList) {
		
		//String query  = "DELETE cwns_user_setting "+
		//				"WHERE user_and_role='"+userAndRole+"' and "+
		//					   "list_type='"+CwnsUserSettingDAO.SELECTED_LIST_TYPE+"'";
		
		String query  = "";
		PreparedStatement deleteUserSetting=null;
		PreparedStatement insertUserSetting=null;
		//Statement statement = null;
		try {
		    //statement = connection.createStatement();
			query  = "DELETE cwns_user_setting WHERE user_and_role=? and list_type=? ";
			deleteUserSetting = connection.prepareStatement(query);
			deleteUserSetting.setString(1, userAndRole);
			deleteUserSetting.setString(2, CwnsUserSettingDAO.SELECTED_LIST_TYPE);			
		        
			// Remove existing records
			deleteUserSetting.executeUpdate();
	        
	        // Add new records
	        Iterator iter = masterList.iterator();
	        while (iter.hasNext()) {

				// Get the facilityId
				String facilityId = (String) iter.next();
				if (!"".equals(facilityId)) {
					
					// Get a sequence
					query="SELECT cwns_user_setting_seq.nextval FROM DUAL";
					PreparedStatement selectSequence = connection.prepareStatement(query);
					ResultSet resultSet = selectSequence.executeQuery();
					resultSet.next();

					int cwnsUserSettingId = resultSet.getInt(1);

					// Write a record 
					//query = "INSERT INTO cwns_user_setting ( cwns_user_setting_id, user_and_role, facility_id_fk, list_type ) "+
					//		"VALUES ("+ cwnsUserSettingId + " ,'"+ userAndRole+ "','" + 
					//		            facilityId + "','"+ CwnsUserSettingDAO.SELECTED_LIST_TYPE+"')";
					
					query = "INSERT INTO cwns_user_setting ( cwns_user_setting_id, user_and_role, facility_id, list_type, last_update_userid, last_update_ts) VALUES (?,? ,? ,?,?,?)";
					insertUserSetting = connection.prepareStatement(query);
					insertUserSetting.setInt(1, cwnsUserSettingId);
					insertUserSetting.setString(2, userAndRole);
					insertUserSetting.setString(3, facilityId);
					insertUserSetting.setString(4, CwnsUserSettingDAO.SELECTED_LIST_TYPE);
					insertUserSetting.setString(5, userAndRole);
					Date date = new Date();					
					insertUserSetting.setDate(6, new java.sql.Date(date.getTime()));					
					insertUserSetting.executeUpdate();
					if (resultSet!=null)resultSet.close();
					if (selectSequence != null) selectSequence.close();
					if (insertUserSetting != null) insertUserSetting.close();
				}
			}

		} catch (SQLException e) {
			log.error("SQLException Thown for query :"+ query, e);
		} finally {
			try {
				if (deleteUserSetting != null) deleteUserSetting.close();
			} catch (SQLException e){
				// due nothing
			}
			try {
				if (insertUserSetting != null) insertUserSetting.close();
			} catch (SQLException e){
				// due nothing
			}
		}

	}
	
	/**
	 * 
	 * @param masterList
	 * @param facilities
	 * @param selectedFacilities
	 * @return
	 */
	protected Set updateMasterList(Set masterList, String facilities, String selectedFacilities) {
		
		// Create an array of facilityIds 
		String[] facilitiesArray = facilities.split(" ");
		
		// Create an array of selected facilityIds 
		String[] selectedFacilitiesArray = selectedFacilities.split(" ");

		// Remove facilities displayed on the Portlet
		masterList.removeAll(Arrays.asList(facilitiesArray));
		
		// Add selected facilities displayed on the Portlet
		masterList.addAll(Arrays.asList(selectedFacilitiesArray));

		return masterList;
	}
	
}
