<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import= "oracle.portal.provider.v2.url.UrlUtils" 
	import="org.apache.struts.action.DynaActionForm"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.Entity"%>
<%@ page import="gov.epa.owm.mtb.cwns.reviewstatus.ReviewStatusAction" %>	
<%@page import="gov.epa.owm.mtb.cwns.reviewstatus.ReviewStatusHelper"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%
    String stateId = "";
	PortletRenderRequest prr = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	NameValue[] linkParams   = new NameValue[3];
	NameValue[] tlinkParams   = new NameValue[8];			
	String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();			
	HttpSession httpsession =request.getSession();
     if(httpsession!=null){
           stateId = (String)httpsession.getAttribute("state");
     } 
	 	
	DynaActionForm  reviewStatusForm = (DynaActionForm)request.getAttribute("reviewStatusForm");
	String helpKey = (String)request.getAttribute("helpKey");
%>

<SCRIPT type=text/javascript>
	function ShowAllPopUp(popupUrl)
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

  var w = window.open(popupUrl, null, settings);
  if(w != null) 
    w.focus();     
}
function submitreviewForm(action){
 document.getElementById("actionId").value=action;
 window.document.reviewStatusForm.submit();
}

</SCRIPT>
<pdk-html:form name="reviewStatusForm" method="post" styleId="reviewStatusFormId" 
   type="org.apache.struts.action.DynaActionForm" action="reviewStatus.do" >
   <%--<bean:define id="actId"><bean:write name="reviewStatusForm" property="action"/></bean:define>--%>
   <pdk-html:hidden name="reviewStatusForm" property="action" styleId="actionId"/>	

 <P align="right">
	<FONT class="PortletText1">
	    <A href="javascript:void(0);" onclick='ShowAllPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>
	    <pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
	    </A>
	</FONT>
 </P>
<bean:define id="revState"><bean:write name="state"/></bean:define>	
<TABLE width="100%" border="0">
	<TBODY>
		<logic:equal name="showStatesDropdown" value="true">
		<TR>
	  	<TD class=RegionHeaderColor>
			<STRONG>State:</STRONG>&nbsp;&nbsp;
		</TD>
		<TD>
		    <pdk-html:select size="1" name="reviewStatusForm" property="locationId" styleId="locationId">
		    	<option value='HQ'>All</option>
		    	<logic:iterate id="location" name="locationRefs" type="Entity">
		  		<logic:match name="location" property="key" value='<%=stateId%>'>				
 					<OPTION value='<bean:write name="location" property="key"/>' selected="selected"> <bean:write name="location" property="value"/></OPTION> 
 				</logic:match>
		  		<logic:notMatch name="location" property="key" value='<%=stateId%>'>
 					<OPTION value='<bean:write name="location" property="key"/>'> <bean:write name="location" property="value"/></OPTION> 
 				</logic:notMatch>
		        </logic:iterate>
            </pdk-html:select>
            <A href="javascript: submitreviewForm('<%=ReviewStatusAction.ACTION_SEARCH%>')"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A> 
		</TD>
	</TR>
	</logic:equal>            
		<TR>
			<TD align="left" colspan="3">
				<FONT class="PortletText1">
					<TABLE border="0" cellspacing="0" width="100%">						
						<TR class="PortletSubHeaderColor">
							<TD>
								<P align="left">
									<FONT class="PortletText1"><B>Status</B><FONT> </FONT>
									</FONT>
								</P>
							</TD>
							<TD>
								<P align="center">
									<FONT class="PortletText1"><B>Total</B>
									</FONT>
								</P>
							</TD>
						</TR>
						<c:set var="i" value="1" />
						<logic:iterate id="rs" name="revStatus" type="ReviewStatusHelper">
							<c:choose>
								<c:when test='${i%2=="0"}'>
									<c:set var="class" value="PortletSubHeaderColor" />
								</c:when>

								<c:otherwise>
									<c:set var="class" value="" />
								</c:otherwise>
							</c:choose>


							<logic:match name="rs" value="Total" property="reviewStatusDesc">
                                <TR><TD>&nbsp;</TD></TR>
								<TR class="<c:out value="${class}"/>">

									<TD>
										<FONT class="PortletText1">
										<B>
										    <bean:write name="rs" property="reviewStatusDesc" />
										</B>
										</FONT>
									</TD>
									<TD>
										<P align="center">
											<logic:equal name="rs" property="reviewStatusCount" value="0">
												<FONT class="PortletText1">
												 <B>
												 	<bean:write name="rs" property="reviewStatusCount" />
												 </B>
												</FONT>
											</logic:equal>
												<%
 													tlinkParams[0] = new NameValue("action", "advance");
 													tlinkParams[1] = new NameValue("reviewStatus", "'FA','FRR','FRC','SAS','SIP','SCR'");
 													tlinkParams[1] = new NameValue("reviewStatus", "FA");
 													tlinkParams[2] = new NameValue("reviewStatus", "FRR");
 													tlinkParams[3] = new NameValue("reviewStatus", "FRC");
 													tlinkParams[4] = new NameValue("reviewStatus", "SAS");
 													tlinkParams[5] = new NameValue("reviewStatus", "SIP");
 													tlinkParams[6] = new NameValue("reviewStatus", "SCR");
 													tlinkParams[7] = new NameValue("state", revState);
 												%>
											
												<A href='<%=CWNSEventUtils.constructEventLink(prr,"advanceKeyword", tlinkParams,true, true)%>'>
												  <FONT class="PortletText1">
												    <B>
												    	<bean:write	name="rs" property="reviewStatusCount" />
													</B>
												  </FONT>
												</A>
											</logic:notEqual>
										</P>
									</TD>
								</TR>
							</logic:match>
							<logic:notMatch name="rs" value="Total" property="reviewStatusDesc">
							   <logic:match name="rs" value="Feedback Status" property="reviewStatusDesc">
							      <TR><TD>&nbsp;</TD></TR>
							      <TR class="<c:out value="${class}"/>">
							        
									<TD>
										<FONT class="PortletText1"> <B><bean:write name="rs" property="reviewStatusDesc" /></B> </FONT>
									</TD>
									
								  </TR>
								   	
							   </logic:match>
							   <logic:notMatch name="rs" value="Feedback Status" property="reviewStatusDesc">
								<TR class="<c:out value="${class}"/>">
									<TD>
										<FONT class="PortletText1"> 
											<bean:write name="rs" property="reviewStatusDesc" /> 
										</FONT>
									</TD>
									<TD>
										<P align="center">
										    
											<logic:equal name="rs" property="reviewStatusCount" value="0">
												<FONT class="PortletText1">
													<bean:write name="rs" property="reviewStatusCount" />
												</FONT>
											</logic:equal>
											<logic:notEqual name="rs" property="reviewStatusCount" value="0">
											   	<%
 													linkParams[0] = new NameValue("action", "advance");
 													linkParams[1] = new NameValue("reviewStatus", rs.getReviewStatusId());
 													linkParams[2] = new NameValue("state", revState);
 												%>
							                    <A href='<%=CWNSEventUtils.constructEventLink(prr,"advanceKeyword", linkParams,true, true)%>'>											
												  <FONT class="PortletText1">
													<bean:write	name="rs" property="reviewStatusCount"/>
												  </FONT>
												</A>
											</logic:notEqual>
										</P>
									</TD>
								</TR>
							  </logic:notMatch>
							</logic:notMatch>
							<c:set var="i" value="${i+1}" />
						</logic:iterate>


					</TABLE> &nbsp;</FONT>
			</TD>
		</TR>
		<TR></TR>
	</TBODY>
</TABLE>
</pdk-html:form>
