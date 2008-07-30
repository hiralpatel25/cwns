<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.facilityComments.FacilityCommentsForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>
<%@ page import="gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper"%>

<%
			PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

	String facilityId = request.getParameter("facilityId");
	String dataAreaType = request.getParameter("dataAreaType");
	String dataAreaHeader = request.getParameter("dataAreaHeader");

	String dataAreaTypeJS = dataAreaType.replaceAll(" ", "_");
	String dataAreaHeaderJS = dataAreaHeader.replaceAll(" ", "_");
	String helpKey = (String)request.getAttribute("helpKey");
	FacilityCommentsForm facilityCommentsForm = (FacilityCommentsForm) request
			.getAttribute("facilityCommentsForm");
			
			if(facilityCommentsForm.isFacility() == false)
			{
			 dataAreaHeader = dataAreaHeader.replaceAll("Facility", "Project");
			}
			
	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
			
		String pref = pReq.getRenderContext().getPortletPageReference();
	
	//feedback facility form display
	String fsHtmlReportUrl = "";
	String feedbackFacilityId = "";
	if (request.getAttribute("feedbackFacilityId")!=null){
		feedbackFacilityId = (String)request.getAttribute("feedbackFacilityId");
	}
	if (request.getAttribute("displayFeedbackLink")!=null&&
			request.getAttribute("displayFeedbackLink").toString().equals("true")){
			//Fact sheet display	    	
	    	PopulationHelper ph= (PopulationHelper)request.getAttribute("population");
	    	String dataAreaQueryParameters = (String)request.getAttribute("dataAreaQueryParameters");	    	
	    	
	    	int presentResPop=0;
	    	int presentNonResPop=0;
	    	int projectedResPop=0;
	    	int projectedNonResPop=0;
	    	if(ph!=null){
	    		presentResPop=ph.getPresentResPopulation();
	    		presentNonResPop=ph.getPresentNonResPopulation();
	    		projectedResPop=ph.getProjectedResPopulation();
	    		projectedNonResPop=ph.getProjectedNonResPopulation();
	    	}
	    	String reportUrl = CWNSProperties.getProperty("report.url");
	    	String reportLocation = CWNSProperties.getProperty("report.location");
	    	String fsReportName = CWNSProperties.getProperty("report.facilityfeedback");
	    	String fsReporthtmlFormat = CWNSProperties.getProperty("report.desformat.html");
	    	fsHtmlReportUrl = reportUrl+"&desformat="+fsReporthtmlFormat+"&report="+reportLocation+"/"+fsReportName+"&facility_id=" 
	    							+feedbackFacilityId+"&pUpStrResPop="+presentResPop+"&pUpStrNonResPop="+presentNonResPop
	    							+"&fUpStrResPop="+projectedResPop+"&fUpStrNonResPop="+projectedNonResPop;
	    							
	    	if (dataAreaQueryParameters!=null){
	    		fsHtmlReportUrl += dataAreaQueryParameters;
	    	}	
	}
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pReq,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>
// Removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

// Removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

// Removes leading and ending whitespaces
function trim( value ) {
	
	return LTrim(RTrim(value));
	
}

function getControlWithParent(controlName, tag_Name, tagIDValue)
{
	var x=document.getElementsByName(controlName);
	
	for(k=0;k<x.length;k++)
		{
			var pElement = x[k];
			while(pElement.tagName.toUpperCase() != tag_Name)
				pElement = pElement.parentElement;
			
			//alert("getControlWithParent: - " + controlName + " - " + tag_Name + " - " + tagIDValue + ": " + pElement.id);
			
			if(pElement!=null && pElement.id == tagIDValue)
				return x[k];
		}
	return null;
}

function setControlTextWithParent(controlName, tag_Name, tagIDValue, controlValue)
{
	var elmnt = getControlWithParent(controlName, tag_Name, tagIDValue);
	if (elmnt!=null)
		elmnt.value=controlValue;
}

// Set the value of the "action" hidden attribute
function facilityCommentsSetActionAndSubmit<%=dataAreaTypeJS%>(status)
{
     if(status == "add")
     {
	     var inputCom = getControlWithParent("inputComments", "DIV", "facilityCommentDiv<%=dataAreaTypeJS%>");
	      
	     if(inputCom.value == null || trim(inputCom.value) == "")
	     {
	       alert("Error: The facility comments should not be empty.");
	       return false;
	     }
     } 
     
	 setControlTextWithParent("facilityCommentAct", "DIV", "hidden_fields_<%=dataAreaTypeJS%>", status);
	 //submitForm<%=dataAreaTypeJS%>();
	 return true;
}

function submitForm<%=dataAreaTypeJS%>()
{

	alert("window.document.forms.length - " + window.document.forms.length);
  for (var j = 1 ; j <= window.document.forms.length ; j++) 
  { var form = window.document.forms[j]; 
    alert("windows form " + j + " " + form.name);
  }

	 window.document.facilityCommentsBean.submit();
}

function restoreDBValue<%=dataAreaTypeJS%>()
{
	var dbValue = window.document.getElementById("inputComments_DB_Value_<%=dataAreaTypeJS%>").value;
	
	setControlTextWithParent("inputComments", "DIV", "facilityCommentDiv<%=dataAreaTypeJS%>", dbValue);
	
}

// Show or Hide the add comment area of the Portlet
function showOrHide(id, mode)
{
     window.document.getElementById(id).style.display=mode;
}

// Open help window
function Show<%=dataAreaTypeJS%>Help(popupUrl)
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
    //alert('<%=dataAreaType%>');
  var w = window.open(popupUrl, '<%=dataAreaTypeJS%>', settings);
  if(w != null) 
   w.focus();     
}

</SCRIPT>

<pdk-html:form name="facilityCommentsBean"
	type="gov.epa.owm.mtb.cwns.facilityComments.FacilityCommentsForm"
	action="facilityComments.do">

<bean:define id="dataAreaTypeFromBean" name="facilityCommentsBean" property="dataAreaName"/> 

	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->
	<DIV id="hidden_fields_<%=dataAreaTypeJS%>" style="display:none">
		<pdk-html:text styleId="facilityCommentAct"
			property="facilityCommentAct"
			value="<%=facilityCommentsForm.getFacilityCommentAct()%>" />
		<pdk-html:text styleId="facilityId" property="facilityId"
			value="<%=facilityId%>" />
		<pdk-html:text styleId="dataAreaName" property="dataAreaName"
			value="<%=dataAreaType%>" />
		<pdk-html:text styleId="facilityCommentId"
			property="facilityCommentId"
			value="<%=facilityCommentsForm.getFacilityCommentId()%>" />
		<textarea name="inputComments_DB_Value_<%=dataAreaTypeJS%>"><%=facilityCommentsForm.getInputComments()%></textarea>      
	</DIV>

	<TABLE class="PortletText1" cellpadding="0" cellspacing="0"
		width="100%" border="0">
		<TR>
			<TD align="left">
				<FONT size="4"><STRONG><%=dataAreaHeader%></STRONG> </FONT>
			</TD>

			<TD align="right">
				<FONT class=TabBackgroundText>
				<logic:equal name="displayFeedbackLink" value="true">
				<a href="javascript:void(0);" onclick="ShowFacilityListHelp('<%=fsHtmlReportUrl%>');">
				<pdk-html:img page="/images/feedback.gif" alt="Local User FeedBack" border="0" /></a>&nbsp;|&nbsp;
				</logic:equal>
					<logic:greaterThan name="facilityCommentsBean"
						property="facilityCommentId" value="0">
							<a href="javascript:void(0);"
					           onclick="showOrHide('facilityCommentDiv<%=dataAreaTypeJS%>', 'block')">
						<pdk-html:img page="/images/commentsHi.gif"
							alt="Comments" border="0" /> 
							</a>							
					</logic:greaterThan>		
					<logic:lessThan name="facilityCommentsBean"
						property="facilityCommentId" value="0">
						<logic:equal name="facilityCommentsBean" property="isUpdatable" value="Y">
							<a href="javascript:void(0);"
					           onclick="showOrHide('facilityCommentDiv<%=dataAreaTypeJS%>', 'block')">		
					    </logic:equal>				
						<pdk-html:img page="/images/comments.gif"
							alt="Comments" border="0" /> 
						<logic:equal name="facilityCommentsBean" property="isUpdatable" value="Y">
							</a>
					    </logic:equal>				
					</logic:lessThan>
					        					        				        			 | 
							<A href="javascript:void(0);" onclick='Show<%=dataAreaTypeJS%>Help("<%=url%><bean:message key="<%=helpKey%>"/>")'>
							 <pdk-html:img page="/images/help.gif"
							alt="Help" border="0" /> 
							</A> 
							</FONT>
			</TD>
		</TR>
	</TABLE>
	
	
	<div class="MessageZone">
	     <table width="100%" border="0" cellpadding="0" cellspacing="0">
	      <logic:present name="fesErrors">
	     	<tr>
	     		<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/err24.gif" alt="Data Area Errors" border="0"/>	
	     		</td>
	     		<td class="ErrorText" align="left" valign="middle">
	     			<logic:iterate id="fesError" name="fesErrors">
	        			<bean:define id="msgKey" name="fesError" property="id.errorMessageKey"/> 
	        			<bean:write name="fesError" property="sourceCode"/>: <bean:message key="<%=msgKey %>"/><br>
	     			</logic:iterate>
	     		</td>
	     	</tr>
	      </logic:present>
	      <logic:equal name="facilityCommentsBean" property="isUpdatable" value="Y">
	        <logic:present name="warnFeedBack">
				<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/warn24.gif" alt="Data Area Warnings" border="0"/>	
	     		</td>
	     		<td class="WarningText" align="left" valign="middle">
	        			<bean:message key="error.general.feedback.warning"/><br>
	     		</td>	      	
	        </logic:present> 
	      </logic:equal>
	     </table>
	</div>     

	
	<!-- Feed Back Warning -->
	

	<DIV id="facilityCommentDiv<%=dataAreaTypeJS%>" style="DISPLAY:none">

		<TABLE class="PortletText1" cellpadding="0" cellspacing="0"
			width="100%" border="0">

			<TR class="PortletText1">
				<TD align="center">
					<b>Comments</b>
				</TD>
			</TR>
			<TR class="PortletText1">
				<TD align="center" width="100%">
				<logic:equal name="facilityCommentsBean" property="isUpdatable" value="Y">
					<pdk-html:textarea name="facilityCommentsBean"
						styleId="inputComments" property="inputComments" rows="12"
						cols="66" />
				</logic:equal>		
				<logic:equal name="facilityCommentsBean" property="isUpdatable" value="N">
					<textarea readonly styleId="inputComments" rows="12" cols="66">
					<bean:write name="facilityCommentsBean" property="inputComments"/>
					</textarea>
				</logic:equal>		
				</TD>
			</TR>
			<logic:present name="facilityCommentsBean" property="userId">
			<logic:present name="facilityCommentsBean" property="lastUpdatedTs">
					<TR class="PortletText1">
						<TD align="center">
							Last Updated:&nbsp;
							<b><bean:write name="facilityCommentsBean" property="userId"/></b>
							&nbsp;&nbsp;&nbsp;
							<b><bean:write name="facilityCommentsBean" property="lastUpdatedTs"/></b>				
						</TD>
					</TR>
			</logic:present>
			</logic:present>
			<TR class="PortletText1">
				<TD align="left">
				<logic:equal name="facilityCommentsBean" property="isUpdatable" value="Y">
					<input type="submit"
						onclick="return facilityCommentsSetActionAndSubmit<%=dataAreaTypeJS%>('add')"
						value="Update" />
					&nbsp;&nbsp;
					<logic:greaterThan name="facilityCommentsBean"
						property="facilityCommentId" value="0">
						<input type="submit"
						onclick="return facilityCommentsSetActionAndSubmit<%=dataAreaTypeJS%>('delete')"
							value="Delete" />&nbsp;&nbsp;
    	 			</logic:greaterThan>
    	 			</logic:equal>
					<input type="button"
						onclick="restoreDBValue<%=dataAreaTypeJS%>();showOrHide('facilityCommentDiv<%=dataAreaTypeJS%>', 'none');"
						value="Cancel" />
				</TD>
			</TR>
		</TABLE>
	</DIV>

</pdk-html:form>
