<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@page import="oracle.portal.provider.v2.personalize.PortletReference,
                oracle.portal.provider.v2.http.HttpCommonConstants,
                oracle.portal.provider.v2.render.PortletRenderRequest,
                oracle.portal.provider.v2.render.PortletRendererUtil,
                gov.epa.owm.mtb.cwns.common.struts.CWNSUrlUtils,
                gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
        import="oracle.portal.utils.NameValue"
%>
<%@page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page import="gov.epa.owm.mtb.cwns.model.LocationRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.PopulationRef" %>
<%@ page import="gov.epa.owm.mtb.cwns.model.FlowRef" %>
<%@ page import="gov.epa.owm.mtb.cwns.model.DocumentTypeRef" %>

<%@ page import="java.util.Collection,
                 java.util.ArrayList,
                 java.util.Iterator" %>

<%
    PortletRenderRequest pRequest = (PortletRenderRequest)
        request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	
	String defaultLocation = (String)request.getAttribute("defaultLocation");
	
    String eventSubmit  = CWNSEventUtils.eventName("advanceKeyword");
    String actionParam  = CWNSEventUtils.eventParameter("action");
	String facNameParam  = CWNSEventUtils.eventParameter("facName");   
	String reviewStatusParam = CWNSEventUtils.eventParameter("reviewStatus");    
	String overAllTypeParam  = CWNSEventUtils.eventParameter("overAllType");   
	String authorityParam = CWNSEventUtils.eventParameter("authority");
	String sysnameParam  = CWNSEventUtils.eventParameter("sysname");   
	String countyParam  = CWNSEventUtils.eventParameter("county");   
	String watershedParam = CWNSEventUtils.eventParameter("watershed");    
	String needOperatorParam = CWNSEventUtils.eventParameter("needOperator");    
	String needTotalParam = CWNSEventUtils.eventParameter("needTotal");     
	String docDateParam = CWNSEventUtils.eventParameter("docDate"); 
	String notChangedParam = CWNSEventUtils.eventParameter("notChanged");
	String permitNumberParam = CWNSEventUtils.eventParameter("permitNumber");
	String stateParam = CWNSEventUtils.eventParameter("state");
	String errorStatusParam = CWNSEventUtils.eventParameter("errorStatus");
	String docOperatorParam = CWNSEventUtils.eventParameter("docOperator");
	String presPopulationOperatorParam = CWNSEventUtils.eventParameter("presPopulationOperator");
	String presPopulationCountParam = CWNSEventUtils.eventParameter("presPopulationCount");
	String presPopulationTypeParam = CWNSEventUtils.eventParameter("presPopulationType");
	String presFlowOperatorParam = CWNSEventUtils.eventParameter("presFlowOperator");
	String presFlowCountParam = CWNSEventUtils.eventParameter("presFlowCount");
	String presFlowTypeParam = CWNSEventUtils.eventParameter("presFlowType");
	String facProjectOperatorParam = CWNSEventUtils.eventParameter("facProjectOperator");
	String docTypeParam = CWNSEventUtils.eventParameter("docType");
	
	String docTypeIn = pRequest.getParameter("docType");
	if(docTypeIn==null){
		docTypeIn="";
	}
	
	String facProjectOperatorIn = pRequest.getParameter("facProjectOperator");
	if(facProjectOperatorIn==null){
		facProjectOperatorIn="";
	}
	
	String docOperatorIn = pRequest.getParameter("docOperator");
	if(docOperatorIn==null){
		docOperatorIn="";
	}
	
	String presPopulationOperatorIn = pRequest.getParameter("presPopulationOperator");
	if(presPopulationOperatorIn==null){
		presPopulationOperatorIn="";
	}
	
	String presPopulationCountIn = pRequest.getParameter("presPopulationCount");
	if(presPopulationCountIn==null){
		presPopulationCountIn="";
	}
	
	String presPopulationTypeIn = pRequest.getParameter("presPopulationType");
	if(presPopulationTypeIn==null){{
		presPopulationTypeIn="";	
	}}
	
	String presFlowOperatorIn = pRequest.getParameter("presFlowOperator");
	if(presFlowOperatorIn==null){
		presFlowOperatorIn="";
	}
	
	String presFlowCountIn = pRequest.getParameter("presFlowCount");
	if(presFlowCountIn==null){
		presFlowCountIn="";
	}
	
	String presFlowTypeIn = pRequest.getParameter("presFlowType");
	if(presFlowTypeIn==null){{
		presFlowTypeIn="";	
	}}
	
	String facNameIn  = pRequest.getParameter("facName");
	if(facNameIn==null){
	   facNameIn="";
	}  
	String[] reviewStatusIn = pRequest.getParameterValues("reviewStatus");    
	String[] overAllTypeIn  = pRequest.getParameterValues("overAllType");   
	String authorityIn = pRequest.getParameter("authority");
	if(authorityIn==null){
	   authorityIn="";
	}  	
	String sysnameIn  = pRequest.getParameter("sysname");   
	if(sysnameIn==null){
	   sysnameIn="";
	}  		
	String countyIn  = pRequest.getParameter("county");   
	if(countyIn==null){
	   countyIn="";
	}  		
	String watershedIn = pRequest.getParameter("watershed");    
	if(watershedIn==null){
	   watershedIn="";
	}  			
	String needOperatorIn = pRequest.getParameter("needOperator");    
	if(needOperatorIn==null){
	   needOperatorIn="";
	}  				
	String needTotalIn = pRequest.getParameter("needTotal");     
	if(needTotalIn==null){
	   needTotalIn="";
	}  				
	String docDateIn = pRequest.getParameter("docDate"); 
	if(docDateIn==null){
	   docDateIn="";
	}  					
	String notChangedIn = pRequest.getParameter("notChanged");
	if(notChangedIn==null){
	   notChangedIn="";
	}
	String permitNumberIn = pRequest.getParameter("permitNumber");
	if(permitNumberIn==null){
	   permitNumberIn="";
	}  						
	String stateIn = pRequest.getParameter("state");
	if(stateIn==null){
	   stateIn="";
	}  						
	
	String errorStatusIn = pRequest.getParameter("errorStatus"); 	  
	if(errorStatusIn==null){
	   errorStatusIn="";
	}  						
    
    Collection statesInRegion = (Collection)request.getAttribute("statesInRegion"); 
    if(statesInRegion==null){
      statesInRegion = new ArrayList();
    }
    
    String userType = (String)request.getAttribute("userType");
    if(userType==null){
      userType="";
    } 
    
    
    String keyword=pRequest.getParameter("keyword");
    String showCal =null;
    String showCal2 =null;
    if(keyword == null) keyword="";
    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String popurl = url +"/javascript/popcalendar.js";
    String dateurl = url +"/javascript/date.js";
    String imgurl = url +"/images/";
    NameValue[] linkParams = null;
     
    String formName         = null;
    String formAction       = null;
    String formHiddenFields = null;
    try
    {
        formName   = CWNSUrlUtils.htmlFormName(pRequest,null);
        showCal ="showCalendar(this,document."+ formName +"."+ docDateParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
        showCal2="showCalendar(this,document."+ formName +"."+ notChangedParam +", 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
        formAction = CWNSUrlUtils.htmlFormActionLink(pRequest, CWNSUrlUtils.EVENT_LINK);
        formHiddenFields = 
            CWNSUrlUtils.htmlFormHiddenFields(pRequest, CWNSUrlUtils.EVENT_LINK);
    }
    catch (IllegalStateException e)
    {
        //do something
    }
    String helpKey = (String)request.getAttribute("helpKey");
%>
<SCRIPT type=text/javascript>


function clearAct(){
	var inputTags = window.document.getElementsByTagName('input');
	for(var i=0;i<inputTags.length;i++){
	 element = inputTags[i];
	 var type = element.getAttribute('type');
	 if (type == 'hidden'){
	    var name = element.getAttribute('name');	    
	    if (name.indexOf('.act') > 1){
	    	element.value='';
	    }
	 } 
  }
  return true;
}

function ShowLookUpWindow(popupUrl,lookUp)
{
    var w = 600;
    var h = 300;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    var combobox = document.getElementById('stateId');
    var s=combobox.options[combobox.selectedIndex].value;
    
    if (lookUp == 'county')
       var url = popupUrl+'&_event_locationId='+s+'&_event_formId='+<%=formName%>.name;  
    else
       var url = popupUrl+'&_event_locationId='+s;     
    var w = window.open(url, null, settings);
  if(w != null) 
    w.focus();     
}

function validateFieldsAndSubmit(){
var docdate = document.<%=formName%>.<%=docDateParam%>.value;
 if(docdate !=""){
	if(!validateDate(docdate,'u','a')){
  		alert("Please enter Document Published Date Older than date in MM/DD/YYYY format");
  		return false;
 	}
}

var notchanged = document.<%=formName%>.<%=notChangedParam%>.value;
 if(notchanged !=""){
	if(!validateDate(notchanged,'u','a')){
  		alert("Please enter Facility not Changed Since date in MM/DD/YYYY format");
  		return false;
	}
 }	
 var needs = document.<%=formName%>.<%=needTotalParam%>.value;
  if(needs !=""){
   if(!isNum(needs)){
   	  alert("Please enter total needs in integer format");
   	  return false;
   }
  }
var presPopulationCount = document.<%=formName%>.<%=presPopulationCountParam%>.value;
  if(presPopulationCount !=""){
   if(!isNum(presPopulationCount)){
   	  alert("Please enter present population count in integer format");
   	  return false;
   }
  }
var presFlowCount = document.<%=formName%>.<%=presFlowCountParam%>.value;
  if(presFlowCount !=""){
   if(!isNum(presFlowCount)){
   	  alert("Please enter present flow count in integer format");
   	  return false;
   }
  }
clearAct();
document.<%=formName%>.submit(); 
return true;
}
function ShowAdvancedSearchHelp(popupUrl)
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

  var w = window.open(popupUrl, 'AdvancedSearchHelp', settings);
  if(w != null) 
    w.focus();     
}

function clearForm(formIdent) { 
  var form, elements, i, elm, 
  form = formIdent;
	if (document.getElementsByTagName)
	{
		elements = form.getElementsByTagName('input');
		for( i=0, elm; elm=elements.item(i++); )
		{
			if (elm.getAttribute('type') == "text")
			{
				elm.value = '';
			}
		}
		
		selements = form.getElementsByTagName('select');
		for( i=0, elm; elm=selements.item(i++); )
		{
		    
		    for(x=0; x<elm.options.length;x++){
			if (elm.options[x].selected == true){
			   elm.options[x].selected=false;
			}
			
			}
			if(elm.type == 'select-one'){
				elm.selectedIndex = 0;
			}
		}
	}
	
	//set the location to default location
	var stateElement = form['stateId']; 
	for(x=0; x<stateElement.options.length;x++){
	  if (stateElement.options[x].value == '<%=defaultLocation%>'){
	    stateElement.options[x].selected=true;
	  }else{
	     stateElement.options[x].selected=false;
	  }		
	}
}

function enableOrDisableNeeds(form){
    
	var selType=form['stateId'];
	var indx = selType.selectedIndex;
	var v = '';
	
	if (indx != null) {
		v = selType.options[indx].value;
	}
	
	<%if("State".equalsIgnoreCase(userType)){ %>
	   if(v != '<%=defaultLocation%>'){
	     return disableNeeds();
	   }
	   else{
	   return enableNeeds();
	   }
	<%}else if("Regional".equalsIgnoreCase(userType)){ %>
	          var contains = 'false';
			 <% for (Iterator iter = statesInRegion.iterator(); iter.hasNext();){%>
			    if (v == '<%=iter.next()%>'){
				  contains='true';													  		    
			    }
			 <%}%>
			 if(contains=='false'){
			 return disableNeeds();
			 }
			 else{
			 return enableNeeds();
			 }
	    
	<%}%>
}

function disableNeeds(){
document.getElementById('docTypeId').disabled='disabled';
document.getElementById('totalNeedsId').disabled='disabled';
document.getElementById('totalNeedsTextId').disabled='disabled';
document.getElementById('docPublishedDateId').disabled='disabled';
document.getElementById('docPublishedDateTextId').disabled='disabled';
}

function enableNeeds(){
document.getElementById('docTypeId').disabled='';
document.getElementById('totalNeedsId').disabled='';
document.getElementById('totalNeedsTextId').disabled='';
document.getElementById('docPublishedDateId').disabled='';
document.getElementById('docPublishedDateTextId').disabled='';
}

function checkPopulationType(form){
    
	var selType=form['populationTypeId'];
	var indx = selType.selectedIndex;
	var populationType = '';
	
	if (indx != null) {
		populationType = selType.options[indx].value;
	}
	if(populationType==''){
	  document.getElementById('populationCountId').disabled='disabled';
	}
	else
	document.getElementById('populationCountId').disabled='';
}	
function checkFlowType(form){
    
	var selType=form['flowTypeId'];
	var indx = selType.selectedIndex;
	var flowType = '';
	
	if (indx != null) {
		flowType = selType.options[indx].value;
	}
	if(flowType==''){
	  document.getElementById('flowCountId').disabled='disabled';
	}
	else
	document.getElementById('flowCountId').disabled='';
}	
</SCRIPT>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>
<SCRIPT type=text/javascript>
	setImgDir('<%=imgurl%>');
</script>	

<form name="<%=formName%>" method="POST" action="<%=formAction%>" id="advanceSearchFormId">
    <!--Set hidden fields required by portal-->
    <%=formHiddenFields%>
    <input type="hidden" name="<%=eventSubmit %>" value="search">
    <input type="hidden" name="<%=actionParam %>" value="advance">
          							  <DIV align="right">
                    					 <FONT class="PortletText1">
                     					    <A href="javascript:void(0);" onclick='ShowAdvancedSearchHelp("<%=url%><bean:message key="<%=helpKey %>"/>")'>Help</A>
                     					 </FONT>
					                  </DIV>							           
										<TABLE class=RegionBorder cellSpacing=0 cellPadding=2 width="100%" border=1>
											<TBODY>
												<TR>
													<TD class=RegionHeaderColor width="100%">
													   <TABLE cellSpacing=0 cellPadding=2 width="100%" border=1 class="PortletText1">
															<TBODY>
																<TR>
																	<TD width="5">
																		Review Status
																	</TD>
																	<TD>
																		<SELECT name="<%=reviewStatusParam%>" multiple="multiple" size="4" style="width: 240">
																			<OPTION value=""></OPTION>
																			<logic:iterate id="reviewStatus" name="reviewStatusList" type="Entity">
																			  <bean:define id="reviewStatusId"><bean:write name="reviewStatus" property="key"/></bean:define>											
																			  <%
																			  		if(reviewStatusIn!=null){
																			  		boolean contains = false;
																			  		 for(int i=0; i<reviewStatusIn.length;i++){
																			  		    if (reviewStatusId.equals(reviewStatusIn[i])){
																							contains=true;													  		    
																			  		    }
																			  		 }
																			  		 if(contains){      
																			   %>								
																			        		<option value="<%=reviewStatusId%>" selected="selected"> <bean:write name="reviewStatus" property="value"/></option>
																			   <%	
																			   		  } else{
																			    %>
																			   				<option value="<%=reviewStatusId%>"> <bean:write name="reviewStatus" property="value"/></option>
																				<%
																					  }
																					}else {  
																				 %>
																				      <option value="<%=reviewStatusId%>"> <bean:write name="reviewStatus" property="value"/></option>
																				  <% 
																				  	 } 
																				  %>  
																			  </logic:iterate>
																			</logic:iterate>
																		</SELECT>
																	</TD>
																	<TD>&nbsp;</TD>
																</TR>
																<TR>
																	<TD>
																		Facility/Project Name
																	</TD>
																	<TD>
																		<INPUT type="text" name="<%=facNameParam%>" size="16" maxlength="30" value="<%=facNameIn %>">
																	</TD>
																	<TD>&nbsp;</TD>
																</TR>
																<TR>
																	<TD>
																		Overall Type
																	</TD>
																	<TD>
																		<SELECT name="<%=overAllTypeParam %>" multiple="multiple" style="width: 240">
																			<OPTION value="" >
																			</option>
																			<logic:iterate id="overAllNatureType" name="overAllNatureTypes" type="Entity">
																			    <bean:define id="overAllNatureTypeId"><bean:write name="overAllNatureType" property="key"/></bean:define>
 																				<%
																			  		if(overAllTypeIn!=null){
																			  		boolean contains = false;
																			  		 for(int i=0; i<overAllTypeIn.length;i++){
																			  		    if (overAllNatureTypeId.equals(overAllTypeIn[i])){
																							contains=true;													  		    
																			  		    }
																			  		 }
																			  		 if(contains){      
																			     %>								
																			        		<option value="<%=overAllNatureTypeId%>" selected="selected"> <bean:write name="overAllNatureType" property="value"/></option>
																			     <%	
																			   		  } else{
																			      %>
																			   				<option value="<%=overAllNatureTypeId%>"> <bean:write name="overAllNatureType" property="value"/></option>
																				  <%
																					  }
																					}else {  
																				   %>
																				      <option value="<%=overAllNatureTypeId%>"> <bean:write name="overAllNatureType" property="value"/></option>
																				   <% 
																				  	 } 
																				   %>
																			</logic:iterate>
																		</SELECT>
																	</TD>
																	<TD>&nbsp;</TD>
																</TR>
																<TR>
																	<TD>
																		 Authority
																	</TD>
																	<TD>
																		<INPUT name="<%=authorityParam %>" id="authority" type="text" size="16" maxlength="30" value="<%=authorityIn %>">  
																	</TD>
																	<TD>&nbsp;</TD>
																</TR>																
																<TR>
																	<TD>
																		 System Name
																	</TD>
																	<TD>
																		<INPUT name="<%= sysnameParam%>" id="systemName" type="text" size="16" maxlength="30" value="<%=sysnameIn%>">  
																	</TD>
																	<TD>
																	   <%
																	   String eventPopupWinowUrl1 = CWNSEventUtils.constructEventLink(pRequest,"SystemNameListPage", linkParams,true, true);%>
																	   <A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl1 %>","system")'">
																	        <pdk-html:img page="/images/findsmall.gif" border="0" alt="System Name Lookup"/>
																	   </A>
																	</TD>
																</TR>
																<TR>
																	<TD>
																		 County Name
																	</TD>
																	<TD>
																		<INPUT name="<%=countyParam %>" id="countyName" type="text" size="16" maxlength="30" value="<%=countyIn%>">  
                                                                    </TD>
																	<TD>
																	   <%
							                                           String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(pRequest,"ViewCountyListPage", linkParams,true, true);%>
																	   <A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl2%>","county")'">
																	        <pdk-html:img page="/images/findsmall.gif" border="0" alt="Lookup County"/>
																	   </A>
																	</TD>
																</TR>
																<TR>
																	<TD>
																		 Watershed
																	</TD>
																	<TD>
																		<INPUT name="<%=watershedParam%>" id="watershedName" type="text" size="16" maxlength="30" value="<%=watershedIn%>">  
																	</TD>
																	<TD>
																	    <%
							                                           String eventPopupWinowUrl3 = CWNSEventUtils.constructEventLink(pRequest,"WatershedListPage", linkParams,true, true);%>
																	   <A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl3%>","watershed")'">
																	        <pdk-html:img page="/images/findsmall.gif" border="0" alt="watershed lookup"/>
																	   </A>
																	</TD>
																</TR>
																<tr>
																	<td>
																		Document Type
																	</td>
																	<td>
																		<SELECT name="<%=docTypeParam %>" style="width: 240" id="docTypeId">
																			<OPTION value=""></OPTION>
																		   <logic:iterate id="documentTypeRef" name="documentTypeRefs" type="DocumentTypeRef">
																				<OPTION value='<bean:write name="documentTypeRef" property="documentTypeId"/>' <%if(documentTypeRef.getDocumentTypeId().equalsIgnoreCase(docTypeIn))out.println("SELECTED");%>> <bean:write name="documentTypeRef" property="documentTypeId"/> - <bean:write name="documentTypeRef" property="name"/></OPTION> 
																				</logic:iterate>
																		</SELECT>
																	</td>
																	<td>
																		&nbsp;
																	</td>
																</tr>
																<TR>
																	<TD>
																		Total Needs
																		<SELECT name="<%=needOperatorParam%>" id="totalNeedsId">
																		  <% 
																		     if(needOperatorIn !=null && "gt".equals(needOperatorIn)){
																		  %>
																			     <OPTION value="gt"  selected="selected">&gt;</option>
																		  <%
																		  	 }else{
																		   %>	  
																			   <OPTION value="gt">&gt;</option>   
																		   <%
																		   	 }
																		    
																		     if(needOperatorIn !=null && "lt".equals(needOperatorIn)){
																		   %>
																			  <OPTION value="lt" selected="selected">&lt;</option>
																		   <%
																		  	 }else{
																		   %>	  
																			   <OPTION value="lt">	&lt;</option>
																		   <%
																		   	 }
																		     if(needOperatorIn !=null && "eq".equals(needOperatorIn)){
																		   %>
																			  <OPTION value="eq" selected="selected">=</option>
																		   <%
																		  	 }else{
																		   %>	  
																			   <OPTION value="eq">=</option>
																		   <%
																		   	 }
																		   %>																			
																			
																		</SELECT>
																	</TD>
																	<TD>													
																		<INPUT name="<%=needTotalParam  %>" type="text" size="16" maxlength="12" value="<%=needTotalIn%>" id="totalNeedsTextId">&nbsp;($, Official, Adjusted)
														            </TD>
																	<TD>&nbsp;</TD>
																</TR>
																<TR>
																	<TD width="10">
																		 Document Published Date
																		 <select name="<%=docOperatorParam%>" id="docPublishedDateId">
																		 	<option value="lt" <%if(docOperatorIn.equalsIgnoreCase("lt"))out.println("SELECTED");%>><</option>
																		 	<option value="gt" <%if(docOperatorIn.equalsIgnoreCase("gt"))out.println("SELECTED");%>>></option>
																		 	<option value="eq" <%if(docOperatorIn.equalsIgnoreCase("eq"))out.println("SELECTED");%>>=</option>
																		 </select>
																	</TD>
																	<TD>
																		
																	<INPUT type="text" name="<%=docDateParam %>" size="16" maxlength="30" value="<%=docDateIn %>" id="docPublishedDateTextId">  </TD>
																	<TD>
																		<a href="javascript: void(0);" onclick="<%=showCal%>"> <pdk-html:img page="/images/cal.jpg" border="0"/></a>
																	</TD>
																</TR>
																<tr>
																	<td>
																		Present Population 
																		<select name="<%=presPopulationOperatorParam%>">
																			<option value="gt" <%if(presPopulationOperatorIn.equalsIgnoreCase("gt"))out.println("SELECTED");%>>></option>
																		 	<option value="lt" <%if(presPopulationOperatorIn.equalsIgnoreCase("lt"))out.println("SELECTED");%>><</option>
																		 	<option value="eq" <%if(presPopulationOperatorIn.equalsIgnoreCase("eq"))out.println("SELECTED");%>>=</option>
																		 </select>
																	</td>
																	<td>
																		<SELECT name="<%=presPopulationTypeParam%>"  style="width: 240" id="populationTypeId" onchange="checkPopulationType(form);">
																		<OPTION value=""></OPTION>
																		<logic:iterate id="populationRef" name="populationRefs" type="PopulationRef">
																		<bean:define id="populationId" name="populationRef" property="populationId"/>
																			<OPTION value='<bean:write name="populationRef" property="populationId"/>' <%if(populationId.toString().equalsIgnoreCase(presPopulationTypeIn))out.println("SELECTED");%>> <bean:write name="populationRef" property="name"/></OPTION> 
																		</logic:iterate>
																		</SELECT><br>
																		<input type="text" name="<%=presPopulationCountParam%>" size="16" maxlength="30" value="<%=presPopulationCountIn %>" id="populationCountId"/>
																	</td>
																	<td>
																		&nbsp;
																	</td>
																</tr>
																<tr>
																	<td>
																		Present Flow
																		<select name="<%=presFlowOperatorParam%>">
																			<option value="gt" <%if(presFlowOperatorIn.equalsIgnoreCase("gt"))out.println("SELECTED");%>>></option>
																		 	<option value="lt" <%if(presFlowOperatorIn.equalsIgnoreCase("lt"))out.println("SELECTED");%>><</option>
																		 	<option value="eq" <%if(presFlowOperatorIn.equalsIgnoreCase("eq"))out.println("SELECTED");%>>=</option>
																		 </select>
																	</td>
																	<td>
																		<SELECT name="<%=presFlowTypeParam%>" style="width: 240" id="flowTypeId" onchange="checkFlowType(form);">
																		<OPTION value=""></OPTION>
																		<logic:iterate id="flowRef" name="flowRefs" type="FlowRef">
																			<bean:define id="flowId" name="flowRef" property="flowId"/>
																			<OPTION value='<bean:write name="flowRef" property="flowId"/>' <%if(flowId.toString().equalsIgnoreCase(presFlowTypeIn))out.println("SELECTED");%>> <bean:write name="flowRef" property="name"/></OPTION> 
																		</logic:iterate>
																		</SELECT><br>
																		<input type="text" name="<%=presFlowCountParam%>" size="16" maxlength="30" value="<%=presFlowCountIn %>" id="flowCountId"/>
																	</td>
																	<td>
																		&nbsp;
																	</td>
																</tr>
																<TR>
																	<TD width="10">
																		 Facility/Project Last Changed
																		 <select name="<%=facProjectOperatorParam%>">
																		 	<option value="lt" <%if(facProjectOperatorIn.equalsIgnoreCase("lt"))out.println("SELECTED");%>><</option>
																		 	<option value="gt" <%if(facProjectOperatorIn.equalsIgnoreCase("gt"))out.println("SELECTED");%>>></option>
																		 	<option value="eq" <%if(facProjectOperatorIn.equalsIgnoreCase("eq"))out.println("SELECTED");%>>=</option>
																		 </select>
																	</TD>
																	<TD>
																		<INPUT type="text" name="<%=notChangedParam %>" size="16" maxlength="30" value="<%=notChangedIn %>">  
																	</TD>
																	<TD>
																		<a href="javascript: void(0);" onclick="<%=showCal2%>"> <pdk-html:img page="/images/cal.jpg" border="0"/></a>
																	</TD>
																</TR>
																<TR>
																	<TD>
																		Permit Number
																	</TD>
																	<TD>
																		<INPUT type="text" name="<%=permitNumberParam %>" size="16" maxlength="30" value="<%=permitNumberIn%>">
																	</TD>
																	<TD>&nbsp;</TD>
															    </TR>
																<TR>
																	<TD>
																		State
																	</TD>
																	<TD>
																		
												
																		<SELECT name="<%=stateParam %>" id="stateId" onchange="enableOrDisableNeeds(form)">
																				<option value=""></option>
																			   <logic:iterate id="locationRef" name="locationRefs" type="LocationRef">
																				<OPTION value='<bean:write name="locationRef" property="locationId"/>' 
																				<%if(locationRef.getLocationId().equalsIgnoreCase(defaultLocation) && stateIn.length() <= 0)out.println("SELECTED");
																				if(locationRef.getLocationId().equalsIgnoreCase(stateIn))out.println("SELECTED");%>> <bean:write name="locationRef" property="name"/></OPTION> 
																				</logic:iterate>
																			
																		</SELECT>
																	</TD>
																	<TD>&nbsp;</TD>
																</TR>
																<TR>
																	<TD colspan="3"><B> Show&nbsp;Facilities/Projects &nbsp;</B>
																	  <%
																	    if ("".equals(errorStatusIn)){
																	   %>
																	      <INPUT type="radio" name="<%=errorStatusParam %>" value="" checked="checked"/>All
																	   <%
																	   	 } else{ 	
																	    %>   
																	      <INPUT type="radio" name="<%=errorStatusParam %>" value=""/>All
																	    <%
																	     }
																	     %>  
																	  <%
																	    if ("N".equals(errorStatusIn)){
																	   %>
																	      <INPUT type="radio" name="<%=errorStatusParam %>" value="N" checked="checked">No Errors
																	   <%
																	   	 } else{ 	
																	    %>   
																	      <INPUT type="radio" name="<%=errorStatusParam %>" value="N">No Errors
																	    <%
																	     }
																	     %>  
																	  <%
																	    if ("Y".equals(errorStatusIn)){
																	   %>
	   																	  <INPUT type="radio" name="<%=errorStatusParam %>" value="Y" checked="checked">With Errors 
																	   <%
																	   	 } else{ 	
																	    %>   
       																	  <INPUT type="radio" name="<%=errorStatusParam %>" value="Y">With Errors 
																	    <%
																	     }
																	     %>  
																	</TD>
																</TR>																
																<TR>
																	<TD colspan="3">
																		<P align="center">
																			<INPUT type="button" name="searchbtn" value="Search" onclick="validateFieldsAndSubmit();"> 
																			<input type="button" value="Clear" name="clear" onclick="clearForm(<%=formName%>);"/>
																			
																		</P>

																	</TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
												</TR>
											</TBODY>
										</TABLE><br>
</form>										
<script type="text/javascript">
	     var ftform =document.getElementById('advanceSearchFormId');
	     enableOrDisableNeeds(ftform);
	     checkPopulationType(ftform);
	     checkFlowType(ftform);
	</script>
															    