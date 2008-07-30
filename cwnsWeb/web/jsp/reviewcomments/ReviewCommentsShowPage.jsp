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
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils"%>
<%@ page import="oracle.portal.utils.NameValue"%>
<%@ page import="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListHelper"%>

<%
   PortletRenderRequest pReq = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   ReviewCommentsListForm reviewCommentsListForm = (ReviewCommentsListForm)request.getAttribute("reviewCommentsForm");
   String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
                
                
		String pref = pReq.getRenderContext().getPortletPageReference();
        String helpKey = (String)request.getAttribute("helpKey");        
%>

<SCRIPT type=text/javascript>

// Set the value of the "action" hidden attribute
function reviewCommentsSetActionAndSubmit(status)
{
	 hidden_field = window.document.getElementById("reviewCommentAct");
	 hidden_field.value=status;
	 
	 //alert("reviewCommentAct" + status);
	 
	 // window.document.reviewCommentsListBean.submit();
	 reviewCommentsEnableRadio();
	 return true;
}

function reviewCommentsEnableRadio()
{
     window.document.getElementById("facilityVersionCodeRadioS").disabled = false;
     window.document.getElementById("facilityVersionCodeRadioF").disabled = false;     
}

function submitForm()
{
     reviewCommentsEnableRadio();
	 window.document.reviewCommentsListBean.submit();
}

function submitNextResult(nextRslt)
{
     hidden_field = window.document.getElementById("RCnextResult");
	 hidden_field.value=nextRslt;
	 submitForm();
}
// Show or Hide the add comment area of the Portlet
function showOrHide(id, mode)
{
     window.document.getElementById(id).style.display=mode;
}

function ViewAllPopUp(popupUrl)
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

  var w = window.open(popupUrl, 'ReviewComments', settings);
  if(w != null) 
    w.focus();     
}

</SCRIPT>


<pdk-html:form 	name="reviewCommentsListBean" 
				type="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListForm"
				action="reviewComments.do">
	<TABLE class="PortletText1" cellpadding="0" width="100%" border="0">
    	<TR><TD align="right">
	           <A href="javascript:void(0);" onclick='ViewAllPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>
	           <pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
	           </A>
       </TD></TR>
	</TABLE>
	<logic:present name="feedbackMessage">
	  <TABLE class="PortletText1" cellpadding="1" width="100%" border="1">
	    <TR>
	       <TD>
	         Feedback copy does not exist
	       </TD>  
	    </TR>  
	   </TABLE>
	</logic:present>
	<logic:notPresent name="feedbackMessage">
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="reviewCommentAct" property="reviewCommentAct" value="<%=reviewCommentsListForm.getReviewCommentAct()%>"/>
		<pdk-html:text styleId="reviewCommentsFacilityId" property="reviewCommentsFacilityId" value="<%=reviewCommentsListForm.getReviewCommentsFacilityId()%>"/>
		<pdk-html:text styleId="reviewCommentsFacilityIdF" property="reviewCommentsFacilityIdF" value="<%=reviewCommentsListForm.getReviewCommentsFacilityIdF()%>"/>
		<pdk-html:text styleId="RCnextResult" property="nextResult" value="1"/>		
	</DIV>
	
	
<%
    String showRadio = "none";
    String disableRadio = "false";
    if((reviewCommentsListForm.getUserType()).equals(ReviewCommentsListForm.STATE))
    {
      showRadio = "block";
      
      if(reviewCommentsListForm.getReviewCommentsFacilityIdF() < 0)
      {
        disableRadio = "true";
      }
      
    }
%>
      
     <DIV id="radio_button_div" 
         style="display:<%=showRadio%>">
	<center>
	<FONT class="PortletText1">
			<pdk-html:radio disabled="<%=disableRadio%>" name="reviewCommentsListBean" property="facilityVersionCode" styleId="facilityVersionCodeRadioS" 
						          value="<%=ReviewCommentsListForm.TYPE_SURVEY%>" title="Survey" onclick="submitForm()">Survey</pdk-html:radio>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<pdk-html:radio disabled="<%=disableRadio%>" name="reviewCommentsListBean" property="facilityVersionCode" styleId="facilityVersionCodeRadioF" 
			                      value="<%=ReviewCommentsListForm.TYPE_FACILITY%>" title="Facility" onclick="submitForm()">Feedback</pdk-html:radio>			
    </FONT>
	</center>
	</DIV>
			
   <TABLE class="PortletText1" cellpadding="1" width="100%" border="1">
	
	<TR>
	 
		<TD align="center" width="30%">
			<b>Author/Date</b>
		</TD>
		<TD align="center" width="70%">
			<b>Comment</b>
		</TD>
	</TR>
	<logic:iterate id="revCommHelper" name="reviewCommentsListBean"  property="reviewCommentsHelpers"
				type="gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListHelper">
	<TR>
		<TD width="30%">
			<bean:write name="revCommHelper" property="userName"/>	                                                      
			<br/>
			<bean:write name="revCommHelper" property="updateDate"/>
		</TD>
		<TD width="30%">
		    <SPAN id="shortComment_<bean:write name="revCommHelper" property="reviewCommentId"/>" >
		    <bean:define id="shortCommentsFromBean" name="revCommHelper" property="shortComments"/> 
		    <%=((String)shortCommentsFromBean).replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")%>
	           
		            <logic:notEmpty name="revCommHelper" property="longComments">
			            <A href="javascript:void(0);" 
			               onclick="showOrHide('shortComment_<bean:write name="revCommHelper" property="reviewCommentId"/>', 'none');showOrHide('longComment_<bean:write name="revCommHelper" property="reviewCommentId"/>', 'block');">
			                  More...&gt;
			            </A>         
			        </logic:notEmpty>
	        </SPAN>

            <logic:notEmpty name="revCommHelper" property="longComments">
	            <SPAN id="longComment_<bean:write name="revCommHelper" property="reviewCommentId"/>" style="DISPLAY:none">
		    <bean:define id="longCommentsFromBean" name="revCommHelper" property="longComments"/> 
		    <%=((String)longCommentsFromBean).replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")%>
	                    <A href="javascript:void(0);" 
			               onclick="showOrHide('shortComment_<bean:write name="revCommHelper" property="reviewCommentId"/>', 'block');showOrHide('longComment_<bean:write name="revCommHelper" property="reviewCommentId"/>', 'none');">
			                  &lt;Collapse
			            </A>
	            </SPAN>  
	        </logic:notEmpty>    
		</TD>
	</TR>
	</logic:iterate>

<logic:greaterThan name="reviewCommentsListBean" property="numOfComments" value="0">
	<TR class="PortletText1" >
		<TD align="right" colspan="2" >
		<% 
		String facTypeStr = reviewCommentsListForm.getFacilityVersionCode();
		long tmpFacId = -1;
		
		if(facTypeStr.equals(ReviewCommentsListForm.TYPE_SURVEY))
		{
		   tmpFacId = reviewCommentsListForm.getReviewCommentsFacilityId();
		}
		else if(facTypeStr.equals(ReviewCommentsListForm.TYPE_FACILITY))
		{
		   tmpFacId = reviewCommentsListForm.getReviewCommentsFacilityIdF();
		}

        NameValue[] linkParams       = new NameValue[1];
		linkParams[0] = new NameValue("facilityId", Long.toString(tmpFacId));
		String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(pReq,"FacilityViewAllCommentsEvent", linkParams, true, true);
		
		
		%>
            <A href="javascript:void(0);" onclick="ViewAllPopUp('<%=eventPopupWinowUrl%>')"><b>View All</b></a>
            &nbsp;|&nbsp;
			<!-- Prev -->
			<logic:greaterThan name="reviewCommentsListBean" property="prevReviewCommentsToDisplay" value="0">
				<A href="javascript: submitNextResult(<bean:write name="reviewCommentsListBean" property="prevReviewCommentsToDisplay"/>)">Prev</A>&nbsp;|&nbsp;
			</logic:greaterThan>
			<logic:lessEqual name="reviewCommentsListBean"	property="prevReviewCommentsToDisplay" value="0">
				Prev&nbsp;|&nbsp;
			</logic:lessEqual>

			<!-- x to y of z comments -->
			<bean:write name="reviewCommentsListBean" property="fromReviewComments" />
			-
			<bean:write name="reviewCommentsListBean" property="toReviewComments" />
			of
			<bean:write name="reviewCommentsListBean" property="numOfComments" />
			&nbsp;|&nbsp;
			<!-- Next -->
			
			<logic:lessThan name="reviewCommentsListBean" property="toReviewComments" value="<%=reviewCommentsListForm.getNumOfComments()%>">
				<A href="javascript: submitNextResult(<bean:write name="reviewCommentsListBean" property="nextReviewCommentsToDisplay"/>)">Next</A>&nbsp;
			</logic:lessThan>	
			
			<logic:greaterEqual name="reviewCommentsListBean" property="toReviewComments" value="<%=reviewCommentsListForm.getNumOfComments()%>">
				Next
			</logic:greaterEqual>	
		</TD>
	</TR>
</logic:greaterThan>	
		
<logic:equal name="reviewCommentsListBean" property="isUpdatable" value="Y">
	<TR>
		<TD colspan="2" align="left">
			<A href="javascript:void(0);" onclick="showOrHide('divAddComment', 'block')">Add Comment</A>
		</TD>
	</TR>
</logic:equal>
  </TABLE>
  

<DIV id="divAddComment" style="DISPLAY:none">   

	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0" align="right">
	 	<tr class="PortletText1">
    	<td align="right">
    	    <A href="javascript:void(0);" onclick="showOrHide('divAddComment', 'none')">Hide</A>		
    	</td>
    	</tr>
 	 	<tr class="PortletText1">
	 	<td align="center" width="100%">   	
    		<pdk-html:textarea name="reviewCommentsListBean" styleId="inputComments" property="inputComments" rows="5" cols="30" />
		</td>
		</tr>
	 	<tr class="PortletText1">
    	<td align="center">
    		<pdk-html:submit  onclick="reviewCommentsSetActionAndSubmit('add')" value="Save"/>
    	</td>
    	</tr>
	</TABLE>

</DIV>  
</logic:notPresent>
</pdk-html:form>