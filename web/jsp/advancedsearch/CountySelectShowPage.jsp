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
<%@page import="gov.epa.owm.mtb.cwns.model.CountyRef" %>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		String formName = prr.getParameter("formId")!=null?prr.getParameter("formId"):"";
%>
<SCRIPT type=text/javascript>

function get_county() { 
   if(window.opener && !window.opener.closed) 
   {
     var c = "";
     if (document.countyListForm.county.checked) {
            c = document.countyListForm.county.value;
     }
     for (var i=0; i < document.countyListForm.county.length; i++){
         if (document.countyListForm.county[i].checked) {
            c = document.countyListForm.county[i].value;
         }
     }
     var county = "";
     var fipscode = "";
     if (c != ""){
       fipscode = c.substring(0,5);
       county = c.substring(5,c.length);
     }
        window.opener.document.<%=formName%>.countyName.value=county;
        var f = window.opener.document.getElementById("fipsCodeId");
        if (f!=undefined){
          window.opener.document.getElementById("fipsCodeId").value=fipscode;
        }     
   }
   window.close();
}

</SCRIPT>
<pdk-html:form name="countyListForm" method="post" styleId="countyListFormId" type="org.apache.struts.action.DynaActionForm" action="countyList.do">
     <logic:present name="facilityId">
	 <div style="DISPLAY: none;"> 
	    <pdk-html:text name="countyListForm"  property="facilityId"/>
	 </div> 
	 <% String disablePreferred = (String)request.getAttribute("disablePreferred");%>
	<TABLE cellspacing="0" cellpadding="0" width="100%" style="border-bottom-width:1  ;border-bottom: thin; border-bottom-color: blue">
		<TR>	
			<TD width="10%">&nbsp;&nbsp;
				<pdk-html:radio name="countyListForm" property="filter" value="A" onclick="this.form.submit();">All</pdk-html:radio>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<pdk-html:radio name="countyListForm" property="filter" value="P" onclick="this.form.submit();" disabled="<%=disablePreferred %>">Preferred</pdk-html:radio>
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
					County FIPS/Name
				</TH>
			</TR>
			<logic:present name="counties">
			<logic:iterate id="county" name="counties" type="CountyRef">
				<tr>
					<TD align="center">
					    <input type="radio" name="county" value="<bean:write name="county" property="fipsCode"/><bean:write name="county" property="name"/>">    					    
					</TD>
					<TD align="left">
						<FONT class="PortletText1"><bean:write name="county" property="fipsCode"/>&nbsp;&nbsp;&nbsp;
						   <bean:write name="county" property="name" />
						   <%--<logic:present name="facilityId">
						      <logic:equal name="countyListForm" property="filter" value="P">
						        , <bean:write name="county" property="locationId"/>
						      </logic:equal>
						   </logic:present>--%>
						</FONT>
						 <logic:present name="prefCountyId">
					        <bean:define id="prefCode"><bean:write name="prefCountyId"/></bean:define> 
					        <logic:match name="county" property="fipsCode" value="<%=prefCode%>">
					          &nbsp;*
					        </logic:match>
					    </logic:present>
					</TD>
					<TD></TD>

				</tr>
			</logic:iterate>
			</logic:present>
            <br>
            <logic:present name="prefCountyId">
            <TR>
		       <TD colspan="3" class="PortletText1"><br/>
		  		  * County containing Facility/Project Coordinates<br/><br/>
		       </TD>
		    </TR>
		    </logic:present>
		    <TR>
		       <TD align="center">
		  		<input type="button" name="Select" value="Select" onclick="get_county()"/>
		       </TD>
		    </TR>
			
	</TABLE>
</pdk-html:form>

						   