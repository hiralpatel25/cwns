
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
<%@ page import="gov.epa.owm.mtb.cwns.facilitySelect.FacilitySelectAction" %>
<%@ page import="gov.epa.owm.mtb.cwns.facilitySelect.FacilitySelectForm" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.Facility"%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	
	String formName = prr.getParameter("formId")!=null?prr.getParameter("formId"):"";
	FacilitySelectForm fsForm = (FacilitySelectForm)request.getAttribute("fsForm");
	String stateId = fsForm.getLocationId();
	Collection facilityList = (Collection)request.getAttribute("facilityList");	
	Iterator fsIter = facilityList.iterator();
%>


<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>

function get_facility() { 
   if(window.opener && !window.opener.closed) 
   {
     var f = -1;
     var onlyOneFacilityDisplayed = false;
     
     if (document.facilitySelectForm.facility.checked) {
     	onlyOneFacilityDisplayed = true;
     }

     for (var i=0; i < document.facilitySelectForm.facility.length; i++){
         if (document.facilitySelectForm.facility[i].checked) {
            f = i;
         }
     }

     if (f == -1 && !onlyOneFacilityDisplayed){
     	alert('You must select a facility');
     	return false;
     } 
     
     if (onlyOneFacilityDisplayed) {
	     cwns_nbr = document.facilitySelectForm.cwnsNbr.value;
    	 name 	  = document.facilitySelectForm.name.value;
	     desc     = document.facilitySelectForm.description.value;
     
     } else {
	     cwns_nbr = document.facilitySelectForm.cwnsNbr[f].value;
    	 name 	  = document.facilitySelectForm.name[f].value;
	     desc     = document.facilitySelectForm.description[f].value;
     }


     window.opener.document.getElementById('cwnsDischargeNumber').innerHTML=cwns_nbr;
     window.opener.document.getElementById('d_cwnsNumber').value=cwns_nbr;
     
     window.opener.document.getElementById('cwnsDischargeName').innerHTML=name;
     window.opener.document.getElementById('d_name').value=name
	 if (desc != 'null') {
	     window.opener.document.getElementById('facilityDischargeDescription').innerHTML=desc;
	 } else {
	     window.opener.document.getElementById('facilityDischargeDescription').innerHTML='';
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
	window.document.facilitySelectForm.submit();
}

function setFocus() {
	window.document.getElementById("keyword").focus();
}
</SCRIPT>


<pdk-html:form method="post" 
	name="facilitySelectForm" 
	type="gov.epa.owm.mtb.cwns.facilitySelect.FacilitySelectForm" 
	styleId="facilitySelectForm"
	action="facilitySelect.do">

	<%-- Hidden Fields --%>
	<pdk-html:hidden name="facilitySelectForm" property="action" value="<%=FacilitySelectAction.ACTION_SEARCH%>"/>

	<h3>Select a Facility</h3>

	<TABLE cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>State</td>
			<td></td>
			<TD colspan="2">
				<pdk-html:select size="1"  name ="facilitySelectForm" styleId="locationId" property="locationId" >  
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
		</tr>
	
		<tr>
            <td valign="bottom" colspan="2">
               Facility Name<br>keyword&nbsp;
            </td>
            
            <td colspan="2" valign="top">
			   <pdk-html:text name="facilitySelectForm" styleId="keyword" property="keyword" onkeypress="submitenter(event);" size="20" maxlength="50" />
				<A href="javascript: submitForm()"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>               			   
            </td>    
		</tr>

		<tr><td colspan="4"><hr></td></tr>

		<tr>
			<td colspan="3"><center>
			    <bean:write name="facilitySelectForm" property="displaySize"/> of 
		    	<bean:write name="facilitySelectForm" property="listSize"/> Facilities
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
					    <input type="radio" name="facility" value="<%=indx%>">
					</TD>
					<TD></TD>
					<TD align="left">
						<FONT class="PortletText1">
						<%=facility.getCwnsNbr()%>&nbsp;&nbsp;&nbsp;
						</FONT>
						<input type="hidden" value="<%=facility.getName()%>" name="name">
						<input type="hidden" value="<%=facility.getLocationId()%>" name="loc_id">
						<input type="hidden" value="<%=facility.getCwnsNbr()%>" name="cwnsNbr">
						<input type="hidden" value="<%=facility.getDescription()%>" name="description">
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
		  		<input type="submit" name="Select" value="Select" onclick="get_facility()"/>
		       </TD>
		    </TR>
			
	</TABLE>

</pdk-html:form>	
<body onload="setFocus();"> </body>
