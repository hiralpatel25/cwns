<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>

<%
    PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<SCRIPT type=text/javascript>

function populateValues(){
 if(window.opener && !window.opener.closed) {
   var type = "<%=prr.getParameter("conType")%>";
   var dddd = "";
   if (type == 'latitude')
      dddd = window.opener.document.getElementById('latitude').value;
   else if (type == 'longitude')  
          dddd = window.opener.document.getElementById('longitude').value;
   if (dddd != ""){
   
   
      var d = "";
      var m = "";
      var s = "";
      var xy = "";
      var tempArray1 = dddd.split('.');
      d = tempArray1[0];
      xy = (parseFloat('0.'+tempArray1[1]))*60+"";
      var tempArray2 = xy.split('.');
      m = tempArray2[0];
      s = (parseFloat('0.'+tempArray2[1]))*60;
      window.document.getElementById('degree1').value=d;
      window.document.getElementById('minutes').value=m;
      window.document.getElementById('seconds').value=parseInt(s);
      
      window.document.getElementById('degree2').value=d;
      var dminutes = Math.round(xy*Math.pow(10,2))/Math.pow(10,2)
      window.document.getElementById('decimalminutes').value=dminutes;
    }
  else
      window.document.getElementById('convertId').disabled='disabled';   
     }
}

function changeOption(){
  var c = "";  
    for (var i=0; i < window.document.converterForm.converter.length; i++){
         if (window.document.converterForm.converter[i].checked) {
            c = window.document.converterForm.converter[i].value;
         }
    }
    if (c == 'option1'){
       window.document.getElementById('radio_button1').style.display='block';
       window.document.getElementById('radio_button2').style.display='none';
    }
    else if (c == 'option2'){
           window.document.getElementById('radio_button2').style.display='block';
           window.document.getElementById('radio_button1').style.display='none';
         }
}

function disableOrEnableButton(id){
 var v = window.document.getElementById(id).value
   if (v == "")
     window.document.getElementById('convertId').disabled='disabled';   
   else
     window.document.getElementById('convertId').disabled='';     
}

function convStrToFloat(str){
     if(str == null || str == "")
       return 0;
     else
       return parseFloat(str);
   }

function convert() {
  var d = "";
  var m = "";
  var s = "";
  var dd = "";
  var dddd = "";
  var type = "<%=prr.getParameter("conType")%>";
  
  if(window.opener && !window.opener.closed) 
  {
    var c = "";  
    for (var i=0; i < window.document.converterForm.converter.length; i++){
         if (window.document.converterForm.converter[i].checked) {
            c = window.document.converterForm.converter[i].value;
         }
    }
    if (c == 'option1'){
      d = window.document.getElementById('degree1').value;
      m = window.document.getElementById('minutes').value;
      s = window.document.getElementById('seconds').value;
      if (!ValidateDegree(d,type) || !ValidateMinutes(m) || !ValidateSeconds(s)){
         return false; 
      } 
      s = parseInt(s);
      dd = convStrToFloat(d)+convStrToFloat(m/60)+convStrToFloat(s/3600);
    }
    else if (c == 'option2'){
           d = window.document.getElementById('degree2').value;
           m = window.document.getElementById('decimalminutes').value;
           if (!ValidateDegree(d,type) || !ValidateDecimalMin(m)){
             return false;
           }
           m = Math.round(m*Math.pow(10,2))/Math.pow(10,2);
           dd = convStrToFloat(d)+convStrToFloat(m/60);
         }
  
  //dddd = Math.round(dd*Math.pow(10,4))/Math.pow(10,4);
  dddd = dd.toFixed(4);
  if (type == 'latitude')
    window.opener.document.getElementById('latitude').value=dddd;
  else if (type == 'longitude')  
          window.opener.document.getElementById('longitude').value=dddd;
  window.close();
  }
}

function ValidateDegree(degree,type){
  if (type == 'latitude'){
    if (degree>=0 && degree<=90){
      return true;
    }  
    else {
     alert("Degree should be from 0 to 90"); 
     return false;
    }
  }
  else
    if (type == 'longitude'){ 
      if (degree>=0 && degree<=180){
         return true;
      }  
      else {
          alert("Degree should be from 0 to 180");
          return false;
      }
    }
}    

function ValidateMinutes(minutes){
    if (minutes>=0 && minutes<=59){
      return true;
    }  
    else {
     alert("Minutes should be from 0 to 59");
     return false;
    }
}

function ValidateSeconds(seconds){    
    if (seconds>=0 && seconds<=59){
      return true;
    }  
    else {
     alert("Seconds should be from 0 to 59");
     return false;
    }  
}  

function ValidateDecimalMin(dminutes){
    if (dminutes>=0 && dminutes<=59.99){
      return true;
    }  
    else {
     alert("Decimal Minutes should be from 0 to 59.99");
     return false;
    }
}

</SCRIPT>
<BODY onload="populateValues()">
<FORM name="converterForm">
	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0" >

		<tr>
		   <TD align="center">
			   <input type="radio" name="converter" value="option1" checked="checked" onclick="changeOption()"/>
			</TD>
			<TD align="left">
				<FONT class="PortletText1">Degrees-Minutes-Seconds -> Decimal Degree </FONT>
			</TD>
		</tr>
		<tr>
		   <TD align="center">
			   <input type="radio" name="converter" value="option2" onclick="changeOption()"/>
			</TD>
			<TD align="left">
				<FONT class="PortletText1">Degrees-Decimal Minutes -> Decimal Degree </FONT>
			</TD>
		</tr>
		<tr>
		<td>
		</td>
		</tr>
		</TABLE>
		<DIV id="radio_button1" style="display:block">
		<table align="center">
		<tr>
		  <td class="PortletText1" align="left">
		    Degree
		  </td>
		  <td class="PortletText1" align="left">
		    Minutes
		  </td>
		  <td class="PortletText1" align="left">
		    Seconds
		  </td>
		</tr>
		  
		<tr>
		  <td class="PortletText1">
		    <input type="text" name="degree1" id="degree1" onchange="disableOrEnableButton('degree1')">
		  </td>
		  <td class="PortletText1">
		    <input type="text" name="minutes" id="minutes">
		  </td>
		  <td class="PortletText1">
		    <input type="text" name="seconds" id="seconds">
		  </td>
		</tr>
		</table>
		</DIV>
				
		<DIV id="radio_button2" style="display:none">
		<table align="center">
		<tr>
		  <td class="PortletText1" align="left">
		    Degree
		  </td>
		  <td class="PortletText1" align="left">
		    DecimalMinutes
		  </td>
		</tr>
		  
		<tr>
		  <td class="PortletText1">
		    <input type="text" name="degree2" id="degree2" onchange="disableOrEnableButton('degree2')">
		  </td>
		  <td class="PortletText1">
		    <input type="text" name="decimalminutes" id="decimalminutes">
		  </td>
		</tr>
		</table>
		</DIV>
		<TABLE align="center">
		<TR align="center">
		    <TD align="center">
		  	  <input type="button" name="Convert" value="Convert" id="convertId" onclick="convert()" />
		    </TD>
		</TR>
		</TABLE>

</FORM>
</BODY>