<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/c-1_0-rt.tld" prefix="c-rt"%>

<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.BasicProfileAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.RegistrationPreliminaryAction"%>
<%@ page import="gov.epa.owm.mtb.cwns.userregistration.RegistrationForm"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.Entity" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils" %>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties" %>



<%
	PortletRenderRequest prr = (PortletRenderRequest)
      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      
    String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
                
       String dateurl = url +"/javascript/date.js";
	RegistrationForm rForm = (RegistrationForm)request.getAttribute("rForm");
	String stateID = rForm.getStateId();
	boolean updateable = ("true".equals(rForm.getUpdateable())) ? true : false;
   String previousActionUrl = CWNSStrutsUtils.createHref(request,"registrationPreliminary.do");
   
   String iamResponse = (String)request.getAttribute("response");
   String helpKey = (String)request.getAttribute("helpKey");
   

   
%>

<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>
<SCRIPT type="text/javascript">

function submitRegForm(action){
	var form = document.getElementById('registrationForm'); 

<%if (updateable) { %>	
	if(validateRegistrationForm(form)==false){
    	return;
    }
<%}%>    
    
	window.document.getElementById("registration_action").value = action;
	form.submit();
}

// Go back to the Preliminary Information Registration page
function previousPage(action){
	var form = document.getElementById('registrationForm'); 
	window.document.getElementById("registration_action").value = action;
	form.submit();
}
  
// Open help window
function showPortalRegistrationHelp(popupUrl)
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

  var w = window.open(popupUrl, 'PortalRegistrationHelp', settings);
  if(w != null) 
    w.focus();     
}
    

function clearRegistrationForm() { 
  	var form, elements, i, elm; 
  	form = document.getElementById('registrationForm'); 
  
	if (document.getElementsByTagName)
	{
		elements = form.getElementsByTagName('input');
		for( i=0, elm; elm=elements.item(i++); )
		{
			if (elm.getAttribute('type') == "text")
			{
				elm.value = '';
			}
		}
		
		selements = form.getElementsByTagName('select');
		for( i=0, elm; elm=selements.item(i++); )
		{
			elm.options[0].selected=true;
		    for(x=0; x<elm.options.length;x++){
				if (elm.options[x].selected == true){
				   elm.options[x].selected=false;
				}
		    }	
		}
	}
}

</SCRIPT>

<DIV align="right">
	<FONT class="PortletText1">
		<A href="javascript:void(0);" onclick='showPortalRegistrationHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
    </FONT>
</DIV>

<FONT color="red">
<html:errors />
</FONT>

<pdk-html:form method="post" 
	name="registrationForm" 
	type="gov.epa.owm.mtb.cwns.userregistration.RegistrationForm" 
	styleId="registrationForm"
	action="basicProfile.do">

	<pdk-html:hidden styleId="registration_action" name="registrationForm" property="action"/>
	<pdk-html:hidden styleId="oidUserId" name="registrationForm" property="oidUserId" />
	<pdk-html:hidden styleId="cwnsUserId" name="registrationForm" property="cwnsUserId"/>
	<pdk-html:hidden styleId="userTypeId" name="registrationForm" property="userTypeId"/>
	<pdk-html:hidden styleId="comments" name="registrationForm" property="comments"/>

	
<!--  UPDATEALBE  -->
<logic:equal name="registrationForm" property="updateable" value="true"> 

<TABLE cellpadding="0"  cellspacing="0" border="0" align="center" class="PortletText1">
	
	<TR>
		<TD colspan="2" align="center" ><STRONG>User Profile Information</STRONG></TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>	
	<TR>
		<TD colspan="2"><HR></TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>First Name</strong></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="20" size="35" name="registrationForm" styleId="firstName" property="firstName" />
        </TD>
	</TR>
	
	<TR height="35">
      	<TD><font color="red">*</font><strong>Last Name</strong></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="30" size="35" name="registrationForm" styleId="lastName" property="lastName" />
        </TD>
	</TR>
	
	<TR height="35">
     	<TD><font color="red">*</font><strong>Email Address</strong><BR> &nbsp;&nbsp;<FONT size="-1">format: emailId@domainname.com</FONT></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="40" size="35" name="registrationForm" styleId="emailAddress" property="emailAddress" />
        </TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>Street Address</strong></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="50" size="35" name="registrationForm" styleId="street" property="street" />
        </TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>City</strong></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="50" size="35" name="registrationForm" styleId="city" property="city" />
      	</TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>State</strong></TD>
		<TD>
			<pdk-html:select name="registrationForm" styleId="stateId" property="stateId" >
			 	<OPTION value=""></OPTION>
				<logic:iterate id="state" name="states" type="Entity">
			  		<logic:match name="state" property="key" value='<%=stateID%>'>				
						<OPTION value='<bean:write name="state" property="key"/>' selected="selected"> <bean:write name="state" property="value"/></OPTION> 
					</logic:match>
			  		<logic:notMatch name="state" property="key" value='<%=stateID%>'>
						<OPTION value='<bean:write name="state" property="key"/>'> <bean:write name="state" property="value"/></OPTION> 
					</logic:notMatch>
		        </logic:iterate>
	     	</pdk-html:select>  
		
		</TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>Postal Code</strong><BR> &nbsp;&nbsp;<FONT size="-1">format: nnnnn</FONT></TD>
        <TD valign="top" >
			<pdk-html:text maxlength="30" size="35" name="registrationForm" styleId="zip" property="zip" />
        </TD>
	</TR>
	
	<TR height="35">
    	<TD><font color="red">*</font><strong>Daytime Phone Number</strong><BR> &nbsp;&nbsp;<FONT size="-1">format: (xxx) xxx-xxxx</FONT>&nbsp;</TD>
        <TD valign="top" >
			<pdk-html:text maxlength="50" size="35" name="registrationForm" styleId="phone" property="phone" />
        </TD>
	</TR>
	
</TABLE>

</logic:equal>







<!--  DISPLAY ONLY  -->
<logic:notEqual name="registrationForm" property="updateable" value="true"> 

<!--  Hidden variables -->
	<pdk-html:hidden name="registrationForm" property="firstName" />
	<pdk-html:hidden name="registrationForm" property="lastName" />
	<pdk-html:hidden name="registrationForm" property="emailAddress" />
	<pdk-html:hidden name="registrationForm" property="street" />
	<pdk-html:hidden name="registrationForm" property="city" />
	<pdk-html:hidden name="registrationForm" property="stateId" />
	<pdk-html:hidden name="registrationForm" property="zip" />
	<pdk-html:hidden name="registrationForm" property="phone" />

<TABLE cellpadding="0"  cellspacing="0" border="0" align="center">
	
	<TR>
		<TD colspan="3" align="center" ><STRONG>Portal Profile Information</STRONG></TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>	
	<TR>
		<TD colspan="3"><HR></TD>
	</TR>
	
	<TR>
    	<TD><strong>First Name</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="firstName"/></TD>
	</TR>
	
	<TR>
    	<TD><strong>Last Name</Strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="lastName"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>Email Address</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="emailAddress"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>Street Address</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="street"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>City</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="city"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>State</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="stateId"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>Postal Code</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="zip"/></TD>
	</TR>
	
	<TR>
     	<TD><strong>Daytime Phone Number</strong></TD>
		<TD>&nbsp;</TD>     	
        <TD><bean:write  name="registrationForm" property="phone"/></TD>
	</TR>
	
</TABLE>
</logic:notEqual>


<P style="margin-bottom:10px;text-align:center;">

<INPUT type="button" name="back" value="Back" onclick="javascript:previousPage('<%=RegistrationPreliminaryAction.ACTION_PRELIM_REDISPLAY%>');">
<logic:equal name="registrationForm" property="mode" value="<%=RegistrationForm.PORTAL_USER%>"> 
	<INPUT type="button" value="Next" onclick="javascript:submitRegForm('<%=BasicProfileAction.ACTION_PROCESS%>');">
</logic:equal>
<logic:notEqual name="registrationForm" property="mode" value="<%=RegistrationForm.PORTAL_USER%>"> 
	<INPUT type="button" value="Next" onclick="javascript:submitRegForm('<%=BasicProfileAction.ACTION_PROCESS%>');">
</logic:notEqual>

</P>

</pdk-html:form>
<pdk-html:javascript formName="registrationForm" />

<logic:notEmpty name="return">
	<logic:equal name="return" value="<%=BasicProfileAction.RETURN_TO_SELFREGISTION%>">
		
		<script type="text/javascript">
		<!--
				window.location = "<%=CWNSProperties.getProperty("iam.self.registration.page")%>?response=<%=iamResponse%>";
		-->
		</script>
	</logic:equal>
	<logic:equal name="return" value="<%=BasicProfileAction.RETURN_TO_SUBSCRIPTION%>">
		
		<script type="text/javascript">
		
		<!--	
				window.location = "<%=CWNSProperties.getProperty("iam.group.subscription.page")%>?response=<%=iamResponse%>";
		-->
		</script>
	</logic:equal>
</logic:notEmpty>