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

<STRONG><FONT size="4" face="Arial">Operation
			and Maintenance Costs</FONT>
</STRONG>
	<TABLE cellspacing="3" cellpadding="2" border="0" width="100%"
		class="PortletText1"
		style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		<TR class="PortletHeaderColor">
			<TD width="1%">
				<P align="center">
					<STRONG><FONT color="#ffffff">Year</FONT>&nbsp;</STRONG>
				</P>
			</TD>
			<TD>
				<P align="center">
					<STRONG><FONT color="#ffffff">Category</FONT>
					</STRONG>
				</P>
			</TD>
			<TD>
				<DIV align="center">
					<STRONG><FONT color="#ffffff">Plant Cost</FONT>
					</STRONG>
				</DIV>
			</TD>
			<TD>
				<P align="center">
					<STRONG><FONT color="#ffffff">Coll. Cost</FONT>&nbsp;</STRONG>
				</P>
			</TD>
			<TD>
				<P align="center">
					<STRONG><FONT color="#ffffff">Total</FONT>&nbsp;</STRONG>
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				<DIV align="center">
					2001
				</DIV>
			</TD>
			<TD>
				Debt Service
			</TD>
			<TD>
				<DIV align="left">
					$124,000
				</DIV>
			</TD>
			<TD>
				<DIV align="left">
					$276,000
				</DIV>
			</TD>
			<TD>
				<DIV align="left">
					$400,000
				</DIV>
			</TD>
		</TR>
		<TR class="PortletSubHeaderColor">
			<TD>
				<DIV align="center">
					2001
				</DIV>
			</TD>
			<TD>
				Replacement
			</TD>
			<TD>
				<DIV align="left">
					$224,000
				</DIV>
			</TD>
			<TD>
				<DIV align="left">
					$276,000
				</DIV>
			</TD>
			<TD>
				<DIV align="left">
					$500,000
				</DIV>
			</TD>
		</TR>

		<TR style="font-weight: bold; background-color: #d3d3d3">
			<TD align="center">
				2001
			</TD>
			<TD>
				Total
			</TD>
			<TD>
				$348,000
			</TD>
			<TD>
				$552,000
			</TD>
			<TD>
				$900,000
			</TD>
		</TR>
		<TR>
			<TD>
				<DIV align="center">
					2000
				</DIV>
			</TD>
			<TD>
				Debt Service
			</TD>
			<TD>
				$90,00
			</TD>
			<TD>
				$120,000
			</TD>
			<TD>
				<DIV align="left">
					$210,000
				</DIV>
			</TD>
		</TR>
		<TR style="font-weight: bold; background-color: #d3d3d3">
			<TD align="center">
				2000
			</TD>
			<TD>
				Total
			</TD>
			<TD>
				$90,000
			</TD>
			<TD>
				$120,000
			</TD>
			<TD>
				$210,000
			</TD>
		</TR>
		<TR id="omCost" style="display:none">
			<TD>
				<SELECT size="1" name="year">
					<OPTION value="2004">
						2004
					</OPTION>
					<OPTION value="2003">
						2003
					</OPTION>
					<OPTION value="2002">
						2002
					</OPTION>
					<OPTION value="2001">
						2001
					</OPTION>
					<OPTION value="2000">
						2000
					</OPTION>
					<OPTION value="1999">
						1999
					</OPTION>
					<OPTION value="1998">
						1998
					</OPTION>
					<OPTION value="1997">
						1997
					</OPTION>
				</SELECT>
			</TD>
			<TD>
				<SELECT size="1" name="year">
					<OPTION value="Debt Service">
						Debt Service
					</OPTION>
					<OPTION value="Maintenance">
						Maintenance
					</OPTION>
					<OPTION value="Operation">
						Operation
					</OPTION>
					<OPTION value="Other">
						Other
					</OPTION>
					<OPTION value="Replacement">
						Replacement
					</OPTION>
				</SELECT>
			</TD>
			<TD>
				<INPUT type="text" name="Plant Cost">
			</TD>
			<TD>
				<INPUT type="text" name="Collection Cost">
			</TD>
			<TD></TD>

			<TD align="center">
				<pdk-html:img page="/images/delete.gif"/>
			</TD>
		</TR>
	</TABLE>