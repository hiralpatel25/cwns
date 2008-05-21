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
<%@ page import="gov.epa.owm.mtb.cwns.impairedWatersInformation.ImpairedWatersForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			
  			ImpairedWatersForm impairedWatersForm = (ImpairedWatersForm)request.getAttribute("impairedWatersform");				
  			String envirofactsUrl = CWNSProperties.getProperty("enviromapper.url","http://map8.epa.gov/scripts/esrimap.dll?name=NHDMapper&Cmd=ZoomInByEntity&th=0.3&im=on");
%>

<SCRIPT type=text/javascript>
// Set the value of the "action" hidden attribute
function ImpairedWaterInfoConfirmAndSubmit(action, listId, waterBodyName)
{
    hidden_field = window.document.getElementById("impairedWaterListId");
	hidden_field.value=listId;
	
	hidden_field = window.document.getElementById("impairedWaterWaterBodyName");
	hidden_field.value=waterBodyName;
    
    hidden_field = window.document.getElementById("impairedWatersAct");
	hidden_field.value=action;
	window.document.ImpairedWatersFormBean.submit();
	return true;
    
}
</SCRIPT>

<pdk-html:form 	name="ImpairedWatersFormBean" styleId="ImpairedWatersFormBeanId"
				type="gov.epa.owm.mtb.cwns.impairedWatersInformation.ImpairedWatersForm"
				action="impairedWaters.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="impairedWatersAct" property="impairedWatersAct" value="<%=impairedWatersForm.getImpairedWaterAct()%>"/>
		<pdk-html:text styleId="impairedWaterFacilityId" property="impairedWaterFacilityId" value="<%=impairedWatersForm.getImpairedWaterFacilityId()%>"/>
		<pdk-html:text styleId="impairedWaterListId" property="listId" value=""/>
		<pdk-html:text styleId="impairedWaterWaterBodyName" property="waterBodyName" value=""/>
	</DIV>
<br/>
	<TABLE class="PortletText1" cellpadding="0" cellspacing="0"
		width="100%" border="0">
		<TR>
			<TD align="left">
				<FONT size="4"><STRONG>Impaired Waters</STRONG> </FONT>
			</TD>
		</TR>
	</TABLE>
<br/>	
<!--303D Impaired Water Information -->
<TABLE class="PortletText1" border="0" cellpadding="3" cellspacing="3" width="100%" style="BORDER-TOP: rgb(122,150,223) thin solid;">
		<TR class="PortletHeaderColor">
			<TD>
				<P align="center">
					<STRONG> <FONT color="#ffffff"> List ID </FONT> </STRONG>
				</P>
			</TD>
			<TD>
				<P align="center">
					<STRONG> <FONT color="#ffffff"> Water Body Name </FONT> </STRONG>
				</P>
			</TD>
			<TD>
				<P align="center">
					<STRONG> <FONT color="#ffffff">TMDL </FONT> &nbsp;</STRONG>
				</P>
			</TD>
			<logic:equal name="ImpairedWatersFormBean" property="isUpdatable" value="Y">
				<TD class="PortletHeaderText" align="center">
				    Delete
				</TD>
			</logic:equal>
		</TR>
			<c:set var="i" value="1" />
			<logic:iterate id="IWInfoHelper" name="ImpairedWatersFormBean" property="impairedWatersListHelpers"
				type="gov.epa.owm.mtb.cwns.impairedWatersInformation.ImpairedWatersListHelper">
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
						<FONT class="PortletText1">
							<logic:notEmpty name="IWInfoHelper" property="listUrl">
								<logic:match name="IWInfoHelper" 
										property="listUrl" value="http://">
									<A href="javascript:impairedWatersSearchPopUp('<bean:write name="IWInfoHelper" property="listUrl"/>');">
										<bean:write name="IWInfoHelper" property="listId"/>
									</A>
								</logic:match>
								<logic:notMatch name="IWInfoHelper" 
										property="listUrl" value="http://">
									<bean:write name="IWInfoHelper" property="listId"/>
								</logic:notMatch>								
							</logic:notEmpty>	
							<logic:empty name="IWInfoHelper" property="listUrl">	
								<bean:write name="IWInfoHelper" property="listId"/>
							</logic:empty>
						</FONT>
					</TD>
					<TD align="center">
					    <bean:define id="eUrl"><%=envirofactsUrl%>&SYS=<bean:write name="IWInfoHelper" property="waterBodyType"/>&EntityID=<bean:write name="IWInfoHelper" property="listId"/></bean:define>  
						<FONT class="PortletText1">
							<bean:write name="IWInfoHelper" property="waterBodyName" />
							<A href="javascript:impairedWatersSearchPopUp('<bean:write name="eUrl"/>');">
							    <pdk-html:img page="/images/map.gif" alt="launch enviromapper" border="0"/>
							</A>
						</FONT>
					</TD>
					<TD align="center">	
						<FONT class="PortletText1">
						 <logic:notEmpty name="IWInfoHelper" property="waterBodyDetailUrl">
							<logic:match name="IWInfoHelper" 
									property="waterBodyDetailUrl" value="http://">
								<A href="javascript:impairedWatersSearchPopUp('<bean:write name="IWInfoHelper" property="waterBodyDetailUrl"/>');">
									Detail Report
								</A>
							</logic:match>
							<logic:notMatch name="IWInfoHelper" 
									property="waterBodyDetailUrl" value="http://">
								<bean:write name="IWInfoHelper" property="waterBodyDetailUrl"/>
							</logic:notMatch>	
							</logic:notEmpty>
							<logic:empty name="IWInfoHelper" property="waterBodyDetailUrl">		
								&nbsp;
							</logic:empty>						
						</FONT>						
					</TD>
					<logic:equal name="ImpairedWatersFormBean" property="isUpdatable" value="Y">
						<TD align="center">
							<logic:equal name="IWInfoHelper" property="hasCost"  value="N">
				    			<logic:equal name="isFeedback" value="true">
				         			<bean:define id="delcall">javascript:ImpairedWaterInfoConfirmAndSubmit('delete', '<bean:write name="IWInfoHelper" property="listId"/>', '<bean:write name="IWInfoHelper" property="waterBodyName"/>')</bean:define>
				         			<logic:equal name="IWInfoHelper" property="feedbackDeleteFlag" value="Y">
				            			<input type="checkbox" name="delete" checked="checked" onclick="<%=delcall%>">
				         			</logic:equal>
				         			<logic:notEqual name="IWInfoHelper" property="feedbackDeleteFlag" value="Y">
				            			<input type="checkbox" name="delete" onclick="<%=delcall%>">
				         			</logic:notEqual>
				    			</logic:equal> 
				    			<logic:notEqual name="isFeedback" value="true">
				    				<A href="javascript:ImpairedWaterInfoConfirmAndSubmit('delete', '<bean:write name="IWInfoHelper" property="listId"/>', '<bean:write name="IWInfoHelper" property="waterBodyName"/>')">
									    <pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
								    </A>
				    			</logic:notEqual>														
							</logic:equal>	
							<logic:equal name="IWInfoHelper" property="hasCost"  value="Y">
									<pdk-html:img page="/images/delete_disable.gif" alt="Can't be deleted because cost(s) or cost curve exist" border="0"/>
							</logic:equal>						
						</TD>
					</logic:equal>
				</TR>
				<c:set var="i" value="${i+1}" />
			</logic:iterate>
		
</TABLE>
<!--303D Impaired Water Information -->
</pdk-html:form>