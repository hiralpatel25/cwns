<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="gov.epa.owm.mtb.cwns.summary.SummaryHelper"
         import="gov.epa.owm.mtb.cwns.summary.SummaryForm"
%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<SCRIPT type=text/javascript>
        function closeiframe(){
			window.close();
			
		}
   </Script>
<pdk-html:form 	name="summaryFormBean" 
				type="gov.epa.owm.mtb.cwns.summary.SummaryForm"
				action="viewAllComments.do">

   
	<TABLE height=5 cellSpacing=0 cellPadding=9 width="100%" background="../images/pobtrans.gif" border=0 align="center">
	
	<logic:iterate id="facCommentHelpers" name="summaryFormBean" property="facCommentHelpers">
	     <tr bgcolor="silver">
	         <td class="portletText1">
	            <strong> 
	              <bean:write name="facCommentHelpers" property="dataAreaTypeName"/> Comments - <bean:write name="facCommentHelpers" property="lastUpdateTS"/>
	            </strong>
	         </td>
	     </tr>
	     <tr>
		     <td><bean:write name="facCommentHelpers" property="comments"/></td>
	     </tr>	
	</logic:iterate>
	<tr>
	   <td>
		 <a href="javascript:closeiframe()"></a> <a href="javascript:closeiframe()">Close</a>
	   </td>
	</tr>
	</TABLE>

</pdk-html:form>
