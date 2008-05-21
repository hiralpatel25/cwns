<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="gov.epa.owm.mtb.cwns.model.FacilityPermit"
	import="gov.epa.owm.mtb.cwns.permits.FacilityPermitForm"
	import="gov.epa.owm.mtb.cwns.service.FacilityPermitService"
	%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>	
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	NameValue[] linkParams       = new NameValue[2];		
	String addActionUrl = CWNSStrutsUtils.createHref(request,"addFacilityPermit.do");		
	String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteFacilityPermit.do");
	String editActionUrl = CWNSStrutsUtils.createHref(request,"editFacilityPermit.do");
    String facilityPermitParam1 = PortletRendererUtil.portletParameter(prr, "permitId");
    FacilityPermitForm facilityPermitForm = (FacilityPermitForm)request.getAttribute("facilityPermitForm");
    String param = PortletRendererUtil.portletParameter(prr, "org.apache.struts.taglib.html.CANCEL");
    String id = "";
%>

<SCRIPT type=text/javascript>
function ShowPermitDetails(url){
     var w = 800;
     var h = 500;
	 var winl = (screen.width-w)/2;
	 var wint = (screen.height-h)/2;
	 var settings ='height='+h+',';
	 settings +='width='+w+',';
	 settings +='top='+wint+',';
	 settings +='left='+winl+',';
	 settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

     var w = window.open(url, null, settings);
     if(w != null) 
        w.focus();
   }

function markDelete(url){
   
   window.open(url,'_self',null);
}

function FacilityPermitCancel(){
  var hidden_field = document.getElementById('cancelParamId');
  hidden_field.name = "<%=param %>";
  hidden_field.value = "Cancel";
  window.document.facilityPermitForm.submit();
  return true;
}

function ConfirmAndSave(){
var permitNbr = '';
var permitType = '';
if (window.document.getElementById('permitNumber')!=undefined){
  permitNbr = window.document.getElementById('permitNumber').value;
  if (trim(permitNbr).length < 1){
    alert("Permit Number is required");
    window.document.getElementById("permitNumber").focus();
	return false;
  }		
}  
if (window.document.getElementById('permitTypeId')!=undefined){
  element = window.document.getElementById('permitTypeId');
  for (var i=0; i < element.options.length; i++){
         if (element.options[i].selected == true) {
           permitType = element.options[i].value;
         }
     }
     if (permitType.length < 1)
     alert("Permit Type is required");
     window.document.getElementById("permitTypeId").focus();
	return false;
}  
var c = '<%=facilityPermitForm.getConfirm()%>'
var useData = window.document.getElementById('useDataCheckBox');

if (useData != undefined && useData.checked){

  if (c == 'Y'){
  var result = confirm("This Permit is already Used By another facility.\n"+ 
                         "Do you want to use this Permit’s Facility Coordinates and Address for this Facility?");
                          
  if (result == true){
     window.document.getElementById('autoPopulateId').value='Y'; 
     window.document.facilityPermitForm.submit();
     return true;                     
  }
  else{
     window.document.facilityPermitForm.submit();
     return true;
  }
  }
  window.document.getElementById('autoPopulateId').value='Y'; 
}
window.document.facilityPermitForm.submit();
 return true;
}

function FacilityPermitSave(){
var permitNbr = '';
var permitType = '';
if (window.document.getElementById('permitNumber')!=undefined){
  permitNbr = window.document.getElementById('permitNumber').value;
  if (trim(permitNbr).length < 1){
    alert("Permit Number is required");
    window.document.getElementById("permitNumber").focus();
	return false;
  }		
}  
if (window.document.getElementById('permitTypeId')!=undefined){
  element = window.document.getElementById('permitTypeId');
  for (var i=0; i < element.options.length; i++){
         if (element.options[i].selected == true) {
           permitType = element.options[i].value;
         }
     }
     if (permitType.length < 1){
       alert("Permit Type is required");
       window.document.getElementById("permitTypeId").focus();
	   return false;
	 }
}  
window.document.facilityPermitForm.submit();
return true;
}
</SCRIPT>

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
	<TR>
		<TD colspan="6">
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">

				<TR>
			      <TD colspan="6">
				    
			      </TD>
		        </TR>
				<TR class="PortletHeaderColor">
		<TD class="PortletHeaderText" align="center">
			NPDES
		</TD>
		<TD class="PortletHeaderText" align="center">
			Permit Number
		</TD>
		<TD class="PortletHeaderText" align="center">
			Type
		</TD>
		<TD class="PortletHeaderText" align="center">
			Use Data
		</TD>
		<logic:equal name="facilityPermitForm" property="isUpdatable" value="Y">
           <TD align="center" width="40">
				<FONT color="#ffffff"> <STRONG> Edit </STRONG> </FONT>
		   </TD>
		   <TD align="center" width="40">
				<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
		   </TD>   
       </logic:equal>
       <logic:notEqual name="facilityPermitForm" property="isUpdatable" value="Y">
       <logic:equal name="isNonLocalUser" value="false">
           <TD align="center" width="40">
				<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
		   </TD>
	   </logic:equal>   
       </logic:notEqual>
	</TR>
	
	
	<c:set var="i" value="1" />
				<logic:iterate id="permit" name="facilityPermitForm" property="permits" 
				                                 type="FacilityPermit">
                <c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>	
				<c:set var="i" value="${i+1}" />
				<logic:equal name="permit" property="id.permitId" value="<%=facilityPermitForm.getPermitId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		        </logic:equal>
				<TR class="<c:out value="${class}"/>">
					<TD align="center">
					    <logic:equal name="permit" property="permit.permitTypeRef.npdesFlag" value="Y">
						   <pdk-html:img page="/images/checkmark.gif" />
						</logic:equal>
					</TD>
					<TD align="center">
					  <logic:equal name="permit" property="permit.permitTypeRef.npdesFlag" value="Y">
						 <%
 							linkParams[0] = new NameValue("permitNbr", permit.getPermit().getPermitNumber());
 							//long tmpFacId = -1;
		                    //tmpFacId = facilityPermitForm.getFacilityID();
 							linkParams[1] = new NameValue("facilityId", facilityPermitForm.getFacilityId().toString());
 							System.out.println("facility id---"+facilityPermitForm.getFacilityId().toString());
 							String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewPermitDetailsPage", linkParams,true, true);
 						 %>
						 <A href="javascript:void(0);" onclick='ShowPermitDetails("<%=eventPopupWinowUrl%>")' title="Envirofacts">
						   <bean:write name="permit" property="permit.permitNumber"/>
					     </A>
					  </logic:equal>
					  <logic:notEqual name="permit" property="permit.permitTypeRef.npdesFlag" value="Y">
					     <bean:write name="permit" property="permit.permitNumber"/>
					  </logic:notEqual>
					  	
					</TD>
					<TD align="center">
						<bean:write name="permit" property="permit.permitTypeRef.name"/>
					</TD>
					<TD align="center">
					    <logic:equal name="permit" property="usedForFacilityLocatnFlag" value="Y">
						   <pdk-html:img page="/images/checkmark.gif" />
						</logic:equal>
					</TD>
					<bean:define id="permitId"><bean:write name="permit" property="id.permitId"/></bean:define> 
					<logic:equal name="facilityPermitForm" property="isUpdatable" value="Y">
					
					<TD align="center">
					   <A href="<%=editActionUrl%>&<%=facilityPermitParam1%>=<%=permitId%>"><pdk-html:img page="/images/edit.gif" alt="edit" border="0"/></A>
					</TD>
					<logic:equal name="isNonLocalUser" value="true">
					<TD align="center">
					    <A href="<%=deleteActionUrl%>&<%=facilityPermitParam1%>=<%=permitId%>" ><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A> 
					</TD>
					</logic:equal>
					
					<logic:equal name="isNonLocalUser" value="false">
					<TD align="center">
					<bean:define id="url">
					  javascript:markDelete('<%=deleteActionUrl%>&<%=facilityPermitParam1%>=<%=permitId%>')
					</bean:define>
					<pdk-html:checkbox name="permit" property="feedbackDeleteFlag" value="Y" 
		                  onclick='<%=url%>'>
			        </pdk-html:checkbox>
													    
					</TD>
					</logic:equal>
					</logic:equal>
					<logic:notEqual name="facilityPermitForm" property="isUpdatable" value="Y">
					<logic:equal name="isNonLocalUser" value="false">
					<TD align="center">
					
					<pdk-html:checkbox name="permit" property="feedbackDeleteFlag" value="Y" 
		                  disabled="true">
			        </pdk-html:checkbox>
													    
					</TD>
					</logic:equal>
					</logic:notEqual>
				</TR>
				</logic:iterate>
		
		<TR><TD colspan="6">&nbsp;</TD></TR>

  </TABLE>
  </TD>
  </TR>
  </TABLE>
  <logic:equal name="facilityPermitForm" property="isUpdatable" value="Y">
 <logic:equal name="facilityPermitForm" property="mode" value="list">
  <DIV class="PortletText1" align="left">
    <A href="<%=addActionUrl%>">Add Permit</A>
  </DIV> 
 </logic:equal>
 
 <logic:present name="facilityPermitForm" property="mode">
<logic:notEqual name="facilityPermitForm" property="mode" value="list">

<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="facilityPermitForm" styleId="facilityPermitFormId"
	           type="gov.epa.owm.mtb.cwns.permits.FacilityPermitForm"
	           action="saveFacilityPermit.do">
		           
	<STRONG><FONT size="2">Add/Edit Permit</FONT></STRONG><br><br>
	<html:errors/><br>
	
	<logic:equal name="facilityPermitForm" property="mode" value="add">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="facilityPermitForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="facilityPermitForm"  property="facilityId" styleId="facilityId"/>
	 <input type="hidden" id="cancelParamId" name="" value="">
	  
	  </div>  
	<TABLE border="0" width="400" class="PortletText1">
	  <TR>
	    <TD align="left" colspan="2">
	      <pdk-html:checkbox name="facilityPermitForm" property="type_NPDES" value="Y" styleId="type_NPDES" disabled="true">NPDES</pdk-html:checkbox>
	    </TD>
	    
	  </TR>
	  <TR>
	    <TD>
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;Permit Number:</STRONG>
	    </TD>
	    <TD>
	       <pdk-html:text name="facilityPermitForm" property="permitNumber" styleId="permitNumber" size="9" maxlength="9" />
	    </TD>
	  </TR>
	  <TR>
	     <TD>
	        <STRONG><span class="required">*</span>&nbsp;&nbsp;Permit Type:</STRONG>
	     </TD>
	     <TD>
	        <pdk-html:select name="facilityPermitForm"  property="permitTypeId" styleId="permitTypeId" size="1">
	             <OPTION value="" selected="selected"></OPTION>
	   	         <logic:iterate id="permitType" name="facilityPermitForm" property="permitTypes" type="Entity">
				 	 <OPTION value="<%=permitType.getKey()%>"><%=permitType.getValue()%></OPTION>
				 </logic:iterate>
	        </pdk-html:select>
	     </TD>
	  </TR>
	  
	</TABLE><br>
	
	</logic:equal>
	
	<logic:equal name="facilityPermitForm" property="mode" value="edit">
	  <div style="DISPLAY: none;"> 
	  <input type="hidden" id="cancelParamId" name="" value="">
	  <pdk-html:text name="facilityPermitForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="facilityPermitForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="facilityPermitForm"  property="permitId" styleId="permitId"/>
	  <pdk-html:hidden name="facilityPermitForm" property="autoPopulate" styleId="autoPopulateId" value="<%=facilityPermitForm.getAutoPopulate() %>"/>
	  <pdk-html:hidden name="facilityPermitForm" property="currentPermitNbr" value="<%=facilityPermitForm.getCurrentPermitNbr() %>"/>
	  <pdk-html:text name="facilityPermitForm"  property="type_NPDES"/>
	 </div>  
	<TABLE border="0" width="400" class="PortletText1">
	  <TR>
	    <TD align="left" colspan="2">
	      <pdk-html:checkbox name="facilityPermitForm" property="type_NPDES" value="Y" styleId="type_NPDES" disabled="true">NPDES</pdk-html:checkbox>
	    </TD>
	    
	  </TR>
	  <TR>
	    <TD width="10%" >
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;Permit Number:</STRONG>
	    </TD>
	    <TD width="20%">
	      <logic:equal name="facilityPermitForm"  property="type_NPDES" value="Y">
	      <bean:write name="facilityPermitForm" property="permitNumber"/>
	      </logic:equal>
		   <logic:notEqual name="facilityPermitForm"  property="type_NPDES" value="Y">
		   <pdk-html:text name="facilityPermitForm" property="permitNumber" styleId="permitNumber" size="9" maxlength="9" />
		   </logic:notEqual> 
	    </TD>
	  </TR>
	  <TR>
	     <TD>
	        <STRONG><span class="required">*</span>&nbsp;&nbsp;Permit Type:</STRONG>
	     </TD>
	     <TD>
	        
	        <logic:equal name="facilityPermitForm"  property="type_NPDES" value="Y">
	          <bean:write name="facilityPermitForm"  property="permitType"/>
	        </logic:equal>
	        <logic:notEqual name="facilityPermitForm"  property="type_NPDES" value="Y">
	        <pdk-html:select name="facilityPermitForm"  property="permitTypeId" styleId="permitTypeId" size="1">
	             <%-- <option value="" selected="selected"></option> --%>
	   	         <logic:iterate id="permitType" name="facilityPermitForm" property="permitTypes" type="Entity">
				 <logic:match name="facilityPermitForm" property="permitTypeId" value='<%=permitType.getKey()%>'>
				 <OPTION value="<%=permitType.getKey()%>" selected="selected"><%=permitType.getValue()%></OPTION>
				 </logic:match>
				 <logic:notMatch name="facilityPermitForm" property="permitTypeId" value='<%=permitType.getKey()%>'>
				 <OPTION value="<%=permitType.getKey()%>"><%=permitType.getValue()%></OPTION>
				 </logic:notMatch> 		            	
                 </logic:iterate>
	        </pdk-html:select>
	        </logic:notEqual>
	     </TD>
	  </TR>
	  <logic:equal name="facilityPermitForm"  property="type_NPDES" value="Y">
	  <TR>
	     <TD colspan="2">
	      <pdk-html:checkbox name="facilityPermitForm" property="useData" value="Y" styleId="useDataCheckBox" /><STRONG>Use Data</STRONG>
	      
	     </TD>
	  </TR>
	  </logic:equal>
	</TABLE><br>
	</logic:equal>
	<logic:equal name="facilityPermitForm"  property="type_NPDES" value="Y">
	     <INPUT type="button" name="Save" value="Save" onclick ="javascript:ConfirmAndSave()">
	</logic:equal>
	<logic:notEqual name="facilityPermitForm"  property="type_NPDES" value="Y">
	     <INPUT type="button" name="Save" value="Save" onclick ="javascript:FacilityPermitSave()">
	</logic:notEqual>     
		<INPUT type="button" name="cancel" value="Cancel" onclick="javascript:FacilityPermitCancel()"> 
	</pdk-html:form>
	
</DIV>
</logic:notEqual>
</logic:present>
</logic:equal>

