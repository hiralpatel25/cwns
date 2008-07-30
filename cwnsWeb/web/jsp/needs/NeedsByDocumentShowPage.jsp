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
	import="gov.epa.owm.mtb.cwns.needs.NeedsForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.needs.NeedsDocumentGroupTypeHelper"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	import="gov.epa.owm.mtb.cwns.common.util.Password"
	import="java.net.URLEncoder"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			
   			NeedsForm needsForm = (NeedsForm)request.getAttribute("needsform");
   			Collection documentGroupTypeList0	= (Collection)request.getAttribute("documentGroupTypeList");
   			
		    String showCalPublishedDate =null;
		    String showCalBaseMonthYear =null;

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		    String toolTipurl = url +"/javascript/tooltip.js";
		    String popurl = url +"/javascript/popcalendar.js";
		    //String dateurl = url +"/javascript/date.js";
		    String imgurl = url +"/images/";
		    String webtopurl = CWNSProperties.getProperty("edms.webtop.url");
		    String baseurl = CWNSProperties.getProperty("edms.annotation.url","");
		    String annotationNextUrl = URLEncoder.encode(CWNSProperties.getProperty("edms.annotation.nextUrl",""), "UTF-8"); 
		    String viewNextUrl = URLEncoder.encode(CWNSProperties.getProperty("edms.view.nextUrl",""), "UTF-8");
		    String docbaseName = CWNSProperties.getProperty("edms.docbaseName","");
		    String userName = CWNSProperties.getProperty("edms.annotate.userName","");
		    String password = CWNSProperties.getProperty("edms..annotate.password","");
		    String viewuserName = CWNSProperties.getProperty("edms.view.userName","");
		    String viewpassword = CWNSProperties.getProperty("edms.view.password","");
		    String annotateurl =baseurl +"?docbaseName="+docbaseName+"&userName="+userName+"&sessionId="+Password.encrypt(password)+"&nexturl="+ annotationNextUrl;
		    String viewurl =baseurl +"?docbaseName="+docbaseName+"&userName="+viewuserName+"&sessionId="+Password.encrypt(viewpassword)+"&nexturl="+ annotationNextUrl;
	        showCalPublishedDate = "showCalendar(this,document.NeedsFormBeanId.publishedDate, 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
	        showCalBaseMonthYear = "showCalendar(this,document.NeedsFormBeanId.baseMonthYear, 'm/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";

%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,"../../css/cwns.css")%>">
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
function NeedsConfirmAndSubmit(action, docId, hasCost)
{
   if (action == "cancel"){
   }
   
   else if(action == "delete")
   {
    if(hasCost == "Y")
    {
     var result = confirm("Cost information exist for this Document and will be deleted. \n" + 
     						"Continue with Delete?");
      if (result == false)
      {
		return;
      }
    } 
   	  setNeedsDocumentId(docId);
   		      
   }
   else if(action == "edit")
   {
   		setNeedsDocumentId(docId);
   }
   else if(action == "expandCost")
   {
        setNeedsDocumentId(docId);
   }
   else if(action == "save")
   {
	  var formId = window.document.getElementById("NeedsFormBeanId");
	  var dType = window.document.getElementById("documentType");
	  <%if(!"Y".equalsIgnoreCase(needsForm.getIsLocalUser())){%>
	      if (dType.value==""){
	         alert("Document Type is required");
	         return;
	      }
	  <%}%>
	    
	  if(formId != undefined)
	  {
	    if(validateNeedsFormBean(formId) == false)
	    {
	      return;
	    }
	  }
      var narrativeText = window.document.getElementById("narrativeText");	  
      <logic:notEqual name="NeedsFormBean" property="isLocalUser" value="Y">
      // validate narrative text
      	
	  if(window.document.getElementById("ongoingDialogCheckbox").checked == true)
	  {
	  	if(trim(narrativeText.value) == "")
	  	{
	  	  alert("Please explain in Narrative!");
	  	  narrativeText.focus();
	  	  return;
	  	}
	  }
	  </logic:notEqual>
	  <logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
      // validate narrative text
      	if(trim(narrativeText.value) == "")
	  	{
	  	  alert("Please explain in Narrative!");
	  	  narrativeText.focus();
	  	  return;
	  	}
	  </logic:equal>
	  narrativeText.value = trim(narrativeText.value);
	  
	  // validate published dates
	  
	  var currentDate=new Date();
	  var pblshdDateCtl = window.document.getElementById("publishedDate");
	  var bsmthYearCtl = window.document.getElementById("baseMonthYear");
	
	  var pblshdDate = Date.parse(pblshdDateCtl.value);
	  
	  if (currentDate < pblshdDate)
	  {
	   	  alert("The published date must be earlier than the current date.");
	  	  pblshdDateCtl.focus();
	  	  return;
	  }

	  // validate base month year

	  var d1 = bsmthYearCtl.value.split("/");
	  var badBaseMonthyear = "N";
	  
	  if(d1.length != 2)
	  {
	    badBaseMonthyear = "Y";
	    alert("The base month and year must be in the format of mm/yyyy.");
		  badBaseMonthyear = "Y";
	  	  bsmthYearCtl.focus();	    
	  }
	  else
	  {
	    if(d1[0].length == 1)
	    {
	      d1[0] = "0" + d1[0];
	    }
	    
	    var d2 = d1[0] + "/" + "01" + "/" + d1[1];
	    var bsmthYear = Date.parse(d2);	  
		if(!isDate(d2)){
		  alert("The base month and year must be in the format of mm/yyyy.");
		  badBaseMonthyear = "Y";
	  	  bsmthYearCtl.focus();
	  	  return;  
		}else if(pblshdDate < bsmthYear){
	   	  alert("Document base date must be equal or older than document published date.");
	   	  badBaseMonthyear = "Y";
	  	  bsmthYearCtl.focus();
	  	  return;	   	  
	    }  
	  }
	  
	  var yy = window.document.getElementById("needsStartYear");
	  var ty = window.document.getElementById("targetDesignYear");
	  if(yy!=null && trim(yy.value) == ""){
	    alert("Needs Start Year is Required");
	    return;
	  }
	  if(yy!=null && (!isAllDigits(trim(yy.value)) || 
	  	(trim(yy.value).length<4 || trim(yy.value).length>4))){
	  	alert("Needs Start Year must be a number (all digits) and 4 digits long.");
	  	return;
	  }
	  
	  if(ty!=null && trim(ty.value) == ""){
	    alert("Target Design Year is Required");
	    return;
	  }	
	  
	  if(ty!=null && (!isAllDigits(trim(ty.value)) || 
	  	(trim(ty.value).length<4 || trim(ty.value).length>4))){
	  	alert("Target Design Year must be a number (all digits) and 4 digits long.");
	  	return;
	  }  
	  // all validation passed
	 
   	//var yy = window.document.getElementById("needsStartYear");
   	if(yy!=null)
   	  yy.disabled = false;

   	//yy = window.document.getElementById("targetDesignYear");
   	if(ty!=null)
   	  ty.disabled = false;   	  
    
	window.document.getElementById("publishedDate").disabled = false;
	window.document.getElementById("baseMonthYear").disabled = false;   	     	  
   }
   
       hidden_field = window.document.getElementById("needsAct");
	   hidden_field.value=action;
	   window.document.NeedsFormBean.submit();
	   return true;
}

function setNeedsDocumentId(docId)
{
	window.document.getElementById("documentId").value = docId;
}
function uploadDocument(rowid){

	var elem = document.getElementById('iframe');
	var vis = elem.style;
	vis.display = 'block';
		
	var sf = document.NeedsFormBean;
	
	s = new String(window.location.href);
	
	if(s.indexOf('?') < 1) {
		
		s = s + '?_pageid=' + sf._pageid.value + '&_dad=' + sf._dad.value + '&_schema=' + sf._schema.value + '&facilityId=<%=needsForm.getNeedsFacilityId()%>&navigationTabType=' + sf.navigationTabType.value
				 
	}	
	
	var str = escape(s);
	<% String iframeUrl = "http://" + request.getServerName()+ ":"+ request.getServerPort()+"/cwns/jsp/needs/DocumentUpload.jsp";%>
	document.getElementById('uploadFrame').src='<%=iframeUrl%>?vurl=' + str + '&rowid=' + rowid;	
}

function closeWindow(){
	var elem = document.getElementById('iframe');
	var vis = elem.style;
	vis.display = 'none';
	//window.close();
	}

function setDocID(docid){
	frames['uploadFrame'].document.uploadfrm.rowID.value=docid;
}

// Show or Hide the add comment area of the Portlet
function showOrHideNeedsDocDetails(id, mode)
{
     var d = window.document.getElementById(id);
     
     if(d!=null)
       d.style.display=mode;
}

   //set all the document types
   var documentTypeArray = new Array();

	<%
		Iterator iter = documentGroupTypeList0.iterator();
	
	   	long oldGroupId = -999;
	   	
	   	long maxGroup = 0;
	   	    	
	   	while ( iter.hasNext() ) 
	   	{
	   	    NeedsDocumentGroupTypeHelper ndgth = (NeedsDocumentGroupTypeHelper) iter.next();
	   		
	   		if(oldGroupId != ndgth.getGroupId())
	   		{
	%>
				documentTypeArray[<%=ndgth.getGroupId()%>] = new Array();
    <%
    		}
    %>
			documentTypeArray[<%=ndgth.getGroupId()%>][<%=ndgth.getGroupIndex()%>] = new Array();
			documentTypeArray[<%=ndgth.getGroupId()%>][<%=ndgth.getGroupIndex()%>][0] = "<%=ndgth.getGroupTypeId()%>";
			documentTypeArray[<%=ndgth.getGroupId()%>][<%=ndgth.getGroupIndex()%>][1] = "<%=ndgth.getGroupTypeDesc()%>";
	<%
	   		oldGroupId = ndgth.getGroupId();
	   		
	   		maxGroup = (oldGroupId>maxGroup)?oldGroupId:maxGroup;
	    }
	%>

function setDocumentTypes(form)
{
  		var docTypeList=form['documentType'];

		docTypeList.options.length = 0;
	
  		docTypeList.options[0] = new Option("Select a Document Type", "");
		
  		var docGroupList=form['documentGroupType'];
  		var groupIdSelected = docGroupList.options[docGroupList.selectedIndex].value;
  		
  		//alert("documentTypeArray[groupIdSelected].length=" + documentTypeArray[groupIdSelected].length);
  		
  		var docLength = documentTypeArray[groupIdSelected].length;
  		
  		for(iDoc=0; iDoc<docLength; iDoc++)
  		{
  			//alert(documentTypeArray[groupIdSelected][iDoc][0] + " - " + documentTypeArray[groupIdSelected][iDoc][1]);
  			docTypeList.options[iDoc+1] = new Option(documentTypeArray[groupIdSelected][iDoc][0] + " - " + documentTypeArray[groupIdSelected][iDoc][1], documentTypeArray[groupIdSelected][iDoc][0]);
  			docTypeList.options[iDoc+1].title = documentTypeArray[groupIdSelected][iDoc][0] + " - " + documentTypeArray[groupIdSelected][iDoc][1];
  		}	
}
function popDocument(objid) {
day = new Date();
id = day.getTime();
var URL = '<%=webtopurl%>/action/view?&objectId=' + objid;
eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=820,height=700,left = 230,top = 162');");
}

function annotateDocument(docid) {
day = new Date();
id = day.getTime();
<%if ("Y".equalsIgnoreCase(needsForm.getIsAnnotate())){%>
   s = new String('<%=annotateurl%>');
<%}else{%>
   s = new String('<%=viewurl%>');
   <%}%>   
var URL = s.replace("%3C%3Cdocid%3E%3E",docid);
//alert(URL);
eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=1,menubar=0,resizable=1,width=820,height=700,left = 230,top = 162');");
}

// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strMonth=dtStr.substring(0,pos1)
	var strDay=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	iyear=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : mm/dd/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(iyear)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	
	if (strYear.length != 4 || iyear==0){
		alert("Please enter a valid 4 digit year");
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
} 
</SCRIPT>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=toolTipurl %>"></script>
<%--<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>--%>
<SCRIPT type=text/javascript>
	setImgDir('<%=imgurl%>');
</script>	

<pdk-html:form 	name="NeedsFormBean" styleId="NeedsFormBeanId"
				type="gov.epa.owm.mtb.cwns.needs.NeedsForm"
				action="needs.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="needsAct" property="needsAct" value="<%=needsForm.getNeedsAct()%>"/>
		<pdk-html:text styleId="documentId" property="documentId" value="<%=needsForm.getDocumentId()%>"/>
		<pdk-html:text styleId="needsFacilityId" property="needsFacilityId" value="<%=needsForm.getNeedsFacilityId()%>"/>
		<pdk-html:text styleId="rowID" property="rowID" value=""/>
	</DIV>
<div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="warnMessage" name="warnMessages" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/warn24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=warnMessage %>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>     

<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

	<TR>
		<TD class="PortletHeaderColor" width="25%" colspan="2">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Document&nbsp; 
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor" >
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG>Type</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Published Date </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor" width="10%">
			<DIV align="center">

				<FONT color="#ffffff"> <STRONG> Author </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Need Type </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Adjusted Total($) </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Submit<br/>Updated<br/>Document </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Footnotable </STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Engineer's<br/>Certification<br/>of Outdated<br/>Document</STRONG>
				</FONT>
			</DIV>
		</TD>
        <logic:notEqual name="NeedsFormBean" property="isUpdatable" value="Y">
			<TD class="PortletHeaderColor">
				<DIV align="left">
					<FONT color="#ffffff"> <STRONG>Details</STRONG>
					</FONT>
				</DIV>
			</TD>        
         </logic:notEqual>		
        <logic:equal name="NeedsFormBean" property="isUpdatable" value="Y">
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
         <logic:notEqual name="NeedsFormBean" property="isUpdatable" value="Y">
			<logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
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
	<logic:iterate id="needsHelper" scope="request" name="needsInfoList" type="gov.epa.owm.mtb.cwns.needs.NeedsHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
				
			<logic:equal name="needsHelper" property="documentId" value="<%=needsForm.getDocumentId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		    </logic:equal>

	<TR class="<c:out value="${class}"/>">
	<TD>
	<logic:equal name="needsHelper" property="isDocumentUpdatable" value="Y">
	<logic:equal name="NeedsFormBean" property="isUpdatable" value="Y">
		<logic:notEqual name="needsHelper" property="repositoryId" value="">
		        <logic:equal name="needsHelper" property="isDocumentReload" value="Y">				
				    <A href="javascript:uploadDocument('<bean:write name="needsHelper" property="documentId"/>');"><img src="<%=imgurl%>upload3.gif" alt="Reload Document" border="0"/></A>&nbsp;
				</logic:equal>    
		</logic:notEqual>		
		<logic:equal name="needsHelper" property="repositoryId" value="">
		      <A href="javascript:uploadDocument('<bean:write name="needsHelper" property="documentId"/>');"><img src="<%=imgurl%>upload3.gif" alt="Upload Document" border="0"/></A>
		</logic:equal>
	</logic:equal>	
	</logic:equal>	
	<logic:notEqual name="needsHelper" property="repositoryId" value="">
	   <A href="javascript:annotateDocument('<bean:write name="needsHelper" property="repositoryId"/>');"><img src="<%=imgurl%>annotate2.jpg" alt=<%="Y".equalsIgnoreCase(needsForm.getIsUpdatable())?"Annotate Document":"view Document"%> border="0"/></A>
	</logic:notEqual>
	</TD>	
		<TD align="center" width="25%">
			<P align="left">			
			    <A href="javascript:NeedsConfirmAndSubmit('expandCost', '<bean:write name="needsHelper" property="documentId"/>', 'N')">
			    <bean:write name="needsHelper" property="documentTitle"/>
			    </A>
			</P>
		</TD>
		<TD align="center">
			<P align="center" title="<bean:write name="needsHelper" property="documentTypeDesc"/>">
				      <bean:write name="needsHelper" property="documentTypeId"/>
			</P>                                                                                                                                                                                                                                    
		</TD>
		<TD align="center">
			<P align="center">
				<bean:write name="needsHelper" property="publishedDate"/>
			</P>
		</TD>
		<TD align="left" valign="center">
				<bean:write name="needsHelper" property="authorName"/>
		</TD>
		<TD align="center">
			<logic:equal name="needsHelper" property="hasFederalAmount" value="Y">
			Official
			</logic:equal>
			<logic:equal name="needsHelper" property="hasSSEAmount" value="Y">
				<logic:equal name="needsHelper" property="hasFederalAmount" value="Y">
					<br/>
				</logic:equal>			
			Unofficial
			</logic:equal>
		</TD>
		<TD align="right" valign="center">
			<logic:equal name="needsHelper" property="hasFederalAmount" value="Y">
			      <fmt:formatNumber type="currency" pattern="$#,##0">
			      	<bean:write name="needsHelper" property="federalAmount"/>
			      </fmt:formatNumber>			
			</logic:equal>
			<logic:equal name="needsHelper" property="hasSSEAmount" value="Y">
				<logic:equal name="needsHelper" property="hasFederalAmount" value="Y">
					<br/>
				</logic:equal>		
			      <fmt:formatNumber type="currency" pattern="$#,##0">
			      	<bean:write name="needsHelper" property="sseAmount"/>
			      </fmt:formatNumber>					
			</logic:equal>
		</TD>
		<TD align="center">
			<logic:equal name="needsHelper" property="submitFlag" value="Y">
				<P align="center">
					<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
				</P>
			</logic:equal>
		</TD>
		<TD align="center">
		<logic:equal name="needsHelper" property="footnoteableFlag" value="Y">
		   <logic:lessThan name="NeedsFormBean" property="federalAdjustedAmountTotal" value="20000000" >
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</P>
		   </logic:lessThan>
		</logic:equal>
		</TD>
		<TD align="center">
		<logic:equal name="needsHelper" property="outdatedDocCertificatnFlag" value="Y">
			<P align="center">
				<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			</P>
		</logic:equal>
		</TD>

        <logic:notEqual name="NeedsFormBean" property="isUpdatable" value="Y">
			<TD align="center">
				<P align="center">
					<A href="javascript:NeedsConfirmAndSubmit('edit', '<bean:write name="needsHelper" property="documentId"/>', 'N')">
						<pdk-html:img page="/images/plus.gif" alt="Show Details" border="0"/>
					</A>
				</P>
			</TD>
         </logic:notEqual>
         
        <logic:equal name="NeedsFormBean" property="isUpdatable" value="Y">
			<TD align="center">
				<P align="center">
				<A href="javascript:NeedsConfirmAndSubmit('edit', '<bean:write name="needsHelper" property="documentId"/>', 'N')">
					<logic:equal name="needsHelper" property="isDocumentUpdatable" value="Y">
					<pdk-html:img page="/images/edit.gif" alt="Edit" border="0"/>
					</logic:equal>
					<logic:equal name="needsHelper" property="isDocumentUpdatable" value="N">
					<pdk-html:img page="/images/plus.gif" alt="Show Details" border="0"/>
					</logic:equal>
				</A>
				</P>
			</TD>
			
			<logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
				<TD>
					<P align="center">
					    <bean:define id="documentId"><bean:write name="needsHelper" property="documentId"/></bean:define>
					    <bean:define id="hasCost"><bean:write name="needsHelper" property="hasCost"/></bean:define>
					    <bean:define id="delete">
					      javascript:NeedsConfirmAndSubmit('delete', '<bean:write name="needsHelper" property="documentId"/>', '<bean:write name="needsHelper" property="hasCost"/>')
					    </bean:define>
					    <pdk-html:checkbox name="needsHelper" property="feedbackDeleteFlag" value="Y" 
					                  onclick='<%=delete%>'>
					    </pdk-html:checkbox>
					</P>
				</TD>
			</logic:equal>
			<logic:notEqual name="NeedsFormBean" property="isLocalUser" value="Y">
			<TD align="center">
				<P align="center">
				<A href="javascript:NeedsConfirmAndSubmit('delete', '<bean:write name="needsHelper" property="documentId"/>', '<bean:write name="needsHelper" property="hasCost"/>')">
					<pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
				</A>
				</P>
			</TD>
			</logic:notEqual>
         </logic:equal>
         <logic:notEqual name="NeedsFormBean" property="isUpdatable" value="Y">
         <logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
				<TD>
					<P align="center">
					    <pdk-html:checkbox name="needsHelper" property="feedbackDeleteFlag" value="Y" 
					                  disabled="true">
					    </pdk-html:checkbox>
					</P>
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
	
	<c:choose>
		<c:when test='${i>"1"}'>	
			<TR class="<c:out value="${class}"/>">
			<TD>&nbsp;</TD>
				<TD valign="top" aligh="left">
					<STRONG>Total</STRONG>
				</TD>
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<TD valign="top" align="center">
					<STRONG>
					<logic:greaterThan name="NeedsFormBean" property="federalAdjustedAmountTotal" value="0" >
						Official
					</logic:greaterThan>
					<logic:greaterThan name="NeedsFormBean" property="sseAdjustedAmountTotal" value="0">
						<logic:greaterThan name="NeedsFormBean" property="federalAdjustedAmountTotal" value="0" >
							<br/>
						</logic:greaterThan>
						Unofficial
					</logic:greaterThan>			
					</STRONG>
				</TD>
				<TD>
					<P align="right">
						<STRONG>
					<logic:greaterThan name="NeedsFormBean" property="federalAdjustedAmountTotal" value="0" >
				      <fmt:formatNumber type="currency" pattern="$#,##0">
				      	<bean:write name="NeedsFormBean" property="federalAdjustedAmountTotal"/>
				      </fmt:formatNumber>								
					</logic:greaterThan>
					<logic:greaterThan name="NeedsFormBean" property="sseAdjustedAmountTotal" value="0">
						<logic:greaterThan name="NeedsFormBean" property="federalAdjustedAmountTotal" value="0" >
							<br/>
						</logic:greaterThan>
				      <fmt:formatNumber type="currency" pattern="$#,##0">
				      	<bean:write name="NeedsFormBean" property="sseAdjustedAmountTotal"/>
				      </fmt:formatNumber>							
					</logic:greaterThan>				
						</STRONG>
					</P>
				</TD>
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<logic:equal name="NeedsFormBean" property="isUpdatable" value="Y">				
				<TD></TD>				
				</logic:equal>				
			</TR>	
		</c:when>
	</c:choose>	
	<TR>	
		<TD>
		&nbsp;
		</TD>
	</TR>	
<%
    String showDetail = "none";
	String showAddLink = "block";
	
    if((needsForm.getDetailEditExpand()).equals("Y"))
    {
      showDetail = "block";
      showAddLink = "none";
    }
%>
	
        <logic:equal name="NeedsFormBean" property="isUpdatable" value="Y">
			<TR>
				<TD colspan="11" align="left">
					 <DIV id="addNewDocLink" style="display:<%=showAddLink%>" class="PortletText1">
						<a href="javascript: setNeedsDocumentId(''); NeedsConfirmAndSubmit('new', '', 'N');">
							Add Document
						</a>
					 </DIV>
				</TD>
			</TR>
         </logic:equal>
		<TR>	
			<TD>
			&nbsp;
			</TD>
		</TR>	
</TABLE>
<div id="iframe" style="display:none">
<iframe name="uploadFrame" src="" style="width:900;height:150;" frameborder="0"></iframe>
</div>
<div id="images">
</div>

<DIV id="docDetails" style="display:<%=showDetail%>; background-color: #cccccc; padding: 5"  class="PortletText1">
<BR>
		<P>&nbsp;<Strong>
		   <FONT size="4">
					  Document Details&nbsp;
		   </FONT>&nbsp;</Strong>
		</P>

		<TABLE border="0" class="PortletText1" cellspacing="1" cellpadding="0" width="100%" 
		          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
			<TR>
				<logic:notEqual name="NeedsFormBean" property="isLocalUser" value="Y">
					<TD><span class="required">*</span></TD>
				</logic:notEqual>
				<logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
					<td>&nbsp;</td>
				</logic:equal>
                <TD><STRONG> Document Type: </STRONG></TD>
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="Y">	
						<pdk-html:select onmouseover="checkEvent(this.event, 'tooltipDocTypeSelect', this.options[this.selectedIndex].title);" 				
						                 onmouseout="checkEvent(this.event, 'tooltipDocTypeSelect', '');" 
										 name="NeedsFormBean" 
										 styleId="documentType" 
										 property="documentType" 
										 size="1" 
										 style="width: 300px">
						<OPTION value="">
							Select a Document Type
						</OPTION>					
							<logic:iterate id="documentGroupTypeList" name="documentGroupTypeList">
									 <bean:define id="groupTypeId" name="documentGroupTypeList" property="groupTypeId"/> 
									 <bean:define id="groupTypeDesc" name="documentGroupTypeList" property="groupTypeDesc"/> 
								<logic:equal name="documentGroupTypeList" property="groupId" value="0">
									<logic:equal name="documentGroupTypeList" property="groupTypeId" value="<%=needsForm.getDocumentType()%>">
										<OPTION SELECTED title="<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>"
										        value="<bean:write name="documentGroupTypeList" property="groupTypeId"/>">
											<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>
										</OPTION>
								    </logic:equal>
									<logic:notEqual name="documentGroupTypeList" property="groupTypeId" value="<%=needsForm.getDocumentType()%>">
										<OPTION title="<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>"
										        value="<bean:write name="documentGroupTypeList" property="groupTypeId" />">
											<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>
										</OPTION>
								    </logic:notEqual>
							    </logic:equal>						
							</logic:iterate>					
						</pdk-html:select>
						&nbsp;
						<STRONG> Filter: </STRONG>&nbsp;
						
						<%
						   if(maxGroup > 0)
						   {
						 %>
						
						<pdk-html:select name="NeedsFormBean" styleId="documentGroupType" property="documentGroupType" 
						  onchange="setDocumentTypes(this.form);" size="1">
							<OPTION value="0" selected="selected">
								 All
							</OPTION>	
						<logic:iterate id="documentGroupList" name="documentGroupList">
							<OPTION value="<bean:write name="documentGroupList" property="groupId" />">
								 <bean:write name="documentGroupList" property="groupDesc" />
							</OPTION>						
						 </logic:iterate>					
						 </pdk-html:select>	
						 
						 <%
						 	}
						 	else
						 	{
						  %>
						<pdk-html:select name="NeedsFormBean" styleId="documentGroupType" property="documentGroupType" 
						  disabled="true" size="1">
							<OPTION value="0" selected="selected">
								 All
							</OPTION>	
						 </pdk-html:select>							  
						  <%
						    }
						   %>
					  </logic:equal>
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="N">
						<bean:write name="NeedsFormBean" property="documentType" />&nbsp;-&nbsp;
						<bean:write name="NeedsFormBean" property="documentTypeDesc"/>					  
					    <pdk-html:hidden name="NeedsFormBean" styleId="documentType" property="documentType"/>
					  </logic:equal>
					</logic:equal>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
					    <pdk-html:hidden name="NeedsFormBean" styleId="documentType" property="documentType"/>
						<bean:write name="NeedsFormBean" property="documentType" />&nbsp;-&nbsp;
						<bean:write name="NeedsFormBean" property="documentTypeDesc"/>
					</logic:equal>	
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Published Date: </STRONG>
				</TD>
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">				
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="Y">
						<pdk-html:text name="NeedsFormBean"  styleId="publishedDate" property="publishedDate" size="12" 
						       maxlength="12"/>
						<a href="javascript: void(0);" onclick="<%=showCalPublishedDate%>"> <pdk-html:img page="/images/cal.jpg" border="0"/></a>
						&nbsp; (mm/dd/yyyy)
					  </logic:equal>
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="N">
					    <pdk-html:hidden name="NeedsFormBean" styleId="publishedDate" property="publishedDate"/> 
						<bean:write name="NeedsFormBean" property="publishedDate"/>
					  </logic:equal>					  
					</logic:equal>		
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
						<bean:write name="NeedsFormBean" property="publishedDate" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
					</logic:equal>							
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Document Title: </STRONG>
				</TD>
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">					
					<pdk-html:text name="NeedsFormBean" styleId="title" property="title" size="80" 
					     maxlength="80"/> &nbsp; 
				    <!-- pdk-html:img page="/images/findsmall.gif" border="0" alt="Look For Document Title"/ -->
				    </logic:equal>	
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
						<bean:write name="NeedsFormBean" property="title" />
					</logic:equal>											    
				 </TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD><STRONG> Document  Author: </STRONG>
				</TD>
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">						
						<pdk-html:text name="NeedsFormBean" styleId="author" property="author" size="80" maxlength="80"/>
				    </logic:equal>	
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
						<bean:write name="NeedsFormBean" property="author" />
					</logic:equal>			
				</TD>
			</TR>																					
			<TR>
				<TD></TD>
				<TD></TD>				
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">	
						<pdk-html:checkbox name="NeedsFormBean" property="othersCheckbox" value="Y" styleId="othersCheckbox"/>
						<STRONG> This document can be shared with the public </STRONG>
					</logic:equal>		
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
						<pdk-html:checkbox name="NeedsFormBean" property="othersCheckbox" value="Y" disabled="true" styleId="othersCheckboxDisabled"/>
						<STRONG> This document can be shared with the public </STRONG>
					</logic:equal>
				</TD>
			</TR>
			<logic:notEqual name="NeedsFormBean" property="isLocalUser" value="Y">
			<TR>
				<TD></TD>
				<TD></TD>				
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">	
						<pdk-html:checkbox name="NeedsFormBean" property="ongoingDialogCheckbox" value="Y" styleId="ongoingDialogCheckbox" />
						<STRONG>Engineer's Certification of outdated document</STRONG>
					</logic:equal>		
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
						<pdk-html:checkbox name="NeedsFormBean" property="ongoingDialogCheckbox" value="Y" disabled="true" styleId="ongoingDialogCheckboxDisabled" />
						Engineer's Certification of outdated document
					</logic:equal>				
				</TD>	
			</TR>
			</logic:notEqual>
			
			<TR>
			    <TD>&nbsp;</TD>
			    <logic:notEqual name="NeedsFormBean" property="isLocalUser" value="Y">
				<TD><STRONG> Narrative: </STRONG></TD>
				</logic:notEqual>
				<logic:equal name="NeedsFormBean" property="isLocalUser" value="Y">
				<TD><STRONG>Project Description <br>Associated with the <br>Capital Cost <br>(See Below):</STRONG></TD>
				</logic:equal>
				<TD>
				<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">				
					<pdk-html:textarea name="NeedsFormBean" styleId="narrativeText" property="narrativeText" 
					    rows="5" cols="60">
					</pdk-html:textarea>
					<br/>
					Describe how this document relates to Needs being entered, referencing relevant page/section numbers.
				</logic:equal>							
				<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">	
				<table>
				<tr><td>
					<pdk-html:textarea styleClass="readOnlyTextArea" name="NeedsFormBean" styleId="narrativeText" property="narrativeText" 
					    rows="5" cols="60" readonly="true">
					</pdk-html:textarea>
				</td></tr>
				</table>
				</logic:equal>	
				</TD>
			</TR>
			
			<%
			   if(!"Y".equalsIgnoreCase(needsForm.getIsLocalUser())){
				if((needsForm.getNeedsStartYear()!=null && needsForm.getNeedsStartYear().length()>0)
					|| (needsForm.getTargetDesignYear()!=null && needsForm.getTargetDesignYear().length()>0))
				{
			 %>
			<TR>
				<TD>&nbsp;</TD>
				<TD><STRONG><STRONG> Needs Start Year: </STRONG></TD>
				<TD>
					<bean:write name="NeedsFormBean" property="needsStartYear"/>											     
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
					<STRONG> Target Design Year: </STRONG>&nbsp;&nbsp;
					<bean:write name="NeedsFormBean" property="targetDesignYear"/>															     					
				</TD>
			</TR>
			<%
				}
				}else if("Y".equalsIgnoreCase(needsForm.getIsLocalUser())){
			 %>
			 <TR>
				<TD><span class="required">*</span></TD>
				<TD><STRONG><STRONG> Needs Start Year: </STRONG></TD>
				<TD>
				    <logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">
					 <logic:equal name="NeedsFormBean" property="isneedsStartYearUpdatable" value="Y">	
						<pdk-html:text name="NeedsFormBean" styleId="needsStartYear" property="needsStartYear" 
						     size="4" maxlength="4"/>
					 </logic:equal>
					 <logic:equal name="NeedsFormBean" property="isneedsStartYearUpdatable" value="N">	
						<bean:write name="NeedsFormBean" property="needsStartYear"/>
					 </logic:equal>	
					</logic:equal>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
					  <bean:write name="NeedsFormBean" property="needsStartYear"/>	
					</logic:equal> 	
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="required">*</span>	
				<STRONG> Target Design Year: </STRONG>&nbsp;&nbsp;
				   <logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">
					<logic:equal name="NeedsFormBean" property="isneedsStartYearUpdatable" value="Y">	
						<pdk-html:text name="NeedsFormBean" styleId="targetDesignYear" property="targetDesignYear" 
						      size="4" maxlength="4"/>				
					</logic:equal>	
					<logic:equal name="NeedsFormBean" property="isneedsStartYearUpdatable" value="N">	
						<bean:write name="NeedsFormBean" property="targetDesignYear"/>
					</logic:equal>	
				   </logic:equal>
				   <logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">
				      <bean:write name="NeedsFormBean" property="targetDesignYear"/>
				   </logic:equal>											     					
				</TD>
			</TR>
			 <%} %>			
			<TR>
				<TD valign="top">
					<span class="required">*</span></TD><TD valign="top"><STRONG>Base Month/Year<br/> of Cost Information</STRONG>
				</TD>
				<TD>
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="Y">
						<pdk-html:text name="NeedsFormBean" styleId="baseMonthYear" property="baseMonthYear" 
							size="12" maxlength="7"/> 
						 <a href="javascript: void(0);" onclick="<%=showCalBaseMonthYear%>"> 
						   <pdk-html:img page="/images/cal.jpg" border="0"/></a>&nbsp;&nbsp;(mm/yyyy)
				      </logic:equal>
					  <logic:equal name="NeedsFormBean" property="datesUpdatable" value="N">
						<pdk-html:hidden name="NeedsFormBean" styleId="baseMonthYear" property="baseMonthYear"/> 
						<bean:write name="NeedsFormBean" property="baseMonthYear"/>
				      </logic:equal>				      
					</logic:equal>	
					<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="N">	
						<bean:write name="NeedsFormBean" property="baseMonthYear"/>
					</logic:equal>											     										 
				</TD>
			</TR>
			<TR>
				<TD colspan="3">
				&nbsp;
				</TD>
			</TR>
			
			<TR>
				<TD colspan="3">
				<div align="left" width="150">
				<logic:equal name="NeedsFormBean" property="isDetailUpdatable" value="Y">			  
					<A href="javascript:NeedsConfirmAndSubmit('save', '', 'N')">
					<pdk-html:img page="/images/submit.gif" alt="save" border="0"/></A>				   
				   <FONT size="1">&nbsp;&nbsp;</FONT>
				</logic:equal>				   
					<%--<A href="javascript:{showOrHideNeedsDocDetails('addNewDocLink', 'block'); showOrHideNeedsDocDetails('docDetails', 'none')}">--%>
					<A href="javascript:NeedsConfirmAndSubmit('cancel', '', '')">
					<FONT size="1">
					<pdk-html:img page="/images/cancel.gif" alt="Cancel" border="0"/>
					</FONT>
					</A>
				</div>			   
   			   </TD>
			</TR>
		</TABLE>
</DIV>
<span id="tooltipDocTypeSelect"></span>

</pdk-html:form>

<SCRIPT type=text/javascript>
window.onload=function() 
{
	<%
		if(needsForm.getIsBaseMonthYearValid().equals("N"))
		{
	%>
			alert("Error: Base Month/Year not found in Construction Cost Index..");
			var bsmthYearCtl = window.document.getElementById("baseMonthYear");
			bsmthYearCtl.focus();
	<%
		} %>

}
</script>	
<pdk-html:javascript formName="NeedsFormBean" staticJavascript="true"/>