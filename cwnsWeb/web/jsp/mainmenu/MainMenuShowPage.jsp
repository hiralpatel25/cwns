<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>
<%@ page import="oracle.portal.utils.NameValue"%>



<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
   PortletRenderRequest prr = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
     NameValue[] linkParams       = new NameValue[1];   
     String library = CWNSProperties.getProperty("ocs.library");        
     String webConference = CWNSProperties.getProperty("ocs.webConference");
     String collaborationSite=CWNSProperties.getProperty("ocs.collaborationSite");
     String askwatersUrl = CWNSProperties.getProperty("url.askwaters","http://iaspub.epa.gov/waters/query_tool.question_list");     
     String permitDocUrl = CWNSProperties.getProperty("url.permit.documentation","http://cfpub.epa.gov/npdes/permitissuance/genpermits.cfm");
     String helpKey = (String)request.getAttribute("helpKey");
%>
<script language="JavaScript">
// Open help window
function showPopUp(popupUrl)
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

  var w = window.open(popupUrl, 'MainMenuHelp', settings);
  if(w != null) 
    w.focus();     
}
</script> 
<DIV align="right">
   <FONT class="PortletText1">
       <A href="javascript:void(0);" onclick='showPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
   </FONT>
</DIV>
  
<TABLE width="100%" border=0>
															<TBODY>
															
															<logic:present name="updateAccess">
														     <logic:equal name="updateAccess" value="Y">
																<TR>
																	<TD align=left colSpan=3>
																		<%
											 							   linkParams[0] = new NameValue("facilityId", "0");
											 							%>
																		<A href='<%=CWNSEventUtils.constructEventLink(prr,"NewFacilityInfoPage", linkParams,true, true)%>'>
																			<FONT class=PortletText1>Create New Facility/Project</FONT>
																		</A>
																	</TD>
																</TR>
															  </logic:equal> 	
															</logic:present>									
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);" onclick='showPopUp("<%=library%>")'>
																		   <FONT class=PortletText1><bean:message key="label.ocs.library"/></FONT>
																		</A>
																	</TD>
																</TR>
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);" onclick='showPopUp("<%=webConference%>")'>
																		   <FONT class=PortletText1><bean:message key="label.ocs.webConference"/></FONT>
																		</A>
																	</TD>
																</TR>
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);" onclick='showPopUp("<%=collaborationSite%>")'>
																		   <FONT class=PortletText1><bean:message key="label.ocs.collaborationSite"/></FONT>
																		</A>
																	</TD>
																</TR>											
																															
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);" onclick='showPopUp("<%=askwatersUrl%>")'>
																		  <FONT class="PortletText1"> AskWATERS</FONT>
																		</A>
																	</TD>
																</TR>
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);" onclick='showPopUp("<%=permitDocUrl%>")'>
																		  <FONT class="PortletText1"> Permit&nbsp;Documentation</FONT>
																		</A>
																	</TD>
																</TR>																
																<!-- permitDocUrl
																<TR>
																	<TD align=left colSpan=3>
																		<A href="javascript:void(0);"><FONT class="PortletText1">Total Population</FONT></A>
																	</TD>
																</TR>
																<TR>
																	<TD>
																		<A href="javascript:void(0);"><FONT class="PortletText1">Import Data</FONT></A>
																	</TD>
																</TR>
																 -->
															</TBODY>
														</TABLE>