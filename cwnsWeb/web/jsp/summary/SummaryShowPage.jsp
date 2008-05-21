<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
        import= "oracle.portal.provider.v2.url.UrlUtils"
        import="oracle.portal.utils.NameValue"
        import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
        import="org.apache.struts.util.MessageResources"
%>

<%@ page import="gov.epa.owm.mtb.cwns.summary.SummaryHelper"
         import="gov.epa.owm.mtb.cwns.summary.SummaryForm"
%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
   String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
                    
   
   SummaryForm summaryForm = (SummaryForm)request.getAttribute("summary");
   NameValue[] linkParams       = new NameValue[1];
   String helpKey = (String)request.getAttribute("helpKey");
%>

<SCRIPT type=text/javascript>

// Set the value of the show cost curve
  function setCheckboxAndSubmit(){
	 window.document.summaryFormBean.submit();
	 return true;
   }

// Set the value of the "action" hidden attribute
  function setAction(status){
	 hidden_field = window.document.getElementById("summaryAct");
	 hidden_field.value=status;
	 // window.document.reviewCommentsListBean.submit();
	 return true;
   }
   
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

     var w = window.open(url, 'Summary', settings);
     if(w != null) 
        w.focus();
   }
</SCRIPT>   

<pdk-html:form 	name="summaryFormBean" 
				type="gov.epa.owm.mtb.cwns.summary.SummaryForm"
				action="summary.do">
				
<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="DISPLAY:none">	
		<pdk-html:text styleId="summaryAct" property="summaryAct" value=""/>
		<pdk-html:text styleId="facilityId" property="facilityId" value="<%=summaryForm.getFacilityId()%>"/>
	</DIV>				
    <DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='ShowAllPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
		</FONT>
	</DIV>
				
<TABLE class="PortletText1" cellpadding="1" width="100%" border="1">
    <logic:equal name="isNonLocalUser" value="true">                                                      															
															<TR>
																<TD></TD>
																<logic:equal name="summaryFormBean" property="showCorrection" value="Y">
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Correction">Cor</A>
																	</P>
																</TD>
																</logic:equal>
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Required">Req</A>
																	</P>
																</TD>
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Entered">Ent</A>
																	</P>
																</TD>
																<TD>
																	<P align="center">
																       <logic:equal name="summaryFormBean" property="showChanged" value="Y">		
																		<A href="javascript:void(0);" style="text-decoration:none" title="Changed">Chg</A>
																	   </logic:equal>
																	   <logic:notEqual name="summaryFormBean" property="showChanged" value="Y">
																	    <A href="javascript:void(0);" style="text-decoration:none" title="Error">Err </A>
																	   </logic:notEqual>	
																	</P>
																</TD>
																<logic:equal name="summaryFormBean" property="showCostCurve" value="Y">
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Cost Curve Error">CC Err</A>
																	</P>
																</TD>
																</logic:equal>
															</TR>
	</logic:equal>
	<logic:equal name="isNonLocalUser" value="false">
	   <TR>
																<TD></TD>
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Required">Req</A>
																	</P>
																</TD>
																<TD>
																	<P align="center">
																		<A href="javascript:void(0);" style="text-decoration:none" title="Entered">Ent</A>
																	</P>
																</TD>
																
															</TR>
	</logic:equal>														
															<logic:iterate id="summaryHelper" name="summaryFormBean" property="summHelpers"
															       type="gov.epa.owm.mtb.cwns.summary.SummaryHelper">
															<TR>
																<TD>
																	<!-- <A href="javascript:void(0);"> -->
																		<%if("Facility".equalsIgnoreCase(summaryHelper.getName())){ %>
																		   <logic:equal name="summaryFormBean" property="isFacility" value="Y">
																		     <bean:write name="summaryHelper" property="name"/>
																		   </logic:equal>
																		   <logic:notEqual name="summaryFormBean" property="isFacility" value="Y">
																		     Project
																		   </logic:notEqual> 
																		<%} else{%>
																		     <bean:write name="summaryHelper" property="name"/>
																		   <%} %>   
																	<!-- </A> -->
																</TD>
																<logic:equal name="isNonLocalUser" value="true">
																<logic:equal name="summaryFormBean" property="showCorrection" value="Y">
																<TD>
																	<P align="center">
																		<logic:equal name="summaryHelper" property="correction" value="Y">
																		    
																		    <logic:equal name="summaryFormBean" property="check_box" value="Yes">
																		         <pdk-html:multibox name="summaryFormBean" property="dataAreaTypes"><bean:write name="summaryHelper" property="name"/></pdk-html:multibox> 
																		    </logic:equal>
																		    <logic:notEqual name="summaryFormBean" property="check_box" value="Yes">
																		         <pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
																		    </logic:notEqual> 
																		 					    
																		</logic:equal>
																		<logic:notEqual name="summaryHelper" property="correction" value="Y">
																		   <logic:equal name="summaryFormBean" property="check_box" value="Yes">
																		      <pdk-html:multibox name="summaryFormBean" property="dataAreaTypes"><bean:write name="summaryHelper" property="name"/></pdk-html:multibox>
																		    </logic:equal>
																		    <logic:notEqual name="summaryFormBean" property="check_box" value="Yes">
																		         &nbsp;
																		    </logic:notEqual>
																		</logic:notEqual>
																	</P>
																</TD>
																</logic:equal>
																</logic:equal>
																<TD>
																   <P align="center">
																	   <logic:equal name="summaryHelper" property="required" value="Y">
																		    <pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
																		</logic:equal>
																		<logic:notEqual name="summaryHelper" property="required" value="Y">
																		     &nbsp;
																		</logic:notEqual>  															                                       
																	</P>
																</TD>
																
																<TD>
																	<P align="center">
																		<logic:equal name="summaryHelper" property="entered" value="Y">
																		    <pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
																		</logic:equal>
																		<logic:notEqual name="summaryHelper" property="entered" value="Y">
																		     &nbsp;
																		</logic:notEqual>
																	</P>
																</TD>
																<logic:equal name="isNonLocalUser" value="true">
																<TD>
																	<P align="center">
																	   <logic:equal name="summaryHelper" property="error" value="Y">
																		    <pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
																		</logic:equal>
																		<logic:notEqual name="summaryHelper" property="error" value="Y">
																		     &nbsp;
																		</logic:notEqual>
																	</P>
																</TD>
																</logic:equal>
																<logic:equal name="isNonLocalUser" value="true">
																<logic:equal name="summaryFormBean" property="showCostCurve" value="Y">
																<TD>
																	<P align="center">
																		<logic:equal name="summaryHelper" property="ccerror" value="Y">
																		    <pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
																		</logic:equal>
																		<logic:notEqual name="summaryHelper" property="ccerror" value="Y">
																		     &nbsp;
																		</logic:notEqual>
																	</P>
																</TD>
																</logic:equal>
																</logic:equal>
															</TR>
															</logic:iterate>
															<logic:equal name="isNonLocalUser" value="true">
															<logic:equal name="summaryFormBean" property="showCorrection" value="Y">
															     <logic:equal name="summaryFormBean" property="check_box" value="Yes">
															          <TR>
															             <TD> &nbsp;</TD>
																         <TD colspan="5">
																            <pdk-html:submit  onclick="setAction('save')" value="Save"/>
																         </TD>
															          </TR>
															     </logic:equal>
															</logic:equal>
															</logic:equal>
															<%--          
															<TR>
																<TD colspan="5">
																    <logic:equal name="summaryFormBean" property="showCostCurve" value="Y">
																         <pdk-html:multibox name="summaryFormBean" property="costCurve" value="Y" onclick="setCheckboxAndSubmit()"/>
																    </logic:equal>
																    <logic:notEqual name="summaryFormBean" property="showCostCurve" value="Y">
																         <pdk-html:multibox name="summaryFormBean" property="costCurve" value="Y" disabled="true"/>
																	</logic:notEqual>
																	<STRONG>
																		Show Only Cost Curve Errors
																	</STRONG>
																</TD>
															</TR> --%>
															
															<TR class="PortletText1" >
		                                                        <TD align="left" colspan="5" >
		                                                        <%linkParams[0] = new NameValue("facilityId", summaryForm.getFacilityId());%>
			                                                        <logic:notEmpty name="summaryFormBean" property="facCommentHelpers">
			                                                        <% String eventPopupWinowUrl1 = CWNSEventUtils.constructEventLink(pReq,"ViewAllCommentsPage", linkParams,true, true);%>
			                                                        
			                                                        <A href="javascript:void(0);" onclick='ShowAllPopUp("<%=eventPopupWinowUrl1%>")'>view all comments</A>
			                                                            
			                                                        <!-- <IFRAME id="allComments" name="allComments" src="<%=url%>" STYLE="width:525px;height:300px;position:absolute;top:50px;left:200px;display:none; border:2px; border-color: #6699CC; border-style: solid" scrolling="auto"></IFRAME>-->
			                                                        <logic:equal name="summaryFormBean" property="isFacilityInSewershed" value="Y">
			                                                           &nbsp;|&nbsp;
			                                                           <% String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(pReq,"ViewSewershedPage", linkParams,true, true);%>
			                                                           <A href="javascript:void(0);" onclick='ShowAllPopUp("<%=eventPopupWinowUrl2%>")'>
				                                                              view sewershed
			                                                           </A>
			                                                        </logic:equal>			                                                        
			                                                        </logic:notEmpty>
			                                                        <logic:empty name="summaryFormBean" property="facCommentHelpers">
			                                                           <logic:equal name="summaryFormBean" property="isFacilityInSewershed" value="Y">
			                                                           <% String eventPopupWinowUrl2 = CWNSEventUtils.constructEventLink(pReq,"ViewSewershedPage", linkParams,true, true);%>
			                                                           <A href="javascript:void(0);" onclick='ShowAllPopUp("<%=eventPopupWinowUrl2%>")'>
				                                                              view sewershed
			                                                           </A>
			                                                           </logic:equal>
			                                                        </logic:empty>
			                                                     </TD>
			                                                </TR>        
			                                                       
															<TR>
																<TD colspan="5">
																	<STRONG>
																		State:
																	</STRONG>
																	<bean:write name="summaryFormBean" property="lastUpdatedUserName"/>&nbsp; 
																	<bean:write name="summaryFormBean" property="lastUpdateTS"/>
																</TD>
															</TR>
															<TR>
																<TD colspan="5">
																	<STRONG>
																		Reviewer:
																	</STRONG>
																	<bean:write name="summaryFormBean" property="reviewerName"/>&nbsp; 
																	<bean:write name="summaryFormBean" property="lastReviewedTS"/>
																</TD>
															</TR>
														</TABLE>
														</pdk-html:form>