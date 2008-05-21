<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="gov.epa.owm.mtb.cwns.common.CurrentUser"
	import="java.net.URLEncoder" import="javax.servlet.http.HttpSession"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"
    import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>

<%@ page import="java.util.Collection"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.UserRole"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>




<%
			PortletRenderRequest pRequest = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();

	CurrentUser currentUser = (CurrentUser) request.getSession()
			.getAttribute(CurrentUser.CWNS_USER);

	int pendingUsers = 0;
	Integer pUsers = (Integer) pRequest.getAttribute("pendingUsers");
	if (pUsers != null) {
		pendingUsers = pUsers.intValue();
	}
	
	NameValue userLocation = new NameValue();
	NameValue userType = new NameValue();
	NameValue[] linkParams = new NameValue [2];
	userLocation.setName("userLocation");
	userType.setName("userType");
	Collection roles = (Collection) request.getAttribute("roles");
	UserRole currentRole = (UserRole) request.getAttribute("currentRole");
	Iterator roleIter = null;
	if(roles != null){
		roleIter = roles.iterator();
	}
	String currentURL= request.getParameter("_page_url");
    String changePassBaseURL = CWNSProperties.getProperty("epa.changepassword.url",""); 
    String changePasswordURL= changePassBaseURL + "?doneurl=" + URLEncoder.encode(currentURL, "UTF-8");
    String helpKey = (String)request.getAttribute("helpKey");
    
%>



<SCRIPT language="JavaScript" type="text/javascript">
// Open help window
function ShowAdminHelp(popupUrl)
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

  var w = window.open(popupUrl, 'AdminHelp', settings);
  if(w != null) 
    w.focus();     
}
</SCRIPT>


<DIV align="right">
	<FONT class="PortletText1"> <A href="javascript:void(0);"
		onclick='ShowAdminHelp("<%=url%><bean:message key="<%=helpKey %>"/>")'>Help</A>
	</FONT>
</DIV>

<TABLE width="100%" border="0">
	<TBODY>

		<TR>
			<TD align="left" colspan="3">
				<P>
					<A href="<%=changePasswordURL %>"><FONT class="PortletText1">Change
							Password</FONT> </A>
				</P>
			</TD>
		</TR>

		<!-- User Managment -->

		<%
		if (currentUser != null && currentUser.isUserManager()) {
		%>
		<TR>
			<TD>
				<FONT class="PortletText1"> <A
					href='<%=CWNSEventUtils.constructEventLink(pRequest,
								"UserManagementPage", null, true, true)%>'>
						Manage Users (<%=pendingUsers%> Pending) </A> </FONT>
			</TD>
		</TR>
		<%
		}
		%>
<!-- 
		<TR>
			<TD>
				<A href="javascript:void(0);"><FONT class="PortletText1">
						Manage Document Type</FONT> </A>
			</TD>
		</TR>
		<TR>
			<TD>
				<A href="javascript:void(0);"><FONT class="PortletText1">
						Manage O &amp; M Category</FONT> </A>
			</TD>
		</TR>
		<TR>
			<TD>
				<A href="javascript:void(0);"><FONT class="PortletText1">Manage
						Pollution Problem</FONT> </A>
			</TD>
		</TR>
 -->
	</TBODY>
</TABLE>


<logic:notEmpty name="roles">
	<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pRequest,
							"../../css/cwns.css")%>">
	<br>
	<TABLE cellspacing="0" cellpadding="3" border="0" width="100%"
		class="PortletText1">
	<TR>
	<TD>
	<FONT class="PortletText1"><b>Primary Role</b></FONT>
	</TD>
	</TR>
		<c:set var="i" value="0" />
		<logic:iterate id="role" name="roles">
			<c:choose>
				<c:when test='${i%2=="0"}'>
					<c:set var="class" value="PortletSubHeaderColor" />
				</c:when>
				<c:otherwise>
					<c:set var="class" value="" />
				</c:otherwise>
			</c:choose>
			<%
					if(roleIter != null){
					UserRole thisRole = (UserRole) roleIter.next();
					String thisLocationId = thisRole.getLocationId();
					String thisLocationTypeId = thisRole.getLocationTypeId();
					userLocation.setValue(thisLocationId);
					userType.setValue(thisLocationTypeId);
					linkParams[0] = userLocation;
					linkParams[1] = userType;
					if (thisLocationId.equals(currentRole.getLocationId())
					&& thisLocationTypeId.equals(currentRole
							.getLocationTypeId())) {
			%>
			<TR class="RowHighlighted">
				<TD>
					<bean:write name="role" property="locationId" />&nbsp;
					<bean:write name="role" property="locationTypeId" />&nbsp; User
				</TD>
			</TR>

			<%
			} else {
			%>
			<TR class="<c:out value="${class}"/>">
				
				<TD>
					<logic:equal name="role" property="locationTypeId" value="Federal">
						<A href='<%=CWNSEventUtils.constructEventLink(pRequest,"CWNSFederalHomePage", linkParams, true, true)%>'>
						<bean:write name="role" property="locationId" />&nbsp; 
						<bean:write name="role" property="locationTypeId" />&nbsp; User 
						</A>
					</logic:equal>
					<logic:equal name="role" property="locationTypeId" value="Local">
						<A href='<%=CWNSEventUtils.constructEventLink(pRequest,"CWNSLocalHomePage", linkParams, true, true)%>'>
						<bean:write name="role" property="locationId" />&nbsp; 
						<bean:write name="role" property="locationTypeId" />&nbsp; User 
						</A>
					</logic:equal>
					<logic:equal name="role" property="locationTypeId" value="Regional">
						<A href='<%=CWNSEventUtils.constructEventLink(pRequest,"CWNSRegionalHomePage", linkParams, true, true)%>'>
						<bean:write name="role" property="locationId" />&nbsp; 
						<bean:write name="role" property="locationTypeId" />&nbsp; User 
						</A>
					</logic:equal>
					<logic:equal name="role" property="locationTypeId" value="State">
						<A href='<%=CWNSEventUtils.constructEventLink(pRequest,"CWNSStateHomePage", linkParams, true, true)%>'>
						<bean:write name="role" property="locationId" />&nbsp; 
						<bean:write name="role" property="locationTypeId" />&nbsp; User 
						</A>
					</logic:equal>
					
				</TD>
			</TR>
			<%
			}}
			%>

			<c:set var="i" value="${i+1}" />
		</logic:iterate>
	</TABLE>
	<br>
</logic:notEmpty>
