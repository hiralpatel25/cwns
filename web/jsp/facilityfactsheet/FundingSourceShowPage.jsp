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
	import="gov.epa.owm.mtb.cwns.funding.FundingForm"
	import="java.util.Collection"
	import="java.text.SimpleDateFormat"
	import="java.text.NumberFormat"
	import="oracle.portal.utils.NameValue"
    import="oracle.portal.provider.v2.render.PortletRendererUtil"
    import="oracle.portal.provider.v2.url.UrlUtils"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"    
%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			
			
			FundingForm fundingForm = (FundingForm)request.getAttribute("fundingForm");
			
			if(fundingForm == null) fundingForm = new FundingForm();
			
			
   			Collection searchResults = (Collection)request.getAttribute("searchResults");
   			
   			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
   			NumberFormat nf = NumberFormat.getInstance();
   			
   			
   			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
   			String popurl = url +"/javascript/popcalendar.js";
		   	String dateurl = url +"/javascript/date.js";
   			String imgurl = url +"/images/";
   			
   			String formName = "fundingForm";
   			String dateAwardParam = "awardDate";
   			
   			String showCal ="showCalendar(this,document."+ formName +"."+ dateAwardParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";   			
   			NameValue[] linkParams = null;
   			
   			String eventPopupWinowUrl1 = CWNSEventUtils.constructEventLink(prr,"CbrLoanDetailPage", linkParams,true, true);   
   					
			
%>
<SCRIPT type=text/javascript>
// Removes leading whitespaces
function submitFundingPage(action) {
	
	if(action=='save'){
		if(!validateFundingPage()) return;
	}
	
	hidden_field = window.document.getElementById("fundingAction");
	hidden_field.value=action;
	window.document.fundingForm.submit();
	return true;
	
}
function validateFundingPage(){

	//TODO: add validation here
	var dt_award = window.document.getElementById("awardDate");
	var str_loannumb = window.document.getElementById("loanNumber");
	var ftotal = window.document.getElementById("fundingTotal");
	var percentcwsrf = window.document.getElementById("percentbyCwsrf");
	var type_index=document.fundingForm.fundingType.selectedIndex;
	var agency_index=document.fundingForm.fundingAgency.selectedIndex;
	var source_index=document.fundingForm.fundingSource.selectedIndex;
	var funding_agency = document.fundingForm.fundingAgency;
	
	
	if (type_index==0) {
		alert("\nYou must make a selection from the Type drop-down list.");
		document.fundingForm.fundingType.focus();
		return false;
	}	
	
	if (agency_index==0) {
		alert("\nYou must make a selection from the Agency/Program drop-down list.");
		document.fundingForm.fundingAgency.focus();
		return false;
	}
	
	if (source_index==0) {
		alert("\nYou must make a selection from the Source drop-down list.");
		document.fundingForm.fundingSource.focus();
		return false;
	}
	/*
	if (str_loannumb.value.length == 0) {
        alert("Loan Number is a required field.");
        str_loannumb.focus();
        return false;
    }*/
    
    if(str_loannumb.value.length>20){
        alert("Loan Number should not exceed 20 characters.");
        str_loannumb.focus();
        return false;    
    }
    
    if (dt_award.value.length == 0) {
        alert("Award Date is a required field.");
        dt_award.focus();
        return false;
    }    	
	
	if (isDate(dt_award.value)==false){		
		dt_award.focus();
		return false;
	}
	
	var currentDate=new Date();
	var dt_award_date = Date.parse(dt_award.value);
	if (dt_award_date > currentDate){
		alert("Award Date must be earlier than or equal to today's date.");
		dt_award.focus();
		return false;		
	}
	
	
	if (ftotal.value.length == 0) {
        alert("Total is a required field.");
        ftotal.focus();
        return false;
    }
    
    if(!isInteger(ftotal.value)){
    	alert("Total must be 0 or a positive number.");
        ftotal.focus();
        return false;    
    }
    
    if(isInteger(ftotal.value)){
    	
    	if(parseInt(ftotal.value) <= 0){
    		alert("Total must be a positive number.");
        	ftotal.focus();
	        return false;
    	}    	   
    }
    
	if (funding_agency[funding_agency.selectedIndex].value=='S'){
    	if (percentcwsrf.value.length == 0) {
        	alert("% Funded by CWSRF is a required field.");
        	percentcwsrf.focus();
        	return false;
    	}
    
    	if(!isInteger(percentcwsrf.value)){
    		alert("% Funded by CWSRF must be a number between 0 and 100");
        	ftotal.focus();
        	return false;    
    	}
    
    	if(isInteger(percentcwsrf.value)){    	
    		if(parseInt(percentcwsrf.value) < 0 || parseInt(percentcwsrf.value) >  100){
    			alert("% Funded by CWSRF must be a number between 0 and 100.");
        		ftotal.focus();
	        	return false;
    		}    	   
    	}
	}
	
	
    return true;
 }
function editFundingSource(fId){
	
	hidden_field = window.document.getElementById("fundingAction");
	hidden_field.value='edit';
	
	hidden_field = window.document.getElementById("fundingId");
	hidden_field.value=fId;
	window.document.fundingForm.submit();
	return true;
} 
function deleteFundingSource(type, fId){	
	hidden_field = window.document.getElementById("fundingAction");	
	if (type=='delete'){
		hidden_field.value='delete';
	}else if(type=='mark delete'){
		hidden_field.value='mark delete';	
	}
	hidden_field = window.document.getElementById("fundingId");
	hidden_field.value=fId;
	window.document.fundingForm.submit();
	return true;	
}
function refreshFundingSource(fId){
	
	hidden_field = window.document.getElementById("fundingAction");
	hidden_field.value='refresh';
	
	hidden_field = window.document.getElementById("fundingId");
	hidden_field.value=fId;
	window.document.fundingForm.submit();
	return true;	

}
function popupLoanDetail(loanid){

	day = new Date();
	id = day.getTime();
	var URL = '<%=eventPopupWinowUrl1%>&_event_loanId=' + loanid;
	eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,resizable=0,width=820,height=700,left = 230,top = 162');");
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

var displayRequired = true;
function fundingAgencyChanged(){
	// if funding source != SRF then disable % Funded by CWSRF
	var t = window.document.getElementById("percentbyCwsrf");
	var percent = window.document.getElementById("srfPercentRequired");
	percent.innerText = ' ';
	if (window.document.forms['fundingForm'].fundingAgency.value!='S'){
		t.value = '';
		t.disabled = true;
		t.className = 'disabledField';
		percent.innerText = ' ';
		displayRequired = false;
	}else{	
		t.disabled = false;
		t.className = '';
		displayRequired = true;
		percent.innerText='*';
	}
}

</SCRIPT>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<%-- <script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>--%>
<body onload="fundingAgencyChanged();">
<pdk-html:form 	name="fundingForm" styleId="fundingForm"
				type="gov.epa.owm.mtb.cwns.funding.FundingForm"
				action="funding.do">
<DIV id="hidden_fields" style="display:none">				
	<pdk-html:text name="fundingForm" styleId="fundingAction" property="fundingAction" size="20" maxlength="30"/>
	<pdk-html:text name="fundingForm" styleId="fundingId" property="fundingId" size="20" maxlength="30"/>
</DIV>	
<FONT class="PortletHeading1"><FONT size="2">
			<BR/> <FONT size="4"><STRONG>Funding Source</STRONG> </FONT>			
	        <logic:equal name="feedbackCopyExists" value="Y">
				<p align="left" class="WarningText" colspan="14">
	     			<pdk-html:img page="/images/warn24.gif" alt="Data Area Warnings" border="0"/>	
        			&nbsp;<bean:message key="error.general.feedback.warning"/><br>        			
        		</p>	        
	        </logic:equal>
			<TABLE cellspacing="2" cellpadding="1" border="0" width="100%"
				class="PortletText1">				
	        <logic:equal name="feedbackCopyExists" value="Y">
	        </logic:equal>
				<TR class="PortletHeaderColor">
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Type</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Agency / Program</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							<STRONG><FONT color="#ffffff">Award Date</FONT>
							</STRONG>
						</DIV>
					</TD>
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Loan Number</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Source</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Project</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Cost Category</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD width="8%">
						<P align="center">
							<STRONG><FONT color="#ffffff">Amount ($)</FONT>
							</STRONG>
						</P>
					</TD>
					<TD align="center">
						<STRONG><FONT color="#ffffff">% Funded <BR>by
								CWSRF</FONT>
						</STRONG>
					</TD>
					<TD align="center">
						<STRONG><FONT color="#ffffff">Consistent<BR>with
								CBR</FONT>
						</STRONG>
					</TD>
					<logic:equal name="fundingForm" property="isUpdatable" value="Y">
					<TD align="center">
						<STRONG><FONT color="#ffffff">Edit</STRONG>
					</TD>
					<TD align="center">
						<STRONG><FONT color="#ffffff">Delete</STRONG>
					</TD>
					</logic:equal>
					<logic:notEqual name="fundingForm" property="isUpdatable" value="Y">
					<logic:equal name="stateUser" value="false">
					<TD align="center">
						<STRONG><FONT color="#ffffff">Delete</STRONG>
					</TD>
					</logic:equal>
					</logic:notEqual>
				</TR>
				<c:set var="total" value="${0}" />
				<c:set var="i" value="1" />
				<c:set var="count" value="0" />
				<c:choose>
						<c:when test='${fundingSource.fundingSourceId == id}'>
							<c:set var="class" value="RowHighlighted" />  
						</c:when>
						<c:when test='${i%2=="0"}'>
							<c:set var="class" value="PortletSubHeaderColor" />   
						</c:when>						
						<c:otherwise>
							<c:set var="class" value="" />
						</c:otherwise>
				</c:choose>
				<c:choose>
						<c:when test='${empty fundingForm.fundingId}'>
								<c:set var="id" value="${0}" />	
						</c:when>
						<c:otherwise>
								<c:set var="id" value="${fundingForm.fundingId}" />
						</c:otherwise>
					</c:choose>				
				<logic:iterate id="fundingSource" scope="request" name="searchResults" type="gov.epa.owm.mtb.cwns.model.FundingSource">
					<c:choose>
						<c:when test='${fundingSource.fundingSourceId == id}'>
							<c:set var="class" value="RowHighlighted" />  
						</c:when>
						<c:when test='${i%2=="0"}'>
							<c:set var="class" value="PortletSubHeaderColor" />   
						</c:when>						
						<c:otherwise>
							<c:set var="class" value="" />
						</c:otherwise>
					</c:choose>
					
				<TR>	
					<c:choose>
						<c:when test='${empty fundingSource.awardedAmount}'>
								<c:set var="amount" value="${0}" />	
						</c:when>
						<c:otherwise>
								<c:set var="amount" value="${fundingSource.awardedAmount}" />
						</c:otherwise>
					</c:choose>					
					
				<TD class="<c:out value="${class}"/>">
						<DIV align="center">
							<c:out value='${fundingSource.loanTypeRef.name}'/>
						</DIV>
				</TD>
						<TD align="center"  class="<c:out value="${class}"/>">
						<c:out value='${fundingSource.fundingAgencyRef.name}'/>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align="center">
							<fmt:formatDate pattern="MM/dd/yyyy" value="${fundingSource.awardDate}"/>		      				
						</DIV>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align="center">
						<c:choose>
							<c:when test='${!empty fundingSource.consistentWithCbrCode}'>
								<A href="javascript:void(0);" onclick='popupLoanDetail("<bean:write name="fundingSource" property="fundingSourceId"/>")'" title="Detail Funding Information"><bean:write name="fundingSource" property="loanNumber"/></A>
							</c:when>											
							<c:otherwise>
								<bean:write name="fundingSource" property="loanNumber"/>
							</c:otherwise>
						</c:choose>							
						</DIV>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align="center">
							<c:out value='${fundingSource.fundingSourceTypeRef.name}'/>							
						</DIV>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align="center">
							<c:out value='${fundingSource.cbrProjectNumber}'/>
						</DIV>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align=center>
							<c:out value='${fundingSource.cbrAmountInformation.id.cbrCategoryId}'/>
						</DIV>
					</TD>					
					<TD class="<c:out value="${class}"/>">
						<DIV align="right">
								<fmt:formatNumber type="currency" pattern="#,##0" value="${amount}"/>
						</DIV>
					</TD>
					<TD align="right" class="<c:out value="${class}"/>">
						<c:out value='${fundingSource.percentageFundedBySrf}'/>
					</TD>
					<TD class="<c:out value="${class}"/>">
						<DIV align="center">
							<c:choose>
								<c:when test='${fundingSource.consistentWithCbrCode==67}'>									
										<pdk-html:img page="/images/checkmark.gif" alt="Consistent" border="0"/>										
								</c:when>
								<c:when test='${fundingSource.consistentWithCbrCode==73 && fundingForm.isUpdatable=="Y"}'>
									<A href="javascript:refreshFundingSource('<bean:write name="fundingSource" property="fundingSourceId"/>')">
										<pdk-html:img page="/images/refresh_icon.gif" alt="Refresh from CBR" border="0"/>
									</A>	
								</c:when>
								<c:when test='${fundingSource.consistentWithCbrCode==68 && fundingForm.isUpdatable=="Y"}'>
									<A href="javascript:deleteFundingSource('delete', '<bean:write name="fundingSource" property="fundingSourceId"/>')">
										<pdk-html:img page="/images/refresh_delete.gif" alt="Deleted from CBR" border="0"/>
									</A>									
								</c:when>								
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>
						</DIV>
					</TD>
					<logic:equal name="fundingForm" property="isUpdatable" value="Y">
					<TD class="<c:out value="${class}"/>">
					<c:choose>
							<c:when test='${!empty fundingSource.consistentWithCbrCode}'>
								&nbsp;
							</c:when>											
							<c:otherwise>
								<DIV align="center">
									<A href="javascript:editFundingSource('<bean:write name="fundingSource" property="fundingSourceId"/>')">
										<pdk-html:img page="/images/edit.gif" alt="Edit" border="0"/>
									</A>
								</DIV>
							</c:otherwise>
						</c:choose>						
					</TD>
					<TD class="<c:out value="${class}"/>">
								<DIV align="center">
								<logic:equal name="stateUser" value="true">
									<A href="javascript:deleteFundingSource('delete', '<bean:write name="fundingSource" property="fundingSourceId"/>')">
										<pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
									</A>
								</logic:equal>
									
								<logic:equal name="stateUser" value="false">
									<bean:define id="clickSubmit">
										javascript:deleteFundingSource('mark delete', '<bean:write name="fundingSource" property="fundingSourceId"/>')
									</bean:define>						
				    				<pdk-html:checkbox name="fundingSource" property="feedbackDeleteFlag"  value="Y"
		                  														onclick="<%=clickSubmit%>">
				    				</pdk-html:checkbox>
								</logic:equal>
								</DIV>
					</TD>
					</logic:equal>
					
					<logic:notEqual name="fundingForm" property="isUpdatable" value="Y">
					<logic:equal name="stateUser" value="false">
					   <TD class="<c:out value="${class}"/>">
							<DIV align="center">
								<pdk-html:checkbox name="fundingSource" property="feedbackDeleteFlag"  value="Y"
		                  														disabled="true">
				    			</pdk-html:checkbox>
							</DIV>
					   </TD>
					</logic:equal>
					</logic:notEqual>				
				</TR>
				<c:set var="total" value="${amount+total}" />
				<c:set var="i" value="${i+1}" />
				<c:set var="count" value="${count+1}" />
				</logic:iterate>				
				
				<c:choose>						
						<c:when test='${count > 0}'>
							<TR style="font-weight: bold; background-color: #d3d3d3">
								<TD>
									<DIV align="center">
										Total
									</DIV>
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD>
									&nbsp;
								</TD>
								<TD width="8%">
									<DIV align="right">
										<fmt:formatNumber type="currency" pattern="$#,##0" value="${total}"/>
									</DIV>
								</TD>
								<TD align="center">
									&nbsp;
								</TD>
								<TD align="center">
									&nbsp;
								</TD>
								<logic:equal name="fundingForm" property="isUpdatable" value="Y">
								<TD align="center">
									&nbsp;
								</TD>
								<TD align="center">
									&nbsp;
								</TD>	
								</logic:equal>				
							</TR>							 
						</c:when>
				</c:choose>				

				<TR>
					<TD>
						&nbsp;				
					</TD>					
				</TR>
			</TABLE>			
			 </FONT>
</FONT>

<%
    String showDetail = "none";
	String showAddLink = "block";	
	
    if((fundingForm.getFundingAction()).equals("add") || (fundingForm.getFundingAction()).equals("edit"))
    {
      showDetail = "block";
      showAddLink = "none";
    }
    
%>
<logic:equal name="fundingForm" property="isUpdatable" value="Y">			
         
<DIV id="fundingDetails" style="display:<%=showAddLink%>">
	<TABLE cellspacing="1" cellpadding="0" width="100%" border="0" style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		<TR>
			<TD align="left">
				 <DIV id="addNewDocLink" class="PortletText1">
					<a href="javascript: submitFundingPage('add');">
						<strong>Add Funding Source</strong>
					</a>
				</DIV>
			</TD>
		</TR>
		<TR>
			<TD>
				&nbsp;				
			</TD>					
		</TR>	
	</TABLE>
</DIV>
</logic:equal>

<DIV id="fundingDetails" style="display:<%=showDetail%>; background-color: #cccccc; padding: 5"  class="PortletText1">
<BR/>
		<P>&nbsp;<Strong>
		   <FONT size="4">
					  Add/Edit Funding Source&nbsp;
		   </FONT>&nbsp;</Strong>
		</P>
		
		<TABLE border="0" class="PortletText1" cellspacing="1" cellpadding="0" width="100%" 
		          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
		    <TR>
				<TD>
					<span class="required">*</span></TD><TD width="15%"><STRONG> Type: </STRONG>
				</TD>
				<TD>				
					<pdk-html:select name="fundingForm" styleId="fundingType" property="fundingType" size="1" style="width: 200px">
						<OPTION value="">
							&nbsp;						
						</OPTION>														
						<logic:iterate id="loanTypes" name="loanTypes">						
						
							<c:choose>
								<c:when test='${loanTypes.loanTypeId==fundingForm.fundingType}'>
									<OPTION SELECTED title="<c:out value='${loanTypes.name}'/>" value="<c:out value='${loanTypes.loanTypeId}'/>">
							       		<c:out value='${loanTypes.name}'/>
									</OPTION>
								</c:when>
								<c:otherwise>
									<OPTION title="<c:out value='${loanTypes.name}'/>" value="<c:out value='${loanTypes.loanTypeId}'/>">
										<c:out value='${loanTypes.name}'/>	
									</OPTION>							
								</c:otherwise>
							</c:choose>
							
						</logic:iterate>
					</pdk-html:select>
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD width="15%"><STRONG> Agency/Program: </STRONG>
				</TD>
				<TD><pdk-html:select name="fundingForm" styleId="fundingAgency" property="fundingAgency" size="1" style="width: 200px" onchange="fundingAgencyChanged();">
						<OPTION value="">
							&nbsp;						
						</OPTION>														
						<logic:iterate id="fundingAgencyTypes" name="fundingAgencyTypes">								
							
							<c:choose>
								<c:when test='${fundingAgencyTypes.fundingAgencyId==fundingForm.fundingAgency}'>
									<OPTION SELECTED title="<c:out value='${fundingAgencyTypes.name}'/>" value="<c:out value='${fundingAgencyTypes.fundingAgencyId}'/>">
										<c:out value='${fundingAgencyTypes.name}'/>	
									</OPTION>
								</c:when>
								<c:otherwise>
									<OPTION title="<c:out value='${fundingAgencyTypes.name}'/>" value="<c:out value='${fundingAgencyTypes.fundingAgencyId}'/>">
										<c:out value='${fundingAgencyTypes.name}'/>	
									</OPTION>
								</c:otherwise>
							</c:choose>
							
						</logic:iterate>
					</pdk-html:select>
				</TD>
			</TR>      
			<TR>
				<TD>
					<span class="required">*</span></TD><TD width="15%"><STRONG> Award Date: </STRONG>
				</TD>
				<TD><pdk-html:text name="fundingForm" styleId="awardDate" property="awardDate" size="16" maxlength="30" style="width: 200px"/>&nbsp;&nbsp;<a href="javascript: void(0);" onclick="<%=showCal%>"><pdk-html:img page="/images/cal.jpg" border="0"/></a>&nbsp;&nbsp;<strong>(mm/dd/yyyy)</strong>
				</TD>
			</TR>
			<TR>
				<TD></TD>
				<TD width="15%"><STRONG> Loan Number: </STRONG>
				</TD>
				<TD><pdk-html:text name="fundingForm" styleId="loanNumber" property="loanNumber" size="16" maxlength="20" style="width: 200px"/>
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD width="15%"><STRONG> Source: </STRONG>
				</TD>
				<TD><pdk-html:select name="fundingForm" styleId="fundingSource" property="fundingSource" size="1" style="width: 200px">
						<OPTION value="">
							&nbsp;						
						</OPTION>														
						<logic:iterate id="fundingSourceTypes" name="fundingSourceTypes">								
							
							<c:choose>
								<c:when test='${fundingSourceTypes.fundingSourceTypeId==fundingForm.fundingSource}'>
									<OPTION SELECTED title="<c:out value='${fundingSourceTypes.name}'/>" value="<c:out value='${fundingSourceTypes.fundingSourceTypeId}'/>">
										<c:out value='${fundingSourceTypes.name}'/>
									</OPTION>
								</c:when>
								<c:otherwise>
									<OPTION title="<c:out value='${fundingSourceTypes.name}'/>" value="<c:out value='${fundingSourceTypes.fundingSourceTypeId}'/>">
										<c:out value='${fundingSourceTypes.name}'/>
									</OPTION>
								</c:otherwise>
							</c:choose>
							
						</logic:iterate>
					</pdk-html:select>
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span></TD><TD width="15%"><STRONG> Total($): </STRONG>
				</TD>
				<TD><pdk-html:text name="fundingForm" styleId="fundingTotal" property="fundingTotal" size="16" maxlength="30" style="width: 200px"/>
				</TD>
			</TR>
			<TR>
				<TD>
					<span id="srfPercentRequired" class="required">*</span></TD><TD width="15%"><STRONG> % Funded by CWSRF: </STRONG>
				</TD>
				<TD><pdk-html:text name="fundingForm" styleId="percentbyCwsrf" property="percentbyCwsrf" size="16" maxlength="30" style="width: 200px"/>
				</TD>
			</TR>
			<TR>
			<TD>&nbsp;
			</TD>
			</TR>
			<TR>
			<TD colspan="3">			
			<A href="javascript: submitFundingPage('save');">
					<pdk-html:img page="/images/submit.gif" alt="Save" border="0"/></A>				   
				   <FONT size="1">&nbsp;&nbsp;</FONT>							   
			<A href="javascript: submitFundingPage('cancel');">
				<FONT size="1">
					<pdk-html:img page="/images/cancel.gif" alt="Cancel" border="0"/>
				</FONT>
			</A>
			</TD>
			</TR>		
		</TABLE>				
</DIV>
</pdk-html:form>
</body>

















