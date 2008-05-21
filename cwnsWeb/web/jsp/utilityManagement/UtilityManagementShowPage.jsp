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
	import="gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		    String ajaxurl = url +"/javascript/prototype.js";
			
   			UtilityManagementForm utilityManagementForm = (UtilityManagementForm)request.getAttribute("utilityManagementForm");	
  			
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<script type="text/javascript" language="JavaScript" src="<%=ajaxurl %>"></script>

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

function UtilMgmtCheckCosts(statusId, i)
{
	var remainingCostCtrl = window.document.getElementById("remainingCost_" + i);
	var annualCostCtrl = window.document.getElementById("annualCost_" + i);

	if(statusId==2 || statusId==3)
	{
	    umEnableField(remainingCostCtrl);
	}
	else
	{
	    umDisableField(remainingCostCtrl);
	}

	if(statusId==1 || statusId==2 || statusId==3)
	{
		umEnableField(annualCostCtrl);
	}
	else
	{
	    umDisableField(annualCostCtrl);
	}
}

function umDisableField(field){
   	field.disabled = true;
   	field.value = '';
   	field.className = 'disabledField';
}

function umEnableField(field){
   	field.disabled = false;
   	field.className = '';
}
// Set the value of the "action" hidden attribute
function UtilityManagementConfirmAndSubmit(action, total)
{
	var resultStr = "";
	
	for(var i=1; i<=total; i++)
	{
		var utilMgmtPracticeHiddenIdCtrl = window.document.getElementById("utilMgmtPracticeHiddenId_" + i);
		var utilMgmtStautsSelectCtrl = window.document.getElementById("utilMgmtStautsSelectId_" + i);
		var remainingCostCtrl = window.document.getElementById("remainingCost_" + i);
		var annualCostCtrl = window.document.getElementById("annualCost_" + i);

		resultStr = resultStr + utilMgmtPracticeHiddenIdCtrl.value + ":";
		resultStr = resultStr + utilMgmtStautsSelectCtrl.value + ":";
		
	      if(remainingCostCtrl.disabled==false && trim(remainingCostCtrl.value)!="" )
	      {
		      if(isNaN(parseInt(remainingCostCtrl.value)))
		      {
		      	alert("Error: Remaining Costs to Implement field accepts only numeric values.");
				remainingCostCtrl.focus();
				return;  		      	
		      }
		      else if(remainingCostCtrl.value.indexOf(".") >=0)
		      {
		      	alert("Error: Remaining Costs to Implement must be an integer.");
				remainingCostCtrl.focus();
				return;  			      	
		      }
		      else if(remainingCostCtrl.value <= 0)
		      {
				alert("Error: Remaining Costs to Implement must be greater than 0.");
				remainingCostCtrl.focus();
				return;  	
		      }
	      }

	      if(annualCostCtrl.disabled==false && trim(annualCostCtrl.value)!="" )
	      {
		      if(isNaN(parseInt(annualCostCtrl.value)))
		      {
		      	alert("Error: Annual Costs to Operate field accepts only numeric values.");
				annualCostCtrl.focus();
				return;  		      	
		      }
		      else if(annualCostCtrl.value.indexOf(".") >=0)
		      {
		      	alert("Error: Annual Costs to Operate must be an integer.");
				annualCostCtrl.focus();
				return;  			      	
		      }
		      else if(annualCostCtrl.value <= 0)
		      {
				alert("Error: Annual Costs to Operate must be greater than 0.");
				annualCostCtrl.focus();
				return;  	
		      }
	      }
      		
		if(remainingCostCtrl.disabled==true)
			resultStr = resultStr + "-999" + ":";
		else if(trim(remainingCostCtrl.value)=="")
			resultStr = resultStr + "-999" + ":";
		else 
			resultStr = resultStr + remainingCostCtrl.value + ":";

		if(annualCostCtrl.disabled==true)
			resultStr = resultStr + "-999";
		else if(trim(annualCostCtrl.value)=="")
			resultStr = resultStr + "-999";
		else 
			resultStr = resultStr + annualCostCtrl.value;

		resultStr = resultStr + ";";
	}

		//alert(resultStr);
       var result_field = window.document.getElementById("utilityManagementResultListString");
		result_field.value=resultStr;
				
       var hidden_field = window.document.getElementById("UtilityManagementAct");
	   hidden_field.value=action;
	   window.document.UtilityManagementFormBean.submit();          	
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

</SCRIPT>

<pdk-html:form name="UtilityManagementFormBean"
	styleId="UtilityManagementFormBeanId"
	type="gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementForm"
	action="utilityManagement.do">

	<DIV id="hidden_fields" style="display:none">
		<pdk-html:text styleId="utilityManagementAct"
			property="utilityManagementAct"
			value="<%=utilityManagementForm.getUtilityManagementAct()%>" />
		<pdk-html:text styleId="utilityManagementFacilityId"
			property="utilityManagementFacilityId"
			value="<%=utilityManagementForm.getUtilityManagementFacilityId()%>" />
		<pdk-html:text styleId="utilityManagementResultListString"
			property="resultListString"
			value="<%=utilityManagementForm.getResultListString()%>" />
	</DIV>
	<logic:present name="warnStateUser">
		<logic:equal name="warnStateUser" value="Y">
		<div class="MessageZone">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" valign="middle" width="40">
						<pdk-html:img page="/images/warn24.gif" alt="Warning" border="0" />
					</td>

					<td class="WarningText" align="left" valign="middle">
						A Local user has updated Utility Management Information. Any changes may
						overwrite that data.
					</td>
				</tr>
			</table>
		</div>
		</logic:equal>
	</logic:present>

	<TABLE class="PortletText1" cellspacing="3" cellpadding="2"
		width="100%" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		<TR>
			<TD class="PortletHeaderColor" width="40%">
				<P align="center">
					<STRONG> <FONT color="#ffffff"> Utility Management
							Practices </FONT> </STRONG>
				</P>
			</TD>
			<TD class="PortletHeaderColor" width="20%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG>Implementation</STRONG> </FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="20%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Remaining Costs to
							<br />Implement ($) </STRONG> </FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor" width="20%">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Annual Costs to <br />Operate
							($) </STRONG> </FONT>
				</DIV>
			</TD>
		</TR>
		<c:set var="iii" value="1" />
		<logic:iterate id="utilityManagementList" name="utilityManagementList"
			scope="request"
			type="gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementHelper">

			<c:choose>
				<c:when test='${iii%2=="0"}'>
					<c:set var="class" value="PortletSubHeaderColor" />
				</c:when>
				<c:otherwise>
					<c:set var="class" value="" />
				</c:otherwise>
			</c:choose>

			<TR class="<c:out value="${class}"/>">
				<TD>
					<bean:write name="utilityManagementList"
						property="utilMgmtPracticeName" />
					<input type="hidden"
						name="utilMgmtPracticeHiddenId_<c:out value="${iii}"/>"
						id="utilMgmtPracticeHiddenId_<c:out value="${iii}"/>"
						value="<bean:write name="utilityManagementList" property="utilMgmtPracticeId"/>" />
				</TD>
				<bean:define id="umiStatusId_1" name="utilityManagementList"
					property="utilMgmtImplmntStatusId" />
				<TD>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="Y">
						<select id="utilMgmtStautsSelectId_<c:out value="${iii}"/>"
							name="utilMgmtStautsSelectId_<c:out value="${iii}"/>"
							onchange="UtilMgmtCheckCosts(this.value, '<c:out value="${iii}"/>')"
							size="1">
							<OPTION value="-999"></OPTION>
							<logic:iterate id="utilityManagementStatusList"
								name="utilityManagementStatusList" scope="request"
								type="gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementStatusHelper">
								<bean:define id="umiStatusId_2"
									name="utilityManagementStatusList" property="utilMgmtStatusId" />
								<OPTION
									<%=((Long)umiStatusId_1).longValue() == ((Long)umiStatusId_2).longValue()?"SELECTED":"" %>
									value="<bean:write name="utilityManagementStatusList" property="utilMgmtStatusId"/>">
									<bean:write name="utilityManagementStatusList"
										property="utilMgmtStatusName" />
								</OPTION>
							</logic:iterate>
						</select>
					</logic:equal>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="N">
						<bean:write name="utilityManagementList"
							property="utilMgmtImplmntStatusName" />
					</logic:equal>
				</TD>
				<TD>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="Y">
						<logic:equal name="utilityManagementList"
							property="isRemainingCostDisabled" value="Y">
							<input type="text" disabled class="disabledField" maxlength="11"
								name="remainingCost_<c:out value="${iii}"/>"
								id="remainingCost_<c:out value="${iii}"/>" value="" />
						</logic:equal>
						<logic:equal name="utilityManagementList"
							property="isRemainingCostDisabled" value="N">
							<logic:greaterThan name="utilityManagementList"
								property="remainingCostToImplement" value="0">
								<input type="text" maxlength="11"
									name="remainingCost_<c:out value="${iii}"/>"
									id="remainingCost_<c:out value="${iii}"/>"
									value="<bean:write name='utilityManagementList' property='remainingCostToImplement'/>" />
							</logic:greaterThan>
							<logic:lessEqual name="utilityManagementList"
								property="remainingCostToImplement" value="0">
								<input type="text" maxlength="11"
									name="remainingCost_<c:out value="${iii}"/>"
									id="remainingCost_<c:out value="${iii}"/>" value="" />
							</logic:lessEqual>
						</logic:equal>
					</logic:equal>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="N">
						<logic:greaterEqual name='utilityManagementList'
							property='remainingCostToImplement' value="0">
							<bean:write name='utilityManagementList'
								property='remainingCostToImplement' />
						</logic:greaterEqual>
					</logic:equal>
				</TD>

				<TD>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="Y">
						<logic:equal name="utilityManagementList"
							property="isAnnualCostToOperatetDisabled" value="Y">
							<input type="text" disabled class="disabledField" maxlength="11"
								name="annualCost_<c:out value="${iii}"/>"
								id="annualCost_<c:out value="${iii}"/>" value="" />
						</logic:equal>
						<logic:equal name="utilityManagementList"
							property="isAnnualCostToOperatetDisabled" value="N">
							<logic:greaterThan name="utilityManagementList"
								property="annualCostToOperate" value="0">
								<input type="text" name="annualCost_<c:out value="${iii}"/>"
									id="annualCost_<c:out value="${iii}"/>" maxlength="11"
									value="<bean:write name='utilityManagementList' property='annualCostToOperate'/>" />
							</logic:greaterThan>
							<logic:lessEqual name="utilityManagementList"
								property="annualCostToOperate" value="0">
								<input type="text" maxlength="11"
									name="annualCost_<c:out value="${iii}"/>"
									id="annualCost_<c:out value="${iii}"/>" value="" />
							</logic:lessEqual>
						</logic:equal>
					</logic:equal>
					<logic:equal name="UtilityManagementFormBean"
						property="isUpdatable" value="N">
						<logic:greaterEqual name='utilityManagementList'
							property='annualCostToOperate' value="0">
							<bean:write name='utilityManagementList'
								property='annualCostToOperate' />
						</logic:greaterEqual>
					</logic:equal>
				</TD>
			</TR>

			<c:set var="iii" value="${iii+1}" />
		</logic:iterate>

		<logic:equal name="UtilityManagementFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD colspan="4">
					<div align="left" width="150">
						<A
							href="javascript:UtilityManagementConfirmAndSubmit('save', <c:out value="${iii-1}"/>)">
							<pdk-html:img page="/images/submit.gif" alt="save" border="0" />
						</A>
						<FONT size="1">&nbsp;&nbsp;</FONT>
						<A
							href="javascript:UtilityManagementConfirmAndSubmit('reset', <c:out value="${iii-1}"/>)">
							<pdk-html:img page="/images/reset.gif" alt="Reset" border="0" />
						</A>

					</div>
				</TD>
			</TR>
		</logic:equal>

	</TABLE>


</pdk-html:form>
