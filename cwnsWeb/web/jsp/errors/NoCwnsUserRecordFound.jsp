<%@ page language="java" pageEncoding="ISO-8859-1"
		 import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>

	 

<%
  String username = request.getParameter("_username");
  String suportAddress = CWNSProperties.getProperty("support.address");

 %>
  <body>
  	<div align="center" class="PortletText1">
    	The user <%=username%> is not recognized by CWNS. <br>
    	If you have received this message in error email<br>
    		<a href="mailto:<%=suportAddress%>?subject=Clean Watersheds Needs Survey - CWNS User Record Not Found">
			   CWNS Support</a>
	</div>											
  </body>
