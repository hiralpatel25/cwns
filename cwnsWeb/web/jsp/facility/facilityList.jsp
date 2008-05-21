<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.dao.FacilityDAO"%>
<%@ page import="gov.epa.owm.mtb.cwns.facility.FacilityListForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.facility.FacilityListImportServlet"%>
<%@ page import="gov.epa.owm.mtb.cwns.facility.FacilityListDisplayAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.UserRole"%>
<%@ page import="java.util.Set"%>
           
<% 
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	NameValue[] linkParams       = new NameValue[1];
	FacilityListForm facListForm = (FacilityListForm)request.getAttribute("facListBean");
	String forwardUrl = request.getParameter("_page_url");

	String importUrl = "http://"+request.getServerName()+ ":"+ request.getServerPort()+"/cwns/servlet/FacilityListImportServlet";
	String exportUrl = "http://"+request.getServerName()+ ":"+ request.getServerPort()+"/cwns/servlet/FacilityListExportServlet";
    String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
                  
    UserRole currentRole = (UserRole) request.getAttribute("currentRole");
    String facilityIdsNotUpdated = (String) request.getAttribute("facilityIdsNotUpdated");
    HttpSession pSession = request.getSession(false);
   	// Facilities previously selected
	Set masterSelectedList = (Set) pSession.getAttribute("masterSelectedList");
   	String[] selectedList = (String[])(masterSelectedList.toArray(new String[masterSelectedList.size()]));	    
  
    String helpKey = (String)request.getAttribute("helpKey");
%>



<SCRIPT type=text/javascript>

// Let the user know the functionality is not yet implemented
function local_assigned() {
	alert('Changing the facility status to Local Assigned is not currently implemented');
}

// Change status of selected facilities
function change_status(status) {
 	window.document.getElementById("newStatus").value = status;
	return setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_SET_REVIEW_STATUS%>')
}

// Set the value of the "action" hidden attribute
function setActionAndSubmit(action){
	 window.document.getElementById("act").value = action;
	 window.document.facListBean.submit();
}

// Re-sort the list
function setSortOrder(sortColumn) {

 	window.document.getElementById("sortColumn").value = sortColumn;
    setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_SORT%>');
}

// Export a file that contains a list of facility numbers

var intervalID;
var winRef;
function ExportFacilities(form){

    cwnsUserSettingId = window.document.getElementById("cwnsUserSettingId");	
	// Store the Facility numbers and Selected Facility numbers in HTML hidden attributes
	facilities 		   = window.document.getElementById("facilities");
    selectedFacilities = window.document.getElementById("selectedFacilities");	

	saveFacilities(facilities,selectedFacilities);
			 
	// open a pop-up window and call the FacilityListExportServlet servlet
	leftVal = (screen.width  - 300)  / 2;
	topVal  = (screen.height - 200 ) / 2;
	
	winRef = window.open('<%=exportUrl%>'+'?facilities='+facilities.value+'&selectedFacilities='+selectedFacilities.value+'&<%=FacilityListDisplayAction.IMPORT_EXPORT_KEY%>=+<%=facListForm.getImportExportKey()%>	',
                            'FacilityImportWindow','left='+leftVal+',top='+topVal+',width=10,height=10,toolbar=0,resizable=0,scrollbars=0,status=1,location=0');                            			  
	if (winRef != null) {
		winRef.focus();                            
	 	intervalID = window.setInterval("checkWinRef(winRef)",500);
	}
   }

   function checkWinRef(winRef) {
	if ( !winRef || winRef.closed ){
   	 	window.clearInterval(intervalID);
		setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_CLEAR_ALL%>');
   	 }
   }
   
   
   
// Save a space delimeted list of facilitiies, and selected facilites, 
// in HTML hidden attributes
function saveFacilities(facilities,selectedFacilities) { 
	
	// clear any existing values
	facilities.value         = "";
	selectedFacilities.value = "";	
	
	for (var i=0;i<window.document.facListBean.elements.length;i++) {
   		var e = window.document.facListBean.elements[i];
   		if (e.type.indexOf("checkbox") == 0 ) {
			facilities.value = facilities.value + e.value + " ";
   			if (e.checked == true) {
    			selectedFacilities.value = selectedFacilities.value + e.value + " ";
   			}
		}
	}
	
	
	 
}

// Show or Hide the import area of the Portlet
function showOrHideImport(mode){
     window.document.getElementById('import').style.display=mode;
}

// Open help window
function ShowFacilityListHelp(popupUrl)
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

  var w = window.open(popupUrl, 'FacilityListHelp', settings);
  if(w != null) 
    w.focus();     
}

</SCRIPT>

<pdk-html:form method="post" name="facListBean" 
				type="gov.epa.owm.mtb.cwns.facility.FacilityListForm"
				action="facilityListDisplay.do">
	
<!-- Kludge because the pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
<DIV id="hidden_fields" style="DISPLAY:none">	
	<pdk-html:text styleId="act" property="act" /> 
	<pdk-html:text styleId="nextFacilityToDisplay" name="facListBean" property="nextFacilityToDisplay" /> 
	<pdk-html:text styleId="fromFacilityToDisplay" name="facListBean" property="fromFacility" /> 		
	<pdk-html:text styleId="prevFacilityToDisplay" name="facListBean" property="prevFacilityToDisplay" /> 	
    
    <pdk-html:text styleId="newStatus" property="newStatus" /> 
    <pdk-html:text styleId="facilities" property="facilities" /> 
	<pdk-html:text styleId="selectedFacilities" property="selectedFacilities" />
	<pdk-html:text styleId="sortColumn" property="sortColumn" />
</DIV>

<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='ShowFacilityListHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>
</DIV>

<html:errors />

<center><B><bean:write name="facListBean" property="searchDescription" /></B><br>
<font color=green size=2>Sort column <bean:write name="facListBean" property="sortColumnDescription" /> by <bean:write name="facListBean" property="sortOrderDescription" /> order</font>

<logic:notEqual name="facListBean" property="searchDescription" value="<%=FacilityListDisplayAction.QUERY_TYPE_DEFAULT_DESC%>">
<br/>
<font size="-1"><a href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_DEFAULT_QUERY%>')">Display All Facilities</a></font>
</logic:notEqual>	

</center>
	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0">
		<TBODY>
		    
			<TR class="PortletSubHeaderColor">
				<TH class="PortletSubHeaderText" align="center" width="10%">
					Select
				</TH>
				<TH class="PortletSubHeaderText" align="left" width="10%">
					<a href="javascript: setSortOrder('<%=FacilityListDisplayAction.SORT_COLUMN_CWNS_NUMBER%>')">CWNS<br>Number</a>
				</TH>
				<TH width="1%">
					&nbsp; &nbsp;
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					<a href="javascript:  setSortOrder('<%=FacilityListDisplayAction.SORT_COLUMN_NAME%>')">Facility/Project Name</a>
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					<a href="javascript: setSortOrder('<%=FacilityListDisplayAction.SORT_COLUMN_REVIEW_STATUS%>')">Review Status</a>
				</TH>
				<TH>
					&nbsp;
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					<a href="javascript: setSortOrder('<%=FacilityListDisplayAction.SORT_COLUMN_COUNTY%>')">County</a>
				</TH>
				<TH>
					&nbsp;
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					<a href="javascript: setSortOrder('<%=FacilityListDisplayAction.SORT_COLUMN_LOCAL_REVIEW_STATUS%>')">Feedback Status</a>
				</TH>
				<TH class="PortletSubHeaderText" align="left">
					<a href="javascript: setSortOrder('<%=FacilityListDisplayAction.SORT_AUTHORITY%>')">Authority</a>
				</TH>
			</TR>

			<c:set var="i" value="1" />
			<logic:iterate id="facilityHelper" name="facListBean"  property="facHelpers"
				type="gov.epa.owm.mtb.cwns.facility.FacilityListHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>

				<TR class="<c:out value="${class}"/>">
					<TD align="center">
					    <pdk-html:multibox name="facListBean" property="facilityIds">
					       <bean:write name="facilityHelper" property="facId"/>
					    </pdk-html:multibox> 
					</TD>

					<TD align="left">
						<FONT class="PortletText1"><bean:write name="facilityHelper" property="cwnsNbr" /></FONT>
					</TD>
					<TD></TD>
					<td>
						<FONT class="PortletText1"> 
							<%
							if("Local".equals(currentRole.getLocationTypeId()) && facilityHelper.getFeedbackFacId()!=null && !"0".equals(facilityHelper.getFeedbackFacId())){  
							   linkParams[0] = new NameValue("facilityId", facilityHelper.getFeedbackFacId());
							   System.out.println("local");
							}else{
							   linkParams[0] = new NameValue("facilityId", facilityHelper.getFacId());
							}
 							%>
							<A href='<%=CWNSEventUtils.constructEventLink(prr,"FacilityInfoPage", linkParams,true, true)%>'>
							<bean:write name="facilityHelper" property="name" /></A> 
						</FONT>
					</td>
					<TD nowrap align="left">
						<FONT class="PortletText1">
							<bean:write name="facilityHelper" property="reviewStatus" />
						</FONT>
					</TD>
					<TD>
						&nbsp;
					</TD>
					<TD>
						<FONT class="PortletText1"><bean:write name="facilityHelper" property="county" /></FONT>
					</TD>
					<TD>
						&nbsp;
					</TD>
					<TD>
						<FONT class="PortletText1"><bean:write name="facilityHelper" property="localReviewStatus" /></FONT>
					</TD>
					<TD>
						<FONT class="PortletText1"><bean:write name="facilityHelper" property="authority" /></FONT>
					</TD>
				</tr>
				<c:set var="i" value="${i+1}" />
			</logic:iterate>
			
			<tr><td>&nbsp;</td></tr>

			<!-- If at least one facility is displayed then display footer links -->
			<logic:greaterThan name="facListBean"
					property="numOfFacilities" value="0">

				<TR class="PortletText1">
					<TD align="left" colspan="6">
						<a href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_SELECT_ALL%>')">Select All</a>&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_CLEAR_ALL%>')">Clear All</a>
						<logic:notEqual name="currentRole" property="locationTypeId" value="Local">
						&nbsp;&nbsp;|&nbsp;&nbsp;
						<A href="javascript: showOrHideImport('block')">Import</A>&nbsp;&nbsp;|&nbsp;&nbsp;
						<A href="javascript: ExportFacilities('this')">Export</A>&nbsp;&nbsp;|&nbsp;&nbsp;
						</logic:notEqual>
						<!-- <a href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_MY_FACILITIES%>')">My Facilities</a>  -->
					</TD>
					<TD colspan="4" align="right">

						<!-- Prev -->
						<logic:greaterThan name="facListBean"
							property="prevFacilityToDisplay" value="0">
							<A href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_FIRST%>')">|&lt;&lt;</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							<A href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_PREVIOUS%>')">Prev</A>&nbsp;&nbsp;|&nbsp;&nbsp;
						</logic:greaterThan>
						<logic:lessEqual name="facListBean"	property="prevFacilityToDisplay" value="0">
						   |&lt;&lt;&nbsp;|&nbsp;
							Prev&nbsp;|&nbsp;
						</logic:lessEqual>

						<!-- x to y of z facilities-->
						<bean:write name="facListBean" property="fromFacility" />
						-
						<bean:write name="facListBean" property="toFacility" />
						of
						<bean:write name="facListBean" property="numOfFacilities" />
						&nbsp;|&nbsp;
	
						<!-- Next -->
						<bean:define id="maxFacility"><bean:write name="facListBean" property="numOfFacilities" /></bean:define>					
						<logic:lessThan name="facListBean" property="toFacility" value="<%=maxFacility%>">
							    <A href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_NEXT%>')">Next</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							    <A href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_LAST%>')">&gt;&gt;|</A>&nbsp;&nbsp;
						</logic:lessThan>	
					
						<logic:greaterEqual name="facListBean" property="toFacility" value="<%=maxFacility%>">
							Next&nbsp;&nbsp;|&nbsp;&nbsp;
							&gt;&gt;|&nbsp;&nbsp;
						</logic:greaterEqual>	
					</TD>
				</TR>

			
			<TR class="PortletText1">
				<TD align="left" colspan="7">
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_FRR%>"><A href="javascript: setActionAndSubmit('<%=FacilityDAO.FEDERAL_REVIEW_REQUESTED%>')">Federal Review Requested</A>&nbsp;&nbsp;|&nbsp;&nbsp;</logic:notEmpty>
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_LA%>"><a href="javascript: setActionAndSubmit('<%=FacilityDAO.LOCAL_ASSIGNED%>')">Local Assigned</a>&nbsp;&nbsp;|&nbsp;&nbsp;</logic:notEmpty>
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_FA%>"><A href="javascript: setActionAndSubmit('<%=FacilityDAO.FEDERAL_ACCEPTED%>')">Federal Accepted</A>&nbsp;&nbsp;|&nbsp;&nbsp;</logic:notEmpty>
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_SCR%>"><a href="javascript: setActionAndSubmit('<%=FacilityDAO.STATE_CORRECTION_REQUESTED%>')">State Correction Requested</a>&nbsp;&nbsp;|&nbsp;&nbsp;</logic:notEmpty>
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_SA%>"><a href="javascript: setActionAndSubmit('<%=FacilityDAO.STATE_ASSIGNED%>')">State Assigned</a>&nbsp;&nbsp;|&nbsp;&nbsp;</logic:notEmpty>
					<logic:notEmpty name="facilityIdsNotUpdated"><a href="javascript:displayErrorFacilities()">Change Review Status Error</a></logic:notEmpty>
					<!-- Only for local users -->
					<logic:notEmpty name="<%=FacilityListDisplayAction.DISPLAY_REQUEST_ACCESS%>"><A href="javascript: setActionAndSubmit('<%=FacilityListDisplayAction.ACTION_REQUEST_ACCESS%>')">Request Access</A></logic:notEmpty>
				</TD>
			</TR>		
			</logic:greaterThan>
			<logic:lessEqual name="facListBean"
					property="numOfFacilities" value="0">
					
			<TR class="PortletText1">
				<TD align="center" colspan="9">
					<b>No Facilities Match The Search Criteria! <bean:write name="facListBean" property="numOfFacilities"/></b>
				</TD>
			</TR>
									
			</logic:lessEqual>					
							
	</TABLE>

</pdk-html:form>

<!-- FACILITY IMPORT -->

<DIV id="import" style="DISPLAY:none">   

<SCRIPT type=text/javascript>
	function validateAndSubmit() {
		importFile = window.document.getElementById("importFile");	
		if (importFile.value.length < 1) 
		{
			alert("You must enter a filename to upload.");
			return false;
		}
		else 
		{
			return true;
		}
}
</SCRIPT>

<form action="<%=importUrl%>"  method="post" enctype="multipart/form-data" >    
 	<input type="hidden" name="forwardUrl" value="<%=forwardUrl%>" />	
 	<input type="hidden" name=<%=FacilityListDisplayAction.IMPORT_EXPORT_KEY%> 
 	                     value="<%=facListForm.getImportExportKey()%>" />	
	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0"	align="right">
		<TR class="PortletSubHeaderColor"><TD>&nbsp;</Td></tr>
		<TR class="PortletSubHeaderColor">
			<TH class="PortletSubHeaderText">
				<center>Facility Import</center>
			</TH>
		</tr>
		
		<TR class="PortletSubHeaderColor">
			<TH class="PortletSubHeaderText">
    			Select File: <input type="file" name="importFile"> <br><br>
			</TH>
		</tr>

		<TR class="PortletSubHeaderColor">
			<TH class="PortletSubHeaderText">
 				<input type="submit" value="Upload File"></input>
			</TH>
		</tr>
	</TABLE>

</form>


</DIV> 

<logic:notEmpty name="facilityIdsNotUpdated">
	<SCRIPT type=text/javascript>
	function displayErrorFacilities(){
		
		msgWindow=window.open("","ReviewStatusReport","menubar=no,scrollbars=yes,status=no,width=300,height=300");
		msgWindow.document.write("<%=facilityIdsNotUpdated%>");
	}
	</SCRIPT>
</logic:notEmpty> 

