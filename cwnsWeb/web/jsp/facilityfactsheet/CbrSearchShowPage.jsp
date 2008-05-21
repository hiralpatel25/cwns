<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
        import="gov.epa.owm.mtb.cwns.funding.CbrSearchForm"
        import="java.util.Collection"
        import="oracle.portal.utils.NameValue"
        import="oracle.portal.provider.v2.render.PortletRendererUtil"
        import="oracle.portal.provider.v2.url.UrlUtils"
        import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
   
   String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
   	String popurl = url +"/javascript/popcalendar.js";
   	String dateurl = url +"/javascript/date.js";
   	String imgurl = url +"/images/";
   	String formName = "cbrSearchForm";
   	
   	String startdateParam = "startDate";   
   	String enddateParam = "endDate";
      
   String showCal ="showCalendar(this,document."+ formName +"."+ startdateParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
   String showCal2="showCalendar(this,document."+ formName +"."+ enddateParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
   NameValue[] linkParams = null;
   
   CbrSearchForm cbrSearchForm = (CbrSearchForm)request.getAttribute("cbrSearchForm");
   Collection searchResults = (Collection)request.getAttribute("searchResults");
   int total = ((Integer)request.getAttribute("countSearchTotal")).intValue();
   
   
   if(cbrSearchForm == null) cbrSearchForm = new CbrSearchForm();      
     
     
   
	String eventPopupWinowUrl1 = CWNSEventUtils.constructEventLink(pReq,"CbrLoanDetailPage", linkParams,true, true);   
      
%>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>

<SCRIPT type=text/javascript>
// Removes leading whitespaces
function submitSearch(action) {
	
	if(!validateCbrSearchPage()) return false;
	
	hidden_field = window.document.getElementById("cbrAction");
	hidden_field.value=action;
	window.document.cbrSearchForm.submit();
	forward_url = window.document.getElementById("forwardUrl");	
	return true;
	
}
function nextPage(){
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	first_result.value = parseInt(first_result.value) + parseInt(max_result.value);
	submitSearch('search');	
}
function prevPage(){
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	first_result.value = parseInt(first_result.value) - parseInt(max_result.value);
	submitSearch('search');	
}
function firstPage(){
	first_result = window.document.getElementById("firstResult");
	first_result.value = '1';
	submitSearch('search');	
}
function lastPage(){
	first_result = window.document.getElementById("firstResult");
	max_result = window.document.getElementById("maxResult");
	
	var mod = parseInt('<%=total%>') % max_result.value;
	if(mod==0)
	  first_result.value = parseInt('<%=total%>') - (max_result.value - 1);
	else  
	  first_result.value = parseInt('<%=total%>') - (mod - 1);

	submitSearch('search');	
}
function popupLoanDetailS(loanid){

	day = new Date();
	id = day.getTime();
	var URL = '<%=eventPopupWinowUrl1%>&_event_loanId=' + loanid;
	eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,resizable=0,width=820,height=700,left = 230,top = 162');");
}
function validateCbrSearchPage(){
	//TODO: add validation here
	dt_start = window.document.getElementById("startDate");
	dt_end = window.document.getElementById("endDate");
		
    if (dt_end.value.length > 0 && !isDate(dt_end.value)){
        dt_end.focus();
        return false;        
    }
    
    if (dt_start.value.length> 0  && !isDate(dt_start.value)){
        dt_start.focus();
        return false;    
    }    

    if (dt_end.value.length > 0 && dt_start.value.length == 0) {
        alert("Valid Start Date is required.");
        dt_start.focus();
        return false;
    }
    
	if (dt_start.value.length > 0 && dt_end.value.length > 0){
		var date_end = Date.parse(dt_end.value);
		var date_start = Date.parse(dt_start.value);
	  
		if (date_end < date_start){
			alert("End date must be later than the Start date.");
	  		dt_start.focus();
	  		return false;
		}	
	}
    
    return true;
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
 
 
  function checkLoanIds(loan, cat) {
	var form=window.document.getElementById("cbrSearchForm");
	var loans = form["LoanId"];
	var cats = form["CatId"];
	
	if(cat==''){
		if (loans.checked != true && loans.checked != false){
		if(loans.length > 0){
			for (i = 0; i < loans.length; i++){
				//alert("loan check 1");
				if(loans[i].value==loan){
					//alert("loan check 2");
					if (loans[i].checked == true){
						for (x = 0; x < cats.length; x++){
							//alert("loan check 3");
							if(cats[x].value.indexOf(loan + ';') > -1){
								//alert("loan check 4");
								cats[x].disabled=true;
							}
						}
					}else if(loans[i].checked == false ){					
						for (x = 0; x < cats.length; x++){						
							if(cats[x].value.indexOf(loan + ';') > -1){
								cats[x].disabled=false;
							}
						}
					}
				}
			}//ends FOR
			}
		}else{			
			if(loans.checked == true){		
				if (cats!=undefined && (cats.length > 0)){
					for (x = 0; x < cats.length; x++){						
						if(cats[x].value.indexOf(loan + ';') > -1){							
							cats[x].disabled=true;
						}
					}
				}else if(cats!=undefined && cats.value.indexOf(loan + ';') > -1){
					cats.disabled=true;
				}
			}else if(loans.checked == false){
				if(cats!=undefined && cats.length > 0){
					for (x = 0; x < cats.length; x++){						
						if(cats[x].value.indexOf(loan + ';') > -1){
							cats[x].disabled=false;
						}
					}
				}else if(cats!=undefined && cats.value.indexOf(loan + ';') > -1){
					cats.disabled=false;
				}
			}		
		}
	}else{		
		if(cats!=undefined && cats.length >0){
			for (i = 0; i < cats.length; i++){			
				if(cats[i].value==cat){				
					if (cats[i].checked == true){					
						if(loans.length > 0){
							for (x = 0; x < loans.length; x++){						
								if(loans[x].value == loan){
									loans[x].disabled=true;
								}
							}
						}else if(loans.value == loan){
							loans.disabled=true;
						}
					
					}if (cats[i].checked == false){					
						var disable = false;					
						for (x = 0; x < cats.length; x++){						
							if(cats[x].value.indexOf(loan + ';') > -1){						
								if (cats[x].checked == true){
									disable = true;
								}
							}
						}
					
						if(loans!=undefined && loans.length > 0){
							for (x = 0; x < loans.length; x++){						
								if(loans[x].value == loan){
									loans[x].disabled=disable;
								}
							}
						}else if(loans.value == loan){
							loans.disabled=disable;
						}
					}
				}
			}
		}else {	
			if(cats.checked == true){		
				if(loans!=undefined && loans.length > 0){
					for (x = 0; x < loans.length; x++){						
						if(loans[x].value == loan){
							loans[x].disabled=true;
						}
					}
				}else if(loans.value == loan){
					loans.disabled=true;
				}					
			}else if(cats.checked == false){			
				var disable = false;					
				for (x = 0; x < cats.length; x++){					
					if(cats[x].value.indexOf(loan + ';') > -1){						
						if (cats[x].checked == true){
							disable = true;
						}
					}
				}
					
				if(loans!=undefined && loans.length > 0){
					for (x = 0; x < loans.length; x++){						
						if(loans[x].value == loan){
							loans[x].disabled=disable;
						}
					}
				}else if(loans.value == loan){
					loans.disabled=disable;
				}			
			}	
		}
	}
} 
</SCRIPT>

<pdk-html:form 	name="cbrSearchForm" styleId="cbrSearchForm"
				type="gov.epa.owm.mtb.cwns.funding.CbrSearchForm"
				action="cbrSearch.do">

<DIV id="hidden_fields" style="display:none">				
	<pdk-html:text name="cbrSearchForm" styleId="cbrAction" property="cbrAction" size="20" maxlength="30"/>
	<pdk-html:text name="cbrSearchForm" styleId="firstResult" property="firstResult" size="20" maxlength="30"/>
	<pdk-html:text name="cbrSearchForm" styleId="maxResult" property="maxResult" size="20" maxlength="30"/>
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
			<TD align="left" colspan="3" width="35%" class="PortletText1">Borrower</TD>
			<TD><pdk-html:text name="cbrSearchForm" styleId="borrower" property="borrower" size="20" maxlength="30"/></TD>
		</TR>
		<TR>
			<TD align="left" colspan="3" width="35%" class="PortletText1">Tracking #</TD>
			<TD><pdk-html:text name="cbrSearchForm" styleId="trackingNumber" property="trackingNumber" size="20" maxlength="30"/></TD>
		</TR>
		<TR>
			<TD align="left" colspan="3" width="35%" class="PortletText1">Execution Start Date</TD>
			<TD><pdk-html:text name="cbrSearchForm" styleId="startDate" property="startDate" size="20" maxlength="30"/>&nbsp;&nbsp;<a href="javascript: void(0);" onclick="<%=showCal%>"><pdk-html:img page="/images/cal.jpg" border="0"/></a></TD>
		</TR>
		<TR>
			<TD align="left" colspan="3" width="35%" class="PortletText1">Execution End Date</TD>
			<TD><pdk-html:text name="cbrSearchForm" styleId="endDate" property="endDate" size="20" maxlength="30"/>&nbsp;&nbsp;<a href="javascript: void(0);" onclick="<%=showCal2%>"><pdk-html:img page="/images/cal.jpg" border="0"/></a></TD>
		</TR>		
	</TABLE>
	<TABLE cellspacing="1" cellpadding="0" width="100%" border="0" class="PortletText1">
		<TR>
			<TD align="left" width="8%">
				<pdk-html:multibox name="cbrSearchForm" property="assocWithCwnsNumber">		
					Y
				</pdk-html:multibox>
			</TD>
			<TD align="left">
				 Associated with current Facility/Project in CBR
			</TD>
		</TR>
		<TR>
			<TD align="left" width="8%">
				<pdk-html:multibox name="cbrSearchForm" property="assocWithPermit">		
					Y
				</pdk-html:multibox>
			</TD>
			<TD align="left">
				 Associated with current Facility/Project's NPDES Permit(s) in CBR
			</TD>
		</TR>
		<TR>
		<TD colspan="2"><A href="javascript: firstPage('search');"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>&nbsp;&nbsp;Search</TD>
		</TR>	
	</TABLE>
</TD>
</TR>
<TR>
</TR>
</TBODY>
</TABLE>
<TABLE border="1" cellspacing="3" cellpadding="2" width="100%" class="PortletText1" style="BORDER: #d3d3d3 thin solid">
<logic:notEmpty name="searchResults">
<TR style="font-weight: bold; background-color: #d3d3d3">
	<TD width="18%">
		Select
	</TD>
	<TD>
		Loan Info
	</TD>	
</TR>
</logic:notEmpty>
<logic:iterate id="cbrSearchResult" scope="request" name="searchResults" type="gov.epa.owm.mtb.cwns.funding.CbrSearchResult">
<bean:define id="cbrLoanInformation" name="cbrSearchResult" property="cbrLoanInformation" type="gov.epa.owm.mtb.cwns.model.CbrLoanInformation" />
<TR>
<% String sloanid = "javascript:checkLoanIds('" + cbrLoanInformation.getCbrLoanInformationId() + "','')"; %>
<TD align="center">


<c:set var="checkbox" value="Y" />
<logic:iterate id="amountid" scope="request" name="catIds" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformationId">

	<c:choose>			
		<c:when test='${cbrLoanInformation.cbrLoanInformationId==amountid.cbrLoanInformationId}'>
			<c:set var="checkbox" value="N" />
		</c:when>																
	</c:choose>

</logic:iterate>

		<c:choose>			
			<c:when test='${cbrSearchForm.isUpdatable=="Y" && checkbox=="Y"}'>
					<pdk-html:multibox name="cbrSearchForm" property="loanIds" onclick="<%=sloanid%>"  styleId="LoanId">		
						<bean:write name="cbrLoanInformation" property="cbrLoanInformationId"/>		
					</pdk-html:multibox>
			</c:when>																
			<c:otherwise>
				&nbsp;
			</c:otherwise>
		</c:choose>	
</TD>

<TD>Borrower:&nbsp;<c:out value='${cbrLoanInformation.cbrBorrower.name}'/><br/>
Tracking #:&nbsp;
<logic:equal name="cbrSearchResult" property="displayLink" value="Y">
<A href="javascript:void(0);" onclick='popupLoanDetailS("<bean:write name="cbrLoanInformation" property="cbrLoanInformationId"/>")'"><bean:write name="cbrLoanInformation" property="loanNumber"/></A>
</logic:equal>
<logic:equal name="cbrSearchResult" property="displayLink" value="N">
<bean:write name="cbrLoanInformation" property="loanNumber"/>
</logic:equal><br/>
Loan Execution Date:&nbsp;<fmt:formatDate pattern="MM/dd/yyyy" value="${cbrLoanInformation.loanDate}"/><br/>
Total:&nbsp;<fmt:formatNumber type="currency" pattern="$#,##0" value="${cbrLoanInformation.loanAmount}"/><br/>
<TABLE class="PortletText1" width="100%" cellspacing="0" cellpadding="0">
	<bean:define id="cbrProjectInformations" name="cbrLoanInformation" property="cbrProjectInformations" type="java.util.Set"/>
	<logic:iterate id="project" name="cbrProjectInformations" type="gov.epa.owm.mtb.cwns.model.CbrProjectInformation">
	<TR style="background-color: #BFBFBF"><TD colspan="2" style="BORDER: #BFBFBF thin solid">Project:&nbsp;<bean:write name="project" property="projectNumber"/>
	<logic:present name="project" property="cwnsNumber">
		<br/>CWNS Number:&nbsp;<bean:write name="project" property="cwnsNumber"/>
	</logic:present>
	<logic:present name="project" property="facilityName">
		<br/>Facility Name:&nbsp;<bean:write name="project" property="facilityName"/>
	</logic:present>
	</TD></TR>
	
	<bean:define id="cbrAmountInformations" name="project" property="cbrAmountInformations" type="java.util.Set"/>
	
	<logic:iterate id="category" name="cbrAmountInformations" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformation">
	
	<bean:define id="id" name="category" property="id" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformationId"/>
	<TR style="background-color: #E0E0E0; BORDER: #E0E0E0 thin solid">
	<TD width="15%" align="center">
		<% String scatid = "javascript:checkLoanIds('" + cbrLoanInformation.getCbrLoanInformationId() + "','" + cbrLoanInformation.getCbrLoanInformationId() + ";" + id.getCbrProjectInformationId()  + ";" + id.getCbrCategoryId() + "')"; %>
				
		<c:set var="checkbox" value="Y" />
		<logic:iterate id="amountid" scope="request" name="catIds" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformationId">

			<c:choose>			
				<c:when test='${id==amountid}'>
					<c:set var="checkbox" value="N" />
				</c:when>																
			</c:choose>

		</logic:iterate>		
		
		<c:choose>			
			<c:when test='${cbrSearchForm.isUpdatable=="Y" && checkbox=="Y"}'>
					<pdk-html:multibox name="cbrSearchForm" property="categoryIds" onclick="<%=scatid%>" styleId="CatId">		
						<bean:write name="id" property="cbrLoanInformationId"/>;<bean:write name="id" property="cbrProjectInformationId"/>;<bean:write name="id" property="cbrCategoryId"/>			
					</pdk-html:multibox>
			</c:when>																
			<c:otherwise>
				<pdk-html:img page="/images/checkmark.gif" alt="Already added to facility" border="0"/>
			</c:otherwise>
		</c:choose>
		
	</TD>
	<TD>Category:&nbsp;<bean:write name="id" property="cbrCategoryId"/><br/>
	Amount:&nbsp;<fmt:formatNumber type="currency" pattern="$#,##0" value="${category.amount}"/></TD>	
	
	</logic:iterate>
	</TR>
	</logic:iterate>
</TABLE></TD></TR>
</logic:iterate>
<% 
int first = Integer.parseInt(cbrSearchForm.getFirstResult());
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
<logic:equal name="cbrSearchForm" property="isUpdatable" value="Y">
<TR>
<TD colspan="2">
<INPUT type="Button" name="Add" value="Associate with Facility" onclick="javascript: submitSearch('add')"/>
</TD>
</TR>
</logic:equal>
<%} 
else if(total==0 && cbrSearchForm.getCbrAction().equals("search")){%>
<TD colspan="2" align="center">
<b>No records found!</b>
</TD>
<%}%>
</TABLE>


</pdk-html:form>