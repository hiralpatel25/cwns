<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
            
%>
<SCRIPT type=text/javascript>
// Open help window
function ShowDocumentsHelp(url){
     var w = 800;
     var h = 500;
	 var winl = (screen.width-w)/2;
	 var wint = (screen.height-h)/2;
	 var settings ='height='+h+',';
	 settings +='width='+w+',';
	 settings +='top='+wint+',';
	 settings +='left='+winl+',';
	 settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

     var w = window.open(url, 'DocumentsHelp', settings);
     if(w != null) 
        w.focus();
   }
</SCRIPT>
<DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='ShowDocumentsHelp("<%=url%><bean:message key="help.documents"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
		</FONT>
	</DIV>

<TABLE width="100%" border=0>
															<TBODY>



																<TR>
																	<TD></TD>
																</TR>
																<TR>
																	<TD align="left" colspan="3" class="PortletText1">
																		<TABLE border="1" cellpadding="1" cellspacing="0" width="100%" class="PortletText1">
																			<TR>
																				<TD>
																					Title
																				</TD>
																				<TD>
																					date
																				</TD>
																				<TD>
																					public
																				</TD>
																			</TR>
																			<TR>
																				<TD>
																					<A href="#">Captial Improvement Plan for passaic County</A>
																				</TD>
																				<TD>
																					04/02/2008
																				</TD>
																				<TD>
																					<P align="center">
																						<IMG src="../images/checkmark.gif">
																					</P>
																				</TD>
																			</TR>
																			<TR>
																				<TD><A href="#"> Sanitary Sewer System Addendum</A></TD>
																				<TD>01/02/2008</TD>
																				<TD>&nbsp;</TD>
																			</TR>
																			
																			<TR>
																				<TD colspan="3">
																				<INPUT type="Button" name="Add" value="Add" />
																				</TD>
																			</TR>
																		</TABLE>
																	</TD>
																</TR>
																<TR>
																</TR>
															</TBODY>
														</TABLE>