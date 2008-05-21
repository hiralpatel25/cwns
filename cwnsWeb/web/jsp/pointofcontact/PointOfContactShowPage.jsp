<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactAction"%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="oracle.portal.utils.NameValue"
	%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
	NameValue[] linkParams       = new NameValue[1];
	linkParams[0] = new NameValue("formId", "pointOfContactForm");


	PointOfContactForm pocForm = (PointOfContactForm)request.getAttribute("pocForm");
	String stateId = pocForm.getStateId();
					
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pRequest,
							"../../css/cwns.css")%>">


<SCRIPT type=text/javascript>

// Show Add/Edit POC
function showAddResponsibleEntity(){
     window.document.getElementById('responsibleEntity').style.display='block';
     window.document.getElementById('responsibleEntityLink').style.display='none';
     
}
// Hide Add/Edit POC
function hideAddResponsibleEntity(){
     window.document.getElementById('responsibleEntity').style.display='none';
     window.document.getElementById('responsibleEntityLink').style.display='block';
}

// Set the action, pocId and submit
function setPocAndSubmit(pocId,action) {
    window.document.getElementById('editPocId').value = pocId;
	setActionAndSubmit(action);
}

// Set the action and submit
function setActionAndSubmit(action) {
	window.document.getElementById('pocAction').value    = action;
	window.document.pointOfContactForm.submit();

}
// Validate and submit
function validateAndSubmit(pocId,action) {
	var authorityName = window.document.getElementById("authorityName").value;
	authorityName = trimSpaces(authorityName);
	var zip = window.document.getElementById("zip").value;
	zip = trimSpaces(zip);
	
	if (authorityName.length < 1) {
		alert('You must supply an Authority Name.');
		window.document.getElementById("authorityName").focus();
		return false;
	} else if (!validZip(zip))  {
		alert('Zip Code must either be 5 numbers or 5 numbers followed by a - followed by 4 numbers');
		window.document.getElementById("zip").focus();
		return false;
	} else {
		setPocAndSubmit(pocId,action);
	}
}

// Validate the zipcode
function validZip(zip) {
	valid = false
	if (zip.length == 0) {
		valid = true;
	} else if (zip.length == 5) {
		valid = !isNaN(zip);
	} else if (zip.length == 10) {
		prefix = zip.substring(0,5);
		dash   = zip.substring(5,6);
		suffix = zip.substring(6,zip.length);
		if (!isNaN(prefix) && !isNaN(suffix) && dash == '-') {
			valid = true;
		}
	} 
	return valid;
}
            
// Trim whitespace from left and right sides of s.
function trimSpaces(s) {
    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}

// Set action to Add User and submit
function addNewPoc() {
	window.document.getElementById('pocAction').value  = '<%=PointOfContactAction.ACTION_ADD_POC%>';
	window.document.pointOfContactForm.submit();
}


function ShowLookUpWindow(popupUrl)
{
    var w = 600;
    var h = 300;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    var combobox = document.getElementById('selectStateId');
    var s=combobox.options[combobox.selectedIndex].value;
    var url = popupUrl+'&_event_locationId='+s;  
    var w = window.open(url, null, settings);
  if(w != null) 
    w.focus();     
}

</SCRIPT>

<pdk-html:form method="post" 
	name="pointOfContactForm" 
	type="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactForm" 
	styleId="pointOfContactForm"
	action="pointOfContact.do">

<%-- Hidden Fields --%>
<pdk-html:hidden styleId="pocAction" name="pointOfContactForm" property="action"/>
<pdk-html:hidden styleId="editPocId" name="pointOfContactForm" property="editPocId"/>

<TABLE cellspacing="2" cellpadding="3" border="0" width="100%" 	class="PortletText1">
	<TR>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> Responsible<BR> Entity </STRONG>
			</FONT>
		</TD>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> Authority </STRONG>
			</FONT>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Role/Title </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				&nbsp;
				<STRONG> <FONT color="#ffffff"> Contact Name</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Phone </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Email </STRONG>
				</FONT>
			</P>
		</TD>
		<%-- 
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Envirofacts </STRONG>
				</FONT>
			</P>
		</TD> --%>
		
		
		<td class="PortletHeaderColor">		
			<%if (pocForm.isUpdateable()) { %>
				<FONT color="#ffffff"><STRONG>Edit</STRONG></FONT>
			<%}else{  %>
				<FONT color="#ffffff"><STRONG>Details</STRONG></FONT>
			<%}%>		
		</td>
		
		<%if (pocForm.isUpdateable()) { %>
		<td class="PortletHeaderColor">
			<FONT color="#ffffff"><STRONG>Delete</STRONG></FONT>
		</td>
		<%} else{%>
		<logic:equal name="localUser" value="true">
		<td class="PortletHeaderColor">
			<FONT color="#ffffff"><STRONG>Delete</STRONG></FONT>
		</td>
		</logic:equal>
		<%} %>		
		
	</TR>

	<c:set var="i" value="1" />
	<logic:iterate id="poc" name="pocs" >
		<c:choose>
			<c:when test='${i%2=="0"}'>
				<c:set var="class" value="PortletSubHeaderColor" />   
			</c:when>
			<c:otherwise>
				<c:set var="class" value="" />
			</c:otherwise>
		</c:choose>
		
	<logic:equal name="poc" property="pointOfContactId" value="<%=pocForm.getEditPocId()%>">
	<TR class="RowHighlighted">	
	</logic:equal>	

	<logic:notEqual name="poc" property="pointOfContactId" value="<%=pocForm.getEditPocId()%>">
	<TR class="<c:out value="${class}"/>">	
	</logic:notEqual>	

		<TD  valign="top" align="center">
			<logic:equal name="poc" property="pointOfContactId" value="<%=pocForm.getPrimaryPocId()%>">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</logic:equal>	
		</TD>
		<TD><bean:write name="poc" property="authorityName" /></TD>
		<TD><P align="center"><bean:write name="poc" property="roleTitle" /></P></TD>
		<TD><P align="center"><bean:write name="poc" property="name" /></P></TD>
		<TD><P align="center"><bean:write name="poc" property="phone" /></P></TD>
		<TD>
			<a href="mailto:<bean:write name="poc" property="email" />?subject=Clean Watersheds Needs Survey" ><bean:write name="poc" property="email" /></a>			
		</TD>
		<%-- 
		<TD>
			<logic:equal name="poc" property="sourcedFromNpdesFlag" value="Y">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</logic:equal>	
		</TD> --%>
		<TD  valign="top">
			<P align="center">
			    <A href="javascript: setPocAndSubmit('<bean:write name="poc" property="pointOfContactId" />','<%=PointOfContactAction.ACTION_EDIT_POC%>')">
				<%if (pocForm.isUpdateable()) { %>
			    	<pdk-html:img page="/images/edit.gif" border="0" alt="Edit Point of Contact"/>
				<%} else { %>
			    	<pdk-html:img page="/images/plus.gif" border="0" alt="Point of Contact Details"/>
				<%} %>
			    </A>
			</P>
		</TD>
		<%if (pocForm.isUpdateable()) { %>
				
		<td valign="top" align="center" >
		  <logic:equal name="localUser" value="false">
		    <A href="javascript: setPocAndSubmit('<bean:write name="poc" property="pointOfContactId" />','<%=PointOfContactAction.ACTION_DELETE%>')">
			    <pdk-html:img page="/images/delete.gif" border="0" alt="Delete Point of Contact"/>		
		    </A>
		  </logic:equal>
		  <logic:equal name="localUser" value="true">
		    <bean:define id="pocId">
               javascript: setPocAndSubmit('<bean:write name="poc" property="pointOfContactId"/>','<%=PointOfContactAction.ACTION_DELETE%>')
            </bean:define>
            <pdk-html:checkbox name="poc" property="feedbackDeleteFlag" value="Y" onclick='<%=pocId%>'></pdk-html:checkbox>
		  </logic:equal>  
		</td>
		<%} else{%>
		<logic:equal name="localUser" value="true">
		<td valign="top" align="center" >
		  <pdk-html:checkbox name="poc" property="feedbackDeleteFlag" value="Y" disabled="true"></pdk-html:checkbox>
		</td>
		</logic:equal>
		<%} %>		
		
	</TR>
	<c:set var="i" value="${i+1}" />
	</logic:iterate>
	
</TABLE>	


<logic:equal name="pointOfContactForm" property="displayDetails" value="Y">				
	<DIV id="responsibleEntityLink" class="PortletText1"  style="DISPLAY:none" >
</logic:equal>	
<logic:notEqual name="pointOfContactForm" property="displayDetails" value="Y">				
	<DIV id="responsibleEntityLink" class="PortletText1"  style="DISPLAY:block" >
</logic:notEqual>	
	<%if (pocForm.isUpdateable()) { %>
		&nbsp;&nbsp;<A href="javascript: addNewPoc()">Add Point of Contact</A>
	<%}%>
</DIV>	


<!--  Add/Edit Point of Contact -->

<logic:equal name="pointOfContactForm" property="displayDetails" value="Y">				
	<DIV  class="PortletText1" id="responsibleEntity"  style="background-color: #cccccc;padding: 5">   
</logic:equal>	
<logic:notEqual name="pointOfContactForm" property="displayDetails" value="Y">				
	<DIV id="responsibleEntity" style="padding: 5;DISPLAY:none">   
</logic:notEqual>	

<br>
<%if (pocForm.isUpdateable()) { %>
	<font color="red">
	<html:errors />
	</font>
	
<table cellspacing="2" cellpadding="3" border="0" width="100%" 	class="PortletText1">
		<tr>
			<td colspan="3">
				<STRONG>
				
				<FONT size="2">	Add/Edit &nbsp;Point of Contact</FONT>
				</STRONG>
			</td>
		</tr>
	<logic:equal name="pointOfContactForm" property="sourcedFromNpdes" value="Y">					
		<tr>
		    <TD width="1%"></TD>  
			<td><STRONG>Source:</STRONG></td>
			<td>Envirofacts</td>
		</tr>
	</logic:equal>	
		<TR>
		    <TD width="1%">
		       <STRONG> <FONT color="#ff0000">* </FONT></STRONG>
		    </TD>  
			<TD>
				<STRONG>Authority Name:</STRONG>
			</TD>
			<TD>
			<pdk-html:text name="pointOfContactForm" styleId="authorityName" property="authorityName" size="50" maxlength="50" />
			</TD>
			<TD>
				<pdk-html:checkbox name="pointOfContactForm" property="responsibleEntity" value="Y"/>&nbsp;
				<STRONG>Responsible Entity </STRONG><br>
				<pdk-html:checkbox name="pointOfContactForm" property="tribe" value="Y"/>&nbsp;
				<STRONG>Tribe</STRONG><br>
				<logic:equal name="pointOfContactForm" property="superfundPossible" value="true">
				  <pdk-html:checkbox name="pointOfContactForm" property="superfundResponsibleParty" value="Y" />&nbsp;
				</logic:equal>
				<logic:equal name="pointOfContactForm" property="superfundPossible" value="false">
				  <pdk-html:checkbox name="pointOfContactForm" property="superfundResponsibleParty" value="Y" disabled="true"/>&nbsp;
				</logic:equal>
				<STRONG>Superfund Responsible Party</STRONG><br>
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD><STRONG>Contact Name:</STRONG></TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="contactName" size="50" maxlength="50" />
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD><STRONG>Role/Title:</STRONG></TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="title" size="50" maxlength="50" />
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD><STRONG>Phone Number:</STRONG></TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="phone" size="50" maxlength="20" value='<%=pocForm.getPhoneFormated()%>' />
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD><STRONG>Fax Number:</STRONG></TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="fax" size="50" maxlength="20"  value='<%=pocForm.getFaxFormated()%>' />
			</TD>
		</TR>
	
		<TR><TD>&nbsp;</TD></TR>
		
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>Address:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="address1" size="50" maxlength="50" />
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>Address 2:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="address2" size="50" maxlength="50" />
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>City:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="city" size="50" maxlength="50" />
			</TD>
		</TR>
		
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>State:</STRONG>
			</TD>
			<TD>
				<pdk-html:select size="1"  name ="pointOfContactForm" styleId="selectStateId" property="stateId" >  
					<logic:iterate id="state" name="states" type="Entity">
			  		<logic:match name="state" property="key" value='<%=stateId%>'>				
	 					<OPTION value='<bean:write name="state" property="key"/>' selected="selected"> <bean:write name="state" property="value"/></OPTION> 
	 				</logic:match>
			  		<logic:notMatch name="state" property="key" value='<%=stateId%>'>
	 					<OPTION value='<bean:write name="state" property="key"/>'> <bean:write name="state" property="value"/></OPTION> 
	 				</logic:notMatch>
			        </logic:iterate>
	           </pdk-html:select>  
			</TD>
		</TR>
	
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>County:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" styleId="countyName" property="countyName" size="50" maxlength="50" />
			   <%
	           String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(pRequest,"ViewCountyListPage", linkParams,true, true);%>
			   <A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl2%>")'">
			        <pdk-html:img page="/images/findsmall.gif" border="0" alt="Lookup County"/>
			   </A>
			</TD>
		</TR>
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>Zip Code:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm"  styleId="zip" property="zip" size="50" maxlength="50" />
			</TD>
		</TR>
	
		<TR><TD>&nbsp;</TD></TR>
		
		<TR>
		    <TD width="1%"></TD>  
			<TD>
				<STRONG>Email:</STRONG>
			</TD>
			<TD>
				<pdk-html:text name="pointOfContactForm" property="email" size="50" maxlength="50" />
			</TD>
			
		</TR>
	
		<TR>
			<td colspan="6">
	
			<logic:equal name="pointOfContactForm" property="action" value="<%=PointOfContactAction.ACTION_ADD_POC%>">				
				<INPUT type="button" name="Save" value="Save" onclick ="javascript: validateAndSubmit('<bean:write name="pointOfContactForm" property="editPocId" />','<%=PointOfContactAction.ACTION_PROCESS_NEW_POC%>')">&nbsp;&nbsp;
			</logic:equal>	
			<logic:notEqual name="pointOfContactForm" property="action" value="<%=PointOfContactAction.ACTION_ADD_POC%>">				
				<INPUT type="button" name="Save" value="Save" onclick ="javascript: validateAndSubmit('<bean:write name="pointOfContactForm" property="editPocId" />','<%=PointOfContactAction.ACTION_UPDATE_POC%>')">&nbsp;&nbsp;
			</logic:notEqual>			
				<INPUT type="button" name="cancel" onclick="javascript: setActionAndSubmit('<%=PointOfContactAction.ACTION_DEFAULT_QUERY%>')" value="Cancel"> 
			</td>
		</TR>
	</TABLE>    
	
	<!-- POC details display only  -->
	
<%}else { %>	   
	<TABLE cellspacing="2" cellpadding="3" border="0" width="99%" class="PortletText1">
		<tr>
			<td colspan="3">
				<STRONG><FONT size="2">Point of Contact Details</FONT></STRONG>
			</td>
		</tr>
	<logic:equal name="pointOfContactForm" property="sourcedFromNpdes" value="Y">					
		<tr>
		    <TD width="1%"></TD>  
			<td><STRONG>Source:</STRONG></td>
			<td>Envirofacts</td>
		</tr>
	</logic:equal>	
		<TR>
		    <TD valign="top" width="1%">
		       <STRONG> <FONT color="#ff0000">* </FONT></STRONG>
		    </TD>  
			<TD valign="top" ><STRONG>Authority Name:</STRONG></TD>
			<TD valign="top" ><bean:write name="pointOfContactForm" property="authorityName" /></TD>
			
			<TD>
				<pdk-html:checkbox name="pointOfContactForm" disabled="true" property="responsibleEntity" value="Y"/>&nbsp;
				<STRONG>Responsible Entity </STRONG><br>
				<pdk-html:checkbox name="pointOfContactForm" disabled="true" property="tribe" value="Y"/>&nbsp;
				<STRONG>Tribe</STRONG><br>
				<pdk-html:checkbox name="pointOfContactForm" disabled="true" property="superfundResponsibleParty" value="Y"/>&nbsp;
				<STRONG>Superfund Responsible Party</STRONG><br>
			</TD>
		</TR>
		
		<logic:notEqual name="pointOfContactForm" property="contactName" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Contact Name:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="contactName" /></TD>
			</TR>
		</logic:notEqual>		
			
		<logic:notEqual name="pointOfContactForm" property="title" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD>
					<STRONG>Role/Title:</STRONG>
				</TD>
				<TD><bean:write name="pointOfContactForm" property="title" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="phone" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Phone Number:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="phoneFormated" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="fax" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Fax Number:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="faxFormated" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="address1" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Address:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="address1" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="address2" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Address 2:</STRONG></TD>
     			<TD><bean:write name="pointOfContactForm" property="address2" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="city" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>City:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="city" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="stateId" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>State:</STRONG>	</TD>
				<TD><bean:write name="pointOfContactForm" property="stateId" /></TD>
			</TR>
		</logic:notEqual>
	
		<logic:notEqual name="pointOfContactForm" property="countyName" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>County:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="countyName" /></TD>
			</TR>
		</logic:notEqual>
		
		<logic:notEqual name="pointOfContactForm" property="zip" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Zip Code:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="zip" /></TD>
			</TR>
		</logic:notEqual>

		<logic:notEqual name="pointOfContactForm" property="email" value="">
			<TR>
			    <TD width="1%"></TD>  
				<TD><STRONG>Email:</STRONG></TD>
				<TD><bean:write name="pointOfContactForm" property="email" /></TD>
			</TR>
		</logic:notEqual>
	
		<TR>
			<td colspan="6">
				<INPUT type="button" name="hideDetails" onclick="javascript: setActionAndSubmit('<%=PointOfContactAction.ACTION_DEFAULT_QUERY%>')" value="Cancel"> 
			</td>
		</TR>
	</TABLE>    
<%} %>

</DIV>

</pdk-html:form>
