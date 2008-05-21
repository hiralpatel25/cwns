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
	<TABLE border="0" width="100%" class="PortletText1">
		<TR class="PortletHeaderColor">

			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Primary
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center">
					Method
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Present
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Projected
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center">
					Location
				</P>
			</TD>
		</TR>
		<TR>

			<TD>
				<P align="center">
					<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"
						title="present"/>
				</P>
			</TD>
			<TD>
				Outfall To Surface Waters
			</TD>
			<TD>
				<P align="center">
					<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"
						title="present"/>
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;&nbsp;&nbsp;&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					<pdk-html:img page="/images/checkmark.gif" alt="Checkmark"/>
				</P>
			</TD>
		</TR>
		<TR>
			<TD></TD>
		</TR>
		<TR class="PortletSubHeaderColor">
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				&nbsp;Spray irrigation
			</TD>
			<TD>
				<P align="center"></P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center"></P>
			</TD>
		</TR>
	</TABLE>