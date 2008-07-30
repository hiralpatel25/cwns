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

<TABLE class="PortletText1" cellspacing="2" cellpadding="3" width="100%">
	<TR>
		<TD colspan="4">
			<BR>
			<FONT size="4"><STRONG>Needs By Category</STRONG>
			</FONT>
		</TD>
	</TR>
	<TR>
		<TD class="PortletHeaderColor" width="10%">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Category </STRONG> </FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> Description&nbsp; </STRONG> </FONT>
		</TD>
		<TD class="PortletHeaderColor" width="20%">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Federal </STRONG> </FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor" width="20%">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Separate State
						Estimate (SSE) </STRONG> </FONT>
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			I
		</TD>
		<TD>
			Secondary Treatment
		</TD>
		<TD>
			<P align="right">
				$105,000
			</P>
		</TD>
		<TD>
			&nbsp;
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD>
			II
		</TD>
		<TD>
			Advance Treatment
		</TD>
		<TD>
			<P align="right">
				$55,000
			</P>
		</TD>
		<TD align="right">
			$20,000
		</TD>
	</TR>


	<TR>
		<TD>
			IV-B
		</TD>
		<TD>
			New Interceptor Sewers
		</TD>
		<TD>
			<P align="right">
				$200,000
			</P>
		</TD>
		<TD>
			&nbsp;
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD>
			<STRONG> Total </STRONG>
		</TD>
		<TD></TD>
		<TD>
			<P align="right">
				<STRONG> $360,000 </STRONG>
			</P>
		</TD>
		<TD align="right">
			<STRONG> $20,000 </STRONG>
		</TD>
	</TR>
</TABLE>