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
	import="gov.epa.owm.mtb.cwns.watershed.WatershedForm" 
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"
	import="gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	String addActionUrl = CWNSStrutsUtils.createHref(request,"addWatershed.do");
	String editActionUrl = CWNSStrutsUtils.createHref(request,"editWatershed.do");
	String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteWatershed.do");
    String watershedParam1 = PortletRendererUtil.portletParameter(prr, "watershedId");
    String watershedParam2 = PortletRendererUtil.portletParameter(prr, "geographicAreaId"); 
	WatershedForm watershedForm = (WatershedForm)request.getAttribute("watershedForm");
%>
<SCRIPT type=text/javascript>

function ShowWatershedLookUp(popupUrl)
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

function Watershed_ConfirmAndDelete(url){  
  window.open(url,'_self',null);
}

function WatershedSubmit(form){
  var id = window.document.getElementById('watershedId');
  if (id!=undefined)
    window.document.getElementById('watershedId').disabled=false;
  var name = window.document.getElementById('watershedName');
  if (name!=undefined)  
    window.document.getElementById('watershedName').disabled=false;
  if (id!=undefined && name!=undefined){
   if(!validateWatershedForm(form)){
     window.document.getElementById('watershedId').disabled=true;
     window.document.getElementById('watershedName').disabled=true;
     return false;
   }
  } 
  /*
  var c = "<%=watershedForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result != true){
         window.document.getElementById('watershedId').disabled=true;
         window.document.getElementById('watershedName').disabled=true;
         return false;
      }
  }  */  
  
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
						Watershed
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Number
					</TD>
					<logic:equal name="watershedForm" property="isUpdatable" value="Y">
                    <TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Edit </STRONG> </FONT>
					</TD>
					<TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD>
					</logic:equal>
					<logic:notEqual name="watershedForm" property="isUpdatable" value="Y">
					<logic:equal name="isFeedback" value="true">
                    <TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD>
					</logic:equal>
					</logic:notEqual>
				</TR>
				<c:set var="i" value="1" />
				<logic:iterate id="geographicAreaWatershed" name="watershedForm" property="geographicAreaWatersheds" 
				                                 type="GeographicAreaWatershed">
                <c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>	
				<c:set var="i" value="${i+1}" />
				<logic:equal name="geographicAreaWatershed" property="watershedRef.watershedId" value="<%=watershedForm.getWatershedId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		        </logic:equal>	
				<TR class="<c:out value="${class}"/>">
					<TD align="center">
						<logic:equal name="geographicAreaWatershed" property="primaryFlag" value="Y">
						   <pdk-html:img page="/images/checkmark.gif" />
						</logic:equal>
					</TD>
					<TD align="center">
						<bean:write name="geographicAreaWatershed" property="watershedRef.name"/>
					</TD>
					<TD align="center">
						<bean:write name="geographicAreaWatershed" property="watershedRef.watershedId"/>
					</TD>
					<logic:equal name="watershedForm" property="isUpdatable" value="Y">
					<bean:define id="watershedId"><bean:write name="geographicAreaWatershed" property="watershedRef.watershedId"/></bean:define> 
					<bean:define id="geographicAreaId"><bean:write name="geographicAreaWatershed" property="geographicArea.geographicAreaId"/></bean:define>  
					<TD align="center">
					    <A href="<%=editActionUrl%>&<%=watershedParam1%>=<%=watershedId%>&<%=watershedParam2%>=<%=geographicAreaId%>"><pdk-html:img page="/images/edit.gif" alt="edit" border="0"/></A>
					</TD>
					<TD align="center">
                      <logic:equal name="isFeedback" value="true">
				         <bean:define id="dUrl">location.href='<%=deleteActionUrl%>&<%=watershedParam1%>=<%=watershedId%>&<%=watershedParam2%>=<%=geographicAreaId%>';</bean:define>
				         <logic:equal name="geographicAreaWatershed" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" onclick="<%=dUrl%>">
				         </logic:equal>
				         <logic:notEqual name="geographicAreaWatershed" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" onclick="<%=dUrl%>">
				         </logic:notEqual>
				     </logic:equal> 
				     <logic:notEqual name="isFeedback" value="true">
					    <A href="javascript:Watershed_ConfirmAndDelete('<%=deleteActionUrl%>&<%=watershedParam1%>=<%=watershedId%>&<%=watershedParam2%>=<%=geographicAreaId%>')" onclick=""><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A>
				     </logic:notEqual>					
					</TD>
					</logic:equal>
					
					<logic:notEqual name="watershedForm" property="isUpdatable" value="Y">
					<logic:equal name="isFeedback" value="true">
					<TD align="center">
                       <logic:equal name="geographicAreaWatershed" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" disabled="disabled">
				       </logic:equal>
				       <logic:notEqual name="geographicAreaWatershed" property="feedbackDeleteFlag" value="Y">
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
<logic:equal name="watershedForm" property="isUpdatable" value="Y">
  <logic:equal name="watershedForm" property="mode" value="list">
  <DIV class="PortletText1" align="left">
    <A href="<%=addActionUrl%>">Add Watershed</A>
  </DIV> 
 </logic:equal>
 
 <logic:present name="watershedForm" property="mode">
<logic:notEqual name="watershedForm" property="mode" value="list">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="watershedForm" styleId="watershedFormId"
	           type="gov.epa.owm.mtb.cwns.watershed.WatershedForm"
	           action="saveWatershed.do" onsubmit="return WatershedSubmit(this);">
	<STRONG><FONT size="2">Add/Edit Watershed</FONT></STRONG><br><br>
	<html:errors/><br>
	 <logic:equal name="watershedForm" property="mode" value="add">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="watershedForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="watershedForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="watershedForm"  property="locationId" styleId="locationId"/>
	 </div>  
	<TABLE border="0" width="500" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;Watershed</STRONG>
		
	   </TD>
	   <TD>
	       <pdk-html:text name="watershedForm" property="watershedId" styleId="watershedId" size="14" maxlength="14" disabled="true" value="<%=watershedForm.getWatershedId() %>"/>
	       &nbsp;&nbsp;&nbsp;
	       <pdk-html:text name="watershedForm" property="watershedName" styleId="watershedName" size="35" maxlength="35" disabled="true" />
	       &nbsp;
	       
	       <%
				NameValue[] linkParams       = new NameValue[2];
				linkParams[0] = new NameValue("locationId", watershedForm.getLocationId());
				linkParams[1] = new NameValue("facilityId", watershedForm.getFacilityId().toString());
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewWatershedListPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowWatershedLookUp("<%=eventPopupWinowUrl %>")'">
				   <pdk-html:img page="/images/find.gif" border="0" alt="Search" />  
				</A>
	   </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="watershedForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
	<logic:equal name="watershedForm" property="mode" value="edit">
	  <div style="DISPLAY: none;"> 
	  <pdk-html:text name="watershedForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="watershedForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="watershedForm"  property="geographicAreaId" styleId="geographicAreaId"/>
	  <pdk-html:text name="watershedForm"  property="watershedId" styleId="watershedId"/>
	 </div>  
	<TABLE border="0" width="400" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG>Watershed</STRONG>
		
	   </TD>
	   <TD>
	       <bean:write name="watershedForm" property="watershedId"/>
	       &nbsp;&nbsp;&nbsp;
	       <bean:write name="watershedForm" property="watershedName"/>
	    </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="watershedForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
       <pdk-html:submit value="Save"/>
	   <pdk-html:cancel value="Cancel"/> 
	</pdk-html:form>
	<pdk-html:javascript formName="watershedForm" staticJavascript="false"/>
</DIV>
</logic:notEqual>
</logic:present>	
</logic:equal>	
