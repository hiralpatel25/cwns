
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.portal.provider.v2.url.UrlUtils"
	import="gov.epa.owm.mtb.cwns.facilityInformation.FacilityInformationForm"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"
	import="gov.epa.owm.mtb.cwns.model.Facility"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"	
	%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    NameValue[] linkParams       = new NameValue[1];
    Facility facility = (Facility)request.getAttribute("facility");
%>

<LINK REL=stylesheet TYPE="text/css" HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,"../../css/cwns.css")%>">

		<div class="PortletText1">
			<FONT size="4"><STRONG>New Facility/Project</STRONG></FONT>
		</div>
		<br></br>
	     <table width="100%" border="0" cellpadding="0" cellspacing="0">
	     	<tr>
	     		<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/info24.gif" alt="Save sucessfull icon" border="0"/>	
	     		</td>
	     		<td class="InformationText" align="left" valign="middle">
					Facility/Project "<bean:write name="facility" property="name"/>" has been saved successfully!!<br>Click next to continue.	     		
				</td>
	     	</tr>
	     	<tr>	     
				<td colspan="2" class="PortletText1">
		   			<br><br></br>
		   			<%
 			 			linkParams[0] = new NameValue("facilityId", facility.getFacilityId()+"");
 		   			%>
		   			<A href='<%=CWNSEventUtils.constructEventLink(prr,"FacilityInfoPage", linkParams,true, true)%>'>
		      			Next
		   			</A> 
		   
				</td>
			</tr>
		</table>			
