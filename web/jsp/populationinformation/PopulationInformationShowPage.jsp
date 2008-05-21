<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.portal.utils.NameValue"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils"
    import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="gov.epa.owm.mtb.cwns.populationInformation.PopulationInformationForm"%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			PopulationInformationForm populationInformationForm = (PopulationInformationForm)request.getAttribute("populationform");
            String param = PortletRendererUtil.portletParameter(prr, "org.apache.struts.taglib.html.CANCEL");
	        NameValue[] linkParams       = new NameValue[1];
	        
	        String facilityId = request.getParameter("facilityId");
	        String displayClusteredDetailsUrl = CWNSStrutsUtils.createHref(request,"clusteredSystemDetails.do");
	        String displayOWTSDetailsUrl = CWNSStrutsUtils.createHref(request,"owtSystemDetails.do");
			linkParams[0] = new NameValue("facilityId", facilityId);	

			int resPresentTotal =0;
			int resProjectedTotal = 0;
			int nonResPresentTotal = 0;
			int nonResProjectedTotal = 0;
			
			String eventPopupWinowUrl = "";
			
%>
<SCRIPT type=text/javascript>
function PopulationConfirmAndSubmit(){
  
  var resRecPresentPopCntText = window.document.getElementById("resRecPresentPopCnt")
  var nonResRecPresentPopCntText = document.getElementById("nonResRecPresentPopCnt");
  var resRecProjectedPopCntText = window.document.getElementById("resRecProjectedPopCnt")
  var nonResRecProjectedPopCntText = document.getElementById("nonResRecProjectedPopCnt");
  <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionPresent" value="Y">
    if(resRecPresentPopCntText.value <= 0 && nonResRecPresentPopCntText.value <= 0){
      alert("Residential and/or Non-residential present Receiving Collection Population is required");
      return;
    }
  </logic:equal>
  <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
    if(resRecProjectedPopCntText.value <= 0 && nonResRecProjectedPopCntText.value <= 0){
      alert("Residential and/or Non-residential projected Receiving Collection Population is required");
      return;
    }
  </logic:equal>
   
  var formId = window.document.getElementById("PopulationInformationFormBeanId");
  
  if(formId != undefined)
  {
    if(validatePopulationInformationFormBean(formId) == false)
    {
      return;
    }
  }
  
  window.document.getElementById("resRecPresentPopCnt").disabled=false;
       hidden_field = window.document.getElementById("populationAct");
	    hidden_field.value="save";
	    window.document.PopulationInformationFormBean.submit();
	    return true;
}

function ClusteredSystemSubmit(){
  var resPopulationPerHouse = document.getElementById("resPopulationPerHouse").value;
  var nonResPopulationPerHouse = document.getElementById("nonResPopulationPerHouse").value;
  var presentResClusteredHouses = document.getElementById("presentResClusteredHouses").value;
  var projectedResClusteredHouses = document.getElementById("projectedResClusteredHouses").value;
  var presentNonResClusteredHouses = window.document.getElementById("presentNonResClusteredHouses").value;
  var projectedNonResClusteredHouses = document.getElementById("projectedNonResClusteredHouses").value;
  <logic:equal name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
    if(presentResClusteredHouses > 0 || projectedResClusteredHouses > 0){
      if(resPopulationPerHouse <= 0){
        alert("Residential Population per unit is required");
        return;
      }
    }
    if(presentNonResClusteredHouses > 0 || projectedNonResClusteredHouses > 0){
      if(nonResPopulationPerHouse <= 0){
        alert("Non Residential Population per unit is required");
        return;
      }
    }  
  </logic:equal>
  
var formId = window.document.getElementById("PopulationInformationFormBeanId1");
  
  if(formId != undefined)
  {
    if(validatePopulationInformationFormBean1(formId) == false)
    {
      return;
    }
  }
  hidden_field = window.document.getElementById("clusteredAct");
  hidden_field.value="save";
  window.document.PopulationInformationFormBean1.submit();
  return true;
}

function ClusteredSystemCancel(){
  var hidden_field = document.getElementById('cancelParamId');
  hidden_field.name = "<%=param %>";
  hidden_field.value = "Cancel";
  window.document.PopulationInformationFormBean1.submit();
  return true;
}

function OWTSystemSubmit(){
  var resOWTSPopulationPerHouse = document.getElementById("resOWTSPopulationPerHouse").value;
  var nonResOWTSPopulationPerHouse = document.getElementById("nonResOWTSPopulationPerHouse").value;
  var presentResOWTSHouses = document.getElementById("presentResOWTSHouses").value;
  var projectedResOWTSHouses = document.getElementById("projectedResOWTSHouses").value;
  var presentNonResOWTSHouses = document.getElementById("presentNonResOWTSHouses").value;
  var projectedNonResOWTSHouses = document.getElementById("projectedNonResOWTSHouses").value;
  
  <logic:equal name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
    if(presentResOWTSHouses > 0 || projectedResOWTSHouses > 0){
      if(resOWTSPopulationPerHouse <= 0){
        alert("Residential Population per unit is required");
        return;
      }
    }
    if(presentNonResOWTSHouses > 0 || projectedNonResOWTSHouses > 0){
      if(nonResOWTSPopulationPerHouse <= 0){
        alert("Non Residential Population per unit is required");
        return;
      }
    }  
  </logic:equal>

var formId = window.document.getElementById("PopulationInformationFormBeanId2");
  
  if(formId != undefined)
  {
    if(validatePopulationInformationFormBean2(formId) == false)
    {
      return;
    }
  }
  hidden_field = window.document.getElementById("owtsAct");
  hidden_field.value="save";
  window.document.PopulationInformationFormBean2.submit();
  return true;
}

function OWTSystemCancel(){
  var hidden_field = document.getElementById('cancelParamId');
  hidden_field.name = "<%=param %>";
  hidden_field.value = "Cancel";
  window.document.PopulationInformationFormBean2.submit();
  return true;
}

function PopulationCancel()
{
   window.document.PopulationInformationFormBean.reset();
   tallyAllFields("resRecPresentPopCnt");
   tallyAllFields("resRecProjectedPopCnt");
   tallyAllFields("nonResRecPresentPopCnt");
   tallyAllFields("nonResRecProjectedPopCnt");  
}

 function showUpstreamCollection(url){
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

function convStrToInt(str)
{
  if(str == null || str == "")
    return 0;
  else
    return parseInt(str);
}
function populationConvertZeroToBlank(intNum)
{
  if(isNaN(intNum) || intNum == 0)
    return "";
  else
    return intNum + "";
}

function checkProjected(elemId, yearId)
{
  var projectElem = document.getElementById(elemId);
  var yearElem = document.getElementById(yearId);
  
  if(projectElem != null)
  {
   if(projectElem.value == "" || isNaN(convStrToInt(projectElem.value)) || convStrToInt(projectElem.value) == 0)
   {
    if(yearElem !=null)
      { 
        yearElem.value="";
        yearElem.disabled = true;
        yearElem.className = 'disabledField';
      }
   }
   else
   {
     if(yearElem !=null)
       yearElem.disabled = false;
       yearElem.className = '';
   }
    
  }
}

function tallyAllFields(elemId)
{
  var elemIdField = document.getElementById(elemId);
  var value = elemIdField.value;
  var tempArray = value.split('.');
  if (tempArray.length > 2){
     alert("not valid")
     return;
  }          
  else {   
  var joinedString= tempArray.join('');
  }
  if(!isAllDigits(joinedString))
  {
  	alert("This field accepts only integers.");
  	elemIdField.focus();
  	return;
  }
  else if(elemIdField.value < 0)
  {
  	alert("This field accepts only positive integers.");
  	elemIdField.focus();
  	return;
  }
  if(elemId == "presentResClusteredHouses")
  {
    var presentResClusteredHouses = document.getElementById("presentResClusteredHouses").value;
    var presentNonResClusteredHousesReqText = document.getElementById("presentNonResClusteredHousesReqSpan");
    if (presentResClusteredHouses > 0){
       presentNonResClusteredHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       presentNonResClusteredHousesReqText.innerHTML = "*";
    }
    var populationPerHouse = document.getElementById("resPopulationPerHouse").value;
    //var populationPerHouse = <%=populationInformationForm.getResPopulationPerHouse()%>
    var resClusteredPresentPopCntSpan = document.getElementById("resClusteredPresentPopCntSpan");
    
    var resClusteredPresentPopCnt = Math.round(presentResClusteredHouses * populationPerHouse);
    resClusteredPresentPopCntSpan.innerHTML = ""+resClusteredPresentPopCnt+"";
  }
  if(elemId == "projectedResClusteredHouses")
  {
    var projectedResClusteredHouses = document.getElementById("projectedResClusteredHouses").value;
    var projectedNonResClusteredHousesReqText = document.getElementById("projectedNonResClusteredHousesReqSpan");
    if (projectedResClusteredHouses > 0){
       projectedNonResClusteredHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       projectedNonResClusteredHousesReqText.innerHTML = "*";
    }
    //var populationPerHouse = <%=populationInformationForm.getResPopulationPerHouse()%>
    var populationPerHouse = document.getElementById("resPopulationPerHouse").value;
    var resClusteredProjectedPopCntSpan = document.getElementById("resClusteredProjectedPopCntSpan");
    
    var resClusteredProjectedPopCnt = Math.round(projectedResClusteredHouses * populationPerHouse);
    resClusteredProjectedPopCntSpan.innerHTML = ""+resClusteredProjectedPopCnt+"";
  }
  if(elemId == "presentNonResClusteredHouses")
  {
    var presentNonResClusteredHouses = document.getElementById("presentNonResClusteredHouses").value;
    var presentResClusteredHousesReqText = document.getElementById("presentResClusteredHousesReqSpan");
    if (presentNonResClusteredHouses > 0){
       presentResClusteredHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       presentResClusteredHousesReqText.innerHTML = "*";
    }
    //var populationPerHouse = <%=populationInformationForm.getNonResPopulationPerHouse()%>
    var populationPerHouse = document.getElementById("nonResPopulationPerHouse").value;
    var nonResClusteredPresentPopCntSpan = document.getElementById("nonResClusteredPresentPopCntSpan");
    
    var nonResClusteredPresentPopCnt = Math.round(presentNonResClusteredHouses * populationPerHouse);
    nonResClusteredPresentPopCntSpan.innerHTML = ""+nonResClusteredPresentPopCnt+"";
  }
  if(elemId == "projectedNonResClusteredHouses")
  {
    var projectedNonResClusteredHouses = document.getElementById("projectedNonResClusteredHouses").value;
    var projectedResClusteredHousesReqText = document.getElementById("projectedResClusteredHousesReqSpan");
    if (projectedNonResClusteredHouses > 0){
       projectedResClusteredHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       projectedResClusteredHousesReqText.innerHTML = "*";
    }
    //var populationPerHouse = <%=populationInformationForm.getNonResPopulationPerHouse()%>
     var populationPerHouse = document.getElementById("nonResPopulationPerHouse").value;
    var nonResClusteredProjectedPopCntSpan = document.getElementById("nonResClusteredProjectedPopCntSpan");
    
    var nonResClusteredProjectedPopCnt = Math.round(projectedNonResClusteredHouses * populationPerHouse);
    nonResClusteredProjectedPopCntSpan.innerHTML = ""+nonResClusteredProjectedPopCnt+"";
  }
  if(elemId == "resPopulationPerHouse")
  {
    var resPopulationPerHouse = document.getElementById("resPopulationPerHouse").value;
    var nonResPopulationPerHouseReqText = document.getElementById("nonResPopulationPerHouseReqSpan");
    if (resPopulationPerHouse > 0){
       nonResPopulationPerHouseReqText.innerHTML = "&nbsp;";
    }
    else{
       nonResPopulationPerHouseReqText.innerHTML = "*";
    }
    var presentResClusteredHouses = document.getElementById("presentResClusteredHouses").value;
    var resClusteredPresentPopCntSpan = document.getElementById("resClusteredPresentPopCntSpan");
    var resClusteredPresentPopCnt = Math.round(presentResClusteredHouses * resPopulationPerHouse);
    resClusteredPresentPopCntSpan.innerHTML = ""+resClusteredPresentPopCnt+"";
    
    var projectedResClusteredHouses = document.getElementById("projectedResClusteredHouses").value;
    var resClusteredProjectedPopCntSpan = document.getElementById("resClusteredProjectedPopCntSpan");
    var resClusteredProjectedPopCnt = Math.round(projectedResClusteredHouses * resPopulationPerHouse);
    resClusteredProjectedPopCntSpan.innerHTML = ""+resClusteredProjectedPopCnt+"";
  
  }
  if(elemId == "nonResPopulationPerHouse")
  {
    var nonResPopulationPerHouse = document.getElementById("nonResPopulationPerHouse").value;
    var resPopulationPerHouseReqText = document.getElementById("resPopulationPerHouseReqSpan");
    if (nonResPopulationPerHouse > 0){
      resPopulationPerHouseReqText.innerHTML = "&nbsp;";
    }
    else{
      resPopulationPerHouseReqText.innerHTML = "*"; 
    }
    var presentNonResClusteredHouses = document.getElementById("presentNonResClusteredHouses").value;
    var nonResClusteredPresentPopCntSpan = document.getElementById("nonResClusteredPresentPopCntSpan");
    var nonResClusteredPresentPopCnt = Math.round(presentNonResClusteredHouses * nonResPopulationPerHouse);
    nonResClusteredPresentPopCntSpan.innerHTML = ""+nonResClusteredPresentPopCnt+"";
    
    var projectedNonResClusteredHouses = document.getElementById("projectedNonResClusteredHouses").value;
    var nonResClusteredProjectedPopCntSpan = document.getElementById("nonResClusteredProjectedPopCntSpan");
    var nonResClusteredProjectedPopCnt = Math.round(projectedNonResClusteredHouses * nonResPopulationPerHouse);
    nonResClusteredProjectedPopCntSpan.innerHTML = ""+nonResClusteredProjectedPopCnt+"";
  }
  // OWTS
  if(elemId == "presentResOWTSHouses")
  {
    var presentResOWTSHouses = document.getElementById("presentResOWTSHouses").value;
    var presentNonResOWTSHousesReqText = document.getElementById("presentNonResOWTSHousesReqSpan");
    if (presentResOWTSHouses > 0){
       presentNonResOWTSHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       presentNonResOWTSHousesReqText.innerHTML = "*";
    }
    var populationPerHouse = document.getElementById("resOWTSPopulationPerHouse").value;
    var resOWTSPresentPopCntSpan = document.getElementById("resOWTSPresentPopCntSpan");
    var resOWTSPresentPopCnt = Math.round(presentResOWTSHouses * populationPerHouse);
    resOWTSPresentPopCntSpan.innerHTML = ""+resOWTSPresentPopCnt+"";
  }
  if(elemId == "projectedResOWTSHouses")
  {
    var projectedResOWTSHouses = document.getElementById("projectedResOWTSHouses").value;
    var projectedNonResOWTSHousesReqText = document.getElementById("projectedNonResOWTSHousesReqSpan");
    if (projectedResOWTSHouses > 0){
       projectedNonResOWTSHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       projectedNonResOWTSHousesReqText.innerHTML = "*";
    }
    var populationPerHouse = document.getElementById("resOWTSPopulationPerHouse").value;
    var resOWTSProjectedPopCntSpan = document.getElementById("resOWTSProjectedPopCntSpan");
    var resOWTSProjectedPopCnt = Math.round(projectedResOWTSHouses * populationPerHouse);
    resOWTSProjectedPopCntSpan.innerHTML = ""+resOWTSProjectedPopCnt+"";
  }
  
  if(elemId == "presentNonResOWTSHouses")
  {
    var presentNonResOWTSHouses = document.getElementById("presentNonResOWTSHouses").value;
    var presentResOWTSHousesReqText = document.getElementById("presentResOWTSHousesReqSpan");
    if (presentNonResOWTSHouses > 0){
       presentResOWTSHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       presentResOWTSHousesReqText.innerHTML = "*";
    }
    var populationPerHouse = document.getElementById("nonResOWTSPopulationPerHouse").value;
    var nonResOWTSPresentPopCntSpan = document.getElementById("nonResOWTSPresentPopCntSpan");
    var nonResOWTSPresentPopCnt = Math.round(presentNonResOWTSHouses * populationPerHouse);
    nonResOWTSPresentPopCntSpan.innerHTML = ""+nonResOWTSPresentPopCnt+"";
  }
  if(elemId == "projectedNonResOWTSHouses")
  {
    var projectedNonResOWTSHouses = document.getElementById("projectedNonResOWTSHouses").value;
    var projectedResOWTSHousesReqText = document.getElementById("projectedResOWTSHousesReqSpan");
    if (projectedNonResOWTSHouses > 0){
       projectedResOWTSHousesReqText.innerHTML = "&nbsp;";
    }
    else{
       projectedResOWTSHousesReqText.innerHTML = "*";
    }
    var populationPerHouse = document.getElementById("nonResOWTSPopulationPerHouse").value;
    var nonResOWTSProjectedPopCntSpan = document.getElementById("nonResOWTSProjectedPopCntSpan");
    var nonResOWTSProjectedPopCnt = Math.round(projectedNonResOWTSHouses * populationPerHouse);
    nonResOWTSProjectedPopCntSpan.innerHTML = ""+nonResOWTSProjectedPopCnt+"";
  }
  
  if(elemId == "resOWTSPopulationPerHouse")
  {
    var resPopulationPerHouse = document.getElementById("resOWTSPopulationPerHouse").value;
    var nonResOWTSPopulationPerHouseReqText = document.getElementById("nonResOWTSPopulationPerHouseReqSpan");
    if (resPopulationPerHouse > 0){
      nonResOWTSPopulationPerHouseReqText.innerHTML = "&nbsp;";
    }
    else{
      nonResOWTSPopulationPerHouseReqText.innerHTML = "*";
    }
    var presentResOWTSHouses = document.getElementById("presentResOWTSHouses").value;
    var resOWTSPresentPopCntSpan = document.getElementById("resOWTSPresentPopCntSpan");
    var resOWTSPresentPopCnt = Math.round(presentResOWTSHouses * resPopulationPerHouse);
    resOWTSPresentPopCntSpan.innerHTML = ""+resOWTSPresentPopCnt+"";
    
    var projectedResOWTSHouses = document.getElementById("projectedResOWTSHouses").value;
    var resOWTSProjectedPopCntSpan = document.getElementById("resOWTSProjectedPopCntSpan");
    var resOWTSProjectedPopCnt = Math.round(projectedResOWTSHouses * resPopulationPerHouse);
    resOWTSProjectedPopCntSpan.innerHTML = ""+resOWTSProjectedPopCnt+"";
  
  }
  if(elemId == "nonResOWTSPopulationPerHouse")
  {
    var nonResPopulationPerHouse = document.getElementById("nonResOWTSPopulationPerHouse").value;
    var resOWTSPopulationPerHouseReqText = document.getElementById("resOWTSPopulationPerHouseReqSpan");
    if (nonResPopulationPerHouse > 0){
       resOWTSPopulationPerHouseReqText.innerHTML = "&nbsp;";
    }
    else{
       resOWTSPopulationPerHouseReqText.innerHTML = "*";
    }
    var presentNonResOWTSHouses = document.getElementById("presentNonResOWTSHouses").value;
    var nonResOWTSPresentPopCntSpan = document.getElementById("nonResOWTSPresentPopCntSpan");
    var nonResOWTSPresentPopCnt = Math.round(presentNonResOWTSHouses * nonResPopulationPerHouse);
    nonResOWTSPresentPopCntSpan.innerHTML = ""+nonResOWTSPresentPopCnt+"";
    
    var projectedNonResOWTSHouses = document.getElementById("projectedNonResOWTSHouses").value;
    var nonResOWTSProjectedPopCntSpan = document.getElementById("nonResOWTSProjectedPopCntSpan");
    var nonResOWTSProjectedPopCnt = Math.round(projectedNonResOWTSHouses * nonResPopulationPerHouse);
    nonResOWTSProjectedPopCntSpan.innerHTML = ""+nonResOWTSProjectedPopCnt+"";
  }
  
  if(elemId == "resRecPresentPopCnt"){
    var resRecPresentPopCntText = document.getElementById("resRecPresentPopCnt");
    var nonResRecPresentPopRequiredText = document.getElementById("nonResRecPresentPopRequiredSpan");
    if (resRecPresentPopCntText.value>0){
       nonResRecPresentPopRequiredText.innerHTML = "&nbsp;";
    }
    else {
       nonResRecPresentPopRequiredText.innerHTML = "*";
    }
    
  }
  
  if(elemId == "resRecProjectedPopCnt"){
    var resRecProjectedPopCntText = document.getElementById("resRecProjectedPopCnt");
    var nonResRecProjectedPopRequiredText = document.getElementById("nonResRecProjectedPopRequiredSpan");
    if (resRecProjectedPopCntText.value>0){
       nonResRecProjectedPopRequiredText.innerHTML = "&nbsp;";
    }
    else{
       nonResRecProjectedPopRequiredText.innerHTML = "*";
    }
    
  }
  
  if(elemId == "nonResRecPresentPopCnt"){
    var nonResRecPresentPopCntText = document.getElementById("nonResRecPresentPopCnt");
    var resRecPresentPopRequiredText = document.getElementById("resRecPresentPopRequiredSpan");
    if (nonResRecPresentPopCntText.value>0){
       resRecPresentPopRequiredText.innerHTML = "&nbsp;";
    }
    else{
       resRecPresentPopRequiredText.innerHTML = "*";
    }
    
  }
  
  if(elemId == "nonResRecProjectedPopCnt"){
    var nonResRecProjectedPopCntText = document.getElementById("nonResRecProjectedPopCnt");
    var resRecProjectedPopRequiredText = document.getElementById("resRecProjectedPopRequiredSpan");
    if (nonResRecProjectedPopCntText.value>0){
      resRecProjectedPopRequiredText.innerHTML = "&nbsp;";
    }
    else{
      resRecProjectedPopRequiredText.innerHTML = "*";
    }
  }
   
  if(elemId == "resRecPresentPopCnt" || elemId == "resDecPresentPopCnt" || elemId == "resISDSPresentPopCnt")
  {
     var upstreamText = document.getElementById("upstreamPresentResPopulation");
     var resRecPresentPopCntText = document.getElementById("resRecPresentPopCnt");
     var resDecPresentPopCntText = document.getElementById("resDecPresentPopCnt");
     var resISDSPresentPopCntText = document.getElementById("resISDSPresentPopCnt");
     
     var totalRecTreatmentPresentResPopText = document.getElementById("totalRecTreatmentPresentResPopulation");
     var totalRecTreatmentPresentResPopSpan = document.getElementById("totalRecTreatmentPresentResPopulationSpan");

     var totalPresentResPopulationText = document.getElementById("totalPresentResPopulation");
     var totalPresentResPopulationSpan = document.getElementById("totalPresentResPopulationSpan");

     totalRecTreatmentPresentResPopText.value = 
           convStrToInt(resRecPresentPopCntText.value) + 
           convStrToInt(upstreamText.value);

     totalRecTreatmentPresentResPopSpan.innerHTML = "&nbsp;" + populationConvertZeroToBlank(totalRecTreatmentPresentResPopText.value) + "";
     
     totalPresentResPopulationText.value = 
           convStrToInt(resRecPresentPopCntText.value) + 
           convStrToInt(resDecPresentPopCntText.innerHTML) +
           convStrToInt(resISDSPresentPopCntText.innerHTML);
          
     totalPresentResPopulationSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalPresentResPopulationText.value) + "";
  }else if(elemId == "resRecProjectedPopCnt" || elemId == "resDecProjectedPopCnt" || elemId == "resISDSProjectedPopCnt")
  {
     var upstreamText = document.getElementById("upstreamProjectedResPopulation");
     var resRecProjectedPopCntText = document.getElementById("resRecProjectedPopCnt");
     var resDecProjectedPopCntText = document.getElementById("resDecProjectedPopCnt");
     var resISDSProjectedPopCntText = document.getElementById("resISDSProjectedPopCnt");
     
     var totalRecTreatmentProjectedResPopText = document.getElementById("totalRecTreatmentProjectedResPopulation");
     var totalRecTreatmentProjectedResPopSpan = document.getElementById("totalRecTreatmentProjectedResPopulationSpan");

     var totalProjectedResPopulationText = document.getElementById("totalProjectedResPopulation");
     var totalProjectedResPopulationSpan = document.getElementById("totalProjectedResPopulationSpan");

     totalRecTreatmentProjectedResPopText.value = 
           convStrToInt(resRecProjectedPopCntText.value) + 
           convStrToInt(upstreamText.value);

     totalRecTreatmentProjectedResPopSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalRecTreatmentProjectedResPopText.value) + "";
     
     totalProjectedResPopulationText.value = 
           convStrToInt(resRecProjectedPopCntText.value) + 
           convStrToInt(resDecProjectedPopCntText.innerHTML) +
           convStrToInt(resISDSProjectedPopCntText.innerHTML);
          
     totalProjectedResPopulationSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalProjectedResPopulationText.value) + "";
  }else if(elemId == "nonResRecPresentPopCnt" || elemId == "nonResDecPresentPopCnt" || elemId == "nonResISDSPresentPopCnt")
  {
     var upstreamText = document.getElementById("upstreamPresentNonResPopulation");
     var nonResRecPresentPopCntText = document.getElementById("nonResRecPresentPopCnt");
     var nonResDecPresentPopCntText = document.getElementById("nonResDecPresentPopCnt");
     var nonResISDSPresentPopCntText = document.getElementById("nonResISDSPresentPopCnt");
     
     var totalRecTreatmentPresentNonResPopText = document.getElementById("totalRecTreatmentPresentNonResPopulation");
     var totalRecTreatmentPresentNonResPopSpan = document.getElementById("totalRecTreatmentPresentNonResPopulationSpan");

     var totalPresentNonResPopulationText = document.getElementById("totalPresentNonResPopulation");
     var totalPresentNonResPopulationSpan = document.getElementById("totalPresentNonResPopulationSpan");

     totalRecTreatmentPresentNonResPopText.value = 
           convStrToInt(nonResRecPresentPopCntText.value) + 
           convStrToInt(upstreamText.value);

     totalRecTreatmentPresentNonResPopSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalRecTreatmentPresentNonResPopText.value) + "";
     
     totalPresentNonResPopulationText.value = 
           convStrToInt(nonResRecPresentPopCntText.value) + 
           convStrToInt(nonResDecPresentPopCntText.innerHTML) +
           convStrToInt(nonResISDSPresentPopCntText.innerHTML);
          
     totalPresentNonResPopulationSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalPresentNonResPopulationText.value) + "";
  } else if(elemId == "nonResRecProjectedPopCnt" || elemId == "nonResDecProjectedPopCnt" || elemId == "nonResISDSProjectedPopCnt")
  {
     var upstreamText = document.getElementById("upstreamProjectedNonResPopulation");
     var nonResRecProjectedPopCntText = document.getElementById("nonResRecProjectedPopCnt");
     var nonResDecProjectedPopCntText = document.getElementById("nonResDecProjectedPopCnt");
     var nonResISDSProjectedPopCntText = document.getElementById("nonResISDSProjectedPopCnt");
     
     var totalRecTreatmentProjectedNonResPopText = document.getElementById("totalRecTreatmentProjectedNonResPopulation");
     var totalRecTreatmentProjectedNonResPopSpan = document.getElementById("totalRecTreatmentProjectedNonResPopulationSpan");

     var totalProjectedNonResPopulationText = document.getElementById("totalProjectedNonResPopulation");
     var totalProjectedNonResPopulationSpan = document.getElementById("totalProjectedNonResPopulationSpan");

     totalRecTreatmentProjectedNonResPopText.value = 
           convStrToInt(nonResRecProjectedPopCntText.value) + 
           convStrToInt(upstreamText.value);

     totalRecTreatmentProjectedNonResPopSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalRecTreatmentProjectedNonResPopText.value) + "";
     
     totalProjectedNonResPopulationText.value = 
           convStrToInt(nonResRecProjectedPopCntText.value) + 
           convStrToInt(nonResDecProjectedPopCntText.innerHTML) +
           convStrToInt(nonResISDSProjectedPopCntText.innerHTML);
          
     totalProjectedNonResPopulationSpan.innerHTML =  "&nbsp;" + populationConvertZeroToBlank(totalProjectedNonResPopulationText.value) + "";
  }
  
  
}

</SCRIPT>
<pdk-html:form name="PopulationInformationFormBean" styleId="PopulationInformationFormBeanId"
                type="gov.epa.owm.mtb.cwns.populationInformation.PopulationInformationForm" 
                action="population.do">
<DIV id="hidden_fields" style="DISPLAY:none">	
	<pdk-html:text styleId="populationAct" property="populationAct" value=""/> 
	<pdk-html:text styleId="surveyFacilityId" property="surveyFacilityId" value="<%=populationInformationForm.getSurveyFacilityId()%>"/>
</DIV>	
<logic:present name="errorMessages">
<div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="errorMessage" name="errorMessages" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/err24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=errorMessage %>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>
</logic:present>

<TABLE border="0" width="100%" bgcolor="#ffffff" cellspacing="0"
	cellpadding="0" bordercolor="#808080">
	<TR>
		<TD valign="bottom">
			<TABLE border="0" width="100%" class="PortletText1" cellspacing="3"
				cellpadding="1" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
				<TR class="PortletHeaderColor">
					<TD></TD>
					<TD colspan="3" align="center" class="PortletHeaderText">
						Resident Population
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderText">
						Non- Resident Population
					</TD>
				</TR>
				<TR>
					<TD>
						&nbsp; &nbsp;
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Projected</FONT>
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Projected Year</FONT>
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Projected</FONT>
					</TD>
					<TD align="center" class="PortletHeaderColor">
						<FONT color="#ffffff">Projected Year</FONT>
					</TD>
				</TR>

		<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
				
				<TR class="PortletSubHeaderColor">
					<TD>
						Receiving Collection
					</TD>
					<TD align="right">
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResRecPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionResPresent" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="nonResRecPresentPopCnt" value="0">
						     <span id="resRecPresentPopRequiredSpan"  class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="nonResRecPresentPopCnt" value="0">
						     <span id="resRecPresentPopRequiredSpan"  class="required">&nbsp;</span>
						   </logic:greaterThan>
					    					       
					       <pdk-html:text onblur="tallyAllFields(this.id)"  name="PopulationInformationFormBean" 
					       property="resRecPresentPopCnt" styleId="resRecPresentPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecPresentPopCnt())%>" 
					       size="10" maxlength="8" />
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionResPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resRecPresentPopCnt" styleId="resRecPresentPopCnt" 
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecPresentPopCnt())%>" 
					        disabled="true" styleClass="disabledField"
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD align="right">
					         <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResRecProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <logic:lessEqual name="PopulationInformationFormBean" property="nonResRecProjectedPopCnt" value="0">
						     <span id="resRecProjectedPopRequiredSpan" class="required">*</span>
						    </logic:lessEqual>
						    <logic:greaterThan name="PopulationInformationFormBean" property="nonResRecProjectedPopCnt" value="0">
						     <span id="resRecProjectedPopRequiredSpan" class="required">&nbsp;</span>
						    </logic:greaterThan>
					      					        
					        <pdk-html:text onblur="checkProjected(this.id, 'resRecProjectedYear');tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resRecProjectedPopCnt" styleId="resRecProjectedPopCnt"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedPopCnt())%>" 
					         size="10" maxlength="8"/>
					    </logic:equal>
					    <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resRecProjectedPopCnt" styleId="resRecProjectedPopCnt"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedPopCnt())%>" 
					         disabled="true" styleClass="disabledField"
					         size="10" maxlength="8"/>
					    </logic:notEqual>    
					</TD>
					<TD align="center">
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resRecProjectedYear" styleId="resRecProjectedYear"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedYear())%>" 
					         size="10" maxlength="4"/>
					    </logic:equal>
					    <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resRecProjectedYear" styleId="resRecProjectedYear"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedYear())%>"
					         disabled="true" styleClass="disabledField"
					         size="10" maxlength="4"/>
					    </logic:notEqual>
					    
					</TD>
					<TD align="right">
					        <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResRecPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionPresent" value="Y">
						   <logic:lessEqual name="PopulationInformationFormBean" property="resRecPresentPopCnt" value="0">
						     <span id="nonResRecPresentPopRequiredSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="resRecPresentPopCnt" value="0">
						     <span id="nonResRecPresentPopRequiredSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
						   <pdk-html:text onblur="tallyAllFields(this.id)"  
						   name="PopulationInformationFormBean" property="nonResRecPresentPopCnt" styleId="nonResRecPresentPopCnt" 
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecPresentPopCnt())%>"
					        size="10" maxlength="8"/>
					    </logic:equal> 
					    <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionPresent" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResRecPresentPopCnt" styleId="nonResRecPresentPopCnt" 
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecPresentPopCnt())%>" 
					        disabled="true" styleClass="disabledField"
					        size="10" maxlength="8"/>
					    </logic:notEqual>   
					</TD>
					<TD align="right">
					        <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResRecProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="resRecProjectedPopCnt" value="0">
						     <span id="nonResRecProjectedPopRequiredSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="resRecProjectedPopCnt" value="0">
						     <span id="nonResRecProjectedPopRequiredSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="checkProjected(this.id, 'nonResRecProjectedYear');tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResRecProjectedPopCnt" styleId="nonResRecProjectedPopCnt" 
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedPopCnt())%>"					        
					        size="10" maxlength="8"/>
					    </logic:equal>
					    <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResRecProjectedPopCnt" styleId="nonResRecProjectedPopCnt" 
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedPopCnt())%>" 
					        disabled="true" styleClass="disabledField"
					        size="10" maxlength="8"/> 
					    </logic:notEqual>    
					</TD>
					<TD align="center">
					    <logic:equal name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResRecProjectedYear" styleId="nonResRecProjectedYear"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedYear())%>" 
					         size="10" maxlength="4"/>
					    </logic:equal>
					    <logic:notEqual name="PopulationInformationFormBean" property="enableRecCollectionProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResRecProjectedYear" styleId="nonResRecProjectedYear"
					         value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedYear())%>"
					         disabled="true" styleClass="disabledField"
					         size="10" maxlength="4"/>
					    </logic:notEqual>
					</TD>    
				</TR>
				<TR>
					<TD>
						Upstream Collection
						<logic:equal name="PopulationInformationFormBean" property="hasUpstream" value="Y">
						<%
							eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"UpsreamCollectionPage", linkParams,true, true);
						%>
						<A href="javascript:void(0);" onclick='showUpstreamCollection("<%=eventPopupWinowUrl%>")'> details </A>
						</logic:equal>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="upstreamPresentResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentResPopulation())%>">
							<span align="left" id="upstreamPresentResPopulationSpan">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="upstreamProjectedResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedResPopulation())%>">
							<span align="left" id="upstreamProjectedResPopulationSpan">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="upstreamPresentNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentNonResPopulation())%>">
							<span align="left" id="upstreamPresentNonResPopulationSpan">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentNonResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="upstreamProjectedNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedNonResPopulation())%>">
							<span align="left" id="upstreamProjectedNonResPopulationSpan">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedNonResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>

					</TD>
				</TR>
				<TR class="PortletSubHeaderColor">
					<TD>
						<STRONG> Total Receiving Treatment </STRONG>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalRecTreatmentPresentResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(resPresentTotal+populationInformationForm.getUpstreamPresentResPopulation())%>">
							<STRONG><span align="left" id="totalRecTreatmentPresentResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(resPresentTotal+populationInformationForm.getUpstreamPresentResPopulation()));%>						
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalRecTreatmentProjectedResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(resProjectedTotal +populationInformationForm.getUpstreamProjectedResPopulation())%>">
							<STRONG><span align="left" id="totalRecTreatmentProjectedResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(resProjectedTotal +populationInformationForm.getUpstreamProjectedResPopulation()));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalRecTreatmentPresentNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(nonResPresentTotal+populationInformationForm.getUpstreamPresentNonResPopulation())%>">
							<STRONG><span align="left" id="totalRecTreatmentPresentNonResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(nonResPresentTotal+populationInformationForm.getUpstreamPresentNonResPopulation()));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalRecTreatmentProjectedNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(nonResProjectedTotal+populationInformationForm.getUpstreamProjectedNonResPopulation())%>">
							<STRONG><span align="left" id="totalRecTreatmentProjectedNonResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(nonResProjectedTotal+populationInformationForm.getUpstreamProjectedNonResPopulation()));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
				</TR>
				<%-- 
				<TR>
				    <TD>
						Decentralized Systems
					</TD>
					
					<TD>
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResDecPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resDecPresentPopCnt" styleId="resDecPresentPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecPresentPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resDecPresentPopCnt" styleId="resDecPresentPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecPresentPopCnt())%>" 
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResDecProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text onblur="checkProjected(this.id, 'resDecProjectedYear');tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resDecProjectedPopCnt" styleId="resDecProjectedPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resDecProjectedPopCnt" styleId="resDecProjectedPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedPopCnt())%>"  
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="resDecProjectedYear" styleId="resDecProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>" 
					       size="10" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resDecProjectedYear" styleId="resDecProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>"
					        disabled="true" 
					        size="10" maxlength="4"/>
					     </logic:notEqual>
						
					</TD>
					<TD>
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResDecPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResDecPresentPopCnt" styleId="nonResDecPresentPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecPresentPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResDecPresentPopCnt" styleId="nonResDecPresentPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecPresentPopCnt())%>" 
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResDecProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text onblur="checkProjected(this.id, 'nonResDecProjectedYear');tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResDecProjectedPopCnt" styleId="nonResDecProjectedPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResDecProjectedPopCnt" styleId="nonResDecProjectedPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedPopCnt())%>"  
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="nonResDecProjectedYear" styleId="nonResDecProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>" 
					       size="10" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResDecProjectedYear" styleId="nonResDecProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>"
					        disabled="true" 
					        size="10" maxlength="4"/>
					     </logic:notEqual>
					</TD>
				</TR>
				<TR>
					<TD>
						Individual Sewage Disposal Systems
					</TD>
					<TD>
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResISDSPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resISDSPresentPopCnt" styleId="resISDSPresentPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSPresentPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resISDSPresentPopCnt" styleId="resISDSPresentPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSPresentPopCnt())%>" 
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResISDSProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text onblur="checkProjected(this.id, 'resISDSProjectedYear');tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resISDSProjectedPopCnt" styleId="resISDSProjectedPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resISDSProjectedPopCnt" styleId="resISDSProjectedPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedPopCnt())%>"  
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="resISDSProjectedYear" styleId="resISDSProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>" 
					       size="10" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resISDSProjectedYear" styleId="resISDSProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>" 
					        disabled="true" 
					        size="10" maxlength="4"/>
					     </logic:notEqual>
					</TD>
					<TD>
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResISDSPresentPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResISDSPresentPopCnt" styleId="nonResISDSPresentPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSPresentPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResISDSPresentPopCnt" styleId="nonResISDSPresentPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSPresentPopCnt())%>"  
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResISDSProjectedPopCnt(); %>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text onblur="checkProjected(this.id, 'nonResISDSProjectedYear');tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResISDSProjectedPopCnt" styleId="nonResISDSProjectedPopCnt"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedPopCnt())%>" 
					       size="10" maxlength="8"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResISDSProjectedPopCnt" styleId="nonResISDSProjectedPopCnt"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedPopCnt())%>" 
					        disabled="true" 
					        size="10" maxlength="8"/>
					     </logic:notEqual>  
					</TD>
					<TD>
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="nonResISDSProjectedYear" styleId="nonResISDSProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>" 
					       size="10" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResISDSProjectedYear" styleId="nonResISDSProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>"
					        disabled="true" 
					        size="10" maxlength="4"/>
					     </logic:notEqual>
					</TD>
				</TR>
				--%>
				
				<TR>
				    <TD>
						Clustered Systems&nbsp; 
						<logic:equal name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
						<A href="<%=displayClusteredDetailsUrl%>">details</A> 
						</logic:equal>
					</TD>
					<TD>
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResDecPresentPopCnt(); %>
					       <P align="right">
					       <span align="left" id="resDecPresentPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecPresentPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD>
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResDecProjectedPopCnt(); %>
					       <P align="right">
					       <span align="left" id="resDecProjectedPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD align="center">
					  <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>
					</TD>
					<TD>
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResDecPresentPopCnt(); %>
					      <P align="right">
					      <span align="left" id="nonResDecPresentPopCnt">
					      <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecPresentPopCnt())%>
					      </span>
					      </P>
					</TD>
					<TD>
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResDecProjectedPopCnt(); %>
					       <P align="right">
					       <span align="left" id="nonResDecProjectedPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD align="center">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>
					</TD>
				</TR>
				<TR class="PortletSubHeaderColor">
					<TD>
						Onsite Wastewater Treatment Systems&nbsp; 
						<logic:equal name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
						<A href="<%=displayOWTSDetailsUrl%>">details</A>
						</logic:equal> 
					</TD>
					<TD>
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResISDSPresentPopCnt(); %>
					       <P align="right">
					       <span align="left" id="resISDSPresentPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSPresentPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD>
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResISDSProjectedPopCnt(); %>
					       <P align="right">
					       <span align="left" id="resISDSProjectedPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedPopCnt())%>
					       </span>
					</TD>
					<TD align="center">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>
					</TD>
					<TD>
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResISDSPresentPopCnt(); %>
					       <P align="right">
					       <span align="left" id="nonResISDSPresentPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSPresentPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD>
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResISDSProjectedPopCnt(); %>
					       <P align="right">
					       <span align="left" id="nonResISDSProjectedPopCnt">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedPopCnt())%>
					       </span>
					       </P>
					</TD>
					<TD align="center">
					      <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>
					</TD>
				</TR>
				
				<TR>
					<TD>
						<STRONG> Total (Excluding Upstream) </STRONG>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalPresentResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(resPresentTotal)%>">
							<STRONG><span align="left" id="totalPresentResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(resPresentTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalProjectedResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(resProjectedTotal)%>">
							<STRONG><span align="left" id="totalProjectedResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(resProjectedTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalPresentNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(nonResPresentTotal)%>">
							<STRONG><span align="left" id="totalPresentNonResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(nonResPresentTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<INPUT TYPE="hidden" id="totalProjectedNonResPopulation" 
						VALUE="<%=PopulationInformationForm.convertIntToString(nonResProjectedTotal)%>">
							<STRONG><span align="left" id="totalProjectedNonResPopulationSpan">
							<%out.print(PopulationInformationForm.convertIntToString(nonResProjectedTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
				</TR>
				<TR>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
				</TR>
             </logic:equal>	
			
             <logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="N">
				
				<TR class="PortletSubHeaderColor">
					<TD>
						Receiving Collection
					</TD>
					<TD align="right">
					       <%resPresentTotal = 0;
					         resPresentTotal = resPresentTotal+populationInformationForm.getResRecPresentPopCnt(); %>
					   	   <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecPresentPopCnt())%>
					</TD>
					<TD align="right">
					         <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResRecProjectedPopCnt(); %>
                       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedPopCnt())%>
					</TD>
					<TD>
					   <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResRecProjectedYear())%>
					</TD>
					<TD align="right">
					        <%nonResPresentTotal = 0;
					          nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResRecPresentPopCnt();%>
					        <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecPresentPopCnt())%>
					</TD>
					<TD align="right">
					        <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResRecProjectedPopCnt(); %>
					        <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedPopCnt())%>
					</TD>
					<TD align="center">
					    <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResRecProjectedYear())%>
					</TD>    
				</TR>
				<TR>
					<TD>
						Upstream Collection
						<logic:equal name="PopulationInformationFormBean" property="hasUpstream" value="Y">
						<%
							eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"UpsreamCollectionPage", linkParams,true, true);
						%>
						<A href="javascript:void(0);" onclick='showUpstreamCollection("<%=eventPopupWinowUrl%>")'> details </A>
						</logic:equal>
					</TD>
					<TD>
						<P align="right">
						    <span align="left">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left">
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamPresentNonResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left" >
							<%=PopulationInformationForm.convertIntToString(populationInformationForm.getUpstreamProjectedNonResPopulation())%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>

					</TD>
				</TR>
				<TR class="PortletSubHeaderColor">
					<TD>
						<STRONG> Total Receiving Treatment </STRONG>
					</TD>
					<TD>
						<P align="right">
						<STRONG><span align="left" >
							<%out.print(PopulationInformationForm.convertIntToString(resPresentTotal+populationInformationForm.getUpstreamPresentResPopulation()));%>						
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<STRONG><span align="left">
							<%out.print(PopulationInformationForm.convertIntToString(resProjectedTotal +populationInformationForm.getUpstreamProjectedResPopulation()));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left">
							<%out.print(PopulationInformationForm.convertIntToString(nonResPresentTotal+populationInformationForm.getUpstreamPresentNonResPopulation()));%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left">
							<%out.print(PopulationInformationForm.convertIntToString(nonResProjectedTotal+populationInformationForm.getUpstreamProjectedNonResPopulation()));%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
				</TR>
				<TR>
				    <TD>
						Clustered Systems&nbsp; 
						<logic:equal name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
						<A href="<%=displayClusteredDetailsUrl%>">details</A> 
						</logic:equal>
					</TD>
					<TD align="right">
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResDecPresentPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecPresentPopCnt())%>
					</TD>
					<TD align="right">
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResDecProjectedPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedPopCnt())%>
					</TD>
					<TD align="center">
					  <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>
					</TD>
					<TD align="right">
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResDecPresentPopCnt(); %>
					      <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecPresentPopCnt())%>
					</TD>
					<TD align="right">
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResDecProjectedPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedPopCnt())%>
					</TD>
					<TD align="center">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>
					</TD>
				</TR>
				<TR class="PortletSubHeaderColor">
					<TD>
						Onsite Wastewater Treatment Systems&nbsp;
						<logic:equal name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
						 <A href="<%=displayOWTSDetailsUrl%>">details</A> 
						</logic:equal>
					</TD>
					<TD align="right">
					       <%resPresentTotal = resPresentTotal+populationInformationForm.getResISDSPresentPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSPresentPopCnt())%>
					</TD>
					<TD align="right">
					       <%resProjectedTotal = resProjectedTotal+populationInformationForm.getResISDSProjectedPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedPopCnt())%>
					</TD>
					<TD align="center">
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>
					</TD>
					<TD align="right">
					       <%nonResPresentTotal = nonResPresentTotal+populationInformationForm.getNonResISDSPresentPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSPresentPopCnt())%>
					</TD>
					<TD align="right">
					       <%nonResProjectedTotal = nonResProjectedTotal+populationInformationForm.getNonResISDSProjectedPopCnt(); %>
					       <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedPopCnt())%>
					</TD>
					<TD align="center">
					      <%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>
					</TD>
				</TR>
				<TR>
					<TD>
						<STRONG> Total (Excluding Upstream) </STRONG>
					</TD>
					<TD>
						<P align="right">
						<STRONG><span align="left" >
							<%out.print(PopulationInformationForm.convertIntToString(resPresentTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
						<STRONG><span align="left" >
							<%out.print(PopulationInformationForm.convertIntToString(resProjectedTotal));%>
							</span></STRONG>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left" >
							<%out.print(PopulationInformationForm.convertIntToString(nonResPresentTotal));%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
						<span align="left" >
							<%out.print(PopulationInformationForm.convertIntToString(nonResProjectedTotal));%>
							</span>
						</P>
					</TD>
					<TD>
						<P align="right">
							&nbsp;
						</P>
					</TD>
				</TR>
				<TR>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
				</TR>
               </logic:equal>	
               
               
				<TR>
					<TD colspan="7">
						<p class="PortletText1">
						<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
						    <pdk-html:checkbox name="PopulationInformationFormBean" property="smallCommunityExceptionFlag"
						      value="Y"/>
						</logic:equal>
						<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="N">
						    <pdk-html:checkbox name="PopulationInformationFormBean" property="smallCommunityExceptionFlag"
						      disabled="true" value="Y"/>
						</logic:equal>      
							&nbsp;&nbsp;Small Community Exception Flag
						</p>
					</TD>
				</TR>
            <logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
								
				<TR>
					<TD colspan="7">
						<div align="left" width="150">
						  
						   <A href="javascript:PopulationConfirmAndSubmit()"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
						   <FONT size="1">&nbsp;&nbsp;</FONT>
						   <A href="javascript:PopulationCancel()"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Cancel" border="0"/></FONT></A>
						</div>
					</TD>
				</TR>
             </logic:equal>
								
			</TABLE>
		</TD>
		<TD valign="bottom"></TD>
	</TR>

</TABLE>

<BR/>

</pdk-html:form>
<pdk-html:javascript formName="PopulationInformationFormBean" staticJavascript="true"/>
<logic:equal name="PopulationInformationFormBean" property="displayClusteredDetails" value="Y">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="PopulationInformationFormBean1" styleId="PopulationInformationFormBeanId1"
                type="gov.epa.owm.mtb.cwns.populationInformation.PopulationInformationForm" 
                action="clusteredSystemDetails.do">
	 <div style="DISPLAY: none;">
	   <pdk-html:text styleId="clusteredAct" property="clusteredAct" value=""/> 
	   <input type="hidden" id="cancelParamId" name="" value="">
	   
	 </div>  
   <TABLE border="0" width="100%" class="PortletText1">
	<TR>
		<TD colspan="15">
		<div style="float: left" width="300">
			<FONT size="2"><STRONG>Add/Edit Clustered Systems Population</STRONG></FONT> 
		</div>
		</TD>
	</TR>
	<TR>
		<TD valign="bottom">
			<TABLE border="0" width="100%" class="PortletText1" cellspacing="3"
				cellpadding="1" >
				<TR class="PortletHeaderColor">
					<TD>&nbsp;</TD>
					<TD colspan="6" align="center" class="PortletHeaderText">
						<FONT color="#ffffff">Resident Population</FONT>
					</TD>
					<TD colspan="6" align="center" class="PortletHeaderText">
						<FONT color="#ffffff">Non- Resident Population</FONT>
					</TD>
				</TR>
				<TR>
					<TD>
						&nbsp; 
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Projected</FONT>
					</TD>
					
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Projected</FONT>
					</TD>
					
	           </TR>
	           <TR>
					<TD>
						&nbsp; 
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Population<BR>per Unit</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Year</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Population<BR>per Unit</FONT>
					</TD>
					<TD class="PortletHeaderColor">		
					<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Year</FONT>
					</TD>
					
	           </TR>
	           	           
	           <TR>
					<TD nowrap="nowrap">
						Clustered Systems
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="nonResPopulationPerHouse" value="0">
						     <span id="resPopulationPerHouseReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="nonResPopulationPerHouse" value="0">
						     <span id="resPopulationPerHouseReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resPopulationPerHouse" styleId="resPopulationPerHouse"
					       value="<%=populationInformationForm.getResPopulationPerHouse()%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resPopulationPerHouse" styleId="resPopulationPerHouse"
					        value="<%=populationInformationForm.getResPopulationPerHouse()%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					  <TD align="right">
					    <bean:write name="PopulationInformationFormBean" property="resPopulationPerHouse" />
					  </TD>
					</logic:notEqual>
										
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="presentNonResClusteredHouses" value="0">
						     <span id="presentResClusteredHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="presentNonResClusteredHouses" value="0">
						     <span id="presentResClusteredHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="presentResClusteredHouses" styleId="presentResClusteredHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentResClusteredHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="presentResClusteredHouses" styleId="presentResClusteredHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentResClusteredHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					 </logic:equal>
					 <logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					 <TD align="right">
					    <bean:write name="PopulationInformationFormBean" property="presentResClusteredHouses"/> 
					 </TD>   
					 </logic:notEqual>   
					
					<TD align="right">
					<span align="left" id="resClusteredPresentPopCntSpan">
					    <bean:write name="PopulationInformationFormBean" property="resDecPresentPopCnt" />
					</span>    
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="projectedNonResClusteredHouses" value="0">
						     <span id="projectedResClusteredHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="projectedNonResClusteredHouses" value="0">
						     <span id="projectedResClusteredHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="checkProjected(this.id, 'resDecProjectedYear');tallyAllFields(this.id)"   
					       name="PopulationInformationFormBean" property="projectedResClusteredHouses" styleId="projectedResClusteredHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedResClusteredHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="projectedResClusteredHouses" styleId="projectedResClusteredHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedResClusteredHouses())%>" 
					        disabled="true" styleClass="disabledField" 
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="projectedResClusteredHouses"/>
					</TD>   
					</logic:notEqual>
					
					<TD align="right">
					<span align="left" id="resClusteredProjectedPopCntSpan">
					   <bean:write name="PopulationInformationFormBean" property="resDecProjectedPopCnt" />
					</span>   
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="resDecProjectedYear" styleId="resDecProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>" 
					       size="4" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resDecProjectedYear" styleId="resDecProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResDecProjectedYear())%>"
					        disabled="true" styleClass="disabledField"
					        size="4" maxlength="4"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="center">
					   <bean:write name="PopulationInformationFormBean" property="resDecProjectedYear"/>
					</TD>   
					</logic:notEqual>
					
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="resPopulationPerHouse" value="0">
						     <span id="nonResPopulationPerHouseReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="resPopulationPerHouse" value="0">
						     <span id="nonResPopulationPerHouseReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResPopulationPerHouse" styleId="nonResPopulationPerHouse"
					       value="<%=populationInformationForm.getNonResPopulationPerHouse()%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="isClusteredSystemExists" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResPopulationPerHouse" styleId="nonResPopulationPerHouse"
					        value="<%=populationInformationForm.getNonResPopulationPerHouse()%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					  <TD align="right">
					    <bean:write name="PopulationInformationFormBean" property="nonResPopulationPerHouse" />
					  </TD>
					</logic:notEqual>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="presentResClusteredHouses" value="0">
						     <span id="presentNonResClusteredHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="presentResClusteredHouses" value="0">
						     <span id="presentNonResClusteredHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="presentNonResClusteredHouses" styleId="presentNonResClusteredHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentNonResClusteredHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="presentNonResClusteredHouses" styleId="presentNonResClusteredHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentNonResClusteredHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="presentNonResClusteredHouses"/>
					</TD>   
					</logic:notEqual>
					
					<TD align="right">
					<span align="left" id="nonResClusteredPresentPopCntSpan">
					   <bean:write name="PopulationInformationFormBean" property="nonResDecPresentPopCnt" />
					</span>   
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="projectedResClusteredHouses" value="0">
						     <span id="projectedNonResClusteredHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="projectedResClusteredHouses" value="0">
						     <span id="projectedNonResClusteredHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="checkProjected(this.id, 'nonResDecProjectedYear');tallyAllFields(this.id)"   
					       name="PopulationInformationFormBean" property="projectedNonResClusteredHouses" styleId="projectedNonResClusteredHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedNonResClusteredHouses())%>" 
					       size="8" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="projectedNonResClusteredHouses" styleId="projectedNonResClusteredHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedNonResClusteredHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="projectedNonResClusteredHouses"/>
					</TD>   
					</logic:notEqual>
					
					<TD align="right">
					<span align="left" id="nonResClusteredProjectedPopCntSpan">
					    <bean:write name="PopulationInformationFormBean" property="nonResDecProjectedPopCnt" />
					</span>    
					</TD>
					
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="nonResDecProjectedYear" styleId="nonResDecProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>" 
					       size="4" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableDecentralizedProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResDecProjectedYear" styleId="nonResDecProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResDecProjectedYear())%>"
					        disabled="true" styleClass="disabledField"
					        size="4" maxlength="4"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="center">
					   <bean:write name="PopulationInformationFormBean" property="nonResDecProjectedYear"/>
					</TD>   
					</logic:notEqual>
			   </TR>
			   <%if ("Y".equalsIgnoreCase(populationInformationForm.getEnableDecentralizedPresent())||"Y".equalsIgnoreCase(populationInformationForm.getEnableDecentralizedProjected())) {
			                      if("Y".equalsIgnoreCase(populationInformationForm.getIsUpdatable())){%>
			   <TR>
			   <TD>&nbsp;</TD>
			   </TR>
			   <TR>
					<TD colspan="15">
						<div align="left" width="150">
						   <INPUT type="button" name="Save" value="Save" onclick ="javascript:ClusteredSystemSubmit()">
						   <FONT size="1">&nbsp;&nbsp;</FONT>
						   <INPUT type="button" name="cancel" value="Cancel" onclick="javascript:ClusteredSystemCancel()"> 
						</div>
					</TD>
				</TR>
				<%} }%>		
	    </TABLE>			
	    </TD>
	    </TR>
	</TABLE>
	</pdk-html:form>
	<pdk-html:javascript formName="PopulationInformationFormBean1" staticJavascript="true"/>
</DIV>
</logic:equal>

<logic:equal name="PopulationInformationFormBean" property="displayOWTSDetails" value="Y">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="PopulationInformationFormBean2" styleId="PopulationInformationFormBeanId2"
                type="gov.epa.owm.mtb.cwns.populationInformation.PopulationInformationForm" 
                action="owtSystemDetails.do">
	 <div style="DISPLAY: none;">
	   <pdk-html:text styleId="owtsAct" property="owtsAct" value=""/> 
	   <input type="hidden" id="cancelParamId" name="" value="">
	   
	 </div>  
   <TABLE border="0" width="100%" class="PortletText1">
	<TR>
		<TD colspan="15">
		<div style="float: left" width="300">
			<FONT size="2"><STRONG>Add/Edit Onsite Wastewater Treament Systems Population</STRONG></FONT> 
		</div>
		</TD>
	</TR>
	<TR>
		<TD valign="bottom">
			<TABLE border="0" width="100%" class="PortletText1" cellspacing="3"
				cellpadding="1" >
				<TR class="PortletHeaderColor">
					<TD>&nbsp;</TD>
					<TD colspan="6" align="center" class="PortletHeaderText">
						<FONT color="#ffffff">Resident Population</FONT>
					</TD>
					<TD colspan="6" align="center" class="PortletHeaderText">
						<FONT color="#ffffff">Non- Resident Population</FONT>
					</TD>
				</TR>
				<TR>
					<TD>
						&nbsp; &nbsp;
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Projected</FONT>
					</TD>
					
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Present</FONT>
					</TD>
					<TD colspan="3" align="center" class="PortletHeaderColor">
							<FONT color="#ffffff">Projected</FONT>
					</TD>
					
	           </TR>
	           <TR>
					<TD>
						&nbsp; 
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Population<BR>per Unit</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Year</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Population<BR>per Unit</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units </FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
						<FONT color="#ffffff">Number of<BR>Units</FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Total</FONT>
					</TD>
					<TD class="PortletHeaderColor">
					    <FONT color="#ffffff">Year</FONT>
					</TD>
					
	           </TR>
	           	           
	           <TR>
					<TD nowrap="nowrap">
						Onsite Wastewater<BR>Treatment Systems
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					   <TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="nonResOWTSPopulationPerHouse" value="0">
						     <span id="resOWTSPopulationPerHouseReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="nonResOWTSPopulationPerHouse" value="0">
						     <span id="resOWTSPopulationPerHouseReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="resOWTSPopulationPerHouse" styleId="resOWTSPopulationPerHouse"
					       value="<%=populationInformationForm.getResOWTSPopulationPerHouse()%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="resOWTSPopulationPerHouse" styleId="resOWTSPopulationPerHouse"
					        value="<%=populationInformationForm.getResOWTSPopulationPerHouse()%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <bean:write name="PopulationInformationFormBean" property="resOWTSPopulationPerHouse" />
					</TD>
					</logic:notEqual>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="presentNonResOWTSHouses" value="0">
						     <span id="presentResOWTSHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="presentNonResOWTSHouses" value="0">
						     <span id="presentResOWTSHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					    
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="presentResOWTSHouses" styleId="presentResOWTSHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentResOWTSHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="presentResOWTSHouses" styleId="presentResOWTSHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentResOWTSHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					 </logic:equal>
					 <logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="presentResOWTSHouses"/>
					</TD>   
					</logic:notEqual>   
					
					<TD align="right">
					<span align="left" id="resOWTSPresentPopCntSpan">
					    <bean:write name="PopulationInformationFormBean" property="resISDSPresentPopCnt" />
					</span>    
					</TD>
					
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="projectedNonResOWTSHouses" value="0">
						     <span id="projectedResOWTSHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="projectedNonResOWTSHouses" value="0">
						     <span id="projectedResOWTSHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					      
					       <pdk-html:text onblur="checkProjected(this.id, 'resISDSProjectedYear');tallyAllFields(this.id)"   
					       name="PopulationInformationFormBean" property="projectedResOWTSHouses" styleId="projectedResOWTSHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedResOWTSHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="projectedResOWTSHouses" styleId="projectedResOWTSHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedResOWTSHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="projectedResOWTSHouses"/>
					</TD>   
					</logic:notEqual>
					
					<TD align="right">
					<span align="left" id="resOWTSProjectedPopCntSpan">
					   <bean:write name="PopulationInformationFormBean" property="resISDSProjectedPopCnt" />
					</span>   
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="resISDSProjectedYear" styleId="resISDSProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>" 
					       size="4" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="resISDSProjectedYear" styleId="resISDSProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getResISDSProjectedYear())%>"
					        disabled="true" styleClass="disabledField"
					        size="4" maxlength="4"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="center">
					   <bean:write name="PopulationInformationFormBean" property="resISDSProjectedYear"/>
					</TD>   
					</logic:notEqual>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="resOWTSPopulationPerHouse" value="0">
						     <span id="nonResOWTSPopulationPerHouseReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="resOWTSPopulationPerHouse" value="0">
						     <span id="nonResOWTSPopulationPerHouseReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="nonResOWTSPopulationPerHouse" styleId="nonResOWTSPopulationPerHouse"
					       value="<%=populationInformationForm.getNonResOWTSPopulationPerHouse()%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="isOWTSystemExists" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="nonResOWTSPopulationPerHouse" styleId="nonResOWTSPopulationPerHouse"
					        value="<%=populationInformationForm.getNonResOWTSPopulationPerHouse()%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <bean:write name="PopulationInformationFormBean" property="nonResOWTSPopulationPerHouse" />
					</TD> 
					</logic:notEqual>    
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					     <logic:equal name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="presentResOWTSHouses" value="0">
						     <span id="presentNonResOWTSHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="presentResOWTSHouses" value="0">
						     <span id="presentNonResOWTSHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="tallyAllFields(this.id)"  
					       name="PopulationInformationFormBean" property="presentNonResOWTSHouses" styleId="presentNonResOWTSHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentNonResOWTSHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSPresent" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="presentNonResOWTSHouses" styleId="presentNonResOWTSHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getPresentNonResOWTSHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="presentNonResOWTSHouses"/>
					</TD>   
					</logic:notEqual>
					
					<TD align="right">
					<span align="left" id="nonResOWTSPresentPopCntSpan">
					   <bean:write name="PopulationInformationFormBean" property="nonResISDSPresentPopCnt" />
					</span>   
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <logic:lessEqual name="PopulationInformationFormBean" property="projectedResOWTSHouses" value="0">
						     <span id="projectedNonResOWTSHousesReqSpan" class="required">*</span>
						   </logic:lessEqual>
						   <logic:greaterThan name="PopulationInformationFormBean" property="projectedResOWTSHouses" value="0">
						     <span id="projectedNonResOWTSHousesReqSpan" class="required">&nbsp;</span>
						   </logic:greaterThan>
					       <pdk-html:text onblur="checkProjected(this.id, 'nonResISDSProjectedYear');tallyAllFields(this.id)"   
					       name="PopulationInformationFormBean" property="projectedNonResOWTSHouses" styleId="projectedNonResOWTSHouses"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedNonResOWTSHouses())%>" 
					       size="6" maxlength="6"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text onblur="tallyAllFields(this.id)"  
					        name="PopulationInformationFormBean" property="projectedNonResOWTSHouses" styleId="projectedNonResOWTSHouses"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getProjectedNonResOWTSHouses())%>" 
					        disabled="true" styleClass="disabledField"
					        size="6" maxlength="6"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					   <bean:write name="PopulationInformationFormBean" property="projectedNonResOWTSHouses"/>
					</TD>   
					</logic:notEqual>
					<TD align="right">
					<span align="left" id="nonResOWTSProjectedPopCntSpan">
					    <bean:write name="PopulationInformationFormBean" property="nonResISDSProjectedPopCnt" />
					</span>    
					</TD>
					<logic:equal name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="right">
					    <logic:equal name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					       <pdk-html:text name="PopulationInformationFormBean" property="nonResISDSProjectedYear" styleId="nonResISDSProjectedYear"
					       value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>" 
					       size="4" maxlength="4"/>
					     </logic:equal> 
					     <logic:notEqual name="PopulationInformationFormBean" property="enableISDSProjected" value="Y">
					        <pdk-html:text name="PopulationInformationFormBean" property="nonResISDSProjectedYear" styleId="nonResISDSProjectedYear"
					        value="<%=PopulationInformationForm.convertIntToString(populationInformationForm.getNonResISDSProjectedYear())%>"
					        disabled="true" styleClass="disabledField"
					        size="4" maxlength="4"/>
					     </logic:notEqual>
					</TD>
					</logic:equal>
					<logic:notEqual name="PopulationInformationFormBean" property="isUpdatable" value="Y">
					<TD align="center">
					   <bean:write name="PopulationInformationFormBean" property="nonResISDSProjectedYear"/>
					</TD>   
					</logic:notEqual>
			   </TR>
			   <%if ("Y".equalsIgnoreCase(populationInformationForm.getEnableISDSPresent())||"Y".equalsIgnoreCase(populationInformationForm.getEnableISDSProjected())) {
			                 if("Y".equalsIgnoreCase(populationInformationForm.getIsUpdatable())){%>
			    <TR>
			      <TD>&nbsp;</TD>
			    </TR>
			    <TR>
					<TD colspan="15">
						<div align="left" width="150">
						   <INPUT type="button" name="Save" value="Save" onclick ="javascript:OWTSystemSubmit()">
						   <FONT size="1">&nbsp;&nbsp;</FONT>
						   <INPUT type="button" name="cancel" value="Cancel" onclick="javascript:OWTSystemCancel()"> 
						</div>
					</TD>
				</TR>
				<%}} %>		
	    </TABLE>			
	    </TD>
	    </TR>
	</TABLE>
	</pdk-html:form>
	<pdk-html:javascript formName="PopulationInformationFormBean2" staticJavascript="true"/>
</DIV>
</logic:equal>


