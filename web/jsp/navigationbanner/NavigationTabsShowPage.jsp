
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.RenderContext"
	import="java.net.URLEncoder" import="javax.servlet.http.HttpSession"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"%>

<%@ page language="java" session="false"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page
	import="gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListForm"%>
<%@ page
	import="gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListHelper"%>
<%@ page
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%
			PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

	RenderContext rc = pReq.getRenderContext();

	String pageURL = rc.getPageURL();

	String m_strPortalURL = pageURL.substring(0, pageURL.indexOf("/",
			pageURL.indexOf("://") + 3));

	String infraURL = rc.getLoginServerURL();

	String m_strInfraURL = infraURL.substring(0, infraURL.indexOf("/",
			infraURL.indexOf("://") + 3));

	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();

	HttpSession httpSess = request.getSession();

	NavigationTabsListForm navigationTabsListForm = (NavigationTabsListForm) request
			.getAttribute("navigationTabsForm");
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pReq,
							"../../css/cwns.css")%>">

<pdk-html:form name="navigationTabsListBean"
	type="gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListForm"
	action="navigationTabs.do">

	<%
		long tmpFacId = -1;
		tmpFacId = navigationTabsListForm.getFacilityID();

		NameValue[] linkParams = new NameValue[2];
		linkParams[0] = new NameValue("facilityId", Long.toString(tmpFacId));

		String tabLinkUrl = null;
	%>
	<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0">

		<TR>
			<TD>
				&nbsp;
			</TD>

			<logic:iterate id="navTabsHelper" name="navigationTabsListBean"
				property="navigationTabsHelpers"
				type="gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListHelper">

				<logic:equal name="navTabsHelper" property="active" value="disabled">
					<TD>
						<TABLE BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0"
							CELLSPACING="0">
							<TR>
								<TD class="LeftTabForegroundSlantDisabled" valign="top" align="left"
									width="10" height="19" NOWRAP="">
									&nbsp;
								</TD>
								<TD class=TabForegroundColorDisabled valign="middle" NOWRAP>
									&nbsp;
									<FONT class=TabForegroundTextDisabled>
									<logic:equal name="navigationTabsListBean" property="facility" value="true">
										<bean:write name="navTabsHelper" property="tabText" />
									</logic:equal>
									<logic:equal name="navigationTabsListBean" property="facility" value="false">
										<logic:equal name="navTabsHelper" property="tabText" value="Facility">
											Project
										</logic:equal>
										<logic:notEqual name="navTabsHelper" property="tabText" value="Facility">
											<bean:write name="navTabsHelper" property="tabText" />
										</logic:notEqual>										
									</logic:equal>
									
									</FONT>&nbsp;
								</td>
								<TD align="right" class="RightTabForegroundCurveDisabled" width="8"
									NOWRAP="">
									&nbsp;
								</TD>
							</TR>
						</TABLE>
					</TD>
				</logic:equal>

				<logic:equal name="navTabsHelper" property="active" value="true">
					<TD>
						<TABLE BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0"
							CELLSPACING="0">
							<TR>
								<TD class="LeftTabForegroundSlant" valign="top" align="left"
									width="10" height="19" NOWRAP="">
									&nbsp;
								</TD>
								<TD class=TabForegroundColor valign="middle" NOWRAP>
									&nbsp;
									<FONT class=TabForegroundText>
									<logic:equal name="navigationTabsListBean" property="facility" value="true">
										<bean:write name="navTabsHelper" property="tabText" />
									</logic:equal>
									<logic:equal name="navigationTabsListBean" property="facility" value="false">
										<logic:equal name="navTabsHelper" property="tabText" value="Facility">
											Project
										</logic:equal>
										<logic:notEqual name="navTabsHelper" property="tabText" value="Facility">
											<bean:write name="navTabsHelper" property="tabText" />
										</logic:notEqual>										
									</logic:equal>
									
									</FONT>&nbsp;
								</td>
								<TD align="right" class="RightTabForegroundCurve" width="8"
									NOWRAP="">
									&nbsp;
								</TD>
							</TR>
						</TABLE>
					</TD>
				</logic:equal>

				<logic:equal name="navTabsHelper" property="active" value="false">
					<bean:define id="param1" name="navTabsHelper" property="tabText" />
					<%
							String tmpStr = (String) param1;

							linkParams[1] = new NameValue("navigationTabType", 
									tmpStr.replaceAll(" & ", "_AND_").replaceAll(" ", "_"));

							String eventStr = tmpStr.replaceAll(" & ", "_AND_");
							eventStr = eventStr.replaceAll(" ", "_");
							eventStr = eventStr + "_Tab";
							
							tabLinkUrl = CWNSEventUtils.constructEventLink(pReq,
							eventStr, linkParams, true, true);
					%>

					<TD>
						<TABLE BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0"
							CELLSPACING="0">
							<TR>
								<TD class="LeftTabBackgroundSlant" valign="top" align="left"
									width="10" height="19" NOWRAP="">
									&nbsp;
								</TD>
								<td class=TabBackgroundColor valign="middle" nowrap
									class="TabBackgroundColor">
									&nbsp;
									<FONT class=TabBackgroundText><A HREF="<%=tabLinkUrl%>">
									<logic:equal name="navigationTabsListBean" property="facility" value="true">
										<bean:write name="navTabsHelper" property="tabText" />
									</logic:equal>
									<logic:equal name="navigationTabsListBean" property="facility" value="false">
										<logic:equal name="navTabsHelper" property="tabText" value="Facility">
											Project
										</logic:equal>
										<logic:notEqual name="navTabsHelper" property="tabText" value="Facility">
											<bean:write name="navTabsHelper" property="tabText" />
										</logic:notEqual>										
									</logic:equal>
									</A> </FONT>&nbsp;
								</td>
								<TD align="right" class="RightTabBackgroundCurve" width="8"
									NOWRAP="">
									&nbsp;
								</TD>
							</TR>
						</TABLE>
					</TD>
				</logic:equal>

			</logic:iterate>
		</TR>		
	</TABLE>

	<DIV class=TabForegroundColor style="width:100%;">
		<logic:equal name="navigationTabsListBean" property="hasLink" value="Y">
		<bean:define id="param2" name="navigationTabsListBean" property="linkText" />
	    <TABLE>		
		<TR>
			<TD align="left">
					<%
							if(((String)param2).indexOf("Cost") < 0)
							{
								linkParams[1] = new NameValue("navigationTabType", "Needs");
								String linkUrlCC = CWNSEventUtils.constructEventLink(pReq, "Needs_Tab", linkParams, true, true);
					%>			
					<A HREF="<%=linkUrlCC%>">
					<FONT class=TabForegroundTextLink>Capital Costs</FONT>
					</A>
					<%
							}
							else
							{
					%>
					<FONT class=TabForegroundText>Capital Costs</FONT>
					<%
							}
					%>		
					&nbsp;|&nbsp;					
					<%
							if(((String)param2).indexOf("Funding") < 0)
							{
								linkParams[1] = new NameValue("navigationTabType", "Funding_Source_Link");
								String linkUrlFS = CWNSEventUtils.constructEventLink(pReq, "Funding_Source_Link", linkParams, true, true);
					%>			
					<A HREF="<%=linkUrlFS%>">
					<FONT class=TabForegroundTextLink>Funding</FONT>
					</A>
					<%
							}
							else
							{
					%>
					<FONT class=TabForegroundText>Funding</FONT>
					<%
							}
					%>					
					&nbsp;|&nbsp;					
					<%
							if(((String)param2).indexOf("M") < 0)
							{
								linkParams[1] = new NameValue("navigationTabType", "Operation_and_Maintenance_Link");
								String linkUrlOM = CWNSEventUtils.constructEventLink(pReq, "Operation_and_Maintenance_Link", linkParams, true, true);
					%>			
					<A HREF="<%=linkUrlOM%>">
					<FONT class=TabForegroundTextLink>O & M</FONT>
					</A>
					<%
							}
							else
							{
					%>
					<FONT class=TabForegroundText>O & M</FONT>
					<%
							}
					%>					
			</TD>		
		</TR>
		</TABLE>		
		</logic:equal>
	</DIV>

</pdk-html:form>
		