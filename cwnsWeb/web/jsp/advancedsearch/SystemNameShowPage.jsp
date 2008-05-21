<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<SCRIPT type=text/javascript>

function get_system_name() { 
   if(window.opener && !window.opener.closed) 
   { 
     var c = "";
     if (document.systemname.sysname.checked) {
           c = document.systemname.sysname.value;
     }
     for (var i=0; i < document.systemname.sysname.length; i++){
         if (document.systemname.sysname[i].checked) {
           c = document.systemname.sysname[i].value;
         }
     }
      window.opener.document.getElementById("systemName").value=c;
   }
   window.close();
}

</SCRIPT>
<FORM name="systemname">

	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0">
		<TBODY>
			<TR class="PortletSubHeaderColor">
				<TH class="PortletSubHeaderText" align="center" width="10%">
					Select
				</TH>
				
				<TH class="PortletSubHeaderText" align="left">
					System Name
				</TH>
			</TR>
			<logic:present name="snames">
			<logic:iterate id="sname" name="snames" type="Entity">
				<tr>
					<TD align="center">
					    <input type="radio" name="sysname" value="<bean:write name="sname" property="value" />"/>
					</TD>

					<TD align="left">
						<FONT class="PortletText1"><bean:write name="sname" property="value" /></FONT>
					</TD>
					<TD></TD>

				</tr>
			</logic:iterate>
			</logic:present>
            <br>
		    <TR>
		       <TD align="center">
		  		<input type="submit" name="Select" value="Select" onclick="get_system_name()"/>
		       </TD>
		    </TR>
			
	</TABLE>

</FORM>