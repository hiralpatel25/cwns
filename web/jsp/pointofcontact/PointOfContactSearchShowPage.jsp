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
<%@ page import="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactSearchForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactSearchAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.Entity"%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    NameValue[] linkParams = null;

	PointOfContactSearchForm pocsForm = (PointOfContactSearchForm)request.getAttribute("pocsForm");
	String stateId = pocsForm.getStateId();		
	boolean isUpdateable = pocsForm.isUpdateable();			
		
	String pocSearchUrl = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + request.getContextPath();

	String helpKey = (String)request.getAttribute("helpKey");
%>

<SCRIPT type=text/javascript>
function  submitPocSearchForm(action) {

	if (action == '<%=PointOfContactSearchAction.ACTION_ASSOCIATE_WITH_FACILITY%>') {
	  
      	var form=window.document.getElementById("pointOfContactSearchForm");
      	var pocs = form["selectedPocs"];
      	var found = false;
        if (pocs.checked == true){
          found = true;
        }
          
      	for (i = 0; i < pocs.length; i++){
         	if (pocs[i].checked == true){
         		found = true;
        	}
        }
        
        if (!found) {
        	alert('You must select a Point of Contact to associate with this Facility');
        	return;
        }
	}
	
	window.document.getElementById('pocSearchAction').value    = action;
	window.document.pointOfContactSearchForm.submit();
}

// Open help window
function showPocSearchHelp(popupUrl)
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

  var w = window.open(popupUrl, 'PocSearchHelp', settings);
  if(w != null) 
    w.focus();     
}

</SCRIPT>


<pdk-html:form method="post" 
	name="pointOfContactSearchForm" 
	type="gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactSearchForm" 
	styleId="pointOfContactSearchForm"
	action="pointOfContactSearch.do">
	
	<pdk-html:hidden styleId="pocSearchAction" name="pointOfContactSearchForm" property="action"/>
	<pdk-html:hidden name="pointOfContactSearchForm" property="searchType"/>
	<pdk-html:hidden name="pointOfContactSearchForm" property="startIndex"/>
	<pdk-html:hidden name="pointOfContactSearchForm" property="endIndex"/>
	<pdk-html:hidden name="pointOfContactSearchForm" property="listSize"/>

	<DIV align="right">
		<FONT class="PortletText1">
			<A href="javascript:void(0);" onclick='showPocSearchHelp("<%=pocSearchUrl%><bean:message key="<%=helpKey%>"/>")'>
			<pdk-html:img page="/images/help.gif"
							alt="Help" border="0" />
			</A>
	    </FONT>
	</DIV>
<TABLE cellspacing="2" cellpadding="3" border="0" width="100%" class="PortletText1">

	<TR>
	  	<TD class=RegionHeaderColor>
			<STRONG>State:</STRONG>&nbsp;&nbsp;
		</TD>
		<TD>
			<pdk-html:select size="1"  name ="pointOfContactSearchForm" styleId="stateId" property="stateId" >  
				<logic:iterate id="state" name="states" type="Entity">
		  		<logic:match name="state" property="key" value='<%=stateId%>'>				
 					<OPTION value='<bean:write name="state" property="key"/>' selected="selected"> <bean:write name="state" property="value"/></OPTION> 
 				</logic:match>
		  		<logic:notMatch name="state" property="key" value='<%=stateId%>'>
 					<OPTION value='<bean:write name="state" property="key"/>'> <bean:write name="state" property="value"/></OPTION> 
 				</logic:notMatch>
		        </logic:iterate>
           </pdk-html:select>  
		</TD>
	</TR>
	
  	<TR>
		<TD class="PortletText1">
			<strong>Keyword:</strong>
		</TD>											
		<td>
			<pdk-html:text name="pointOfContactSearchForm" property="keyword" size="20" maxlength="30" />
			<A href="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_KEYWORD_SEARCH%>')"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>
		</td>																
	</TR>


	<logic:lessEqual name="pointOfContactSearchForm" property="listSize" value="0">
	<TR><TD>&nbsp;</TD></TR>
			
	<TR class="PortletText1">
		<TD align="center" colspan="9">
			<b><strong>No records found.</strong></b>
		</TD>
	</TR>
									
	</logic:lessEqual>					
	<logic:greaterThan name="pointOfContactSearchForm" property="listSize" value="0">
					
	
	<TR bgcolor="gray">
		<TD align="center">
			<STRONG>
<%				if (isUpdateable) { %>Select <%  } %>		
			</STRONG></TD>
		<TD><Strong>Info</strong></TD>
	</TR>
	
	<logic:iterate id="poc" name="pocList">
		<tr>
			<TD align="center" valign="top">
<%  	if (isUpdateable) { %>	
			    <pdk-html:multibox name="pointOfContactSearchForm" 
			    	property="selectedPocs" 
			    	styleId="selectedPocs">
			       <bean:write name="poc" property="pointOfContactId"/>
			    </pdk-html:multibox> 
<%  	} %>		
			</TD>

			<TD>
				<logic:notEqual name="poc" property="authorityName" value="">
					<bean:write name="poc" property="authorityName" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="poc" property="name" value="">
					<bean:write name="poc" property="name" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="poc" property="streetAddress1" value="">
					<bean:write name="poc" property="streetAddress1" />
					<BR>
				</logic:notEqual>
				<logic:notEqual name="poc" property="streetAddress2" value="">
					<bean:write name="poc" property="streetAddress2" />
					<BR>
				</logic:notEqual>
				
				<!-- city & state -->
				<logic:notEqual name="poc" property="city" value="">
					<logic:notEqual name="poc" property="stateId" value="">
						<bean:write name="poc" property="city" />, <bean:write name="poc" property="stateId" />&nbsp;<bean:write name="poc" property="zip" />
					</logic:notEqual>
				</logic:notEqual>

				<!-- No city, no state  -->
				<logic:equal name="poc" property="city" value="">
					<logic:equal name="poc" property="stateId" value="">
						<bean:write name="poc" property="zip" />
					</logic:equal>
				</logic:equal>

			<!-- No city  -->
				<logic:equal name="poc" property="city" value="">
					<logic:notEqual name="poc" property="stateId" value="">
						<bean:write name="poc" property="stateId" />&nbsp;<bean:write name="poc" property="zip" />
					</logic:notEqual>
				</logic:equal>

		
				<!-- No state  -->
				<logic:notEqual name="poc" property="city" value="">
					<logic:equal name="poc" property="stateId" value="">
						<bean:write name="poc" property="city" />&nbsp;<bean:write name="poc" property="zip" />
					</logic:equal>
				</logic:notEqual>
			</TD>
		</tr>	
	</logic:iterate>											

		<tr><td>&nbsp;</td></tr>

		<TR>
			<TD colspan="3" align="center">

				<!-- Prev -->
				<logic:greaterThan name="pointOfContactSearchForm" property="startIndex" value="1">
					<A href="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_FIRST%>')">|&lt;&lt;</A>&nbsp;&nbsp;|&nbsp;&nbsp;
					<A href="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_PREVIOUS%>')">Prev</A>&nbsp;&nbsp;|&nbsp;&nbsp;
				</logic:greaterThan>
				<logic:lessEqual name="pointOfContactSearchForm"	property="startIndex" value="1">
				   |&lt;&lt;&nbsp;|&nbsp;
					Prev&nbsp;|&nbsp;
				</logic:lessEqual>

				<!-- x to y of z facilities-->
				
				<bean:write name="pointOfContactSearchForm" property="startIndex" />
				-
				<bean:write name="pointOfContactSearchForm" property="endIndex" />
				of
				<bean:write name="pointOfContactSearchForm" property="listSize" />
				&nbsp;|&nbsp;

				<!-- Next -->
				
				<bean:define id="maxPocs"><bean:write name="pointOfContactSearchForm" property="listSize" /></bean:define>					
				<logic:lessThan name="pointOfContactSearchForm" property="endIndex" value="<%=maxPocs%>">
					    <A href="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_NEXT%>')">Next</A>&nbsp;&nbsp;|&nbsp;&nbsp;
					    <A href="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_LAST%>')">&gt;&gt;|</A>&nbsp;&nbsp;
				</logic:lessThan>	
			
				<logic:greaterEqual name="pointOfContactSearchForm" property="endIndex" value="<%=maxPocs%>">
					Next&nbsp;&nbsp;|&nbsp;&nbsp;
					&gt;&gt;|&nbsp;&nbsp;
				</logic:greaterEqual>	
			</TD>
		</TR>
		
<%      if (isUpdateable) { %>	
		<tr>
			<td colspan="2">
				<INPUT type="button" onclick="javascript: submitPocSearchForm('<%=PointOfContactSearchAction.ACTION_ASSOCIATE_WITH_FACILITY%>')"  value="Associate with Facility">
			</td>
		</tr>
<%      } %>		

</logic:greaterThan>		
	</TABLE>

</pdk-html:form>
		