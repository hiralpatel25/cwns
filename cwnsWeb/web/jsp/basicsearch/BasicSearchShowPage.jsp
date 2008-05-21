<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="oracle.portal.provider.v2.personalize.PortletReference,
                oracle.portal.provider.v2.http.HttpCommonConstants,
                oracle.portal.provider.v2.render.PortletRenderRequest,
                oracle.portal.provider.v2.render.PortletRendererUtil,
                gov.epa.owm.mtb.cwns.common.struts.CWNSUrlUtils,
                gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
%>

<%
    PortletRenderRequest pRequest = (PortletRenderRequest)
        request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
    String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
    String eventSubmit       = CWNSEventUtils.eventName("searchKeyword");
    String keywordParam = CWNSEventUtils.eventParameter("keyword");
    String actionParam = CWNSEventUtils.eventParameter("action");
    String keyword=pRequest.getParameter("keyword");
    if(keyword == null) keyword="";
     
    String formName         = null;
    String formAction       = null;
    String formHiddenFields = null;
    try
    {
        formName   = CWNSUrlUtils.htmlFormName(pRequest,null);
        formAction = CWNSUrlUtils.htmlFormActionLink(pRequest, CWNSUrlUtils.EVENT_LINK);
        formHiddenFields = 
            CWNSUrlUtils.htmlFormHiddenFields(pRequest, CWNSUrlUtils.EVENT_LINK);
    }
    catch (IllegalStateException e)
    {
        //do something
    }
    String helpKey = (String)request.getAttribute("helpKey");
    
%>

<script language="JavaScript">

function submitenter(e)
{
 var keycode;
 if (window.event) keycode = window.event.keyCode;
 else if (e) keycode = e.which;
 else return true;
 if (keycode == 13){
   submitform();
   return false;
 } else
   return true;
}


function clearAct(){
	var inputTags = window.document.getElementsByTagName('input');
	for(var i=0;i<inputTags.length;i++){
	 element = inputTags[i];
	 var type = element.getAttribute('type');
	 if (type == 'hidden'){
	    var name = element.getAttribute('name');	    
	    if (name.indexOf('.act') > 1){
	    	element.value='';
	    }
	 } 
  }
}

function submitform()
{
 	clearAct();   
    document.<%=formName%>.submit();
}

// Open help window
function ShowSearchHelp(popupUrl)
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

  var w = window.open(popupUrl, 'BasicSearchHelp', settings);
  if(w != null) 
    w.focus();     
}
</script> 


<form name="<%=formName%>" method="POST" action="<%=formAction%>" >
    <!--Set hidden fields required by portal-->
    <%=formHiddenFields%>
    <input type="hidden" name="<%=eventSubmit %>" value="search">
    <input type="hidden" name="<%=actionParam %>" value="simple">
        <DIV align="right">
           <FONT class="PortletText1">
               <A href="javascript:void(0);" onclick='ShowSearchHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
           </FONT>
	    </DIV>
			
    <table border="0">
        <tr>
            <td class="PortletText1">
               Keyword  &nbsp;&nbsp;<input type="text" size="20" name="<%=keywordParam%>" value="<%=keyword%>" onkeypress="submitenter(event);"><A href="javascript: submitform()"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>               
            </td>    
        </tr>
    </table>    
    </form>