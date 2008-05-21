<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'DocumentUploadResult.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
<script type="text/javascript">
function load(){

	if(document.resultfrm.result.value == 'false')
		alert(document.resultfrm.errormsg.value);
	
	top.location.replace(document.resultfrm.vurl.value);
}

</script>

  </head>
  
  <body onload="load()">
  
  <% 
  	if(new Boolean(true).equals(request.getAttribute("result"))){
  %>  
    The file was successfully uploaded.<br/>    
   <%} else{%> 
    The file did not upload successfully.<br/>        
    <%}%>
    <form name="resultfrm">
    	<input type="hidden" name="vurl" value="<%=request.getAttribute("vurl")%>"/>
    	<input type="hidden" name="result" value="<%=request.getAttribute("result")%>"/>
    	<input type="hidden" name="errormsg" value="<%=request.getAttribute("errormsg")%>"/>
    </form>
  </body>
</html>
