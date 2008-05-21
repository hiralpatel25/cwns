<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants,
                oracle.portal.provider.v2.render.PortletRenderRequest,
                gov.epa.owm.mtb.cwns.common.struts.CWNSUrlUtils,
                gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils,
                gov.epa.owm.mtb.cwns.userlist.UserListAction,
                gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction,
                gov.epa.owm.mtb.cwns.usersearch.UserSearchForm"
%>
<%@ page import="gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.LocationTypeRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.LocationRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.model.AccessLevelRef"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.Entity"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%
    PortletRenderRequest pRequest = (PortletRenderRequest)
        request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
    
	UserSearchForm userSearchForm = (UserSearchForm)request.getAttribute("userSearchForm");
    
    String url = request.getScheme()+"://"+request.getServerName()+
                ":"+request.getServerPort()+request.getContextPath();
    String eventSubmit       = CWNSEventUtils.eventName("userSearch");
    String keywordParam = CWNSEventUtils.eventParameter("keyword");
    String actionParam = CWNSEventUtils.eventParameter("action");
    String locationParam = CWNSEventUtils.eventParameter("locationId");
    String typeParam = CWNSEventUtils.eventParameter("userType");
    String statusParam = CWNSEventUtils.eventParameter("userStatus");
    String accessLevelParam = CWNSEventUtils.eventParameter("AccessLevel");
    String keyword = userSearchForm.getKeyword();
    
    Collection locationRefs = (Collection) request.getAttribute("locationRefs");
    Collection locationTypeRefs = (Collection) request.getAttribute("locationTypeRefs");
    String helpKey = (String)request.getAttribute("helpKey"); 
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

function submitTheForm()
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

  var w = window.open(popupUrl, 'UserSearchHelp', settings);
  if(w != null) 
    w.focus();     
}

function clearForm(formIdent) { 
  var form, elements, i, elm; 
  form = formIdent;
  
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


</script> 


<form name="<%=formName%>" method="POST" action="<%=formAction%>" >
    <!--Set hidden fields required by portal-->
    <%=formHiddenFields%>
    <input type="hidden" name="<%=eventSubmit %>" value="search">
    <input type="hidden" name="<%=actionParam %>" value="<%=UserListAction.ACTION_SEARCH%>">
    
        <DIV align="right">
           <FONT class="PortletText1">
               <A href="javascript:void(0);" onclick='ShowSearchHelp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
           </FONT>
	    </DIV>
			
	<TABLE cellspacing="3" cellpadding="2" border="0" width="100%" class="PortletText1">
			
	    <tr>
         	<td>
               <strong>Name:&nbsp;&nbsp;</strong>
            </td>    
            <td class="PortletText1">
			  <input type="text" size="20" name="<%=keywordParam%>" value="<%=keyword%>"> 
            </td>    
        </tr>
        <tr>
            <td class="PortletText1">
               <strong>Location:</strong>&nbsp;&nbsp;
            </td>    
			<%if (locationRefs.size() == 1) { 
				Iterator iter = locationRefs.iterator();
				LocationRef locationRef = (LocationRef) iter.next();
				String name = locationRef.getName();
				String locId = locationRef.getLocationId();
				%>
			<TD>
				<%=name%>
				<pdk-html:hidden name="userDetailsForm" property="currentLocId" value="<%=locId%>"/>
			<%}else { %>
				<TD class="PortletText1">
				<select size="1"  name ="<%=locationParam%>" property="locationId" >
					<option value='all'>All</option>
        	    	<logic:iterate id="loc" name="locationRefs" type="LocationRef">
						<logic:match name="loc" property="locationId" value='<%=userSearchForm.getLocationId()%>'>
	 						<OPTION selected="selected" value='<bean:write name="loc" property="locationId" />'><bean:write name="loc"  property="name" /></OPTION>
						</logic:match>
						<logic:notMatch name="loc" property="locationId" value='<%=userSearchForm.getLocationId()%>'>
	 						<OPTION value='<bean:write name="loc" property="locationId" />'><bean:write name="loc"  property="name" /></OPTION>
						</logic:notMatch> 		            	

            	    </logic:iterate>
    	  	  	</select>  
			<%}%>
			</TD>
		</tr>

        <tr>
            <td >
               <strong>Type:</strong>&nbsp;&nbsp;
            </td>    
            <td>
            
				<select name ="<%=typeParam%>" property="locationTypeRefs" > 
					<option value='all'>All</option>
	       	    	<logic:iterate id="locType" name="locationTypeRefs" type="LocationTypeRef">
						<logic:match name="locType" property="locationTypeId" value='<%=userSearchForm.getLocationType()%>'>
							<OPTION value='<bean:write name="locType" property="locationTypeId" />' selected="selected"><bean:write name="locType"  property="locationTypeId" /></OPTION>
						</logic:match>
						<logic:notMatch name="locType" property="locationTypeId" value='<%=userSearchForm.getLocationType()%>'>
							<OPTION value='<bean:write name="locType"  property="locationTypeId" />'><bean:write name="locType"  property="locationTypeId" /></OPTION>
						</logic:notMatch> 		            	
        	        </logic:iterate>

	            </select>  
			</td>    
		</tr>

        <tr>
            <td>
               <strong>Status:</strong>&nbsp;&nbsp;
            </td>    
            <td>
				<select name="<%=statusParam%>"  property="status" > 
    	            <OPTION value="">All</OPTION>
        	    	<logic:iterate id="userStatus" name="cwnsUserStatusRefs" type="CwnsUserStatusRef">
						<logic:match name="userSearchForm" property="status" value='<%=userStatus.getCwnsUserStatusId()%>'>
							<OPTION value=<bean:write name="userStatus" property="cwnsUserStatusId" /> selected="selected"><bean:write name="userStatus" property="name" /></OPTION>
						</logic:match>
						<logic:notMatch name="userSearchForm" property="status" value='<%=userStatus.getCwnsUserStatusId()%>'>
							<OPTION value=<bean:write name="userStatus" property="cwnsUserStatusId" />><bean:write name="userStatus" property="name" /></OPTION>
						</logic:notMatch> 		            	
            	    </logic:iterate>
	            </select>  
            </td>    
		</tr>

        <tr>
        
		<TR>
        	<td valign="top"><strong>Access Levels:</strong></td>
			<TD>  

				<select size="4" multiple="11" name ="<%=accessLevelParam%>" property="accessLevelIds" > 
        	    	<logic:iterate id="al" name="userAccessLevels" type="AccessLevelRef">
						  <bean:define id="accessLevelId"><bean:write name="al" property="accessLevelId"/></bean:define>											
						  <%
						  		boolean contains = false;
						  		 for(int i=0; i< userSearchForm.getAccessLevelIds().length;i++){
						  		    if (accessLevelId.equals(userSearchForm.getAccessLevelIds()[i])){
										contains=true;													  		    
						  		    }
						  		 }
						  		 if(contains){      
						   %>								
		 						<OPTION value='<bean:write name="al"  property="accessLevelId" />' selected="selected" ><bean:write name="al"  property="name" /></OPTION>
						   <%	
						   		  } else{
						    %>
 								<OPTION value='<bean:write name="al"  property="accessLevelId" />'><bean:write name="al"  property="name" /></OPTION>
							<% } %>
            	    </logic:iterate>
	           </select>  	
		 	</TD>
		</TR>

		<TR>
			<TD colspan="3">
				<P align="center">
				<INPUT type="button" name="searchbtn" onclick="submitTheForm()" value="Search" > 
				<input type="button" value="Clear" name="clear" onclick="clearForm(<%=formName%>)"/>				
				</P>
			</TD>
		</TR>		            
    </table>    
    </form>