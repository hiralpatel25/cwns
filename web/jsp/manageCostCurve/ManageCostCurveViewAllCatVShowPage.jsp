<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
	    import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveViewAllCatVListHelper"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>

<pdk-html:form 	name="ManageCostCurveViewAllCatVFormBean" 
				type="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm"
				action="manageCostCurveViewAllCatV.do">
	
<TABLE class="PortletText1" cellpadding="1" width="100%" border="1">
	<TR>
		<TD align="center">
			<b>CWNS Number</b>
		</TD>
		<TD align="center">
			<b>Facility Name</b>
		</TD>
		<TD align="center">
			<b>Cost</b>
		</TD>		
	</TR>

	<logic:iterate id="ccCatVHelper" name="ManageCostCurveFormBean"  property="ccCatVHelpers"
				type="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveViewAllCatVListHelper">
	<TR>
		<TD>
			<bean:write name="ccCatVHelper" property="cwnsNumber"/>	                                                      
		</TD>
		<TD>
			<bean:write name="ccCatVHelper" property="facilityName"/>	                                                      
		</TD>
		<TD>$
		<fmt:formatNumber type="currency" pattern="#,##0">
			<bean:write name="ccCatVHelper" property="adjustedAmount"/>	                                                      
		</fmt:formatNumber>
		</TD>
	</TR>
	</logic:iterate>

</TABLE>
</pdk-html:form>