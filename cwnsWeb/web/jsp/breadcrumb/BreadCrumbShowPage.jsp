<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
        import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils" %>
<%@ page
	import="gov.epa.owm.mtb.cwns.breadcrumb.BreadcrumbForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.UserRole"%>

<%
   PortletRenderRequest prr = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
    UserRole currentRole = (UserRole) request.getAttribute("currentRole");
%>

<div class="PortletText1" style="float: right">
<logic:present name="facility">
    <logic:equal name="isFacility" value="true">
        Facility:&nbsp;<bean:write name="BreadcrumbFormBean" property="cwnsNbr"/>&nbsp;-&nbsp;<bean:write name="BreadcrumbFormBean" property="facilityName"/>	
    </logic:equal>
    <logic:notEqual name="isFacility" value="true">
        Project:&nbsp;<bean:write name="BreadcrumbFormBean" property="cwnsNbr"/>&nbsp;-&nbsp;<bean:write name="BreadcrumbFormBean" property="facilityName"/>	
    </logic:notEqual>
	
</logic:present>
</div>

<div class="PortletText1" style="float: left">
<logic:equal name="currentRole" property="locationTypeId" value="Federal">
<A href="<%=CWNSEventUtils.constructEventLink(prr,"CWNSFederalHomePage", null,true,true)%>">home</A>
</logic:equal>
<logic:equal name="currentRole" property="locationTypeId" value="State">
<A href="<%=CWNSEventUtils.constructEventLink(prr,"CWNSStateHomePage", null,true,true)%>">home</A>
</logic:equal>
<logic:equal name="currentRole" property="locationTypeId" value="Regional">
<A href="<%=CWNSEventUtils.constructEventLink(prr,"CWNSRegionalHomePage", null,true,true)%>">home</A>
</logic:equal>
<logic:equal name="currentRole" property="locationTypeId" value="Local">
<A href="<%=CWNSEventUtils.constructEventLink(prr,"CWNSLocalHomePage", null,true,true)%>">home</A>
</logic:equal>
<logic:present name="pageName">
       &nbsp;>&nbsp;
       <bean:write name="pageName"/>
</logic:present>

</div>