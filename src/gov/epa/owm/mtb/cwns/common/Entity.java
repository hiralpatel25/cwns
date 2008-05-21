package gov.epa.owm.mtb.cwns.common;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class Entity {

private String key; 

private String value;

public Entity(String key, String value){

	this.key = key;
	this.value = value;
}

public String getKey() {
	return this.key;
}

public void setKey(String key) {
	this.key = key;
}

public String getValue() {
	return this.value;
}

public void setValue(String value) {
	this.value = value;
}

}
