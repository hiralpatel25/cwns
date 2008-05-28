<%--  PLEASE READ !!!

	This jsp uses the oracle.security.sso. To allow the jsp to compile (although the actual
	login will not work)on a developer's local machine 3 sections of the code must be commented out. 
	These are delimeted by "MNC START" & "MNC END".
--%>

<%-- MNC START --%>
<%@ page language="java" 
    import="java.util.*,
    		javax.servlet.http.*,
    		java.util.regex.*,
    		gov.epa.portal.util.*,
    		gov.epa.portal.util.filter.sso.*,
    		oracle.security.sso.server.mesg.*"%>
    		
<jsp:useBean id="msgFactory" scope="application" 
    class="oracle.security.sso.util.SSOResourceFactory" />
    
<%-- MNC END --%>
<%-- @ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties" --%>

<%
	//Passing the midtier server name from the properties file
	String midtierURL = SSOUtilSettings.getInstance().getSetting("midtier_sso_url");

	String landingPage         = "/pls/portal/url/page/cwns/CWNS_State_Home_Page";
	String selfRegistrationUrl = "/pls/portal/url/page/cwns/CWNS_Self_Registration";
 %>
 
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><!-- #BeginTemplate "/Templates/beach-opa.dwt" --><!-- DW6 -->
<!-- EPA Template version 3.2.1, 28 June 2006 -->
<head>
	<!-- #BeginEditable "doctitle" --> 
<title>US EPA Clean Watersheds Needs Survey (CWNS) - 2008</title>
<meta name="Description" content="" />
<meta name="Keywords" content="" />
<!-- #EndEditable -->
	<!-- #BeginEditable "metaElements" -->
	<meta name="DC.Date.modified" content="" />
	<meta name="DC.Subject" content="" />
	<meta name="DC.Type" content="" />
	<meta name="DC.Date" content="2006-05-25" />
	<!-- #EndEditable -->

	<meta name="DC.Language" content="en|sp" />
	<meta name="DC.Creator" content="US EPA, OW, Office of Science and Technology" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="schema.DC" href="http://purl.org/dc/elements/1.1/" />
	<link rel="meta" href="http://www.epa.gov/labels.rdf" type="application/rdf+xml" title="ICRA labels" />
	
	<style type="text/css" media="screen">@import 'http://www.epa.gov/epafiles/s/epa.css';</style>
	<!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="http://www.epa.gov/epafiles/s/ie.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" media="print" href="http://www.epa.gov/epafiles/s/print.css" />
<%-- 	<script type="text/javascript" src="http://www.epa.gov/epafiles/js/epa-core.js"></script> --%>
	<script type="text/javascript" src="http://www.epa.gov/epafiles/js/extra.js"></script>	
</head> 

<body>
<p class="skip"><a id="skiptop" href="#content" title="Jump to main content.">Jump to main content.</a></p>

<div id="header"> <!-- START EPA HEADER -->

	<div id="logo"><a href="http://www.epa.gov/" title="US EPA home page"><img src="http://www.epa.gov/epafiles/images/logo_epaseal.gif" alt="[logo] US EPA" width="100" height="110" /></a></div>

	<div id="areaname"> <!-- START AREA NAME -->
		<p>Clean Watersheds Needs Survey (CWNS)</p>
	</div> <!-- END AREA NAME -->

	<!-- START SEARCH CONTROLS -->
	<form id="EPAsearch" method="get" action="http://nlquery.epa.gov/epasearch/epasearch">
		<p><a href="http://www.epa.gov/cwns/contact.htm">Contact Us</a> |
		<A HREF="http://www.epa.gov/cgi-bin/epaprintonly.cgi">Print Version</A> 
			<span class="search"><strong>Search:</strong> 
			<input type="hidden" name="fld" value="owmitnet" />
			<input type="hidden" name="areaname" value="Clean Watersheds Needs Survey" />
			<input type="hidden" name="areacontacts" value="http://www.epa.gov/cwns" />
			<input type="hidden" name="areasearchurl" value="" />
			<input type="hidden" name="result_template" value="epafiles_default.xsl" />
 			<input type="hidden" name="filter" value="samplefilt.hts" />
			<input name="typeofsearch" id="EPAall" type="radio" value="epa"/><label for="EPAall">All EPA</label> 
			<input name="typeofsearch" id="Areaall" type="radio" value="area" checked="checked" /><label for="Areaall">This Area</label> 
			<input name="querytext" id="searchbox" value="" /> 
			<INPUT TYPE="image" SRC="http://www.epa.gov/epafiles/images/epafiles_btn_gosearch.gif" VALUE="Run Search" BORDER="0" WIDTH="27" HEIGHT="16" ALT="Run Search" NAME="image" /></span>
		</p>
	</form> 
	<!-- END SEARCH CONTROLS -->
		
	<ul> <!-- BEGIN BREADCRUMBS -->
		<li class="first">You are here: <a href="http://www.epa.gov/">EPA Home</a></li>
		<!-- START AREA BREADCRUMBS -->
                    <li> <a href="http://www.epa.gov/ow/ ">Water</a></li> 
					<li> <a href="http://www.epa.gov/owm/ ">Wastewater</a></li>
					<li> <a href="http://epa.gov/cwns/">CWNS</a></li> 
                                                                           
                    <li><a href="http://www.epa.gov/owm/mtb/cwns/cwns2008.htm">CWNS 2008</a></li>
                    <li>Login</li>
</ul> <!-- END BREADCRUMBS -->

</div> <!-- END EPA HEADER -->	<table border="0" cellpadding="0" cellspacing="0" width="95%">
	<tr> 
		<!--@@@startprint@@@-->
		<td class="epaSideNavCell" valign="top">
		<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr> 
					<td id="epaSideBar"> 

<!-- BEGIN SIDEBAR NAVIGATION -->

<div id="area-nav"> 


<!-- BEGIN LEFT SIDEBAR NAVIGATION -->
	<h3 class="skip">Local Navigation</h3>
	<ul>
	
<li><a href="http://www.epa.gov/cwns/" class="epaSideBarLinks">CWNS Home Page</a></li>
	
</ul>
</div> <!-- END LEFT SIDEBAR NAVIGATION -->

<!-- END SIDEBAR NAVIGATION -->

			</td>
				</tr>
			</table><p>&nbsp;</p>
			<img src="images/epafiles_misc_space.gif" alt="" border="0" height="1" hspace="69" vspace="2" width="2"></td>
		<td valign="top"><img src="images/epafiles_misc_space.gif" alt="" height="5" width="5"></td>
		 
		<!--@@@endprint@@@-->
		<td valign="top" width="100%" style=""> 
			<!-- BEGIN HORIZONTAL TASKBAR -->
			<!-- If using a horizontal taskbar, put the code here -->
			<!-- END HORIZONTAL TASKBAR -->
			<a name="pagecontents"></a> 
<!-- BEGIN PAGE NAME -->
<h1 class="epaPageName"><!-- #BeginEditable "PageName" --> 

<!-- #EndEditable --></h1>



<!-- START CWNS 2008 SPECIFIC INFORMATION -->



<SCRIPT TYPE="text/javascript">
</script>
	  <br>	
	  <p> 
	   
	  <strong>CWNS 2008</strong> 
		 
		<br /><br />Data collection for CWNS 2008 is from February 5, 2008 through February 27, 2009.&nbsp; For the first time, both state staff and local users (e.g., wastewater facilities, municipal governments, conservation districts) can enter data directly into the CWNS system.&nbsp; The state CWNS coordinator reviews submitted data before submitting it to EPA.<br /><br />If you do not have an account on the CWNS system, click here to <a href="<%=midtierURL%><%=selfRegistrationUrl%>">apply</a>.&nbsp; Accounts must be approved by the CWNS State Coordinator in your state. Please allow up to five business days for account approval process.<br /></p>	
		
		
		
		

<!-- START CWNS 2008 FORM -->


<%-- START MNC --%>
<%
response.setContentType("text/html");
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 


// Initilize oracle messages resource bundle
ResourceBundle msgBundle = msgFactory.getResourceBundle(request, 
      ServerMsgID.MESSAGE_BUNDLE_NAME);
      
String str_err = null;
String cookieName = null;
String cookieValue = null;
String ob_name = null;
String ob_value = null;
String ss_name = null;
String ss_value = null;

try
{
   str_err = request.getParameterValues("p_error_code")[0];
}
catch(Exception e)
{
  str_err = null;
}

Cookie[] cookies = request.getCookies();
	
	if( cookies != null && cookies.length>  0){
    	for(int i=0; i<cookies.length; i++){
          	Cookie cookie = cookies[i];
          	cookieName = cookie.getName();
          	cookieValue = cookie.getValue();	
        	if( ("ObSSOCookie".equalsIgnoreCase(cookieName) ) && (cookieValue != null)){
				ob_name = cookieName; 
				ob_value = cookieValue;   
			}
			if( ("SSO_ID".equalsIgnoreCase(cookieName) ) && (cookieValue != null)) {	
				ss_name = cookieName;
				ss_value = cookieValue;
			}
		}
	}
        boolean loggedIn = false;
        if( ("ObSSOCookie".equalsIgnoreCase(ob_name)) && 
			(ob_value != null) && 
			("SSO_ID".equalsIgnoreCase(ss_name) ) && 
			(ss_value != null) ){
		
			loggedIn = true;	
		}
        
		if( loggedIn ) {
%>
		<table border="0" cellpadding="0" cellspacing="0" width="95%">
			<tr><td colspan="2">		
				You have already logged in, please click the link below to go to the CWNS Home Page
				<br><center><a href="<%=midtierURL%><%=landingPage%>">CWNS</a> </center>
			</tr></td>
		</table>
		
		<% } else  if((str_err != null) && (str_err.length() > 1)) {%>
		<table border="0" cellpadding="0" cellspacing="0" width="95%">
			<tr><TD colspan="2">		
				<p style="text-align:center;font-family:Arial, Helvetica, sans-serif;font-size:12px;"><span style="font-weight:bold;color:#990000;">Error:&nbsp;</span><%=msgBundle.getString(str_err)%></p>
				</TD>
			</TR>
		</table>
		<%}%>		


<%   if (!loggedIn ) { %>	
<%-- END MNC  --%>		
	<table border="0" cellpadding="0" cellspacing="0" width="95%">
		<tr><td>
		<!-- Begin login Form -->
		<form method="post" action="/sso/authCWNS">
			<table  align="center" style="margin-top:15px;margin-bottom:15px;font-family:Arial, Helvetica, sans-serif;font-size:12px;">
				<tr>
					<td align="right"><strong>User Name:</strong> </td>
					<td><input type="text" name="loginid" size="20" maxlength="80" value=""></td>
				</tr>
				<tr>
					<td align="right"><strong>Password:</strong> </td>
					<td><input type="password" name="password" size="20" maxlength="255"></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:right;padding-right:10px;"><input type="submit" value="Login"></td>
				</tr>
			</table>
		</form>
		</td></tr>
		</table>
<%-- START MNC --%>
<%}%>
<%-- END MNC --%>

<!-- END CWNS 2008 FORM -->
<!-- END CWNS 2008 SPECIFIC INFORMATION -->





<table border="0" cellpadding="0" cellspacing="0" width="95%">
	<tr> 
		<td rowspan="4" align="center" valign="top"><img src="images/epafiles_misc_space.gif" alt="" height="5" width="5"></td>
		<td align="center" valign="top" width="100%"> 
			<!-- BEGIN AREA FOOTER -->
			<!-- ADD LINKS AND TEXT FOR AREA FOOTER WITHIN THIS PARAGRAPH TAG -->
			
			<!-- END AREA FOOTER -->
		</td>
	</tr>
	<tr> 
		<td align="center" valign="top" width="100%">&nbsp;</td>
	</tr>
	<tr> 
		<td align="center" valign="top" width="100%"> 
			<!-- BEGIN FOOTER IMAGE -->
			<img src="images/epafiles_misc_dot_dkblue.gif" alt="Begin Site Footer" height="4" vspace="2" width="460"> 
			<!-- END FOOTER IMAGE -->
		</td>
	</tr>
	<tr> 
		<td align="center" valign="top" width="100%"> 
			<!-- BEGIN GLOBAL FOOTER -->
			
	  <p class="epaFooterText">
			<a href="http://www.epa.gov/">EPA Home</a> | 
			<a href="http://www.epa.gov/epafiles/usenotice.htm">Privacy and Security Notice</a> | 

		<!-- BEGIN AREA COMMENTS LINK -->
		<!-- CHANGE THIS URL TO POINT TO CONTACTS PAGE FOR THIS AREA -->
		<a href="http://www.epa.gov/owm/contactowm.htm">Contact Us</a>
		<!-- END AREA COMMENTS LINK -->

			</p>
			<p class="epaFooterText">			
			<p>&nbsp;</p>
			<!-- END GLOBAL FOOTER -->
		</td>
	</tr>
</table><p>&nbsp;</p>
</html>