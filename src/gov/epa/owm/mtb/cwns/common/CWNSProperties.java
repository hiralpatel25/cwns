package gov.epa.owm.mtb.cwns.common;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * @author Raj Lingam
 *
 * Retrieves the value of a property in resource bundles.
 * 
 */
public class CWNSProperties {
	
	//list of resource bundles
//	protected static String[] resourcebundles = {"gov.epa.owm.mtb.cwns.ApplicationConfig"};
	static String[] resourcebundles = {"ApplicationConfig","mail"};
	
	/**
	 * Retrieves the value of the property
	 * @param key property key
	 * @return String value of property
	 */
	public static String getProperty(String key){
		return getValue(key);
	}	

	/**
	 * Retrieves the value of the property. If the key is not defined the default value is returned.
	 * @param key property key
	 * @param defaultvalue default value to be returned if the value is missing
	 * @return String value of the property
	 */
	public static String getProperty(String key, String defaultvalue){
		String value;
		value= getValue(key);
		if(value==null){
			return defaultvalue;
		}
		return value;		
	}
	
	private static String getValue(String key){
		String value=null;
		
		for (int i=0; i< resourcebundles.length; i++){
			try{
				Locale l = Locale.getDefault();
				ResourceBundle msgRes = ResourceBundle.getBundle(resourcebundles[i],l);
				value = msgRes.getString(key);
				return value;
			}catch(MissingResourceException mre){
				//try another bundle
			}
		}	
		return value;
	}	
	
	/**
	 * Returns the key if existing key. If the key is not defined the defaultkey is returned.
	 * @param key property key
	 * @param defaultkey default key to be returned if the first key is missing
	 * @return String key property
	 */
	public static String getKey(String key, String defaultkey){
        
		Locale l = Locale.getDefault();
		ResourceBundle msgRes = ResourceBundle.getBundle("ApplicationResources",l);
		Enumeration keys = msgRes.getKeys();
		while(keys.hasMoreElements()){
			if(key.equalsIgnoreCase((String)keys.nextElement()))
			  return key;
		}
		return defaultkey;
	}

    /**
     * @param strings
     */
    public static void setResourcebundles(String[] strings) {
        resourcebundles = strings;
    }

}
