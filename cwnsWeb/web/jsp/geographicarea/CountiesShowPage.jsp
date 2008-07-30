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
	import="gov.epa.owm.mtb.cwns.county.FacilityCountyHelper"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"
	import="gov.epa.owm.mtb.cwns.county.FacilityCountyForm"
	import="gov.epa.owm.mtb.cwns.model.GeographicAreaCounty"%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
			String addActionUrl = CWNSStrutsUtils.createHref(request,"addFacilityCounty.do");
			String editActionUrl = CWNSStrutsUtils.createHref(request,"editFacilityCounty.do");
			String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteFacilityCounty.do");
    String facilityCountyParam1 = PortletRendererUtil.portletParameter(prr, "countyId");
    String facilityCountyParam2 = PortletRendererUtil.portletParameter(prr, "geographicAreaId");
    FacilityCountyForm facilityCountyForm = (FacilityCountyForm)request.getAttribute("facilityCountyForm");
%>
<SCRIPT type=text/javascript>

function DisplayAdd(){
window.document.getElementById('addCountyDiv').style.display='block';
window.document.getElementById('add_button_div').style.display='none';
}

function ShowCountyLookUp(popupUrl)
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

function County_ConfirmAndDelete(url){   
  window.open(url,'_self',null);
}

function countySubmit(form){
  var f = window.document.getElementById('fipsCodeId');
  if (f!=undefined)
     window.document.getElementById('fipsCodeId').disabled=false;
  var c = window.document.facilityCountyForm.countyName;   
  if (c!=undefined)   
     window.document.facilityCountyForm.countyName.disabled=false;
   if (f!=undefined && c!=undefined){
   if(!validateFacilityCountyForm(form)){
     window.document.getElementById('fipsCodeId').disabled=true;
     window.document.facilityCountyForm.countyName.disabled=true;
     return false;
   }
   }
  return true;
}
</SCRIPT>

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
	<TR>
		<TD>
			<FONT size="4"> <STRONG> Areas Related To Needs</STRONG> </FONT>
		</TD>
	</TR>
	
	<TR>
		<TD colspan="5">
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
						County
					</TD>
					<TD class="PortletHeaderText" align="center" width="100">
						Code
					</TD>
						
                    <logic:equal name="facilityCountyForm" property="isUpdatable" value="Y">
                     <TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Edit </STRONG> </FONT>
					</TD>
					<TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD>   
                    </logic:equal>
                    <logic:notEqual name="facilityCountyForm" property="isUpdatable" value="Y">
                    <logic:equal name="isFeedback" value="true">
					<TD align="center" width="40">
						<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
					</TD> 
					</logic:equal>  
                    </logic:notEqual>
				</TR>
				<c:set var="i" value="1" />
				<logic:iterate id="geographicAreaCounty" name="facilityCountyForm" property="geographicAreaCounties" 
				                                 type="GeographicAreaCounty">
                <c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>	
				<c:set var="i" value="${i+1}" />
				<logic:equal name="geographicAreaCounty" property="countyRef.countyId" value="<%=facilityCountyForm.getCountyId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		        </logic:equal>			                                 
				<TR class="<c:out value="${class}"/>">
					<TD align="center">
					    <logic:equal name="geographicAreaCounty" property="primaryFlag" value="Y">
						   <pdk-html:img page="/images/checkmark.gif" />
						</logic:equal>
					</TD>
					<TD align="center">
						<bean:write name="geographicAreaCounty" property="countyRef.name"/>
					</TD>
					<TD align="center">
						<bean:write name="geographicAreaCounty" property="countyRef.fipsCode"/>
					</TD>
					<logic:equal name="facilityCountyForm" property="isUpdatable" value="Y">
					<bean:define id="countyId"><bean:write name="geographicAreaCounty" property="countyRef.countyId"/></bean:define> 
					<bean:define id="geographicAreaId"><bean:write name="geographicAreaCounty" property="geographicArea.geographicAreaId"/></bean:define>  
					<TD align="center">
					    <A href="<%=editActionUrl%>&<%=facilityCountyParam1%>=<%=countyId%>&<%=facilityCountyParam2%>=<%=geographicAreaId%>"><pdk-html:img page="/images/edit.gif" alt="edit" border="0"/></A>
					</TD>
					<TD align="center">
					 <logic:equal name="isFeedback" value="true">
				         <bean:define id="dUrl">location.href='<%=deleteActionUrl%>&<%=facilityCountyParam1%>=<%=countyId%>&<%=facilityCountyParam2%>=<%=geographicAreaId%>';</bean:define>
				         <logic:equal name="geographicAreaCounty" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" onclick="<%=dUrl%>">
				         </logic:equal>
				         <logic:notEqual name="geographicAreaCounty" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" onclick="<%=dUrl%>">
				         </logic:notEqual>
				     </logic:equal> 
				     <logic:notEqual name="isFeedback" value="true">
					    <A href="javascript:County_ConfirmAndDelete('<%=deleteActionUrl%>&<%=facilityCountyParam1%>=<%=countyId%>&<%=facilityCountyParam2%>=<%=geographicAreaId%>')" onclick=""><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A>
				     </logic:notEqual>					    
					</TD>
					</logic:equal>
					<logic:notEqual name="facilityCountyForm" property="isUpdatable" value="Y">
					 <logic:equal name="isFeedback" value="true">
					 <TD align="center">
					     <logic:equal name="geographicAreaCounty" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" disabled="disabled">
				         </logic:equal>
				         <logic:notEqual name="geographicAreaCounty" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" disabled="disabled">
				         </logic:notEqual>
				     </TD>
					 </logic:equal>
					</logic:notEqual>
				</TR>
				</logic:iterate>
								
			</table>
		</TD>
	</TR>

	</TABLE>
<logic:equal name="facilityCountyForm" property="isUpdatable" value="Y">
 <logic:equal name="facilityCountyForm" property="mode" value="list">
  <DIV class="PortletText1" align="left">
    <A href="<%=addActionUrl%>">Add County</A>
  </DIV> 
 </logic:equal>
<logic:present name="facilityCountyForm" property="mode">
<logic:notEqual name="facilityCountyForm" property="mode" value="list">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="facilityCountyForm" styleId="facilityCountyFormId"
	           type="gov.epa.owm.mtb.cwns.county.FacilityCountyForm"
	           action="saveFacilityCounty.do" onsubmit="return countySubmit(this);">
	<STRONG><FONT size="2">Add/Edit County</FONT></STRONG><br><br>
	<html:errors/><br>
	 <logic:equal name="facilityCountyForm" property="mode" value="add">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="facilityCountyForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="facilityCountyForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="facilityCountyForm"  property="locationId" styleId="locationId"/>
	 </div>  
	<TABLE border="0" width="400" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;County</STRONG>
		
	   </TD>
	   <TD>
	       <pdk-html:text name="facilityCountyForm" property="fipsCode" styleId="fipsCodeId" size="5" maxlength="5" disabled="true" />
	       &nbsp;&nbsp;&nbsp;
	       <pdk-html:text name="facilityCountyForm" property="countyName" styleId="countyName" size="25" maxlength="25" disabled="true" />
	       &nbsp;
	       <%
				NameValue[] linkParams       = new NameValue[3];
				linkParams[0] = new NameValue("locationId", facilityCountyForm.getLocationId());
				linkParams[1] = new NameValue("formId", "facilityCountyForm");
				linkParams[2] = new NameValue("facilityId", facilityCountyForm.getFacilityId().toString());
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewCountyListPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowCountyLookUp("<%=eventPopupWinowUrl %>")'">
				   <pdk-html:img page="/images/find.gif" border="0" alt="Search" />  
				</A>
	   </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="facilityCountyForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
	<logic:equal name="facilityCountyForm" property="mode" value="edit">
	  <div style="DISPLAY: none;"> 
	  <pdk-html:text name="facilityCountyForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="facilityCountyForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="facilityCountyForm"  property="geographicAreaId" styleId="geographicAreaId"/>
	  <pdk-html:text name="facilityCountyForm"  property="countyId" styleId="countyId"/>
	 </div>  
	<TABLE border="0" width="400" class="PortletText1">
	<TR>
	   <TD>
	       <STRONG>County</STRONG>
		
	   </TD>
	   <TD>
	       <bean:write name="facilityCountyForm" property="fipsCode"/>
	       &nbsp;&nbsp;&nbsp;
	       <bean:write name="facilityCountyForm" property="countyName"/>
	    </TD>
	</TR>
	<TR>
	    <TD>
	       <pdk-html:checkbox name="facilityCountyForm" property="primary" value="Y"/>&nbsp;Primary
	    </TD>
	</TR>
	</TABLE><br>
	</logic:equal>
		<pdk-html:submit value="Save"/>
		<pdk-html:cancel value="Cancel"/> 
	</pdk-html:form>
	<pdk-html:javascript formName="facilityCountyForm" staticJavascript="false"/>
</DIV>
</logic:notEqual>
</logic:present>
</logic:equal>	