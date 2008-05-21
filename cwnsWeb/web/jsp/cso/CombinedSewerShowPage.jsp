<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			
			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    		String validationurl = url +"/javascript/validation.js";
    		String dateurl = url +"/javascript/date.js";
%>
<script type="text/javascript" language="JavaScript" src="<%=validationurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>
<script type="text/javascript">
   
   function submitCSO(){
     var form = window.document.getElementById('csoFormId');
     if(validateCSO(form)){
        form.submit();
     }
   }
   
   function validateCSO(form){
      if(form.csoStatusId.value == 'D'){
        if(!validateFieldValue(form.docAreaId.value,'N')){
          alert("The Documented Area should be an Integer and greater than 0"); 
          return false;
        }
        if(!validateFieldValue(form.docPopulationId.value,'N')){
          alert("The Documented Population should be an Integer and greater than 0"); 
          return false;
        }
      }else if(form.csoStatusId.value == 'C'){
        //CC required
        if(!validateFieldValue(form.ccAreaId.value,'Y')){
          alert("The Cost Curve Area should be an Integer and greater than 0"); 
          return false;
        }
        if(!validateFieldValue(form.ccPopulationId.value,'Y')){
          alert("The Cost Curve Population should be an Integer and greater than 0"); 
          return false;
        }
      	
      }else if(form.csoStatusId.value == 'B'){
        //documented
        if(!validateFieldValue(form.docAreaId.value,'N')){
          alert("The Documented Area should be an Integer and greater than 0"); 
          return false;
        }
        if(!validateFieldValue(form.docPopulationId.value,'N')){
          alert("The Documented Population should be an Integer and greater than 0"); 
          return false;
        }
        //CC required
        if(!validateFieldValue(form.ccAreaId.value,'Y')){
          alert("The Cost Curve Area should be an Integer and greater than 0"); 
          return false;
        }
        if(!validateFieldValue(form.ccPopulationId.value,'Y')){
          alert("The Cost Curve Population should be an Integer and greater than 0"); 
          return false;
        }        
      }else if(form.csoStatusId.value == 'N'){
         //do nothing
         return true;
      }else{
         alert("CSO Status is required"); 
         return false;
      }
      return true;
   }
   
   function validateFieldValue(value, required){
     if(value !=null) value = trim(value);
     if(value=='' && required == 'Y'){
        return false;
     }
     if(value!=''&&!isIntAndGTZero(value)){
         return false;
     }
     return true;
   }   
   
   function isIntAndGTZero(value){
		if (!isAllDigits(value)) {
   			return false;
		}else {
  			var iValue = parseInt(value);
  			if (isNaN(iValue) || !(iValue >= -2147483648 && iValue <= 2147483647)){
    			return false;
  			} else{
    			if(iValue <=0) {
      				return false;
    			}
  			}
		}
		return true;
   }
   
   function resetCSO(){
    var form = window.document.getElementById('csoFormId'); 
   	form.reset();
   	updateFields(form);
   }
   
   function updateFields(form){
   
      if(form.csoStatusId.value == 'N'){
        disableField(form.docAreaId); 
        disableField(form.docPopulationId);
      	window.document.getElementById('docTextId').className='disabledText';           
      	
        disableField(form.ccAreaId); 
        disableField(form.ccPopulationId);
      	window.document.getElementById('ccTextId').className='disabledText';           
      	window.document.getElementById('ccReq').style.display='none';
      	window.document.getElementById('ccSpace').style.display='block';
      	
      }else if(form.csoStatusId.value == 'D'){
        enableField(form.docAreaId); 
        enableField(form.docPopulationId);
      	window.document.getElementById('docTextId').className='';           
      	      	
        disableField(form.ccAreaId); 
        disableField(form.ccPopulationId);
      	window.document.getElementById('ccTextId').className='disabledText';
      	window.document.getElementById('ccReq').style.display='none';
      	window.document.getElementById('ccSpace').style.display='block';

      }else if(form.csoStatusId.value == 'C'){
        disableField(form.docAreaId); 
        disableField(form.docPopulationId);
      	window.document.getElementById('docTextId').className='disabledText';           

        enableField(form.ccAreaId); 
        enableField(form.ccPopulationId);
      	window.document.getElementById('ccTextId').className='';      
      	window.document.getElementById('ccReq').style.display='block';
      	window.document.getElementById('ccSpace').style.display='none';
      }else if(form.csoStatusId.value == 'B'){
        enableField(form.docAreaId); 
        enableField(form.docPopulationId);
      	window.document.getElementById('docTextId').className='';
      	
      	enableField(form.ccAreaId); 
        enableField(form.ccPopulationId);
      	window.document.getElementById('ccTextId').className='';
      	window.document.getElementById('ccReq').style.display='block';
      	window.document.getElementById('ccSpace').style.display='none';
      }else{
      	disableField(form.docAreaId); 
        disableField(form.docPopulationId);
      	window.document.getElementById('docTextId').className='disabledText';           
      	
        disableField(form.ccAreaId); 
        disableField(form.ccPopulationId);
      	window.document.getElementById('ccTextId').className='disabledText';           
      	window.document.getElementById('ccReq').style.display='none';
      	window.document.getElementById('ccSpace').style.display='block';
      }
   }
   
   function disableField(field){
      	field.disabled = true;
      	field.value = '';
      	field.className = 'disabledField';
   }
   
   function enableField(field){
      	field.disabled = false;
      	field.className = '';
   }
   
   

</script>

<pdk-html:form name="csoForm" styleId="csoFormId"  method="post" type="org.apache.struts.action.DynaActionForm" action="saveCSO.do">

	<TABLE class="PortletText1" border="0" cellpadding="2" cellspacing="1" width="100%">
		<TR>
			<TD><STRONG> <FONT size="2"> CSO Status </FONT> </STRONG></TD>
			<TD>
			   <logic:equal name="updatable" value="true"> 
				<pdk-html:select styleId="csoStatusId" name="csoForm"  property="statusId" onchange="updateFields(this.form)">
				    <option value=""></option>
	   	 			<pdk-html:options collection="csoStatusReferences" property="combinedSewerStatusId" labelProperty="name"/>
				</pdk-html:select>			
			   </logic:equal>	
			   <logic:notEqual name="updatable" value="true"> 
			    	<bean:write  name="statusLabel"/>
			    </logic:notEqual>
			</TD>
		</TR>
		<TR><td colspan="3"><br/></td></TR>
		<TR>
			<TD class="PortletHeaderColor">
				&nbsp;
			</TD>
			<TD class="PortletHeaderColor">
				<P align="center" class="PortletHeaderText">Area (Acres)</P>
			</TD>
			<TD class="PortletHeaderColor">
				<P align="center" class="PortletHeaderText">Population</P>
			</TD>
		</TR>
		<TR>
			<TD>
				<div id="docSpace" style="float:left; display: block"><p class="required">&nbsp;&nbsp;&nbsp;</p></div><div style="float:left" id="docTextId">Documented</div>
			</TD>
			<TD>

				<logic:equal name="updatable" value="true"> 
			    	<P align="center"><pdk-html:text styleId="docAreaId" name="csoForm" property="docArea" maxlength="6"/></P>
			    </logic:equal>
			    <logic:notEqual name="updatable" value="true"> 
			    	<P align="center"><bean:write  name="csoForm" property="docArea"/></P>
			    </logic:notEqual>			   					
			</TD>
			<TD>
				<logic:equal name="updatable" value="true"> 
			    	<P align="center"><pdk-html:text styleId="docPopulationId"  name="csoForm" property="docPopulation" maxlength="8"/></P>
			    </logic:equal>
			    <logic:notEqual name="updatable" value="true"> 
			    	<P align="center"><bean:write  name="csoForm" property="docPopulation"/></P>
			    </logic:notEqual>			   				
			</TD>
		</TR>
		<TR class="PortletSubHeaderColor">
			<TD>
				<div id="ccSpace" style="float:left; display: block"><p class="required">&nbsp;&nbsp;&nbsp;</p></div><div id="ccReq" style="float:left; display: none"><p class="required">*&nbsp;&nbsp;</p></div><div style="float:left" id="ccTextId">Cost Curves</div>
			</TD>
			<TD>			
				<logic:equal name="updatable" value="true"> 
			    	<P align="center"><pdk-html:text styleId="ccAreaId"  name="csoForm" property="ccArea" maxlength="6" /></P>
			    </logic:equal>
			    <logic:notEqual name="updatable" value="true"> 
			    	<P align="center"><bean:write  name="csoForm" property="ccArea"/></P>
			    </logic:notEqual>	
			</TD>
			<TD>
			    <logic:equal name="updatable" value="true"> 
			    	<P align="center"><pdk-html:text styleId="ccPopulationId" name="csoForm" property="ccPopulation" maxlength="8"/></P>
			    </logic:equal>
			    <logic:notEqual name="updatable" value="true"> 
			    	<P align="center"><bean:write  name="csoForm" property="ccPopulation"/></P>
			    </logic:notEqual>			   	
			</TD>
		</TR>
		<logic:equal name="updatable" value="true">
		<tr>
			<td colspan="3">
				<a href="javascript:submitCSO()"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></a>
				&nbsp;
				<a href="javascript:resetCSO()"><pdk-html:img page="/images/reset.gif" alt="Cancel" border="0"/></a>
			</td>
		</tr>		
		</logic:equal>
	</TABLE>
	 <logic:equal name="updatable" value="true"> 
    	<script type="text/javascript">  updateFields(window.document.getElementById('csoFormId'));</script>
    </logic:equal>	

</pdk-html:form>