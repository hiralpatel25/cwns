
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.portal.provider.v2.url.UrlUtils"
	import="gov.epa.owm.mtb.cwns.facilityInformation.FacilityInformationForm"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"
	%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	FacilityInformationForm facilityInformationForm = (FacilityInformationForm)request.getAttribute("facilityinfo");
	String stateId = (String)request.getAttribute("stateId");
    String param = PortletRendererUtil.portletParameter(prr, "org.apache.struts.taglib.html.CANCEL");
%>
<SCRIPT type=text/javascript>

function FacilityReset(){
  var hidden_field = document.getElementById('paramId');
  hidden_field.name = "<%=param %>";
  hidden_field.value = "Cancel";
  window.document.facilityInformationFormBean.submit();
  return true;
}

function ConfirmAndSubmit(){
 var form = document.getElementById('facilityInformationFormBeanId');   
 if(form!=undefined){
   if(validateFacilityInformationFormBean(form)==false){
     return;
   }   
   if(validateFormBean(form) == false){
      return; 
  }
 } 
 window.document.facilityInformationFormBean.submit();
 return true;             
}



function ShowLookUpWindow(popupUrl)
{
    var w = 600;
    var h = 300;
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


var bCancel = false; 

function validateFormBean(form) {
        //check if the cwns 
        if (bCancel){ 
           return true; 
        } else {
           var oRequired = new FacilityInformation_required();
           var returnValue = validateRequired1(form, oRequired); 
           if(!returnValue)return returnValue
           //check if CWNS number states with stateId
           var cwnsnbr = document.getElementById('cwnsNbr').value;
           if(cwnsnbr == ""){
                alert("CWNS Number is required");
                return false;
           }
           if(cwnsnbr.length != 11){
                alert("CWNS Number should be 11 digits long");
                return false;
           }
           if('<%=stateId%>' != cwnsnbr.substring(0,2)){
                alert("CWNS number should start with " + '<%=stateId%>');
                return false;
           }
        }   
        return true;
} 

function FacilityInformation_required () { 
    this.aa = new Array("facilityName", "Facility Name is required.", new Function ("varName", " return this[varName];"));
} 

    function validateRequired1(form, oRequired) {
                var isValid = true;
                var focusField = null;
                var i = 0;
                var fields = new Array();
                //oRequired = new required();
                for (x in oRequired) {
                	var field = form[oRequired[x][0]];
                	
                    if (field.type == 'text' ||
                        field.type == 'textarea' ||
                        field.type == 'file' ||
                        field.type == 'select-one' ||
                        field.type == 'radio' ||
                        field.type == 'password') {
                        
                        var value = '';
						// get field's value
						if (field.type == "select-one") {
							var si = field.selectedIndex;
							if (si >= 0) {
								value = field.options[si].value;
							}
						} else {
							value = field.value;
						}
                        
                        if (trim(value).length == 0) {
                        
	                        if (i == 0) {
	                            focusField = field;
	                        }
	                        fields[i++] = oRequired[x][1];
	                        isValid = false;
                        }
                    }
                }
                if (fields.length > 0) {
                   focusField.focus();
                   alert(fields.join('\n'));
                }
                return isValid;
            }

</SCRIPT>

<pdk-html:form name="facilityInformationFormBean" 
               type="gov.epa.owm.mtb.cwns.facilityInformation.FacilityInformationForm" 
               action="saveFacilityInfo.do" styleId="facilityInformationFormBeanId" >
           
<DIV id="hidden_fields" style="DISPLAY:none">
    <input type="hidden" id="paramId" name="" value="">
    <pdk-html:text styleId="surveyFacilityId" property="surveyFacilityId" value="<%=facilityInformationForm.getSurveyFacilityId() %>"/>
	<pdk-html:text styleId="showWarningMessage" property="showWarningMessage" value="<%=facilityInformationForm.getShowWarningMessage() %>"/>
	<pdk-html:text styleId="isUpdatable" property="isUpdatable" value="<%=facilityInformationForm.getIsUpdatable() %>"/>
	<pdk-html:text styleId="locationId" property="locationId" value="<%=facilityInformationForm.getLocationId() %>"/>
	<pdk-html:text styleId="showPrivate" property="showPrivate" value="<%=facilityInformationForm.getShowPrivate() %>"/>
	<pdk-html:text styleId="isFacility" property="isFacility" value="<%=facilityInformationForm.getIsFacility() %>"/>
</DIV>	                     
<!--  
<div style="float: left" width="300">
	&nbsp;&nbsp;
	<STRONG><FONT size="4">Facility Information</FONT>
	</STRONG>
	<FONT size="1"></FONT>
</div>
-->
<logic:equal name="facilityInformationFormBean" property="surveyFacilityId" value="0">
	<div class="PortletText1">
		<FONT size="4"><STRONG>New Facility/Project</STRONG></FONT>
	</div>
</logic:equal>

<div class="error"><html:errors/></div>
               
<table cellspacing="2" cellpadding="3" border="0" width="100%"
	class="PortletText1">
	<TR>
	    <TD width="1%">
	       <STRONG> <FONT color="#ff0000">* </FONT></STRONG>
	    </TD>  
		<TD width="15%">
			<STRONG> <%="Y".equalsIgnoreCase(facilityInformationForm.getIsFacility())?"Name":"Name" %>: </STRONG>
		</TD>
		<logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<TD width="45%">
			<pdk-html:text name="facilityInformationFormBean" styleId="facilityName" property="facilityName" size="40" maxlength="35">
			    <bean:write name="facilityInformationFormBean" property="facilityName"/>
			</pdk-html:text>&nbsp;
			<logic:equal name="facilityInformationFormBean" property="isNpdesIconVisible" value="Y">
			  <pdk-html:img page="/images/npdesLogosmall.jpg" alt="<%=facilityInformationForm.getNpdesFacilityName() %>" border="0"/> 
			</logic:equal>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<!-- View mode -->
		<TD width="45%">
			<bean:write name="facilityInformationFormBean" property="facilityName"/>
		</TD>
		</logic:notEqual>		
		
	</TR>
		
	<TR>
	      
		<TD width="1%">
	       &nbsp;
	    </TD>
		<TD>
			<STRONG> Description: </STRONG>
		</TD>
		<logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<TD>
		    <pdk-html:textarea name="facilityInformationFormBean" styleId="description" property="description" cols="50" rows="3">
		        <bean:write name="facilityInformationFormBean" property="description"/>
		    </pdk-html:textarea>
		</TD>
		</logic:equal>
		<!-- View mode -->
		<logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<TD>
			<bean:write name="facilityInformationFormBean" property="description"/>
		</TD>
		</logic:notEqual>
		
	</TR>
	<TR>
	      
	    <TD width="1%">
	        <STRONG> <FONT color="#ff0000"> * </FONT></STRONG>
	    </TD>
		<TD>
			<STRONG> CWNS Number: </STRONG>
		</TD>
		<logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
		  <TD>
		    <logic:equal name="facilityInformationFormBean" property="surveyFacilityId" value="0">
		      <pdk-html:text styleId="cwnsNbr" name="facilityInformationFormBean" property="cwnsNbr" size="25" maxlength="11"/>
		    </logic:equal>
		    <logic:notEqual name="facilityInformationFormBean" property="surveyFacilityId" value="0">
		      <pdk-html:text styleId="cwnsNbr" name="facilityInformationFormBean" property="cwnsNbr" size="25" maxlength="11" disabled="true"/>
		    </logic:notEqual>  
		  </TD>
		</logic:equal>
		<logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
		   <TD>
		     <bean:write name="facilityInformationFormBean" property="cwnsNbr" />
		   </TD>
		</logic:notEqual>
		
	</TR>
	<TR>
	     
	    <TD width="1%">
	        &nbsp;
	    </TD> 
	    
		<TD>
			<STRONG> System Name: </STRONG>
		</TD>
		<logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<TD>
		    <pdk-html:text styleId="systemName" name="facilityInformationFormBean" property="systemName" size="30" maxlength="30">
		       <bean:write name="facilityInformationFormBean" property="systemName"/>
		    </pdk-html:text>   
		    <%
				NameValue[] linkParams       = new NameValue[1];
				linkParams[0] = new NameValue("locationId", facilityInformationForm.getLocationId());
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"SystemNameListPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl %>")'">
				   <pdk-html:img page="/images/find.gif" border="0" alt="Search" />  
				</A>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
		<!-- view mode -->
		<TD>
		   <bean:write name="facilityInformationFormBean" property="systemName"/>
		</TD>
		</logic:notEqual>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
	<TR>
	   <logic:equal name="facilityInformationFormBean" property="isLocalUser" value="Y">
	   <TD>&nbsp;</TD>
	    <TD>&nbsp;</TD>
	     <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD>
	            <pdk-html:checkbox name="facilityInformationFormBean" property="militaryFlag" 
	                 value="Y" />
	             <STRONG>Military</STRONG>
	        </TD>
	    </logic:equal>
	    <logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD width="45%">
	            <pdk-html:checkbox name="facilityInformationFormBean" property="militaryFlag" 
	                 value="Y" disabled="true"/>
		        <STRONG>Military</STRONG>
	        </TD>
	    </logic:notEqual> 
	       
	   </logic:equal>
	   
	   <logic:notEqual name="facilityInformationFormBean" property="isLocalUser" value="Y">
	    <TD width="1%">
	       <strong> <FONT color="#ff0000"> * </FONT> </strong>
	    </TD>
	    <TD> 
	       <strong> Owner:</strong>
	    </TD>
	    <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD>
	            <pdk-html:select name="facilityInformationFormBean" styleId="ownerId" property="ownerCode">
	             <logic:equal name="facilityInformationFormBean" property="showPrivate" value="Y">
	            	 <pdk-html:option value="PRI"> Private </pdk-html:option>
	             </logic:equal>	 
	            	 <pdk-html:option value="PUB"> Public </pdk-html:option>
	            	 <pdk-html:option value="FED"> Federal </pdk-html:option>
	            </pdk-html:select>
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            <pdk-html:checkbox name="facilityInformationFormBean" property="militaryFlag" 
	                 value="Y" />
	                 
		        <STRONG>Military</STRONG>
	        </TD>
	    </logic:equal>
	    <logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD width="45%">
	            <bean:write name="facilityInformationFormBean" property="ownerCodeValue"/>
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            <pdk-html:checkbox name="facilityInformationFormBean" property="militaryFlag" 
	                 value="Y" disabled="true"/>
		        <STRONG>Military</STRONG>
	        </TD>
	    </logic:notEqual>    
	    </logic:notEqual>	
			
	</TR>
	<logic:notEqual name="facilityInformationFormBean" property="isLocalUser" value="Y">
	<TR>
	   
	    <TD width="1%">
	       <strong> <FONT color="#ff0000"> * </FONT> </strong>
	    </TD>
	    <TD> 
	       <strong> TMDL:</strong>
	    </TD>
	    <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD>
	            <pdk-html:select name="facilityInformationFormBean" styleId="tmdlFlgId" property="tmdlFlg">
	             	 <pdk-html:option value="Y"> Yes </pdk-html:option>
	            	 <pdk-html:option value="N"> No </pdk-html:option>
	            </pdk-html:select>
	        </TD>
	    </logic:equal>
	    <logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
	    <TD>
	      <%="Y".equalsIgnoreCase(facilityInformationForm.getTmdlFlg())?"Yes":"No" %>
	    </TD>   
	    </logic:notEqual>
	    <TD></TD>
	    <TD></TD>
	    <TD></TD>
	    	
	</TR>
	<TR>
	   
	    <TD width="1%">
	       <strong> <FONT color="#ff0000"> * </FONT> </strong>
	    </TD>
	    <TD> 
	       <strong> Source Water Protection:</strong>
	    </TD>
	    <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
	        <TD>
	            <pdk-html:select name="facilityInformationFormBean" styleId="sourceWaterProtectionFlgId" property="sourceWaterProtectionFlg">
	             	 <pdk-html:option value="Y"> Yes </pdk-html:option>
	            	 <pdk-html:option value="N"> No </pdk-html:option>
	            </pdk-html:select>
	        </TD>
	    </logic:equal>
	    <logic:notEqual name="facilityInformationFormBean" property="isUpdatable" value="Y">
	    <TD>
	       <%="Y".equalsIgnoreCase(facilityInformationForm.getSourceWaterProtectionFlg())?"Yes":"No" %>
	    </TD>   
	    </logic:notEqual>
	    <TD></TD>
	    <TD></TD>
	    <TD></TD>
	    	
	</TR> 
	</logic:notEqual>       
	<TR>
	     <TD>
	         
	     </TD>
	</TR> 
	<TR>
		<TD colspan="7">
		   <div align="left" width="150">
		     <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
   				<A href="javascript:ConfirmAndSubmit()"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
   				<!--<pdk-html:submit value="Save"/>-->
   				<FONT size="1">&nbsp;&nbsp;</FONT>
   				<logic:equal name="facilityInformationFormBean" property="surveyFacilityId" value="0">
      				<A href="<%=CWNSEventUtils.constructEventLink(prr,"home", null,true,true)%>"><pdk-html:img page="/images/cancel.gif" alt="Reset" border="0" /></A>
   				</logic:equal>
   				<logic:notEqual name="facilityInformationFormBean" property="surveyFacilityId" value="0">
      				<A href="javascript:FacilityReset();"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0" /></FONT></A>
   				</logic:notEqual>
  			</logic:equal>
		   </div>
		</TD>
	</TR>
		
</TABLE>
<%--  
<div style="float: left;" width="150">
  <logic:equal name="facilityInformationFormBean" property="isUpdatable" value="Y">
   <A href="javascript: ConfirmAndSubmit()"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
   <FONT size="1">&nbsp;&nbsp;</FONT>
   <A href="javascript:Cancel();"><FONT size="1"><pdk-html:img page="/images/cancel.gif" alt="Cancel" border="0"/></FONT></A>
  </logic:equal> 
</div>
--%>
</pdk-html:form>
 <logic:notEqual name="facilityInformationFormBean" property="surveyFacilityId" value="0">
   <pdk-html:javascript formName="facilityInformationFormBean" staticJavascript="false"/>
 </logic:notEqual>
 <logic:equal name="facilityInformationFormBean" property="surveyFacilityId" value="0">  
    <pdk-html:javascript formName="facilityInformationFormBean" staticJavascript="true"/> 
 </logic:equal>   



