<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="oracle.portal.utils.NameValue"
	%>
<%@ page import="gov.epa.owm.mtb.cwns.common.UserRole"
		 import="gov.epa.owm.mtb.cwns.myroles.MyRolesAction"
		 import="gov.epa.owm.mtb.cwns.service.UserService"
	     import="oracle.webdb.provider.v2.struts.StrutsUtils"%>		 
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

    String helpUrl = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
	
	Collection roles     = (Collection)	request.getAttribute("roles");
	Iterator roleIter    = roles.iterator();
	UserRole currentRole = (UserRole)request.getAttribute("currentRole");
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pRequest,
							"../../css/cwns.css")%>">


<SCRIPT type=text/javascript>

function switch_role(locationTypeId, locationId)
{

 	window.document.getElementById("myRolesAction").value         = '<%=MyRolesAction.ACTION_SWITCH_ROLES%>';
 	window.document.getElementById("myRolesLocationTypeId").value = locationTypeId;
 	window.document.getElementById("myRolesLocationId").value     = locationId;
    window.document.myRolesForm.submit();

}
// Open help window
function MyRolesHelp(popupUrl)
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



<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='MyRolesHelp("<%=helpUrl%><bean:message key="help.myroles"/>")'>Help</A>
    </FONT>
</DIV>
<pdk-html:form  method="post" 
				name="myRolesForm" 
				type="gov.epa.owm.mtb.cwns.myroles.MyRolesForm"
				action="myRoles.do">

<%-- Hidden Fields --%>
<pdk-html:hidden styleId="myRolesAction"         name="myRolesForm" property="action"/>
<pdk-html:hidden styleId="myRolesLocationTypeId" name="myRolesForm" property="locationTypeId"/>
<pdk-html:hidden styleId="myRolesLocationId"     name="myRolesForm" property="locationId"/>

<TABLE cellspacing="0" cellpadding="3" border="0" width="100%" 	class="PortletText1">

	<c:set var="i" value="0" />
	<logic:iterate id="role" name="roles" >
		<c:choose>
			<c:when test='${i%2=="0"}'>
				<c:set var="class" value="PortletSubHeaderColor" />   
			</c:when>
			<c:otherwise>
				<c:set var="class" value="" />
			</c:otherwise>
		</c:choose>   
    <%UserRole thisRole         = (UserRole)roleIter.next();
      String thisLocationId     = thisRole.getLocationId();
      String thisLocationTypeId = thisRole.getLocationTypeId();
      if (thisLocationId.equals(currentRole.getLocationId()) &&
    	  thisLocationTypeId.equals(currentRole.getLocationTypeId())) { %>
			<TR class="RowHighlighted">	
				<TD>&nbsp;</TD>
				<TD>
					<bean:write name="role" property="locationId" />&nbsp;
					<bean:write name="role" property="locationTypeId" />&nbsp;
					User
				</TD>
			</TR>
	
	<%} else {%>
			<TR class="<c:out value="${class}"/>">	
				<TD>&nbsp;</TD>
				<TD>
				    <A href='javascript: switch_role("<%=thisLocationTypeId%>","<%=thisLocationId%>")'>
					<bean:write name="role" property="locationId" />&nbsp;
					<bean:write name="role" property="locationTypeId" />&nbsp;
				
					User </A>
				</TD>
			</TR>
	<%}%>
	
	<c:set var="i" value="${i+1}" />
	</logic:iterate>		
	
</TABLE>	

</pdk-html:form>										