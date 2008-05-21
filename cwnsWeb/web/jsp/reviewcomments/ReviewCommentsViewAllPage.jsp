<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%@page contentType="text/html; charset=windows-1252"
        import="oracle.portal.provider.v2.render.PortletRenderRequest"
        import="oracle.portal.provider.v2.http.HttpCommonConstants"
	    import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListHelper"%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   NameValue[] linkParams       = new NameValue[1];
   ReviewCommentsListForm reviewCommentsListForm = (ReviewCommentsListForm)request.getAttribute("reviewCommentsForm");
%>

<SCRIPT type=text/javascript>

// Show or Hide the add comment area of the Portlet
function showOrHide(id, mode)
{
     window.document.getElementById(id).style.display=mode;
}
/*
function PassBack() { 
   if(window.opener && !window.opener.closed) 
   {
     var t1 = document.getElementById("text1").value;
     window.opener.document.getElementById("inputComments").value=t1;
   }
   window.close();
   window.opener.location.reload();
}
*/
</SCRIPT>

<pdk-html:form 	name="reviewCommentsListBean" 
				type="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListForm"
				action="reviewComments.do">
	
<TABLE class="PortletText1" cellpadding="1" width="100%" border="1">
	<TR>
		<TD align="center" width="25%">
			<b>Author/Date</b>
		</TD>
		<TD align="center" width="75%">
			<b>Comment</b>
		</TD>
	</TR>

	<logic:iterate id="revCommHelper" name="reviewCommentsListBean"  property="reviewCommentsHelpers"
				type="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListHelper">
	<TR>
		<TD width="25%">
			<bean:write name="revCommHelper" property="userName"/>	                                                      
			<br/>
			<bean:write name="revCommHelper" property="updateDate"/>
		</TD>
		<TD width="75%">
		            <logic:notEmpty name="revCommHelper" property="longComments">
					    <bean:define id="longCommentsFromBean" name="revCommHelper" property="longComments"/> 
					    <%=((String)longCommentsFromBean).replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")%>
			        </logic:notEmpty>
			        <logic:empty name="revCommHelper" property="longComments">
					    <bean:define id="shortCommentsFromBean" name="revCommHelper" property="shortComments"/> 
					    <%=((String)shortCommentsFromBean).replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")%>
			        </logic:empty>
		</TD>
	</TR>
	</logic:iterate>

	<!-- 
		<br/><br/>
		<center>
		<input type="text" name="text1" id="text1"/><br/>
		<input type="submit" name="Close" value="Close" onclick="PassBack()"/>
		</center>
	-->
</TABLE>
</pdk-html:form>