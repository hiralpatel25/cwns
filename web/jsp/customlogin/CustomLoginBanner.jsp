
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
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>
<%@ page import="oracle.portal.provider.v2.render.RenderContext"%>	

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

			String loginUrl = CWNSProperties.getProperty("custom.login.jsp.location");


		RenderContext rc = prr.getRenderContext();
	    String infraURL = rc.getLoginServerURL();
	    String m_strInfraURL = infraURL.substring(0, infraURL.indexOf("/", infraURL.indexOf("://") + 3));	
                
	System.out.println("m_strInfraURL = "+m_strInfraURL);	
	
	
	
	
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/epa.css")%>">
	<!--[if lt IE 7]>
	<LINK REL=stylesheet TYPE="text/css"
		HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
								"../../css/ie.css")%>">
	<![endif]-->


<!-- <div id="header" style="DISPLAY:none"> <!-- START EPA HEADER --> 
<div id="header"> <!-- START EPA HEADER -->

	<div id="logo"><a href="http://www.epa.gov/" title="US EPA home page"><img src="http://www.epa.gov/epafiles/images/logo_epaseal.gif" alt="[logo] US EPA" width="100" height="110" /></a></div>

	<div id="areaname"> <!-- START AREA NAME -->
		<p style="font-size:1.25em;">Clean Watersheds Needs Survey (CWNS)</p>
	</div> <!-- END AREA NAME -->

<br>
<br>		
	<ul> <!-- BEGIN BREADCRUMBS -->
		<li class="first">You are here: <a href="http://www.epa.gov/">EPA Home</a></li>
		<!-- START AREA BREADCRUMBS -->
                    <li> <a href="http://www.epa.gov/ow/ ">Water</a></li> 
					<li> <a href="http://www.epa.gov/owm/ ">Wastewater</a></li>
					<li> <a href="http://epa.gov/cwns/">CWNS</a></li> 
                    <li><a href="http://epa.gov/cwns/">CWNS 2008</a></li>
                    <li><a href="<%=loginUrl%>">Login</a></li>
                    <li>Registration</li>
</ul> <!-- END BREADCRUMBS -->

</div> <!-- END EPA HEADER -->

	