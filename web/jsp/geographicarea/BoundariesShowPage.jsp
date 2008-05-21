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
	import="gov.epa.owm.mtb.cwns.boundary.BoundaryForm" 
	import="gov.epa.owm.mtb.cwns.model.GeographicAreaBoundary"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
	import="oracle.portal.utils.NameValue"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
	.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	String addActionUrl = CWNSStrutsUtils.createHref(request,"addBoundary.do");
	String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteBoundary.do");
    String boundaryParam1 = PortletRendererUtil.portletParameter(prr, "boundaryId");
    String boundaryParam2 = PortletRendererUtil.portletParameter(prr, "geographicAreaId"); 
	BoundaryForm boundaryForm = (BoundaryForm)request.getAttribute("boundaryForm");
%>

<SCRIPT type=text/javascript>

function ShowBoundaryLookUp(popupUrl)
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

function Boundary_ConfirmAndDelete(url){
  /*
  var c = "<%=boundaryForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result != true){
            return false;
      }
  } */   
  window.open(url,'_self',null);
}

function BoundarySubmit(form){
window.document.getElementById('boundaryType').disabled=false;
  window.document.getElementById('boundaryName').disabled=false;
 
   if(!validateBoundaryForm(form)){
     window.document.getElementById('boundaryType').disabled=true;
     window.document.getElementById('boundaryName').disabled=true;
     return false;
   }
  /*
  var c = "<%=boundaryForm.getShowWarningMessage()%>"
   if(c == "Y"){
     var result = confirm("A Feedback version already exists for this facility.\n"+ 
                         "Any changes will not be reflected on the Feedback version. Continue with the update?");
      if (result != true){
         window.document.getElementById('boundaryType').disabled=true;
         window.document.getElementById('boundaryName').disabled=true;
         return false;
      }
  }    
  */
  return true;
}
</SCRIPT>

<TABLE class="PortletText1" cellspacing="1" cellpadding="1" width="100%">
    <TR>
		<TD colspan="6"> </TD>
	</TR>
	<TR>
		<TD colspan="5">
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">
		<TR>
			<TD colspan='5'>
				
			</TD>
		</TR>
		<TR class="PortletHeaderColor">
			<TD class="PortletHeaderText">
				<P align="center">
					Type
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center">
					Special Program Area
				</P>
			</TD>
			<TD class="PortletHeaderText">
				<P align="center">
					Number
				</P>
			</TD>
			<logic:equal name="boundaryForm" property="isUpdatable" value="Y">
            <TD align="center" width="40">
				<FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
			</TD>
            </logic:equal>
            <logic:notEqual name="boundaryForm" property="isUpdatable" value="Y">
              <logic:equal name="isFeedback" value="true">
                <TD align="center" width="40">
				   <FONT color="#ffffff"> <STRONG> Delete </STRONG> </FONT>
			    </TD>
			  </logic:equal>
            </logic:notEqual> 
		</TR>
		<c:set var="i" value="1" />
		<logic:iterate id="geographicAreaBoundary" name="boundaryForm" property="geographicAreaBoundaries" 
				                                 type="GeographicAreaBoundary">
            <c:choose>
		      <c:when test='${i%2=="0"}'>
				<c:set var="class" value="PortletSubHeaderColor" />   
			  </c:when>
			  <c:otherwise>
				<c:set var="class" value="" />
			  </c:otherwise>
		    </c:choose>	
		<c:set var="i" value="${i+1}" />
		<logic:equal name="geographicAreaBoundary" property="boundaryRef.boundaryId" value="<%=boundaryForm.getBoundaryId()%>">				
				    <c:set var="class" value="RowHighlighted" />
		</logic:equal>
		<TR class="<c:out value="${class}"/>">
			<TD>
				<P align="center">
					<bean:write name="geographicAreaBoundary" property="boundaryRef.boundaryTypeRef.name"/>
				</P>
			</TD>
			<TD>
				<P align="center">
					<bean:write name="geographicAreaBoundary" property="boundaryRef.name"/>
				</P>
			</TD>
			<TD>
				<P align="center">
					<bean:write name="geographicAreaBoundary" property="boundaryRef.boundaryId"/>
				</P>
			</TD>
			<logic:equal name="boundaryForm" property="isUpdatable" value="Y">
				<bean:define id="boundaryId"><bean:write name="geographicAreaBoundary" property="boundaryRef.boundaryId"/></bean:define> 
				<bean:define id="geographicAreaId"><bean:write name="geographicAreaBoundary" property="geographicArea.geographicAreaId"/></bean:define>  
					
				<TD align="center">
				      <logic:equal name="isFeedback" value="true">
				         <bean:define id="dUrl">location.href='<%=deleteActionUrl%>&<%=boundaryParam1%>=<%=boundaryId%>&<%=boundaryParam2%>=<%=geographicAreaId%>';</bean:define>
				         <logic:equal name="geographicAreaBoundary" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" onclick="<%=dUrl%>">
				         </logic:equal>
				         <logic:notEqual name="geographicAreaBoundary" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" onclick="<%=dUrl%>">
				         </logic:notEqual>
				     </logic:equal> 
				     <logic:notEqual name="isFeedback" value="true">
					    <A href="javascript:Boundary_ConfirmAndDelete('<%=deleteActionUrl%>&<%=boundaryParam1%>=<%=boundaryId%>&<%=boundaryParam2%>=<%=geographicAreaId%>')" onclick=""><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A>
				     </logic:notEqual>	
					 
				</TD>
			</logic:equal>
			<logic:notEqual name="boundaryForm" property="isUpdatable" value="Y">
				<logic:equal name="isFeedback" value="true">					
				<TD align="center">
				    <logic:equal name="geographicAreaBoundary" property="feedbackDeleteFlag" value="Y">
				        <input type="checkbox" name="delete" checked="checked" disabled="disabled">
				    </logic:equal>
				    <logic:notEqual name="geographicAreaBoundary" property="feedbackDeleteFlag" value="Y">
				        <input type="checkbox" name="delete" disabled="disabled">
				    </logic:notEqual>
				</TD>
				</logic:equal>
			</logic:notEqual>
		</TR>
		</logic:iterate>	
		
</TABLE>
</TD>
	</TR>

	</TABLE>
<logic:equal name="boundaryForm" property="isUpdatable" value="Y">
  <logic:equal name="boundaryForm" property="mode" value="list">
  <DIV class="PortletText1" align="left">
    <A href="<%=addActionUrl%>">Add Special Program Area</A>
  </DIV> 
  </logic:equal>
 
  <logic:present name="boundaryForm" property="mode">
  <logic:notEqual name="boundaryForm" property="mode" value="list">
  <DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
  <pdk-html:form name="boundaryForm" styleId="boundaryFormId"
	           type="gov.epa.owm.mtb.cwns.boundary.BoundaryForm"
	           action="saveBoundary.do" onsubmit="return BoundarySubmit(this);">
	<STRONG><FONT size="2">Add Special Program Area</FONT></STRONG><br><br>
	<html:errors/><br>
	 <logic:equal name="boundaryForm" property="mode" value="add">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="boundaryForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="boundaryForm"  property="facilityId" styleId="facilityId"/>
	  <pdk-html:text name="boundaryForm"  property="locationId" styleId="locationId"/>
	  <pdk-html:text name="boundaryForm"  property="boundaryId" styleId="boundaryId"/>
	 </div>  
	<TABLE border="0" width="800" class="PortletText1">
	<TR>
	   <TD width="20%">
	       <STRONG><span class="required">*</span>&nbsp;&nbsp;Special Program Area</STRONG>
		
	   </TD>
	   <TD>
	       <pdk-html:text name="boundaryForm" property="boundaryType" styleId="boundaryType" size="35" maxlength="35" disabled="true" />
	       &nbsp;
	       <pdk-html:text name="boundaryForm" property="boundaryName" styleId="boundaryName" size="35" maxlength="35" disabled="true" />
	       &nbsp;
	       
	       <%
				NameValue[] linkParams       = new NameValue[1];
				linkParams[0] = new NameValue("locationId", boundaryForm.getLocationId());
				String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(prr,"ViewBoundaryListPage", linkParams,true, true);%>
				<A href="javascript:void(0);" onclick='ShowBoundaryLookUp("<%=eventPopupWinowUrl %>")'">
				   <pdk-html:img page="/images/find.gif" border="0" alt="Search" />  
				</A>
	   </TD>
	</TR>
	
	</TABLE><br>
	</logic:equal>
	<pdk-html:submit value="Save"/>
	<pdk-html:cancel value="Cancel"/> 
	</pdk-html:form>
	<pdk-html:javascript formName="boundaryForm" staticJavascript="false"/>
    </DIV>	
</logic:notEqual>
</logic:present>
</logic:equal>	