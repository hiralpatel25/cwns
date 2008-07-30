<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="gov.epa.owm.mtb.cwns.congressionalDistrict.CongressionalDistrictForm" 
	import="gov.epa.owm.mtb.cwns.congressionalDistrict.ConDistrictHelper"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	  .getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	String addActionUrl = CWNSStrutsUtils.createHref(request,"addConDistrict.do"); 
	String editActionUrl = CWNSStrutsUtils.createHref(request,"editConDistrict.do");
	String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteConDistrict.do");
    String conDistrictParam1 = PortletRendererUtil.portletParameter(prr, "conDistrictId");
    String conDistrictParam2 = PortletRendererUtil.portletParameter(prr, "geographicAreaId"); 
	CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)request.getAttribute("conDistrictForm");
%>
<SCRIPT type=text/javascript>

function ShowConDistrictLookUp(popupUrl)
{
    var w = 600;
    var h = 300;
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

function ConDistrict_ConfirmAndDelete(url){
  /*
  var c = "<%=conDistrictForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result != true){
            return false;
      }
  } */   
  window.open(url,'_self',null);
}

function conDistrictSubmit(form){
 var id = window.document.getElementById('conDistrictId');
 var name = window.document.getElementById('conDistrictName');
 if (id!=undefined)  
  window.document.getElementById('conDistrictId').disabled=false;
 if (name!=undefined) 
  window.document.getElementById('conDistrictName').disabled=false;
  if (id!=undefined && name!=undefined){ 
   if(!validateConDistrictForm(form)){
     window.document.getElementById('conDistrictId').disabled=true;
     window.document.getElementById('conDistrictName').disabled=true;
     return false;
   }
  } 
  /*
  var c = "<%=conDistrictForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result != true){
         window.document.getElementById('conDistrictId').disabled=true;
         window.document.getElementById('conDistrictName').disabled=true;
         return false;
      }
  }    
  */
  //window.document.facilityCountyForm.submit();
  return true;
}
</SCRIPT>


<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
    <TR>
		<TD colspan="6"> </TD>
	</TR>
	<tr>
		<td colspan="5">
			<P>
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">

				<TR>
					<TD colspan="6">
						
					</TD>
				</TR>
				<TR class="PortletHeaderColor">
					<TD class="PortletHeaderText" align="center" width="50">
						Primary
					</TD>
					<TD class="PortletHeaderText" align="center">
						Congressional District
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Code
					</TD>
					<logic:equal name="conDistrictForm" property="isUpdatable" value="Y">
                    <TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Edit </STRONG> </FONT>
					</TD>
					<TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD>
					</logic:equal>
					<logic:notEqual name="conDistrictForm" property="isUpdatable" value="Y">
					<logic:equal name="isFeedback" value="true">
                    <TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD>
					</logic:equal>
					</logic:notEqual>
				</TR>
				<c:set var="i" value="1" />
				<logic:iterate id="conDistrictHelper" name="conDistrictForm" property="conDistrictHelpers" 
				                                 type="ConDistrictHelper">
                <c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>	
				<c:set var="i" value="${i+1}" />
				<logic:equal name="conDistrictHelper" property="conDistrictId" value="<%=conDistrictForm.getConDistrictId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		        </logic:equal>	
				<TR class="<c:out value="${class}"/>">
					<TD align="center">
						<logic:equal name="conDistrictHelper" property="primary" value="Y">
						   <pdk-html:img page="/images/checkmark.gif" />
						</logic:equal>
					</TD>
					<TD align="center">
						<bean:write name="conDistrictHelper" property="conDistrictName"/>
					</TD>
					<TD align="center">
						<bean:write name="conDistrictHelper" property="conDistrictId"/>
					</TD>
					<logic:equal name="conDistrictForm" property="isUpdatable" value="Y">
					<bean:define id="conDistrictId"><bean:write name="conDistrictHelper" property="conDistrictId"/></bean:define> 
					<bean:define id="geographicAreaId"><bean:write name="conDistrictHelper" property="geographicAreaId"/></bean:define>  
					<TD align="center">
					    <A href="<%=editActionUrl%>&<%=conDistrictParam1%>=<%=conDistrictId%>&<%=conDistrictParam2%>=<%=geographicAreaId%>"><pdk-html:img page="/images/edit.gif" alt="edit" border="0"/></A>
					</TD>
					<TD align="center">
					 <logic:equal name="isFeedback" value="true">
				         <bean:define id="dUrl">location.href='<%=deleteActionUrl%>&<%=conDistrictParam1%>=<%=conDistrictId%>&<%=conDistrictParam2%>=<%=geographicAreaId%>';</bean:define>
				         <logic:equal name="conDistrictHelper" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" onclick="<%=dUrl%>">
				         </logic:equal>
				         <logic:notEqual name="conDistrictHelper" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" onclick="<%=dUrl%>">
				         </logic:notEqual>
				     </logic:equal> 
				     <logic:notEqual name="isFeedback" value="true">
					    <A href="javascript:ConDistrict_ConfirmAndDelete('<%=deleteActionUrl%>&<%=conDistrictParam1%>=<%=conDistrictId%>&<%=conDistrictParam2%>=<%=geographicAreaId%>')" onclick=""><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A>
				     </logic:notEqual>					
					    
					</TD>
					</logic:equal>
					<logic:notEqual name="conDistrictForm" property="isUpdatable" value="Y">
					<logic:equal name="isFeedback" value="true">
					<TD align="center">
					   <logic:equal name="conDistrictHelper" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" disabled="disabled">
				       </logic:equal>
				       <logic:notEqual name="conDistrictHelper" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" disabled="disabled">
				       </logic:notEqual>
				   	</TD>
					</logic:equal>
					</logic:notEqual>
				</TR>
				</logic:iterate>
                       
			</table>

		</td>
	</tr>
</TABLE>	
<logic:equal name="conDistrictForm" property="isUpdatable" value="Y">
 <logic:equal name="conDistrictForm" property="mode" value="list">
  <DIV class="PortletText1" align="left">
    <A href="<%=addActionUrl%>">Add Congressional District</A>
  </DIV> 
 </logic:equal>
 <logic:present name="conDistrictForm" property="mode">
<logic:notEqual name="conDistrictForm" property="mode" value="list">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="conDistrictForm" styleId="conDistrictFormId"
	           type="gov.epa.owm.mtb.cwns.congressionalDistrict.CongressionalDistrictForm"
	           action="saveConDistrict.do" onsubmit="return conDistrictSubmit(this);">
	<STRONG><FONT size="2">Add/Edit Congressional District</FONT></STRONG><br><br>
	<html:errors/><br>
	 <logic:equal name="conDistrictForm" property="mode" value="add">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="conDistrictForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="conDistrictForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="conDistrictForm"  property="locationId" styleId="locationId"/>
	 </div>  
	<TABLE border="0" width="500" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;Congressional District</STRONG>
		
	   </TD>
	   <TD>
	       <pdk-html:text name="conDistrictForm" property="conDistrictId" styleId="conDistrictId" size="5" maxlength="5" disabled="true" />
	       &nbsp;&nbsp;&nbsp;
	       <pdk-html:text name="conDistrictForm" property="conDistrictName" styleId="conDistrictName" size="25" maxlength="25" disabled="true" />
	       &nbsp;
	       <%
				NameValue[] linkParams       = new NameValue[2];
				linkParams[0] = new NameValue("locationId", conDistrictForm.getLocationId());
				linkParams[1] = new NameValue("facilityId", conDistrictForm.getFacilityId().toString());
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ConDistrictListPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowConDistrictLookUp("<%=eventPopupWinowUrl %>")'>
				   <pdk-html:img page="/images/find.gif" border="0" alt="Search" />  
				</A>
	   </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="conDistrictForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
	<logic:equal name="conDistrictForm" property="mode" value="edit">
	  <div style="DISPLAY: none;"> 
	  <pdk-html:text name="conDistrictForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="conDistrictForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="conDistrictForm"  property="geographicAreaId" styleId="geographicAreaId"/>
	  <pdk-html:text name="conDistrictForm"  property="conDistrictId" styleId="conDistrictId"/>
	 </div>  
	<TABLE border="0" width="400" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG>Congressional District</STRONG>
		
	   </TD>
	   <TD>
	       <bean:write name="conDistrictForm" property="conDistrictId"/>
	       &nbsp;&nbsp;&nbsp;
	       <bean:write name="conDistrictForm" property="conDistrictName"/>
	    </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="conDistrictForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
        <pdk-html:submit value="Save"/>
		<pdk-html:cancel value="Cancel"/> 
	</pdk-html:form>
	<pdk-html:javascript formName="conDistrictForm" staticJavascript="false"/>
</DIV>
</logic:notEqual>
</logic:present>	
</logic:equal>