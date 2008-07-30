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
	import="gov.epa.owm.mtb.cwns.capitalCost.CapitalCostForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.capitalCost.CapitalCostListHelper"
	import="gov.epa.owm.mtb.cwns.capitalCost.CategoryClassificationHelper"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		    String ajaxurl = url +"/javascript/prototype.js";
		    String toolTipurl = url +"/javascript/tooltip.js";		    
			
   			CapitalCostForm capitalCostForm = (CapitalCostForm)request.getAttribute("capitalCostForm");	
   			Collection categoryClassificationList0	= (Collection)request.getAttribute("categoryClassificationList");
   			
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
function CapitalCostConfirmAndSubmit(action, docId, hasCost)
{
   if(action == "save")
   {
	  var formId = window.document.getElementById("CapitalCostFormBeanId");
	  
	  if(formId != undefined)
	  {
	    if(validateCapitalCostFormBean(formId) == false)
	    {
	      return;
	    }
	  }
	  
	  var baseAmountCtrl = window.document.getElementById("capitalCostbaseAmount");
	  var baseAmount = baseAmountCtrl.value;
	  
	  if (!isAllDigits(baseAmount) || 
	  	(baseAmount<=0 || baseAmount>=99999999999)){
	  	alert("Base Amount must be number (all digits), greater than 0 and less than 99999999999.");
	  	return;
	  }
	  	  
	  var ssfCtrl = window.document.getElementById("srfEligible");
	  var stateUser = '<%=request.getAttribute("stateUser")%>'	 
	  
	  if(stateUser=='true' && (ssfCtrl.value == "" || trim(ssfCtrl.value) == ""))
	  {
  			alert("SRF Eligible is required.");
  			ssfCtrl.focus();
  			return;	  	
	  }
	  
	  if(window.document.getElementById("capitalCostValidForSSOHidden").value == "Y")
	  {
	  	var ssoList=formId['capitalCostSSO'];
  		var ssoIdSelected = ssoList.options[ssoList.selectedIndex].value;
  		if(ssoIdSelected == "")
  		{
  			alert("Sanitary Sewer Overflow is required.");
  			ssoList.focus();
  			return;
  		}
	  }
	  
	  if(window.document.getElementById("capitalCostIsAddAction").value == 'Y')
	  {

        <logic:equal name="remote" value="true">
        new Ajax.Request('/cwns/servlet/AjaxRequestProcessor',
         { method: 'post',
          asynchronous: true,
          parameters: {url: '<%=url%>/servlet/CapitalCostUniquenessCheck?combinedId='+encodeURIComponent($('costCategory').value) + '|' + encodeURIComponent($('classification').value) + '|' + encodeURIComponent($('costNeedType').value) + '|' + encodeURIComponent($('capitalCostSSO').value) + '|' + encodeURIComponent($('capitalCostFacilityId').value) + '|' + encodeURIComponent($('capitalCostDocumentId').value), test: ' '},
       </logic:equal>
       
	   <logic:notEqual name="remote" value="true">
       new Ajax.Request('/cwns/servlet/CapitalCostUniquenessCheck',
        { method: 'post',
          asynchronous: true,
          parameters: {combinedId: encodeURIComponent($('costCategory').value) + '|' + encodeURIComponent($('classification').value) + '|' + encodeURIComponent($('costNeedType').value) + '|' + encodeURIComponent($('capitalCostSSO').value) + '|' + encodeURIComponent($('capitalCostFacilityId').value) + '|' + encodeURIComponent($('capitalCostDocumentId').value), test: ' '},
       </logic:notEqual>    

          onSuccess: function(transport){       
          	var response = transport.responseText || "no response text";       
         	
          	if(response.indexOf("Y") >= 0)
		    {
		       hidden_field = window.document.getElementById("CapitalCostAct");
			   hidden_field.value=action;
			   window.document.CapitalCostFormBean.submit();    	
		    }
			else if(response.indexOf("N") >= 0)
				alert('Error: The combination of category, classification, need type and SSO must be unique.');     
          },
          onFailure: function(){ alert('Ajax failed. Please contact your system adminstrator.') }          
        });
      } // if capitalCostIsAddAction
      else 
      {
      	// edit, just save
       hidden_field = window.document.getElementById("CapitalCostAct");
	   hidden_field.value=action;
	   window.document.CapitalCostFormBean.submit();          	
      }
      
      
	  
   }
   else if(action == "delete")
   {
    if(hasCost == "Y")
    {
     var result = confirm("Continue with Delete?");
      if (result == false)
      {
		return;
      }
    } 
   	  setCapitalCostId(docId);
   }
   else if(action == "edit")
   {
   		setCapitalCostId(docId);
   }   
   else if(action == "mark delete")
   {
     hidden_field = window.document.getElementById("CapitalCostAct");
     hidden_field.value=action;
     setCapitalCostId(docId);
     window.document.CapitalCostFormBean.submit();
     return true;
   }
   
   if(action != "save")
   {
       hidden_field = window.document.getElementById("CapitalCostAct");
	   hidden_field.value=action;
	   
	   window.document.getElementById("costNeedType").disabled=false;

	   window.document.CapitalCostFormBean.submit();
	   return true;
   }
}

function setCapitalCostId(docId)
{
	window.document.getElementById("capitalCostId").value = docId;
}

// Show or Hide the add comment area of the Portlet
function showOrHideCapitalCostDetails(id, mode)
{
     var d = window.document.getElementById(id);
     
     if(d!=null)
       d.style.display=mode;
}

   var catClassfArray = new Array();
   
	<%
      if(categoryClassificationList0!=null)
	  {
		Iterator iter0 = categoryClassificationList0.iterator();
	
	   	int catIndex = 0;
	   	    	
	   	while ( iter0.hasNext() ) 
	   	{
	   	    CategoryClassificationHelper cch = (CategoryClassificationHelper) iter0.next();

	%>
			catClassfArray[<%=catIndex%>] = new Array();

			catClassfArray[<%=catIndex%>][0] = "<%=cch.getCostCategoryId()%>";
			catClassfArray[<%=catIndex%>][1] = "<%=cch.getCostCategoryName()%>";
			catClassfArray[<%=catIndex%>][2] = "<%=cch.getClassificationId()%>";
			catClassfArray[<%=catIndex%>][3] = "<%=cch.getClassificationName()%>";
			catClassfArray[<%=catIndex%>][4] = "<%=cch.getCategoryValidForSSO()%>";
			catClassfArray[<%=catIndex%>][5] = "<%=cch.getCategoryNoFacilityTypeChange()%>";		
			catClassfArray[<%=catIndex%>][6] = "<%=cch.getCategoryNoClassification()%>";		
	<%
			catIndex++;
	    }
	  }
	%>

function setClassificationsAndSSO(form)
{
  		var classFList=form['classification'];

		classFList.options.length = 0;
	
  		classFList.options[0] = new Option("Select a Classification", "");
		
  		var catList=form['costCategory'];
  		var catIdSelected = catList.options[catList.selectedIndex].value;
  		
  		var classFLength = catClassfArray.length;
  		
  		var iClassFIndex = 1;
  		
  		var validForSSO = "";
  		
  		var setNeedTypeSSE = "";
  		
  		var noClassification = "";
  		
  		for(iClassF=0; iClassF<classFLength; iClassF++)
  		{
  			if(catIdSelected!=null && catIdSelected==catClassfArray[iClassF][0])
  			{
  				if (!classValAlreadyAdded(classFList, catClassfArray[iClassF][2])){
	  				classFList.options[iClassFIndex] = new Option(catClassfArray[iClassF][3], catClassfArray[iClassF][2]);
	  				classFList.options[iClassFIndex].title = catClassfArray[iClassF][3];
					iClassFIndex++;
				}
				
				validForSSO = catClassfArray[iClassF][4];				
				setNeedTypeSSE = catClassfArray[iClassF][5];
				noClassification = catClassfArray[iClassF][6];				
  			}
  		}	

		capitalCostSetNeedType(setNeedTypeSSE, form);

  		capitalCostShowHideSSO(validForSSO, form);
  		
		capitalCostShowHideClassification(noClassification, form);

}

function classValAlreadyAdded(classFList, classValue){
	var alreadyAdded=false;
	for(i=0; i < classFList.options.length; i++){
		if (classFList.options[i].value==classValue){
			alreadyAdded = true;
			break;
		}
	}
	return alreadyAdded;
}

function capitalCostShowHideClassification(noClassification, form)
{
	  	if(noClassification == "N")
  		{
  			window.document.getElementById("capitalCostClassificationShowHide").style.display = "block";
  			window.document.getElementById("capitalCostClassificationTextOnly").style.display = "none";
  		}
  		else if(noClassification == "Y")
  		{
  			window.document.getElementById("capitalCostClassificationShowHide").style.display = "none";
  			window.document.getElementById("capitalCostClassificationTextOnly").style.display = "block";  				
  		}
  		
  		//form['capitalCostSSO'].options[0].selected = "1";
}

function capitalCostSetNeedType(flag, form)
{

	var needTypeList=form['costNeedType'];
	
	var selectedNeedTypeIndex = needTypeList.selectedIndex;
		
	needTypeList.options.length = 0;
	
	if(flag == "Y")
	{
		// set it to SSE only
		needTypeList.options[0] = new Option("Unofficial", "S");
		needTypeList.selectedIndex = 0;
	}
	else if(flag == "N")
	{
		needTypeList.options[0] = new Option("Official", "F");
		needTypeList.options[1] = new Option("Unofficial", "S");
	    // restore it to Official and Unofficial
	    needTypeList.selectedIndex = selectedNeedTypeIndex;
	}
}

function capitalCostShowHideSSO(validForSSO, form)
{
	  	if(validForSSO == "Y")
  		{
  			window.document.getElementById("capitalCostSSOShowHide").style.display = "block";
  			window.document.getElementById("capitalCostSSOTextOnly").style.display = "none";
  			window.document.getElementById("capitalCostValidForSSOHidden").value = "Y"; 			
  		}
  		else if(validForSSO == "N")
  		{
  			window.document.getElementById("capitalCostSSOShowHide").style.display = "none";
  			window.document.getElementById("capitalCostSSOTextOnly").style.display = "block";  
  			window.document.getElementById("capitalCostValidForSSOHidden").value = "N";  					
  		}
  		
  		form['classification'].options[0].selected = "1";
}

function calculateCapitalCostAdjustedAmount(baseAmt, labelCtrlId, hiddenCtrlId)
{
  var monthlyCostIndex = <%=capitalCostForm.getMonthlyCostIndexAmt()%>;
  var monthlyBaseCostIndex = <%=capitalCostForm.getMonthlyBaseCostIndexAmt()%>;
  var result = baseAmt * monthlyBaseCostIndex / monthlyCostIndex;
  adjustAmtField = window.document.getElementById(hiddenCtrlId);
  if(adjustAmtField !=null)
    adjustAmtField.value = Math.round(result);
  
  window.document.getElementById(labelCtrlId).innerHTML = formatCurrency(Math.round(result));

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


function isAllDigits(argvalue) {
	argvalue = argvalue.toString();
    var validChars = "0123456789";
    var startFrom = 0;
    for (var n = startFrom; n < argvalue.length; n++) {
        if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
    }
    return true;
}
</SCRIPT>

<pdk-html:form 	name="CapitalCostFormBean" styleId="CapitalCostFormBeanId"
				type="gov.epa.owm.mtb.cwns.capitalCost.CapitalCostForm"
				action="capitalCost.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="capitalCostAct" property="capitalCostAct" value="<%=capitalCostForm.getCapitalCostAct()%>"/>
		<pdk-html:text styleId="capitalCostId" property="capitalCostId" value="<%=capitalCostForm.getCapitalCostId()%>"/>
		<pdk-html:text styleId="capitalCostFacilityId" property="capitalCostFacilityId" value="<%=capitalCostForm.getCapitalCostFacilityId()%>"/>
		<pdk-html:text styleId="capitalCostDocumentId" property="capitalCostDocumentId" value="<%=capitalCostForm.getDocumentId()%>"/>
		<pdk-html:text styleId="capitalCostIsAddAction" property="capitalCostIsAddAction" value="<%=capitalCostForm.getIsAddAction()%>"/>
	</DIV>

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%"><TR><TD>
<FONT size="4"><STRONG>&nbsp;Capital Costs</STRONG> </FONT>
</TD></TR></TABLE>

<logic:present name="warnings">
<div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="warning" name="warnings" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/warn24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=warning%>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>
</logic:present> 

<logic:lessEqual name="CapitalCostFormBean" property="documentId" value="0">
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
	<TR>
		<TD>
			&nbsp;&nbsp;Please select a document
		</TD>
	</TR>
</TABLE>
</logic:lessEqual>

<logic:greaterThan name="CapitalCostFormBean" property="documentId" value="0">
<br/>
<i><FONT size="2" color="#999999">
&nbsp;&nbsp;&nbsp;Adjusted to <bean:write name="CapitalCostFormBean" property="costIndexBaseDate"/>
</FONT></i>
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

	<TR>
		<TD class="PortletHeaderColor" width="25%">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Category
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor" width="15%">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG>Classification</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Cost Type </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="10%">
			<DIV align="center">

				<FONT color="#ffffff"> <STRONG> Need Type </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Base ($) </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Adjusted ($) </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> % SRF Eligible </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Sanitary Sewer Overflow </STRONG>
				</FONT>
			</DIV>
		</TD>
        <logic:equal name="CapitalCostFormBean" property="isUpdatable" value="Y">
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
         <logic:notEqual name="CapitalCostFormBean" property="isUpdatable" value="Y">
			<logic:equal name="stateUser" value="false">
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Delete </STRONG>
					</FONT>
				</DIV>
			</TD>
			</logic:equal>
         </logic:notEqual>
	</TR>

    <c:set var="i" value="1" />
	<logic:iterate id="capitalCostHelper" scope="request" name="capitalCostList" type="gov.epa.owm.mtb.cwns.capitalCost.CapitalCostListHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
				
			<logic:equal name="capitalCostHelper" property="capitalCostId" value="<%=capitalCostForm.getCapitalCostId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		    </logic:equal>

	<TR class="<c:out value="${class}"/>">
		<TD align="left">
			<P align="left" title="<bean:write name="capitalCostHelper" property="category"/>">
				      <bean:write name="capitalCostHelper" property="category"/>:
				      <bean:write name="capitalCostHelper" property="categoryName"/>
			</P>                                                                                                                                                                                                                                    
		</TD>
		<TD align="left">
			<P align="left">
				<bean:write name="capitalCostHelper" property="classification"/>
			</P>
		</TD>
		<TD align="center" valign="center">
			<bean:write name="capitalCostHelper" property="costMethodCode"/>
		</TD>
		<TD align="center">	
				<bean:write name="capitalCostHelper" property="costNeedType"/>
		</TD>
		<TD align="right" valign="center">
			      <fmt:formatNumber type="currency" pattern="#,##0">
			      	<bean:write name="capitalCostHelper" property="baseAmount"/>
			      </fmt:formatNumber>					
		</TD>
		<TD align="right">
			      <fmt:formatNumber type="currency" pattern="#,##0">
			      	<bean:write name="capitalCostHelper" property="adjustedAmount"/>
			      </fmt:formatNumber>		
		</TD>
		<TD align="center">
			      	<bean:write name="capitalCostHelper" property="srfEligible"/>
		</TD>
		<TD align="center">
			<logic:equal name="capitalCostHelper" property="categoryValidForSSO" value="Y">
				<logic:equal name="capitalCostHelper" property="sso" value="Y">
				      	Yes
				</logic:equal>
				<logic:equal name="capitalCostHelper" property="sso" value="N">
				      	No
				</logic:equal>
				<logic:equal name="capitalCostHelper" property="sso" value="">
				      	&nbsp;
				</logic:equal>				
			</logic:equal>
			<logic:equal name="capitalCostHelper" property="categoryValidForSSO" value="N">
				<logic:equal name="capitalCostHelper" property="sso" value="Y">
				      	Yes
				</logic:equal>
				<logic:equal name="capitalCostHelper" property="sso" value="N">
				      	No
				</logic:equal>
				<logic:equal name="capitalCostHelper" property="sso" value="">
				      	N/A
				</logic:equal>
			</logic:equal>			
		</TD>
        <logic:equal name="CapitalCostFormBean" property="isUpdatable" value="Y">
			<TD align="center">
				<P align="center">
				<A href="javascript:CapitalCostConfirmAndSubmit('edit', '<bean:write name="capitalCostHelper" property="capitalCostId"/>', 'N')">
					<pdk-html:img page="/images/edit.gif" alt="Edit" border="0"/>
				</A>
				</P>
			</TD>
			<TD align="center">
				<logic:equal name="stateUser" value="true">
					<logic:equal name="capitalCostHelper" property="costMethodCode" value="D">
						<A href="javascript:CapitalCostConfirmAndSubmit('delete', '<bean:write name="capitalCostHelper" property="capitalCostId"/>', '<bean:write name="capitalCostHelper" property="canDelete"/>')">
							<pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
						</A>
					</logic:equal>
					<logic:equal name="capitalCostHelper" property="costMethodCode" value="C">
							<pdk-html:img page="/images/delete_disable.gif" alt="can't delete a cost-curve cost" border="0"/>
					</logic:equal>
				</logic:equal>
				<logic:equal name="stateUser" value="false">	
					<bean:define id="costId"><bean:write name="capitalCostHelper" property="capitalCostId"/></bean:define>
					<bean:define id="canRemove"><bean:write name="capitalCostHelper" property="canDelete"/></bean:define>
					<bean:define id="clickSubmit">
						javascript:CapitalCostConfirmAndSubmit('mark delete', '<%=costId%>', '')
					</bean:define>
								
				    <pdk-html:checkbox name="capitalCostHelper" property="feedbackDeleteFlag"  value="Y"
		                  onclick="<%=clickSubmit%>">
				    </pdk-html:checkbox>
				</logic:equal>
			</TD>
       </logic:equal>
       
       <logic:notEqual name="CapitalCostFormBean" property="isUpdatable" value="Y">
			<logic:equal name="stateUser" value="false">
			<TD align="center">
				<pdk-html:checkbox name="capitalCostHelper" property="feedbackDeleteFlag"  value="Y"
		                 disabled="true">
				</pdk-html:checkbox>
			</TD>
			</logic:equal>
       </logic:notEqual>
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

<%
    String showDetail = "none";
	String showAddLink = "block";
	
    if((capitalCostForm.getDetailEditExpand()).equals("Y"))
    {
      showDetail = "block";
      showAddLink = "none";
    }    
    
    if (capitalCostForm.getValidForCost().equals("N")){
    	showAddLink = "none";
    }
    
    if (request.getAttribute("canNotAddCost").equals("Y")){
    	showAddLink = "none";
		showDetail = "none";
    }    
%>
	

<TR>
		<TD colspan="11" align="left">
		 <DIV id="addNewCapitalCostLink" style="display:<%=showAddLink%>" class="PortletText1">
	     	<logic:equal name="CapitalCostFormBean" property="isUpdatable" value="Y">
	     	<logic:equal name="CapitalCostFormBean" property="validForCost" value="Y">		 
				<a href="javascript: setCapitalCostId(''); CapitalCostConfirmAndSubmit('new', '', 'N');">
					Add Cost
				</a>
			</logic:equal>
      		</logic:equal>&nbsp;
		 </DIV>

	<logic:equal name="canNotAddCost" value="Y">
	<div class="MessageZone">
	     <table width="100%" border="0" cellpadding="0" cellspacing="0">	      
	     	<tr>
	     		<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/err24.gif" border="0" alt="Data Area Errors"/>
	     		</td>
	     		<td class="ErrorText" align="left" valign="middle">	     	
					Cannot Add Cost, at least one Facility Type must be assigned to the Facility
				</td>
			</tr>
		</table>
	</div><br>&nbsp;
	</logic:equal>
	</TD>
</TR>



</TABLE>

<DIV id="capitalCostDetails" style="display:<%=showDetail%>; background-color: #cccccc; padding: 5"  class="PortletText1">
<BR>
		<P>&nbsp;<Strong>
		   <FONT size="4">
					  Add/Edit Cost&nbsp;
		   </FONT>&nbsp;</Strong>
		</P>

		<TABLE border="0" class="PortletText1" cellspacing="1" cellpadding="1" width="100%" 
		          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Category: </STRONG>
				</TD>
				<TD>
					<logic:equal name="CapitalCostFormBean" property="isAddAction" value="Y">
						<pdk-html:select name="CapitalCostFormBean" 
										 styleId="costCategory" 
										 property="costCategory" 
										 onchange="setClassificationsAndSSO(this.form);"
										 size="1" 
										 style="width: 200px">
						<OPTION value="">
							Select a Category
						</OPTION>					
						<%
						    String oldCatId = "*****";
						    
							Iterator iter = categoryClassificationList0.iterator();
						   	    	
						   	while ( iter.hasNext() ) 
						   	{
						   	    CategoryClassificationHelper cch = (CategoryClassificationHelper) iter.next();
					
								if(oldCatId.equals(cch.getCostCategoryId()))
									continue;
									
								oldCatId = cch.getCostCategoryId();
						%>
								<OPTION title="<%=cch.getCostCategoryId()%>: <%=cch.getCostCategoryName()%>"
								        value="<%=cch.getCostCategoryId()%>">
								        <%=cch.getCostCategoryId()%>: <%=cch.getCostCategoryName()%>
								</OPTION>					
						<%
						    }
						%>					
						</pdk-html:select>
					</logic:equal>
					<logic:equal name="CapitalCostFormBean" property="isAddAction" value="N">
							<bean:write name="CapitalCostFormBean" property="costCategory" />: <bean:write name="CapitalCostFormBean" property="costCategoryName" />
							<pdk-html:hidden name="CapitalCostFormBean" property="costCategory" styleId="costCategory"/>
					</logic:equal>					
				</TD>
				<TD colspan="2">						
					<STRONG> Cost Method: </STRONG>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<logic:equal name="CapitalCostFormBean" property="costMethodCode" value="">
						Documented	
					</logic:equal>	
					<logic:equal name="CapitalCostFormBean" property="costMethodCode" value="D">
						Documented
					</logic:equal>						
					<logic:equal name="CapitalCostFormBean" property="costMethodCode" value="C">
						Cost Curve
					</logic:equal>	
					<pdk-html:hidden name="CapitalCostFormBean" property="costMethodCode"/>
				</TD>
			</TR>
<%
    String showClassificationDropDown = "block";
	String showClassificationTextOnly = "none";
	
    if((capitalCostForm.getCategoryNoClassification()).equals("Y"))
    {
      showClassificationDropDown = "none";
      showClassificationTextOnly = "block";
    }
%>						
			<TR>
				<TD>
					<br/><span class="required">&nbsp;</span>
				</TD>	
				<TD valign="center"><STRONG>Classification: </STRONG>
				</TD>
				<TD valign="center">
					<SPAN id="capitalCostClassificationShowHide" style="display:<%=showClassificationDropDown%>" class="PortletText1">
						<pdk-html:select name="CapitalCostFormBean" 
										 styleId="classification" 
										 property="classification" 
										 size="1" 
										 style="width: 200px">
						<pdk-html:option value="">
							Select a Classification
						</pdk-html:option>
						<logic:equal name="CapitalCostFormBean" property="isAddAction" value="N">
						<logic:notEqual name="CapitalCostFormBean" property="costCategory" value="">
							<%
						      if(categoryClassificationList0!=null && capitalCostForm.getCostCategory()!=null && capitalCostForm.getCostCategory().length() > 0 )
							  {
								Iterator iter1 = categoryClassificationList0.iterator();   	    	
							   	while ( iter1.hasNext() ) 
							   	{
							   	    CategoryClassificationHelper cch = (CategoryClassificationHelper) iter1.next();
							   	    
							   	    if(cch.getCostCategoryId()!=null && capitalCostForm.getCostCategory().equals(cch.getCostCategoryId()))
							   	    {
							%>
									<pdk-html:option value="<%=cch.getClassificationId()%>">
										<%=cch.getClassificationName()%>
									</pdk-html:option>
							<%
							       }
							    }
							  }
							%>
						</logic:notEqual>	
						</logic:equal>																		
						</pdk-html:select>		
					</SPAN>	
					<SPAN id="capitalCostClassificationTextOnly" style="display:<%=showClassificationTextOnly%>" class="PortletText1">
					    N/A
					</SPAN>							
				</TD>
				<TD>						
						&nbsp;
				</TD>
				<TD>
						&nbsp;	
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Need Type: </STRONG>
				</TD>
				<TD>
				<%
					if(capitalCostForm.getPrivateAllowFederal().equals("N") ||
					(request.getAttribute("stateUser").toString().equals("false") && 
						capitalCostForm.getPrivateAllowFederal().equals("Y")) ||
					(request.getAttribute("stateUser").toString().equals("true") &&
					request.getAttribute("allTypesNoChange").toString().equals("Y")))
					{				
				 %>
						<pdk-html:select name="CapitalCostFormBean" 
										 styleId="costNeedType" 
										 property="costNeedType" 
										 size="1" 
										 style="width: 200px">
							<pdk-html:option value="S">Unofficial
							</pdk-html:option>		
						</pdk-html:select>	
				<%
					}
					else
					{
				%>				 
						<pdk-html:select name="CapitalCostFormBean" 
										 styleId="costNeedType" 
										 property="costNeedType" 
										 size="1" 
										 style="width: 200px">			
							<pdk-html:option value="F">Official
							</pdk-html:option>					
							<pdk-html:option value="S">Unofficial
							</pdk-html:option>		
						</pdk-html:select>	
				<%
					}
				%>						
				</TD>
				<TD>						
						&nbsp;
				</TD>
				<TD>
						&nbsp;	
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Base Amount ($): </STRONG>
				</TD>
				<TD>
					<logic:equal name="CapitalCostFormBean" property="costMethodCode" value="C">
						<fmt:formatNumber type="currency" pattern="#,##0">
						<bean:write name="CapitalCostFormBean" property="baseAmount" />
						</fmt:formatNumber>
						<pdk-html:hidden name="CapitalCostFormBean" styleId="capitalCostbaseAmount" property="baseAmount"/>
					</logic:equal>
					<logic:notEqual name="CapitalCostFormBean" property="costMethodCode" value="C">
					  <logic:equal name="CapitalCostFormBean" property="baseAmount" value="0">
						<pdk-html:text name="CapitalCostFormBean" 
									   property="baseAmount" 
									   styleId="capitalCostbaseAmount"
									   value="" maxlength="11"
									   onblur="calculateCapitalCostAdjustedAmount(this.value, 'capitalCostAdjustedAmtLabel', 'capitalCostadjustedAmount')"/>
					  </logic:equal>	 			   
					  <logic:notEqual name="CapitalCostFormBean" property="baseAmount" value="0">
						<pdk-html:text name="CapitalCostFormBean" 
									   property="baseAmount" maxlength="11"
									   styleId="capitalCostbaseAmount"
									   onblur="calculateCapitalCostAdjustedAmount(this.value, 'capitalCostAdjustedAmtLabel', 'capitalCostadjustedAmount')"/>
					  </logic:notEqual>	 
					</logic:notEqual>  			   				  
				</TD>
				<TD colspan="2">						
				<STRONG> Adjusted Amount ($): </STRONG>&nbsp;&nbsp;&nbsp;
				<span id="capitalCostAdjustedAmtLabel">
				    <fmt:formatNumber type="currency" pattern="#,##0">
						<bean:write name="CapitalCostFormBean" property="adjustedAmount" />
					</fmt:formatNumber>
				</span>
                    <pdk-html:hidden styleClass="readOnlyTextArea" 
                    			   name="CapitalCostFormBean" property="adjustedAmount" styleId="capitalCostadjustedAmount"/>	            			   				                      			   										
				</TD>
			</TR>																					
			<TR>
				<TD><logic:equal name="stateUser" value="true"><span class="required">*</span></logic:equal></TD>
				<TD><STRONG> SRF Eligible (%): </STRONG>
				</TD>
				<TD>
					<pdk-html:text name="CapitalCostFormBean" property="srfEligible" maxlength="3" styleId="srfEligible"/>
				</TD>
				<TD>						
					  &nbsp;	
				</TD>
				<TD>
				      &nbsp;					
				</TD>
			</TR>
<%
    String showSSODropDown = "block";
	String showSSOTextOnly = "none";
	String stateUser = request.getAttribute("stateUser").toString();
	
    if((capitalCostForm.getCategoryValidForSSO()).equals("N") ||
    ((capitalCostForm.getCategoryValidForSSO()).equals("Y") && stateUser.equals("false")))
    {
      showSSODropDown = "none";
      showSSOTextOnly = "block";
    }
%>			
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Sanitary Sewer <br/>Overflow: </STRONG>
				</TD>
				<TD>
					<SPAN id="capitalCostSSOShowHide" style="display:<%=showSSODropDown%>" class="PortletText1">
						<pdk-html:select name="CapitalCostFormBean" 
										 styleId="capitalCostSSO" 
										 property="sso" 
										 size="1" 
										 style="width: 240px">										 
							<pdk-html:option value="">
								Does this Cost address an SSO?
							</pdk-html:option>					
							<pdk-html:option value="Y">Yes
							</pdk-html:option>					
							<pdk-html:option value="N">No
							</pdk-html:option>		
						</pdk-html:select>		
					</SPAN>	
					<SPAN id="capitalCostSSOTextOnly" style="display:<%=showSSOTextOnly%>" class="PortletText1">
					    N/A
					</SPAN>	
				</TD>
				<TD>						
						&nbsp;<input type="hidden" id="capitalCostValidForSSOHidden" value="<bean:write name="CapitalCostFormBean" property="categoryValidForSSO"/>"/>
				</TD>
				<TD>
						&nbsp;	
				</TD>
			</TR>

			<TR>
				<TD colspan="4">
				<div align="left" width="150">
				<logic:equal name="CapitalCostFormBean" property="isUpdatable" value="Y">	
				<INPUT type="button" value="Save" 
					alt="Save"
					onclick="javascript:CapitalCostConfirmAndSubmit('save', '', 'N')"/>			   
				   <FONT size="1">&nbsp;&nbsp;</FONT>
				</logic:equal>	
				<INPUT type="button" value="Cancel" 
					alt="Cancel"
					onclick="javascript:{showOrHideCapitalCostDetails('addNewCapitalCostLink', 'block'); showOrHideCapitalCostDetails('capitalCostDetails', 'none')}"/>								   
				</div>			   
   			   </TD>
			</TR>
		</TABLE>
</DIV>

</logic:greaterThan>
</pdk-html:form>
<pdk-html:javascript formName="CapitalCostFormBean" staticJavascript="true"/>