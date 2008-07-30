<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils"
	%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	String resetActionUrl = CWNSStrutsUtils.createHref(request,"effluent.do");	
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pRequest,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>
<%--
window.onload=function(){

<logic:equal name="present" value="Y">
    <logic:equal name="presentDischargeSurfaceWater" value="Y">
          if(form['presentEffluentLevelId'].value=='10' || form['presentEffluentLevelId'].value=='20' || form['presentEffluentLevelId'].value=='30'){
          	alert("Present Discharge to surface water is less than Secondary.....");
          } 
    </logic:equal>
</logic:equal>
<logic:equal name="projected" value="Y">
     <logic:equal name="projectedDischargeSurfaceWater" value="Y">
          if(form['projectedEffluentLevelId'].value=='10' || form['projectedEffluentLevelId'].value=='20' || form['projectedEffluentLevelId'].value=='30'){
          	alert("Projected Discharge to surface water is less than Secondary.");         	   
          } 
     </logic:equal>
</logic:equal>

}--%>

function effluentSave(){
  var form = window.document.effluentForm;
  if(validateEffluent(form)==false)
    return;
  window.document.effluentForm.submit();
}

function validateEffluent(form){
   <logic:equal name="present" value="Y">
       if(form['presentEffluentLevelId'].value==''){
          alert('Present Treatment Level is required');
          return false;
       }
       if(form['presentEffluentLevelId'].value=='50'){
          if(!isCheckBoxesSelected(form,'presentTreatmentIndicator')){
			alert('At least one present Advance Treatment Indicator should be selected');
            return false;          
          }         
       }
       <%--
       <logic:equal name="presentDischargeSurfaceWater" value="Y">
          if(form['presentEffluentLevelId'].value=='10' || form['presentEffluentLevelId'].value=='20' || form['presentEffluentLevelId'].value=='30'){
          	
          	var res = confirm("Present Discharge to surface water is less than Secondary. Continue with the update?");
         	if (res == false){
         	   return false;
			}
			       	   
          } 
       </logic:equal>--%>
	</logic:equal>
	<logic:equal name="projected" value="Y">
       if(form['projectedEffluentLevelId'].value==''){
          alert('Projected Treatment Level is required');
          return false;
       }
       if(form['projectedEffluentLevelId'].value=='50'){
          if(!isCheckBoxesSelected(form,'projectedTreatmentIndicator')){
			alert('At least one projected Advance Treatment Indicator should be selected');
            return false;          
          }         
       }
       <%--
       <logic:equal name="projectedDischargeSurfaceWater" value="Y">
          if(form['projectedEffluentLevelId'].value=='10' || form['projectedEffluentLevelId'].value=='20' || form['projectedEffluentLevelId'].value=='30'){
          	var res = confirm("Projected Discharge to surface water is less than Secondary. Continue with the update?");
         	if (res == false){
         	   return false;
			}
		  } 
       </logic:equal>--%>
	</logic:equal>
	
	
	return true;
}

function onChangePresentEffluentLevel(form){
   if(form['presentEffluentLevelId'].value !=50){
      disableCheckBoxes(form,'presentTreatmentIndicator' ,true);
   }else{
      disableCheckBoxes(form,'presentTreatmentIndicator' ,false); 
   }
}

function onChangeProjectedEffluentLevel(form){
   if(form['projectedEffluentLevelId'].value !=50){
      disableCheckBoxes(form,'projectedTreatmentIndicator' ,true);
   }else{
      disableCheckBoxes(form,'projectedTreatmentIndicator', false); 
   }
}

function disableCheckBoxes(form,fieldName,on){
   var checkboxes = form[fieldName];
   if(checkboxes != undefined && checkboxes.length != undefined && checkboxes.length > 0){ 
	for(i=0; i< checkboxes.length; i++){
	    checkboxes[i].checked=false;
		checkboxes[i].disabled = on;
	}
   }	
}

function isCheckBoxesSelected(form,fieldName){
   var checkboxes = form[fieldName];
   if(checkboxes != undefined && checkboxes.length != undefined && checkboxes.length > 0){ 
	 for(i=0; i< checkboxes.length; i++){
	    if(checkboxes[i].checked)return true;
	 }
   }
   return false;	
}

</script>
<pdk-html:form name="effluentForm" method="get" styleId="effluentForm" type="org.apache.struts.action.DynaActionForm" action="saveEffluent.do">
<logic:notEmpty name="stateViewWarnings" >
<div id="stateViewWarnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="stateViewWarnMessage" name="stateViewWarnings" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/warn24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=stateViewWarnMessage%>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>
</logic:notEmpty>
<TABLE align="center" border="0" width="100%" class="PortletText1">
	<TR class="PortletHeaderColor">
		<TD></TD>
		<TD class="PortletHeaderText">
			<P align="center" style="COLOR: white">
				Present
			</P>
		</TD>
		<TD class="PortletHeaderText">
			<P align="center" style="COLOR: white">
				Projected
			</P>
		</TD>
	</TR>
	<TR>
		<TD>
			<b>Treatment Level</b>
		</TD>
		<TD>
			<P align="center">
			    <logic:equal name="present" value="Y">
			      <pdk-html:select name="effluentForm"  styleId="presentEffluentLevelId" property="presentEffluentLevelId" onchange="onChangePresentEffluentLevel(this.form);">
			        <logic:equal name="effluentForm" property="presentEffluentLevelId" value="0">
			           <option value="" selected="selected"></option>
			        </logic:equal>
	   	 			<pdk-html:options collection="effluentTreatmentLevels" property="effluentTreatmentLevelId" labelProperty="name"/>
				  </pdk-html:select>
				</logic:equal>
			    <logic:notEqual name="present" value="Y">
			      <pdk-html:select name="effluentForm"  styleId="presentEffluentLevelId" property="presentEffluentLevelId" disabled="true">
	     			<option value="" selected="selected"></option>
	   	 			<pdk-html:options collection="effluentTreatmentLevels" property="effluentTreatmentLevelId" labelProperty="name"/>
				  </pdk-html:select>
				</logic:notEqual>				
			</P>
		</TD>
		<TD>
			<P align="center">
   		        <logic:equal name="projected" value="Y">
			        <pdk-html:select name="effluentForm"  styleId="projectedEffluentLevelId" property="projectedEffluentLevelId" onchange="onChangeProjectedEffluentLevel(this.form);">
			          <logic:equal name="effluentForm" property="projectedEffluentLevelId" value="0">
	     			      <option value="" selected="selected"></option>
	     			  </logic:equal>
	   	 			  <pdk-html:options collection="effluentTreatmentLevels" property="effluentTreatmentLevelId" labelProperty="name"/>
				   </pdk-html:select>
				</logic:equal>
   		        <logic:notEqual name="projected" value="Y">
			        <pdk-html:select name="effluentForm"  styleId="projectedEffluentLevelId" property="projectedEffluentLevelId" disabled="true">
	     			  <option value="" selected="selected"></option>
	   	 			  <pdk-html:options collection="effluentTreatmentLevels" property="effluentTreatmentLevelId" labelProperty="name"/>
				   </pdk-html:select>
				</logic:notEqual>
				
			</P>
		</TD>
	</TR>
	
	<TR>
		<td>
			Disinfection
		</td>
		<td>
		   <p align="center">
		   <logic:equal name="isUpdatable" value="Y">
		   <pdk-html:checkbox name="effluentForm" styleId="presentDisinfection" property="presentDisinfection">		   		
		   </pdk-html:checkbox>
		   </logic:equal>
		   <logic:notEqual name="isUpdatable" value="Y">
		   <pdk-html:checkbox name="effluentForm" styleId="presentDisinfection" property="presentDisinfection" disabled="true">
		   </pdk-html:checkbox>
		   </logic:notEqual>		   
		   </p>
	     </td>
	     <td>
	     	<p align="center">
	     	<logic:equal name="isUpdatable" value="Y">
		 	<pdk-html:checkbox name="effluentForm" styleId="projectedDisinfection" property="projectedDisinfection">		   		
		 	</pdk-html:checkbox>
		 	</logic:equal>
	     	<logic:notEqual name="isUpdatable" value="Y">
		 	<pdk-html:checkbox name="effluentForm" styleId="projectedDisinfection" property="projectedDisinfection" disabled="true">
		 	</pdk-html:checkbox>
		 	</logic:notEqual>		 	
	     	</p>
	     </td>
	</TR>	
	<tr>
		<td colspan="2"><b>Advance Treatment Indicators</b></td>
	</tr>
	<% int i = 0; %>
	<logic:iterate id="advanceTreatmentType" name="advanceTreatmentTypes">
	<%	i++; 
		if (i%2!=0){
	%>
	<TR class="PortletSubHeaderColor">
	<%
		}else{
	%>
	<TR>
	<%}%>
		<TD>
			<bean:write name="advanceTreatmentType" property="name"/>
		</TD>
		<TD>
			<P align="center">			   
			   <logic:equal name="presentInd" value="Y">
			     <pdk-html:multibox name="effluentForm" styleId="presentTreatmentIndicator" property="presentAdvanceTreatmentIndicators">
					  <bean:write name="advanceTreatmentType" property="advancedTreatmentTypeId"/>
			     </pdk-html:multibox> 	
			   </logic:equal>  	
			   <logic:notEqual name="presentInd" value="Y">
			     <pdk-html:multibox name="effluentForm" styleId="presentTreatmentIndicator" property="presentAdvanceTreatmentIndicators" disabled="true">
					  <bean:write name="advanceTreatmentType" property="advancedTreatmentTypeId"/>
			     </pdk-html:multibox> 	
			   </logic:notEqual>  	   
			</P>
		</TD>
		<TD>
			<P align="center">
			   <logic:equal name="projectedInd" value="Y">
			   <pdk-html:multibox name="effluentForm" styleId="projectedTreatmentIndicator" property="projectedAdvanceTreatmentIndicators">
					  <bean:write name="advanceTreatmentType" property="advancedTreatmentTypeId"/>
			   </pdk-html:multibox> 
			   </logic:equal>
			   <logic:notEqual name="projectedInd" value="Y">
			   <pdk-html:multibox name="effluentForm" styleId="projectedTreatmentIndicator" property="projectedAdvanceTreatmentIndicators" disabled="true">
					  <bean:write name="advanceTreatmentType" property="advancedTreatmentTypeId"/>
			   </pdk-html:multibox> 
			   </logic:notEqual>
			</P>
		</TD>
	</TR>
	</logic:iterate>
	<logic:equal name="isUpdatable" value="Y">
	<TR>
	   <TD colspan="2">
			<div align="left">
			   <A href="javascript:effluentSave()">
		          <FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
			      <FONT size="1">&nbsp;&nbsp;</FONT>
			   <A href="<%=resetActionUrl %>"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0"/></FONT></A>				   
			</div>	   
	   </TD>
	</TR>
	</logic:equal>
</TABLE>
</pdk-html:form>