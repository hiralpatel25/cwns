
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.RenderContext"
	import="java.net.URLEncoder"
	import="javax.servlet.http.HttpSession"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="gov.epa.owm.mtb.cwns.common.CurrentUser"  
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"  
	import="gov.epa.owm.mtb.cwns.common.UserRole"%>

<%
			PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			RenderContext rc = pReq.getRenderContext();
			
		    String pageURL = rc.getPageURL(); 
	    
		    String m_strPortalURL = pageURL.substring(0, pageURL.indexOf("/", pageURL.indexOf("://") + 3));
		    String infraURL = rc.getLoginServerURL();
		    
		    String m_strInfraURL = infraURL.substring(0, infraURL.indexOf("/", infraURL.indexOf("://") + 3));	
			    
   			String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
               
			String username = "";               
			String location = "";
		    CurrentUser currentUser = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER);
		    if (currentUser != null) {
			    username = currentUser.getFullName();
		    	UserRole currentRole = currentUser.getCurrentRole();
		    	location = currentRole.getLocationId() + " " + currentRole.getLocationTypeId() + " User";
		    }
		    
			String suportAddress = CWNSProperties.getProperty("support.address");

			String customLogoutUrl = "";
			boolean returnToCustomLogin = false;
			if (!"".equals(CWNSProperties.getProperty("custom.logout.url"))) {
				customLogoutUrl = CWNSProperties.getProperty("custom.logout.url");
				returnToCustomLogin = true;
			}
		     
%>
<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%">
	<TR>
		<TD VALIGN="top" style="Background-Color: #353e52" width="5%">
			<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%"
				style="Background-Color: #353e52">
				<TR ALIGN="LEFT">
					<TD width="4">
						<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"
							width="4" />
					</TD>
					<TD vAlign="top" width="100%">
						<pdk-html:img page="/images/banner_seal.gif" border="0" />
						<BR>
					</TD>
					<TD width="4">
						<pdk-html:img page="/images/pobtrans.gif" border="0" height="1" width="4" />
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD VALIGN="top" style="Background-Color: #353e52" width="50%">
			<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%"
				style="Background-Color: #353e52">
				<TR ALIGN="LEFT">
					<TD vAlign="top" width="100%">
						<P>					
								<FONT size="4"><FONT face="Arial" color="#ffffff"><STRONG><SPAN
											style="FONT-SIZE: 18px"><SPAN style="FONT-SIZE: 19px">Welcome
													to the EPA Portal</SPAN>
										</SPAN>
									</STRONG>
								</FONT>
								</FONT>
						</P>
					</TD>
				</TR>
				<TR ALIGN="LEFT">
					<TD vAlign="top" width="100%">
						<SPAN
							style="FONT-WEIGHT: bold; Font-Family: Arial; FONT-SIZE: 19px; COLOR: #ffffff">
							Clean Watersheds Needs Survey (CWNS) 
						</SPAN>
					</TD>
				</TR>
				<TR ALIGN="LEFT">
					<TD vAlign="top" nowrap width="100%">
								<FONT size="3"><FONT face="Arial" color="#ffffff"><STRONG>
									<SPAN style="FONT-SIZE: 16px">
											<%-- pReq.getUser().getName()--%>							
											<%=username %>
									</SPAN>
									</STRONG>
								</FONT>
								</FONT>
					</TD>
				</TR>
				<TR ALIGN="LEFT">
					<TD vAlign="top" nowrap width="100%">
								<FONT size="3"><FONT face="Arial" color="#ffffff"><STRONG>
									<SPAN style="FONT-SIZE: 16px">
									        <%=location %>
											<!-- New York State User-->
									</SPAN>
									</STRONG>
								</FONT>
								</FONT>
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD VALIGN="top" width="45%">
			<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%" style="Background-Color: #353e52">
				<TR ALIGN="RIGHT">
					<TD width="4">
						<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"	width="4" />
					</TD>
					<TD vAlign="top" width="100%">
						<pdk-html:img page="/images/banner_image.jpg" border="0" />
						<BR>
					</TD>
					<TD width="4">
						<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"	width="4" />
					</TD>
				</TR>
			</TABLE>
			<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%">
				<TR>
					<TD VALIGN="top" style="Background-Color: #353e52" width="60%">
					</TD>
					<TD VALIGN="top" style="Background-Color: #353e52">
						<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%"
							style="Background-Color: #353e52" height="0">
							<TR ALIGN="RIGHT">
								<TD width="10" >
									<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"	width="4" />
								</TD>
								<TD vAlign="top" width="100%" nowrap="nowrap" >
									<a href="<%=m_strPortalURL%>/pls/portal/PORTAL.home"> <SPAN
										style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
											EPA Portal Home </SPAN> </a>
								</TD>
								<TD>&nbsp;&nbsp;</TD>
								<TD vAlign="top" >
									<a href="mailto:<%=suportAddress%>?subject=Clean Watersheds Needs Survey - Support" ><SPAN
										style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
											Support </SPAN></a>			
								</TD>
								<TD>&nbsp;&nbsp;</TD>
								
								<TD vAlign="top" width="100%">
								<%if("Local".equalsIgnoreCase(currentUser.getCurrentRole().getLocationTypeId())) {%>
								    <a href="<%=url%>/LocalUserHelpPages/!SSL!/WebHelp/LocalUserHelpPages.htm#Local.Overview.htm" target="_blank"> <SPAN
										style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
											Help </SPAN> </a>
								<%}else{ %>
									<a href="<%=url%>/Help/!SSL!/WebHelp/Help.htm#Overview.htm" target="_blank"> <SPAN
										style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
											Help </SPAN> </a>
								<%} %>			
								</TD>
								<TD width="4">
									<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"
										width="4" />
								</TD>
							</TR>
						</TABLE>
					</TD>
					<TD VALIGN="top" style="Background-Color: #353e52">
						<TABLE border="0" CELLSPACING="0" CELLPADDING="0" width="100%"
							style="Background-Color: #353e52">
							<TR ALIGN="RIGHT">
								<TD width="4">
									<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"
										width="4" />
								</TD>
								<%if (returnToCustomLogin){ %>								
								<TD vAlign="top" nowrap width="100%">
									<a href="<%=customLogoutUrl%>" >    
									<SPAN style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
									Logout 
									</SPAN> 
									</a>
								</TD>
								<%} else { %>
									<TD vAlign="top" nowrap width="100%">
										<a href="<%=m_strInfraURL%>/pls/orasso/orasso.wwsso_app_admin.ls_logout?p_done_url=<%=URLEncoder.encode(m_strPortalURL+"/pls/portal/PORTAL.home", "UTF-8")%>"
										   target="_parent" > 
										<SPAN style="Color: #FFFFFF;Text-Decoration:Underline;Font-Family: Arial;Font-Size: 9pt;Font-Weight: Bold;">
										Logout 
										</SPAN> 
										</a>
									</TD>
								<%} %>
								<TD width="4">
									<pdk-html:img page="/images/pobtrans.gif" border="0" height="1"
										width="4" />
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>