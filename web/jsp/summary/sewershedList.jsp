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

<%
	PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
%>

<SCRIPT type=text/javascript>
       function closeWindow(){
		window.close();
	}
</SCRIPT>
 <%
  String disablePresent = "false";
  String disableProjected = "false";
  if("false".equals(request.getAttribute("hasPresentSewershed")))
     disablePresent = "true";
  if("false".equals(request.getAttribute("hasProjectedSewershed")))
     disableProjected = "true";   
 %>

<div align="right"><a href="javascript:closeWindow()">Close</a>
</div> 

<STRONG>
	<FONT size="4">
		<FONT face="Arial">
			 Sewershed
		</FONT>
	</FONT>
</STRONG>
<BR>
<DIV id="radio_button_div" 
         style="display:block">
</DIV>

<pdk-html:form name="sewershedListForm" method="post" styleId="sewershedListFormId" type="org.apache.struts.action.DynaActionForm" action="sewershedList.do">

  <TABLE cellspacing="0" cellpadding="0" width="100%" style="border-bottom-width:1  ;border-bottom: thin; border-bottom-color: blue">
   <TR>
      <TD>
	   
	    <FONT class="PortletText1">
	        <pdk-html:radio name="sewershedListForm" property="dischargeType" styleId="dischargeTypePresent" 
						          value="0" onclick="this.form.submit();" disabled="<%=disablePresent%>">
						          <STRONG><FONT size="2" face="Arial">Present</FONT></STRONG></pdk-html:radio>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<pdk-html:radio name="sewershedListForm" property="dischargeType" styleId="dischargeTypeProjected" 
			                      value="1" onclick="this.form.submit();" disabled="<%=disableProjected%>">
			                      <STRONG><FONT size="2" face="Arial">Projected</FONT></STRONG></pdk-html:radio>			
        </FONT>
	   
	</TD>
	</TR>
	</TABLE>
	<BR>
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
				</TR>            

		 <c:set var="i" value="1" />                      			
         <logic:iterate id="populationHelper" name="populationHelperList" 
                type="gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper">
          
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
							 <bean:write name="populationHelper" property="disFacilityCwnsNbr"/><br>
							 <bean:write name="populationHelper" property="disFacilityName"/>
					</P></TD>
				<TD>
					<P align="left">
							 <bean:write name="populationHelper" property="recFacilityCwnsNbr"/><br>
							 <bean:write name="populationHelper" property="recFacilityName"/>
					</P></TD>
					</TR>

		<c:set var="i" value="${i+1}" />

			</logic:iterate>
</TABLE>
</pdk-html:form>