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
<%@page import="gov.epa.owm.mtb.cwns.boundary.BoundaryListHelper" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<SCRIPT type=text/javascript>

function GetBoundaryInfo() { 
   if(window.opener && !window.opener.closed) 
   { 
     var c = "";
     if (document.boundaryForm.boundaryname.checked) {
           c = document.boundaryForm.boundaryname.value;
     }
     for (var i=0; i < document.boundaryForm.boundaryname.length; i++){
         if (document.boundaryForm.boundaryname[i].checked) {
           c = document.boundaryForm.boundaryname[i].value;
         }
     }
     var boundaryName = "";
     var boundaryId = "";
     var boundaryType = "";
     if (c != ""){
       var tempArray = c.split('+');
       boundaryId = tempArray[0];
       boundaryType = tempArray[1];
       boundaryName = tempArray[2];
     }    
     window.opener.document.getElementById("boundaryId").value=boundaryId;
     window.opener.document.getElementById("boundaryType").value=boundaryType;
     window.opener.document.getElementById("boundaryName").value=boundaryName;
          
   }
   window.close();
}

</SCRIPT>
<FORM name="boundaryForm">

	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0">
		<TBODY>
			<TR class="PortletSubHeaderColor">
				<TH class="PortletSubHeaderText" align="center" width="10%">
					Select
				</TH>
				
				<TH class="PortletSubHeaderText" align="left">
					Special Program Area Type/Special Program Area Name
				</TH>
			</TR>
			
			<logic:iterate id="boundary" name="boundaries" type="BoundaryListHelper">
				<tr>
					<TD align="center">
					    <input type="radio" name="boundaryname" value="<bean:write name="boundary" property="boundaryId"/>+<bean:write name="boundary" property="boundaryType"/>+<bean:write name="boundary" property="boundaryName" />"/>
					</TD>

					<TD align="left">
						<FONT class="PortletText1"><bean:write name="boundary" property="boundaryType"/>&nbsp;&nbsp;
						              <bean:write name="boundary" property="boundaryName" /></FONT>
					</TD>
					<TD></TD>

				</tr>
			</logic:iterate>
            <br>
		    <TR>
		       <TD align="center">
		  		<input type="submit" name="Select" value="Select" onclick="GetBoundaryInfo()"/>
		       </TD>
		    </TR>
			
	</TABLE>

</FORM>