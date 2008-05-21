<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.utils.NameValue"%>

<%@ page import="gov.epa.owm.mtb.cwns.common.Entity"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsHelper"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsRoleHelper"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.LocationRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.Facility"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.AccessLevelRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.CwnsUser"%>
<%@ page import="gov.epa.owm.mtb.cwns.service.UserService" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.CurrentUser"  %>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils" %>


<%
	PortletRenderRequest prr = (PortletRenderRequest) request
								.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	
	NameValue[] linkParams = new NameValue[1];
	linkParams[0] = new NameValue("formId", "userDetailsForm");
	
	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
	String listboxurl = url +"/javascript/listbox.js";
	Collection locationRefs = (Collection)request.getAttribute("locationRefs");
	UserDetailsForm udForm = (UserDetailsForm)request.getAttribute("userDetailsForm");
    HttpSession httpSess = request.getSession(); 
	CurrentUser adminUser = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER); 

	Collection facilitiesForRole = (Collection)request.getAttribute("facilitiesForRole");
	if (facilitiesForRole == null) facilitiesForRole = new ArrayList();
	
	boolean userDenied = false;
	if ("I".equals(udForm.getStatus()) &&
  	    (udForm.getOidUserId() == null || udForm.getOidUserId().length() == 0)) {
		
		userDenied = true; // The user was denied access to CWNS before receiving a a Portal account
	}
	String helpKey = (String)request.getAttribute("helpKey");
%>


<script type="text/javascript" language="JavaScript" src="<%=listboxurl %>"></script>
<SCRIPT type=text/javascript>

function removeAllOptionsFromSelectBox(selobjid){
	
	/*selectBox=window.document.getElementById(selobjid);
	window.alert("delete all options " + selectBox.options.length);
	for(I = selectBox.options.length - 1; I >= 0;  I--){
		selectBox.remove(I);
	}*/
}

function enableSelFacs(selFacs)
{
	if(selFacs=="Y")
	{
		window.document.getElementById("facilitiesSelBlock").style.display="block";
	}
	else
	{
		window.document.getElementById("facilitiesSelBlock").style.display="none";
	}
}



function deleteFacilities(selobjid)
{
	var SelectFrom=window.document.getElementById(selobjid);
	deleteFromList(SelectFrom.options);
}

function showFacilitySelectWindow(popupUrl,locationId)
{
	if(locationId == ""){
		selectedLocation = window.document.getElementById("selectLocation");
		selectedLocationValue = selectedLocation.value;
		if(selectedLocationValue == ""){
			window.alert("Location is required");
			selectedLocation.focus()
			locationId = selectedLocationValue;
			return;
		}else{
		 	//locationId = "AL";
		 	locationId = selectedLocationValue;
		}
	}
	
	//if(locationId != ""){
	//alert(locationId);
    var w = 600;
    var h = 600;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    var url = popupUrl+'&_event_locationId='+ locationId;
    //alert(url);
    var w = window.open(url, null, settings);
    if(w != null) 
    	w.focus();     
   //}
}


   function appear(){
	 window.document.getElementById("action").value = "<%=UserDetailsAction.ACTION_ADD_ROLE%>";
     document.getElementById('displayRoleInfo').style.display='block';
     document.getElementById('displayRoleLink').style.display='none';
   }
    
   function disappear(){
    	document.getElementById('displayRoleInfo').style.display='none'; 
    	document.getElementById('displayRoleLink').style.display='block'; 
   }

function setRoleInfoAndSubmit(roleInfoId,action) {
	window.document.getElementById("selectedRoleInfoId").value = roleInfoId;
	setUserDetailsActionAndSubmit(action);
}

function deleteFacility(locId,facId) {
	window.document.getElementById("currentLocationId").value = locId;
	window.document.getElementById("selectedFacility").value = facId;
	setUserDetailsActionAndSubmit('<%=UserDetailsAction.ACTION_DELETE_FACILITY%>');
}

function validateRoleAndSubmit(form, action) {

	// verify LocationType data
	var selType=form['selectType'];
	if ( selType != undefined) {
		var indx = selType.selectedIndex;
		var typeId = selType.options[indx].value;
		if (typeId == "") {
			alert('You must select a Type');
			return;
		}
	}
	
	// verify Location data
	var selLocation=form['selectLocation'];
	if ( selLocation != undefined )  {
		var indx = selLocation.selectedIndex;
		var typeId = selLocation.options[indx].value;
		if (typeId == "") {
			alert('You must select a Location');
			return;
		}
	}
	
	// verify Access level data
	var selAccessLevel=form['selectAccessLevel'];
	// var indx = selAccessLevel.selectedIndex;
	// var text = selAccessLevel.options[0].text;
	
	
	//form string for facilities
	
	var af = document.getElementById('facilitiesSelBlock').style.display;
	//alert(af);
	if(af=='block')
	{
		var resultStr;
		var selectedList = window.document.getElementById('facilityIds');
	
		if(selectedList.options.length > 0)
		{
	    	resultStr = selectedList.options[0].value;
	    
			for (i=1;i<selectedList.options.length;i++) 
			{
				resultStr = resultStr + ',' + selectedList.options[i].value;
			}
   			window.document.getElementById("strFacilityIds").value = resultStr;
		}
		else
		{
			alert("Please select at least one facility or check 'All Facilities in the state'");
			return;
		}
	}
	
	if (validateUserData(form)) {
		if (adminUserId == form.currentUserId.value) {
			alert('Your changes will not completely take effect until you logout and log back in.');
		}
	
		// submit the form
    	setUserDetailsActionAndSubmit(action);
    }
}

function validateUserDataAndSubmit (action) {
	form =window.document.userDetailsForm;
	if (validateUserData(form)) {
		
		if (adminUserId == form.currentUserId.value &&
		    action != '<%=UserDetailsAction.ACTION_NEW_ROLE%>') {
			alert('Your changes will take effect on your next login.');
		}

		// submit the form
    	setUserDetailsActionAndSubmit(action);
    }
}

function validateUserData(form) {
	// validate user status data
	<%if (!"P".equals(udForm.getStatus()) && !"I".equals(udForm.getStatus()) &&  !UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())) { %>
		var select=form['selectStatus'];
		var indx = select.selectedIndex;
		var statusId = select.options[indx].value;
		if (statusId == "") {
			alert('You must select a valid Status');
			return false;
		}
	<%}%>
		
	return true;
}


// Set the value of the "action" hidden attribute and submit the form
function setUserDetailsActionAndSubmit(action){
	 window.document.getElementById("act").value = action;
	 window.document.userDetailsForm.submit();
}

// Open help window
function showUserDetailsHelp(popupUrl)
{
    var w = 800;
    var h = 500;   
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

  var w = window.open(popupUrl, 'UserDetailsHelp', settings);
  if(w != null) 
    w.focus();     
}

// called when the user changes the Location Type value
function changeLocationType(form)
{
	var selType=form['selectType'];
	var indx = selType.selectedIndex;
	var typeId = selType.options[indx].value;

	if ( typeId == '<%=UserService.LOCATION_TYPE_ID_FEDERAL%>' ) {
		changeAccesslevelAndLocation(form, fedAccessLevels, fedLocations);
		showFacilityArea('no');
	} else if (typeId == '<%=UserService.LOCATION_TYPE_ID_REGIONAL%>' ) {
		changeAccesslevelAndLocation(form, regAccessLevels, regLocations);
		showFacilityArea('no');
	} else if (typeId == '<%=UserService.LOCATION_TYPE_ID_STATE%>' ) {
		changeAccesslevelAndLocation(form, stateAccessLevels, stateLocations);
		showFacilityArea('yes');
	} else if (typeId == '<%=UserService.LOCATION_TYPE_ID_LOCAL%>'	) {
		changeAccesslevelAndLocation(form, localAccessLevels, localLocations);
		showLocalFacilityArea('yes');
	} else { // no selection

		// access levels
		/*
   		var selAccessLevel=form['selectAccessLevel'];
		var current = selAccessLevel.options.length; 
		for (var j=current-1;j>=0;j--) selAccessLevel.options[j] = null;		
		selAccessLevel.options[0] = new Option('Please Select a Type' ,''); 		
		*/
		
		cleanTable("altbl");
		var title = new Array("Please select a Type");
		insertHeaders("altbl",title);
		
		// location
   		var selLocation=form['selectLocation'];
		current = selLocation.options.length; 
		for (var j=current-1;j>=0;j--) selLocation.options[j] = null;	
		selLocation.options[0] = new Option('Please Select a Type',''); 
	}

}

// Show (or hide) the area of the screen that allows a user to select 
// All Facilities or Selected Facilities
function showFacilityArea(yesOrNo)   {

	if (yesOrNo == 'yes') {
	   	document.getElementById('facilityArea').style.display='block';
	} else {
	   	document.getElementById('facilityArea').style.display='none';
	}
   	document.getElementById('allFacilitiesId').checked='Y';
   	enableSelFacs('N');
}

function showLocalFacilityArea(yesOrNo)   {
	if (yesOrNo == 'yes') {
	   	document.getElementById('facilityArea').style.display='block';
	} else {
	   	document.getElementById('facilityArea').style.display='none';
	}
	document.getElementById('allFacilitiesId2').checked='Y';
   	enableSelFacs('Y');
}

function insertHeaders(tblname,title)
{
	var Row,Cell;
    Row = document.getElementById(tblname).insertRow(-1);
    var titles = title.length;
    
    for(var i = 0; i < titles;i++)
    {
    	Cell = Row.insertCell(i);  
    	Cell.innerHTML=title[i]
    }
     
}

function insertRowToTable(tblname,checkboxvalue,text)
{
 	var Row,Cell;
    Row = document.getElementById(tblname).insertRow(-1);
    Cell = Row.insertCell(0); 
    var cellcheckbox;
    if(text=="View")
    {
    	cellcheckbox = '<input type="checkbox" disabled checked/>';
    }
    else
    {    	
    	cellcheckbox = '<pdk-html:checkbox name="userDetailsForm" property="accessLevelIds" value="'+checkboxvalue+'"></pdk-html:checkbox>';
    	
    	// cellcheckbox = '<pdk-html:multibox name="userDetailsForm" property="accessLevelIds" value="1"></pdk-html:multibox>';
    	//alert(cellcheckbox);
    }
    Cell.innerHTML=cellcheckbox;
    Cell = Row.insertCell(1); 
    Cell.innerHTML=text; 
}

function cleanTable(tblname)
{
	var table = document.getElementById(tblname);
	var rows = table.rows;
	while(rows.length) // length=0 -> stop 
        table.deleteRow(rows.length-1); 
}


// set the values in the Access Level and Location list boxes
function changeAccesslevelAndLocation(form, arrAccessLevel, arrLocation) {
			
		cleanTable("altbl");
		// var titles = new Array("Select","access levels");
		// insertHeaders("altbl",titles);
		for (var i=0;i< arrAccessLevel.length;i++) {
			insertRowToTable("altbl",arrAccessLevel[i][0],arrAccessLevel[i][1]);
		}
		
		// location
   		var selLocation=form['selectLocation'];
   		if (selLocation != undefined )  {
			current = selLocation.options.length; 
			for (var j=current-1;j>=0;j--) selLocation.options[j] = null;
			var i = 0;
			if (arrLocation.length > 1) {
				selLocation.options[i] = new Option("",''); 
				for (var i=0;i< arrLocation.length;i++) {
					selLocation.options[i+1] = new Option(arrLocation[i][1],arrLocation[i][0]); 		
				}
			} else {
				for (var i=0;i< arrLocation.length;i++) {
					selLocation.options[i] = new Option(arrLocation[i][1],arrLocation[i][0]); 		
				}
			}
		}
}

	// Keeps track of the current user so we can alert them if they 
	// change their own user data.
 	var adminUserId = '<%=adminUser.getUserId()%>';

// Establish the arrays for Select boxes

 	<%if (!UserDetailsAction.NOTHING_TO_DISPLAY.equals(udForm.getDisplayData())) { %>
	     //List of Federal access Levels 
	    i = 0;
	    var fedAccessLevels = new Array();
	    <logic:iterate id="fed" name="federalAccessLevels">
	       fedAccessLevels[i++] = new Array('<bean:write name="fed" property="accessLevelId"/>','<bean:write name="fed" property="name"/>');
	    </logic:iterate>		

	
	    //Regional access Level
	    i = 0;
	    var regAccessLevels = new Array();
	    <logic:iterate id="reg" name="regionalAccessLevels">
	       regAccessLevels[i++] = new Array('<bean:write name="reg" property="accessLevelId"/>','<bean:write name="reg" property="name"/>');
	    </logic:iterate>		

	    //List of State access Levels 
	    i = 0;
	    var stateAccessLevels = new Array();
	    <logic:iterate id="state" name="stateAccessLevels">
	       stateAccessLevels[i++] = new Array('<bean:write name="state" property="accessLevelId"/>','<bean:write name="state" property="name"/>');
	    </logic:iterate>		

	
	    //List of Local access Levels 
	    i = 0;
	    var localAccessLevels = new Array();
	    <logic:iterate id="local" name="localAccessLevels">
	       localAccessLevels[i++] = new Array('<bean:write name="local" property="accessLevelId"/>','<bean:write name="local" property="name"/>');
	    </logic:iterate>		
		
	     //List of Federal locations
	    i = 0;
	    var fedLocations = new Array();
	    <logic:iterate id="fed" name="federalLocationRefs">
	       fedLocations[i++] = new Array('<bean:write name="fed" property="locationId"/>','<bean:write name="fed" property="name"/>');
	    </logic:iterate>		

	    //List of Regional locations 
	    i = 0;
	    var regLocations = new Array();
	    <logic:iterate id="reg" name="regionalLocationRefs">
	       regLocations[i++]= new Array('<bean:write name="reg" property="locationId"/>','<bean:write name="reg" property="name"/>');
	    </logic:iterate>		

	    //List of State locations
	    i = 0;
	    var stateLocations = new Array();
	    <logic:iterate id="state" name="stateLocationRefs">
	       stateLocations[i++] = new Array('<bean:write name="state" property="locationId"/>','<bean:write name="state" property="name"/>');
	    </logic:iterate>		
	
	    //List of Local locations
	    i = 0;
	    var localLocations = new Array();
	    <logic:iterate id="local" name="localLocationRefs">
	       localLocations[i++] = new Array('<bean:write name="local" property="locationId"/>','<bean:write name="local" property="name"/>');
	    </logic:iterate>		
	
	    //List of Facilities
	    i = 0;
	    var facs = new Array();
	    <logic:iterate id="fac" name="facilitiesForRole">
	       facs[i++] = new Array('<bean:write name="fac" property="facilityId"/>','<bean:write name="fac" property="name"/>');
	    </logic:iterate>		
	<%}%>

</SCRIPT>

<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='showUserDetailsHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>
</DIV>

<font color="red">
<html:errors />
</font>

<logic:equal name="userDetailsForm" property="displayData"
			value="<%=UserDetailsAction.NOTHING_TO_DISPLAY%>">
<br><br>
<center>  
	<DIV class="PortletText1">
		<strong>Please Select a User From the User List</strong>
	</DIV>
</center>
<br><br>
</logic:equal>

<logic:notEqual name="userDetailsForm" property="displayData"
			value="<%=UserDetailsAction.NOTHING_TO_DISPLAY%>">

<pdk-html:form method="post" name="userDetailsForm" styleId="userDetailsForm"
	type="gov.epa.owm.mtb.cwns.userdetails.UserDetailsForm" action="userDetails.do">

<DIV id="hidden_fields" style="DISPLAY:none">	
	<pdk-html:text  name="userDetailsForm"  styleId="act" property="act" /> 
	<pdk-html:text  name="userDetailsForm"  styleId="roleDisplay" property="roleDisplay" /> 
	<pdk-html:text  name="userDetailsForm"  styleId="currentUserId"  property="cwnsUserId" /> 
	<pdk-html:text  name="userDetailsForm"  styleId="selectedRoleInfoId" property="selectedRoleInfoId" />
	<pdk-html:hidden name="userDetailsForm" property="regType"/>
	
	<input type="hidden" name="originalPortalUserName" value="<bean:write name="userDetailsForm" property="oidUserId" />">
</DIV>

<TABLE cellspacing="0" cellpadding="2" border="0" width="100%" class="PortletText1">

	<tr>
		<td><strong>CWNS User Id:</strong></td>
		<td><bean:write name="userDetailsForm" property="cwnsUserId"/>
	</tr>
 	<tr>
		<td><strong>Name:</strong></td>
		<td><bean:write name="userDetailsForm" property="name" /></td>
	</tr>
	
	<%if (udForm.getOidUserId() != null &&
		  udForm.getOidUserId().length() > 0 ) { %>
	<TR>
		<TD><strong>Portal User Name:</strong></TD>
		<TD>
			<bean:write name="userDetailsForm" property="oidUserId" />
				<pdk-html:hidden name="userDetailsForm" property="oidUserId"/>
		</TD>
	</TR>
	<%}%>
	
	<!-- STATUS  -->
	<TR>
		<TD><strong>Status:</strong></TD>
		<TD>  
			<logic:equal name="userDetailsForm" property="status" value="P"> 
				Pending
				<pdk-html:hidden name="userDetailsForm" property="status"/>
			</logic:equal>
			<logic:notEqual name="userDetailsForm" property="status" value="P"> 
				<%if (userDenied) {%> 
				Inactive
				<pdk-html:hidden name="userDetailsForm" property="status"/>
				<%} else if(UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())) { %>
				   <logic:equal name="userDetailsForm" property="status" value="P">
				       Pending
				   </logic:equal>
				   <logic:equal name="userDetailsForm" property="status" value="A">
				       Active
				   </logic:equal>
				   <logic:equal name="userDetailsForm" property="status" value="I">
				       Inactive
				   </logic:equal>
				   <pdk-html:hidden name="userDetailsForm" styleId="selectStatus" property="status"/>
				<%} else { %>
                        <pdk-html:select name="userDetailsForm" styleId="selectStatus" size="1" property="status" >
		            	<logic:iterate id="userStatus" name="cwnsUserStatusRefs" type="CwnsUserStatusRef">
							<%if (!"P".equals(userStatus.getCwnsUserStatusId())) { %>
								<logic:match name="userDetailsForm" property="status" value='<%=userStatus.getCwnsUserStatusId()%>'>
									<OPTION value=<bean:write name="userStatus" property="cwnsUserStatusId" /> selected="selected"><bean:write name="userStatus" property="name" /></OPTION>
								</logic:match>
								<logic:notMatch name="userDetailsForm" property="status" value='<%=userStatus.getCwnsUserStatusId()%>'>
									<OPTION value=<bean:write name="userStatus" property="cwnsUserStatusId" />><bean:write name="userStatus" property="name" /></OPTION>
								</logic:notMatch> 		            	
							<%}%>
		                </logic:iterate>
		            </pdk-html:select>  
	            <%}%>
			</logic:notEqual>
		 </TD>
	</TR>
	
	<TR>
		<TD><strong> Email:</strong></TD>
		<TD>
		    <%if(UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())) { %>
		       <bean:write name="userDetailsForm" property="email"/>
		       <pdk-html:hidden name="userDetailsForm"  property="email" />
		   <%}else {%>   
			  <pdk-html:text maxlength="50" size="39" name="userDetailsForm" styleId="email" property="email" />
			<%}%>
		</TD>
	</TR>	

	<TR>
		<TD><strong>Phone Number:</strong></TD>
		<TD>
		     <%if(UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())) { %>
		         <bean:write name="userDetailsForm"  property="phone" /> 
		         <pdk-html:hidden name="userDetailsForm"  property="phone" /> 
		     <%}else {%> 
		         <pdk-html:text maxlength="50" size="39" name="userDetailsForm" styleId="phone" property="phone" /> 
		     <%}%> 
			
		</TD>
	</TR>		

	<%if ("P".equals(udForm.getStatus())) { %>
		<TR>
			<TD width="25%">
			   <strong><bean:message bundle="SelfService" key="facility.list" /></strong>
			</TD>
			<TD>
				<bean:write name="userDetailsForm" property="facilityList" />
			</TD>
		</TR>
		<TR>
			<TD>
			   <strong><bean:message bundle="SelfService" key="town.and.county.list" /></strong>
			</TD>
			<TD>
				<bean:write name="userDetailsForm" property="townAndCountyList" />
			</TD>
		</TR>
		<TR>
			<TD>
			   <strong><bean:message bundle="SelfService" key="affiliation" /></strong>
			</TD>
			<TD>
				<bean:write name="userDetailsForm" property="affiliation" />
			</TD>
		</TR>
		<TR>
			<TD>
			   <strong><bean:message bundle="SelfService" key="title" /></strong>
			</TD>
			<TD>
				<bean:write name="userDetailsForm" property="title" />
			</TD>
		</TR>
		<TR>
			<TD><strong>User<br>Comments:</strong></TD>
			<TD>
				<bean:write name="userDetailsForm" property="comments" />		
			</TD>
		</TR>

	<%} else {%>
		<pdk-html:hidden name="userDetailsForm" property="comments"/>
	<%} %>
	
	<!-- Registration Notification Comments -->
	
	<logic:equal name="userDetailsForm" property="status" value="P"> 
		<TR> 
			<TD><strong>Registration Email<br>Notification Comments:</strong></TD>
			<TD>
 		<pdk-html:textarea name="userDetailsForm" property="regNotificationComments" rows="5" cols="30" /> 
			</TD>
		</TR>
	</logic:equal>
	<logic:notEqual name="userDetailsForm" property="status" value="P"> 
		<pdk-html:hidden name="userDetailsForm" property="regNotificationComments"/>
	</logic:notEqual>
	
	
</table>

<!-- Summary Role Information -->

<TABLE cellspacing="3" cellpadding="2" border="0" width="100%" class="PortletText1">
	<TR>
		<TD colspan="6">
			<font size="+1" >Roles</font>
		</TD>
	</TR>
	<TR>
		<TD width="5%" class="PortletHeaderColor">
			<STRONG> <FONT color="#ffffff">Primary</FONT> </STRONG>
		</TD>
		<TD width="25%" class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Role </STRONG> </FONT>
			</DIV>
		</TD>
		<TD width="25%" class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Access </STRONG> </FONT>
			</DIV>
		</TD>
		

		<TD width="6%" class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Edit </STRONG> </FONT>
			</P>
		</TD>
		<TD width="10%" class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
			</P>
		</TD>

		
		
	</TR>

	<logic:iterate id="userRole" name="userDetailsForm"  property="helpers"
					type="UserDetailsHelper">
		<TR>
			<TD align="center" valign="top" >
				<bean:define id="locId"><bean:write name="userRole" property="locationId" /></bean:define>
				<bean:define id="roleInfo"><bean:write name="userRole" property="role" /></bean:define>
				<bean:define id="limitedFacilities"><bean:write name="userRole" property="limitedFacilities" /></bean:define>
				<%if(UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())) { %>
				   <pdk-html:radio name="userDetailsForm" property="primaryRoleInfo" value="<%=roleInfo%>" disabled="true"></pdk-html:radio>
				   <logic:equal name="userDetailsForm" property="primaryRoleInfo" value="<%=roleInfo%>">
				       <pdk-html:hidden  name="userDetailsForm" styleId="primaryRole" property="primaryRoleInfo" value="<%=roleInfo%>"/>
				   </logic:equal>
				<%}else {%> 
		         <pdk-html:radio name="userDetailsForm" styleId="primaryRole" property="primaryRoleInfo" value="<%=roleInfo%>"></pdk-html:radio>
		        <%}%>   
			</TD>
			<TD valign="top" align="center"><bean:write name="userRole" property="role" /></TD>
			<TD valign="top" align="center">
				<logic:iterate id="accessLev" name="userRole"  property="accessLevels"	type="String">
					<bean:write name="accessLev" />
					<br>
				</logic:iterate>		
			</TD>
			
			<%if(adminUser.isRoleEditable(roleInfo) && adminUser.isEditableByLocal(limitedFacilities))  { %>
				
				<TD  valign="top">
					<P align="center">
					    <A href="javascript: setRoleInfoAndSubmit('<%=roleInfo%>','<%=UserDetailsAction.ACTION_DISPLAY_ROLE%>')">
					    	<pdk-html:img page="/images/edit.gif" border="0" alt="Edit User Role"/>
					    </A>
					</P>
				</TD>
				<TD width="10%" valign="top">
					<P align="center">
					    <A href="javascript: setRoleInfoAndSubmit('<%=roleInfo%>','<%=UserDetailsAction.ACTION_DELETE_ROLE%>')">
						    <pdk-html:img page="/images/delete.gif" border="0" alt="Delete User Role"/>
					    </A>
					</P>
				</TD>
			<%}%>			
			
		</TR>
	</logic:iterate>		
</table>



<logic:equal name="userDetailsForm" property="roleDisplay" value="<%=UserDetailsAction.DISPLAY_ROLE_INFO%>">				
	<DIV id="displayRoleLink" style="DISPLAY:none ">
</logic:equal>	
<logic:notEqual name="userDetailsForm" property="roleDisplay" value="<%=UserDetailsAction.DISPLAY_ROLE_INFO%>">				
	<DIV id="displayRoleLink" style="DISPLAY:block ">
</logic:notEqual>	


<TABLE  align="center" cellspacing="3" cellpadding="2" border="0" width="100%" class="PortletText1">
 <%if (udForm.isEnableAddRoleLink()) { %>
	<TR align="center">
		<TD>
			<P align="center">
  			    <A href="javascript: validateUserDataAndSubmit('<%=UserDetailsAction.ACTION_NEW_ROLE%>')">  
				    Add Role
			    </A>
			</P>		
		</TD>
	</TR>
	
 <%}%>
 
	<TR align="center">
	
		<TD>
			<logic:equal name="userDetailsForm" property="status" value="P"> 
				<input type="button" onclick="javascript: validateUserDataAndSubmit('<%=UserDetailsAction.ACTION_APPROVE_USER%>')" value="Approve">&nbsp;&nbsp;
				<input type="button" onclick="javascript: validateUserDataAndSubmit('<%=UserDetailsAction.ACTION_UPDATE_USER_INFO%>')" value="Update User Info">&nbsp;&nbsp;
				<input type="button" onclick="javascript: validateUserDataAndSubmit('<%=UserDetailsAction.ACTION_DENY_USER%>')" value="Deny">&nbsp;&nbsp;
			</logic:equal>
			<logic:notEqual name="userDetailsForm" property="status" value="P"> 
				<input type="button" onclick="javascript: validateUserDataAndSubmit('<%=UserDetailsAction.ACTION_UPDATE_USER_INFO%>')" value="Update User Info">&nbsp;&nbsp;
			</logic:notEqual>
		
		</TD>
	</TR>

</TABLE>

</DIV>


<!-- ***** ROLES ***** -->


<logic:equal name="userDetailsForm" property="roleDisplay" value="<%=UserDetailsAction.DISPLAY_ROLE_INFO%>">				
	<DIV id="displayRoleInfo" style="DISPLAY:block ">
</logic:equal>	
<logic:notEqual name="userDetailsForm" property="roleDisplay" value="<%=UserDetailsAction.DISPLAY_ROLE_INFO%>">				
	<DIV id="displayRoleInfo" style="DISPLAY:none">
</logic:notEqual>	

	<hr />
	<CENTER><H3>Role Information</H3></CENTER>

	<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	
	<!-- TYPE -->
	<TR>
		<TD>
		&nbsp;<STRONG>Type</STRONG>
		</TD>
		<TD>
			<logic:notEqual name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
				<bean:write name="userDetailsForm" property="currentLocationTypeId" />
				<pdk-html:hidden name="userDetailsForm" property="currentLocationTypeId"/>
			</logic:notEqual>	
			<logic:equal name="userDetailsForm"  property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
				<pdk-html:select size="1" styleId="selectType"  name ="userDetailsForm" property="currentLocationTypeId" 
  			          onchange="changeLocationType(this.form)" >  
				 	<OPTION value=""></OPTION>
				 		
			          <html:options collection="locationTypeRefs" property="locationTypeId"  /> 
	           </pdk-html:select>  

			</logic:equal>			
		</TD>
	</TR>
	
	<!-- LOCATION -->
	<TR>
		<TD>
			&nbsp;<STRONG>Location</STRONG>
		</TD>
		<TD>
			<%
			String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(prr,"UMFacilitySelectPage", linkParams,true, true);
			String locId=null;
			if (udForm.getRoleMode().equals(UserDetailsAction.ROLE_ADD) &&
			      locationRefs.size() == 1) { 
				Iterator iter = locationRefs.iterator();
				LocationRef locationRef = (LocationRef) iter.next();
				String name = locationRef.getName();
				locId = locationRef.getLocationId();
				%>
				<%=name%>
				<pdk-html:hidden name="userDetailsForm" property="currentLocId" value="<%=locId%>"/>
			<%}else { 
					locId = udForm.getCurrentLocId();
				%>
				<logic:notEqual name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
					<bean:write name="userDetailsForm" property="currentLocId" />
					<pdk-html:hidden name="userDetailsForm" property="currentLocId"/>
				</logic:notEqual>	
				<logic:equal name="userDetailsForm"  property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
					<pdk-html:select size="1" name ="userDetailsForm" styleId="selectLocation" property="currentLocId" onchange="removeAllOptionsFromSelectBox('facilityIds')">
					 	<OPTION value=""></OPTION>
		       	   <html:options collection="locationRefs" property="locationId" labelProperty="name"/> 
	          	  </pdk-html:select>  
				</logic:equal>			
			<%}%>
		</TD>
	</TR>	
	
	<!--  ACCESS LEVELS -->
	
	<TR>
		<td valign="top" >&nbsp;<STRONG> Access Levels </STRONG></td>
		<TD>  
			<!-- 
			<pdk-html:select size="11" multiple="11"  styleId="selectAccessLevel"  name ="userDetailsForm" property="accessLevelIds" > 
				<logic:equal name="userDetailsForm"  property="currentLocationTypeId" value="<%=UserService.LOCATION_TYPE_ID_REGIONAL%>">				
					 	<OPTION value=""><%=UserDetailsAction.VIEW_ONLY%></OPTION>
				</logic:equal>
				<logic:notEqual name="userDetailsForm"  property="currentLocationTypeId" value="<%=UserService.LOCATION_TYPE_ID_REGIONAL%>">				
					<logic:equal name="userDetailsForm"  property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
					 	<OPTION value="">Please select a Type</OPTION>
					</logic:equal>			
					<logic:notEqual name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
			    	     <html:options collection="userAccessLevels" property="accessLevelId" labelProperty="name"/> 
					</logic:notEqual>	
				</logic:notEqual>
           </pdk-html:select>  	
           -->
              
           
           <table border=0 id="altbl">
           <logic:equal name="userDetailsForm"  property="currentLocationTypeId" value="<%=UserService.LOCATION_TYPE_ID_REGIONAL%>">				
					 	<tr><td><%=UserDetailsAction.VIEW_ONLY%></td></tr>
			</logic:equal>
			<logic:notEqual name="userDetailsForm"  property="currentLocationTypeId" value="<%=UserService.LOCATION_TYPE_ID_REGIONAL%>">				
				<logic:equal name="userDetailsForm"  property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
					 	<tr><td>Please select a Type</td></tr>
				</logic:equal>			
				<logic:notEqual name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
			    	<logic:iterate id="userAccessLevel" name="userAccessLevels">
				    	<logic:notEqual name="userAccessLevel" property="name" value="View">
				    	<tr><td>
				    		<pdk-html:multibox name="userDetailsForm" property="accessLevelIds">
						       <bean:write name="userAccessLevel" property="accessLevelId"/>
						    </pdk-html:multibox> 
				    	     </td>
				    	     <td>
				    	     <bean:write name="userAccessLevel" property="name"/>
				    	     </td>
				    	</tr>
				    	</logic:notEqual>
				    	<logic:equal name="userAccessLevel" property="name" value="View">
				    		<tr><td>
				    		<input type="checkbox" value="View" disabled="disabled" checked="checked" />
				    		</td><td>View</td></tr>
				    	</logic:equal>
			    	</logic:iterate>			    
				</logic:notEqual>	
			</logic:notEqual>
           </table>
	 	</TD>
		<td width="30%" >&nbsp;</td>
	</TR>
	
	<tr><td>&nbsp;</td></tr>
	</TABLE>
	
<!--  FACILITIES -->
	<%if (UserService.LOCATION_TYPE_ID_REGIONAL.equals(udForm.getCurrentLocationTypeId()) ||
	      UserService.LOCATION_TYPE_ID_FEDERAL.equals(udForm.getCurrentLocationTypeId())){ %>
			<DIV id="facilityArea" style="DISPLAY:none" >
	<%}else {%>		
			<DIV id="facilityArea" style="DISPLAY:block" >
	<%} %>
	
	<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="PortletSubHeaderColor" >
		<td  colspan="2" valign="top" nowrap="nowrap">
		    <%if(UserService.LOCATION_TYPE_ID_LOCAL.equals(adminUser.getCurrentRole().getLocationTypeId())){ %>
		       <logic:equal name="userDetailsForm" property="allFacilities" value="N">
		           <pdk-html:radio name="userDetailsForm" property="allFacilities" styleId="allFacilitiesId" value="Y" onclick="enableSelFacs('N')" disabled="true"></pdk-html:radio> 		 
		       </logic:equal>
		        <logic:notEqual name="userDetailsForm" property="allFacilities" value="N">
		           <pdk-html:radio name="userDetailsForm" property="allFacilities" styleId="allFacilitiesId" value="Y" onclick="enableSelFacs('N')"></pdk-html:radio>
		       </logic:notEqual>
		    <%}else{ %>
			   <pdk-html:radio name="userDetailsForm" property="allFacilities" styleId="allFacilitiesId" value="Y" onclick="enableSelFacs('N')"></pdk-html:radio> 		
			<%} %>
			<strong> All Facilites in the state</strong>
		</td>
	</tr>
	
	
	<tr valign="top"  class="PortletSubHeaderColor" >
		<td valign="top" nowrap="nowrap" colspan=2>
			<pdk-html:radio name="userDetailsForm" property="allFacilities" styleId="allFacilitiesId2" value="N" onclick="enableSelFacs('Y')"></pdk-html:radio> 
			<strong>Selected Facilities</strong> &nbsp;<br/><br/>
		</td>
	</tr>

	</table>
	</DIV>	
	
	<table border="0">
	<tr>
	<td width="50%"></td>
	<td>
	<logic:equal name="userDetailsForm"  property="allFacilities" value="Y">
	<div id="facilitiesSelBlock" style="DISPLAY:none;width:200px;margin-bottom:10px;">
	</logic:equal>
	
	<logic:notEqual name="userDetailsForm"  property="allFacilities" value="Y">
	<div id="facilitiesSelBlock" style="DISPLAY:block;width:200px;margin-bottom:10px">
	</logic:notEqual>
	
	
			<pdk-html:select size="5" multiple="true" name="userDetailsForm" property="facilityIds" styleId="facilityIds" style="width:200"> 
			  	<html:options collection="selfacilities" property="facilityId" labelProperty="name"/>									
			</pdk-html:select>
			<pdk-html:hidden name="userDetailsForm" styleId="strFacilityIds" property="strFacilityIds"/>
	 	
			<center>
			<input type="button" value="delete" onclick="deleteFacilities('facilityIds')"/>
			<input type="button" value="add more.." onclick='showFacilitySelectWindow("<%=eventPopupWinowUrl2%>","<%=locId%>")'/>
			</center>
			</td>
			
	</div>
	</td>
	</tr>
	</table>
	
	<TABLE cellspacing="0" cellpadding="2" border="0" width="100%" class="PortletText1">	<TR>
		<TD align="center" colspan="8" >
			<logic:equal name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">				
				<input type="button" onclick="javascript: validateRoleAndSubmit(this.form,'<%=UserDetailsAction.ACTION_ADD_ROLE%>')" value="Save Role">&nbsp;&nbsp;
			</logic:equal>
			
			<logic:notEqual name="userDetailsForm" property="roleMode" value="<%=UserDetailsAction.ROLE_ADD%>">	
				<logic:notEqual name="userDetailsForm" property="currentLocationTypeId" value="<%=UserService.LOCATION_TYPE_ID_REGIONAL%>">	
					<input type="button" onclick="javascript: validateRoleAndSubmit(this.form, '<%=UserDetailsAction.ACTION_UPDATE_ROLE%>')" value="Update Role">&nbsp;&nbsp;
				</logic:notEqual>
			</logic:notEqual>
			
			
			<input type="button" name="cancel" value="Cancel" onclick="disappear()">
		</TD>
	</TR>
	</table>
</DIV>

</pdk-html:form>
</logic:notEqual>