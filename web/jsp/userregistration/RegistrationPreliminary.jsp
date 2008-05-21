<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page import="gov.epa.owm.mtb.cwns.model.LocationRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.RegistrationPreliminaryAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.RegistrationForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CurrentUser"%>


<%
    String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
	RegistrationForm rForm = (RegistrationForm)request.getAttribute("rForm");
	String facilityStateId = rForm.getFacilityStateId();
	
	boolean admin = (rForm.getMode().equals(RegistrationForm.PUBLIC_USER)) ? false : true;
	String msgBundle = (admin) ? "Admin" : "SelfService";

    CurrentUser currentUser = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER);
    String locationTypeId = "";
    if (currentUser != null) {
		locationTypeId = currentUser.getCurrentRole().getLocationTypeId();
    }
    String helpKey = (String)request.getAttribute("helpKey");
%>


<SCRIPT type="text/javascript">

function submitPrelimForm(action){
	window.document.getElementById("registration_action").value = action;
	var form = document.getElementById('registrationForm'); 
	form.submit();
}

function cancel(){
	window.location = "<%=rForm.getCwns2008Home()%>";
}

function submitAndValidatePrelimForm(action){
	var form = document.getElementById('registrationForm'); 
	if (userInfoValidated(form)) {
		submitPrelimForm(action);
	}
}

// Validate user input  
function userInfoValidated(form) {

	// CWNS Account
	var existingCwnsAccount = '';
	for (i=0; i<2; i++){
		if(form.cwnsAccount[i].checked) existingCwnsAccount = form.cwnsAccount[i].value;
	}
	
	if (existingCwnsAccount == 'yes') {
		// User has an existing CWNS Account
		cwnsUserId = window.document.getElementById("cwnsUserIdForRegistration").value;
		if (trim(cwnsUserId) == '') {
			alert('You indicated you have a CWNS UserId so it must be entered');
			window.document.getElementById("cwnsUserIdForRegistration").focus();
			return false;
		} 
	}
	
	// Portal Account
	var existingPortalAccount = '';
	for (i=0; i<2; i++){
		if(form.portalAccount[i].checked) existingPortalAccount = form.portalAccount[i].value;
	}
	
	
	if (existingPortalAccount == 'yes') {
		// User has an existing Portal Account
		oidUserId = window.document.getElementById("oidUserId").value;
		if (trim(oidUserId) == '') {
			alert('You indicated you have a Portal UserId so it must be entered');
			window.document.getElementById("oidUserId").focus();
			return false;
		} 
	}
	
	if ( existingCwnsAccount != 'yes') {
		// User must enter Role and Facilities location
		var userType = '';
		for (i=0; i<3; i++){
		
			if(form.userTypeId[i] != null && 
			   form.userTypeId[i].checked) userType = form.userTypeId[i].value;
		}

		if (form.userTypeId[0] != null && userType == '') {
			alert('You must specify a Role');
			return false;
		}
		
<%	if (rForm.getMode().equals(RegistrationForm.PUBLIC_USER)) { %>		
	
		var state_id = window.document.getElementById("facilityStateId").value;
		if (state_id == 'xx') {
			alert('You must indicate where the Facilities will be located');
			window.document.getElementById("facilityStateId").focus();
			return false;
		}
<% }%>		
	}
		
	return true;
} 
  
  
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}  
// Open help window
function showPortalRegistrationHelp(popupUrl)
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

  var w = window.open(popupUrl, 'PortalRegistrationHelp', settings);
  if(w != null) 
    w.focus();     
}

// The user entered text into a userId input field so we must:
// 		1.) Ensure the correct Radio button is selected.
//      2.) Input fields are cleared if necessary.
//      3.) Input fields are enabled or disabled as appropriate.
//      4.) Role information is displayed/hidden as necessary
// 
function userIdEntered() {
	var form       = document.getElementById('registrationForm'); 
	var cwnsUserId = trim(document.getElementById('cwnsUserIdForRegistration').value);
	var oidUserId  = trim(document.getElementById('oidUserId').value);

	setRoleReq("off")
	setLocationReq("off");
	
	// User Id
	if (cwnsUserId.length > 0 ) {
			form.cwnsAccount[1].checked = true;
			// User already has a CWNS account so hide questions related to CWNS Role
	    	window.document.getElementById('cwnsRoleInfo').style.display='none';
			
			
	} else {
		// No CWNS userid was specified
     	window.document.getElementById('cwnsRoleInfo').style.display='block';
		form.cwnsAccount[0].checked = true;
		if (form.userTypeId[0] != null ) {
			// user must specify the Role
			setRoleReq("on")			
		}
		
		<%if (!locationTypeId.equals("State") && !locationTypeId.equals("Local")) { %>
			// user must specify the location
			setLocationReq("on");
		<%}%>
	}
	
	// PORTAL ID
	if (oidUserId.length > 0) {
		form.portalAccount[1].checked = true;
	} else {
		form.portalAccount[0].checked = true;
	}
	
	<%if (rForm.getMode().equals(RegistrationForm.PUBLIC_USER) && rForm.getUserTypeId().equals("Local")) { %>
	    window.document.getElementById('localRoleDivId').style.display='block';
	<%}%>

}


// When the user clicks on a userId radio button make sure 
// 	1.) The userId input field is cleared if necessary
//  2.) Input fields are enabled or disabled as appropriate.
function radioButtonSelected() {
	var form       = document.getElementById('registrationForm'); 
	var oidUserId  = document.getElementById('oidUserId'); 
	var cwnsUserId = document.getElementById('cwnsUserIdForRegistration'); 

	setRoleReq("off")
	setLocationReq("off");
	window.document.getElementById('cwnsRoleInfo').style.display='none';
	
	// Portal userId
	for (i=0; i<2; i++){
		if(form.portalAccount[i].checked) var pAccount = form.portalAccount[i].value;
	}
	if (pAccount == 'no') {
		oidUserId.value= '';
	}
	
	// Cwns userId
	for (i=0; i<2; i++){
		if(form.cwnsAccount[i].checked) var cAccount = form.cwnsAccount[i].value;
		
	}
	
	if (cAccount == 'no') {
		// No CWNS account was specified
		cwnsUserId.value= '';
 		// Identify required fields
		form.cwnsAccount[0].checked = true;
		if (form.userTypeId[0] != null ) {
			// user must specify the Role
			
			setRoleReq("on")			
		}
		<%System.out.println("locationTypeId = "+locationTypeId);
		if (!locationTypeId.equals("State") && !locationTypeId.equals("Local")) { %>
			// user must specify the location
			
			setLocationReq("on");
		<%}%>
		window.document.getElementById('cwnsRoleInfo').style.display='block';
	}
	
}

function setRequired(action) {
	if (action == "off") {
      	window.document.getElementById('Req').style.display='none';
      	window.document.getElementById('Space').style.display='block';
      	window.document.getElementById('Req1').style.display='none';
      	window.document.getElementById('Space1').style.display='block';
    } else {
      	window.document.getElementById('Req').style.display='block';
      	window.document.getElementById('Space').style.display='none';
      	window.document.getElementById('Req1').style.display='block';
      	window.document.getElementById('Space1').style.display='none';
    }
}


// The Location is required
function setLocationReq(action) {
	if (action == "off") {
      	window.document.getElementById('Req1').style.display='none';
      	window.document.getElementById('Space1').style.display='block';
    } else {
	  
      	window.document.getElementById('Req1').style.display='block';
      	window.document.getElementById('Space1').style.display='none';
    }
}

// The Role is required
function setRoleReq(action) {
	if (action == "off") {
      	window.document.getElementById('Req').style.display='none';
      	window.document.getElementById('Space').style.display='block';
    } else {
  		
      	window.document.getElementById('Req').style.display='block';
      	window.document.getElementById('Space').style.display='none';
    }
}

function toggleLocalRole(display){
  if(display==true){
  	window.document.getElementById('localRoleDivId').style.display='block';
  }else{
    window.document.getElementById('localRoleDivId').style.display='none'; 
  }
   
   

}
    
</SCRIPT>

<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='showPortalRegistrationHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>
</DIV>

<FONT color="red">
<html:errors />
</FONT>

<pdk-html:form method="post" 
	name="registrationForm" 
	type="gov.epa.owm.mtb.cwns.userregistration.RegistrationForm" 
	styleId="registrationForm"
	action="registrationPreliminary.do">

	<pdk-html:hidden styleId="registration_action" name="registrationForm" property="action" value=""/>
	<pdk-html:hidden  name="registrationForm" property="mode"/>

	<TABLE cellpadding="0" cellspacing="0" align="center" border="0" width="400"  class="PortletText1" >
	
	<TR>
		<TD colspan="3" align="center" ><STRONG>Preliminary User Information</STRONG></TD>
	</TR>
	<TR><TD colspan="3">&nbsp;</TD></TR>	


	
	<TR>		
    	<TD colspan="3"><strong><bean:message bundle="<%=msgBundle%>" key="existing.cwns.account" /></strong><BR>
    	</TD>
	</TR>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="cwnsAccount"  onclick="radioButtonSelected()"  value="no" checked="checked" >
<%-- 		</TD><TD> --%>
		 No</TD>
	</tr>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="cwnsAccount" value="yes">
		Yes</TD>
	</tr>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2" >CWNS UserId
			&nbsp;<pdk-html:text maxlength="50" size="30"  onkeyup="userIdEntered()" name="registrationForm" styleId="cwnsUserIdForRegistration" property="cwnsUserId" />
		</TD>
	</tr>

	<tr><td colspan="3">&nbsp;</td></tr>	
	
	<logic:empty name="username">
	<logic:empty name="user_id">
	<TR>
	
     	<TD colspan="3"><strong><bean:message bundle="<%=msgBundle%>" key="existing.portal.account" /></strong></TD>
	</TR>
	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="portalAccount" onclick="radioButtonSelected()" checked="checked" value="no" >
		No</TD>
	</tr>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="portalAccount" value="yes">
		Yes</TD>
	</tr>
	
	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2">Portal UserId &nbsp;
				<pdk-html:text maxlength="50" size="30" onkeyup="userIdEntered()" name="registrationForm" styleId="oidUserId" property="oidUserId" />
		</TD>
	</tr>
	</logic:empty>
	</logic:empty>
	
	<logic:notEmpty name="username">
	<TR>
	
     	<TD colspan="3"><strong><bean:message bundle="<%=msgBundle%>" key="existing.portal.account" /></strong></TD>
	</TR>
	<tr>
		<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="portalAccount" onclick="radioButtonSelected()" checked="checked" value="no" DISABLED>
		No</TD>
	</tr>

	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2"><input type="radio" name="portalAccount" value="yes">
		Yes</TD>
	</tr>
		<td>&nbsp;&nbsp;</td>
		<TD colspan="2">Portal UserId &nbsp;
				<input type="text" maxlength="50" size="30" name="oidUserId" value="<%=rForm.getOidUserId()%>" READONLY>
				</TD>
	</logic:notEmpty>
	
	<logic:notEmpty name="user_id">
	
	<div style="DISPLAY:none">
	<input type="radio" name="portalAccount" onclick="radioButtonSelected()" checked="checked" value="no" DISABLED>
	<input type="radio" name="portalAccount" value="yes">
	<input type="text" maxlength="50" size="30" name="oidUserId" value="<%=rForm.getOidUserId()%>" READONLY>
	</div>
	</logic:notEmpty>
	
	
	<tr><td colspan="3">&nbsp;</td></tr>
	</table>
	
	<!--  CWNS Role Information -->
	

	<DIV id="cwnsRoleInfo" class="PortletText1" style="DISPLAY:block" >
		<TABLE cellpadding="0" cellspacing="0"  width="400" border="0" align="center"  class="PortletText1">
	    <TR>
      	   <TD colspan="3"><div id="space1" style="float:left; display: none"><p class="required">&nbsp;</p></div><div id="req1" style="float:left; display: block"><p class="required">*&nbsp;</p></div>
      		  <strong><bean:message bundle="<%=msgBundle%>" key="facilities.location" /></strong>
      	   </TD>
	    </TR>
	    <TR>
		    <td width="10">&nbsp;&nbsp;</td>
            <%	if (rForm.getMode().equals(RegistrationForm.PUBLIC_USER)) { %>		
		        <TD colspan="2">
				 <pdk-html:select name ="registrationForm" styleId="facilityStateId" property="facilityStateId" >
			 		<OPTION value="xx"></OPTION>
					<logic:iterate id="locationRef" name="locationRefs" type="LocationRef">
			  			<logic:match name="locationRef" property="locationId" value='<%=facilityStateId%>'>				
							<OPTION value='<bean:write name="locationRef" property="locationId"/>' selected="selected"> <bean:write name="locationRef" property="name"/></OPTION> 
						</logic:match>
			  			<logic:notMatch name="locationRef" property="locationId" value='<%=facilityStateId%>'>
							<OPTION value='<bean:write name="locationRef" property="locationId"/>'> <bean:write name="locationRef" property="name"/></OPTION> 
						</logic:notMatch>
		        	</logic:iterate>
       	  		 </pdk-html:select>  
			    </TD>		   
            <% }else if (locationTypeId.equals("Federal")) { %>		
				<TD colspan="2">
					<pdk-html:select name ="registrationForm" styleId="facilityStateId" property="facilityStateId" >
					 	<OPTION value="xx"></OPTION>
						
						<logic:iterate id="locationRef" name="locationRefs" type="LocationRef">
					  		<logic:match name="locationRef" property="locationId" value='<%=facilityStateId%>'>				
								<OPTION value='<bean:write name="locationRef" property="locationId"/>' selected="selected"> <bean:write name="locationRef" property="name"/></OPTION> 
							</logic:match>
					  		<logic:notMatch name="locationRef" property="locationId" value='<%=facilityStateId%>'>
								<OPTION value='<bean:write name="locationRef" property="locationId"/>'> <bean:write name="locationRef" property="name"/></OPTION> 
							</logic:notMatch>
				        </logic:iterate>
				        
		       	  	</pdk-html:select>  
				</TD>
            <%} else { %>
				<TD colspan="2">
					<logic:iterate id="locationRef" name="locationRefs" type="LocationRef">
				  		<logic:match name="locationRef" property="locationId" value='<%=facilityStateId%>'>	
				  			<pdk-html:hidden  name="registrationForm"  styleId="facilityStateId" property="facilityStateId"/>
							<bean:write name="locationRef" property="name"/>
						</logic:match>
			        </logic:iterate>
				</TD>
		    <%}%>		
		</TR>
		<tr><td colspan="3">&nbsp;</td></tr>
		
       <%	if (rForm.getMode().equals(RegistrationForm.PUBLIC_USER)) { %>		    	
    	<TR>
    		<TD colspan="3">
    			<strong><bean:message bundle="<%=msgBundle%>" key="facility.list"/></strong>
    		</TD>
    	</TR>
    	<TR>
			<td colspan="3">
				<pdk-html:textarea name="registrationForm" property="facilityList" rows="3" cols="40" />
			</td>		
		</TR>
		<tr><td colspan="3">&nbsp;</td></tr>
    	<TR>
    		<TD colspan="3">
    			<strong><bean:message bundle="<%=msgBundle%>" key="town.and.county.list"/></strong>
    		</TD>
    	</TR>
    	<TR>
				<td colspan="3">
			    	<pdk-html:textarea name="registrationForm" property="townAndCountyList" rows="3" cols="40" />
			</td>		
		</TR>
		<tr><td colspan="3">&nbsp;</td></tr>
		<TR>
    		<TD>
    			<strong><bean:message bundle="<%=msgBundle%>" key="affiliation"/></strong>
    		</TD>
    		<td  colspan="2">
    			<pdk-html:text maxlength="40" size="30" name="registrationForm" property="affiliation" />
    		</td>
    	</TR>
    	<tr><td colspan="3">&nbsp;</td></tr>
		<TR>
    		<TD>
    			<strong><bean:message bundle="<%=msgBundle%>" key="title"/></strong>
    		</TD>
    		<td colspan="2">
    			<pdk-html:text maxlength="40" size="30" name="registrationForm" property="title" />
    		</td>
    	</TR>
		<tr><td colspan="3">&nbsp;</td></tr>
		<%} %>


		<TR>
    		<TD colspan="3">
    			<div id="space" style="float:left; display: none"><p class="required">&nbsp;</p></div><div id="req" style="float:left; display: block"><p class="required">*&nbsp;</p></div>
    			<strong><bean:message bundle="<%=msgBundle%>" key="role.type" /></strong>
    		</TD>
		</TR>
	
	
	
	
<%	if (rForm.getMode().equals(RegistrationForm.PUBLIC_USER)) { %>		
	<!-- remove federal radio button from self-registration 
	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="Review"/></TD>
		<TD>Review survey data&nbsp;&nbsp;[Federal User]</TD>
	</tr>
	 -->
	<tr>
		<td>&nbsp;</td>
		<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="State" onclick="toggleLocalRole(false);"/></TD>
		<TD>Add/Update survey data&nbsp;&nbsp;[State User]</TD>
	</tr>
	
	<tr>
		<td>&nbsp;</td>
		<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="Local" onclick="toggleLocalRole(true);"/></TD>
		<TD>Provide feedback &nbsp;&nbsp;[Local User]</TD>
	</tr>
	<tr>
	    <td >&nbsp;</td>
	    <td colspan="2">
	         <DIV id="localRoleDivId" class="PortletText1" style="DISPLAY:none">
	          <pdk-html:checkbox name="registrationForm" property="surveyFeedback"><bean:message bundle="<%=msgBundle%>" key="local.role.feedback"/></pdk-html:checkbox><br/>
	          <pdk-html:checkbox name="registrationForm" property="utilFeedback"><bean:message bundle="<%=msgBundle%>" key="local.role.unitProcess"/></pdk-html:checkbox> <br/>
	          <pdk-html:checkbox name="registrationForm" property="userManagement"><bean:message bundle="<%=msgBundle%>" key="local.role.usermanagement"/></pdk-html:checkbox> <br/>
	         </DIV> 
	    </td>
	</tr>					
	
<%}else if (locationTypeId.equals("Federal")) { %>		
			<tr>
				<td>&nbsp;&nbsp;</td>
				<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="Review"/></TD>
				<TD>Review survey data&nbsp;&nbsp;[Federal User]</TD>
			</tr>	
			<tr>
				<td>&nbsp;&nbsp;</td>
				<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="State"/></TD>
				<TD>Add/Update survey data&nbsp;&nbsp;[State User]</TD>
			</tr>
<%} else if (locationTypeId.equals("State")) { %>
			<tr>
				<td>&nbsp;&nbsp;</td>
				<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="State"/></TD>
				<TD>Add/Update survey data&nbsp;&nbsp;[State User]</TD>
			</tr>

		<tr>
			<td>&nbsp;&nbsp;</td>
			<TD><pdk-html:radio name="registrationForm" styleId="userTypeId" property="userTypeId" value="Local"/></TD>
			<TD>Provide feedback on survey data&nbsp;&nbsp;[Local User]</TD>
		</tr>					
<%} else { // Local User %>
	<tr>
		<td>&nbsp;&nbsp;</td>
		<TD><pdk-html:hidden  name="registrationForm" styleId="userTypeId" property="userTypeId" value="Local"/></TD>
		<TD>Provide feedback on survey data&nbsp;&nbsp;[Local User]</TD>
	</tr>		
<%} %>	

	
	<tr><td>&nbsp;</td></tr>	
	
</TABLE>
</DIV>	



<%if (!admin){ // this is a self registration %>
	<TABLE cellpadding="0" cellspacing="0" align="center" border="0" width="400"  class="PortletText1" >
	
	<TR >
    	<TD colspan="3"><strong>Comments:</strong> <bean:message bundle="<%=msgBundle%>" key="reg.comments.ins"/></TD>
	</TR>
	<TR>
		<td>&nbsp;&nbsp;</td>
		<td colspan="2">
			<pdk-html:textarea name="registrationForm" property="comments" rows="5" cols="40" />
		</td>		
	</TR>
	</TABLE>
<%}%>	



<P style="margin-bottom:10px;text-align:center;">

<logic:equal name="registrationForm" property="mode" value="<%=RegistrationForm.PUBLIC_USER%>">
	<INPUT type="button" value="Cancel" onclick="javascript:cancel();">
</logic:equal>	

<logic:notEqual name="registrationForm" property="mode" value="<%=RegistrationForm.PUBLIC_USER%>">
	<INPUT type="button" value="Cancel" onclick="javascript:submitPrelimForm('<%=RegistrationPreliminaryAction.ACTION_CANCEL%>');"> 
</logic:notEqual>	

<INPUT type="button" value="Next" onclick="javascript:submitAndValidatePrelimForm('<%=RegistrationPreliminaryAction.ACTION_PROCESS%>');">

</P>

</pdk-html:form>
<SCRIPT type="text/javascript">
	userIdEntered();
</SCRIPT>

