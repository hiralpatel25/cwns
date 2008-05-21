
<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="gov.epa.owm.mtb.cwns.facilitySelect.UMFacilitySelectAction" %>
<%@ page import="gov.epa.owm.mtb.cwns.facilitySelect.UMFacilitySelectForm" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.Facility"%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	
	String formName = prr.getParameter("formId")!=null?prr.getParameter("formId"):"";
	UMFacilitySelectForm fsForm = (UMFacilitySelectForm)request.getAttribute("fsForm");
	String stateId = fsForm.getLocationId();
	Collection facilityList = (Collection)request.getAttribute("facilityList");	
	Iterator fsIter = facilityList.iterator();
%>


<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>


function get_facility() 
{ 
	var selectlist = new Array();	
	var cwnslist = new Array();
	
	var targetSelect = window.opener.document.getElementById("facilityIds");
   	var index = targetSelect.options.length;
	
	if(window.opener && !window.opener.closed) 
	{
		var j = 0;
		
    	for (var i=0; i < document.UMfacilitySelectForm.facility.length; i++)
    	{     
			if (document.UMfacilitySelectForm.facility[i].checked == true) 
			{
				name = document.UMfacilitySelectForm.name[i].value;
				cwns = document.UMfacilitySelectForm.facility[i].value;

				// Make sure the facility is not already associated with this user's role.
				var found = false;
				for (var x = 0; x < index; x++) {
					if (targetSelect.options[x].value == cwns) {
						found = true;
					}
				}
				
				if (found == false) {	// Associate the facility with the user's Role		
					selectlist[j] = name; 
					cwnslist[j] = cwns;
					j++;
				}
			}
   		}
   		if (document.UMfacilitySelectForm.facility.checked == true){
   			name = document.UMfacilitySelectForm.name.value;
			cwns = document.UMfacilitySelectForm.facility.value;
			
			// Make sure the facility is not already associated with this user's role.
				var found = false;
				for (var x = 0; x < index; x++) {
					if (targetSelect.options[x].value == cwns) {
						found = true;
					}
				}
				
				if (found == false) {	// Associate the facility with the user's Role		
					selectlist[j] = name; 
					cwnslist[j] = cwns;
					j++;
				}
   		}
		
   	    // Add the facilities to the Parent window
   	 	var k = 0;
   	 	for (var w = index; w < index+j; w++)
   	 	{	
   	 		 	 		
   	 		var oOption = window.opener.document.createElement("OPTION"); 	 		
   	 		oOption.text=selectlist[k];
   			oOption.value=cwnslist[k];
   	 		 	 		
			targetSelect.options[w]= oOption;			
			k++;    	 		
    	}
    }
    window.close();
}


function submitenter(e)
{
 var keycode;
 if (window.event) keycode = window.event.keyCode;
 else if (e) keycode = e.which;
 else return true;
 if (keycode == 13){
	submitForm();
   return false;
 } else
   return true;
}

function submitForm() {
	window.document.UMfacilitySelectForm.submit();
}

function setFocus() {
	window.document.getElementById("keyword").focus();
}
</SCRIPT>


<pdk-html:form method="post" 
	name="UMfacilitySelectForm" 
	type="gov.epa.owm.mtb.cwns.facilitySelect.UMFacilitySelectForm" 
	styleId="UMfacilitySelectForm"
	action="UMfacilitySelect.do">

	<%-- Hidden Fields --%>
	<pdk-html:hidden name="UMfacilitySelectForm" property="action" value="<%=UMFacilitySelectAction.ACTION_SEARCH%>"/>
	<pdk-html:hidden name="UMfacilitySelectForm" property="locationId"/>

	<h3>Select a Facility</h3>

	<TABLE id="ftbl" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>State</td>
			<td></td>
			<TD colspan="2">
<%-- 
				<pdk-html:select size="1"  name ="UMfacilitySelectForm" styleId="locationId" property="locationId" >  
--%>
					<logic:iterate id="state" name="states" type="Entity">
			  		<logic:match name="state" property="key" value='<%=stateId%>'>				
	 					<bean:write name="state" property="value"/>
	 				</logic:match>
<%-- 
			  		<logic:notMatch name="state" property="key" value='<%=stateId%>'>
	 					<OPTION value='<bean:write name="state" property="key"/>'> <bean:write name="state" property="value"/></OPTION> 
	 				</logic:notMatch>
--%>
			        </logic:iterate>
			</TD>
		</tr>
	
		<tr>
            <td valign="bottom">
               Keyword
            </td>
            <td>&nbsp;</td>
            <td colspan="2" valign="top">
			   <pdk-html:text name="UMfacilitySelectForm" styleId="keyword" property="keyword" onkeypress="submitenter(event);" size="20" maxlength="50" />
				<A href="javascript: submitForm()"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>               			   
            </td>    
		</tr>

		<tr><td colspan="4"><hr></td></tr>

		<tr>
			<td colspan="3"><center>
			    <bean:write name="UMfacilitySelectForm" property="displaySize"/> of 
		    	<bean:write name="UMfacilitySelectForm" property="listSize"/> Facilities
	    	</center></td>
	    	<td>
		  		<input type="button" name="Select" value="Select" onclick="get_facility()"/>
	    	</td>
	    </tr>

		<tr><td colspan="4">&nbsp;</td></tr>

		<TR class="PortletSubHeaderColor">
				<TH class="PortletSubHeaderText" align="center" width="10%">
					Select
				</TH>
				<TH></TH>
				<TH class="PortletSubHeaderText" align="left">
					CWNS Number
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					Facility
				</TH>
				
			</TR>
	
			<c:set var="i" value="0" />
			
			<%int indx = 0;
			  while (fsIter.hasNext()) { 
			  Facility facility = (Facility)fsIter.next();%>
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
				<TR class="<c:out value="${class}"/>">
					<TD align="center">
					    <input type="checkbox" name="facility" value="<%=facility.getFacilityId()%>">
					</TD>
					<TD></TD>
					<TD align="left">
						<FONT class="PortletText1">
						<%=facility.getCwnsNbr()%>&nbsp;&nbsp;&nbsp;
						</FONT>
						<input type="hidden" value="<%=facility.getName()%>" name="name">
					</TD>
					<TD align="left">
						<FONT class="PortletText1">
						<%=facility.getName()%>&nbsp;&nbsp;&nbsp;
						</FONT>
					</TD>
				</tr>
				
				<c:set var="i" value="${i+1}" />
				
			<%}%>
			
		
		    <TR><td colspan="2"></td>
		       <TD>
		        <br>
		  		<input type="button" name="Select" value="Select" onclick="get_facility()"/>
		       </TD>
		    </TR>
			
	</TABLE>

</pdk-html:form>	
<body onload="setFocus();"><br>  </body>
						