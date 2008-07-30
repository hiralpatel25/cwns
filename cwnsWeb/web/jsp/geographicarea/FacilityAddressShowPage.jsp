<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"	
	import="gov.epa.owm.mtb.cwns.facilityInformation.FacilityAddressForm"%>

<%
	FacilityAddressForm facilityAddressForm = (FacilityAddressForm)request.getAttribute("facilityAddress");
%>	

<SCRIPT type=text/javascript>

function FacilityAddressReset(){
     window.document.facilityAddressForm.reset();
}
// Validate the zipcode
function validateZip(zip) {
	valid = false
	if (zip.length == 0) {
		valid = true;
	} else if (zip.length == 5) {
		valid = !isNaN(zip);
	} else if (zip.length == 10) {
		prefix = zip.substring(0,5);
		dash   = zip.substring(5,6);
		suffix = zip.substring(6,zip.length);
		if (!isNaN(prefix) && !isNaN(suffix) && dash == '-') {
			valid = true;
		}
	} 
	return valid;
}

function FacilityAddressSubmit(){
/*
 var address1 = trim(document.getElementById('address1').value);
 var city = trim(document.getElementById('city').value);
 var state = trim(document.getElementById('state').value);
 var zipCode = trim(document.getElementById('zipCode').value);
 
    if (address1=="" && (city!="" || state!="" || zipCode!="")){
	  alert("Address is required");
	  return;
	}
    if (city=="" && (state!="" || zipCode!="" || address1!="")){
	  alert("City is required");
	  return;
	}
	if (state=="" && (address1!="" || city!="" || zipCode!="")){
	  alert("State is required");
	  return;
	}
	if (zipCode=="" && (address1!="" || city!="" || state!="")){
	  alert("ZipCode is required");
	  return;
	}
*/	
<logic:equal name="required" value="true">
 var form = document.getElementById('facilityAddressFormId');   
 if(form!=undefined){
   if(validateFacilityAddressForm(form)==false){
     return;
   }
  }
 </logic:equal>
 <logic:equal name="required" value="false"> 
 var zipCode = trim(document.getElementById('zipCode').value);
 if (zipCode.length>0 && !validateZip(zipCode))  {
		alert('Zip Code must either be 5 numbers or 5 numbers followed by a - followed by 4 numbers');
		return;
 }		
  </logic:equal>		
  window.document.facilityAddressForm.submit();
	return true;
	     
  /*
  var c = "<%=facilityAddressForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result == true){
        window.document.facilityAddressForm.submit();
	    return true;
      }
      else 
        return;
   }
   else {
        window.document.facilityAddressForm.submit();
	    return true;
   }*/
}  

</SCRIPT>
	
<pdk-html:form name="facilityAddressForm" styleId="facilityAddressFormId"
               type="gov.epa.owm.mtb.cwns.facilityInformation.FacilityAddressForm"
               action="saveFacilityAddress.do">
<DIV id="hidden_fields" style="DISPLAY:none">
    <pdk-html:text property="facilityId" value="<%=facilityAddressForm.getFacilityId() %>" /> 
</DIV>               	

<table cellspacing="2" cellpadding="3" border="0" width="100%"
	class="PortletText1">
	<!--  
	<TR>
		<TD colspan="5">
			<font size="2"> <Strong> Address </Strong> </font>
		</TD>
	</TR>-->
	<TR>
						<TD colspan="5">
							<div style="float: left" width="300">
								
								<FONT size="4"><STRONG>Address</STRONG> </FONT>
							</div>
							
						</TD>
	</TR>
	<TR>
		<TD colspan="5">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="15%">
			<STRONG> <logic:equal name="required" value="true"><FONT color="#ff0000">*</FONT></logic:equal>&nbsp;&nbsp;Address: </STRONG>
		</TD>
		<logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD width="35%">
		    <pdk-html:text name="facilityAddressForm" property="address1" styleId="address1" size="30" maxlength="40"/>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD width="35%">
			<bean:write name="facilityAddressForm" property="address1" />
		</TD>
		</logic:notEqual>
		<logic:match name="facilityAddressForm" property="isSourcedFromNPDES" value='Y'>
		<TD width="10%">
		    <STRONG>&nbsp;&nbsp;Source: </STRONG>     
		</TD>
		<TD>
		    Envirofacts
		</TD>
		<TD></TD>
		</logic:match>
		<logic:notMatch name="facilityAddressForm" property="isSourcedFromNPDES" value='Y'>
		<TD colspan="3"></TD>
		</logic:notMatch>
	</TR>
	<TR>
		<TD>
			<logic:equal name="required" value="true">&nbsp;</logic:equal>&nbsp;&nbsp;<B>Address 2: </B>
		</TD>
		<logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
		    <pdk-html:text name="facilityAddressForm" property="address2" size="30" maxlength="40"/>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
			<bean:write name="facilityAddressForm" property="address2" />
		</TD>
		</logic:notEqual>
						
		<td colspan="3"></td>
	</TR>
	<TR>
	    <TD>
			<STRONG><logic:equal name="required" value="true"><FONT color="#ff0000">*</FONT></logic:equal>&nbsp;&nbsp;City:</STRONG>
		</TD>
		<logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
		    <pdk-html:text name="facilityAddressForm" property="city" styleId="city" size="30" maxlength="25"/>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
			<bean:write name="facilityAddressForm" property="city" />
		</TD>
		</logic:notEqual>
		
	    <td colspan="3"></td>
	</TR>
	<TR>
	   <TD>
			<STRONG> <logic:equal name="required" value="true"><FONT color="#ff0000">*</FONT></logic:equal>&nbsp;&nbsp;State: </STRONG>
	   </TD>
	   <logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
	   <TD>
	       <pdk-html:select name="facilityAddressForm"  property="state" styleId="state">
	          <!-- <option value="" selected="selected"></option> -->
	   	      <logic:iterate id="stateCode" name="facilityAddressForm" property="states" type="java.lang.String">
					<logic:match name="facilityAddressForm" property="state" value='<%=stateCode%>'>
						<OPTION value="<%=stateCode%>" selected="selected"><%=stateCode%></OPTION>
					</logic:match>
					<logic:notMatch name="facilityAddressForm" property="state" value='<%=stateCode%>'>
						<OPTION value="<%=stateCode%>"><%=stateCode%></OPTION>
					</logic:notMatch> 		            	
                </logic:iterate>
	       </pdk-html:select>
			
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
		    <bean:write name="facilityAddressForm" property="state" />
		</TD>
		</logic:notEqual>
		<td colspan="3"></td>
	</TR>
	<TR>
		<TD>
			<STRONG><logic:equal name="required" value="true"> <FONT color="#ff0000">*</FONT></logic:equal>&nbsp;&nbsp;Zip&nbsp;Code: </STRONG>
		</TD>
		<logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
		    <pdk-html:text name="facilityAddressForm" property="zipCode" styleId="zipCode" size="30" maxlength="10"/>
		</TD>
		</logic:equal>
		<logic:notEqual name="facilityAddressForm" property="isUpdatable" value="Y">
		<TD>
			<bean:write name="facilityAddressForm" property="zipCode" />
		</TD>
		</logic:notEqual>
		
		<TD colspan="3"></TD>
	</TR>
	<logic:equal name="facilityAddressForm" property="isUpdatable" value="Y">
	  <TR>
	     <TD colspan="3">
	     <div align="left" width="150">
		   <A href="javascript:FacilityAddressSubmit();"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
		   &nbsp;&nbsp;
		   <A href="javascript:FacilityAddressReset();"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0" /></FONT></A>
		 </div>
		 </TD>  
	  </TR>
	</logic:equal>
	<TR>
		<TD></TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
</table>
</pdk-html:form>
<pdk-html:javascript formName="facilityAddressForm" staticJavascript="false"/>	