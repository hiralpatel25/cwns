<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<TABLE class="PortletText1" border="0" cellpadding="3" cellspacing="3"
	width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid;">
	<tr class="PortletHeaderColor">
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			Animal Holding
		</td>
	</tr>
	<tr class="PortletSubHeaderColor">
		<td>
			Aquaculture
		</td>
	</tr>
</table>