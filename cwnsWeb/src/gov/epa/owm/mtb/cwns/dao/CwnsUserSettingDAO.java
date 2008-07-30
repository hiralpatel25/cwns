package gov.epa.owm.mtb.cwns.dao;

import java.util.Set;

/**
 * 
 * @author mnconnor
 *
 */
public interface CwnsUserSettingDAO extends DAO{

	public static final String SELECTED_LIST_TYPE = "Selected"; 
	public static final String IMPORT_LIST_TYPE   = "import"; 	

	public void updateCwnsUserSetting(String cwnsUserSettingId, Set masterSelectedList);
	public Set getCwnsUserSetting(String userAndRole, String listType);	
}
