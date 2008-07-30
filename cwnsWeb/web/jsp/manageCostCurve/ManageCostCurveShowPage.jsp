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
	import="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveListHelper"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			
   			ManageCostCurveForm manageCostCurveForm = (ManageCostCurveForm)request.getAttribute("manageCostCurveForm");	
   			
			long tmpFacId = -1;
			tmpFacId = manageCostCurveForm.getManageCostCurveFacilityId();
		
			NameValue[] linkParams = new NameValue[2];
			linkParams[0] = new NameValue("facilityId", Long.toString(tmpFacId));
		
			String tabLinkUrl = null;	
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>
var markDeleted = false;

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
function ManageCostCurveConfirmAndSubmit(action)
{
	var confirmed=true;	
	if (markDeleted){
		confirmed = confirm("Cost will be deleted where Cost Curves are unassigned. Continue?");		
	}
	
	if (confirmed){
       hidden_field = window.document.getElementById("ManageCostCurveAct");
	   hidden_field.value=action;

	   window.document.ManageCostCurveFormBean.submit();
	   return true;
	}
}

function resetManageCostCurveDetails()
{
}

function manageCostCurveConfirmCostId(ctrl)
{
	if(!ctrl.checked)
	{
		markDeleted = true;
		/*if(confirm("Cost will be deleted where Cost Curves are unassigned. Continue?"))
		{
			return true;
		}
		else
		{
			//alert("abort");
			ctrl.checked = true;
			//return false;
		}*/
	}	 
}

function manageCostCurveIdGroupProcess(ctrl, form)
{
      var costCurveIdList = form["costCurveIdList"];	      

	  if(ctrl.value == 1 || ctrl.value == 2 || ctrl.value == 4 || 
	     ctrl.value == 5 || ctrl.value == 6 || ctrl.value == 10)
	  {      
	      for (var i = 0; i < costCurveIdList.length; i++)
	      {
	        var indiCtrl = costCurveIdList[i];
	        
	  		if(indiCtrl.value == 1 || indiCtrl.value == 2 || indiCtrl.value == 4 || indiCtrl.value == 5 || indiCtrl.value == 6 || indiCtrl.value == 10 )
	  		{
	  			if(indiCtrl.value != ctrl.value)
	  			{
					if(ctrl.checked == true)
					{
						// disabled the rest
						indiCtrl.disabled = true;
					}
					else
					{
						// enable the rest
						indiCtrl.disabled = false;
					}	  				
	  			}
	  			// don't do anything to itself		      	
	  		}
	  		// if not, then simply bypass
	      }
      }
      else if(ctrl.value == 11 || ctrl.value == 12 || ctrl.value == 13 || ctrl.value == 15 || ctrl.value == 16 || ctrl.value == 17)
      {
      	var cc1213assigned = false;
      	var cc1516assigned = false;
      	
		for (var i = 0; i < costCurveIdList.length; i++)
		{
		   var indiCtrl = costCurveIdList[i];      
		   
		   if((indiCtrl.value == 12 && indiCtrl.checked == true) ||
		      (indiCtrl.value == 13 && indiCtrl.checked == true))
		      cc1213assigned = true;
		   
		   if((indiCtrl.value == 15 && indiCtrl.checked == true) ||
		      (indiCtrl.value == 16 && indiCtrl.checked == true))
		      cc1516assigned = true;
		}      	
      
        for (var i = 0; i < costCurveIdList.length; i++)
		{
		   var indiCtrl = costCurveIdList[i];
		        
		   if(ctrl.value == 11)
		   {
		  	 if(indiCtrl.value == 12 || indiCtrl.value == 13)
		  	 {
				if(ctrl.checked == true) indiCtrl.disabled = true;
				else indiCtrl.disabled = false; 					  			
		  	 }
		   }
		   else if(ctrl.value == 12 || ctrl.value == 13)
		   {
		   		if(indiCtrl.value == 11)
		   		{
			   		if(cc1213assigned == true)indiCtrl.disabled = true;
			   		else indiCtrl.disabled = false; 
		   		}
		   }
		   else if(ctrl.value == 15)
		   {
		  	 if(indiCtrl.value == 16 || indiCtrl.value == 17)
		  	 {
				if(ctrl.checked == true) indiCtrl.disabled = true;
				else indiCtrl.disabled = false; 					  			
		  	 }
		   }
		   else if(ctrl.value == 16 || ctrl.value == 17)
		   {
		   		if(indiCtrl.value == 15)
		   		{
			   		if(cc1516assigned == true)indiCtrl.disabled = true;
			   		else indiCtrl.disabled = false; 
		   		}
		   }
	      } // last for
      } // else 11 12 13 15 16 17
}

function manageCostCurveViewAllPopUp(popupUrl)
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

<pdk-html:form 	name="ManageCostCurveFormBean" styleId="ManageCostCurveFormBeanId"
				type="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm"
				action="manageCostCurve.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="manageCostCurveAct" property="manageCostCurveAct" value="<%=manageCostCurveForm.getManageCostCurveAct()%>"/>
		<pdk-html:text styleId="manageCostCurveFacilityId" property="manageCostCurveFacilityId" value="<%=manageCostCurveForm.getManageCostCurveFacilityId()%>"/>
		<pdk-html:text styleId="manageCostCurveDocumentId" property="documentId" value="<%=manageCostCurveForm.getDocumentId()%>"/>
	</DIV>

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%"><TR><TD>
<FONT size="4"><STRONG>&nbsp;Cost Curves</STRONG> </FONT>
</TD></TR></TABLE>

<logic:lessEqual name="ManageCostCurveFormBean" property="documentId" value="0">
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

	<TR>
		<TD>
			&nbsp;&nbsp;Please select a document
		</TD>
	</TR>
</TABLE>
</logic:lessEqual>

<logic:greaterThan name="ManageCostCurveFormBean" property="documentId" value="0">
<br/>
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

	<TR>
		<TD class="PortletHeaderColor">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Cost Curve
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG>Assigned</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG>Cost Allocated</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">

				<FONT color="#ffffff"> <STRONG>Error</STRONG>
				</FONT>
			</DIV>
		</TD>
	</TR>

		<% 
        NameValue[] linkParamsCatV       = new NameValue[1];
		linkParamsCatV[0] = new NameValue("facilityId", Long.toString(manageCostCurveForm.getManageCostCurveFacilityId()));
		String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"CostCurveViewAllCatVEvent", linkParamsCatV, true, true);
		%>

<logic:notEmpty scope="request" name="manageCostCurveList">
    <c:set var="i" value="1" />
	<logic:iterate id="manageCostCurveHelper" scope="request" name="manageCostCurveList" type="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveListHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
	<TR class="<c:out value="${class}"/>">
		<TD align="left">
			<!--P align="left" title="<bean:write name="manageCostCurveHelper" property="costCurveId"/>">-->
			<P align="left">
			    <span id="CostCurveLabel_<bean:write name="manageCostCurveHelper" property="costCurveId"/>">			
				<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="N">			
				      <bean:write name="manageCostCurveHelper" property="costCurveName"/>
				</logic:equal>
				<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="Y">
				      <FONT color="#777777"><bean:write name="manageCostCurveHelper" property="costCurveName"/></FONT>
				</logic:equal>		
				</span>
				
				<logic:equal name="manageCostCurveHelper" property="costCurveId" value="7">		
					<logic:equal name="manageCostCurveHelper" property="assignedOrRunCode" value="R">		
						<logic:greaterThan name="manageCostCurveHelper" property="facilityCostCurveId" value="0" >
						      &nbsp;<A href="javascript:void(0);" onclick="ViewAllPopUp('<%=eventPopupWinowUrl%>')">details</A>
						</logic:greaterThan>
					</logic:equal>
				</logic:equal>
			</P>                                                                                                                                                                                                                                    
		</TD>
		<TD align="center">
		  <logic:equal name="ManageCostCurveFormBean" property="isUpdatable" value="Y">
		  	<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="N">
		  		<logic:greaterThan name="manageCostCurveHelper" property="costId" value="0">
				    <pdk-html:multibox name="ManageCostCurveFormBean" property="assignedFacilityCostCurveIds" styleId="costCurveIdList"
				    					onclick="manageCostCurveIdGroupProcess(this, this.form);manageCostCurveConfirmCostId(this)">
				       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
				    </pdk-html:multibox> 
			    </logic:greaterThan>
		  		<logic:lessThan name="manageCostCurveHelper" property="costId" value="0" >
				    <pdk-html:multibox name="ManageCostCurveFormBean" property="assignedFacilityCostCurveIds" styleId="costCurveIdList"
				    					onclick="manageCostCurveIdGroupProcess(this, this.form)">
				       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
				    </pdk-html:multibox> 
			    </logic:lessThan>			    
		    </logic:equal>		    
		  	<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="Y">
		  		<logic:greaterThan name="manageCostCurveHelper" property="costId" value="0">
				    <pdk-html:multibox name="ManageCostCurveFormBean" property="assignedFacilityCostCurveIds" disabled="true" styleId="costCurveIdList"
				    					onclick="manageCostCurveIdGroupProcess(this, this.form);manageCostCurveConfirmCostId(this)">
				       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
				    </pdk-html:multibox> 
			    </logic:greaterThan>
		  		<logic:lessThan name="manageCostCurveHelper" property="costId" value="0" >
				    <pdk-html:multibox name="ManageCostCurveFormBean" property="assignedFacilityCostCurveIds" disabled="true" styleId="costCurveIdList"
				    					onclick="manageCostCurveIdGroupProcess(this, this.form)">
				       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
				    </pdk-html:multibox> 
			    </logic:lessThan>		
		    </logic:equal>				    
		  </logic:equal>

		  <logic:equal name="ManageCostCurveFormBean" property="isUpdatable" value="N">
		    <pdk-html:multibox name="ManageCostCurveFormBean" property="assignedFacilityCostCurveIds" disabled="true">
		       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
		    </pdk-html:multibox> 
		  </logic:equal>
		  <logic:equal name="ManageCostCurveFormBean" property="isUpdatable" value="Y">
			  <div id="hidden_multiboxes" style="display:none">
				  	<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="N">
					    <pdk-html:multibox name="ManageCostCurveFormBean" property="updatedFacilityCostCurveIds">
					       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
					    </pdk-html:multibox> 
				    </logic:equal>		    
				  	<logic:equal name="manageCostCurveHelper" property="disableCostCurve" value="Y">
					    <pdk-html:multibox name="ManageCostCurveFormBean" property="updatedFacilityCostCurveIds" disabled="true">
					       <bean:write name="manageCostCurveHelper" property="costCurveId"/>
					    </pdk-html:multibox> 
				    </logic:equal>				    
			  </div>
	  	  </logic:equal>
		</TD>
		<TD align="center" valign="center">
			<logic:equal name="manageCostCurveHelper" property="costAllocated" value="Y">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</logic:equal>
			<logic:equal name="manageCostCurveHelper" property="costAllocated" value="N">
				&nbsp;
			</logic:equal>
		</TD>
		<TD align="center">
		  <logic:present name="manageCostCurveHelper" property="errorDataAreaNames">
			  <bean:define id="errorDataAreaNameList" property="errorDataAreaNames" name="manageCostCurveHelper"/>
				<logic:iterate id="errorDataAreaNameHelper" name="errorDataAreaNameList" type="gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveErrorsHelper">	
					<logic:equal name="errorDataAreaNameHelper" property="active" value="false">
						<bean:define id="param1" name="errorDataAreaNameHelper" property="navTabText" />
						<%
								String tmpStr = (String) param1;
	
								linkParams[1] = new NameValue("navigationTabType", 
										tmpStr.replaceAll(" & ", "_AND_").replaceAll(" ", "_"));
	
								String eventStr = tmpStr.replaceAll(" & ", "_AND_");
								eventStr = eventStr.replaceAll(" ", "_");
								eventStr = eventStr + "_Tab";
								
								tabLinkUrl = CWNSEventUtils.constructEventLink(prr, eventStr, linkParams, true, true);
						%>
						<A HREF="<%=tabLinkUrl%>">
							<Font class="error"><bean:write name="errorDataAreaNameHelper" property="errorDataAreaName"/></Font>
						</A>				
					</logic:equal>				
					<logic:equal name="errorDataAreaNameHelper" property="active" value="true">
						<Font class="error"><bean:write name="errorDataAreaNameHelper" property="errorDataAreaName"/></Font>
					</logic:equal>				
					&nbsp;
				</logic:iterate>
			</logic:present>
			&nbsp;
		</TD>
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

			<TR>
				<TD colspan="4">
				<div align="left" width="150">
				<logic:equal name="ManageCostCurveFormBean" property="isUpdatable" value="Y">	
					<logic:equal name="ManageCostCurveFormBean" property="atLeastOneEnabled" value="Y">						  
						<A href="javascript:ManageCostCurveConfirmAndSubmit('save')">
						<pdk-html:img page="/images/submit.gif" alt="save" border="0"/></A>				   
					   <FONT size="1">&nbsp;&nbsp;</FONT>
						<A href="javascript:ManageCostCurveConfirmAndSubmit('reset')">
						<FONT size="1">
						<pdk-html:img page="/images/reset.gif" alt="reset" border="0"/>
						</FONT>
						</A>
					</logic:equal>					   
				</logic:equal>				   
				</div>			   
   			   </TD>
			</TR>
</logic:notEmpty>
<logic:empty scope="request" name="manageCostCurveList">
				<TR>
				<TD colspan="4" align="left">
					&nbsp;&nbsp;No Cost Curves are available for selection.
	   			</TD>
			    </TR>
</logic:empty>			
</TABLE>
</logic:greaterThan>
</pdk-html:form>