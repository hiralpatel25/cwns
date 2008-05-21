<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="java.util.ArrayList"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>

<%
	PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	
	String facilityId = request.getParameter("facilityId");
	
	String url = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
			
	String pref = pReq.getRenderContext().getPortletPageReference();
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pReq,
							"../../css/cwns.css")%>">

<SCRIPT type=text/javascript>
       function closeWindow(){
		window.close();
	}
</SCRIPT>

<div align="right"><a href="javascript:closeWindow()">Close</a>
</div> 

<STRONG>
	<FONT size="4">
		<FONT face="Arial">
			 Upstream Facility Populations Receiving Collection
		</FONT>
	</FONT>
</STRONG>
<BR>
<logic:present name="errorMessages">
<div id="warnMessageId" style="display:block">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <logic:iterate id="errorMessage" name="errorMessages" type="String">
	 <tr>
         <td align="center" valign="middle" width="40">
	     	<pdk-html:img page="/images/err24.gif" border="0"/>	
	     </td>
	     <td class="WarningText" align="left" valign="middle">
	     	<bean:message key="<%=errorMessage %>"/><br>
	     </td>
     </tr>
     </logic:iterate>
     </table>
</div>
</logic:present>
<TABLE class="PortletText1" border="1" cellpadding="1" cellspacing="1" width="100%">
			<TR class="PortletHeaderColor">
				<TD>
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Discharging CWNS Number
							</FONT>
						</STRONG></P>
				</TD>
				<TD>
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Receiving CWNS Number
							</FONT>
						</STRONG></P>
				</TD>
				<TD colspan="2">
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Present Resident<br/>
								 Actual / Terminal
							</FONT>
						</STRONG></P>
				</TD>
				<TD colspan="2">
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Present Non-Resident<br/>
								 Actual / Terminal
							</FONT>
						</STRONG></P>
				</TD>
				<TD colspan="2">
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Future Resident<br/>
								 Actual / Terminal
							</FONT>
						</STRONG></P>
				</TD>
				<TD colspan="2">
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Future Non-Resident<br/>
								 Actual / Terminal
							</FONT>
						</STRONG></P>
				</TD>
				<TD>
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Combined Sewer
							</FONT>
						</STRONG></P>
				</TD>
				<TD>
					<P align="center">
						<STRONG>
							<FONT color="#ffffff">
								 Out of State
							</FONT>
						</STRONG></P>
				</TD>
			</TR>            

		 <c:set var="i" value="1" />                      			
         <logic:iterate id="disRcvHelper" name="dischargeReceiveHelperList" 
                type="gov.epa.owm.mtb.cwns.populationInformation.DischargeReceiveHelper">
                <bean:define id="receivingFacility" name="disRcvHelper" property="receivingFacility"/>
                <bean:define id="dischargingFacility" name="disRcvHelper" property="dischargingFacility"/>          

		<c:choose>
			<c:when test='${i%2=="0"}'>
				<c:set var="class" value="PortletSubHeaderColor" />   
			</c:when>
			<c:otherwise>
				<c:set var="class" value="" />
			</c:otherwise>
		</c:choose>

			<TR class="<c:out value="${class}"/>">    
				<TD>				
					<P align="left">	
							 <bean:write name="dischargingFacility" property="facilityCwnsNbr"/><br>
							 <bean:write name="dischargingFacility" property="facilityName"/>
					</P></TD>
				<TD>
					<P align="left">
							 <bean:write name="receivingFacility" property="facilityCwnsNbr"/><br>
							 <bean:write name="receivingFacility" property="facilityName"/>
					</P></TD>
				<TD>
 					<P align="right">				
						<bean:write name="dischargingFacility" property="actualPresentResPopulation"/>
					</P>
					</td>
					<td>
					<P align="right">				
						<%=(int)(0.5 + disRcvHelper.getDischargingFacility().getActualPresentResPopulation() * disRcvHelper.getPresentResPopulationPersant())%>
					</P>
				</TD>					
				<TD>
 					<P align="right">				
						<bean:write name="dischargingFacility" property="actualPresentNonResPopulation"/>
					</P>
					</td>
					<td>
					<P align="right">				
						<%=(int)(0.5 + disRcvHelper.getDischargingFacility().getActualPresentNonResPopulation() * disRcvHelper.getPresentNonResPopulationPersant())%>
					</P>
				</TD>			

				<TD>
 					<P align="right">				
						<bean:write name="dischargingFacility" property="actualProjectedResPopulation"/>
					</P>
					</td>
					<td>
					<P align="right">				
						<%=(int)(0.5 + disRcvHelper.getDischargingFacility().getActualProjectedResPopulation() * disRcvHelper.getProjectedResPopulationPersant())%>
					</P>
				</TD>	
						
				<TD>
 					<P align="right">				
						<bean:write name="dischargingFacility" property="actualProjectedNonResPopulation"/>
					</P>
					</td>
					<td>
					<P align="right">				
						<%=(int)(0.5 + disRcvHelper.getDischargingFacility().getActualProjectedNonResPopulation() * disRcvHelper.getProjectedNonResPopulationPersant())%>
					</P>
				</TD>		
				
				<TD align="center" valign="center" >	
				<logic:equal name="disRcvHelper" property="combinedSewer" value="Y">
					<pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
				</logic:equal>
				&nbsp;
				</TD>

				<TD align="center" valign="center" >
				<logic:equal name="disRcvHelper" property="outOfState" value="Y">
					<pdk-html:img page="/images/checkmark.gif" alt="Checked"/>
				</logic:equal>
				&nbsp;
				</TD>

			</TR>

		<c:set var="i" value="${i+1}" />

			</logic:iterate>
</TABLE>