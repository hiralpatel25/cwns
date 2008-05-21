<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.url.UrlUtils"
	import="gov.epa.owm.mtb.cwns.reviewstatus.FacilityReviewStatusForm"
	import="gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%
			PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			
			String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
			FacilityReviewStatusForm facilityReviewStatusForm = (FacilityReviewStatusForm)request.getAttribute("facilityReviewStatus");
			String helpKey = (String)request.getAttribute("helpKey");
%>

<SCRIPT type=text/javascript>
  function ShowAllPopUp(url){
     var w = 800;
     var h = 500;
	 var winl = (screen.width-w)/2;
	 var wint = (screen.height-h)/2;
	 var settings ='height='+h+',';
	 settings +='width='+w+',';
	 settings +='top='+wint+',';
	 settings +='left='+winl+',';
	 settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

     var w = window.open(url, 'FacilityReviewStatus', settings);
     if(w != null) 
        w.focus();
   }
   function appearAUser(){
       document.getElementById('aUsers').style.display='block';
   }  
   function submitAction(){
   <%if (!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(facilityReviewStatusForm.getUserType())){%>
    <%if ("Y".equalsIgnoreCase(facilityReviewStatusForm.getIsUpdatable())){%> // if S facility's review status updatable
     var element;
     var value = "";
     var result  = "";
     var c = "<%=facilityReviewStatusForm.getIsFacilitySmallCommunity()%>";
     var currentReviewStatusId = "<%=facilityReviewStatusForm.getCurrentReviewStatusId()%>";
     var currentFeedbackStatusId = "<%=facilityReviewStatusForm.getCurrentFeedbackStatusId()%>";
     var hasDataAreaErrors = "<%=request.getAttribute("hasDataAreaErrors")%>";
     var hasCorrectionIndicators = "<%=request.getAttribute("hasCorrectionIndicators")%>";
     var isFacilityInSewershed = "<%=request.getAttribute("isFacilityInSewershed")%>";
     var facilitiesInSate = "<%=request.getAttribute("facilitiesInState")%>";
     var smallCommunityFacilities = "<%=request.getAttribute("smallCommunityFacilities")%>";
     var facilitiesWithFeedbackCopy = "<%=request.getAttribute("facilitiesWithFeedbackCopy")%>";
     
     element = document.getElementById('reviewStatusId');
     for (var i=0; i < element.options.length; i++){
         if (element.options[i].selected == true) {
           value = element.options[i].value;
         }
     }
     
         if (isFacilityInSewershed == "N" && c == "Y" && value == "FRR"){
            var result = confirm("By continuing with submission for Federal review, you certify that: \n" +
                                 "(1) you have received appropriately signed Small Community Form and/or \n" +
                                 "(2) you are documenting needs and costs with other documentation \n" +
                                 "Continue to Submit for Federal Review?");
            if (result == true){
              //document.getElementById('facilityReviewStatusAct').value = "save";
              //document.getElementById('selectedReviewStatus').value = value;
              //window.document.facilityReviewStatusFormBean.submit();
              //return true;
            }
            else
              return;
         
         }else    
            if (isFacilityInSewershed == "N" && (value == "FRR" ||value == "FRC")
                 && (currentFeedbackStatusId == "LAS" || currentFeedbackStatusId == "LIP")) {
                
               var result = confirm("A feedback version  exists for this facility and is assigned to the Local user.\n" +
                                 "Updating the facility review status now will prevent you from applying changes reflected\n" +
                                "on the Feedback version at a later stage. Continue with the update?\n");
                                
               if (result == true){
                // document.getElementById('facilityReviewStatusAct').value = "save";
                 //document.getElementById('selectedReviewStatus').value = value;
                 //window.document.facilityReviewStatusFormBean.submit();
                 //return true;
               }
               else
                 return;
            }
            else if (isFacilityInSewershed == "Y" && value == "FRR"){
                   // if any facility in the sewershed is a small community
                   if(smallCommunityFacilities.length>0){
                     var result = confirm("By continuing with submission for Federal review, you certify that: \n" +
                                 "(1) you have received appropriately signed Small Community Form and/or \n" +
                                 "(2) you are documenting needs and costs with other documentation for the following facilities in the sewershed.\n" +
                                 smallCommunityFacilities + "\n Continue to submit for Federal Review?");
                     if (result == true){
                       //document.getElementById('facilityReviewStatusAct').value = "save";
                       //document.getElementById('selectedReviewStatus').value = value;
                       //window.document.facilityReviewStatusFormBean.submit();
                       //return true;
                     }
                     else
                      return;
                   }
            }
            
             //new
         
            if(isFacilityInSewershed == "Y" && currentReviewStatusId == "SCR" && (value == "FRR" || value == "FRC")){
                  // if any facility in the sewershed has feedback copy with review status of LAS or LIP
                  if(facilitiesWithFeedbackCopy.length>0){
                       var result = confirm("A feedback version exists for the following facilities in the sewershed \n" +
                                 "and is assigned to the Local user:\n" + facilitiesWithFeedbackCopy +
                                 "Updating the facility review status now will prevent you from applying changes\n"+
                                 "reflected on the Feedback version at a later stage. Continue with the update?\n");
                       if (result == true){
                         //document.getElementById('facilityReviewStatusAct').value = "save";
                         //document.getElementById('selectedReviewStatus').value = value;
                         //window.document.facilityReviewStatusFormBean.submit();
                         //return true;  
                       } 
                       else
                        return;
                    }
           
            }else if(isFacilityInSewershed == "Y" && (value == "FRR" || value == "FRC")){
                     if(hasDataAreaErrors == "Y"){
                        alert("At least one facility in this facility’s sewershed has Data Area error(s): review status cannot be updated.");
                        return;
                     }
                     else{
                        if (facilitiesInSate == "Y"){
                           var result = confirm("This facility is part of a sewershed.\n" +
                                 "The review status for all facilities in this sewershed will be updated. Continue with the update?\n");
                        }
                        else{
                            var result = confirm("This facility is part of a sewershed spanning multiple states.\n" +
                                 "The review status for all facilities in this sewershed will be updated. Continue with the update?\n");
                        }                
                        if (result == true){
                        } 
                        else
                          return;  
                     } 
              }
         
         <%if (UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(facilityReviewStatusForm.getUserType())){%>   
            if(isFacilityInSewershed == "Y" && value == "FA"){
              if (hasCorrectionIndicators == "Y"){
                alert("At least one facility in this facility’s sewershed has Correction indicator(s): review status cannot be updated.");
                return;
              }
              else{
                 var result = confirm("This facility is part of a sewershed.\n" +
                                 "The review status for all facilities in this sewershed will be updated. Continue with the update?\n");
                 if (result == true){
                   //document.getElementById('facilityReviewStatusAct').value = "save";
                   //document.getElementById('selectedReviewStatus').value = value;
                   //window.document.facilityReviewStatusFormBean.submit();
                   //return true;  
                 } 
                 else
                   return;
              }
              
            }else 
                 if(isFacilityInSewershed == "Y" && value == "SCR"){
                    var result = confirm("This facility is part of a sewershed.\n" +
                                 "The review status for all facilities in this sewershed will be updated. Continue with the update?\n");
                    if (result == true){
                      //document.getElementById('facilityReviewStatusAct').value = "save";
                      //document.getElementById('selectedReviewStatus').value = value;
                      //window.document.facilityReviewStatusFormBean.submit();
                      //return true;  
                    } 
                    else
                     return;
                 }
         <%}%>   
            // default-has to modify later
            //else {
                  document.getElementById('facilityReviewStatusAct').value = "save";
                   document.getElementById('selectedReviewStatus').value = value;
                  // window.document.facilityReviewStatusFormBean.submit();
                   //return true; 
            //}
    <%}// end of if S facility's review status updatable 
    }   
    if ("Y".equalsIgnoreCase(facilityReviewStatusForm.getIsFeedbackStatusUpdatable())){ %> // if  feedback review status is updatable
       var element;
       var value = "";
       element = document.getElementById('feedBackStatusId');
       for (var i=0; i < element.options.length; i++){
         if (element.options[i].selected == true) {
           value = element.options[i].value;
           
         }
       }
       document.getElementById('facilityReviewStatusAct').value = "save";
       document.getElementById('selectedFeedbackStatus').value = value;
       //window.document.facilityReviewStatusFormBean.submit();
       //return true;   
    <%}%>
      window.document.facilityReviewStatusFormBean.submit();
      return true;
   }        
</SCRIPT>
<pdk-html:form name="facilityReviewStatusFormBean" 
               action="facilityReviewStatus.do"
               type="gov.epa.owm.mtb.cwns.reviewstatus.FacilityReviewStatusForm">
   <DIV id="hidden_fields" style="DISPLAY:none">	
	<pdk-html:text styleId="facilityReviewStatusAct" property="facilityReviewStatusAct" value=""/> 
	<pdk-html:text styleId="selectedReviewStatus" property="selectedReviewStatus" value=""/>
	<pdk-html:text styleId="currentReviewStatusId" property="currentReviewStatusId" value="<%=facilityReviewStatusForm.getCurrentReviewStatusId() %>"/>
	<pdk-html:text styleId="selectedFeedbackStatus" property="selectedFeedbackStatus" value=""/>
	<pdk-html:text styleId="currentFeedbackStatusId" property="currentFeedbackStatusId" value="<%=facilityReviewStatusForm.getCurrentFeedbackStatusId() %>"/>
    <pdk-html:hidden styleId="isUpdatable" property="isUpdatable" value="<%=facilityReviewStatusForm.getIsUpdatable() %>"/>
    <pdk-html:hidden styleId="isFeedbackStatusUpdatable" property="isFeedbackStatusUpdatable" value="<%=facilityReviewStatusForm.getIsFeedbackStatusUpdatable()%>"/>
   </DIV>	           
   <DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='ShowAllPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
		</FONT>
	</DIV>    
<TABLE cellSpacing=0 cellPadding=2 width="100%" border=1>
	<TBODY>
		<TR>
			<TD class=RegionHeaderColor width="100%">
				<TABLE width="100%">
					<TBODY>
						<TR></TR>
						<TR>
							<TD class="PortletText1" align="left" colspan="3">
								<TABLE class="PortletText1" cellpadding="1" width="100%"
									border="1">
                                  <%if (!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(facilityReviewStatusForm.getUserType())){%>
									<TR>
										<TD>
											<B> Status </B>
										</TD>
										<logic:equal name="facilityReviewStatusFormBean" property="isUpdatable" value="Y">
										<TD>
										    
											<SELECT name="reviewStatus" id="reviewStatusId">
												<logic:iterate id="status" name="facilityReviewStatusFormBean" property="reviewStatues" 
												        type="gov.epa.owm.mtb.cwns.common.Entity">
												
												<logic:match name="facilityReviewStatusFormBean" property="currentReviewStatusId" value='<%=status.getKey()%>'>
												  <option value='<%=status.getKey()%>' selected="selected"> <%=status.getValue()%> </option>
												</logic:match>
												<logic:notMatch name="facilityReviewStatusFormBean" property="currentReviewStatusId" value='<%=status.getKey()%>'>
												  <option value='<%=status.getKey()%>'> <%=status.getValue()%> </option>
												</logic:notMatch> 																			   
												</logic:iterate>
											</SELECT>
										</TD>
										</logic:equal>
										<logic:notEqual name="facilityReviewStatusFormBean" property="isUpdatable" value="Y">
										    <TD>
										       <bean:write name="facilityReviewStatusFormBean" property="currentReviewStatus" />
										    </TD>
										</logic:notEqual>
									</TR>
								   <%} %>	
								<logic:equal name="facilityReviewStatusFormBean" property="showfeedbackstatus" value="Y">
									<TR>
										<TD>
											<B>Feedback Status</B>
										</TD>
										<logic:equal name="facilityReviewStatusFormBean" property="isFeedbackStatusUpdatable" value="Y">
										<TD>
										   
										   <SELECT name="feedBackStatus" id="feedBackStatusId">
												<logic:iterate id="feedbackstatus" name="facilityReviewStatusFormBean" property="feedBackStatues"
												        type="gov.epa.owm.mtb.cwns.common.Entity">
												<logic:match name="facilityReviewStatusFormBean" property="currentFeedbackStatusId" value='<%=feedbackstatus.getKey()%>'>
												  <option value='<%=feedbackstatus.getKey()%>' selected="selected"> <%=feedbackstatus.getValue()%> </option>
												</logic:match>
												<logic:notMatch name="facilityReviewStatusFormBean" property="currentFeedbackStatusId" value='<%=feedbackstatus.getKey()%>'>
												  <option value='<%=feedbackstatus.getKey()%>'> <%=feedbackstatus.getValue()%> </option>
												</logic:notMatch> 																			   
												</logic:iterate>
										   </SELECT>
										</TD>
										</logic:equal>
										<logic:notEqual name="facilityReviewStatusFormBean" property="isFeedbackStatusUpdatable" value="Y">
										    <TD>
										       <bean:write name="facilityReviewStatusFormBean" property="currentFeedbackStatus" />
										    </TD>
										</logic:notEqual> 
										
									</TR>
								</logic:equal>
								<logic:equal name="facilityReviewStatusFormBean" property="showfeedbackstatus" value="N">
								     <%if (UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(facilityReviewStatusForm.getUserType())){%>
								         <TR>
								            <TD>
								                Feedback copy does not exist
								            </TD>
								         </TR>
								      <%}%>
								</logic:equal>
									 <!--
									<tr>
										<td colspan="2">
										    
										   <a href="javascript:void(0);" onclick="appearAUser()"><b>Associated Local Users</b></a>
										   <div id="aUsers" style="display:none">
											  <table width="100%" border="0" cellspacing="0" cellpadding="3" class="portletText1">
											     <tr><td>Tim Robins</td></tr>
											     <tr><td>Scott Blocher</td></tr>
											   </table>
											</div>
											
										</td>
									</tr> 	-->
									<%if ("Y".equalsIgnoreCase(facilityReviewStatusForm.getIsUpdatable())||
									       "Y".equalsIgnoreCase(facilityReviewStatusForm.getIsFeedbackStatusUpdatable())){%>
									<TR>
										<TD colspan="2">
										    <A href="javascript: submitAction()"><FONT size="1"><pdk-html:img page="/images/submit.gif" alt="save" border="0"/></FONT></A>
										</TD>
									</TR>
									<%} %>
								</TABLE>
							</TD>
						</TR>
						<TR></TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
</pdk-html:form>
