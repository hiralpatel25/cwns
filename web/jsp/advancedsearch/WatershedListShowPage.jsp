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
<%@ page import="gov.epa.owm.mtb.cwns.model.WatershedRef"%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
%>
<SCRIPT type=text/javascript>

function get_watershed_name() { 
   if(window.opener && !window.opener.closed) 
   { 
     var c = "";
     if (document.watershedListForm.watershedname.checked) {
           c = document.watershedListForm.watershedname.value;
     }
     for (var i=0; i < document.watershedListForm.watershedname.length; i++){
         if (document.watershedListForm.watershedname[i].checked) {
           c = document.watershedListForm.watershedname[i].value;
         }
     }
     var watershedName = "";
     var watershedId = "";
     if (c != ""){
       var tempArray = c.split('+');
       watershedId = tempArray[0];
       watershedName = tempArray[1];
     }    

     window.opener.document.getElementById("watershedName").value=watershedName;
     var f = window.opener.document.getElementById("watershedId");
     if (f!=undefined){
        window.opener.document.getElementById("watershedId").value=watershedId;
     }    
     
     f = window.opener.document.getElementById("ImpairedWatersSearchTextLabel");
     if (f!=undefined){
        window.opener.document.getElementById("ImpairedWatersSearchTextLabel").innerHTML='HUC-8';
     }        
     
   }
   window.close();
}

</SCRIPT>
<pdk-html:form name="watershedListForm" method="post" styleId="watershedListFormId" type="org.apache.struts.action.DynaActionForm" action="watershedList.do">
     <logic:present name="facilityId">
	 <div style="DISPLAY: none;"> 
	    <pdk-html:text name="watershedListForm"  property="facilityId"/>
	 </div>
	 <% String disablePreferred = (String)request.getAttribute("disablePreferred");%> 
	<TABLE cellspacing="0" cellpadding="0" width="100%" style="border-bottom-width:1  ;border-bottom: thin; border-bottom-color: blue">
		<TR>	
			<TD width="10%">&nbsp;&nbsp;
				<pdk-html:radio name="watershedListForm" property="filter" value="A" onclick="this.form.submit();">All</pdk-html:radio>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<pdk-html:radio name="watershedListForm" property="filter" value="P" onclick="this.form.submit();" disabled="<%=disablePreferred %>">Preferred</pdk-html:radio>
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
					Watershed Name
				</TH>
			</TR>
			<logic:present name="watersheds">
			<logic:iterate id="watershed" name="watersheds" type="WatershedRef">
				<tr>
					<TD align="center">
					    <input type="radio" name="watershedname" value="<bean:write name="watershed" property="watershedId"/>+<bean:write name="watershed" property="name" />"/>
					</TD>

					<TD align="left">
						<FONT class="PortletText1">
						<bean:write name="watershed" property="watershedId" />&nbsp;&nbsp;&nbsp;
						<bean:write name="watershed" property="name" /></FONT>
						 <logic:present name="prefWatershedId">
					        <bean:define id="prefId"><bean:write name="prefWatershedId"/></bean:define>
					        <logic:match name="watershed" property="watershedId" value="<%=prefId%>">
					          &nbsp;*
					        </logic:match>
					    </logic:present>
					</TD>
					<TD></TD>
				</tr>
			</logic:iterate>
			</logic:present>
			<logic:present name="prefWatershedId">
            <TR>
		       <TD colspan="3" class="PortletText1">
		  		  <br/>* Watershed containing Facility/Project Coordinates<br/><br/>
		       </TD>
		    </TR>
		    </logic:present>
		    <TR>
		       <TD colspan="3" >
		  		   <input type="button" name="Select" value="Select" onclick="get_watershed_name()"/>
		       </TD>
		    </TR>
			
	</TABLE>
</pdk-html:form>