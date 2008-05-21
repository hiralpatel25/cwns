<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.provider.v2.PortletDefinition"%>
<%@ page import="oracle.portal.provider.v2.ParameterDefinition"%>
<%@ page import="gov.epa.owm.mtb.cwns.impairedWatersSearch.ImpairedWatersSearchListForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.impairedWatersSearch.ImpairedWatersSearchAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.service.ImpairedWatersService"%>
<%@page import="oracle.portal.provider.v2.personalize.PortletReference"%>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil"%>
<%@page import="oracle.portal.provider.v2.url.UrlUtils"%>
<%@page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@page import="oracle.portal.utils.NameValue" %>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
  			ImpairedWatersSearchListForm impairedWatersSearchForm = (ImpairedWatersSearchListForm)request.getAttribute("impairedWatersSearchListForm");				
  			String helpKey = (String)request.getAttribute("helpKey");
%>

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

//check if the given value is a number
var numb = "0123456789";
function isNumber(parm) {
	for (i = 0; i < parm.length; i++) {
		if (numb.indexOf(parm.charAt(i), 0) == -1) {
			return false;
		}
	}
	return true;
}

function ShowLookUpWindow(popupUrl)
{

	window.document.getElementById("ImpairedWatersSearchTextLabel").innerHTML='HUC-8 (defaulted to Primary Watershed)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	window.document.getElementById("watershedId").value = "<%=impairedWatersSearchForm.getPrimaryHUC()%>";
	window.document.getElementById("webServiceMethod_HUC8").checked=true;

    var w = 600;
    var h = 300;
    var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';
    var url = popupUrl+'&_event_locationId=<%=impairedWatersSearchForm.getStateId()%>';  
    var w = window.open(url, null, settings);
  if(w != null) 
    w.focus();     
}

function impairedWatersSearchSubmitenter(e)
{
 var keycode;
 if (window.event) keycode = window.event.keyCode;
 else if (e) keycode = e.which;
 else return true;
 if (keycode == 13){
   impairedWatersSearchSetActionAndSubmit('search', 0);
   return false;
 } else
   return true;
}

// Set the value of the "action" hidden attribute
function impairedWatersSearchSetActionAndSubmit(action, nextResult)
{
	if(action == 'search')
	{
	    var waterShedId = window.document.getElementById("watershedId");
	    
	    var testResult = true;
	    
	    var trimmedValue = trim(waterShedId.value);
	    
	    var keywordId = window.document.getElementById("keywordText")
	    
	    var trimmedKeywordValue = trim(keywordId.value);

		if(window.document.getElementById("webServiceMethod_KeywordOnly").checked == true)
		{
		    if(trimmedKeywordValue == "")
		    {
		  	  alert("Keyword is required.");
				testResult = false;
		    } 
		}
		else if(window.document.getElementById("webServiceMethod_Distance").checked == true)
		{
		    if(trimmedValue == "")
		    {
		  	  alert("Radius is required.");
				testResult = false;
		    } 
		    else
		    {
			    var radius = trimmedValue;
				if (!isNumber(radius) || radius<=0 || radius>50) 
				{
			  	  alert("Radius must be greater than 0 and less than or equal to 50.");
				  testResult = false;
				}    	
		    }
		}
		else if(window.document.getElementById("webServiceMethod_HUC8").checked == true)
		{
		    if(trimmedValue == "")
		    {
		  	  alert("HUC8 code is required.");
			  testResult = false;
		    } 
		    else if(trimmedValue.length != 8)
		    {
		  	  alert("HUC must be 8 characters long.");
			  testResult = false;
		    } 
		    else
		    {
		        var strValidChars = "0123456789";
		    	for(var i=0; i<trimmedValue.length;i++)
		    	{
		    		var strChar = trimmedValue.charAt(i);
		    		if(strValidChars.indexOf(strChar) < 0)
		    		{
		    			alert("HUC8 should not contain non-numeric characters.");
		   			    testResult = false;		    			
		    		}
		    	}
		    }
		}
		
		if(testResult == false)
		{
		    if(window.document.getElementById("webServiceMethod_KeywordOnly").checked == true)
		    	keywordId.focus();
		    else
				waterShedId.focus();
				
			return;
		}
	}else if(action == 'add'){
		var form = window.document.ImpairedWatersSearchListFormBean;
		if (!isCheckBoxesSelected(form, 'impairedWatersCheckBoxes')){
			alert('At least one Impaired Water must be selected.');
			return false;
		}
	}

	 hidden_field = window.document.getElementById("impairedWatersSearchAct");
	 hidden_field.value=action;

     hidden_field = window.document.getElementById("IWSnextResult");
	 hidden_field.value=nextResult;
 
	 window.document.ImpairedWatersSearchListFormBean.submit();
	 return true;
}

// Show or Hide the add comment area of the Portlet
function impairedWatersSearchShowOrHide(id, mode)
{
     window.document.getElementById(id).style.display=mode;
}

function ImpairedWatersSearchSetFocus(ctrlId)
{
	window.document.getElementById(ctrlId).focus();
}

function impairedWatersSearchClearSearchForm()
{
     window.document.getElementById("keywordText").value="";
     window.document.getElementById("watershedId").value="";
}

function impairedWatersSearchPopUp(popupUrl)
{
    var w = 900;
    var h = 600;
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='toolbar=yes,titlebar=yes,menubar=yes,scrollbars=yes,status=yes,resizable=yes,location=yes';

  var w = window.open(popupUrl, 'ImpairedWatersSearch', settings);
  if(w != null) 
    w.focus();     
}

function ImpairedWatersSearchSetText(searchType)
{
	 	t = window.document.getElementById("watershedId");
	 	
	 	t.value = "";
	 	
	 	label = window.document.getElementById("ImpairedWatersSearchTextLabel");

		if(searchType == 'Distance')
		{
			t.disabled = false;
			t.className = '';
		    label.innerHTML='Radius from Facility Coordinates(max 100 miles)';		
		}
		else if(searchType == 'Watershed')
		{
		    t.disabled = false;
		    t.className = '';
			label.innerHTML='HUC-8 (defaulted to Primary Watershed)';
			t.value = "<%=impairedWatersSearchForm.getPrimaryHUC()%>";
		}
		else if(searchType == 'Keyword')
		{
			t.disabled = true;
			t.className = 'disabledField';
		}
}

function disableDistanceRadio(){	
	var dtRadio = window.document.getElementById("webServiceMethod_Distance");
	var hasCoordinates = '<%=request.getAttribute("hasCoordinateInfo")%>';
	if (dtRadio!=null && hasCoordinates=='N'){
		var t = window.document.getElementById("webServiceMethod_HUC8");
	
		dtRadio.disabled = true;		
		t.checked = true;		
		ImpairedWatersSearchSetText('Watershed');		
		window.document.getElementById("watershedId").value = "<%=impairedWatersSearchForm.getPrimaryHUC()%>";
		ImpairedWatersSearchSetFocus('watershedId');		
	}
}


function isCheckBoxesSelected(form,fieldName){
   var checkboxes = form[fieldName];
   
   if (checkboxes.checked == true){
   		return true;
   }   
   
   if(checkboxes != undefined && checkboxes.length != undefined && checkboxes.length > 0){ 
	 for(i=0; i< checkboxes.length; i++){
	    if(checkboxes[i].checked)return true;
	 }
   }
   return false;	
}

</SCRIPT>

<body onload="disableDistanceRadio();">
<pdk-html:form 	name="ImpairedWatersSearchListFormBean" styleId="ImpairedWatersSearchListFormBeanId"
				type="gov.epa.owm.mtb.cwns.impairedWatersSearch.ImpairedWatersSearchListForm"
				action="impairedWatersSearch.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="impairedWatersSearchAct" property="impairedWatersSearchAct" value="<%=impairedWatersSearchForm.getImpairedWatersSearchAct()%>"/>
		<pdk-html:text styleId="impairedWatersSearchFacilityId" property="impairedWatersSearchFacilityId" value="<%=impairedWatersSearchForm.getImpairedWatersSearchFacilityId()%>"/>
		<pdk-html:text styleId="IWSnextResult" property="nextResult" value="<%=impairedWatersSearchForm.getNextResult()%>"/>
		<input type=text name="watershedName" id="watershedName" value=""/>
	</DIV>
	<DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='impairedWatersSearchPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
		</FONT>
	</DIV> 
<!--303D Impaired Water Search -->
	<TABLE class=RegionBorder cellSpacing=0 cellPadding=2 width="100%" border=1>
		<TBODY>
			<TR>
				<TD class=RegionHeaderColor width="100%">
					<TABLE width="100%" border=0 class="PortletText1">
						<TBODY>
							<TR>
								<TD class="PortletText1">
								    <table width="242" border=0 class="PortletText1">
									    <tr>
											<td colspan="2">
												<center>
												<FONT class="PortletText1">
												
												<table class="PortletText1">
													<tr>
														<td width="55%" class="PortletText1">
																<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
																                property="webServiceMethod" 
																			    styleId="webServiceMethod_Distance" 
																			    value="<%=ImpairedWatersService.BY_LAT_LONG_RADIUS%>" 
																			    title="Distance(Miles)" 
																			    onclick="ImpairedWatersSearchSetText('Distance');ImpairedWatersSearchSetFocus('watershedId');">
																	Distance(Miles)
																</pdk-html:radio>
																<br/>
																<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
																                property="webServiceMethod" 
																                styleId="webServiceMethod_HUC8" 
																                value="<%=ImpairedWatersService.BY_HUC8%>" 
																                title="Watershed" 
																                onclick="ImpairedWatersSearchSetText('Watershed');ImpairedWatersSearchSetFocus('watershedId');">
																    Watershed
																</pdk-html:radio>	
																	&nbsp;
															    <%String eventPopupWinowUrl3 = CWNSEventUtils.constructEventLink(prr,"WatershedListPage", null,true, true);%>
																<A href="javascript:void(0);" onclick='ShowLookUpWindow("<%=eventPopupWinowUrl3%>")'">
																     <pdk-html:img page="/images/findsmall.gif" border="0" alt="watershed lookup"/>
																</A>
																<br/>
																<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
																                property="webServiceMethod" 
																			    styleId="webServiceMethod_KeywordOnly" 
																			    value="<%=ImpairedWatersService.BY_KEYWORD%>" 
																			    title="Keyword Only" 
																			    onclick="ImpairedWatersSearchSetText('Keyword');ImpairedWatersSearchSetFocus('keywordText');">
																	Keyword Only
																</pdk-html:radio>
														</td>
														<td width="45%" class="PortletText1">
															<pdk-html:text styleId="watershedId" property="radiusMiles" size="8" maxlength="8"
																value="<%=impairedWatersSearchForm.getRadiusMiles()%>"
																onkeypress="impairedWatersSearchSubmitenter(event);"/>
															<br/>
															<SPAN id="ImpairedWatersSearchTextLabel">
																<logic:equal name="ImpairedWatersSearchListFormBean" property="webServiceMethod" value="<%=ImpairedWatersService.BY_LAT_LONG_RADIUS%>">									
																									Radius from Facility Coordinates(max 50 miles)
																</logic:equal>	
																<logic:equal name="ImpairedWatersSearchListFormBean" property="webServiceMethod" value="<%=ImpairedWatersService.BY_HUC8%>">									
																									HUC-8 (defaulted to Primary Watershed)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																</logic:equal>									
																<logic:equal name="ImpairedWatersSearchListFormBean" property="webServiceMethod" value="<%=ImpairedWatersService.BY_KEYWORD%>">									
																									Radius from Facility Coordinates(max 50 miles)
																</logic:equal>									
															</SPAN>													
														</td>
													</tr>
												</table>		
													
											    </FONT>
												</center>
											</td>
  								        </tr>			
  								        <tr>
	  								        <td colspan="2" align="center" class="PortletText1">
	  								        Keyword 
	  								        &nbsp; 
											<pdk-html:text name="ImpairedWatersSearchListFormBean" 
														   styleId="keywordText" property="keyword" 
												value="<%=impairedWatersSearchForm.getKeyword()%>"/>
	  								        </td>
  								        </tr>		      
								    </table>  																		
								 </TD>
							</TR>
							<TR>
								<TD class="PortletText1" colspan="2">
								    <table table width="100%" 
								    	style="border-style: dashed; border-spacing: 0; border-width: 2" 
								    	class="PortletText1" cellpadding="0" cellspacing="0">
									    <tr>
											<td colspan="2">
												<center>
												<FONT class="PortletText1">
														<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
														                property="tmdlOption" 
																	    styleId="tmdlOption_All" 
																	    value="<%=ImpairedWatersService.TMDL_ALL%>" 
														                onclick="ImpairedWatersSearchSetFocus('watershedId');"															    
																	    title="All">
															All
														</pdk-html:radio>
																	    
														<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
														                property="tmdlOption" 
														                styleId="tmdlOption_TMDL_COMPLETED" 
														                value="<%=ImpairedWatersService.TMDL_COMPLETED%>" 
														                onclick="ImpairedWatersSearchSetFocus('watershedId');"																    
														                title="TDML Completed">
														    TDML Completed
														</pdk-html:radio>	
																				
														<pdk-html:radio name="ImpairedWatersSearchListFormBean" 
														                property="tmdlOption" 
														                styleId="tmdlOption_TMDL_NEEDED" 
														                value="<%=ImpairedWatersService.TMDL_NEEDED%>" 
														                onclick="ImpairedWatersSearchSetFocus('watershedId');"																    
														                title="TMDL Needed">
														    TMDL Needed
														</pdk-html:radio>			
											    </FONT>
												</center>
											</td>
  								        </tr>					      
								    </table>  																		
								 </TD>
							</TR>														
							<TR>
								<TD class="PortletText1" align="left">
                                    <input type="button" value="Search" onclick="javascript:impairedWatersSearchSetActionAndSubmit('search', 0)">							
                                    &nbsp;
                                    <input type="button" value="Clear" onclick="javascript:impairedWatersSearchClearSearchForm()">							                                   
								</TD>
							</TR>
							<TR>
							    <TD class="PortletText1">
									<TABLE width="100%" border=0 class="PortletText1" cellpadding="3">
										<TBODY>	
<logic:equal name="isValidSearch" value="Y">
<logic:equal name="hasNoRecords" value="Y">
											<TR bgcolor="#808080">
												<TD class="PortletText1" colspan="2" align="center">
													<STRONG>
														No records found!
													</STRONG>
												</TD>
											</TR>
</logic:equal>

<logic:equal name="hasNoRecords" value="N">
	<logic:equal name="exceeds1000Results" value="Y">
											<TR bgcolor="#808080">
												<TD class="PortletText1" colspan="2" align="center">
													<STRONG>
														There are more than 1000 results, please narrow down search criteria!
													</STRONG>
												</TD>
											</TR>	
	</logic:equal>
</logic:equal>
</logic:equal>

<logic:greaterThan name="ImpairedWatersSearchListFormBean" property="numOfImpairedWaters" value="0">
											<TR bgcolor="#808080">
												<TD align="center">
													<STRONG>
														Select
													</STRONG>
												</TD>
												<TD align="center">
													<Strong>
														Information
													</strong>
												</TD>
											</TR>											
			<c:set var="i" value="1" />
			<logic:iterate id="IWHelper" name="ImpairedWatersSearchListFormBean" property="impairedWatersSearchHelpers"
				type="gov.epa.owm.mtb.cwns.impairedWatersSearch.ImpairedWatersSearchListHelper">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>

				<TR class="<c:out value="${class}"/>">
					<TD align="center">
					  <logic:equal name="ImpairedWatersSearchListFormBean" property="isUpdatable" value="Y">
					    <pdk-html:multibox name="ImpairedWatersSearchListFormBean" property="listIds" styleId="impairedWatersCheckBoxes">
					       <bean:write name="IWHelper" property="listId"/>
					    </pdk-html:multibox> 
					  </logic:equal>
					  <logic:equal name="ImpairedWatersSearchListFormBean" property="isUpdatable" value="N">
					       &nbsp;
					  </logic:equal>
					</TD>

					<TD align="left">
						<FONT class="PortletText1">
							List ID:&nbsp;
							<logic:match name="IWHelper" 
									property="listUrl" value="http://">
								<A href="javascript:impairedWatersSearchPopUp('<bean:write name="IWHelper" property="listUrl"/>');">
									<bean:write name="IWHelper" property="listId"/>
								</A>
							</logic:match>
							<logic:notMatch name="IWHelper" 
									property="listUrl" value="http://">
								<bean:write name="IWHelper" property="listId"/>
							</logic:notMatch>			
						</FONT>
						<BR>
						<FONT class="PortletText1">
							<bean:write name="IWHelper" property="waterType" />
							 Water Body Name:&nbsp;
							<bean:write name="IWHelper" property="waterBodyName" />
						</FONT>
						<logic:greaterThan name="IWHelper" property="milesToLocation" value="0">
						<BR>
						<FONT class="PortletText1">
							Distance from facility:&nbsp;<bean:write name="IWHelper" property="milesToLocation" /> miles
						</FONT>
						</logic:greaterThan>
						<BR>
						<FONT class="PortletText1">
							<logic:match name="IWHelper" 
									property="waterBodyDetailUrl" value="http://">
								<A href="javascript:impairedWatersSearchPopUp('<bean:write name="IWHelper" property="waterBodyDetailUrl"/>');">
									...TMDLs
								</A>
							</logic:match>
							<logic:notMatch name="IWHelper" 
									property="waterBodyDetailUrl" value="http://">
								<bean:write name="IWHelper" property="waterBodyDetailUrl"/>
							</logic:notMatch>									
						</FONT>						
					</TD>
				</TR>
				<c:set var="i" value="${i+1}" />
			</logic:iterate>
											<TR class="PortletText1">
												<TD colspan="2" align="right">
						<!-- Prev -->
						<logic:greaterThan name="ImpairedWatersSearchListFormBean"
							property="prevImpairedWatersSearchToDisplay" value="0">
							<A href="javascript:impairedWatersSearchSetActionAndSubmit('<%=ImpairedWatersSearchAction.ACTION_FIRST%>', 0)">
								|&lt;&lt;</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							<A href="javascript:impairedWatersSearchSetActionAndSubmit('<%=ImpairedWatersSearchAction.ACTION_PREVIOUS%>', <bean:write name="ImpairedWatersSearchListFormBean" property="prevImpairedWatersSearchToDisplay"/>)">Prev</A>&nbsp;&nbsp;|&nbsp;&nbsp;
						</logic:greaterThan>
						<logic:lessEqual name="ImpairedWatersSearchListFormBean" 
							property="prevImpairedWatersSearchToDisplay" value="0">
						   |&lt;&lt;&nbsp;|&nbsp;
							Prev&nbsp;|&nbsp;
						</logic:lessEqual>

						<!-- x to y of z facilities-->
						<bean:write name="ImpairedWatersSearchListFormBean" property="fromImpairedWatersSearch" />
						-
						<bean:write name="ImpairedWatersSearchListFormBean" property="toImpairedWatersSearch" />
						of
						<bean:write name="ImpairedWatersSearchListFormBean" property="numOfImpairedWaters" />
						&nbsp;|&nbsp;
	
						<!-- Next -->
						<bean:define id="maxFacility">
							<bean:write name="ImpairedWatersSearchListFormBean" property="numOfImpairedWaters" />
						</bean:define>					
						<logic:lessThan name="ImpairedWatersSearchListFormBean" property="toImpairedWatersSearch" value="<%=maxFacility%>">
							<A href="javascript:impairedWatersSearchSetActionAndSubmit('<%=ImpairedWatersSearchAction.ACTION_NEXT%>', <bean:write name="ImpairedWatersSearchListFormBean" property="nextImpairedWatersSearchToDisplay"/>)">Next</A>&nbsp;&nbsp;|&nbsp;&nbsp;
							<A href="javascript:impairedWatersSearchSetActionAndSubmit('<%=ImpairedWatersSearchAction.ACTION_LAST%>', 0)">&gt;&gt;|</A>&nbsp;&nbsp;
						</logic:lessThan>	
					
						<logic:greaterEqual name="ImpairedWatersSearchListFormBean" property="toImpairedWatersSearch" value="<%=maxFacility%>">
							Next&nbsp;&nbsp;|&nbsp;&nbsp;
							&gt;&gt;|&nbsp;&nbsp;
						</logic:greaterEqual>	
												</TD>
											</TR>
											<logic:equal name="ImpairedWatersSearchListFormBean" property="isUpdatable" value="Y">
											<TR class="PortletText1">
												<TD colspan="2" align="center">
		                                       <INPUT type="button" value="Associate with Facility" 
		                                       		alt="Add Impaired Waters"
		                                       		onclick="javascript:impairedWatersSearchSetActionAndSubmit('add', 0)"/>												
												</TD>
											</TR>
											</logic:equal>
</logic:greaterThan>							
										</TBODY>
									</TABLE>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
		</TBODY>
	</TABLE>

<!--303D Impaired Water Search -->
</pdk-html:form>
</body>