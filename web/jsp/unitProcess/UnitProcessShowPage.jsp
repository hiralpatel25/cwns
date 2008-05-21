<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"
	import="oracle.portal.provider.v2.url.UrlUtils"
	import="oracle.portal.utils.NameValue"
	import="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessForm"
	import="gov.epa.owm.mtb.cwns.unitProcess.*"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		    String ajaxurl = url +"/javascript/prototype.js";
		    String listboxurl = url +"/javascript/listbox.js";	
		    			
   			UnitProcessForm unitProcessForm = (UnitProcessForm)request.getAttribute("unitProcessForm");	
  			
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<script type="text/javascript" language="JavaScript" src="<%=ajaxurl%>"></script>
<script type="text/javascript" language="JavaScript" src="<%=listboxurl%>"></script>

<SCRIPT type=text/javascript>
// Removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

// Removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

// Removes leading and ending whitespaces
function trim( value ) {
	
	return LTrim(RTrim(value));
	
}

function get_radio_value(form_name, radio_name)
{
    var form = window.document.getElementById(form_name);

    var radioObj = form.elements[radio_name];
    
	if(!radioObj)
		return -1;
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return -1;
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return -1;

}

function checkUPNoChangeRule(listName)
{
   var returnResult = false;
   var hasNoChange = false;
   var hasOther = false;

   var availCtrl=window.document.getElementById(listName);	
   
   var nochangeOption;

   //alert(availCtrl.options.length);
   
   for (var Current=0;Current < availCtrl.options.length;Current++) 
   {
     if (availCtrl.options[Current].selected) 
     {
          var selval = availCtrl.options[Current].value;
          //alert(selval);
          if(selval == 1)
          {
            nochangeOption = availCtrl.options[Current];
            hasNoChange = true;
          }
          else
            hasOther = true;
     }
   }
   
   if(hasNoChange && hasOther)
   {
     alert("Error: No Change can not be selected with other change types.");
     
	 availCtrl.selectedIndex = -1;
	   
     return false;
   }
   else
   {
     // remove no change
     if(hasOther)
     {
		  for (var Current=availCtrl.options.length-1;Current >=0 ;Current--) 
		   {
		          var selval = availCtrl.options[Current].value;
		          
		          if(selval == 1)
		          {
		            availCtrl.options[Current] = null;
		            break;
		          }
		   }   
     }
     else if(hasNoChange) // remove all others
	 {
	  for (var Current=availCtrl.options.length-1;Current >= 0 ;Current--) 
	   {
	          var selval = availCtrl.options[Current].value;
	          
	          if(selval != 1)
	            availCtrl.options[Current] = null;
	   }     
     }
     
     return true;
   }

}

function repopulateAvailForNoChangeRule()
{
   var selectedCtrl=window.document.getElementById('selectedChangeType');	   
   
   if(selectedCtrl.options.length > 0)
     return;  

   if(window.document.getElementById('presentProjectedFlag_presentProject').checked)
   {
   	 adjustUPAavailChange('presentProject', true);
   }
}

function adjustUPAavailChange(radioType, pp)
{
   var availCtrl=window.document.getElementById('availChangeType');	   
   var selectedCtrl=window.document.getElementById('selectedChangeType');
   var globalCtrl=window.document.getElementById('hiddenGlobalChangeType');
   
   var 	selectedCtrlLength = selectedCtrl.options.length;
   var  selectedOneValue;
   
   if(selectedCtrlLength == 1)
   {
   		selectedOneValue = selectedCtrl.options[0].value;
   }

	if(radioType == 'present' && window.document.getElementById('presentProjectedFlag_present').checked)
	{
   	
	availCtrl.options.length = 0;
	selectedCtrl.options.length = 0;
	   	
		selectedCtrl.options[0] = new Option('Abandonment', '7');
	}
	else if(radioType == 'project'  && window.document.getElementById('presentProjectedFlag_project').checked)
	{
   	
	availCtrl.options.length = 0;
	selectedCtrl.options.length = 0;
	   		
		selectedCtrl.options[0] = new Option('New', '2');
	}
	else if(radioType == 'presentProject'  && window.document.getElementById('presentProjectedFlag_presentProject').checked)
	{
		if((selectedCtrlLength == 1 && selectedOneValue != 1) || pp == true)
		{		
			availCtrl.options.length = 0;
			selectedCtrl.options.length = 0;
	   		
            for (var Current=globalCtrl.options.length-1; Current >=0; Current--) 
            {
              if (globalCtrl.options[Current].value != 2 && globalCtrl.options[Current].value != 7) 
              {

                        var selval = globalCtrl.options[Current].value;

                        var seltext = globalCtrl.options[Current].text;     

                        addOptionToSelectedList(availCtrl, selval, seltext);
              }
            }	
         }	
	}		
}

// Set the value of the "action" hidden attribute
function UnitProcessConfirmAndSubmit(action, treatmentTypeId, radio_name, unitProcessListId, changeTypeId)
{

	window.document.getElementById("treatmentTypeId").value=treatmentTypeId;
	window.document.getElementById("unitProcessListId").value=unitProcessListId;
	window.document.getElementById("changeTypeListId").value=changeTypeId;
	
	if(radio_name !="")
	{
		window.document.getElementById("unitProcessRadioId").value=get_radio_value('UnitProcessFormBeanId', radio_name);
		//alert(window.document.getElementById("unitProcessRadioId").value);
	}

   if(action == "save")
   {
    // validate controls
    
	   var upCtrl = window.document.getElementById("unitProcessFormId")
	   if(upCtrl.value == "")
       {
		    alert("Error: Unit Process is required.");
  			upCtrl.focus();
  			return;	  	       	 
       }

      var plnYrCtrl = window.document.getElementById("planYear")

	  if(trim(plnYrCtrl.value) == '')
	  	plnYrCtrl.value = '';
	  else if(isNaN(parseInt(plnYrCtrl.value)) || plnYrCtrl.value.indexOf(".") >=0 ||plnYrCtrl.value < <%=unitProcessForm.getCurrentYear()%>)
      {
		alert("Error: Planned Year must be greater than the current year <%=unitProcessForm.getCurrentYear()%>.");
		plnYrCtrl.focus();
		return;      	
      }           

       var selectedList = window.document.getElementById("selectedChangeType")
	   if(selectedList.options.length > 0)
	   {
	    resultStr = selectedList.options[0].value;
	    
		for (i=1;i<selectedList.options.length;i++) 
		{
			resultStr = resultStr + ',' + selectedList.options[i].value;
		}
	   }
	   else
	   {
		alert("Error: At least one Change must be selected.");   	
		return;
	   }
	   
	  //alert(resultStr);
   	  window.document.getElementById("commaDilimitedChangeTypeIds").value = resultStr;    
    
      	// edit, just save
       hidden_field = window.document.getElementById("UnitProcessAct");
	   hidden_field.value=action;
	   window.document.UnitProcessFormBean.submit();          	

   }
   else if(action == "delete")
   {
     //var result = confirm("Continue with Delete?");
      //if (result == false)
      //{
		//return;
      //}
   }
   else if(action == "edit")
   {
   }
   
   if(action != "save")
   {
       hidden_field = window.document.getElementById("UnitProcessAct");
	   hidden_field.value=action;
	   
	   window.document.UnitProcessFormBean.submit();
	   return true;
   }
}

function unitProcessEnableMoveButtons(ctrl, buttonName)
{
	//alert(ctrl.options.length);
	
	var nothingSelected = true;
	
		for(var ii = ctrl.options.length-1; ii >= 0; ii--)
		{
			if(ctrl.options[ii].selected == true)
			{
				nothingSelected = false;
				window.document.getElementById(buttonName).disabled = false;
				//alert("something selected");
				break;
			}
		}
		
		if(nothingSelected == true)
		{
			window.document.getElementById(buttonName).disabled = true;
		}
	
}

// Show or Hide the add comment area of the Portlet
function showOrHideUnitProcessDetails(id, mode)
{
     var d = window.document.getElementById(id);
     
     if(d!=null)
       d.style.display=mode;
}

function formatCurrency(num) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
	//return (((sign)?'':'-') + '$' + num + '.' + cents);	
	return num;
}

function unitProcessChangeUpDownArrow(treatmentId, totalUnitProcessNum, currentRadioNum)
{

	var upArrawEnabled = window.document.getElementById("up_" + treatmentId + "_enabled");
	var upArrawDisabled = window.document.getElementById("up_" + treatmentId + "_disabled");
	var downArrawEnabled = window.document.getElementById("down_" + treatmentId + "_enabled");
	var downArrawDisabled = window.document.getElementById("down_" + treatmentId + "_disabled");

	if(currentRadioNum == 1)
	{
		//disable up
		upArrawEnabled.style.display="none";
		upArrawDisabled.style.display="block";
	}
	else
	{
		//enable up
		upArrawDisabled.style.display="none";
		upArrawEnabled.style.display="block";
		
	}
	
	if(currentRadioNum == totalUnitProcessNum)
	{
		//disable down
		downArrawEnabled.style.display="none";
		downArrawDisabled.style.display="block";
	}
	else
	{
		//enable down
		downArrawDisabled.style.display="none";
		downArrawEnabled.style.display="block";
		
	}
}

</SCRIPT>

<pdk-html:form 	name="UnitProcessFormBean" styleId="UnitProcessFormBeanId"
				type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessForm"
				action="unitProcess.do">
	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="unitProcessAct" property="unitProcessAct" value="<%=unitProcessForm.getUnitProcessAct()%>"/>
		<pdk-html:text styleId="unitProcessFacilityId" property="unitProcessFacilityId" value="<%=unitProcessForm.getUnitProcessFacilityId()%>"/>
		<pdk-html:text styleId="unitProcessIsAddAction" property="unitProcessIsAddAction" value="<%=unitProcessForm.getIsAddAction()%>"/>
		<pdk-html:text styleId="treatmentTypeId" property="treatmentTypeId" value="<%=unitProcessForm.getTreatmentTypeId()%>"/>
		<pdk-html:text styleId="unitProcessListId" property="unitProcessListId" value="<%=unitProcessForm.getUnitProcessListId()%>"/>
		<pdk-html:text styleId="changeTypeListId" property="changeTypeListId" value="<%=unitProcessForm.getChangeTypeListId()%>"/>
		<pdk-html:text styleId="unitProcessRadioId" property="unitProcessRadioId" value="<%=unitProcessForm.getUnitProcessRadioId()%>"/>
        <pdk-html:hidden name="UnitProcessFormBean" styleId="commaDilimitedChangeTypeIds" property="commaDilimitedChangeTypeIds"/>		
        <input type="hidden" name="presentProjectRadioType" id="presentProjectRadioType" value="<%=unitProcessForm.getPresentProjectedFlag()%>"/>
	</DIV>

<logic:equal name="UnitProcessFormBean" property="warnStateUser" value="Y">
<div class="MessageZone">
     <table width="100%" border="0" cellpadding="0" cellspacing="0">
     <tr>
			<td align="center" valign="middle" width="40">
     			<pdk-html:img page="/images/warn24.gif" alt="Warning" border="0"/>	
     		</td>
     		<td class="WarningText" align="left" valign="middle">
     		A Local user has updated Unit Process Information. Any changes may overwrite that data.
     		</td>	      	
     </tr>
     </table>
</div>     
</logic:equal>


<logic:iterate id="unitProcessListHelper" scope="request" name="unitProcessGlobalList" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessListHelper">

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
	<TR>
		<TD align="left">
			<FONT size="3"><STRONG>
			<bean:write name="unitProcessListHelper" property="treatmentTypeName"/>
			</STRONG> </FONT>
		</TD>
	</TR>
	<TR>
		<TD>
		<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%">
			<TR>
			<logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">
				<TD class="PortletHeaderColor" width="8%">
					<P align="center">
					<logic:greaterThan name="unitProcessListHelper" property="numberOfUnitProcesses" value="1">
						<A href="javascript:UnitProcessConfirmAndSubmit('up', <bean:write name="unitProcessListHelper" property="treatmentTypeId"/>, 'radio_<bean:write name="unitProcessListHelper" property="treatmentTypeId"/>', 0, 0)">
								<pdk-html:img page="/images/upArrow.gif" alt="Up" border="0"/></A>
						&nbsp;
						<A href="javascript:UnitProcessConfirmAndSubmit('down', <bean:write name="unitProcessListHelper" property="treatmentTypeId"/>, 'radio_<bean:write name="unitProcessListHelper" property="treatmentTypeId"/>', 0, 0)">
								<pdk-html:img page="/images/downArrow.gif" alt="Down" border="0"/></A>
					</logic:greaterThan>
					<logic:lessThan name="unitProcessListHelper" property="numberOfUnitProcesses" value="2">
							&nbsp;
						&nbsp;
							&nbsp;
					</logic:lessThan>
					</P>
				</TD>
				</logic:equal>
				<TD class="PortletHeaderColor" width="25%">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG>Unit Process</STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG> Present </STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor">
					<DIV align="center">
		
						<FONT color="#ffffff"> <STRONG> Projected </STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG> Backup </STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor" width="20%">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG> Change </STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG> Planned Year </STRONG>
						</FONT>
					</DIV>
				</TD>
				<TD class="PortletHeaderColor" width="15%">
					<DIV align="center">
						<FONT color="#ffffff"> <STRONG> Additional Notes </STRONG>
						</FONT>
					</DIV>
				</TD>
		        <logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">
					<TD class="PortletHeaderColor">
						<DIV align="left">
							<FONT color="#ffffff"> <STRONG> Edit </STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD class="PortletHeaderColor">
						<DIV align="center">
							<FONT color="#ffffff"> <STRONG> Delete </STRONG>
							</FONT>
						</DIV>
					</TD>
		         </logic:equal>
			</TR>

			<c:set var="i" value="1" />
			<bean:define id="unitProcessHelperArrayList" name="unitProcessListHelper" property="unitProcessList" type="java.util.ArrayList"/>
			<logic:iterate id="unitProcessHelper" name="unitProcessHelperArrayList" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessHelper">
			<c:choose>
				<c:when test='${i%2=="0"}'>
					<c:set var="class" value="PortletSubHeaderColor" />   
				</c:when>
				<c:otherwise>
					<c:set var="class" value="" />
				</c:otherwise>
			</c:choose>
			
			<logic:equal name="unitProcessHelper" property="unitProcessId" value="<%=unitProcessForm.getUnitProcessListId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		    </logic:equal>			
			
			<TR  class="<c:out value="${class}"/>">
			<logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">
				<TD>
					<P align="center">
					<logic:greaterEqual name="unitProcessListHelper" property="numberOfUnitProcesses" value="2">
					<logic:equal name='unitProcessHelper' property='unitProcessId' value="<%=unitProcessForm.getUnitProcessRadioId()%>">
						<input type="radio" checked
							   name="radio_<bean:write name='unitProcessListHelper' property='treatmentTypeId'/>" 
							   value="<bean:write name='unitProcessHelper' property='unitProcessId'/>" />
					</logic:equal>
					<logic:notEqual name='unitProcessHelper' property='unitProcessId' value="<%=unitProcessForm.getUnitProcessRadioId()%>">
						<input type="radio"
							   name="radio_<bean:write name='unitProcessListHelper' property='treatmentTypeId'/>" 
							   value="<bean:write name='unitProcessHelper' property='unitProcessId'/>" />
					</logic:notEqual>
					</logic:greaterEqual>
					<logic:lessThan name="unitProcessListHelper" property="numberOfUnitProcesses" value="2">
						&nbsp;
					</logic:lessThan>
					</P>
				</TD>
				</logic:equal>
				
				<TD>
					<bean:write name="unitProcessHelper" property="unitProcessName"/>
				</TD>
				<TD>
					<logic:equal name="unitProcessHelper" property="presentFlag" value="Y">
						<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"/>
					</logic:equal>
				</TD>
				<TD>
					<logic:equal name="unitProcessHelper" property="projectedFlag" value="Y">
						<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"/>
					</logic:equal>
				</TD>
				<TD>
					<logic:equal name="unitProcessHelper" property="backupFlag" value="Y">
						<pdk-html:img page="/images/checkmark.gif" alt="Checkmark" border="0"/>
					</logic:equal>
				</TD>
				<TD>
					<bean:define id="unitProcessChangeHelperArrayList" name="unitProcessHelper" property="changeTypeList" type="java.util.ArrayList"/>
					<logic:iterate id="unitProcessChangeHelper" name="unitProcessChangeHelperArrayList" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper">
						<bean:write name="unitProcessChangeHelper" property="changeTypeName"/><br/>
					</logic:iterate>				
				</TD>
				<TD>
					<logic:greaterThan name="unitProcessHelper" property="plannedYear" value="0">
						<bean:write name="unitProcessHelper" property="plannedYear"/>
					</logic:greaterThan>
				</TD>
				<TD>
					<bean:write name="unitProcessHelper" property="description"/>
				</TD>
		        <logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">
					<TD align="center">
						<A href="javascript:UnitProcessConfirmAndSubmit('edit', <bean:write name='unitProcessListHelper' property='treatmentTypeId'/>, '', <bean:write name='unitProcessHelper' property='unitProcessId'/>, 0)">
							<pdk-html:img page="/images/edit.gif" alt="Edit" border="0"/>
						</A>
					</TD>
					<TD align="center">
						<A href="javascript:UnitProcessConfirmAndSubmit('delete', <bean:write name='unitProcessListHelper' property='treatmentTypeId'/>, '', <bean:write name='unitProcessHelper' property='unitProcessId'/>, 0)">
							<pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
						</A>
					</TD>
		         </logic:equal>
			</TR>			
			<c:set var="i" value="${i+1}" />
		    </logic:iterate>
			<c:choose>
				<c:when test='${i%2=="0"}'>
					<c:set var="class" value="PortletSubHeaderColor" />   
				</c:when>
				<c:otherwise>
					<c:set var="class" value="" />
				</c:otherwise>
			</c:choose>
		</TABLE>
		
	  </TD>
	</TR>
	
		<%
		    String showDetail = "none";
			String showAddLink = "block";
		%>	
	
	<logic:equal name="unitProcessListHelper" property="treatmentTypeId" value="<%=unitProcessForm.getTreatmentTypeId()%>">
		<%	
	    if((unitProcessForm.getDetailEditExpand()).equals("Y"))
	    {
	      showDetail = "block";
	      showAddLink = "none";
	    }
		%>
	</logic:equal>
	
	<logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">	
	<TR>
		<TD align="left">		
		<DIV id="addNewUnitProcessLink_<bean:write name='unitProcessListHelper' property='treatmentTypeId'/>" style="display:<%=showAddLink%>" class="PortletText1">		
		<A href="javascript:UnitProcessConfirmAndSubmit('add', <bean:write name='unitProcessListHelper' property='treatmentTypeId'/>, '', 0, 0)">
			Add Unit Process
		</A>
		</DIV>
		
		<logic:equal name="unitProcessListHelper" property="treatmentTypeId" value="<%=unitProcessForm.getTreatmentTypeId()%>">	
		<DIV id="unitProcessDetails" style="display:<%=showDetail%>; background-color: #cccccc; padding: 5"  class="PortletText1">
			<br/>
			<P>&nbsp;<Strong>
			   <FONT size="3">
						  Add/Edit Unit Process&nbsp;
			   </FONT>&nbsp;</Strong>
			</P>		

			<TABLE border="0" class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
				<TR>
					<TD valign="middle">
						<span class="required">*</span>&nbsp;<STRONG> Unit Process: </STRONG>&nbsp;&nbsp;
					<logic:equal name="UnitProcessFormBean" property="isAddAction" value="Y">
						<pdk-html:select name="UnitProcessFormBean" 
										 styleId="unitProcessFormId" 
										 property="unitProcessFormId" 
										 size="1" 
										 style="width: 330px">
						<option value="">
							Select a Unit Process
						</option>	
								     <logic:present name="unitProcessDropdownList">
								   	 	<logic:iterate id="unitProcessDropdownItem" name="unitProcessDropdownList" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessHelper">
											 <OPTION value="<bean:write name="unitProcessDropdownItem" property="unitProcessId"/>"
											 	title="<bean:write name="unitProcessDropdownItem" property="unitProcessId"/>">
												       <bean:write name="unitProcessDropdownItem" property="unitProcessName"/>
											 </OPTION>	   	 	
								   	 	</logic:iterate>
								   	 </logic:present>
						</pdk-html:select>
					</logic:equal>
					<logic:equal name="UnitProcessFormBean" property="isAddAction" value="N">
						<bean:write name="UnitProcessFormBean" property="unitProcessFormName"/>
						<pdk-html:hidden styleId="unitProcessFormId"  name="UnitProcessFormBean" property="unitProcessFormId" value="<%=unitProcessForm.getUnitProcessFormId()%>" />
					</logic:equal>	
					<br/><br/>
					<span class="required">*</span>&nbsp;<STRONG>Status: </STRONG>&nbsp;&nbsp;
						<pdk-html:radio name="UnitProcessFormBean" property="presentProjectedFlag" onclick="adjustUPAavailChange('present', false)"
							styleId="presentProjectedFlag_present" value="1">Present</pdk-html:radio>
						&nbsp;<pdk-html:radio name="UnitProcessFormBean" property="presentProjectedFlag" onclick="adjustUPAavailChange('project', false)"
							styleId="presentProjectedFlag_project" value="2">Projected</pdk-html:radio>											    
						&nbsp;<pdk-html:radio name="UnitProcessFormBean" property="presentProjectedFlag" onclick="adjustUPAavailChange('presentProject', false)"
							styleId="presentProjectedFlag_presentProject" value="3">Present & Projected</pdk-html:radio>						
					</TD>									
					<TD valign="middle">
						<STRONG> Notes: </STRONG><br/>
						<pdk-html:textarea name="UnitProcessFormBean" styleId="unitProcessDescription" 
							property="description" rows="3" cols="30" />
					</TD>
				</TR>
				<TR>
					<TD valign="middle" colspan="2">
						<span class="required">&nbsp;</span>&nbsp;<STRONG>Planned Year: </STRONG>&nbsp;&nbsp;
						<logic:equal name="UnitProcessFormBean" property="planYear" value="0">
						<pdk-html:text name="UnitProcessFormBean" styleId="planYear" value=""
									   property="planYear" maxlength="4" size="4" />
						</logic:equal>
						<logic:notEqual name="UnitProcessFormBean" property="planYear" value="0">
						<pdk-html:text name="UnitProcessFormBean" styleId="planYear"
									   property="planYear" maxlength="4" size="4" />
						</logic:notEqual>						
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<STRONG> Backup: </STRONG>&nbsp;&nbsp;
						<pdk-html:checkbox name="UnitProcessFormBean" property="backupFlag" value="Y" styleId="backupCheckbox"/>
					</TD>
				</TR>				
				<TR>
					<TD colspan="2" align="left">
					<br/>
						<span class="required">&nbsp;</span>&nbsp;<STRONG> Changes: </STRONG><br/>
						<TABLE  class="PortletText1">
						<TR>
							<TD><span class="required">&nbsp;</span>&nbsp;Available Changes</TD>
							<TD>&nbsp;</TD>
							<TD><span class="required">*</span>&nbsp;Selected Changes</TD>
						</TR>
						<TR>
							<TD>
								<span class="required">&nbsp;</span>&nbsp;<pdk-html:select name="UnitProcessFormBean"  multiple="true" size="6" style="width:250;" onchange="unitProcessEnableMoveButtons(this, 'UnitProcessChangeRemoveButton')"
								                 styleId="availChangeType" property="availChangeType">
								     <logic:present name="availableChangeTypes">
								   	 	<logic:iterate id="availableChangeType" name="availableChangeTypes" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper">
											 <OPTION value="<bean:write name="availableChangeType" property="changeTypeId"/>">
												       <bean:write name="availableChangeType" property="changeTypeName"/>
											 </OPTION>	   	 	
								   	 	</logic:iterate>
								   	 </logic:present>
								</pdk-html:select>							
							</TD>
							<TD>
								<INPUT type="button" disabled="true" id="UnitProcessChangeRemoveButton" name="UnitProcessChangeRemoveButton" value="&gt;" onclick="if(checkUPNoChangeRule('availChangeType')){moveOptions('availChangeType','selectedChangeType');this.disabled=true}"/>
								<br/>
								<INPUT type="button" disabled="true" id="UnitProcessChangeAddButton" name="UnitProcessChangeAddButton" value="&lt;" onclick="moveOptions('selectedChangeType', 'availChangeType');this.disabled=true;repopulateAvailForNoChangeRule();"/>		    							
							</TD>
							<TD>
								<pdk-html:select name="UnitProcessFormBean"  multiple="true" size="6" style="width:250;" onchange="unitProcessEnableMoveButtons(this, 'UnitProcessChangeAddButton')"
								                 styleId="selectedChangeType" property="selectedChangeType">
								     <logic:present name="selectedChangeTypes">
								   	 	<logic:iterate id="selectedChangeType" name="selectedChangeTypes" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper">
											 <OPTION value="<bean:write name="selectedChangeType" property="changeTypeId"/>">
												       <bean:write name="selectedChangeType" property="changeTypeName"/>
											 </OPTION>	   	 	
								   	 	</logic:iterate>
								   	 </logic:present>
								</pdk-html:select>	
							    <DIV  style="display:none">
									<select name="hiddenSelectedChangeType" multiple="true" size="6" id="hiddenSelectedChangeType">
									     <logic:present name="selectedChangeTypes">
									   	 	<logic:iterate id="selectedChangeType" name="selectedChangeTypes" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper">
												 <OPTION value="<bean:write name="selectedChangeType" property="changeTypeId"/>">
													       <bean:write name="selectedChangeType" property="changeTypeName"/>
												 </OPTION>	   	 	
									   	 	</logic:iterate>
									   	 </logic:present>
									</select>	
									<select name="hiddenGlobalChangeType" multiple="true" size="6" id="hiddenGlobalChangeType">
									     <logic:present name="unitProcessChangeTypeGlobalList">
									   	 	<logic:iterate id="globalChangeType" name="unitProcessChangeTypeGlobalList" type="gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper">
												 <OPTION value="<bean:write name="globalChangeType" property="changeTypeId"/>">
													       <bean:write name="globalChangeType" property="changeTypeName"/>
												 </OPTION>	   	 	
									   	 	</logic:iterate>
									   	 </logic:present>
									</select>										
								</DIV>																
							</TD>
						</TR>						
						</TABLE>
					</TD>									
				</TR>				
			</TABLE>
			
			<TABLE border="0" class="PortletText1" cellspacing="1" cellpadding="1" width="100%" 
			          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
				<TR>
					<TD colspan="4">
					<div align="left" width="150">
					<logic:equal name="UnitProcessFormBean" property="isUpdatable" value="Y">	
					<A href="javascript:UnitProcessConfirmAndSubmit('save', <bean:write name='unitProcessListHelper' property='treatmentTypeId'/>, '', 0, 0)">
					<pdk-html:img page="/images/submit.gif" alt="submit" border="0"/></A>				   
					   <FONT size="1">&nbsp;&nbsp;</FONT>
					</logic:equal>	
					<A href="javascript:{showOrHideUnitProcessDetails('addNewUnitProcessLink_<bean:write name='unitProcessListHelper' property='treatmentTypeId'/>', 'block'); showOrHideUnitProcessDetails('unitProcessDetails', 'none')}">
					<pdk-html:img page="/images/cancel.gif" alt="Cancel" border="0"/></A>				   							   
					</div>			   
	   			   </TD>
				</TR>
			</TABLE>
		</DIV>
		</logic:equal>
		
		</TD>
	</TR>
	</logic:equal>
</TABLE>
<br/>
</logic:iterate>

</pdk-html:form>