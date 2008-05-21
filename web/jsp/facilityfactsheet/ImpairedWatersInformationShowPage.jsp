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
	<TR>
		<TD colspan="9">
			<br>
			<div style="float: left" width="300">
				<STRONG><FONT size="4">303(d) Impaired Waters
						Information</FONT>
				</STRONG>
				<FONT size="1"></FONT>
			</div>
		</TD>
	</TR>
	<TR></TR>
	<TR class="PortletHeaderColor">
		<TD width="80">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> List ID </FONT>
				</STRONG>
			</P>
		</TD>
		<TD>
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Water Body Name </FONT>
				</STRONG>
			</P>
		</TD>
		<TD>
			<P align="center">
				<STRONG><FONT color="#ffffff">Cause</FONT>
				</STRONG>
			</P>
		</TD>

		<TD>
			<P align="center">
				<STRONG> <FONT color="#ffffff">Potential Source of
						Impairment</FONT> &nbsp;</STRONG>
			</P>
		</TD>
		<TD>
			<P align="center">
				<STRONG> <FONT color="#ffffff"> TMDL Name &nbsp;</FONT> </STRONG>
			</P>
		</TD>
		<TD>
			<P align="center">
				<STRONG> <FONT color="#ffffff"> TMDL&nbsp;Status
						&nbsp;</FONT> </STRONG>
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			<A
				href="http://oaspub.epa.gov/tmdl/enviro.control?p_list_id=VAW-L08R-02&p_cycle=1998">VAW-L08R-02</A>
		</TD>
		<TD>
			North Fork BlackWater River
			<A
				href="http://map8.epa.gov/scripts/esrimap.dll?name=NHDMAPPER&amp;Cmd=ZoomInByEntity&amp;th=0.3&amp;im=on&amp;SYS=303D&amp;EntityID=VAW-L08R-02">
				<pdk-html:img page="/images/map.gif" alt="map" border="0" />
			</a>
		</TD>
		<TD>
			Fecal Coliform
			<br>
			Pathogens
		</TD>
		<TD></TD>
		<TD>

			<A
				href="http://oaspub.epa.gov/tmdl/waters_list.tmdl_report?p_tmdl_id=20479">
				North Fork Blackwater River </A>
		</TD>
		<TD>
			Established
		</TD>
	</TR>
</TABLE>