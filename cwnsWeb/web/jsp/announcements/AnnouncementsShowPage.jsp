<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
   String helpKey = (String)request.getAttribute("helpKey");             
%>

<SCRIPT type=text/javascript>
	function ShowAnnouncementHelp(popupUrl)
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

  var w = window.open(popupUrl, 'AnnouncementHelp', settings);
  if(w != null) 
    w.focus();     
}
</SCRIPT>	
 <DIV align="right">
    <FONT class="PortletText1">
      <A href="javascript:void(0);" onclick='ShowAnnouncementHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>  
 </DIV>

<TABLE width="100%" border="0" class="PortletText1">
															<TBODY>
															    <TR align="center" class="PortletSubHeaderColor">
																	<TD>
																		<b>Date</b>
																	</TD>
																	<td>
																		<P align="left">
																			<B>Message</B></P>
																	</td>
																</TR>
																<logic:iterate id="ament" name="announce" type="Entity">
																   <TR>
																	 <td align="center">
																		<bean:write name="ament" property="key"/>
																	 </td>
																	 <td>
																	    <bean:write name="ament" property="value"/> 
																	 </td>
																   </TR>
																</logic:iterate>
																<!-- 
																<TR >
																	<td align="center">
																		04/02/08
																	</td>
																	<td>
																	    Permit Type has been updated 
																	</td>
																</TR>
																<TR class="PortletSubHeaderColor">
																	<td align="center">
																		04/01/08
																	</td>
																	<td>
																	    Construction Cost Index has been updated 
																	</td>
																</TR>
																-->
															</TBODY>
														</TABLE>