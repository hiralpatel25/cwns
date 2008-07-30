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
	import="gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationForm"
	import="gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationLineItemHelper"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		    String ajaxurl = url +"/javascript/prototype.js";

   			CostCurvePopulationForm costCurvePopulationForm = (CostCurvePopulationForm)request.getAttribute("costCurvePopulationForm");
   			CostCurvePopulationLineItemHelper NewSeparateSewerSystemInterceptorObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewSeparateSewerSystemInterceptorObj");
			CostCurvePopulationLineItemHelper NewSeparateSewerSystemPopulationObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewSeparateSewerSystemPopulationObj");
			CostCurvePopulationLineItemHelper RehabReplaceSeparateSewerSystemPopulationObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabReplaceSeparateSewerSystemPopulationObj");
			CostCurvePopulationLineItemHelper NewOWTSAllResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSAllResidentHousesObj");
			CostCurvePopulationLineItemHelper NewOWTSAllNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSAllNonResidentHousesObj");
			CostCurvePopulationLineItemHelper NewOWTSInnovResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSInnovResidentHousesObj");
			CostCurvePopulationLineItemHelper NewOWTSInnovNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSInnovNonResidentHousesObj");
			CostCurvePopulationLineItemHelper NewOWTSConvenResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSConvenResidentHousesObj");
			CostCurvePopulationLineItemHelper NewOWTSConvenNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewOWTSConvenNonResidentHousesObj");
			CostCurvePopulationLineItemHelper NewClusteredSystemResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewClusteredSystemResidentHousesObj");
			CostCurvePopulationLineItemHelper NewClusteredSystemNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("NewClusteredSystemNonResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSAllResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSAllResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSAllNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSAllNonResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSInnovResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSInnovResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSInnovNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSInnovNonResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSConvenResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSConvenResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabOWTSConvenNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabOWTSConvenNonResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabClusteredSystemResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabClusteredSystemResidentHousesObj");
			CostCurvePopulationLineItemHelper RehabClusteredSystemNonResidentHousesObj  = (CostCurvePopulationLineItemHelper)request.getAttribute("RehabClusteredSystemNonResidentHousesObj");
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

// Set the value of the "action" hidden attribute
function CostCurvePopulationConfirmAndSubmit(action, docId, hasCost)
{

   if(action == "save")
   {
   		// validate
   	var ctrlNewSSS = window.document.getElementById("NewSeparateSewerSystemPopulationText");
   	var ctrlRehabSSS = window.document.getElementById("RehabReplaceSeparateSewerSystemPopulationText");
   	
	 if(ctrlNewSSS!=null && trim(ctrlNewSSS.value)=="")
	 {
	 	// show error
	 	alert("Error: " + "New Separate Sewer System Population is required");
	 	ctrlNewSSS.focus();
	 	return;
	 }

	 if(ctrlRehabSSS!=null && trim(ctrlRehabSSS.value)=="")
	 {
	 	// show error
	 	alert("Error: " + "Rehab/Replace Separate Sewer System Population is required");
	 	ctrlRehabSSS.focus();
	 	return;
	 }

	if(validateBothResNonRes("NewOWTSAllResidentHousesText","NewOWTSAllNonResidentHousesText", "Residential and/or Non-residential Number of Houses for New Onsite Wastewater Treatment System - all must be entered")) return;
	else if(validateBothResNonRes("NewOWTSInnovResidentHousesText","NewOWTSInnovNonResidentHousesText", "Residential and/or Non-residential Number of Houses for New Onsite Wastewater Treatment System - innovative must be entered")) return;
	else if(validateBothResNonRes("NewOWTSConvenResidentHousesText","NewOWTSConvenNonResidentHousesText", "Residential and/or Non-residential Number of Houses for New Onsite Wastewater Treatment System - conventional must be entered")) return;
	else if(validateBothResNonRes("NewClusteredSystemResidentHousesText","NewClusteredSystemNonResidentHousesText", "Residential and/or Non-residential Number of Houses for New Cluster System must be entered")) return;
	else if(validateBothResNonRes("RehabOWTSAllResidentHousesText","RehabOWTSAllNonResidentHousesText", "Residential and/or Non-residential Number of Houses for Rehab Onsite Wastewater Treatment System - all must be entered")) return;
	else if(validateBothResNonRes("RehabOWTSInnovResidentHousesText","RehabOWTSInnovNonResidentHousesText", "Residential and/or Non-residential Number of Houses for Rehab Onsite Wastewater Treatment System - innovative must be entered")) return;
	else if(validateBothResNonRes("RehabOWTSConvenResidentHousesText","RehabOWTSConvenNonResidentHousesText", "Residential and/or Non-residential Number of Houses for Rehab Onsite Wastewater Treatment System - conventional must be entered")) return;
	else if(validateBothResNonRes("RehabClusteredSystemResidentHousesText","RehabClusteredSystemNonResidentHousesText", "Residential and/or Non-residential Number of Houses for Rehab Cluster System must be entered")) return;   		
   }

      	// edit, just save
       hidden_field = window.document.getElementById("CostCurvePopulationAct");
	   hidden_field.value=action;
	   window.document.CostCurvePopulationFormBean.submit();

}

function validateBothResNonRes(id1, id2, message)
{
 var textCtrl1 = window.document.getElementById(id1);
 var textCtrl2 = window.document.getElementById(id2);
 
 if(textCtrl1!=null && textCtrl1.value=="" && textCtrl2!=null && textCtrl2.value=="")
 {
 	// show error
 	alert("Error: " + message);
 	textCtrl1.focus();
 	return true;
 }
 
 return false;
 
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

function ccpValidateNumberField(ctrl)
{
	if(trim(ctrl.value) == "")
	{
		return;
	}
	else
	{
      if(isNaN(parseInt(ctrl.value)) || ctrl.value.indexOf(".") >=0 || parseInt(ctrl.value) <= 0)
      {
		alert("Error: This field must be a integer greater than 0.");
		ctrl.focus();
		return;
      }
    }
}

function ccpCalculateTotal(ctrl, isResidential, popName, spanId)
{
	var longValue;
	var spanCtrl = window.document.getElementById(spanId);

	if(trim(ctrl.value) == "")
	{
		longValue = 0;
		spanCtrl.innerHTML = "&nbsp;";
	}
	else
	{
      if(isNaN(parseInt(ctrl.value)) || ctrl.value.indexOf(".") >=0 || parseInt(ctrl.value) <= 0)
      {
		alert("Error: This field must be a integer greater than 0.");
		ctrl.focus();
		return;
      }
            
      if(isResidential == "residential"){
         if(popName == "OWTS")
	       longValue = parseInt(ctrl.value) * <%=costCurvePopulationForm.getResOWTSPopulationPerHouse()%>;
	     else
	       longValue = parseInt(ctrl.value) * <%=costCurvePopulationForm.getResClusteredPopulationPerHouse()%>;  
	  }else{
	      if(popName == "OWTS")
	        longValue = parseInt(ctrl.value) * <%=costCurvePopulationForm.getNonResOWTSPopulationPerHouse()%>;
	      else
	        longValue = parseInt(ctrl.value) * <%=costCurvePopulationForm.getNonResClusteredPopulationPerHouse()%>;  
      } 
	  longValue = Math.round(longValue);
	  
      spanCtrl.innerHTML = longValue + "";
	}

}
</SCRIPT>

<pdk-html:form 	name="CostCurvePopulationFormBean" styleId="CostCurvePopulationFormBeanId"
				type="gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationForm"
				action="costCurvePopulation.do">

	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->
	<DIV id="hidden_fields" style="display:none">
		<pdk-html:text styleId="costCurvePopulationAct" property="costCurvePopulationAct" value="<%=costCurvePopulationForm.getCostCurvePopulationAct()%>"/>
		<pdk-html:text styleId="costCurvePopulationFacilityId" property="costCurvePopulationFacilityId" value="<%=costCurvePopulationForm.getCostCurvePopulationFacilityId()%>"/>
	</DIV>

<TABLE class="PortletText1" cellspacing="2" cellpadding="2" width="100%"><TR><TD>
<FONT size="4"><STRONG>&nbsp;Cost Curve Population</STRONG> </FONT>
</TD></TR></TABLE>

<%
	if(costCurvePopulationForm.getShowMessageZone().equals("Y"))
	{
 %>
<div class="MessageZone">
     <table width="100%" border="0" cellpadding="0" cellspacing="0">
     <logic:iterate id="errorItem" name="errorsList" scope="request">
     <tr>
			<td align="center" valign="middle" width="40">
     			<pdk-html:img page="/images/err24.gif" alt="Error" border="0"/>	
     		</td>
     		<td class="ErrorText" align="left" valign="middle">
     		<bean:write name="errorItem"/>
     		</td>	      	
     </tr>
     </logic:iterate>
     <logic:iterate id="warningItem" name="warningsList" scope="request">
     <tr>
			<td align="center" valign="middle" width="40">
     			<pdk-html:img page="/images/warn24.gif" alt="Warning" border="0"/>	
     		</td>
     		<td class="WarningText" align="left" valign="middle">
     		<bean:write name="warningItem"/>
     		</td>	      	
     </tr>
     </logic:iterate>
     </table>
</div>     
<%
	}
 %>


<TABLE border="0" class="PortletText1" cellspacing="2" cellpadding="2" width="100%"
          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
<%
		if(NewSeparateSewerSystemInterceptorObj.getDisplayOnly().equals("N") &&
			NewSeparateSewerSystemPopulationObj.getDisplayOnly().equals("N") &&
			RehabReplaceSeparateSewerSystemPopulationObj.getDisplayOnly().equals("N") &&
			costCurvePopulationForm.getPopulationPerHouseDisplayOnly().equals("N"))
		{
%>
	<TR><td colspan="2"><br/>&nbsp;&nbsp;No Cost Curve assigned to Facility.<br/><br/></td></TR>		
<%		
		}
		else
		{

 %>          

	<%
		if(NewSeparateSewerSystemInterceptorObj.getDisplayOnly().equals("Y") ||
			NewSeparateSewerSystemPopulationObj.getDisplayOnly().equals("Y") ||
			RehabReplaceSeparateSewerSystemPopulationObj.getDisplayOnly().equals("Y"))
		{
	%>
	<TR>
		<TD class="PortletHeaderColor" width="52%">
			<P align="left">
				<STRONG> <FONT color="#ffffff"> Separate Sewer System
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor" colspan="5">
			<DIV align="left">
				<FONT color="#ffffff"> <STRONG>Population</STRONG>
				</FONT>
			</DIV>
		</TD>
	</TR>
	<%
		if(NewSeparateSewerSystemInterceptorObj.getDisplayOnly().equals("Y") ||
			NewSeparateSewerSystemPopulationObj.getDisplayOnly().equals("Y"))
		{
	%>
	<TR>
		<TD width="52%">
			<P align="left">
				<span class="required">*</span>&nbsp; New Separate Sewer System:
			</P>
		</TD>
		<TD colspan="5" align="left">
		<%
			 String NewSeparateSewerSystemPopulationObjShowStr="";
			 if(NewSeparateSewerSystemPopulationObj.getValue()!=0)
			   NewSeparateSewerSystemPopulationObjShowStr=new Long(NewSeparateSewerSystemPopulationObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewSeparateSewerSystemPopulationText" property="newSeparateSewerSystemPopulation" size="6"
			onblur="ccpValidateNumberField(this)"
			value="<%=NewSeparateSewerSystemPopulationObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewSeparateSewerSystemPopulationObjShowStr%>
		</logic:equal>
		</td>
	</TR>
		<%
			}
		 %>
		<%
			if(RehabReplaceSeparateSewerSystemPopulationObj.getDisplayOnly().equals("Y"))
			{
		 %>
	<TR>
		<TD width="52%">
			<P align="left">
				<span class="required">*</span>&nbsp; Rehabilitate/Replace Separate Sewer System:
			</P>
		</TD>
		<TD colspan="5" align="left">
		<%
			 String RehabReplaceSeparateSewerSystemPopulationObjShowStr="";
			 if(RehabReplaceSeparateSewerSystemPopulationObj.getValue()!=0)
			   RehabReplaceSeparateSewerSystemPopulationObjShowStr=new Long(RehabReplaceSeparateSewerSystemPopulationObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabReplaceSeparateSewerSystemPopulationText" property="rehabReplaceSeparateSewerSystemPopulation" size="6"
			onblur="ccpValidateNumberField(this)"
			value="<%=RehabReplaceSeparateSewerSystemPopulationObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabReplaceSeparateSewerSystemPopulationObjShowStr%>
		</logic:equal>
		</TD>
	</TR>
		<%
			}
		  } // if either yes
		%>
<%
		if(costCurvePopulationForm.getPopulationPerHouseDisplayOnly().equals("Y"))
		{
%>

	<tr>
		<td class="PortletHeaderColor" width="52%">
			<FONT color="#ffffff"> <STRONG>Onsite Wastewater Treatment Systems</STRONG>
				</FONT>
		</td>
		<td class="PortletHeaderColor" colspan="3" align="center">
			<FONT color="#ffffff"> <STRONG>Resident Population</STRONG>
				</FONT>
		</td>
		<td class="PortletHeaderColor" colspan="3" align="center">
			<FONT color="#ffffff"> <STRONG>Non-Resident Population</STRONG>
				</FONT>
		</td>
	</tr>
	<tr>
		<td width="52%">
			&nbsp;
		</td>
		<td class="PortletHeaderColor"  width="12%" align="center">
			<FONT color="#ffffff">Number of <br/>Units *</FONT>
		</td>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff">Population<BR>per Unit</FONT>
		</TD>
		<td class="PortletHeaderColor"  width="12%" align="center">
			<FONT color="#ffffff">Total</FONT>
		</td>
		<td class="PortletHeaderColor"  width="12%" align="center">
			<FONT color="#ffffff">Number of <br/>Units **</FONT>
		</td>
		<TD class="PortletHeaderColor">
			<FONT color="#ffffff">Population<BR>per Unit</FONT>
		</TD>
		<td class="PortletHeaderColor"  width="12%" align="center">
			<FONT color="#ffffff">Total</FONT>
		</td>
	</tr>

		<%
			if(NewOWTSAllResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>
	<tr>
		<td width="52%">
			<span class="required">*</span>&nbsp;New Onsite Wastewater Treatment System – all:
		</td>
		<td align="center">
		<%
			 String NewOWTSAllResidentHousesObjShowStr="";
			 if(NewOWTSAllResidentHousesObj.getValue()!=0)
			   NewOWTSAllResidentHousesObjShowStr=new Long(NewOWTSAllResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSAllResidentHousesText" property="newOWTSAllResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS', 'NewOWTSAllResidentHousesTotalSpan')"
			value="<%=NewOWTSAllResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSAllResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSAllResidentHousesTotalSpan">
		<%
			 String NewOWTSAllResidentHousesObjTotalStr="";
			 if(NewOWTSAllResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   NewOWTSAllResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSAllResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSAllResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String NewOWTSAllNonResidentHousesObjShowStr="";
			 if(NewOWTSAllNonResidentHousesObj.getValue()!=0)
			   NewOWTSAllNonResidentHousesObjShowStr=new Long(NewOWTSAllNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSAllNonResidentHousesText" property="newOWTSAllNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS', 'NewOWTSAllNonResidentHousesTotalSpan')"
			value="<%=NewOWTSAllNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSAllNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSAllNonResidentHousesTotalSpan">
		<%
			 String NewOWTSAllNonResidentHousesObjTotalStr="";
			 if(NewOWTSAllNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   NewOWTSAllNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSAllNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSAllNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>
		<%
			}
		 %>


		<%
			if(NewOWTSInnovResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>
	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;New Onsite Wastewater Treatment System - innovative:
		</td>
		<td align="center">
		<%
			 String NewOWTSInnovResidentHousesObjShowStr="";
			 if(NewOWTSInnovResidentHousesObj.getValue()!=0)
			   NewOWTSInnovResidentHousesObjShowStr=new Long(NewOWTSInnovResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSInnovResidentHousesText" property="newOWTSInnovResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS', 'NewOWTSInnovResidentHousesTotalSpan')"
			value="<%=NewOWTSInnovResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSInnovResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSInnovResidentHousesTotalSpan">
		<%
			 String NewOWTSInnovResidentHousesObjTotalStr="";
			 if(NewOWTSInnovResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   NewOWTSInnovResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSInnovResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSInnovResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String NewOWTSInnovNonResidentHousesObjShowStr="";
			 if(NewOWTSInnovNonResidentHousesObj.getValue()!=0)
			   NewOWTSInnovNonResidentHousesObjShowStr=new Long(NewOWTSInnovNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSInnovNonResidentHousesText" property="newOWTSInnovNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS' 'NewOWTSInnovNonResidentHousesTotalSpan')"
			value="<%=NewOWTSInnovNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSInnovNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSInnovNonResidentHousesTotalSpan">
		<%
			 String NewOWTSInnovNonResidentHousesObjTotalStr="";
			 if(NewOWTSInnovNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   NewOWTSInnovNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSInnovNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSInnovNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>
		<%
			}
		 %>

		<%
			if(NewOWTSConvenResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>
	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;New Onsite Wastewater Treatment System - conventional:
		</td>
		<td align="center">
		<%
			 String NewOWTSConvenResidentHousesObjShowStr="";
			 if(NewOWTSConvenResidentHousesObj.getValue()!=0)
			   NewOWTSConvenResidentHousesObjShowStr=new Long(NewOWTSConvenResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSConvenResidentHousesText" property="newOWTSConvenResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS' 'NewOWTSConvenResidentHousesTotalSpan')"
			value="<%=NewOWTSConvenResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSConvenResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSConvenResidentHousesTotalSpan">
		<%
			 String NewOWTSConvenResidentHousesObjTotalStr="";
			 if(NewOWTSConvenResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   NewOWTSConvenResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSConvenResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSConvenResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String NewOWTSConvenNonResidentHousesObjShowStr="";
			 if(NewOWTSConvenNonResidentHousesObj.getValue()!=0)
			   NewOWTSConvenNonResidentHousesObjShowStr=new Long(NewOWTSConvenNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewOWTSConvenNonResidentHousesText" property="newOWTSConvenNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS', 'NewOWTSConvenNonResidentHousesTotalSpan')"
			value="<%=NewOWTSConvenNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewOWTSConvenNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewOWTSConvenNonResidentHousesTotalSpan">
		<%
			 String NewOWTSConvenNonResidentHousesObjTotalStr="";
			 if(NewOWTSConvenNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   NewOWTSConvenNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewOWTSConvenNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(NewOWTSConvenNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>
		<%
			}
		 %>

		<%
			if(NewClusteredSystemResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>
	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;New Clustered Systems:
		</td>
		<td align="center">
		<%
			 String NewClusteredSystemResidentHousesObjShowStr="";
			 if(NewClusteredSystemResidentHousesObj.getValue()!=0)
			   NewClusteredSystemResidentHousesObjShowStr=new Long(NewClusteredSystemResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewClusteredSystemResidentHousesText" property="newClusteredSystemResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'clustered', 'NewClusteredSystemResidentHousesTotalSpan')"
			value="<%=NewClusteredSystemResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewClusteredSystemResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resClusteredPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewClusteredSystemResidentHousesTotalSpan">
		<%
			 String NewClusteredSystemResidentHousesObjTotalStr="";
			 if(NewClusteredSystemResidentHousesObj.getValue()*costCurvePopulationForm.getResClusteredPopulationPerHouse()!=0)
			   NewClusteredSystemResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewClusteredSystemResidentHousesObj.getValue()*costCurvePopulationForm.getResClusteredPopulationPerHouse())).toString();
			out.print(NewClusteredSystemResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String NewClusteredSystemNonResidentHousesObjShowStr="";
			 if(NewClusteredSystemNonResidentHousesObj.getValue()!=0)
			   NewClusteredSystemNonResidentHousesObjShowStr=new Long(NewClusteredSystemNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="NewClusteredSystemNonResidentHousesText" property="newClusteredSystemNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'clustered' 'NewClusteredSystemNonResidentHousesTotalSpan')"
			value="<%=NewClusteredSystemNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=NewClusteredSystemNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResClusteredPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="NewClusteredSystemNonResidentHousesTotalSpan">
		<%
			 String NewClusteredSystemNonResidentHousesObjTotalStr="";
			 if(NewClusteredSystemNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResClusteredPopulationPerHouse()!=0)
			   NewClusteredSystemNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+NewClusteredSystemNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResClusteredPopulationPerHouse())).toString();
			out.print(NewClusteredSystemNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>


		<%
			}
		 %>

		<%
			if(RehabOWTSAllResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>

	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;Rehab Onsite Wastewater Treatment System - all:
		</td>
		<td align="center">
		<%
			 String RehabOWTSAllResidentHousesObjShowStr="";
			 if(RehabOWTSAllResidentHousesObj.getValue()!=0)
			   RehabOWTSAllResidentHousesObjShowStr=new Long(RehabOWTSAllResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSAllResidentHousesText" property="rehabOWTSAllResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS', 'RehabOWTSAllResidentHousesTotalSpan')"
			value="<%=RehabOWTSAllResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSAllResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSAllResidentHousesTotalSpan">
		<%
			 String RehabOWTSAllResidentHousesObjTotalStr="";
			 if(RehabOWTSAllResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   RehabOWTSAllResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSAllResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSAllResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String RehabOWTSAllNonResidentHousesObjShowStr="";
			 if(RehabOWTSAllNonResidentHousesObj.getValue()!=0)
			   RehabOWTSAllNonResidentHousesObjShowStr=new Long(RehabOWTSAllNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSAllNonResidentHousesText" property="rehabOWTSAllNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS', 'RehabOWTSAllNonResidentHousesTotalSpan')"
			value="<%=RehabOWTSAllNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSAllNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSAllNonResidentHousesTotalSpan">
		<%
			 String RehabOWTSAllNonResidentHousesObjTotalStr="";
			 if(RehabOWTSAllNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   RehabOWTSAllNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSAllNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSAllNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>


		<%
			}
		 %>

		<%
			if(RehabOWTSInnovResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>

	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;Rehab Onsite Wastewater Treatment System - innovative:
		</td>
		<td align="center">
		<%
			 String RehabOWTSInnovResidentHousesObjShowStr="";
			 if(RehabOWTSInnovResidentHousesObj.getValue()!=0)
			   RehabOWTSInnovResidentHousesObjShowStr=new Long(RehabOWTSInnovResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSInnovResidentHousesText" property="rehabOWTSInnovResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS', 'RehabOWTSInnovResidentHousesTotalSpan')"
			value="<%=RehabOWTSInnovResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSInnovResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSInnovResidentHousesTotalSpan">
		<%
			 String RehabOWTSInnovResidentHousesObjTotalStr="";
			 if(RehabOWTSInnovResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   RehabOWTSInnovResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSInnovResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSInnovResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String RehabOWTSInnovNonResidentHousesObjShowStr="";
			 if(RehabOWTSInnovNonResidentHousesObj.getValue()!=0)
			   RehabOWTSInnovNonResidentHousesObjShowStr=new Long(RehabOWTSInnovNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSInnovNonResidentHousesText" property="rehabOWTSInnovNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS', 'RehabOWTSInnovNonResidentHousesTotalSpan')"
			value="<%=RehabOWTSInnovNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSInnovNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSInnovNonResidentHousesTotalSpan">
		<%
			 String RehabOWTSInnovNonResidentHousesObjTotalStr="";
			 if(RehabOWTSInnovNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   RehabOWTSInnovNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSInnovNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSInnovNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>


		<%
			}
			
			if(RehabOWTSConvenResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>

	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;Rehab Onsite Wastewater Treatment System - conventional:
		</td>
		<td align="center">
		<%
			 String RehabOWTSConvenResidentHousesObjShowStr="";
			 if(RehabOWTSConvenResidentHousesObj.getValue()!=0)
			   RehabOWTSConvenResidentHousesObjShowStr=new Long(RehabOWTSConvenResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSConvenResidentHousesText" property="rehabOWTSConvenResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'OWTS', 'RehabOWTSConvenResidentHousesTotalSpan')"
			value="<%=RehabOWTSConvenResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSConvenResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSConvenResidentHousesTotalSpan">
		<%
			 String RehabOWTSConvenResidentHousesObjTotalStr="";
			 if(RehabOWTSConvenResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse()!=0)
			   RehabOWTSConvenResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSConvenResidentHousesObj.getValue()*costCurvePopulationForm.getResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSConvenResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String RehabOWTSConvenNonResidentHousesObjShowStr="";
			 if(RehabOWTSConvenNonResidentHousesObj.getValue()!=0)
			   RehabOWTSConvenNonResidentHousesObjShowStr=new Long(RehabOWTSConvenNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabOWTSConvenNonResidentHousesText" property="rehabOWTSConvenNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'OWTS', 'RehabOWTSConvenNonResidentHousesTotalSpan')"
			value="<%=RehabOWTSConvenNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabOWTSConvenNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResOWTSPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabOWTSConvenNonResidentHousesTotalSpan">
		<%
			 String RehabOWTSConvenNonResidentHousesObjTotalStr="";
			 if(RehabOWTSConvenNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse()!=0)
			   RehabOWTSConvenNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabOWTSConvenNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResOWTSPopulationPerHouse())).toString();
			out.print(RehabOWTSConvenNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>


		<%
			}
		 %>

		<%
			if(RehabClusteredSystemResidentHousesObj.getDisplayOnly().equals("Y"))
			{
		 %>

	<tr>
		<td width="50%">
			<span class="required">*</span>&nbsp;Rehab Clustered Systems:
		</td>
		<td align="center">
		<%
			 String RehabClusteredSystemResidentHousesObjShowStr="";
			 if(RehabClusteredSystemResidentHousesObj.getValue()!=0)
			   RehabClusteredSystemResidentHousesObjShowStr=new Long(RehabClusteredSystemResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabClusteredSystemResidentHousesText" property="rehabClusteredSystemResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'residential', 'clustered', 'RehabClusteredSystemResidentHousesTotalSpan')"
			value="<%=RehabClusteredSystemResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabClusteredSystemResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="resClusteredPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabClusteredSystemResidentHousesTotalSpan">
		<%
			 String RehabClusteredSystemResidentHousesObjTotalStr="";
			 if(RehabClusteredSystemResidentHousesObj.getValue()*costCurvePopulationForm.getResClusteredPopulationPerHouse()!=0)
			   RehabClusteredSystemResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabClusteredSystemResidentHousesObj.getValue()*costCurvePopulationForm.getResClusteredPopulationPerHouse())).toString();
			out.print(RehabClusteredSystemResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
		<td align="center">
		<%
			 String RehabClusteredSystemNonResidentHousesObjShowStr="";
			 if(RehabClusteredSystemNonResidentHousesObj.getValue()!=0)
			   RehabClusteredSystemNonResidentHousesObjShowStr=new Long(RehabClusteredSystemNonResidentHousesObj.getValue()).toString();
		 %>
		<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
<pdk-html:text styleId="RehabClusteredSystemNonResidentHousesText" property="rehabClusteredSystemNonResidentHouses" size="6"
			onblur="ccpCalculateTotal(this, 'nonResidential', 'clustered', 'RehabClusteredSystemNonResidentHousesTotalSpan')"
			value="<%=RehabClusteredSystemNonResidentHousesObjShowStr%>"/>
</logic:equal>
<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="N">
		<%=RehabClusteredSystemNonResidentHousesObjShowStr%>
		</logic:equal>
		</td>
		<td align="center">
		  <bean:write name="CostCurvePopulationFormBean" property="nonResClusteredPopulationPerHouse"/>
		</td>
		<td align="center">
		<span id="RehabClusteredSystemNonResidentHousesTotalSpan">
		<%
			 String RehabClusteredSystemNonResidentHousesObjTotalStr="";
			 if(RehabClusteredSystemNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResClusteredPopulationPerHouse()!=0)
			   RehabClusteredSystemNonResidentHousesObjTotalStr=
			   	new Long((long)(0.5+RehabClusteredSystemNonResidentHousesObj.getValue()*costCurvePopulationForm.getNonResClusteredPopulationPerHouse())).toString();
			out.print(RehabClusteredSystemNonResidentHousesObjTotalStr);
		 %>
		 </span>
		</td>
	</tr>

		<%
			}
		 %>
<%-- 
	<tr>
		<td width="50%">
			&nbsp;
		</td>
		<td colspan="4" align="left">
			* Residential Population per House: <%=costCurvePopulationForm.getPopulationPerHouseResidential()%>
		</td>
	</tr>
	<tr>
		<td width="50%">
			&nbsp;
		</td>
		<td colspan="4" align="left">
			** Non-residential Population per House: <%=costCurvePopulationForm.getPopulationPerHouseNonResidential()%>
		</td>
	</tr>
--%>
		<%
			} // if per house is good
		 %>

	<TR>
		<TD colspan="5">
			<div align="left" width="150">
			<logic:equal name="CostCurvePopulationFormBean" property="isUpdatable" value="Y">
			<A href="javascript:CostCurvePopulationConfirmAndSubmit('save', '', 'N')">
			<pdk-html:img page="/images/submit.gif" alt="submit" border="0"/></A>				   
			   <FONT size="1">&nbsp;&nbsp;</FONT>
			<A href="javascript:CostCurvePopulationConfirmAndSubmit('reset', '', 'N')">
			<pdk-html:img page="/images/reset.gif" alt="reset" border="0"/></A>	
			</logic:equal>
			</div>
 		</TD>
	</TR>
</TABLE>
		<%
			}
		 %>

</pdk-html:form>