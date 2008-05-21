<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
        import="gov.epa.owm.mtb.cwns.documentSearch.DocumentSearchForm"
        import="java.util.Collection"
        import="gov.epa.owm.mtb.cwns.common.CWNSProperties"        
        import="gov.epa.owm.mtb.cwns.common.util.Password"
       	import="java.net.URLEncoder"
%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
      
   DocumentSearchForm docForm = (DocumentSearchForm)request.getAttribute("docForm");
   Collection searchResults = (Collection)request.getAttribute("searchResults");
   
   
   if(docForm == null) docForm = new DocumentSearchForm();
   
   
   	String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
   	String popurl = url +"/javascript/popcalendar.js";
   	String dateurl = url +"/javascript/date.js";
   	String imgurl = url +"/images/";
   	String formName = "documentSearchForm";
   	int total = ((Integer)request.getAttribute("countDocTotal")).intValue();
   
	String dateFromParam = "dateFrom";   
   	String dateToParam = "dateTo";	
   
   	String showCal ="showCalendar(this,document."+ formName +"."+ dateFromParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
   	String showCal2="showCalendar(this,document."+ formName +"."+ dateToParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
   	
    String baseurl = CWNSProperties.getProperty("edms.annotation.url","");
    String docbaseName = CWNSProperties.getProperty("edms.docbaseName","");
    String password = CWNSProperties.getProperty("edms..annotate.password","");
    String viewuserName = CWNSProperties.getProperty("edms.view.userName","");
    String viewpassword = CWNSProperties.getProperty("edms.view.password","");   	
    String annotationNextUrl = URLEncoder.encode(CWNSProperties.getProperty("edms.annotation.nextUrl",""), "UTF-8");     
   	String viewurl =baseurl +"?docbaseName="+docbaseName+"&userName="+viewuserName+"&sessionId="+Password.encrypt(viewpassword)+"&nexturl="+ annotationNextUrl; 
   	
   	String helpKey = (String)request.getAttribute("helpKey");  	
%>

<SCRIPT type=text/javascript>
// Removes leading whitespaces
function submitSearch(action) {
	var docSelected = false;
	if (action=='add'){
		var form = window.document.documentSearchForm;
		if(!isCheckBoxesSelected(form,'documentSearchCheckBoxes')){
			alert('At least one document must be selected.');
            return false;          
        }	
	}
	
	if(!ValidateForm()) return false;		
	hidden_field = window.document.getElementById("docSearchAct");
	hidden_field.value=action;
	window.document.documentSearchForm.submit();
	forward_url = window.document.getElementById("forwardUrl");
	return true;	
}

function nextPage(){
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	first_result.value = parseInt(first_result.value) + parseInt(max_result.value);
	submitSearch('submit');	
}
function prevPage(){
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	first_result.value = parseInt(first_result.value) - parseInt(max_result.value);
	submitSearch('submit');	
}
function firstPage(){
	first_result = window.document.getElementById("firstResult");
	first_result.value = '1';
	submitSearch('submit');	
}
function lastPage(){	
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	
	var mod = parseInt('<%=total%>') % max_result.value;
	first_result.value = parseInt('<%=total%>') - (mod - 1);
	
	submitSearch('submit');	
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
	if (strYear.length != 4 || iyear==0 || iyear<minYear || iyear>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}

function ValidateForm(){
	
	var dt_from = window.document.getElementById("dateFrom");
	var dt_to = window.document.getElementById("dateTo");
	var doc_type = window.document.getElementById("docType");
	var words = window.document.getElementById("keywords1");
	var keywords = words.value;

	//disable default search
	if (dt_from.value.length == 0 && dt_to.value.length == 0 && 
			doc_type.selectedIndex <= 0 && (trim(keywords)).length==0){
			alert("Please enter search criteria");
			return false;
	}	
	
	if(dt_from.value.length == 0 && dt_to.value.length == 0)
		return true;		
	
	if (dt_from.value.length > 0 && isDate(dt_from.value)==false){
		dt_from.focus();
		return false;
	}
	
	if (dt_to.value.length > 0 && isDate(dt_to.value)==false){
		dt_to.focus();
		return false;
	}	
	
	var currentDate=new Date();
	var Date_From = Date.parse(dt_from.value);
	if (Date_From > currentDate)
	{
		alert("The 'From' published date cannot be later than today's date.");
  		dt_from.focus();
  		return false;
	}	
	
	if (dt_from.value.length > 0 && dt_to.value.length > 0){
		var Date_To = Date.parse(dt_to.value);	
	  
		if (Date_To < Date_From)
		{
			alert("The 'To' published date must be later than the 'From' date.");
	  		dt_from.focus();
	  		return false;
		}	
	}
	
    return true;
 }
 
 
//removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

//removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

//removes leading and ending whitespaces
function trim( value ) {	
	return LTrim(RTrim(value));	
}

function annotateDocument(docid) {
day = new Date();
id = day.getTime();
s = new String('<%=viewurl%>');
var URL = s.replace("%3C%3Cdocid%3E%3E",docid);
//alert(URL);
eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=1,menubar=0,resizable=1,width=820,height=700,left = 230,top = 162');");
}

function isCheckBoxesSelected(form,fieldName){
   var checkboxes = form[fieldName];   

     if (checkboxes.checked == true){
        return true;
     }   

   if(checkboxes != undefined && checkboxes.length != undefined && checkboxes.length > 0){ 
	 for(i=0; i< checkboxes.length; i++){
	    if(checkboxes[i].checked) return true;
	 }
   }
   return false;	
}

// Open help window
function ShowDocumentSearchHelp(popupUrl)
{
    var w = 800;
    var h = 500;
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

  var w = window.open(popupUrl, 'DocumentSearchHelp', settings);
  if(w != null) 
    w.focus();     
}
</SCRIPT>

<pdk-html:form 	name="documentSearchForm" styleId="documentSearchForm"
				type="gov.epa.owm.mtb.cwns.documentSearch.DocumentSearchForm"
				action="documentSearch.do">
				
<DIV id="hidden_fields" style="display:none">				
	<pdk-html:text name="documentSearchForm" styleId="docSearchAct" property="docSearchAct" size="20" maxlength="30"/>
	<pdk-html:text name="documentSearchForm" styleId="firstResult" property="firstResult" size="20" maxlength="30"/>
	<pdk-html:text name="documentSearchForm" styleId="maxResult" property="maxResult" size="20" maxlength="30"/>
</DIV>	
<DIV align="right">
	<FONT class="PortletText1"> <A href="javascript:void(0);"
		onclick='ShowDocumentSearchHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
	</FONT>
</DIV>		
<TABLE width="100%" border=0>
<TBODY>



	<TR>
		<TD></TD>
	</TR>
	<TR>
		<TD align="left" colspan="3" class="PortletText1">
	<TABLE border="0" cellpadding="1" cellspacing="0" width="100%" class="PortletText1">
	<TR>
		<TD align="left" colspan="3" width="35%" class="PortletText1">Document Type:</TD>
		<TD><pdk-html:select name="documentSearchForm" styleId="docType" property="docType" size="1" style="width: 225px">
						<OPTION value="">
							Select a Document Type						
						</OPTION>														
						<logic:iterate id="documentGroupTypeList" name="documentGroupTypeList">								
							<bean:define id="groupTypeId" name="documentGroupTypeList" property="groupTypeId"/> 
							<bean:define id="groupTypeDesc" name="documentGroupTypeList" property="groupTypeDesc"/>
							<logic:equal name="documentGroupTypeList" property="groupId" value="0">
							<logic:equal name="documentGroupTypeList" property="groupTypeId" value="<%=docForm.getDocType()%>">
										<OPTION SELECTED title="<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>"
										        value="<bean:write name="documentGroupTypeList" property="groupTypeId"/>">
											<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>
										</OPTION>
							</logic:equal>
							<logic:notEqual name="documentGroupTypeList" property="groupTypeId" value="<%=docForm.getDocType()%>">
							<OPTION title="<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>"
								value="<bean:write name="documentGroupTypeList" property="groupTypeId" />">
								<%=((String)groupTypeId + " - " + (String)groupTypeDesc)%>
							</OPTION>
							</logic:notEqual>
							</logic:equal>
						</logic:iterate>
			</pdk-html:select>
		</TD>
	
	</TR>
	<TR>
		<TD align="left" colspan="3" width="35%" class="PortletText1">Published Date From:</TD>
		<TD><pdk-html:text name="documentSearchForm" styleId="dateFrom" property="dateFrom" size="16" maxlength="30"/>&nbsp;&nbsp;<a href="javascript: void(0);" onclick="<%=showCal%>"><pdk-html:img page="/images/cal.jpg" border="0"/></a></TD>
	
	</TR>
	<TR>
		<TD align="left" colspan="3" width="35%" class="PortletText1">Published Date To:</TD>
		<TD><pdk-html:text name="documentSearchForm" styleId="dateTo" property="dateTo" size="16" maxlength="30"/>&nbsp;&nbsp;<a href="javascript: void(0);" onclick="<%=showCal2%>"><pdk-html:img page="/images/cal.jpg" border="0"/></a></TD>
	
	</TR>
	<TR>
		<TD align="left" colspan="3" width="35%" class="PortletText1">Keyword:</TD>
		<TD><pdk-html:text name="documentSearchForm" styleId="keywords1" property="keywords" size="20" maxlength="30"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<A href="javascript: firstPage('submit');"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A></TD>
	
	</TR>	
	
	</TABLE>
</TBODY>
</TABLE>

<TABLE width="100%" border="0">
<TBODY>



<TR>
	<TD></TD>
</TR>
<TR>
	<TD align="left" colspan="3" class="PortletText1">
<TABLE border="0" cellspacing="3" cellpadding="2" width="100%" class="PortletText1">
<logic:greaterThan name="countDocTotal" value="0">
<TR style="font-weight: bold; background-color: #d3d3d3">
	<TD width="18%">
		<logic:equal name="documentSearchForm" property="isUpdatable" value="Y">
		Select
		</logic:equal>		
	</TD>
	<TD>
		Info
	</TD>	
</TR>
</logic:greaterThan>
<c:set var="i" value="1" />
<logic:iterate id="needsHelper" scope="request" name="searchResults" type="gov.epa.owm.mtb.cwns.needs.NeedsHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
<TR>
<c:set var="doctype"><bean:write name="needsHelper" property="documentTypeId"/></c:set>
<c:set var="hasCSOCostCurveNeeds"><bean:write name="hasCSOCostCurveNeeds"/></c:set>
<c:choose>
	<c:when test='${doctype=="11" || (hasCSOCostCurveNeeds=="Y" && doctype=="98") }'>
		<TD class="<c:out value="${class}"/>" align="center">&nbsp;</TD>
		<TD class="<c:out value="${class}"/>" align="center" style="display:none">  
	</c:when>
	<c:otherwise>
		<TD class="<c:out value="${class}"/>" align="center">
	</c:otherwise>
</c:choose>
		<c:choose>			
			<c:when test='${documentSearchForm.isUpdatable=="Y"}'>
					<pdk-html:multibox name="documentSearchForm" property="documentIds" styleId="documentSearchCheckBoxes">		
						<bean:write name="needsHelper" property="documentId"/>			
					</pdk-html:multibox></td>
			</c:when>																
			<c:otherwise>
				&nbsp;
			</c:otherwise>
		</c:choose>		
<TD class="<c:out value="${class}"/>">
Type:&nbsp;&nbsp;<bean:write name="needsHelper" property="documentTypeDesc"/><br/>
Title:&nbsp;&nbsp;
<logic:notEqual name="needsHelper" property="repositoryId" value="">
    <A href="javascript:annotateDocument('<bean:write name="needsHelper" property="repositoryId"/>');">
</logic:notEqual>
    <bean:write name="needsHelper" property="documentTitle"/>
<logic:notEqual name="needsHelper" property="repositoryId" value="">    
    </A>
</logic:notEqual><br/>
Author:&nbsp;&nbsp;<bean:write name="needsHelper" property="authorName"/><br/>
Published date:&nbsp;&nbsp;<bean:write name="needsHelper" property="publishedDate"/>
</TD>
</TR>
<c:set var="i" value="${i+1}" />
</logic:iterate>
<TR><TD colspan="2">&nbsp;</TD></TR>
<% 

//String max = "" + (Integer.parseInt(docForm.getFirstResult()) + (Integer.parseInt(docForm.getMaxResult())-1));
//String total = ((Integer)request.getAttribute("countDocTotal")).toString();
int first = Integer.parseInt(docForm.getFirstResult());
int max = 	searchResults.size();
int last = first + (max - 1);

String prev = "Prev";
if(first > 1)
	prev = "<a href=\"javascript: prevPage()\">Prev</a>";
	
String next = "Next";
if(last < total)
	next = "<a href=\"javascript: nextPage()\">Next</a>";	

String firstpage = "&lt;&lt;";
if(first > 1)
	firstpage = "<a href=\"javascript: firstPage()\">&lt;&lt;</a>";

String lastpage = "&gt;&gt;";
if(last < total)
	lastpage = "<a href=\"javascript: lastPage()\">&gt;&gt;</a>";

if(total > 0){%>
<TR><TD colspan="2" align="right">|<%=firstpage%> |  <%=prev%> |  <%=first%> - <%=last%> of <%=total%>  |  <%=next%>  |   <%=lastpage%>|</TD>
</TR>
<%} %>
<logic:notEmpty name="searchResults">
<logic:equal name="documentSearchForm" property="isUpdatable" value="Y">
<TR>
<TD colspan="2">
<INPUT type="Button" name="Add" value="Associate with Facility" onclick="javascript: submitSearch('add')"/>
</TD>
</TR>
</logic:equal>
<script type="text/javascript">
	var isUpdatable = '<bean:write name="documentSearchForm" property="isUpdatable" />';
	if (!isUpdatable.value=='Y'){
		document.documentSearchForm.Add.disabled=true;
	}
</script>
</logic:notEmpty>
<logic:equal name="countDocTotal" value="0">
<tr><td colspan="3" class="PortletSubHeaderColor">
		<b>No records found!</b>
</td></tr>
</logic:equal>
</TABLE>
</TD>
</TR>
<TR>
</TR>
</TBODY>
</TABLE>
</pdk-html:form>