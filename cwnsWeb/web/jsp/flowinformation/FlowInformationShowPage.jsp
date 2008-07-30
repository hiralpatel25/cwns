<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="java.text.DecimalFormat"%>
<%@page
	import="gov.epa.owm.mtb.cwns.flowInformation.FlowInformationForm"%>

	

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			String param = PortletRendererUtil.portletParameter(prr, "org.apache.struts.taglib.html.CANCEL");
			FlowInformationForm flowInfoForm = (FlowInformationForm)request.getAttribute("flowInfo");
			DecimalFormat df = new DecimalFormat("#0.0");
			
%>
<script type="text/javascript">
   function changeState(){
        var totalFlowState;
        var preUpdatable = "<%=flowInfoForm.getIsPreFlowUpdatable()%>"
        var proUpdatable = "<%=flowInfoForm.getIsProFlowUpdatable()%>"
        totalFlowState = document.getElementById("totalFlowStateId").value;
        if (totalFlowState == "disable"){
           
           document.getElementById("existMunicipalFlow").value = "0";
           document.getElementById("preMunicipalFlow").value = "0";
           document.getElementById("proMunicipalFlow").value = "0";
           document.getElementById("existIndustrialFlow").value = "0";
           document.getElementById("preIndustrialFlow").value = "0";
           document.getElementById("proIndustrialFlow").value = "0";
           document.getElementById("existInfiltrationFlow").value = "0";
           document.getElementById("preInfiltrationFlow").value = "0";
           document.getElementById("proInfiltrationFlow").value = "0";
           if (preUpdatable == "Y") {          
             document.getElementById("existMunicipalFlow").disabled = "";
             document.getElementById("existMunicipalFlow").className = '';
             document.getElementById("preMunicipalFlow").disabled = "";
             document.getElementById("preMunicipalFlow").className = '';
             document.getElementById("existIndustrialFlow").disabled = "";
             document.getElementById("existIndustrialFlow").className = '';
             document.getElementById("preIndustrialFlow").disabled = "";
             document.getElementById("preIndustrialFlow").className = '';
             document.getElementById("existInfiltrationFlow").disabled = "";
             document.getElementById("existInfiltrationFlow").className = '';
             document.getElementById("preInfiltrationFlow").disabled = "";
             document.getElementById("preInfiltrationFlow").className = '';
           }
           if (proUpdatable == "Y"){
             document.getElementById("proMunicipalFlow").disabled = "";
             document.getElementById("proMunicipalFlow").className = '';
             document.getElementById("proIndustrialFlow").disabled = "";
             document.getElementById("proIndustrialFlow").className = '';
             document.getElementById("proInfiltrationFlow").disabled = "";
             document.getElementById("proInfiltrationFlow").className = '';
           }
           
           document.getElementById("totalFlowStateId").value = "enable";
           //document.getElementById("existTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %>";
           document.getElementById("existTotalFlow").disabled = "true";
           //document.getElementById("preTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow()) %>";
           document.getElementById("existTotalFlow").className = 'disabledField';
           document.getElementById("preTotalFlow").disabled = "true";
           document.getElementById("preTotalFlow").className = 'disabledField';
           //document.getElementById("proTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow()) %>";
           document.getElementById("proTotalFlow").disabled = "true";
           document.getElementById("proTotalFlow").className = 'disabledField';
        }
        else 
            if (totalFlowState == "enable"){
               document.getElementById("existMunicipalFlow").value = "";
               document.getElementById("existMunicipalFlow").disabled = "true";
               document.getElementById("existMunicipalFlow").className = 'disabledField';
               document.getElementById("preMunicipalFlow").value = "";
               document.getElementById("preMunicipalFlow").disabled = "true";
               document.getElementById("preMunicipalFlow").className = 'disabledField';
               document.getElementById("proMunicipalFlow").value = "";
               document.getElementById("proMunicipalFlow").disabled = "true";
               document.getElementById("proMunicipalFlow").className = 'disabledField';
               document.getElementById("existIndustrialFlow").value = "";
               document.getElementById("existIndustrialFlow").disabled = "true";
               document.getElementById("existIndustrialFlow").className = 'disabledField';
               document.getElementById("preIndustrialFlow").value = "";
               document.getElementById("preIndustrialFlow").disabled = "true";
               document.getElementById("preIndustrialFlow").className = 'disabledField';
               document.getElementById("proIndustrialFlow").value = "";
               document.getElementById("proIndustrialFlow").disabled = "true";
               document.getElementById("proIndustrialFlow").className = 'disabledField';
               document.getElementById("existInfiltrationFlow").value = "";
               document.getElementById("existInfiltrationFlow").disabled = "true";
               document.getElementById("existInfiltrationFlow").className = 'disabledField';
               document.getElementById("preInfiltrationFlow").value = "";
               document.getElementById("preInfiltrationFlow").disabled = "true";
               document.getElementById("preInfiltrationFlow").className = 'disabledField';
               document.getElementById("proInfiltrationFlow").value = "";
               document.getElementById("proInfiltrationFlow").disabled = "true";
               document.getElementById("proInfiltrationFlow").className = 'disabledField';
           
               document.getElementById("totalFlowStateId").value = "disable";
               //document.getElementById("existTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %>";
               //document.getElementById("preTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow()) %>";
               //document.getElementById("proTotalFlow").value = "<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow()) %>";
               
               if (preUpdatable == "Y"){
                 document.getElementById("existTotalFlow").disabled = "";
                 document.getElementById("existTotalFlow").className = '';
                 document.getElementById("preTotalFlow").disabled = "";
                 document.getElementById("preTotalFlow").className = '';
               }
               if (proUpdatable == "Y"){
                 document.getElementById("proTotalFlow").disabled = "";
                 document.getElementById("proTotalFlow").className = '';
               }
               
            } 
   }
   
   function convStrToFloat(str){
     if(str == null || str == "")
       return 0;
     else
       return parseFloat(str);
   }
   
   function Calculatetotal(elemId){
     var elemIdField = document.getElementById(elemId);
     var tempArray = elemIdField.value.split('.');
     var joinedString= tempArray.join('');
     if(!isAllDigits(joinedString))
      {
  	   alert("This field accepts only numeric values.");
  	   elemIdField.focus();
  	   return;
      }
     else if(elemIdField.value < 0)
            {
  	         alert("This field accepts only positive integers.");
  	         elemIdField.focus();
  	         return false;
            }
     if(elemId == "existMunicipalFlow" ||  elemId == "existIndustrialFlow" || elemId == "existInfiltrationFlow"){
        var existMunicipalFlowText = document.getElementById("existMunicipalFlow");
        var existIndustrialFlowText = document.getElementById("existIndustrialFlow");
        var existInfiltrationFlowText = document.getElementById("existInfiltrationFlow");
        var existTotalFlowText = document.getElementById("existTotalFlow");
        var existTotalFlowValue = convStrToFloat(existMunicipalFlowText.value) +
              convStrToFloat(existIndustrialFlowText.value) +
              convStrToFloat(existInfiltrationFlowText.value);
        if (isNaN(existTotalFlowValue)){
            existTotalFlowText.value = "";
        }
        else     
          existTotalFlowText.value = Math.round(existTotalFlowValue*Math.pow(10,3))/Math.pow(10,3);
          
        if(<%=flowInfoForm.getPresent()%> > 0){
            var flowToPopulationExistRatioText = document.getElementById("flowToPopulationExistRatio");
			var existHiRatio = (existTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var existLowRatio = (existTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			if (isNaN(existTotalFlowValue)){
			    flowToPopulationExistRatioText.innerHTML = "";
			}
			else {
			    var ratio = existTotalFlowValue*1000000/<%=flowInfoForm.getPresent()%>;    
			    if (<%=flowInfoForm.getPresent()%> < existLowRatio || <%=flowInfoForm.getPresent()%> > existHiRatio){
			       flowToPopulationExistRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I></FONT>"
				}else{ 
				      flowToPopulationExistRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I>"
				 } 
			}	 
		}      
     }
     else if(elemId == "preMunicipalFlow" || elemId == "preIndustrialFlow" || elemId == "preInfiltrationFlow"){
            var preMunicipalFlowText = document.getElementById("preMunicipalFlow");
            var preIndustrialFlowText = document.getElementById("preIndustrialFlow");
            var preInfiltrationFlowText = document.getElementById("preInfiltrationFlow");
            var preTotalFlowText = document.getElementById("preTotalFlow");
            var preTotalFlowValue = convStrToFloat(preMunicipalFlowText.value) +
                convStrToFloat(preIndustrialFlowText.value) +
                convStrToFloat(preInfiltrationFlowText.value);
            if (isNaN(preTotalFlowValue)){
               preTotalFlowText.value = "";
            }
            else    
               preTotalFlowText.value = Math.round(preTotalFlowValue*Math.pow(10,3))/Math.pow(10,3);
                
          if(<%=flowInfoForm.getPresent()%> > 0){
            var flowToPopulationPreRatioText = document.getElementById("flowToPopulationPreRatio");
			var preHiRatio = (preTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var preLowRatio = (preTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			if (isNaN(preTotalFlowValue)){
			    flowToPopulationPreRatioText.innerHTML = "";
			}
			else {
			    var ratio = preTotalFlowValue*1000000/<%=flowInfoForm.getPresent()%>;    
			    if (<%=flowInfoForm.getPresent()%> < preLowRatio || <%=flowInfoForm.getPresent()%> > preHiRatio){
			       flowToPopulationPreRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I></FONT>"
				}else{ 
				      flowToPopulationPreRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I>"
				 }
			}	  
		}            
     }
     else if(elemId == "proMunicipalFlow" || elemId == "proIndustrialFlow" || elemId == "proInfiltrationFlow"){
            var proMunicipalFlowText = document.getElementById("proMunicipalFlow");
            var proIndustrialFlowText = document.getElementById("proIndustrialFlow");
            var proInfiltrationFlowText = document.getElementById("proInfiltrationFlow");
            var proTotalFlowText = document.getElementById("proTotalFlow");
            var proTotalFlowValue = convStrToFloat(proMunicipalFlowText.value) +
                convStrToFloat(proIndustrialFlowText.value) +
                convStrToFloat(proInfiltrationFlowText.value);
            if (isNaN(proTotalFlowValue)){
               proTotalFlowText.value = "";
            }
            else      
               proTotalFlowText.value = Math.round(proTotalFlowValue*Math.pow(10,3))/Math.pow(10,3);
                
            if(<%=flowInfoForm.getProjected()%> > 0){
            var flowToPopulationProRatioText = document.getElementById("flowToPopulationProRatio");
			var proHiRatio = (proTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var proLowRatio = (proTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			if (isNaN(proTotalFlowValue)){
			    flowToPopulationProRatioText.innerHTML = "";
			}
			else {
			    var ratio = proTotalFlowValue*1000000/<%=flowInfoForm.getProjected()%>;    
			    if (<%=flowInfoForm.getProjected()%> < proLowRatio || <%=flowInfoForm.getProjected()%> > proHiRatio){
			       flowToPopulationProRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I></FONT>"
				}else{ 
				      flowToPopulationProRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I>"
				 }
			}	  
		}                
     }
     else if(elemId == "existTotalFlow"){
            var existTotalFlowValue = document.getElementById("existTotalFlow").value;
            if(<%=flowInfoForm.getPresent()%> > 0){
            var flowToPopulationExistRatioText = document.getElementById("flowToPopulationExistRatio");
			var existHiRatio = (existTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var existLowRatio = (existTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			var ratio = existTotalFlowValue*1000000/<%=flowInfoForm.getPresent()%>;
			if (isNaN(ratio)){
               flowToPopulationExistRatioText.innerHTML = "";
            }  
			else { 
			    if (<%=flowInfoForm.getPresent()%> < existLowRatio || <%=flowInfoForm.getPresent()%> > existHiRatio){
			       flowToPopulationExistRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + '</I></FONT>'; 
				}else{ 
				      flowToPopulationExistRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + '</I>';
				 }
		    }		  
		   }             
     }
     else if(elemId == "preTotalFlow"){
            var preTotalFlowValue = document.getElementById("preTotalFlow").value;
            if(<%=flowInfoForm.getPresent()%> > 0){
            var flowToPopulationPreRatioText = document.getElementById("flowToPopulationPreRatio");
			var preHiRatio = (preTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var preLowRatio = (preTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			var ratio = preTotalFlowValue*1000000/<%=flowInfoForm.getPresent()%>; 
			if (isNaN(ratio)){
               flowToPopulationPreRatioText.innerHTML = "";
            }
            else{   
			    if (<%=flowInfoForm.getPresent()%> < preLowRatio || <%=flowInfoForm.getPresent()%> > preHiRatio){
			       flowToPopulationPreRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I></FONT>"
				}else{ 
				      flowToPopulationPreRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I>"
				 }
			}	  
		   }               
     }
     else if(elemId == "proTotalFlow"){
            var proTotalFlowValue = document.getElementById("proTotalFlow").value;
            if(<%=flowInfoForm.getProjected()%> > 0){
            var flowToPopulationProRatioText = document.getElementById("flowToPopulationProRatio");
			var proHiRatio = (proTotalFlowValue*1000000)/<%=flowInfoForm.getLow()%>;
			var proLowRatio = (proTotalFlowValue*1000000)/<%=flowInfoForm.getHigh()%>;
			var ratio = proTotalFlowValue*1000000/<%=flowInfoForm.getProjected()%>; 
			if (isNaN(ratio)){
               flowToPopulationProRatioText.innerHTML = "";
            }
            else {  
			    if (<%=flowInfoForm.getProjected()%> < proLowRatio || <%=flowInfoForm.getProjected()%> > proHiRatio){
			       flowToPopulationProRatioText.innerHTML = '<FONT color="#ff0000"><I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I></FONT>"
				}else{ 
				      flowToPopulationProRatioText.innerHTML = '<I>' + Math.round(ratio*Math.pow(10,1))/Math.pow(10,1) + "</I>"
				 }
			}	  
		}                
     }
   }
   
   function FlowReset(){
     window.document.FlowInformationFormBean.reset();
     Calculatetotal("existTotalFlow");
     Calculatetotal("preTotalFlow");
     Calculatetotal("proTotalFlow");
     /*
     var hidden_field = document.getElementById('paramId');
     hidden_field.name = "<%=param %>";
     hidden_field.value = "Cancel";
     window.document.FlowInformationFormBean.submit(); 
     return true;*/
   }
   
   function ConfirmAndSubmit(){
       var form = document.getElementById('FlowInformationFormBeanId'); 
         if(form!=undefined){
            if(validateFlowInformationFormBean(form)==false){
               return;
            }
         }
           var existTotalFlow,preTotalFlow;
              
           // hidden_field = window.document.getElementById("flowActId");
	        //hidden_field.value="save";
	        if (document.getElementById("totalFlowStateId")!=null&&document.getElementById("totalFlowStateId").value == "disable"){
	           window.document.getElementById("totalFlowState").value = "enable";
	           window.document.getElementById("existTotalFlow").disabled="";
	           window.document.getElementById("preTotalFlow").disabled="";
	           window.document.getElementById("proTotalFlow").disabled="";
	        }   
	        else{
	           window.document.getElementById("totalFlowState").value = "disable"; 
	           window.document.getElementById("existMunicipalFlow").disabled = "";
               window.document.getElementById("preMunicipalFlow").disabled = "";
               window.document.getElementById("existIndustrialFlow").disabled = "";
               window.document.getElementById("preIndustrialFlow").disabled = "";
               window.document.getElementById("existInfiltrationFlow").disabled = "";
               window.document.getElementById("preInfiltrationFlow").disabled = "";
	           window.document.getElementById("proMunicipalFlow").disabled = "";
               window.document.getElementById("proIndustrialFlow").disabled = "";
               window.document.getElementById("proInfiltrationFlow").disabled = ""; 
               window.document.getElementById("existTotalFlow").disabled="";
	           window.document.getElementById("preTotalFlow").disabled="";
	           window.document.getElementById("proTotalFlow").disabled="";             
	           
	        }
	        	          
	        window.document.FlowInformationFormBean.submit();
	        return true;
     
   }

</script>

<pdk-html:form name="FlowInformationFormBean"
	type="gov.epa.owm.mtb.cwns.flowInformation.FlowInformationForm" styleId="FlowInformationFormBeanId"
	action="saveFlowInfo.do">
	<DIV id="hidden_fields" style="DISPLAY:none">
	  <!--  <input type="hidden" id="paramId" name="" value="">	-->
	  <pdk-html:text styleId="surveyFacilityId" property="surveyFacilityId" value="<%=flowInfoForm.getSurveyFacilityId() %>"/>
      <pdk-html:text styleId="totalFlowState" property="totalFlowState" value="<%=flowInfoForm.getTotalFlowState() %>"></pdk-html:text>
      
    </DIV>
   <div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="warnMessage" name="warnMessages" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/warn24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<%=warnMessage %><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
    </div>    
        
	<TABLE class="PortletText1"
		style="BORDER-BOTTOM: rgb(122,150,223) thin solid" cellspacing="3"
		cellpadding="1" width="100%">
		<!-- 
		<TR>
			<TD colspan="4">
				<STRONG><FONT size="4"></FONT> </STRONG>
				<DIV style="FLOAT: left" width="300">
					<STRONG><FONT size="4">Flow Information</FONT> </STRONG>
					<FONT size="1"></FONT>
				</DIV>
			</TD>
		</TR> -->
		<TR class="PortletHeaderColor">
			<TD></TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Existing
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Present Design
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P style="COLOR: white" align="center">
					Projected Design
				</P>
			</TD>
		</TR>
		<logic:equal name="FlowInformationFormBean" property="isUpdatable"
			value="Y">

			<TR>
				<TD>
					Municipal Flow (MGD)
					
				</TD>
				<TD>
					<P align="center">
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<pdk-html:text  styleId="existMunicipalFlow" name="FlowInformationFormBean" property="existMunicipalFlow"
								value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						    <logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						        <pdk-html:text styleId="existMunicipalFlow" name="FlowInformationFormBean" property="existMunicipalFlow" onblur="Calculatetotal(this.id)"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistMunicipalFlow()) %>" maxlength="8" size="10" />
						    </logic:equal>
						    <logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						       <pdk-html:text styleId="existMunicipalFlow" name="FlowInformationFormBean" property="existMunicipalFlow"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistMunicipalFlow()) %>" maxlength="8" size="10" disabled="true"
								   styleClass="disabledField"/>
						    </logic:notEqual>		   
						</logic:equal>
					</P>
				</TD>
				<TD>
					<P align="center">
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<pdk-html:text styleId="preMunicipalFlow" name="FlowInformationFormBean" property="preMunicipalFlow"
								value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						    <logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="preMunicipalFlow" name="FlowInformationFormBean" property="preMunicipalFlow" onblur="Calculatetotal(this.id)"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreMunicipalFlow()) %>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="preMunicipalFlow" name="FlowInformationFormBean" property="preMunicipalFlow"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreMunicipalFlow()) %>" maxlength="8" size="10" disabled="true"
								    styleClass="disabledField"/>
							</logic:notEqual>	    
						</logic:equal>

					</P>
				</TD>
				<TD>
					<P align="center">
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<pdk-html:text styleId="proMunicipalFlow" name="FlowInformationFormBean" property="proMunicipalFlow"
								value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
							<logic:equal name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
							  <logic:equal name="FlowInformationFormBean" property="isProMunicipalFlowUpdatable" value="Y">
							     <pdk-html:text styleId="proMunicipalFlow" name="FlowInformationFormBean" property="proMunicipalFlow" onblur="Calculatetotal(this.id)"
								 value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProMunicipalFlow()) %>" maxlength="8" size="10" />
						      </logic:equal>
						      <logic:equal name="FlowInformationFormBean" property="isProMunicipalFlowUpdatable" value="N">
						         <pdk-html:text styleId="proMunicipalFlow" name="FlowInformationFormBean" property="proMunicipalFlow"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProMunicipalFlow()) %>" maxlength="8" size="10" disabled="true"
								   styleClass="disabledField"/>
						      </logic:equal>
						    </logic:equal>
						    <logic:notEqual name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
						       <pdk-html:text styleId="proMunicipalFlow" name="FlowInformationFormBean" property="proMunicipalFlow"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProMunicipalFlow()) %>" maxlength="8" size="10" disabled="true"
								   styleClass="disabledField"/>
						    </logic:notEqual>		   
						</logic:equal>
					</P>
				</TD>
			</TR>
		</logic:equal>
		<logic:notEqual name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
					Municipal Flow (MGD)
				</TD>
				<TD>
					<P align="center">
						<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistMunicipalFlow()) %>
						<%--<bean:write name="FlowInformationFormBean"
							property="existMunicipalFlow" />--%>
					</P>
				</TD>
				<TD>
					<P align="center">
					<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreMunicipalFlow()) %>
						<%--<bean:write name="FlowInformationFormBean"
							property="preMunicipalFlow" />--%>
					</P>
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getProMunicipalFlow()) %>
						<%--<bean:write name="FlowInformationFormBean"
							property="proMunicipalFlow" />--%>
					</P>
				</TD>
			</TR>
		</logic:notEqual>
		<logic:equal name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
					Industrial Flow (MGD)
				</TD>
				<TD>
					<P align="center">
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<pdk-html:text styleId="existIndustrialFlow" name="FlowInformationFormBean" property="existIndustrialFlow"
								value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
							<logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							   <pdk-html:text styleId="existIndustrialFlow" name="FlowInformationFormBean" property="existIndustrialFlow" onblur="Calculatetotal(this.id)"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistIndustrialFlow()) %>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							   <pdk-html:text styleId="existIndustrialFlow" name="FlowInformationFormBean" property="existIndustrialFlow"
								   value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistIndustrialFlow()) %>" maxlength="8" size="10" disabled="true"
								   styleClass="disabledField"/>
							</logic:notEqual>	
						</logic:equal>
					</P>
				</TD>
				<TD>
					<P align="center">
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <pdk-html:text styleId="preIndustrialFlow" name="FlowInformationFormBean" property="preIndustrialFlow"
							    value="" maxlength="8" size="10"  disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>	
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						    <logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						       <pdk-html:text styleId="preIndustrialFlow" name="FlowInformationFormBean" property="preIndustrialFlow" onblur="Calculatetotal(this.id)"
							       value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreIndustrialFlow()) %>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							   <pdk-html:text styleId="preIndustrialFlow" name="FlowInformationFormBean" property="preIndustrialFlow"
							       value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreIndustrialFlow()) %>" maxlength="8" size="10" disabled="true"
							       styleClass="disabledField"/> 
							</logic:notEqual>       
						</logic:equal>
							
					</P>
				</TD>
				<TD>
					<P align="center">
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <pdk-html:text styleId="proIndustrialFlow" name="FlowInformationFormBean" property="proIndustrialFlow"
							    value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
					    </logic:equal>
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
					        <logic:equal name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
						       <pdk-html:text styleId="proIndustrialFlow" name="FlowInformationFormBean" property="proIndustrialFlow" onblur="Calculatetotal(this.id)"
							       value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProIndustrialFlow()) %>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
							   <pdk-html:text styleId="proIndustrialFlow" name="FlowInformationFormBean" property="proIndustrialFlow"
							       value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProIndustrialFlow()) %>" maxlength="8" size="10" disabled="true"
							       styleClass="disabledField"/> 
							</logic:notEqual>       
					    </logic:equal>
					    		    
					</P>
				</TD>
			</TR>
		</logic:equal>
		<logic:notEqual name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
					Industrial Flow (MGD)
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistIndustrialFlow()) %>
					</P>
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreIndustrialFlow()) %>
						
					</P>
				</TD>
				<TD>
					<P align="center">
						<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProIndustrialFlow()) %>
					</P>
				</TD>
			</TR>
		</logic:notEqual>
		<logic:equal name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
					Infiltration Flow (MGD)
				</TD>
				<TD>
					<P align="center">
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <pdk-html:text styleId="existInfiltrationFlow" name="FlowInformationFormBean" property="existInfiltrationFlow"
							    value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
					    </logic:equal>
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
					        <logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						        <pdk-html:text styleId="existInfiltrationFlow" name="FlowInformationFormBean" property="existInfiltrationFlow" onblur="Calculatetotal(this.id)"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistInfiltrationFlow()) %>" maxlength="8" size="10"/>
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="existInfiltrationFlow" name="FlowInformationFormBean" property="existInfiltrationFlow"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistInfiltrationFlow()) %>" maxlength="8" size="10" disabled="true"
							        styleClass="disabledField"/>
							</logic:notEqual>        
					    </logic:equal>		    
					</P>
				</TD>
				<TD>
					<P align="center">
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <pdk-html:text styleId="preInfiltrationFlow" name="FlowInformationFormBean" property="preInfiltrationFlow"
							    value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
					    </logic:equal> 
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						    <logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						        <pdk-html:text styleId="preInfiltrationFlow" name="FlowInformationFormBean" property="preInfiltrationFlow" onblur="Calculatetotal(this.id)"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreInfiltrationFlow()) %>" maxlength="8" size="10"/>
							</logic:equal>  
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="preInfiltrationFlow" name="FlowInformationFormBean" property="preInfiltrationFlow"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreInfiltrationFlow()) %>" maxlength="8" size="10" disabled="true"
							        styleClass="disabledField"/> 
							</logic:notEqual>     
					    </logic:equal>		    
					</P>
				</TD>
				<TD>
					<P align="center">
					    <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <pdk-html:text styleId="proInfiltrationFlow" name="FlowInformationFormBean" property="proInfiltrationFlow"
							    value="" maxlength="8" size="10" disabled="true" onblur="Calculatetotal(this.id)" styleClass="disabledField"/>
						</logic:equal>	
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						    <logic:equal name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
						        <pdk-html:text styleId="proInfiltrationFlow" name="FlowInformationFormBean" property="proInfiltrationFlow" onblur="Calculatetotal(this.id)"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProInfiltrationFlow()) %>" maxlength="8" size="10"/>
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
							   <pdk-html:text styleId="proInfiltrationFlow" name="FlowInformationFormBean" property="proInfiltrationFlow"
							        value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProInfiltrationFlow()) %>" maxlength="8" size="10" disabled="true"
							        styleClass="disabledField"/> 
							</logic:notEqual>        
						</logic:equal>    
					</P>
				</TD>
			</TR>
		</logic:equal>
		<logic:notEqual name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
					Infiltration Flow (MGD)
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistInfiltrationFlow()) %>
						
					</P>
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreInfiltrationFlow()) %>
						
					</P>
				</TD>
				<TD>
					<P align="center">
					    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getProInfiltrationFlow()) %>
						
					</P>
				</TD>
			</TR>
		</logic:notEqual>
		<logic:equal name="FlowInformationFormBean" property="isUpdatable"
			value="Y">
			<TR>
				<TD>
				
					<b>Total Flow (MGD)</b>
					<logic:equal name="FlowInformationFormBean" property="isTotalFlowUpdatable" value="Y">
					<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						<input type="button" id="totalFlowStateId" value="disable" onclick="changeState()">
										
					</logic:equal>
					<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
						<input type="button" id="totalFlowStateId" value="enable" onclick="changeState()">
						
					</logic:equal>
                    </logic:equal>
				</TD>

				<TD>
					<P align="center">
					   <logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="existTotalFlow" name="FlowInformationFormBean" property="existTotalFlow" onblur="Calculatetotal(this.id)"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %>" maxlength="8" size="10" />
						    </logic:equal>
						    <logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
						       <pdk-html:text styleId="existTotalFlow" name="FlowInformationFormBean" property="existTotalFlow"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %>" maxlength="8" size="10" disabled="true"
								    styleClass="disabledField"/>
						    </logic:notEqual> 		    
						</logic:equal>
					   	<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
							<pdk-html:text styleId="existTotalFlow" name="FlowInformationFormBean" property="existTotalFlow" onblur="Calculatetotal(this.id)"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %>" maxlength="8" size="10" disabled="true" 
								styleClass="disabledField"/>
						</logic:equal>
					   
					</P>
				</TD>
				<TD>
					<P align="center">
					   
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
							<logic:equal name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							    <pdk-html:text styleId="preTotalFlow" name="FlowInformationFormBean" property="preTotalFlow" onblur="Calculatetotal(this.id)"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow())%>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isPreFlowUpdatable" value="Y">
							   <pdk-html:text styleId="preTotalFlow" name="FlowInformationFormBean" property="preTotalFlow"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow())%>" maxlength="8" size="10" disabled="true"
								    styleClass="disabledField"/>
							</logic:notEqual>	    
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
							<pdk-html:text styleId="preTotalFlow" name="FlowInformationFormBean" property="preTotalFlow" onblur="Calculatetotal(this.id)"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow())%>" maxlength="8" size="10" disabled="true" 
								styleClass="disabledField"/>
						</logic:equal>
					   
					   	
					</P>
				</TD>
				<TD>
					<P align="center">
					   
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="enable">
						    <logic:equal name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
							    <pdk-html:text styleId="proTotalFlow" name="FlowInformationFormBean" property="proTotalFlow" onblur="Calculatetotal(this.id)"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow())%>" maxlength="8" size="10" />
							</logic:equal>
							<logic:notEqual name="FlowInformationFormBean" property="isProFlowUpdatable" value="Y">
							    <pdk-html:text styleId="proTotalFlow" name="FlowInformationFormBean" property="proTotalFlow"
								    value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow())%>" maxlength="8" size="10" disabled="true"
								    styleClass="disabledField"/>
							</logic:notEqual>	    
						</logic:equal>
						<logic:equal name="FlowInformationFormBean" property="totalFlowState" value="disable">
							<pdk-html:text styleId="proTotalFlow" name="FlowInformationFormBean" property="proTotalFlow" onblur="Calculatetotal(this.id)"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow())%>" maxlength="8" size="10" disabled="true" 
								styleClass="disabledField"/>
						</logic:equal>
					   
					   
					</P>
				</TD>
			</TR>
		</logic:equal>
		<logic:notEqual name="FlowInformationFormBean" property="isUpdatable" value="Y">
			<TR>
				<TD>
					<B>Total Flow (MGD)</B>
				</TD>
				<TD>
					<P align="center">
					    <B><%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistTotalFlow()) %></B>
						
					</P>
				</TD>
				<TD>
					<P align="center">
					    <B><%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreTotalFlow()) %></B>
						
					</P>
				</TD>
				<TD>
					<P align="center">
					    <B><%=FlowInformationForm.convertFloatToString(flowInfoForm.getProTotalFlow()) %></B>
						
					</P>
				</TD>
			</TR>
		</logic:notEqual>
		<TR>
			<TD>
				Wet Weather Flow (Peak) (MGD)
			</TD>
			<logic:equal name="FlowInformationFormBean" property="isUpdatable" value="Y">
			   <TD>
				 <P align="center">
				    <pdk-html:text name="FlowInformationFormBean" property="existWWPFlow"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistWWPFlow())%>" maxlength="8" size="10" />
				 </P>
			   </TD>
			   <TD>
				 <P align="center">
					<pdk-html:text name="FlowInformationFormBean" property="preWWPFlow"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreWWPFlow())%>" maxlength="8" size="10" />
				 </P>
			   </TD>
			   <TD>
				 <P align="center">
					<pdk-html:text name="FlowInformationFormBean" property="proWWPFlow"
								value="<%=FlowInformationForm.convertFloatToString(flowInfoForm.getProWWPFlow())%>" maxlength="8" size="10" />
				 </P>
			   </TD>
			</logic:equal>
			<logic:notEqual name="FlowInformationFormBean" property="isUpdatable" value="Y">
			   <TD>
				 <P align="center">
				    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getExistWWPFlow()) %>
					
				 </P>
			   </TD>
			   <TD>
				 <P align="center">
				    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getPreWWPFlow()) %>
					
				 </P>
			   </TD>
			   <TD>
				 <P align="center">
				    <%=FlowInformationForm.convertFloatToString(flowInfoForm.getProWWPFlow()) %>
				    
				 </P>
			   </TD>
			</logic:notEqual>
		</TR>
		<TR>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD>
				<I>Flow to Population Ratio (GPCD)</I>
			</TD>
			<TD align="center">
			    <%
			    if(flowInfoForm.getPresent()>0){
			      double existHiRatio = (flowInfoForm.getExistTotalFlow()*1000000)/flowInfoForm.getLow();
			      double existLowRatio = (flowInfoForm.getExistTotalFlow()*1000000)/flowInfoForm.getHigh();
			      if (flowInfoForm.getPresent()<existLowRatio || flowInfoForm.getPresent()>existHiRatio){
			     %>
				     <span id="flowToPopulationExistRatio"><FONT color="#ff0000"><I><%=df.format(flowInfoForm.getExistTotalFlow()*1000000/flowInfoForm.getPresent()) %></I></FONT>
				     </span>
				 <%}else{ %>
				      <span id="flowToPopulationExistRatio"><I><%=df.format(flowInfoForm.getExistTotalFlow()*1000000/flowInfoForm.getPresent()) %></I>
				      </span>
				      <%} 
			    }%>
			</TD>
			<TD align="center">
			    <%
			    if(flowInfoForm.getPresent()>0){
			      double preHiRatio = (flowInfoForm.getPreTotalFlow()*1000000)/flowInfoForm.getLow();
			      double preLowRatio = (flowInfoForm.getPreTotalFlow()*1000000)/flowInfoForm.getHigh();
			      if (flowInfoForm.getPresent()<preLowRatio || flowInfoForm.getPresent()>preHiRatio){
			     %>
				     <span id="flowToPopulationPreRatio"><FONT color="#ff0000"><I><%=df.format(flowInfoForm.getPreTotalFlow()*1000000/flowInfoForm.getPresent()) %></I> </FONT>
				     </span>
				 <%}else{ %>
				      <span id="flowToPopulationPreRatio"><I><%=df.format(flowInfoForm.getPreTotalFlow()*1000000/flowInfoForm.getPresent()) %></I>
				      </span>
				      <%} 
				}%>    
			</TD>
			<TD align="center">
				<%
				if(flowInfoForm.getProjected()>0){
			      double proHiRatio = (flowInfoForm.getProTotalFlow()*1000000)/flowInfoForm.getLow();
			      double proLowRatio = (flowInfoForm.getProTotalFlow()*1000000)/flowInfoForm.getHigh();
			      if (flowInfoForm.getProjected()<proLowRatio || flowInfoForm.getProjected()>proHiRatio){
			     %>
				     <span id="flowToPopulationProRatio"><FONT color="#ff0000"><I><%=df.format(flowInfoForm.getProTotalFlow()*1000000/flowInfoForm.getProjected()) %></I></FONT> 
				     </span>
				 <%}else{ %>
				     <span id="flowToPopulationProRatio"><I><%=df.format(flowInfoForm.getProTotalFlow()*1000000/flowInfoForm.getProjected()) %></I>
				     </span> 
				    <%} 
				}%>  
				
			</TD>
		</TR>
		<logic:equal name="FlowInformationFormBean" property="isUpdatable" value="Y">
		  <TR>
			 <TD colspan="7">
				<div align="left" width="150">
				   <!-- 
				   <pdk-html:submit value="Save"/>
				   <pdk-html:cancel value="Cancel"/>
				   -->
				   <A href="javascript:ConfirmAndSubmit()">
		              <FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
			       <FONT size="1">&nbsp;&nbsp;</FONT>
				   <A href="javascript:FlowReset();"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0"/></FONT></A>
				   
				</div>
			 </TD>
		  </TR>
		</logic:equal>
	</TABLE>
</pdk-html:form>
<pdk-html:javascript formName="FlowInformationFormBean" />