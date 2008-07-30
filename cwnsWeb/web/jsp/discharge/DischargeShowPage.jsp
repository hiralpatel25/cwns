<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@ page import="java.util.Collection"
         import="java.util.Iterator"
         import="gov.epa.owm.mtb.cwns.model.DischargeMethodRef" %>
         
<%@ page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="oracle.portal.utils.NameValue"
	%>

<%@ page 
	import="gov.epa.owm.mtb.cwns.discharge.DischargeAction"
	import="gov.epa.owm.mtb.cwns.service.DischargeService"
	import="gov.epa.owm.mtb.cwns.discharge.DischargeForm" %>
<%
			PortletRenderRequest pRequest = (PortletRenderRequest) request
				.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			DischargeForm dForm = (DischargeForm)request.getAttribute("dForm");
			String methodId = dForm.getMethodId();

			NameValue[] linkParams       = new NameValue[2];
			linkParams[0] = new NameValue("formId", "dischargeForm");
			linkParams[1] = new NameValue("facilityId", 
		                               new Long(dForm.getFacilityId()).toString());
			
%>


<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pRequest,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>
<%--
window.onload=function(){
<logic:present name="warnMessages">
	    window.document.getElementById("warnMessageId").style.display = "none";
</logic:present>
}--%>

// Initialize screen attributes when the jsp is loaded
function dischargeScreenOnLoad() {

	<%if (DischargeAction.ACTION_ADD.equals(dForm.getAction()) ||
	      DischargeAction.ACTION_EDIT.equals(dForm.getAction())) {%>

		form = window.document.dischargeForm;
		indicateRequiredInput(form);
		displayDischargeLocation(form);
	<%}%>
	
}


// Enable or disable the user's ability to enter a (present or projected)
// flow value. 
function indicateRequiredInput(form) {

	// determine the discharge method status
	status = getDischargeStatus(form);

	typeId = getSelectedDischargeMethodId(form);


	if (status == '<%=DischargeForm.PRESENT%>') {
		window.document.getElementById('presentFlowInput').disabled = "";
		window.document.getElementById('projectedFlowInput').disabled = "true";
		window.document.getElementById('projectedFlowInput').value = " ";
		if ( typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>' ) {
			setFlowRequired('presentFlow', 'on');
			setFlowRequired('projectedFlow', 'off');
		}
	} else if (status == '<%=DischargeForm.PROJECTED%>') {
		window.document.getElementById('presentFlowInput').disabled = "true";
		window.document.getElementById('presentFlowInput').value = " ";
		
		window.document.getElementById('projectedFlowInput').disabled = "";
		if ( typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>' ) {
			setFlowRequired('projectedFlow', 'on');
			setFlowRequired('presentFlow', 'off');
		} 	
	} else if (status == '<%=DischargeForm.PRESENT_AND_PROJECTED%>') {
		window.document.getElementById('presentFlowInput').disabled = "";
		window.document.getElementById('projectedFlowInput').disabled = "";
		if ( typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>' ) {
			setFlowRequired('presentFlow', 'on');
			setFlowRequired('projectedFlow', 'on');
		}
	} 
	
	if ( typeId != '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>' ) {
		setFlowRequired('presentFlow', 'off');
		setFlowRequired('projectedFlow', 'off');
	}
	
}


// Display or hide the astericks that indicate if a value is required
function setFlowRequired(flow, action) {

	var flowReq = flow+'Req';

	if (action == "off") {
      	window.document.getElementById(flow).style.display='block';
      	window.document.getElementById(flowReq).style.display='none';
    } else {
      	window.document.getElementById(flow).style.display='none';
      	window.document.getElementById(flowReq).style.display='block';
    }
}



// Return the selected discharge status (present, projected, presentAndProjected)
function getDischargeStatus(form) {
	status = "";
	for (i=0; i<3; i++){
		if(form.dischargeStatus[i].checked) var status = form.dischargeStatus[i].value;
	}
	return status
}

// When a discharge method is changed the follow must be done:
// 1.) Determine if the discharge location information should be 
//     displayed.
// 2.) Enable or disable status values (present, projected or presentAndProjected)

function dischargeMethodChanged(form) {

	// Deterimine if the discharge location should be displayed
	displayDischargeLocation(form);
	
	// Inicate Required input
	indicateRequiredInput(form);

	// Enable/disable allowable statuses
	enableAllowableStatuses(form);

}

// Enable allowable statuses 
function enableAllowableStatuses(form) {


}

function getSelectedDischargeMethodId(form) {

	var selType=form['selectMethodId'];
	var indx = selType.selectedIndex;
	if (indx != null) {
		return selType.options[indx].value;
	} else {
		return selType.value;
	}
}

// Hide or display the discharge location 
function displayDischargeLocation(form) {

	<%if (DischargeAction.ACTION_ADD.equals(dForm.getAction())) {%>

		var typeId = getSelectedDischargeMethodId(form);
	
		if ( typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>' ) {
			window.document.getElementById('dischargeLocation').style.display='block';
		} else {
			window.document.getElementById('dischargeLocation').style.display='none';
		}
	<%}else if (DischargeAction.ACTION_EDIT.equals(dForm.getAction())) {%>
		if (typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>') {
			window.document.getElementById('dischargeLocation').style.display='block';
		} else {
			window.document.getElementById('dischargeLocation').style.display='none';
		}
	<%}%>
}

// Validate and submit
function validateDischargeAndSubmit(dischargeId,action) {
	msg = "";
	form               = window.document.dischargeForm;
	status             = getDischargeStatus(form);
	cwns_nbr           = window.document.getElementById('d_cwnsNumber').value;
	presentFlowInput   = window.document.getElementById('presentFlowInput').value;
	projectedFlowInput = window.document.getElementById('projectedFlowInput').value;

	var typeId = getSelectedDischargeMethodId(form);
	if (typeId == '<%=DischargeService.SELECT_DISCHARGE_METHOD%>') {
		if (msg.length > 0) msg = msg + '\n';
		msg = msg + 'You must select a Discharge Method';
		alert(msg);
		return false;
	}

	if (typeId != '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>') {
	// check Present Flow
	if (status == '<%=DischargeForm.PRESENT%>' ||
  	    status == '<%=DischargeForm.PRESENT_AND_PROJECTED%>') {
	
		if (!validateFlow(presentFlowInput,typeId)) {
			if (msg.length > 0) msg = msg + '\n';
			msg = msg + 'Present Flow must be a number between 0 and 100';
		}
	}
		
	// check Projected Flow
	if (status == '<%=DischargeForm.PROJECTED%>' ||
  	    status == '<%=DischargeForm.PRESENT_AND_PROJECTED%>') {
	
		if (!validateFlow(projectedFlowInput,typeId)) {
			if (msg.length > 0) msg = msg + '\n';
			msg = msg + 'Projected Flow must be a number between 0 and 100';
		}
	}
	}	
   
	if ( typeId == '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>') {
	
		// Flow values must be provided
	
		// check Present Flow
		if (status == '<%=DischargeForm.PRESENT%>' ||
  	    	status == '<%=DischargeForm.PRESENT_AND_PROJECTED%>') {
	
			if (presentFlowInput.length == 0) {
				if (msg.length > 0) msg = msg + '\n';
				msg = msg + 'A Present Flow value must be entered';
			}
			if (!validateFlow(presentFlowInput,typeId)) {
			   if (msg.length > 0) msg = msg + '\n';
			   msg = msg + 'Present Flow must be a number between 1 and 100';
		    }
		}
		
		// check Projected Flow
		if (status == '<%=DischargeForm.PROJECTED%>' ||
	  	    status == '<%=DischargeForm.PRESENT_AND_PROJECTED%>') {
	
			if (projectedFlowInput.length == 0) {
				if (msg.length > 0) msg = msg + '\n';
				msg = msg + 'A Projected Flow value must be entered';
			}
			if (!validateFlow(projectedFlowInput,typeId)) {
			   if (msg.length > 0) msg = msg + '\n';
			    msg = msg + 'Projected Flow must be a number between 1 and 100';
		    }
		}
	
		// must check for CWNS number
		if (cwns_nbr.length < 1) {
			if (msg.length > 0) msg = msg + '\n';
			msg = msg + 'A CWNS Number must be selected';
		}
		
	} else if ( typeId == '<%=DischargeService.DISCHARGE_OTHER%>') {
		warn = 'Please explain why a Discharge method of Other has been assigned\n' +
		       'to this facility by entering a comment in the Discharge Commment area';
		alert(warn);
	}
		
	if (msg.length == 0) {
		setDischargeActionAndSubmit(dischargeId,action);
	} else {
		alert(msg);
	}
}


// Validate that the flow is between 0 and 100
function validateFlow(flow,typeId) {

	num = trim(flow);

	if (num.length == 0) {
		return true;
	}
	
	if (isNaN(num)) {
		alert('not a number');
	}
	if (typeId != '<%=DischargeService.DISCHARGE_TO_ANOTHER_FACILITY%>') {
	   if (parseInt(num) < '0' || parseInt(num) > '100') {
		  return false;
	   }
	}
	else {
	     if (parseInt(num) < '1' || parseInt(num) > '100') {
		  return false;
	   }
	}
	   
	return true;
}


function convStrToInt(str)
{
  if(str == null || str == "")
    return 0;
  else
    return parseInt(str);
}

            
// Trim whitespace from left and right sides of s.
function trim(s) {
    return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
}


// Set the action and submit
function setDischargeActionAndSubmit(dischargeId, action) {
	window.document.getElementById('dischargeId').value    = dischargeId;
	window.document.getElementById('dischargeAction').value    = action;
	window.document.dischargeForm.submit();
}

function showFacilitySelectWindow(popupUrl,locationId)
{
    var w = 600;
    var h = 600;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    var url = popupUrl+'&_event_locationId='+ locationId;
    var w = window.open(url, 'DischargeHelp', settings);
  if(w != null) 
    w.focus();     
}



// Build the array used to determine the allowable status for a 
// particular Discharge Method

var i = 0;
var validStatuses = new Array();
<logic:iterate id="method" name="dischargeMethodHelpers" >
   validStatuses[i++] = new Array('<bean:write name="method" property="present"/>',
                                  '<bean:write name="method" property="projected"/>');
</logic:iterate>		

</SCRIPT>

<body  onload="dischargeScreenOnLoad();">
<pdk-html:form method="post" 
	name="dischargeForm" 
	type="gov.epa.owm.mtb.cwns.discharge.DischargeForm" 
	styleId="dischargeForm"
	action="discharge.do">

<%-- Hidden Fields --%>
<pdk-html:hidden styleId="dischargeAction" name="dischargeForm" property="action"/>
<pdk-html:hidden styleId="dischargeId"     name="dischargeForm" property="dischargeId"/>
<pdk-html:hidden styleId="d_cwnsNumber"    name="dischargeForm" property="cwnsNumber"/>
<pdk-html:hidden styleId="d_name"    name="dischargeForm" property="name"/>
<pdk-html:hidden styleId="d_facilityLocationId" name="dischargeForm" property="facilityLocationId"/>
<div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="warnMessage" name="warnMessages" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/warn24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=warnMessage %>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>     
      
	<TABLE border="0" width="100%" class="PortletText1">
		<TR class="PortletHeaderColor">

			<TD class="PortletHeaderText">
				<P align="center">
					Method
				</P>
			</TD>
			<TD  class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Present
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Flow Portion %
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Projected
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center" style="COLOR: white">
					Flow Portion %
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center">
					Location
				</P>
			</TD>

			<%if (dForm.isUpdateable()) { %>
				<TD class="PortletHeaderText">
					<P align="center" style="COLOR: white">
						Edit
					</P>
				</TD>
				<TD class="PortletHeaderText">
					<P align="center" style="COLOR: white">
						Delete
					</P>
				</TD>
			<%} else{%>
			<logic:equal name="dischargeForm" property="isLocalUser" value="Y">
			<TD class="PortletHeaderText">
					<P align="center" style="COLOR: white">
						Delete
					</P>
			</TD>
			</logic:equal>
			<%} %>
		</TR>

	<c:set var="i" value="0" />
	<logic:iterate id="discharge" name="facilityDischarges" >
		<c:choose>
			<c:when test='${i%2=="0"}'>
				<c:set var="class" value="PortletSubHeaderColor" />   
			</c:when>
			<c:otherwise>
				<c:set var="class" value="" />
			</c:otherwise>
		</c:choose>
		
		<TR class="<c:out value="${class}"/>">

			<TD>
				<bean:write name="discharge" property="dischargeMethodRef.name" />
			</TD>
			<TD>
				<logic:equal name="discharge" property="presentFlag" value="Y">
					<P align="center">
						<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"
							title="present"/>
					</P>
				</logic:equal>	
			</TD>
			<td>
				<logic:equal name="discharge" property="presentFlag" value="Y">
					<P align="center">
						<bean:write name="discharge" property="presentFlowPortionPersent" />
					</P>				
				</logic:equal>	
			</td>
			<TD>
				<logic:equal name="discharge" property="projectedFlag" value="Y">
					<P align="center">
						<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"
							title="projected"/>
					</P>
				</logic:equal>	
			</TD>
			<TD>
				<logic:equal name="discharge" property="projectedFlag" value="Y">
					<P align="center">
						<bean:write name="discharge" property="projectedFlowPortionPersent" />
					</P>
				</logic:equal>	
			</TD>
			<TD>
				<P align="center">
					<logic:present name="discharge" property="facilityByFacilityIdDischargeTo"  >
						<bean:write name="discharge" property="facilityByFacilityIdDischargeTo.cwnsNbr" />
					</logic:present>
				</P>
			</TD>

			<%if (dForm.isUpdateable()) { %>
				<TD>
					<P align="center">
					    <A href="javascript: setDischargeActionAndSubmit('<bean:write name="discharge" property="facilityDischargeId" />','<%=DischargeAction.ACTION_EDIT%>')">
							<pdk-html:img page="/images/edit.gif" alt="Checkmark" border="0" title="edit"/>
					    </A>
					</P>
				</TD>
				<logic:equal name="dischargeForm" property="isLocalUser" value="Y">
				<bean:define id="delete">
				    javascript: setDischargeActionAndSubmit('<bean:write name="discharge" property="facilityDischargeId" />','<%=DischargeAction.ACTION_DELETE%>')
				</bean:define>
				<TD>
					<P align="center">
					    
					    <pdk-html:checkbox name="discharge" property="feedbackDeleteFlag" value="Y" 
					            onclick='<%=delete%>'>
					    </pdk-html:checkbox>
					</P>
				</TD>
				</logic:equal>
				<logic:notEqual name="dischargeForm" property="isLocalUser" value="Y">
				<TD>
					<P align="center">
					    <A href="javascript: setDischargeActionAndSubmit('<bean:write name="discharge" property="facilityDischargeId" />','<%=DischargeAction.ACTION_DELETE%>')" >
							<pdk-html:img page="/images/delete.gif" alt="Checkmark"  border="0" title="delete"/>
					    </A>
					</P>
				</TD>
				</logic:notEqual>
			<%} else{%>
		<logic:equal name="dischargeForm" property="isLocalUser" value="Y">
		<TD>
					<P align="center">
					    
					    <pdk-html:checkbox name="discharge" property="feedbackDeleteFlag" value="Y" 
					            disabled="true">
					    </pdk-html:checkbox>
					</P>
				</TD>
		</logic:equal>
		<%} %>
		</TR>
		<c:set var="i" value="${i+1}" />
		</logic:iterate>		

	</TABLE>

<logic:notEqual name="dischargeForm" property="displayDetails" value="Y">				
		&nbsp;&nbsp;
		<%if (dForm.isUpdateable()) { %>
		  <DIV class="PortletText1">
			&nbsp;&nbsp;<A href="javascript: setDischargeActionAndSubmit('new','<%=DischargeAction.ACTION_ADD%>')" >Add Discharge</A>
		  </DIV>
		<%}%>
	
</logic:notEqual>	

<!--  Add/Edit Discharge -->
<logic:equal name="dischargeForm" property="displayDetails" value="Y">	
	<DIV  class="PortletText1" style="background-color: #cccccc;padding: 5;DISPLAY:block">   
<BR>

<strong><FONT size="2">	Add/Edit &nbsp;Discharge </FONT></strong>

<font color="red"><html:errors /></font>

	<TABLE border="0" class="PortletText1">
		<tr>
			<td valign="top">
				<TABLE border="0" class="PortletText1" >
				<tr>
					<%if (DischargeAction.ACTION_ADD.equals(dForm.getAction())) {%>
						<td><font color="red">*</font>&nbsp;&nbsp;<strong>Method:</strong></td>
					<%}else {%>
						<td>&nbsp;&nbsp;&nbsp;<strong>Method:</strong></td>
					<%}%>
					<logic:equal name="dischargeForm" property="action" value="<%=DischargeAction.ACTION_ADD%>">				
					<td colspan="2">					
						<pdk-html:select size="1"  name ="dischargeForm" styleId="selectMethodId" onchange="dischargeMethodChanged(form)" property="methodId" >  
		 					<OPTION value='xx' selected="selected">Select Discharge Method</OPTION> 

							<logic:iterate id="method" name="dischargeMethodHelpers" >

						  		<logic:match name="method" property="id" value='<%=methodId%>'>				
				 					<OPTION value='<bean:write name="method" property="id"/>' selected="selected"> <bean:write name="method" property="name"/></OPTION> 
				 				</logic:match>
						  		<logic:notMatch name="method" property="id" value='<%=methodId%>'>
			 						<OPTION value='<bean:write name="method" property="id"/>'> <bean:write name="method" property="name"/></OPTION> 
				 				</logic:notMatch>

					        </logic:iterate>
			           </pdk-html:select>  
					</td>
					</logic:equal>	
					
					<logic:notEqual name="dischargeForm" property="action" value="<%=DischargeAction.ACTION_ADD%>">				
					<td colspan="2">
	 					 <bean:write name="dischargeForm" property="methodName"/>
						 <pdk-html:hidden styleId="selectMethodId" name="dischargeForm" property="methodId"/>
					</td>
					</logic:notEqual>
							
				</tr>
				
				<tr>
					<td nowrap="nowrap" colspan="3" ><font color="red">*</font>&nbsp;&nbsp;<strong>Status:</strong></td>
				</tr>

				<tr>
					<td colspan = "3">
					    <pdk-html:radio name="dischargeForm" styleId="dischargeStatus" property="status" onclick="indicateRequiredInput(form)" 
					         value="<%=DischargeForm.PRESENT%>" disabled="<%=dForm.isPresentFlowUpdatable()?false:true%>"/>&nbsp;Present&nbsp;&nbsp;
						 <pdk-html:radio name="dischargeForm" styleId="dischargeStatus" property="status" onclick="indicateRequiredInput(form)" 
						     value="<%=DischargeForm.PROJECTED%>" disabled="<%=dForm.isProjectedFlowUpdatable()?false:true%>"/>&nbsp;Projected&nbsp;&nbsp;
						<pdk-html:radio name="dischargeForm" styleId="dischargeStatus" property="status" onclick="indicateRequiredInput(form)" 
						     value="<%=DischargeForm.PRESENT_AND_PROJECTED%>" disabled="<%=(dForm.isPresentFlowUpdatable()&&dForm.isProjectedFlowUpdatable())?false:true%>"/>&nbsp;Present and Projected&nbsp;&nbsp;
					</td>
				</tr>
                
				<tr>
					<td>
						<div id="presentFlow" style="float:left; display: none"><p class="required">&nbsp;</p></div><div id="presentFlowReq" style="float:left; display: block"><p class="required">*&nbsp;</p></div>
						<strong>Present Flow %:</strong>
					</td>
					<td colspan="2">
						<pdk-html:text name="dischargeForm" styleId="presentFlowInput" property="presentFlow" size="4" maxlength="3" />
					</td>
					
				</tr>
								
				<tr>
					<td nowrap="nowrap">
						<div id="projectedFlow" style="float:left; display: none"><p class="required">&nbsp;</p></div><div id="projectedFlowReq" style="float:left; display: block"><p class="required">*&nbsp;</p></div>
						<strong>Projected Flow %:</strong>
					</td>
					<td nowrap="nowrap" colspan="2">
						<pdk-html:text name="dischargeForm" styleId="projectedFlowInput" property="projectedFlow" size="4" maxlength="3" />
					</td>
					
				</tr>
				</TABLE>	
			</td>
			<td>
			
				<!-- DISCHARGE LOCATION  -->
				<DIV id="dischargeLocation" style="padding: 5;DISPLAY:block">   
			
				<TABLE border="0" class="PortletText1">
					<tr>
						<td colspan="3" nowrap="nowrap"><strong><FONT size="2">Discharge Location</FONT></strong>&nbsp;&nbsp;
						</td>
						<td nowrap="nowrap">
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font></td>
						<td><strong>CWNS Number:</strong>
						
						</td>
						<td nowrap="nowrap">
							<span id="cwnsDischargeNumber">
								<bean:write name="dischargeForm"  property="cwnsNumber" />
							</span>
							   <%String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(pRequest,"FacilitySelectPage", linkParams,true, true);%>
							    &nbsp;&nbsp;
							   <A href="javascript:void(0);" onclick='showFacilitySelectWindow("<%=eventPopupWinowUrl2%>","<%=dForm.getFacilityLocationId()%>")'">
						        <pdk-html:img page="/images/findsmall.gif" border="0" alt="Lookup Facility"/>
							   </A>
						</td>
					</tr>
					<tr>
						<td></td>
						<td><strong>Facility Name:</strong></td>
						<td>
							<span id="cwnsDischargeName">
								<bean:write name="dischargeForm" property="name" />
							</span>
						</td>
					</tr>
					<tr>
						<td></td>
						<td valign="top"><strong>Description:</strong></td>
						<td>
							<span id="facilityDischargeDescription">
								<bean:write name="dischargeForm" property="facilityDischargeDescription" />
							</span>
						</td>
					</tr>
				</TABLE>
			
				</DIV>
			</td>
		</tr>
		
		
	</TABLE>

    <%if (DischargeAction.ACTION_ADD.equals(dForm.getAction())||DischargeAction.ACTION_PROCESS_ADD.equals(dForm.getAction())) {%>
		<INPUT type="button" name="Save" value="Save" 
			onclick ="javascript: validateDischargeAndSubmit('<bean:write name="dischargeForm" property="dischargeId" />','<%=DischargeAction.ACTION_PROCESS_ADD%>')">&nbsp;&nbsp;
	<%} else{%>
		<INPUT type="button" name="Save" value="Save" 
			onclick ="javascript: validateDischargeAndSubmit('<bean:write name="dischargeForm" property="dischargeId" />','<%=DischargeAction.ACTION_PROCESS_EDIT%>')">&nbsp;&nbsp;
	<%} %>

	<INPUT type="button" 
		onclick="javascript: setDischargeActionAndSubmit('cancel','<%=DischargeAction.ACTION_DEFAULT%>')" value="Cancel"> 

</DIV>
</logic:equal>		
</pdk-html:form>	
 </body>
	
	