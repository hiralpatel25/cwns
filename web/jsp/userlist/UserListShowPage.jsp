<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.RegistrationPreliminaryAction"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSUrlUtils"%>
<%@ page import="gov.epa.owm.mtb.cwns.userlist.UserListAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userlist.UserListForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction"%>


<%
	PortletRenderRequest prr = (PortletRenderRequest) request
								.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
								
	NameValue[] linkParams = new NameValue[2];

	NameValue[] linkParamsSearch = new NameValue[1];
	linkParamsSearch[0] = new NameValue("action", UserListAction.ACTION_USER_DEFAULT_SEARCH);

	NameValue[] linkParamsNewUser = new NameValue[1];
	linkParamsNewUser[0] = new NameValue("action", UserDetailsAction.ACTION_PRELIM_DISPLAY);
		
	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();

    String eventSubmit  = CWNSEventUtils.eventName("NewCwnsUser");
    String oidUserParam = CWNSEventUtils.eventParameter("oidUser");
    String actionParam  = CWNSEventUtils.eventParameter("action");
    String oidUser      = prr.getParameter("oidUser");
    if(oidUser == null) oidUser="";
     
    UserListForm ulForm = (UserListForm) prr.getAttribute("ulForm");
   
    String formName         = null;
    String formAction       = null;
    String formHiddenFields = null;
    try
    {
        formName   = CWNSUrlUtils.htmlFormName(prr,null);
        formAction = CWNSUrlUtils.htmlFormActionLink(prr, CWNSUrlUtils.EVENT_LINK);
        formHiddenFields = 
            CWNSUrlUtils.htmlFormHiddenFields(prr, CWNSUrlUtils.EVENT_LINK);
    }
    catch (IllegalStateException e)
    {
        //do something
    }
    String helpKey = (String)request.getAttribute("helpKey");
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">


<SCRIPT type=text/javascript>


// Re-sort the list
function setSortOrder(userSortColumn) {
 	window.document.getElementById("userSortColumn").value = userSortColumn;
    setUserListActionAndSubmit('<%=UserListAction.ACTION_SORT%>');
}

// Set the value of the "action" hidden attribute and submit the form
function setUserListActionAndSubmit(action){

	for(var i=0;i<document.userListForm.elements.length;i++) {
		element = document.userListForm.elements[i];
	    var name = element.getAttribute('name');	    
	    if (name.indexOf('.act') > 1){
	    	element.value=action;
	    }
	}		
	 
	 window.document.userListForm.submit();
}

// Open help window
function showUserListHelp(popupUrl)
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

  var w = window.open(popupUrl, 'UserListHelp', settings);
  if(w != null) 
    w.focus();     
}

function portalUserName(){
	document.getElementById('portalUserName').style.display='block';

	document.getElementById('addUserLink').style.display='none';
	document.getElementById('addUser').style.display='none';
}    


function newPortalAccount(){
	alert('The user will be sent to the\nPortal Registration Screen now.');
	hideAddUser();
}    


function goToCwnsRegistration() {
	alert('The user will be sent to the\nCWNS Registration Screen now.');
	hideAddUser();
}   


function submitenter(e)
{
 var keycode;
 if (window.event) keycode = window.event.keyCode;
 else if (e) keycode = e.which;
 else return true;
 if (keycode == 13){
   submitform();
   return false;
 } else
   return true;
}


function clearAct(){
	var inputTags = window.document.getElementsByTagName('input');
	for(var i=0;i<inputTags.length;i++){
	 element = inputTags[i];
	 var type = element.getAttribute('type');
	 if (type == 'hidden'){
	    var name = element.getAttribute('name');	    
	    if (name.indexOf('.act') > 1){
	    	element.value='';
	    }
	 } 
  }
}

function submitform()
{
	oidId = window.document.getElementById('<%=oidUserParam%>').value;
	oidId = trimSpaces(oidId);
	if (oidId.length < 1 ) {
		alert('You must supply a Portal User Name.');
		window.document.getElementById('<%=oidUserParam%>').focus();
		return false;
	}
	clearAct();   
    document.<%=formName%>.submit();
}

            
// Trim whitespace from left and right sides of s.
function trimSpaces(s) {
    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}
 
</SCRIPT>




<pdk-html:form method="post" name="userListForm"
	type="gov.epa.owm.mtb.cwns.userlist.UserListForm" action="userList.do">

	<!--  Hidden fields -->
	<DIV id="hidden_fields" style="DISPLAY:none">
		<pdk-html:text name="userListForm" styleId="action" property="action" />
		<pdk-html:text name="userListForm" styleId="userSortColumn"
			property="userSortColumn" />
		<pdk-html:text name="userListForm" property="nextUserToDisplay" />
		<pdk-html:text name="userListForm" property="frmUser" />
		<pdk-html:text name="userListForm" property="prevUserToDisplay" />
	</DIV>


<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='showUserListHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>
</DIV>
	<TABLE cellspacing="0" cellpadding="0" width="98%" align=center border=0 class="PortletText1">

		<!--  Display Search Criteria -->
		<tr>
			<td align="center">
				<strong><bean:write name="userListForm" property="searchDescription" />
				</strong>

				<logic:equal name="userListForm" property="searchDescription"
					value="<%=UserListAction.QUERY_TYPE_SEARCH_DESC%>">					
					<br />
						<FONT class="PortletText1"> 
							<A href='<%=CWNSEventUtils.constructEventLink(prr,"DefaultSearch", linkParamsSearch,true, true)%>'>
							Display	Default List</A> 
						</FONT>
				</logic:equal>
			</td>
		</tr>

		<TR>
			<TD>&nbsp;</TD>
		</TR>

			<logic:lessEqual name="userListForm"
					property="numOfUsers" value="0">
			<TR><TD>&nbsp;</TD></TR>
					
			<TR class="PortletText1">
				<TD align="center" colspan="9">
					<b><font color="red">No Users Match The Search Criteria !</font></b>
				</TD>
			</TR>
									
			</logic:lessEqual>					

		<!--  Display Column Headers -->
	<logic:greaterThan name="userListForm" property="numOfUsers" value="0">

		<TR>
			<TD>
				<DIV align="right">
					<TABLE cellSpacing=0 cellPadding=0 width="98%" align=center border=0>
					
						<TR class="PortletSubHeaderColor">
							<TH class="PortletSubHeaderText" align="left" width="10%">&nbsp;
								<a href="javascript: setSortOrder('<%=UserListAction.SORT_COLUMN_NAME%>')">Name</a>
							</TH>
							<TH width="1%">
								&nbsp;
							</TH>
							<TH class="PortletSubHeaderText" align="left">
								Status
							</TH>
							<TH width="1%">
								&nbsp;
							</TH>
							<TH class="PortletSubHeaderText" align="left">
								Last Modified Date
							</TH>
						</TR>

						<!--  Display Rows -->
 
						<c:set var="i" value="1" />
						<logic:iterate id="ulHelper" name="userListForm"
							property="ulHelpers"
							type="gov.epa.owm.mtb.cwns.userlist.UserListHelper">
							<c:choose>
								<c:when test='${i%2=="0"}'>
									<c:set var="class" value="PortletSubHeaderColor" />
								</c:when>
								<c:otherwise>
									<c:set var="class" value="" />
								</c:otherwise>
							</c:choose>
								
								<%if (ulHelper.getCwnsUserId().equals(ulForm.getCurrentSelection())) { %>
								<TR class="RowHighlighted" >
								<% } else { %>
								<TR class="<c:out value="${class}"/>">
								<%} %>
									<TD nowrap="nowrap" >
									<FONT class="PortletText1"> 								
									<%linkParams[0] = new NameValue("cwnsUserId", ulHelper.getCwnsUserId()); 
									  linkParams[1] = new NameValue("action", UserDetailsAction.ACTION_DISPLAY_SELECTED_USER_INFO);
//									  linkParams[1] = new NameValue("action", UserDetailsAction.ACTION_DISPLAY_USER_INFO);
									%>
									<A href='<%=CWNSEventUtils.constructEventLink(prr,"DisplayUser", linkParams,true, true)%>'>
									<%=ulHelper.getName()%></A> 
									</FONT>
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD align="left">
									<FONT class="PortletText1"><%=ulHelper.getStatus()%></FONT>
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD align="left">
									<FONT class="PortletText1"><%=ulHelper.getLastModifiedDateFormatted()%></FONT>
								</TD>
							</tr>
							<c:set var="i" value="${i+1}" />
						</logic:iterate>

			
						<tr><td>&nbsp;</td></tr>

						<!--  Display Footer Links -->
						<TR class="PortletText1">

							<TD colspan="7" align="right">

								<!-- Prev -->
								<logic:greaterThan name="userListForm"
									property="prevUserToDisplay" value="0">
									<A
										href="javascript: setUserListActionAndSubmit('<%=UserListAction.ACTION_FIRST%>')">|&lt;&lt;</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							<A
										href="javascript: setUserListActionAndSubmit('<%=UserListAction.ACTION_PREVIOUS%>')">Prev</A>&nbsp;&nbsp;|&nbsp;&nbsp;
						</logic:greaterThan>
								<logic:lessEqual name="userListForm"
									property="prevUserToDisplay" value="0">
						   |&lt;&lt;&nbsp;|&nbsp;
							Prev&nbsp;|&nbsp;
						</logic:lessEqual>

								<!-- Identify where we are in the list of Users -->
								<bean:write name="userListForm" property="frmUser" />
								-
								<bean:write name="userListForm" property="toUser" />
								of
								<bean:write name="userListForm" property="numOfUsers" />
								&nbsp;|&nbsp;

								<!-- Next -->
								<bean:define id="maxUsers">
									<bean:write name="userListForm" property="numOfUsers" />
								</bean:define>
								<logic:lessThan name="userListForm" property="toUser"
									value="<%=maxUsers%>">
									<A
										href="javascript: setUserListActionAndSubmit('<%=UserListAction.ACTION_NEXT%>')">Next</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							    <A
										href="javascript: setUserListActionAndSubmit('<%=UserListAction.ACTION_LAST%>')">&gt;&gt;|</A>&nbsp;&nbsp;
						</logic:lessThan>

								<logic:greaterEqual name="userListForm" property="toUser"
									value="<%=maxUsers%>">
							Next&nbsp;&nbsp;|&nbsp;&nbsp;
							&gt;&gt;|&nbsp;&nbsp;
						</logic:greaterEqual>
							</TD>
						</TR>
					</TABLE>
				</DIV>
			</TD>
		</TR>
		
			</logic:greaterThan>
	</TABLE>

<DIV id="addUserLink" style="DISPLAY:block ">
	<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
		
		<tr>
			<TD colspan="9" >&nbsp;
				<FONT class="PortletText1"> 								
					<A href='<%=CWNSEventUtils.constructEventLink(prr,"NewPortalUser", linkParamsNewUser,true, true)%>'>
						Add User</A>
				</FONT>
			</TD>
		</tr>
	</TABLE>		
</DIV>

</pdk-html:form>

				