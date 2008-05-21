<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String requestURL = basePath + "/ghg/" + request.getServletPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Simple upload page</title>
        <link rel="stylesheet" type="text/css" href="<%=basePath%>css/cwns.css" />
        <script src='<%=basePath%>javascript/upload.js'> </script>
        <script src='<%=basePath%>dwr/interface/UploadMonitor.js'> </script>
        <script src='<%=basePath%>dwr/engine.js'> </script>
        <script src='<%=basePath%>dwr/util.js'> </script>
        <style type="text/css">
            body { font: 11px Lucida Grande, Verdana, Arial, Helvetica, sans serif; }
            #progressBar { padding-top: 5px; }
            #progressBarBox { width: 350px; height: 20px; border: 1px inset; background: #eee;}
            #progressBarBoxContent { width: 0; height: 20px; border-right: 1px solid #444; background: #9ACB34; }
        </style>
        <script type="text/javascript">
		function disableCancel(){
			document.getElementById("cancelbutton").disabled="true";
			return true;
		}
		
		function cencelbuttonClicked(){			
			document.getElementById("uploadfields").style.visibility = "hidden";
			document.getElementById("progressBar").style.visibility = "hidden";			
		}		
		</script>
	</head>
<body>
<form name="uploadfrm" action="<%=request.getContextPath()%>/servlet/NeedsFileUpload" enctype="multipart/form-data" method="post" onsubmit="startProgress()">
    <input type="hidden" name="rowID" value="<%=request.getParameter("rowid")%>"/>
    <input type="hidden" name="vurl" value="<%=request.getParameter("vurl")%>"/>
    <div id="uploadfields" style="display: inline">
    <TABLE border="0" class="PortletText1" cellspacing="3" cellpadding="2" width="75%">
			<TR>
				<TD>
    				<span class="required">*</span><FONT size="1"><STRONG>  Document File:  </STRONG><input class="default" type="file" id="file1" name="file1" SIZE="50"/></FONT>
    			</TD>
    		</TR>
    		<TR>
    			<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Begin Upload" id="uploadbutton" onclick="disableCancel();"/>
    				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" id="cancelbutton" onClick="cencelbuttonClicked();"/>
    			</TD>
    		</TR>
    </TABLE>
    <input type="hidden" id="file2" name="file2"/>  
    <input type="hidden" id="file3" name="file3"/>
    <input type="hidden" id="file4" name="file4"/>
    </div>  
    <div id="progressBar" style="display:none">
            <div id="theMeter">
                <div id="progressBarText"></div>
                <div id="progressBarBox">
                    <div id="progressBarBoxContent"></div>
                </div>
            </div>
    </div>
</form>
</body>
</html>
