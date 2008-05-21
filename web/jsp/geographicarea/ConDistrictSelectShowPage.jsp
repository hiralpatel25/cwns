<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page language="java" session="false"%>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@page import="gov.epa.owm.mtb.cwns.congressionalDistrict.ConDistrictDisplayHelper" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<SCRIPT type=text/javascript>

function get_conDistrict(){ 
   if(window.opener && !window.opener.closed) 
   {
     var c = "";
     if (document.conDistListForm.conDistrict.checked) {
            c = document.conDistListForm.conDistrict.value;
     }
     for (var i=0; i < document.conDistListForm.conDistrict.length; i++){
         if (document.conDistListForm.conDistrict[i].checked) {
            c = document.conDistListForm.conDistrict[i].value;
         }
     }
     var conDistrictName = "";
     var conDistrictId = "";
     if (c != ""){
       //alert('['+c+']');
       conDistrictId = c.substring(0,4);
       conDistrictName = c.substring(5,c.length);
     }    
     window.opener.document.getElementById("conDistrictName").value=conDistrictName;
     window.opener.document.getElementById("conDistrictId").value=conDistrictId;
    
   }
   window.close();
}

</SCRIPT>
<pdk-html:form name="conDistListForm" method="post" styleId="conDistListFormId" type="org.apache.struts.action.DynaActionForm" action="/conDistrictList.do">
     <logic:present name="facilityId">
	 <div style="DISPLAY: none;"> 
	    <pdk-html:text name="conDistListForm"  property="facilityId"/>
	 </div>
	 <% String disablePreferred = (String)request.getAttribute("disablePreferred");%> 
	<TABLE cellspacing="0" cellpadding="0" width="100%" style="border-bottom-width:1  ;border-bottom: thin; border-bottom-color: blue">
		<TR>	
			<TD width="10%">&nbsp;&nbsp;
				<pdk-html:radio name="conDistListForm" property="filter" value="A" onclick="this.form.submit();">All</pdk-html:radio>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<pdk-html:radio name="conDistListForm" property="filter" value="P" onclick="this.form.submit();" disabled="<%=disablePreferred %>">Preferred</pdk-html:radio>
		    </TD>
		</TR>
     </TABLE><br> 
	</logic:present>

	<TABLE cellspacing="0" cellpadding="0" width="100%" border="0">
		<TBODY>
			<TR class="PortletSubHeaderColor">
				<TH class="PortletSubHeaderText" align="center" width="10%">
					Select
				</TH>
				
				<TH class="PortletSubHeaderText" align="left">
					Congressional District Name
				</TH>
			</TR>
			<logic:present name="conDistricts">
			<logic:iterate id="conDistrict" name="conDistricts" type="ConDistrictDisplayHelper">
				<tr>
					<TD align="center">
					   <input type="radio" name="conDistrict" value="<bean:write name="conDistrict" property="congressionalDistrictId"/>+<bean:write name="conDistrict" property="name"/>"/>
					</TD>

					<TD align="left">
						<FONT class="PortletText1">
						 <bean:write name="conDistrict" property="name"/>
						 <%--<logic:present name="facilityId">
						      <logic:equal name="conDistListForm" property="filter" value="P">
						        , <bean:write name="conDistrict" property="locationId"/>
						      </logic:equal>
						  </logic:present> --%>
						  <logic:present name="prefCongressionDistrictId">
					        <bean:define id="prefId"><bean:write name="prefCongressionDistrictId"/></bean:define>
					        <logic:equal name="conDistrict" property="congressionalDistrictId" value="<%=prefId%>">
					          &nbsp;*
					        </logic:equal>
					    </logic:present>
					</TD>
					<TD></TD>

				</tr>
			</logic:iterate>
			</logic:present>
			<logic:present name="prefCongressionDistrictId">
            <TR>
		       <TD colspan="3" class="PortletText1">
		  		  <br>* Congressional District containing Facility/Project Coordinates<br/><br/>
		       </TD>
		    </TR>
		    </logic:present>            
		    <TR>
		       <TD align="center">
		  		<input type="button" name="Select" value="Select" onclick="get_conDistrict()"/>
		       </TD>
		    </TR>
			
	</TABLE>

</pdk-html:form>
