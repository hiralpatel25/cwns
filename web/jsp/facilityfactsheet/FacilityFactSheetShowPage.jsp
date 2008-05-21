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
	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
	
%>
<SCRIPT type=text/javascript>
// Open help window
function ShowFacilityFactSheetHelp(popupUrl)
{
    var w = 800;
    var h = 500;
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

  var w = window.open(popupUrl, null, settings);
  if(w != null) 
    w.focus();     
}
</SCRIPT>

<%-- TEST CODE TO DISPLAY ALL INPUT PARAMETERS
<p>This portlet's input parameters are...</p>
<table align="left" width="50%" ><tr><td><span class="PortletHeading1">Name</span></td><td><span class="PortletHeading1">Value</span></td></tr>
<%
   ParameterDefinition[] params =
       prr.getPortletDefinition().getInputParameters();

   String name = null;
   String value = null;  
   String[] values = null; 
   for (int i = 0; i < params.length; i++)
   {
       name = params[i].getName();
       values = prr.getParameterValues(name);
       if (values != null)
       {
           StringBuffer temp = new StringBuffer();
           for (int j = 0; j < values.length; j++)
           {
               temp.append(values[j]);
               if (j + 1 != values.length)
               {
                   temp.append(", ");
               }
           }
           value = temp.toString();
       }
       else
       {
           value = "No values have been submitted yet.";
       }
%>
<tr>
  <td><span class="PortletText2"><%= name %></span></td>
  <td><span class="PortletText2"><%= value %></span></td>
</tr>
<%
   }
%>
</table>
 --%>
<!--  END TEST CODE TO DISPLAY ALL INPUT PARAMETERS -->

<!--  
	 <P align="right">
   <FONT class="PortletText1">
        <A href="javascript:void(0);" onclick='ShowFacilityFactSheetHelp("<%=url%><bean:message key="help.facilityfactsheet"/>")'>Help</A>
       <A href="javascript:void(0);">Help</A>
   </FONT>
</P>
-->
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
		<TD width="15%">
			<STRONG> Name: </STRONG>&nbsp;Passaic County - NPS
		</TD>
		
	</TR>
	<TR>
		<TD width="40%">
			<STRONG>Description: </STRONG>
		</TD>
     </TR>
     <TR>		
		<TD>
			<STRONG> CWNS Number: </STRONG> &nbsp;34001055021
		</TD>
		
	</TR>

	<TR>
		
		<TD>
			<STRONG> System Name: </STRONG>
		</TD>
		
	</TR>
	<TR>
	    <TD>
	       <strong> <FONT color="#ff0000"> * </FONT> Owner: </strong>&nbsp;PUBLIC
	    </TD>
	</TR>
</TABLE>
<TABLE cellspacing="0" cellpadding="0" border="0" width="100%"
	style="BORDER-RIGHT: rgb(122,150,223) thin solid; BORDER-TOP: rgb(122,150,223) thin solid; BORDER-LEFT: rgb(122,150,223) thin solid; BORDER-BOTTOM: rgb(122,150,223) thin solid"
	class="PortletText1"></TABLE>
<BR>
<STRONG> <FONT size="4" face="arial"> Facility Type </FONT>
</STRONG>
<TABLE cellspacing="2" cellpadding="3" border="0" width="100%"
	class="PortletText1" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
	<TR>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Type&nbsp; </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> &nbsp;Present </STRONG>
			</FONT>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				&nbsp;
				<FONT color="#ffffff"> &nbsp;&nbsp;&nbsp; <STRONG>
						&nbsp;Projected&nbsp; </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> &nbsp;&nbsp;Change </STRONG>
				</FONT>
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Treatment Plant
		</TD>
		<TD>
			<P align="center"></P>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
				&nbsp;
			</P>
		</TD>
		<TD>
			<P align="center"></P>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
				&nbsp;
			</P>
		</TD>
		<TD>
			Process Improvement
			<BR>
			Replacement
			<BR>
			Rehabilitation
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD>
			Collection: Combined Sewer
		</TD>
		<TD>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
				&nbsp;
			</P>
		</TD>
		<TD>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
				&nbsp;
			</P>
		</TD>
		<TD>
			Process Improvement
			<BR>
			Rehabilitation
		</TD>
	</TR>
	<tr>
		<td colspan="4">
			<b>Comments (10/2/2008):</b> &nbsp; &nbsp; Expanded plant wil allow
			for the abandonment of two other overloaded STPs
		</td>
	</tr>
</TABLE>
<BR>
<STRONG> <FONT size="4" face="arial"> Permits </FONT>
</STRONG>
<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%"
	style=" BORDER-BOTTOM: rgb(122,150,223) thin solid">
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
	<TR>
		<TD colspan="7"></TD>
	</TR>
</TABLE>
<BR>
<FONT size="4" face="arial"> <STRONG> Points of Contact</STRONG>
</FONT>
<TABLE cellspacing="2" cellpadding="3" border="0" width="100%"
	class="PortletText1" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
	<TR>

		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> Responsible Entity </STRONG>
			</FONT>
		</TD>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff"> <STRONG> Authority </STRONG>
			</FONT>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Role/Title </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				&nbsp;
				<STRONG> <FONT color="#ffffff"> Point of Contact </FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Phone </STRONG>
				</FONT>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<FONT color="#ffffff"> <STRONG> Email </STRONG>
				</FONT>
			</P>
		</TD>
	</TR>
	<TR>

		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
		</TD>
		<TD>
			DC Water Resource Commission
		</TD>
		<TD>
			Cognizant Official
		</TD>
		<TD>
			Robert Lukin
		</TD>
		<TD>
			703-444-1150
		</TD>
		<TD>
			<A href="mailto:rlin@gmail.com"> rlin@gmail.com </A>
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">

		<TD>
			<P align="center"></P>
		</TD>
		<TD>
			Portland Water District Authority
		</TD>
		<TD>
			Sludge Commercial Handler

		</TD>
		<TD>
			Greg Sheehy
		</TD>
		<TD>
			451-223-1123
		</TD>
		<TD>
			<A href="mailto:jk@lmco.com"> jk@lmco.com </A>
		</TD>
	</TR>
	<TR>
		<TD colspan="8">
			<P align="left">
				<A href="addpointofcontact.html" title="Add New Responsible Entity"></A>
			</P>
		</TD>
	</TR>
</TABLE>
<BR>
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
<br>
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
	<TR>
		<TD colspan="11">
			<BR>
			<FONT size="4"><STRONG>Needs By Document</STRONG>
			</FONT>
		</TD>
	</TR>
	<TR>
		<TD class="PortletHeaderColor" width="30%">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Document&nbsp; Title
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor" width="%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> &nbsp;Type </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="25%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Publish Date </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="50%">
			<DIV align="center">

				<FONT color="#ffffff"> <STRONG> Author </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="25%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Federal </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="25%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> SSE </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="13%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Submit Updated
						Document </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="25%">
			<DIV align="left">
				<FONT color="#ffffff"> <STRONG> Footnotable </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="20%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> &nbsp;Ongoing
						Dialogue &nbsp; </STRONG>
				</FONT>
			</DIV>
		</TD>
	</TR>
	<TR>
		<TD valign="top" width="30%">
			<P align="left">
				<A href="javascript:void(0);">Capital Plan for NJ</A>
			</P>
		</TD>
		<TD valign="top" align="center" width="%">
			<P align="center">
				<A href="javascript:void(0);" title="Capital Improvement Plan"> 01 </A>
			</P>
		</TD>
		<TD valign="top" align="center" width="">
			<P align="center">
				&nbsp;07/01/2005
			</P>
		</TD>
		<TD valign="top" width="">
			<P align="left">
				NJDEP
			</P>
		</TD>
		<TD valign="top" align="rigth" width="13%">
			$200,000
		</TD>
		<TD valign="top" align="center" width="13%">
			<P align="right">
				&nbsp;
			</P>
		</TD>
		<TD></TD>

		<TD>
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</P>
		</TD>
		<TD width="20%">
			<P align="center"></P>
		</TD>
	</TR>
	<TR></TR>
	<TR class="PortletSubHeaderColor">
		<TD valign="top" width="35%">
			<P align="left">
				NJ Wellhead Protection Plan
			</P>
		</TD>
		<TD valign="top" align="center" width="%">
			<P align="center">
				<A href="javascript:void(0);" title="Sewer System Evaluation Survey"> 03 </A>
			</P>
		</TD>
		<TD valign="top" align="center" width="">
			<P align="center">
				&nbsp;09/10/2004
			</P>
		</TD>
		<TD valign="top" width="">
			<P align="left">
				NJDEP
			</P>
		</TD>
		<TD valign="top" align="center" width="13%">
			$130,000
		</TD>

		<TD valign="top" align="center" width="13%">
			<P align="right">
				$20,000
			</P>
		</TD>
		<TD></TD>

		<TD align="center"></TD>
		<TD width="20%">
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			<A
				href="http://oaspub.epa.gov/tmdl/enviro.control?p_list_id=VAW-L08R-02&amp;p_cycle=1998">North
				Fork BlackWater River (VAW-L08R-02)</A>
		</TD>
		<TD align="center">
			<A href="javascript:void(0);" title="303(d) Impaired Water"> 97 </A>
		</TD>
		<TD align="center">
			09/30/2004
		</TD>
		<TD>
			303(d) Imapired Waters Database
		</TD>
		<TD align="right">
			$30,000
		</TD>
		<TD>
			<P>
				&nbsp;
			</P>
		</TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD valign="top">

			<STRONG>Total</STRONG>
		</TD>
		<TD></TD>
		<TD></TD>
		<TD width="">
			<P align="center">
				&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
			</P>
		</TD>
		<TD valign="top" align="center" width="13%">
			<STRONG>$360,000 </STRONG>
		</TD>
		<TD width="13%">
			<P align="right">
				<STRONG> $20,000</STRONG>
			</P>
		</TD>
		<TD></TD>

		<TD></TD>
		<TD align="center"></TD>
	</TR>
</TABLE>
<BR>
<FONT size="4"><STRONG>Capital Costs</STRONG> </FONT>
<B><BR> <BR> <B><I>Capital Plan for NJ<I></I>
	</I>
</B>
	<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%">
		<TR>
			<TD class="PortletHeaderColor" width="13%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Category </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="25%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Classification </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Cost Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Need Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Base ($) &nbsp;</STRONG> </FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Adjusted ($)
					</STRONG>
					</FONT>
				</DIV>
			</TD>
		</TR>


		<TR class="PortletSubHeaderColor">
			<TD>
				IV-B:New Interceptor Sewers
			</TD>
			<TD width="16%">
				&nbsp;

			</TD>
			<TD valign="top" width="">
				<P align="center">
					C
				</P>
			</TD>
			<TD>
				Federal
			</TD>
			<TD valign="top" width="%">
				<P>
					$190,000
				</P>
			</TD>
			<TD>
				<P align="right">
					&nbsp;$200,000
				</P>
			</TD>
		</TR>
	</TABLE> <BR> <B><I>NJ Wellhead Protection Plan<I></I>
	</I>
</B>
	<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%">
		<TR>
			<TD class="PortletHeaderColor" width="13%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Category </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="25%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Classification </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Cost Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Need Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Base ($) &nbsp;</STRONG> </FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Adjusted ($)
					</STRONG>
					</FONT>
				</DIV>
			</TD>
		</TR>
		<TR>
			<TD valign="top" align="center" width="28%">
				<P align="left">
					&nbsp; I:Secondary Treatement
				</P>
			</TD>
			<TD></TD>
			<TD valign="top" width="">
				<P align="center">
					C
				</P>
			</TD>
			<TD>
				Federal
			</TD>
			<TD valign="top" align="center" width="">
				<P align="left">
					&nbsp;$100,000
				</P>
			</TD>
			<TD>
				<P align="right">
					$105,000
				</P>
			</TD>
		</TR>
		<TR></TR>
		<TR class="PortletSubHeaderColor">
			<TD>
				&nbsp;&nbsp;II:Advanced Treatment
			</TD>
			<TD width="16%">
				Sanitary Sewer Overflow

			</TD>
			<TD valign="top" width="">
				<P align="center">
					D
				</P>
			</TD>
			<TD>
				Federal
			</TD>
			<TD valign="top" width="%">
				<P>
					$20,000
				</P>
			</TD>
			<TD>
				<P align="right">
					&nbsp;$25,000
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				&nbsp;&nbsp;II:Advanced Treatment
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				<P align="center">
					D
				</P>
			</TD>
			<TD>
				SSE
			</TD>
			<TD>
				&amp;18,000
			</TD>
			<TD align="right">
				$20,000
			</TD>
		</TR>

		<TR>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
	</TABLE> <BR> <B><I>North Fork Black Water River<I></I>
	</I>
</B>
	<TABLE class="PortletText1" cellspacing="3" cellpadding="2"
		width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		<TR>
			<TD class="PortletHeaderColor" width="13%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Category </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="25%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Classification </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Cost Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Need Type </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Base ($) &nbsp;</STRONG> </FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> &nbsp;Adjusted ($)
					</STRONG>
					</FONT>
				</DIV>
			</TD>
		</TR>


		<TR class="PortletSubHeaderColor">
			<TD>
				&nbsp;&nbsp;II:Advanced Treatment
			</TD>
			<TD width="16%">
				Sanitary Sewer Overflow

			</TD>
			<TD valign="top" width="">
				<P align="center">
					D
				</P>
			</TD>
			<TD>
				Federal
			</TD>
			<TD valign="top" width="%">
				<P>
					$29,000
				</P>
			</TD>
			<TD>
				<P align="right">
					&nbsp;$30,000
				</P>
			</TD>
		</TR>
	</TABLE> <br> <STRONG><FONT size="4" face="Arial">Operation
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
	</TABLE> <BR> <FONT class="PortletHeading1"><FONT size="2">
			<BR> <FONT size="4"><STRONG>Funding Source</STRONG> </FONT>

			<TABLE cellspacing="3" cellpadding="2" border="0" width="100%"
				class="PortletText1"
				style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
				<TR class="PortletHeaderColor">
					<TD>
						<P align="center">
							&nbsp; &nbsp; &nbsp;
							<STRONG><FONT color="#ffffff">Type</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<P align="center">
							&nbsp; &nbsp; &nbsp;
							<STRONG><FONT color="#ffffff">Agency / Program</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							&nbsp; &nbsp; &nbsp;
							<STRONG><FONT color="#ffffff">Award Date</FONT>
							</STRONG>
						</DIV>
					</TD>
					<TD width="20%">
						<P align="center">
							&nbsp; &nbsp; &nbsp;
							<STRONG><FONT color="#ffffff">Loan Number</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							&nbsp; &nbsp; &nbsp;
							<FONT color="#ffffff"><STRONG>Source</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Total</FONT>
							</STRONG>
						</P>
					</TD>
					<TD align="center">
						<STRONG><FONT color="#ffffff">% Funded <BR>by
								CWSRF</FONT>
						</STRONG>
					</TD>
				</TR>

				<TR>
					<TD>
						<DIV align="center">
							Loan
						</DIV>
					</TD>
					<TD align="center">
						SRF
					</TD>
					<TD>
						<DIV align="center">
							5/21/2001
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<A href="javascript:void(0);" title="Detail Funding Information">cs-35001-00</A>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							Federal
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							$320,000
						</DIV>
					</TD>
					<TD align="center">
						100
					</TD>
				</TR>
				<TR class="PortletSubHeaderColor">
					<TD>
						<DIV align="center">
							Grant
						</DIV>
					</TD>
					<TD align="center">
						SRF
					</TD>
					<TD>
						<DIV align="center">
							6/22/2002
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<A href="javascript:void(0);" title="Detail Funding Information">cs-67002-00</A>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							Federal
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							$100,000
						</DIV>
					</TD>
					<TD align="center">
						60%
					</TD>
				</TR>
				<TR>
					<TD align="center">
						Loan
					</TD>
					<TD align="center">
						State
					</TD>
					<TD align="center">
						10/10/07
					</TD>
					<TD align="center">
						abb-001-1234
					</TD>
					<TD align="center">
						Private
					</TD>
					<TD align="center">
						$20,000
					</TD>
					<TD align="center">
						&nbsp;
					</TD>
				</TR>
				<TR style="font-weight: bold; background-color: #d3d3d3">
					<TD>
						<DIV align="center">
							Total
						</DIV>
					</TD>
					<TD>
						&nbsp; &nbsp; &nbsp;
					</TD>
					<TD>
						&nbsp; &nbsp; &nbsp;
					</TD>
					<TD>
						<DIV align="right">
							&nbsp; &nbsp; &nbsp;
						</DIV>
					</TD>
					<TD>
						<DIV align="right">
							&nbsp; &nbsp; &nbsp;
						</DIV>
					</TD>
					<TD align="center">
						$420,000
					</TD>

					<TD></TD>
				</TR>
			</TABLE> </FONT>
</FONT>
<BR> <STRONG><FONT size="4" face="arial">Population
			Information</FONT>
</STRONG> <FONT size="1"></FONT>

	<TABLE border="0" width="100%" class="PortletText1" cellspacing="3"
		cellpadding="1" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		<TR class="PortletHeaderColor">
			<TD></TD>
			<TD colspan="3" align="center" class="PortletHeaderText">
				Resident Population
			</TD>
			<TD colspan="3" align="center" class="PortletHeaderText">
				Non- Resident Population
			</TD>
		</TR>
		<TR>
			<TD>
				&nbsp; &nbsp;
			</TD>
			<TD>
				<P align="center">
					Present
				</P>
			</TD>
			<TD>
				<P align="center">
					Projected
				</P>
			</TD>
			<TD>
				<P align="center">
					Projected Year
				</P>
			</TD>
			<TD>
				<P align="center">
					Present
				</P>
			</TD>
			<TD>
				<P align="center">
					Projected
				</P>
			</TD>
			<TD>
				Projected Year
			</TD>
		</TR>
		<TR>
			<TD>
				Receiving Collection
			</TD>
			<TD>

				16,758
			</TD>
			<TD>
				23,000
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				Upstream Collection
			</TD>
			<TD>
				<P align="left">
					&nbsp;1,230
				</P>
			</TD>
			<TD>
				<P align="left">
					&nbsp; 1,300
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
		</TR>
		<TR>
			<TD>

				<STRONG> Total Receiving Treatment </STRONG>
			</TD>
			<TD>
				<P align="left">
					<STRONG>&nbsp;17,988</STRONG>
				</P>
			</TD>
			<TD>
				<P align="left">
					<STRONG>&nbsp; 24,300 </STRONG>
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				Decentralized
			</TD>
			<TD>
				&nbsp;200
			</TD>
			<TD>
				&nbsp;230
			</TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD>
				Individual Sewage Disposal System
			</TD>
			<TD>
				230
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
			<TD>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<STRONG> Total (Excluding Upstream) </STRONG>
			</TD>
			<TD>
				<P align="left">
					<STRONG> &nbsp;17,188 </STRONG>
				</P>
			</TD>
			<TD>
				<P align="left">
					<STRONG> 23,300 </STRONG>
				</P>
			</TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD colspan="7">
				<P class="PortletText1">
					<INPUT type="checkbox" name="smallCommunity" value="true"
						disabled="disabled">
					&nbsp;&nbsp;Small Community Exception Flag
				</P>
			</TD>
		</TR>
		<TR>
			<TD colspan="7">
				<B>comments (9/2/2008):</B> Population to be served and Flow rates
				are correct project will restore Coal Brook stream flow.
			</TD>
		</TR>
	</TABLE> <BR>
	<TABLE class="PortletText1"
		style="BORDER-BOTTOM: rgb(122,150,223) thin solid" cellspacing="3"
		cellpadding="1" width="100%">
		<TR>
			<TD colspan="4">
				<STRONG><FONT size="4"></FONT>
				</STRONG>
				<DIV style="FLOAT: left" width="300">
					<STRONG><FONT size="4">Flow Information</FONT>
					</STRONG>
					<FONT size="1"></FONT>
				</DIV>
			</TD>
		</TR>
		<TR class="PortletHeaderColor">
			<TD></TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Existing
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Present Design
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Future Design
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				Municipal Flow (MGD)
			</TD>
			<TD>
				<P align="center">
					1.55
				</P>
			</TD>
			<TD>
				<P align="center">
					2.3
				</P>
			</TD>
			<TD>
				<P align="center">
					2.3
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				Industrial Flow (MGD)
			</TD>
			<TD>
				<P align="center">
					0.25
				</P>
			</TD>
			<TD>
				<P align="center">
					0.5
				</P>
			</TD>
			<TD>
				<P align="center">
					0.5
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				Infiltration Flow (MGD)
			</TD>
			<TD>
				<P align="center">
					0.4
				</P>
			</TD>
			<TD>
				<P align="center">
					0.3
				</P>
			</TD>
			<TD>
				<P align="center">
					0.3
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				<B>Total Flow (MGD)</B>
			</TD>
			<TD>
				<P align="center">
					2.2
				</P>
			</TD>
			<TD>
				<P align="center">
					3.1
				</P>
			</TD>
			<TD>
				<P align="center">
					3.1
				</P>
			</TD>
		</TR>
		<TR>
			<TD>
				Wet Weather Flow (Peak) (MGD)
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
					&nbsp;
				</P>
			</TD>
			<TD>
				<P align="center">
				</P>
			</TD>
		</TR>
		<TR>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD>
				<I>Flow to Population Ratio (GPCD)</I>
			</TD>
			<TD align="center">
				<I>116.36</I>
			</TD>
			<TD align="center">
				<FONT color="#ff0000"><I>155.15</I>
				</FONT>
			</TD>
			<TD align="center">
				<I>113.04</I>
			</TD>
		</TR>
	</TABLE> <BR>
	<DIV style="float: left" width="300">
		<STRONG><FONT size="4" face="Arial">Discharge
				Information</FONT>
		</STRONG>
		<FONT size="1"></FONT>
	</DIV>
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
	</TABLE> </B>
<br>
<br>
<TABLE align="center" border="0" width="100%" class="PortletText1"
	style="BORDER-TOP: rgb(122,150,223) thin solid; BORDER-BOTTOM: rgb(122,150,223) thin solid ">

	<TR>
		<TD colspan="3">
			<BR>
			<DIV style="float: left" width="300">
				<STRONG><FONT size="4">Effluent Information</FONT>
				</STRONG>
				<FONT size="1"></FONT>
			</DIV>
		</TD>
	</TR>
	<TR class="PortletHeaderColor">
		<TD></TD>
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
	</TR>
	<TR>
		<TD>
			Treatment Level
		</TD>
		<TD>
			<P align="center">
				<SELECT name="presentDesignEffluent" disabled="disabled">
					<OPTION value="secondary" selected="true">
						Secondary
					</OPTION>
					<OPTION value="advanceTreatment1">
						Advance Treament
					</OPTION>
				</SELECT>
			</P>
		</TD>
		<TD>
			<P align="center">
				<SELECT name="projectedDesignEffluent" disabled="disabled">
					<OPTION value="secondary" selected="true">
						Secondary
					</OPTION>
					<OPTION value="advanceTreatment1">
						Advance Treament
					</OPTION>
				</SELECT>
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Nitrogen Removal
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" name="presentNutRemoval" value="on"
					disabled="disabled">
			</P>
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Phosphorous Removal
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Ammonia Removal
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" disabled="disabled">
			</P>
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Metal Removal
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" disabled="disabled">
			</P>
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Syn Organic Removal
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" value="on" disabled="disabled">
			</P>
		</TD>
		<TD>
			<P align="center">
				<INPUT type="checkbox" disabled="disabled">
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			Nutrient Removal
		</TD>
		<TD align="center">
			<pdk-html:img page="/images/checkmark.gif"/>
		</TD>
		<TD></TD>
	</TR>
</TABLE>
<br>
<STRONG><FONT size="4">Pollution Problems</FONT>
</STRONG>
<FONT size="1"></FONT>
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
<br>
<TABLE class="PortletText1" border="0" cellpadding="2" cellspacing="3"
	width="100%">
	<TR>
		<TD colspan="3">
			<STRONG><FONT size="4">Combined Sewer</FONT>
			</STRONG>
			<FONT size="1"></FONT>
		</TD>
	</TR>
	<tr>
		<Td colspan="3">
			<b>Status: Documented Needs Only</b>
		</Td>
	</tr>
	<TR>
		<TD class="PortletHeaderColor">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> </FONT> </STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Area (Acres) </FONT> </STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Population </FONT> </STRONG>
			</P>
		</TD>

	</TR>
	<TR>
		<TD>
			<P align="left">
				Documented
			</P>
		</TD>
		<TD>
			<P align="center">
				120,345
			</P>
		</TD>
		<TD>
			<P align="center">
				20,000
			</P>
		</TD>
	</TR>
	<TR class="PortletSubHeaderColor">
		<TD>
			<P align="left">
				Cost Curves
			</P>
		</TD>
		<TD>
			<P align="center">
				&nbsp;
			</P>
		</TD>
		<TD>
			<P align="center">
				&nbsp;
			</P>
		</TD>
	</TR>
</TABLE>
<br>


<TABLE class="PortletText1"
	style=" BORDER-BOTTOM: rgb(122,150,223) thin solid" cellspacing="1"
	cellpadding="1" width="100%">
	<TR>
		<TD colspan="5">
			<DIV style="FLOAT: left" width="300">
				<STRONG><FONT size="4">Point of Record (POR)</FONT>
				</STRONG>
			</DIV>
		</TD>
	</TR>

	<TR>
		<TD>
			<STRONG><FONT color="#ff0000"></FONT>Location Description: </STRONG>
		</TD>
		<TD colspan="2">
			Processing Area Centroid
		</TD>
		<TD>
			<P align="left">
				<STRONG>Source: </STRONG>
			</P>
		</TD>
		<TD>
			PCS
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG><FONT color="#ff0000">* </FONT>Method: </STRONG>
		</TD>
		<TD>
			Unknown
		</TD>
		<TD colspan="2">
			<STRONG><FONT color="#ff0000">* </FONT>Measure Date: </STRONG>
		</TD>
		<TD class="PortletHeaderText">
			03/03/2008

		</TD>
	</TR>
	<TR>
		<TD class="PortletHeaderText">
			<STRONG><FONT color="#ff0000">* </FONT>Datum: </STRONG>
		</TD>
		<TD>
			North American Datum of 1983
		</TD>
		<TD colspan="2">
			<STRONG>Scale: </STRONG>
		</TD>
		<TD>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG><FONT color="#ff0000">* </FONT>Latitude: </STRONG>
		</TD>
		<TD>
			39.8526


		</TD>
		<TD colspan="2">
			<STRONG><FONT color="#ff0000">* </FONT>Latitude Direction: </STRONG>
		</TD>
		<TD>

			N

		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG><FONT color="#ff0000">* </FONT>Longitude: </STRONG>
		</TD>
		<TD>
			75.5026&nbsp;&nbsp;

		</TD>
		<TD colspan="2">
			<STRONG><FONT color="#ff0000">* </FONT>Longitude Direction:
			</STRONG>
		</TD>
		<TD>


			W
		</TD>
	</TR>
	<TR>
		<TD>
			<B></B>
		</TD>
		<TD></TD>
		<TD colspan="3">
			<INPUT type="checkbox" value="on" disabled="disabled">
			<STRONG> Within Tribal Territory </STRONG>
		</TD>
	</TR>
	<TR>
		<TD colspan="5">
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">




				<TR>
					<TD colspan="6">

						<FONT size="2"> <STRONG> County: </STRONG> </FONT>
					</TD>
				</TR>
				<TR class="PortletHeaderColor">
					<TD class="PortletHeaderText" align="center" width="50">
						POR
					</TD>
					<TD class="PortletHeaderText" align="center">
						Name
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Code
					</TD>
				</TR>
				<TR>
					<TD align="center">
						<pdk-html:img page="/images/checkmark.gif"/>
					</TD>
					<TD align="center">
						Adair
					</TD>
					<TD align="center">
						36059
					</TD>
				</TR>
				<TR>
					<TD></TD>
					<TD align="center">
						Bellview
					</TD>
					<TD align="center">
						36060
					</TD>

				</TR>
			</table>
		</TD>
	</TR>
	<tr>
		<td colspan="5">
			<P>
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

				<TR>
					<TD colspan="6">

						<font size="2"> <STRONG> Congressional District: </STRONG>
						</font>
					</TD>
				</TR>
				<TR class="PortletHeaderColor">
					<TD class="PortletHeaderText" align="center" width="50">
						POR
					</TD>
					<TD class="PortletHeaderText" align="center">
						Name
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Code
					</TD>

				</TR>
				<TR>
					<TD align="center">
						<pdk-html:img page="/images/checkmark.gif"/>
					</TD>
					<TD align="center">
						5th Congressional District
					</TD>
					<TD align="center">
						3605
					</TD>
				</TR>
				<TR>
					<TD></TD>
					<TD align="center">
						6th Congressional District
					</TD>
					<TD align="center">
						3606
					</TD>
				</TR>

			</table>

		</td>
	</tr>
	<tr>
		<td colspan="5">

			<P>
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
				<TR>
					<TD colspan="6">
						<font size="2"> <STRONG> Watershed: </STRONG> </font>
					</TD>
				</TR>
				<TR class="PortletHeaderColor">
					<TD class="PortletHeaderText" align="center" width="50">
						POR
					</TD>
					<TD class="PortletHeaderText" align="center">
						Name
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Number
					</TD>

				</TR>
				<TR>
					<TD align="center">
						<pdk-html:img page="/images/checkmark.gif"/>
					</TD>
					<TD align="center">
						North Folk Salt
					</TD>
					<TD align="center">
						02030101
					</TD>
				</TR>
			</table>
			</td>
			</tr>
			<P></P>
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">
				<TR>
					<TD colspan='5'>
						<font size="2"> <STRONG> Boundaries: </STRONG> </font>
					</TD>
				</TR>
				<TR class="PortletHeaderColor">
					<TD class="PortletHeaderText">
						<P align="center">
							Type
						</P>
					</TD>
					<TD class="PortletHeaderText">
						<P align="center">
							Name
						</P>
					</TD>
					<TD class="PortletHeaderText">
						<P align="center">
							Number
						</P>
					</TD>


				</TR>
				<TR>
					<TD>
						<P align="center">
							National Estuary Program
						</P>
					</TD>
					<TD>
						<P align="center">
							Deleware Estuary
						</P>
					</TD>
					<TD>
						<P align="center">
							3693967
						</P>
					</TD>

				</TR>
			</TABLE>