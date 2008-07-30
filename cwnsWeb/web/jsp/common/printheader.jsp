<%@page contentType="text/html; charset=windows-1252"
	import="java.util.*"
	%>

<html>

<body>

<%
  Cookie[] myCookies = request.getCookies();

  for(int n=0; n < myCookies.length; n++)
  {
    out.print(myCookies[n].getName() + " ");
    out.print(myCookies[n].getValue() + "<BR>");
  }

%>

<table border=1 cellspacing=0 cellpadding=2>
<%
  String strHeaderName = "";
  for(Enumeration e = request.getHeaderNames();
                                   e.hasMoreElements() ;)
  {
    strHeaderName = (String)e.nextElement();
%>
    <tr>
      <td><%= strHeaderName %></td>
      <td><%= request.getHeader(strHeaderName)%> </td>
    </tr>
<%
  }
%>
</table>

</body>

</html>