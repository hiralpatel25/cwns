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
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties" %>

<%
   PortletRenderRequest prr = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
    UserRole currentRole = (UserRole) request.getAttribute("currentRole");
    
	String requestedGroups = (String)request.getAttribute("requestedGroups");
%>


<logic:notEmpty name="currentRole">
	<logic:equal name="currentRole" property="locationTypeId" value="Federal">
		<script type="text/javascript">
			window.location = '<%=CWNSEventUtils.constructEventLink(prr,"CWNSFederalHomePage", null,true,true)%>';
		</script>
	</logic:equal>
	<logic:equal name="currentRole" property="locationTypeId" value="State">
		<script type="text/javascript">
			window.location = '<%=CWNSEventUtils.constructEventLink(prr,"CWNSStateHomePage", null,true,true)%>';
		</script>
	</logic:equal>
	<logic:equal name="currentRole" property="locationTypeId" value="Regional">
		<script type="text/javascript">
			window.location = '<%=CWNSEventUtils.constructEventLink(prr,"CWNSRegionalHomePage", null,true,true)%>';
		</script>
	</logic:equal>
	<logic:equal name="currentRole" property="locationTypeId" value="Local">
		<script type="text/javascript">
			window.location = '<%=CWNSEventUtils.constructEventLink(prr,"CWNSLocalHomePage", null,true,true)%>';
		</script>
		
	</logic:equal>
</logic:notEmpty>

<logic:notEmpty name="requestedGroups">
		<script type="text/javascript">
		<!--	
				window.location = "<%=CWNSProperties.getProperty("iam.group.subscription.page")%>?response=<%=requestedGroups%>";
		-->
		</script>
</logic:notEmpty>


