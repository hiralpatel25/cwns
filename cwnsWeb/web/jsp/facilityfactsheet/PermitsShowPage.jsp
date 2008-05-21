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

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
	<TR class="PortletHeaderColor">
		<TD class="PortletHeaderText" align="center">
			NPDES
		</TD>
		<TD class="PortletHeaderText" align="center">
			Permit Number
		</TD>
		<TD class="PortletHeaderText" align="center">
			Type
		</TD>
		<TD class="PortletHeaderText" align="center">
			Active
		</TD>
		<TD class="PortletHeaderText" align="center">
			Use Data
		</TD>
	</TR>
	<TR>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD align="center">
			<A href="javascript:void(0);" title="Envirofacts">NJ00020923</A>
		</TD>
		<TD align="center">
			Discharge
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</P>
		</TD>
		<TD>
			<P align="center">
				<A href="javascript:void(0);" title="Envirofacts">NJ00020924</A>
			</P>
		</TD>
		<TD>
			<P align="center">
				Discharge
			</P>
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD>
			<P align="center">
				&nbsp;
			</P>
		</TD>
	</TR>
	<TR>
		<TD></TD>
		<TD>
			<P align="center">
				NJA222123
			</P>
		</TD>
		<TD>
			<P align="center">
				State
			</P>
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD></TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD align="center">
			<A href="javascript:void(0);" title="Envirofacts">NJ0020925</A>
		</TD>
		<TD align="center">
			Discharge
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD></TD>
	</TR>
	<TR><TD colspan="5">&nbsp;</TD></TR>

</TABLE>

