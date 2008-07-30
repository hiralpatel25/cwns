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

<STRONG> <FONT size="4"> <FONT size="4" face="Arial">
			Facility&nbsp;Fact Sheet&nbsp; </FONT>
</FONT>
</STRONG>
<A href="javascript:void(0);"><pdk-html:img page="/images/edit.gif" title="edit"
		border="0"/> <FONT class="PortletText1">Edit </FONT>
</A>
<A href="javascript:void(0);"><pdk-html:img page="/images/print.gif" alt="Print" border="0"/>
	<FONT class="PortletText1">Print </FONT>
</A>
<P></P>
<TABLE class="PortletText1" cellspacing="2" cellpadding="3" width="100%">
	<TR>
		<TD>
			<STRONG> Name: </STRONG>
		</TD>
		<TD>
			<%=prr.getAttribute("facilityName")%>
		</TD>
		<TD>
			&nbsp;
		</TD>
		<TD>
			<B>Review Status:</B> 
		</TD>
		<TD>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG>Description</STRONG>
		</TD>
		<TD>
			<%=prr.getAttribute("desc") %>
		</TD>
		<TD>
			&nbsp;
		</TD>
		<TD>
			<STRONG> CWNS Number: </STRONG> &nbsp;<%=prr.getAttribute("cwnsNbr") %>
		</TD>
		<TD>
			&nbsp; &nbsp;
		</TD>
	</TR>

	<TR>
		<TD></TD>
		<TD>
			&nbsp; &nbsp;
		</TD>
		<TD></TD>
		<TD>
			<STRONG> System Name: </STRONG>
		</TD>
		<TD></TD>
	</TR>
	<TR>
		<TD colspan="5">
			<INPUT type="checkbox" value="on" disabled="disabled">
			<STRONG>Privately Owned</STRONG>
			<INPUT type="checkbox" value="on" disabled="disabled">
			<STRONG>Military</STRONG>
			<INPUT type="checkbox" value="on" disabled="disabled">
			<STRONG>Federal Owned</STRONG>
		</TD>
	</TR>
</TABLE>