<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="gov.epa.owm.mtb.cwns.facilityInformation.FacilityPORForm"%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="java.text.DecimalFormat"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	FacilityPORForm facilityPORForm = (FacilityPORForm)request.getAttribute("facilityPOR");
	char updatable = facilityPORForm.getIsUpdatable();
	String webritURL = (String)request.getAttribute("webritURL");
	String param = PortletRendererUtil.portletParameter(prr, "org.apache.struts.taglib.html.CANCEL");
	String showCalMeasureDate =null;
	DecimalFormat formatter1  = new DecimalFormat("00.0000");
	DecimalFormat formatter2  = new DecimalFormat("00.0000");
	String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	String popurl = url +"/javascript/popcalendar.js";
	String dateurl = url +"/javascript/date.js";
	String imgurl = url +"/images/";
	String ajaxurl = url +"/javascript/prototype.js";
	showCalMeasureDate = "showCalendar(this,document.facilityPORFormId.measureDate, 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
%>
<script type="text/javascript" language="JavaScript" src="<%=ajaxurl %>"></script>
<SCRIPT type=text/javascript>
var webritSessionId="";
var saveCoordinates='Y';
function lauchWebRIT(){
       document.body.style.cursor = "wait";
       var facilityId = document.getElementById('facilityId').value;
       <logic:equal name="remote" value="true">
        new Ajax.Request('/cwns/servlet/AjaxRequestProcessor',
         { method: 'post',
          asynchronous: true,
          parameters: {url: '<%=url%>/getWebRITParams.do?facilityId='+facilityId+'&latitude='+getLatitude()+'&longitude='+ getLongitude()+'&session='+webritSessionId},
       </logic:equal>
	   <logic:notEqual name="remote" value="true">
       new Ajax.Request('/cwns/getWebRITParams.do',
        { method: 'post',
          asynchronous: true,
          parameters: {facilityId: facilityId, latitude: getLatitude()+'', longitude: getLongitude()+'', session: webritSessionId+''},
       </logic:notEqual>                 
          onSuccess: function(transport){
          	var response = transport.responseText || "no response text";
          	//alert("Success! \n\n" + response);
          	//construct URL - parse the response
			var  doc=new ActiveXObject("Microsoft.XMLDOM");
			doc.async="false";
			doc.loadXML(response);
			var viewerType='CWNSReview';
			var maxFeatures ='0';
			if('Y' == '<%=updatable%>'){
				viewerType=doc.getElementsByTagName("viewerType")[0].firstChild.nodeValue;
				maxFeatures = doc.getElementsByTagName("maxFeatures")[0].firstChild.nodeValue;
				var coType =  window.document.getElementById('coordinateTypeCodeId').value;
				if(coType=='S' && viewerType != 'CWNSPS'){
				   viewerType='CWNSPS';
				   maxFeatures =1;
				}
				if(coType=='P' && viewerType != 'CWNSNPS'){
				   viewerType='CWNSNPS';
				   maxFeatures =10;
				}
			}
			var viewerUrl = doc.getElementsByTagName("viewerUrl")[0].firstChild.nodeValue;
			var sessionId = null;
			if(webritSessionId !=''){
			    sessionId=webritSessionId; 
			}else{
			   sessionId=doc.getElementsByTagName("sessionId")[0].firstChild.nodeValue;
			}			
			//var offNHDLat = doc.getElementsByTagName("offNHDLat")[0].firstChild.nodeValue;
			//var offNHDLon = doc.getElementsByTagName("offNHDLon")[0].firstChild.nodeValue;	
			var boundingBox = doc.getElementsByTagName("boundingBox")[0].firstChild.nodeValue;
			var viewerProperties = doc.getElementsByTagName("viewerProperties")[0].firstChild.nodeValue;
			var regId = doc.getElementsByTagName("regId")[0].firstChild.nodeValue;
			var url = viewerUrl + '?type=' + viewerType + '&sessionId=' + sessionId + '&RegKey='+ regId + '&MaxFeatures='+ maxFeatures + '&BBOX=' + boundingBox + viewerProperties;
			//var url = viewerUrl + '?type=' + viewerType + '&sessionId=' + sessionId + '&offNHDLat=' + offNHDLat + '&offNHDLon=' + offNHDLon +'&BBOX=' + boundingBox + viewerProperties;
			//alert('url'+url);
          	// launch webrit
          	document.body.style.cursor = "default";
          	ShowPORPopup(url,750, 550);
          	//set webRIT session 
          	webritSessionId=sessionId;
          	//if not in view mode
          	//get the new coordinates
          	if('Y' == '<%=updatable%>'){
          	    document.body.style.cursor = "wait";
          	    getWebRITSessionParameters(facilityId, sessionId);
          	}
          },
          onFailure: function(){ 
            document.body.style.cursor = "default"; 
          	alert('Failed to launch WATERS Lite Viewer. Please try later or contact CWNS administrator') 
          }          
        });  
}

function getWebRITSessionParameters(facilityId, sessionId){
    <logic:equal name="remote" value="true">
       new Ajax.Request('/cwns/servlet/AjaxRequestProcessor',
        { method: 'post',
          asynchronous: true,
          parameters: {url: '<%=url%>/getWebRITSessionParams.do?facilityId='+facilityId + '&sessionId=' + sessionId + '&updatable=<%=updatable%>'},
    </logic:equal>      
    <logic:notEqual name="remote" value="true">      
       new Ajax.Request('/cwns/getWebRITSessionParams.do',
        { method: 'post',
          asynchronous: true,
          parameters: {facilityId: facilityId, sessionId: sessionId+'', updatable: '<%=updatable%>' },    
    </logic:notEqual>
          onSuccess: function(transport){
            //alert('started'); 
            document.body.style.cursor = "default"; 
            if('Y' == '<%=updatable%>'){
                //alert('updating'); 
            	//parse the response
            	var response = transport.responseText || "no response text";
          		//alert("Success! \n\n" + response); 
				var  doc=new ActiveXObject("Microsoft.XMLDOM");
				doc.async="false";
				doc.loadXML(response);

				//check if paramaters can be found
				if(doc.getElementsByTagName("gml:coordinates").length==0){
				    saveCoordinates='N';
				    //alert('Unable to find points');
				    return;
				}	
							
				//get xml parameters
            	var coord = doc.getElementsByTagName("gml:coordinates")[0].firstChild.nodeValue;
            	var method=doc.getElementsByTagName("CollectionMethod")[0].firstChild.nodeValue;
				var accuracy=doc.getElementsByTagName("HorizontalAccuracy")[0].firstChild.nodeValue;
				var scale=doc.getElementsByTagName("SourceMapScale")[0].firstChild.nodeValue;
            	var datum=doc.getElementsByTagName("HorizontalDatum")[0].firstChild.nodeValue;            	
            	//alert('Finished fetching the data from xml response'); 
            	
            	// set form parameters
            	
            	//alert('coord'+coord);
            	var arr=coord.split(",");
            	var lat=getFloatValue(arr[1]);
            	var lon=getFloatValue(arr[0]);
            	//alert('lat' + lat + '  Lon:' +lon);
            	var form = window.document.getElementById('facilityPORFormId');
				if(lat<0){
					document.getElementById('latitude').value=(lat*-1).toFixed(4);
				  	setCheckedValue(form['latitudeDirec'],'<%=FacilityPORForm.SOUTH %>');
				}else{
				  document.getElementById('latitude').value=lat.toFixed(4);
				  	setCheckedValue(form['latitudeDirec'],'<%=FacilityPORForm.NORTH %>');//North				  
				}				
            	if(lon<0){
            	   document.getElementById('longitude').value=(lon *-1).toFixed(4);
            	   setCheckedValue(form['longitudeDirec'],'<%=FacilityPORForm.WEST%>');
            	}else{
            	   document.getElementById('longitude').value=lon.toFixed(4);
            	   setCheckedValue(form['longitudeDirec'],'<%=FacilityPORForm.EAST%>');
            	}
            	
            	if(method=='INTERPOLATION-MAP'){
            	    setListValue(document.getElementById('methodId'), 22);
            	}

            	if(datum=='NAD83'){
            		setListValue(document.getElementById('datumId'), 4);
            	}else{
            	   //WGS ??
            	   setListValue(document.getElementById('datumId'), 10);
            	}
            	
            	var today = new Date(); 
            	var mon= today.getMonth()+1;
            	document.getElementById('measureDate').value=mon+ "/" + today.getDate()+ "/" +today.getFullYear();
            	document.getElementById('scale').value=scale;
            	
            	//alert('Finished setting the data values'); 
            	
            	//change source
            	document.getElementById('sourceName').innerHTML='WATERS Light Viewer';            	
            	document.getElementById('sourceId').value='W';
            	//alert('Finished setting the datasource'); 
            	
            	//change the background            
            	document.getElementById("coBody").style.backgroundColor='#cccccc';
            	//alert('done');
            }
          },
          onFailure: function(){ 
            document.body.style.cursor = "default";
          	alert('Failed to retrieve coordinates.  Please try later or contact CWNS administrator') }            
          });
}

function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}

function getCheckedValue(radioObj){
  if(!radioObj)return '';
  var radioLength = radioObj.length;
  if(radioLength == undefined) {
	return '';
  }
  for(var i = 0; i < radioLength; i++) {
	if(radioObj[i].checked == true){
		return radioObj[i].value;
	}
  }
  return '';
}

function getLatitude(){
  var lat =  window.document.getElementById('latitude');
  if(lat ==undefined || lat==null){
    return '';
  }
  var latitude = window.document.getElementById('latitude').value;
  if (latitude != ""){
     var lat=getFloatValue(latitude); 
     var direc = getDirection('latitudeDirec');
     if(direc !="" && direc=='<%=FacilityPORForm.SOUTH%>'){
       latitude = (lat*-1)+"";
     }
  }
  return latitude;     
}

function getLongitude(){
  var lon =  window.document.getElementById('longitude');
  if(lon ==undefined || lon==null){
    return '';
  }
  var longitude = window.document.getElementById('longitude').value;
  if (longitude != ""){
     var lon=getFloatValue(longitude); 
     var direc = getDirection('longitudeDirec');
     if(direc !="" && direc=='<%=FacilityPORForm.WEST%>'){
       longitude = (lon*-1)+"";
     }
  }
  return longitude;     
}

function getDirection(direcName){
   var form = window.document.getElementById('facilityPORFormId');
   var rio = form[direcName];
   return getCheckedValue(rio);
}

function setListValue(listObj, indexValue) {
	if(!listObj)
		return;
	var listLength = listObj.length;
	if(listLength == undefined) {
		return;
	}
	for(var i = 0; i < listLength; i++) {
		if(listObj[i].value == indexValue) {
			listObj.selectedIndex=i;
		}
	}
}


function getFloatValue(str){
  if(str == null || str == "")
     return 0;
  else
     return parseFloat(str);
}


function getWebRITURL(responseXML){
var url ='';
//if IE
var xmlDoc = new ActiveXObject("Microsoft.XMLDOM")
xmlDoc.async="false"
xmlDoc.load("note.xml")

var viewerURL = xmlDoc.getElementsByTagName("viewerUrl").item(0).text;
var sessionId = xmlDoc.getElementsByTagName("sessionId").item(0).text;
var offNHDLat = xmlDoc.getElementsByTagName("offNHDLat").item(0).text;
var offNHDLon = xmlDoc.getElementsByTagName("offNHDLon").item(0).text;
var boundingBox = xmlDoc.getElementsByTagName("boundingBox").item(0).text;
var viewerProperties = xmlDoc.getElementsByTagName("viewerProperties").item(0).text;

}

function ShowConverter(popupUrl){
    var w = 600;
    var h = 200;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    w = window.open(popupUrl, null, settings);
}

function ShowPORPopup(popupUrl, w, h){
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    //var w = window.open(popupUrl, null, settings);
    window.showModalDialog(popupUrl,window,"status:false;dialogWidth:" + w + "px;dialogHeight:"+ h + "px");

    
  //if(w != null) 
  //  w.focus();     
}



function FacilityPORReset(){
  var hidden_field = document.getElementById('PORparamId');
  hidden_field.name = "<%=param %>";
  hidden_field.value = "Cancel";
  if(webritSessionId!=""){
     document.getElementById('webritSessionId').value=webritSessionId+":"+saveCoordinates;
  }
  window.document.facilityPORForm.submit();
  return true;
}

function PORConfirmAndSubmit(){
//if coordinate are applicable to county or watershed wide
if(document.getElementById('coordinateTypeCodeId').value == 'C'){
       //make sure primary county exists else alert and do not save
       <logic:present name="isPrimaryCounty">
          <logic:equal name="isPrimaryCounty" value="Y">
             window.document.facilityPORForm.submit();
             return true;
          </logic:equal>
          <logic:notEqual name="isPrimaryCounty" value="Y">
             alert("A primary county must be specified");
          </logic:notEqual>
       </logic:present>
       return;
}
//watershed
if(document.getElementById('coordinateTypeCodeId').value == 'W'){
       //make sure primary watershed exists else alert and do not save
       <logic:present name="isPrimaryWatershed">
          <logic:equal name="isPrimaryWatershed" value="Y">
             window.document.facilityPORForm.submit();
             return true;
          </logic:equal>
          <logic:notEqual name="isPrimaryWatershed" value="Y">
             alert("A primary watershed must be specified");
          </logic:notEqual>
       </logic:present>
       return;
}

var s = '<%=facilityPORForm.getSource()%>';
if (s=='E'){
  //var measureDate = document.getElementById('measureDate').value;
  //if (measureDate == ""){
  // alert("Measure Date is required");
  //			return;
  //}
  var form = document.getElementById('facilityPORFormId');   
  if(form!=undefined){
     if(!validateFacilityPORForm(form)){
       return;
     }
  } 
  //if (!ValidateDate(measureDate)){
  //  return;
  //}
}
else {

var locationDescId = document.getElementById('locationDescId').value;
var methodId = document.getElementById('methodId').value;
var datumId = document.getElementById('datumId').value;
var latitude = document.getElementById('latitude').value;
var longitude = document.getElementById('longitude').value;
var measureDate = document.getElementById('measureDate').value;

        if (locationDescId=="" && (measureDate!="" || datumId!="" || longitude!="" || latitude!="" || methodId!="")){
			alert("Location Description is required");
			return;
		}
       if (methodId=="" && (measureDate!="" || datumId!="" || longitude!="" || latitude!="" || locationDescId!="")){
			alert("Method is required");
			return;
		}
		if (datumId=="" && (measureDate!="" || methodId!="" || longitude!="" || latitude!="" || locationDescId!="")){
			alert("Datum is required");
			return;
		}
		if (latitude=="" && (measureDate!="" || datumId!="" || longitude!="" || methodId!="" || locationDescId!="")){
			alert("Latitude is required");
			return;
		}
		if (longitude=="" && (measureDate!="" || datumId!="" || methodId!="" || latitude!="" || locationDescId!="")){
			alert("Longitude is required");
			return;
		}
		if (measureDate=="" && (methodId!="" || datumId!="" || longitude!="" || latitude!="" || locationDescId!="")){
			alert("Measure Date is required");
			return;
		}
        
  var form = document.getElementById('facilityPORFormId');   
  if(form!=undefined){
     if(!validateFacilityPORForm(form)){
       return;
     }
  } 
  var latitude = document.getElementById('latitude').value;
  if (latitude != ""){
     
    var tempArray = latitude.split('.');
    if (tempArray[0]<0 || tempArray[0]>90){
       alert("Degree should be from 0 to 90"); 
       return;
    }   
    if (tempArray[1] == undefined){
      alert("Latitude must have four decimals.");
      return;
    }  
    else
      if (tempArray[1].length < 4 || tempArray[1].length > 4){
        alert("Latitude must have four decimals.");
        return;
      }
 }
  
  var longitude = document.getElementById('longitude').value;
  if (longitude != ""){
    var tempArray2 = longitude.split('.');
    if (tempArray2[0]<0 || tempArray2[0]>180){
       alert("Degree should be from 0 to 180"); 
       return;
    }   
    if (tempArray2[1] == undefined){
      alert("Longitude must have four decimals.");
      return;
    }  
    else
      if (tempArray2[1].length < 4 || tempArray2[1].length > 4){
        alert("Longitude must have four decimals.");
        return;
      }
       
  }
  if (!ValidateDate(measureDate)){
    return;
  }  
}  
   //set the session
   if(webritSessionId!=""){
     document.getElementById('webritSessionId').value=webritSessionId+":"+saveCoordinates;
   }
   window.document.facilityPORForm.submit();
   
   return true;
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

function ValidateDate(measureDate){
var bufArray = measureDate.split("/");
var mm = bufArray[0];
var dd = bufArray[1];
var yyyy = bufArray[2];
var today = new Date();
var msDate = new Date();
msDate.setFullYear(yyyy,mm-1,dd);
if (msDate > today){
   alert("Measure Date must be less than or equal to Today's date");
   return false;
 } 
 return true; 
}

 function ChangeSource(){
   //change source
   document.getElementById('sourceName').innerHTML='Manual';            	
   document.getElementById('sourceId').value='M';
 }  
 
 function hideCoBody() {
   if(document.getElementById('coordinateTypeCodeId').value == 'C' ||document.getElementById('coordinateTypeCodeId').value == 'W'){
       document.getElementById('coBody').style.display='none';
    }else{
       document.getElementById('coBody').style.display='block'; 
    }
 }

</SCRIPT>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>
<SCRIPT type=text/javascript>
	setImgDir('<%=imgurl%>');
</script>

<%
    String showCheckbox = "none";

    if(facilityPORForm.getConfirm() == 'Y')
    {
      showCheckbox = "block";
    }
%>

<pdk-html:form name="facilityPORForm" styleId="facilityPORFormId"
	type="gov.epa.owm.mtb.cwns.facilityInformation.FacilityPORForm"
	action="saveFacilityCoordinates.do">
	<DIV id="hidden_fields" style="DISPLAY:none">
		<input type="hidden" id="PORparamId" name="PORparamIdName" value="">
		<pdk-html:text name="facilityPORForm" property="facilityId"/>
		<pdk-html:text name="facilityPORForm" property="isUpdatable"/>
		<pdk-html:text name="facilityPORForm" styleId="sourceId" property="source"/>
		<pdk-html:text name="facilityPORForm" property="isTriTerritoryUpdatable"/>
		<pdk-html:text name="facilityPORForm" styleId ="webritSessionId" property="sessionId"/>
		<logic:notPresent name="displayCoordinatesType">
			<pdk-html:text name="facilityPORForm"  property="coordinateTypeCode" styleId="coordinateTypeCodeId"/>
		</logic:notPresent>
	</DIV> 
	
	<div class="PortletText1">
		&nbsp;<FONT size="4"><STRONG>Coordinates</STRONG> </FONT><br><br>
	</div>
	<logic:present name="displayCoordinatesType">
	   <logic:equal name="displayCoordinatesType" value="Y">
		<TABLE class="PortletText1" cellspacing="3" cellpadding="3" width="50%">
			<TR>
				<TD width="15%">
					<STRONG> Type of Coordinates: </STRONG>
				</TD>
				<TD  width="35%">
				<logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
					<pdk-html:select name="facilityPORForm"  property="coordinateTypeCode" styleId="coordinateTypeCodeId" size="1" onchange="hideCoBody();ChangeSource();">
	  					<pdk-html:option value="S">Single Point</pdk-html:option>
	  					<pdk-html:option value="P">Polygon</pdk-html:option>
  	  					<pdk-html:option value="C">Primary County</pdk-html:option>
  	  					<pdk-html:option value="W">Primary Watershed</pdk-html:option>
  					</pdk-html:select>			
  				</logic:equal>
  			    <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
  			         <pdk-html:hidden  name="facilityPORForm"  property="coordinateTypeCode" styleId="coordinateTypeCodeId"/> 
	                 <logic:equal name="facilityPORForm" property="coordinateTypeCode" value="S">
	                     Single Point
	                 </logic:equal>
	                 <logic:equal name="facilityPORForm" property="coordinateTypeCode" value="P">
	                     Polygon
	                 </logic:equal>
	                 <logic:equal name="facilityPORForm" property="coordinateTypeCode" value="C">
	                     Primary County
	                 </logic:equal>
	                 <logic:equal name="facilityPORForm" property="coordinateTypeCode" value="W">
	                     Primary Watershed
	                 </logic:equal>	                 
	            </logic:notEqual> 	
				</TD>
			</TR>
		</TABLE>	   
	   </logic:equal>
	</logic:present>	
	<!-- Only available for NPS and OWTS -->	
	
	<div id="coBody" style="DISPLAY:block">
	<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%" >	    
		<TR>
			<TD colspan="5">
				<TABLE class="PortletText1" cellspacing="3" cellpadding="3" width="100%">
					<TR>
						<TD colspan="5">
							<div style="float: right;" width="100">
								<A href="javascript:void(0);" onclick="lauchWebRIT()">
								    Use Map to Get Information
								</A>
							</div>
						</TD>
					</TR>
				    <TR>
				        <TD colspan="5">
							<DIV id="radio_button_div" 
                             style="display:<%=showCheckbox%>" align="left">
	                        <FONT class="error">
		                      <bean:message key="error.facilitycoordinates.porexists"/> 
                            </FONT>
	                        </DIV>
						   <html:errors />
						</TD> 
					</TR>
					<TR>
						<TD colspan="5">
							&nbsp;
						</TD>
					</TR>
					<TR>

						<TD width="15%">
							<STRONG> <FONT color="#ff0000"> * </FONT>&nbsp; Location
								Description: </STRONG>
						</TD>
						
						<TD width="35%">
						<logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						    <pdk-html:select name="facilityPORForm"  property="locationDescId" styleId="locationDescId" size="1">
	                           <option value="" selected="selected"></option>
	   	                       <logic:iterate id="locDesc" name="facilityPORForm" property="locationDescs" type="Entity">
					           <logic:match name="facilityPORForm" property="locationDescId" value='<%=locDesc.getKey()%>'>
						         <OPTION value="<%=locDesc.getKey()%>" selected="selected"><%=locDesc.getValue()%></OPTION>
					           </logic:match>
					          <logic:notMatch name="facilityPORForm" property="locationDescId" value='<%=locDesc.getKey()%>'>
						        <OPTION value="<%=locDesc.getKey()%>"><%=locDesc.getValue()%></OPTION>
					          </logic:notMatch> 		            	
                              </logic:iterate>
	                        </pdk-html:select>
	                     </logic:equal>
	                     <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                         <bean:write name="facilityPORForm" property="locationDesc"/>
	                     </logic:notEqual>   
						</TD>
						<TD width="15%" align="left">
							&nbsp;&nbsp;<STRONG> Source: </STRONG>
							
						</TD>
						<TD>
						    <span id="sourceName">
							   <bean:write name="facilityPORForm" property="sourceDesc"/>
							</span>   
						</TD>
						<TD></TD>
					</TR>
					<TR>
						<TD align="left">
							<strong> <FONT color="#ff0000"> * </FONT> &nbsp;Method: </strong>
						</TD>
						<TD>
						  <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						    <pdk-html:select name="facilityPORForm"  property="methodId" styleId="methodId" size="1" 
						         onchange="ChangeSource()">
	                           <option value="" selected="selected"></option>
	   	                       <logic:iterate id="methodCd" name="facilityPORForm" property="methods" type="Entity">
					           <logic:match name="facilityPORForm" property="methodId" value='<%=methodCd.getKey()%>'>
						         <OPTION value="<%=methodCd.getKey()%>" selected="selected"><%=methodCd.getValue()%></OPTION>
					           </logic:match>
					          <logic:notMatch name="facilityPORForm" property="methodId" value='<%=methodCd.getKey()%>'>
						        <OPTION value="<%=methodCd.getKey()%>"><%=methodCd.getValue()%></OPTION>
					          </logic:notMatch> 		            	
                              </logic:iterate>
	                        </pdk-html:select>
	                      </logic:equal>
	                      <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="methodDesc" />
	                      </logic:notEqual>  
						</TD>
						<!-- <td colspan="2">  -->
						<logic:notEqual name="facilityPORForm" property="source" value="E">
						<td align="left">
							<strong> <FONT color="#ff0000"> * </FONT>&nbsp;Measure Date:
							</strong>
						</td>
						<td>
						   <logic:equal name="facilityPORForm"  property="isMeasureDateUpdatable" value="Y">
						    <pdk-html:text name="facilityPORForm" property="measureDate" styleId="measureDate"
						         size="20" maxlength="50" />
							  &nbsp;
							<a href="javascript: void(0);" onclick="<%=showCalMeasureDate%>"><pdk-html:img page="/images/cal.jpg" border="0" /></a>
							&nbsp; (mm/dd/yyyy)
                           </logic:equal>
                           <logic:notEqual name="facilityPORForm"  property="isMeasureDateUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="measureDate"/>
	                      </logic:notEqual> 
						</td>						
						</logic:notEqual>						
						<logic:equal name="facilityPORForm" property="source" value="E">
						   <td>
						      <div id="md" style="DISPLAY:none">
						         <pdk-html:text name="facilityPORForm" property="measureDate" styleId="measureDate"  size="20" maxlength="50" />
						      </div>
						   </td><td></td>
						</logic:equal>
                        <td></td>
					</TR>
					<TR>
						<TD>
							<Strong> <FONT color="#ff0000"> * </FONT>&nbsp;Datum: </Strong>
						</TD>
						<TD>
						   <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						    <pdk-html:select name="facilityPORForm"  property="datumId" styleId="datumId" size="1"
						       onchange="ChangeSource()">
	                           <option value="" selected="selected"></option>
	   	                       <logic:iterate id="datumCd" name="facilityPORForm" property="datums" type="Entity">
					           <logic:match name="facilityPORForm" property="datumId" value='<%=datumCd.getKey()%>'>
						         <OPTION value="<%=datumCd.getKey()%>" selected="selected"><%=datumCd.getValue()%></OPTION>
					           </logic:match>
					          <logic:notMatch name="facilityPORForm" property="datumId" value='<%=datumCd.getKey()%>'>
						        <OPTION value="<%=datumCd.getKey()%>"><%=datumCd.getValue()%></OPTION>
					          </logic:notMatch> 		            	
                              </logic:iterate>
	                        </pdk-html:select>
	                       </logic:equal>
	                       <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="datumDesc"/>
	                      </logic:notEqual> 
						</TD>
						<!--  <TD colspan="2"> -->
						<TD align="left">
							&nbsp;&nbsp;<Strong> Scale: </Strong>
						</TD>
						<TD>
						   <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						    <pdk-html:text name="facilityPORForm" property="scale" styleId="scale" size="20" maxlength="20" />
						   </logic:equal>
						   <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="scale"/>
	                      </logic:notEqual>
						</TD>
						<TD></TD>
						
					</TR>
					<TR>
						<TD>
							<strong> <FONT color="#ff0000"> * </FONT>&nbsp;Latitude: </strong>
						</TD>
						<TD>
						   <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						   						   
						    <pdk-html:text name="facilityPORForm" property="latitude" value="<%=formatter1.format(facilityPORForm.getLatitude()) %>" 
						             styleId="latitude" size="15" onchange="ChangeSource()" />
							 &nbsp;
							 <%
				NameValue[] linkParams       = new NameValue[1];
				linkParams[0] = new NameValue("conType", "latitude");
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewConverterPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowConverter("<%=eventPopupWinowUrl %>")'">
				   <pdk-html:img page="/images/cal.gif" border="0"/></A>&nbsp;( DD.dddddd )
							
						   </logic:equal>
						   <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="latitude"/>
	                      </logic:notEqual> 	
						</TD>
						
						<TD align="left">
							<strong> <FONT color="#ff0000"> * </FONT>&nbsp;Latitude
								Direction: </strong>
						</TD>
						<TD>
						  <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y"> 
						    <pdk-html:radio name="facilityPORForm" property="latitudeDirec"  styleId="latitudeDirec" value="<%=FacilityPORForm.NORTH %>"
						       onchange="ChangeSource()"/>
						    <%=FacilityPORForm.NORTH %>
						    <pdk-html:radio name="facilityPORForm" property="latitudeDirec" styleId="latitudeDirec" value="<%=FacilityPORForm.SOUTH %>"
						       onchange="ChangeSource()"/>
						    <%=FacilityPORForm.SOUTH %>
						  </logic:equal>
						  <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
						     <logic:equal name="facilityPORForm" property="latitudeDirec" value="<%=FacilityPORForm.NORTH %>">
	                           NORTH
	                         </logic:equal> 
	                         <logic:equal name="facilityPORForm" property="latitudeDirec" value="<%=FacilityPORForm.SOUTH %>">
	                           SOUTH
	                         </logic:equal>
	                      </logic:notEqual>  
						</TD>
						<TD></TD>
					</TR>
					<TR>
						<TD>
							<strong> <FONT color="#ff0000"> * </FONT>&nbsp;Longitude: </strong>
						</TD>
						<TD>
						  <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						    <pdk-html:text name="facilityPORForm" property="longitude" value="<%=facilityPORForm.getLongitude() %>" styleId="longitude" size="15" 
						      onchange="ChangeSource()"/>
							&nbsp;
							<%
				              NameValue[] linkParams       = new NameValue[1];
				              linkParams[0] = new NameValue("conType", "longitude");
				              String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewConverterPage", linkParams,true, true);%>
				              <A href="javascript:void(0);" onclick='ShowConverter("<%=eventPopupWinowUrl %>")'">
				                 <pdk-html:img page="/images/cal.gif" border="0"/></A>&nbsp;( DD.dddddd )
							
						  </logic:equal>
						  <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	                          <bean:write name="facilityPORForm" property="longitude"/>
	                      </logic:notEqual> 	
						</TD>
                 						
						<TD align="left">
							<strong> <FONT color="#ff0000"> * </FONT>&nbsp;Longitude
								Direction: </strong>
						</TD>
						<TD>
						   <logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
						   <pdk-html:radio name="facilityPORForm" property="longitudeDirec"  styleId="longitudeDirec" value="<%=FacilityPORForm.EAST %>"
						      onchange="ChangeSource()"/>
						    <%=FacilityPORForm.EAST %>
						    <pdk-html:radio name="facilityPORForm" property="longitudeDirec"  styleId="longitudeDirec" value="<%=FacilityPORForm.WEST %>"
						      onchange="ChangeSource()"/>
						    <%=FacilityPORForm.WEST %>
						   </logic:equal>
						   <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
						      <logic:equal name="facilityPORForm" property="longitudeDirec" value="<%=FacilityPORForm.EAST %>">
	                             EAST
	                          </logic:equal>
	                          <logic:equal name="facilityPORForm" property="longitudeDirec" value="<%=FacilityPORForm.WEST %>">
	                             WEST
	                          </logic:equal>
	                      </logic:notEqual> 
						</TD>
						<TD>
						</TD>
					</TR>	                
				</table>
		  </TD>
		</TR>
	</TABLE>	
	</div>
	   <TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%" >	
		    <TR>
				<TD colspan="5" align="left">
			       <logic:equal  name="facilityPORForm" property="isTriTerritoryUpdatable" value="Y">	
	             	      <pdk-html:checkbox name="facilityPORForm" property="isInTribalTerritory" value="Y" />
				       <STRONG> &nbsp; Within Tribal Territory </STRONG>
				   </logic:equal>
				   <logic:notEqual name="facilityPORForm" property="isTriTerritoryUpdatable" value="Y">
					   <pdk-html:checkbox name="facilityPORForm" property="isInTribalTerritory" value="Y" disabled="true"/>
					  <STRONG> &nbsp; Within Tribal Territory </STRONG>
				    </logic:notEqual>
				</TD>
			</TR>	   
			<TR>
		        <TD>&nbsp;</TD>
		    	<TD></TD>
		        <TD></TD>
		        <TD></TD>
		        <TD></TD>
	        </TR>			   
		<logic:equal name="facilityPORForm"  property="isUpdatable" value="Y">
			<TR>
                  <TD colspan="3" align="left">
                     <div align="left" width="150">
                       <A href="javascript:PORConfirmAndSubmit();"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
	                    &nbsp;&nbsp;
	                   <A href="javascript:FacilityPORReset();"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0" /></FONT></A>
	                 </div>
		                  
		          </TD>  
	               </TR>
	    </logic:equal>
	    <logic:notEqual name="facilityPORForm"  property="isUpdatable" value="Y">
	          <logic:equal  name="facilityPORForm" property="isTriTerritoryUpdatable" value="Y">
	              <TR>
	                  <TD colspan="3" align="left">
	                      <div align="left" width="150">
		                  <A href="javascript:PORConfirmAndSubmit();"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
		                  &nbsp;&nbsp;
		                  <A href="javascript:FacilityPORReset();"><FONT size="1"><pdk-html:img page="/images/reset.gif" alt="Reset" border="0" /></FONT></A>
		                  
		                  </div>
		              </TD>  
	             </TR>    
	         </logic:equal>
	     </logic:notEqual>         
		</TABLE>
	
</pdk-html:form>
	<logic:present name="displayCoordinatesType">
	   <logic:equal name="displayCoordinatesType" value="Y">
		  <SCRIPT type=text/javascript>	   
	         hideCoBody();
	      </SCRIPT>
	   </logic:equal>
	 </logic:present>
<pdk-html:javascript formName="facilityPORForm" />

