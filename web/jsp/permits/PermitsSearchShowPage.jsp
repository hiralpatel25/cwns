<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="oracle.portal.utils.NameValue"
	%>
<%@ page import="gov.epa.owm.mtb.cwns.permits.PermitsSearchForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.permits.PermitsSearchAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.permits.PermitsSearchListHelper" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.Entity"%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    NameValue[] linkParams = new NameValue[2];

	PermitsSearchForm permitsSearchForm = (PermitsSearchForm)request.getAttribute("permitsSearchForm");
			
	String permitsSearchUrl = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();
    String helpKey = (String)request.getAttribute("helpKey");
	
%>

<SCRIPT type=text/javascript>
function  submitPermitsSearchForm(action) {

    if (action == '<%=PermitsSearchAction.ACTION_ASSOCIATE_WITH_FACILITY%>') {
	  
      	var form=window.document.getElementById("permitsSearchForm");
      	var permits = form["selectedPermits"];
      	var found = false;
        if (permits.checked == true){
          found = true;
        }
      	for (i = 0; i < permits.length; i++){
         	if (permits[i].checked == true){
         		found = true;
        	}
        }
        
        if (!found) {
        	alert('You must select a Permit to associate with this Facility');
        	return;
        }
	}

	window.document.getElementById('permitsSearchAction').value    = action;
	window.document.permitsSearchForm.submit();
	
}


// Open help window
function showPermitsSearchHelp(popupUrl)
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

  var w = window.open(popupUrl, 'PermitsSearchHelp', settings);
  if(w != null) 
    w.focus();     
}

</SCRIPT>


<pdk-html:form method="post" 
	name="permitsSearchForm" 
	type="gov.epa.owm.mtb.cwns.permits.PermitsSearchForm" 
	styleId="permitsSearchForm"
	action="permitsSearch.do">
	
	<pdk-html:hidden styleId="permitsSearchAction" name="permitsSearchForm" property="action"/>
	<pdk-html:hidden name="permitsSearchForm" property="searchType"/>
	<pdk-html:hidden name="permitsSearchForm" property="startIndex"/>
	<pdk-html:hidden name="permitsSearchForm" property="endIndex"/>
	<pdk-html:hidden name="permitsSearchForm" property="listSize"/>
    
	<DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='showPermitsSearchHelp("<%=permitsSearchUrl%><bean:message key="<%=helpKey%>"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
	    </FONT>
	</DIV> 
<TABLE cellspacing="2" cellpadding="3" border="0" width="100%" class="PortletText1">
	
  	<TR>
		<TD class="PortletText1">
			<strong>Keyword:</strong>
		</TD>											
		<td>
			<pdk-html:text name="permitsSearchForm" property="keyword" size="20" maxlength="30" />
			<A href="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_KEYWORD_SEARCH%>')"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>
		</td>																
	</TR>
	<logic:lessEqual name="permitsSearchForm" property="listSize" value="0">
	<TR><TD>&nbsp;</TD></TR>
			
	<TR class="PortletText1">
		<TD align="center" colspan="9">
			<b><strong>No records found.</strong></b>
		</TD>
	</TR>
	</logic:lessEqual>					
	<logic:greaterThan name="permitsSearchForm" property="listSize" value="0">
	<TR bgcolor="gray">
	    <logic:equal name="permitsSearchForm" property="isUpdatable" value="Y">
		<TD align="center">
			<STRONG>Select</STRONG>
		</TD>
		
		<TD>
			<Strong>Info</strong>
		</TD>
		</logic:equal>
		<logic:notEqual name="permitsSearchForm" property="isUpdatable" value="Y">
		<TD colspan="2">
			<Strong>Info</strong>
		</TD>
		</logic:notEqual>
		
	</TR>
	
	<logic:iterate id="permit" name="permitsList" type="PermitsSearchListHelper">
		<tr>
		   <logic:equal name="permitsSearchForm" property="isUpdatable" value="Y">
			<TD align="center" valign="top">
			    <pdk-html:multibox name="permitsSearchForm" property="selectedPermits" styleId="selectedPermits">
			       <bean:write name="permit" property="permitNumber"/>
			    </pdk-html:multibox> 
			</TD>
                      
			<TD>
				<logic:notEqual name="permit" property="permitNumber" value="">
				<%
 					linkParams[0] = new NameValue("permitNbr", permit.getPermitNumber());
 					linkParams[1] = new NameValue("facilityId", permitsSearchForm.getFacilityId().toString());
 					String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(pRequest,"ViewPermitDetailsPage", linkParams,true, true);
 				 %>
 				 <A href="javascript:void(0);" onclick='ShowPermitDetails("<%=eventPopupWinowUrl%>")' title="Envirofacts">
					<bean:write name="permit" property="permitNumber" />
				 </A>	
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="permitType" value="">
					Permit Type:<bean:write name="permit" property="permitType" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="facilityName" value="">
					Facility Name:<bean:write name="permit" property="facilityName" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="countyName" value="">
					County:<bean:write name="permit" property="countyName" />
					<BR>
				</logic:notEqual>
				<logic:equal name="permit" property="assWithAnotherFac" value="Y">
				   <pdk-html:img page="/images/checkmark.gif" />
				   Associated With Other Facility(s)
				</logic:equal>	
			</TD>
			</logic:equal>
			<logic:notEqual name="permitsSearchForm" property="isUpdatable" value="Y">
			<TD colspan="2">
			    <logic:notEqual name="permit" property="permitNumber" value="">
				<%
 					linkParams[0] = new NameValue("permitNbr", permit.getPermitNumber());
 					linkParams[1] = new NameValue("facilityId", permitsSearchForm.getFacilityId().toString());
 					String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(pRequest,"ViewPermitDetailsPage", linkParams,true, true);
 				 %>
 				 <A href="javascript:void(0);" onclick='ShowPermitDetails("<%=eventPopupWinowUrl%>")' title="Envirofacts">
					<bean:write name="permit" property="permitNumber" />
				 </A>	
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="permitType" value="">
					Permit Type:<bean:write name="permit" property="permitType" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="facilityName" value="">
					Facility Name:<bean:write name="permit" property="facilityName" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="permit" property="countyName" value="">
					County:<bean:write name="permit" property="countyName" />
					<BR>
				</logic:notEqual>
				<logic:equal name="permit" property="assWithAnotherFac" value="Y">
				   <pdk-html:img page="/images/checkmark.gif" />
				   Associated With Other Facility(s)
				</logic:equal>
			</TD>
           </logic:notEqual>
		</tr>	
	</logic:iterate>											

		<tr><td>&nbsp;</td></tr>

		<TR>
			<TD colspan="3" align="center">

				<!-- Prev -->
				<logic:greaterThan name="permitsSearchForm" property="startIndex" value="1">
					<A href="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_FIRST%>')">|&lt;&lt;</A>&nbsp;&nbsp;|&nbsp;&nbsp;
					<A href="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_PREVIOUS%>')">Prev</A>&nbsp;&nbsp;|&nbsp;&nbsp;
				</logic:greaterThan>
				<logic:lessEqual name="permitsSearchForm" property="startIndex" value="1">
				   |&lt;&lt;&nbsp;|&nbsp;
					Prev&nbsp;|&nbsp;
				</logic:lessEqual>

				<!-- x to y of z facilities-->
				
				<bean:write name="permitsSearchForm" property="startIndex" />
				-
				<bean:write name="permitsSearchForm" property="endIndex" />
				of
				<bean:write name="permitsSearchForm" property="listSize" />
				&nbsp;|&nbsp;

				<!-- Next -->
				
				<bean:define id="maxPermits"><bean:write name="permitsSearchForm" property="listSize" /></bean:define>					
				<logic:lessThan name="permitsSearchForm" property="endIndex" value="<%=maxPermits%>">
					    <A href="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_NEXT%>')">Next</A>&nbsp;&nbsp;|&nbsp;&nbsp;
					    <A href="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_LAST%>')">&gt;&gt;|</A>&nbsp;&nbsp;
				</logic:lessThan>	
			
				<logic:greaterEqual name="permitsSearchForm" property="endIndex" value="<%=maxPermits%>">
					Next&nbsp;&nbsp;|&nbsp;&nbsp;
					&gt;&gt;|&nbsp;&nbsp;
				</logic:greaterEqual>	
			</TD>
		</TR>
		<logic:equal name="permitsSearchForm" property="isUpdatable" value="Y">
		<tr>
			<td colspan="2">
				<INPUT type="button" onclick="javascript: submitPermitsSearchForm('<%=PermitsSearchAction.ACTION_ASSOCIATE_WITH_FACILITY%>')"  value="Associate with Facility">
			</td>
		</tr>
		</logic:equal>
</logic:greaterThan>		
	</TABLE>

</pdk-html:form>
		