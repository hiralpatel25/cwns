<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
        import="gov.epa.owm.mtb.cwns.model.CbrLoanInformation"
        import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"
%>

<%
	PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);     
      
	String loanid = pReq.getParameter("loanId");      
	CbrLoanInformation loanDetail = null;
	if (request.getAttribute("loanDetail")!=null){
		loanDetail = (CbrLoanInformation)request.getAttribute("loanDetail");
	}      
%>
<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(pReq,
							"../../css/cwns.css")%>">

<DIV style="background-color: #ffffff">							
<form name="loandetails" method="post" action="http://ese-ny.its-ese.local/portal/page" id="loandetails">
<br/>
<FONT class="PortletHeading1">
<FONT size="4"><STRONG>Loan Information</STRONG></FONT>
<logic:notEmpty name="loanDetail">
<TABLE width="100%" border="0">
<TBODY>
<TR>
<TD></TD>
</TR>
<TR>
<TD align="left" colspan="3" class="PortletText1">
<TABLE class="PortletText1" cellspacing="2" cellpadding="5" border="0" width="100%">
<tr>
<td width="18%"><STRONG>Borrower:</STRONG>
</td>
<td><c:out value='${loanDetail.cbrBorrower.name}'/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>Loan Execution Date:</STRONG>
</td>
<td><fmt:formatDate pattern="MM/dd/yyyy" value="${loanDetail.loanDate}"/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>Tracking #:</STRONG>
</td>
<td><c:out value='${loanDetail.loanNumber}'/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>Assistance Type:</STRONG>
</td>
<td><c:out value='${loanDetail.loanTypeRef.name}'/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>Amount:</STRONG>
</td>
<td><fmt:formatNumber type="currency" pattern="$#,##0" value="${loanDetail.loanAmount}"/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>Final Amount:</STRONG>
</td>
<td><c:out value='${loanDetail.finalAmountFlag}'/>
</td>
</tr>
<tr>
<td width="18%"><STRONG>% Funded by CWSRF:</STRONG>
</td>
<td><c:out value='${loanDetail.percentFundedByCwsrf}'/>
</td>
</tr>

</TABLE>
</TD>
</TR>
</TBODY>
</TABLE>
<br/>
<br/>
<FONT size="4"><STRONG>Project Information</STRONG></FONT>
<TABLE width="100%" border="0">
<TBODY>
<TR>
<TD></TD>
</TR>
<TR>
<TD align="left" colspan="3" class="PortletText1">
<TABLE cellspacing="2" cellpadding="1" border="0" width="100%"
				class="PortletText1">
				<TR class="PortletHeaderColor">
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Project</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">Category</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							<STRONG><FONT color="#ffffff">Amount<br/>$</FONT>
							</STRONG>
						</DIV>
					</TD>
					<TD>
						<P align="center">
							<STRONG><FONT color="#ffffff">CWNS<br/>Number</FONT>
							</STRONG>
						</P>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Facility Name</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Description</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD>
						<DIV align="center">
							<FONT color="#ffffff"><STRONG>Permit Number</STRONG>
							</FONT>
						</DIV>
					</TD>
					<TD width="8%">
						<P align="center">
							<STRONG><FONT color="#ffffff">NPDES Permit<br/>Number</FONT>
							</STRONG>
						</P>
					</TD>
					<TD align="center">
						<STRONG><FONT color="#ffffff">Permit<BR/>Type
								CWSRF</FONT>
						</STRONG>
					</TD>										
	</TR>
	<c:set var="i" value="1" />
	<bean:define id="cbrProjectInformations" name="loanDetail" property="cbrProjectInformations" type="java.util.Set"/>
	<logic:iterate id="project" name="cbrProjectInformations" type="gov.epa.owm.mtb.cwns.model.CbrProjectInformation">
	<c:choose>						
						<c:when test='${i%2=="0"}'>
							<c:set var="class" value="PortletSubHeaderColor" />   
						</c:when>						
						<c:otherwise>
							<c:set var="class" value="" />
						</c:otherwise>
					</c:choose>
	<TR class="<c:out value="${class}"/>">
	<td align="center" valign="top"><c:out value='${project.projectNumber}'/>
	</td>
	<td class="<c:out value="${class}"/>" colspan="2">
	 <bean:define id="cbrAmountInformations" name="project" property="cbrAmountInformations" type="java.util.Set"/>
		<TABLE cellspacing="2" cellpadding="1" border="0" width="100%" class="PortletText1">
			<TBODY>
			<logic:iterate id="category" name="cbrAmountInformations" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformation">
			<bean:define id="id" name="category" property="id" type="gov.epa.owm.mtb.cwns.model.CbrAmountInformationId"/>
				<TR>
				<TD width="50%" valign="top"><bean:write name="id" property="cbrCategoryId"/></TD>
				<TD width="50%" align="right" valign="top"><fmt:formatNumber type="currency" pattern="$#,##0" value="${category.amount}"/></TD>
				</TR>
			</logic:iterate>				
			</TBODY>
		</TABLE>	
	</td>
	<td valign="top"><c:out value='${project.cwnsNumber}'/>
	</td>
	<td valign="top"><c:out value='${project.facilityName}'/>
	</td>
	<td valign="top"><c:out value='${project.description}'/>
	</td>
	<td valign="top"><c:out value='${project.permitNumber}'/>
	</td>
	<td valign="top"><c:out value='${project.npdesPermitNumber}'/>
	</td>
	<td valign="top"><c:out value='${project.permitType}'/>
	</td>
	</TR>
	<c:set var="i" value="${i+1}" />
	</logic:iterate>
	<TR>
	</TR>
</TABLE>
</TD>
</TR>
<TR><TD></TD></TR>
<TR><TD>&nbsp;</TD></TR>
<TR><TD align="center"><INPUT type="Button" name="Close" value="Close" onclick="javascript: window.close();"/></TD></TR>
</TBODY>
</TABLE>
</logic:notEmpty>
<br/>
</FONT>
</form>
</DIV>