<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.webdb.provider.v2.struts.StrutsUtils"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
%>

<logic:present name="xml">
  <%=(String)request.getAttribute("xml")%>
</logic:present>
