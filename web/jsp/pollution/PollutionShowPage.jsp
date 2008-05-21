<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil"
	import="oracle.portal.provider.v2.url.UrlUtils"
	import="oracle.portal.utils.NameValue"
	import="gov.epa.owm.mtb.cwns.pollution.PollutionForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper"
	import="gov.epa.owm.mtb.cwns.pollution.PollutionCategoryGroupProblemListHelper"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		    String ajaxurl = url +"/javascript/prototype.js";
		    String listboxurl = url +"/javascript/listbox.js";			
   			PollutionForm pollutionForm = (PollutionForm)request.getAttribute("pollutionForm");	
   			Collection pollutionCategoryGroupProblemList0	= (Collection)request.getAttribute("pollutionCategoryGroupProblemList");
   			
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

<script type="text/javascript" language="JavaScript" src="<%=ajaxurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=listboxurl %>"></script>

<SCRIPT type=text/javascript>
// Removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

// Removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

// Removes leading and ending whitespaces
function trim( value ) {
	
	return LTrim(RTrim(value));
	
}

// Set the value of the "action" hidden attribute
function PollutionConfirmAndSubmit(form, selectedName, action)
{
	   var resultStr = "";
	   
   if(action == "save")
   {	
       var selectedList = window.document.getElementById(selectedName)
	   if(selectedList.options.length > 0)
	   {
	    resultStr = selectedList.options[0].value;
	    
		for (i=1;i<selectedList.options.length;i++) 
		{
			resultStr = resultStr + ',' + selectedList.options[i].value;
		}
	   }
	//alert(resultStr);
   	  window.document.getElementById("commaDilimitedPollutionProblemIds").value = resultStr;
   }
   
       hidden_field = window.document.getElementById("PollutionAct");
	   hidden_field.value=action;
	   
	   window.document.PollutionFormBean.submit();
	   return true;
}

function pollutionEnableMoveButtons(ctrl, buttonName)
{
	//alert(ctrl.options.length);
	
	var nothingSelected = true;
	
		for(var ii = ctrl.options.length-1; ii >= 0; ii--)
		{
			if(ctrl.options[ii].selected == true)
			{
				nothingSelected = false;
				window.document.getElementById(buttonName).disabled = false;
				//alert("something selected");
				break;
			}
		}
		
		if(nothingSelected == true)
		{
			window.document.getElementById(buttonName).disabled = true;
		}
	
}

function setPollutionGroupTypes(form)
{

  		var availPolluProbList=form['availPollutionProblemType'];

		availPolluProbList.options.length = 0;
	
  		var polluProbList=form['pollutionGroupType'];
  		var groupIdSelected = polluProbList.options[polluProbList.selectedIndex].value;

		if(groupIdSelected == "0") //ALL
		{
			var h = 0;
			for(j = 1; j < pollutionGroupProblemArray.length; j++)
				for(k=0; k < pollutionGroupProblemArray[j].length; k++)
				{
				    //alert(pollutionGroupProblemArray[j][k][0]);
		  			availPolluProbList.options[h] = new Option(pollutionGroupProblemArray[j][k][1], pollutionGroupProblemArray[j][k][0]);
		  			availPolluProbList.options[h].title = pollutionGroupProblemArray[j][k][0] + " - " + pollutionGroupProblemArray[j][k][1];				
					h++;
				}
		}
		else
		{
	  		var polluProbLength = pollutionGroupProblemArray[groupIdSelected].length;
	  		
	  		for(iDoc=0; iDoc<polluProbLength; iDoc++)
	  		{
	  			availPolluProbList.options[iDoc] = new Option(pollutionGroupProblemArray[groupIdSelected][iDoc][1], pollutionGroupProblemArray[groupIdSelected][iDoc][0]);
	  			availPolluProbList.options[iDoc].title = pollutionGroupProblemArray[groupIdSelected][iDoc][0] + " - " + pollutionGroupProblemArray[groupIdSelected][iDoc][1];
	  		}	
 		}
}

   var pollutionGroupProblemArray = new Array();
   
	<%
      if(pollutionCategoryGroupProblemList0!=null)
	  {
		Iterator iter0 = pollutionCategoryGroupProblemList0.iterator();
	
	   	long oldGroupId = -999;
	   	
	   	long maxGroup = 0;
	   	
	   	long probIndex = 0;

	   	while ( iter0.hasNext() ) 
	   	{
	   	    PollutionCategoryGroupProblemListHelper pgp = (PollutionCategoryGroupProblemListHelper) iter0.next();

	   		if(oldGroupId != pgp.getPollutionCategoryGroupId())
	   		{
	   			probIndex = 0;
	%>
				pollutionGroupProblemArray[<%=pgp.getPollutionCategoryGroupId()%>] = new Array();
    <%
    		}
    %>
			pollutionGroupProblemArray[<%=pgp.getPollutionCategoryGroupId()%>][<%=probIndex%>] = new Array();
			pollutionGroupProblemArray[<%=pgp.getPollutionCategoryGroupId()%>][<%=probIndex%>][0] = "<%=pgp.getPollutionProblemId()%>";
			pollutionGroupProblemArray[<%=pgp.getPollutionCategoryGroupId()%>][<%=probIndex%>][1] = "<%=pgp.getPollutionProblemName()%>";
	<%
	   		oldGroupId = pgp.getPollutionCategoryGroupId();
	   		probIndex++;
	   		
	   		maxGroup = (oldGroupId>maxGroup)?oldGroupId:maxGroup;
	    }
	  }
	%>

</SCRIPT>

<pdk-html:form 	name="PollutionFormBean" styleId="PollutionFormBeanId"
				type="gov.epa.owm.mtb.cwns.pollution.PollutionForm"
				action="pollution.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="pollutionAct" property="pollutionAct" value="<%=pollutionForm.getPollutionAct()%>"/>
		<pdk-html:text styleId="pollutionFacilityId" property="pollutionFacilityId" value="<%=pollutionForm.getPollutionFacilityId()%>"/>
	</DIV>

<span class="PortletText1"><FONT size="2">Pollution Problem Category</FONT></span>&nbsp;&nbsp;

	<pdk-html:select name="PollutionFormBean" styleId="pollutionGroupType" property="pollutionGroupType" 
	  onchange="setPollutionGroupTypes(this.form);mergeOptions('availPollutionProblemType','hiddenSelectedPolluProbType', 'selectedPolluProbType');deleteSrcSelectedFromTarget('selectedPolluProbType', 'availPollutionProblemType', false);" size="1">
		<OPTION value="0" selected="selected">
			 All
		</OPTION>	
	<logic:iterate id="pollutionGroupList" name="pollutionGroupList">
		<OPTION value="<bean:write name="pollutionGroupList" property="pollutionGroupId" />">
			 <bean:write name="pollutionGroupList" property="pollutionGroupName" />
		</OPTION>						
	 </logic:iterate>					
	 </pdk-html:select>	

    <br/>
    <br/>
    
    <%
       String showHideReadOnly = pollutionForm.getIsUpdatable().equalsIgnoreCase("Y")?"block":"none";
     %>
    
    <TABLE class="PortletText1">
    	 <TR>
		    <TD>
		    <DIV style="display:<%=showHideReadOnly%>">
		    <FONT size="2">Available Problems by Category</FONT>
		    </DIV>
		    </TD>
		    <TD>
		    &nbsp;
		    </TD>
		    <TD>
		    <FONT size="2">Selected Problems by Category</FONT>
		    </TD>
	     </TR>
	     <TR>
		    <TD>
    		<DIV style="display:<%=showHideReadOnly%>">
				<pdk-html:select name="PollutionFormBean"  multiple="true" size="6" style="width:250;" onchange="pollutionEnableMoveButtons(this, 'PollutionRemoveButton')"
				                 styleId="availPollutionProblemType" property="availPollutionProblemType">
				     <logic:present name="availablePollutionProblemTypes">
				   	 	<logic:iterate id="availablePollutionProblemType" name="availablePollutionProblemTypes" type="gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper">
							 <OPTION value="<bean:write name="availablePollutionProblemType" property="pollutionProblemId"/>">
								       <bean:write name="availablePollutionProblemType" property="pollutionProblemName"/>
							 </OPTION>	   	 	
				   	 	</logic:iterate>
				   	 </logic:present>
				</pdk-html:select>
		    </DIV>				
		    </TD>
		    <TD>
		    <DIV style="display:<%=showHideReadOnly%>">    
				<INPUT type="button" disabled="true" id="PollutionRemoveButton" name="PollutionRemoveButton" value="&gt;" onclick="addSelectedOptions('availPollutionProblemType','hiddenSelectedPolluProbType'); moveOptions('availPollutionProblemType','selectedPolluProbType');this.disabled=true;"/>
				<br/>
				<INPUT type="button" disabled="true" id="PollutionAddButton" name="PollutionAddButton" value="&lt;" onclick="deleteSrcSelectedFromTarget('selectedPolluProbType', 'hiddenSelectedPolluProbType', true); moveOptions('selectedPolluProbType', 'availPollutionProblemType');this.disabled=true;"/>		    
		    </DIV>		
		    </TD>
		    <TD>
				<pdk-html:select name="PollutionFormBean"  multiple="true" size="6" style="width:250;" onchange="pollutionEnableMoveButtons(this, 'PollutionAddButton')"
				                 styleId="selectedPolluProbType" property="selectedPolluProbType">
				     <logic:present name="selectedPollutionProblemTypes">
				   	 	<logic:iterate id="selectedPollutionProblemType" name="selectedPollutionProblemTypes" type="gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper">
							 <OPTION value="<bean:write name="selectedPollutionProblemType" property="pollutionProblemId"/>">
								       <bean:write name="selectedPollutionProblemType" property="pollutionProblemName"/>
							 </OPTION>	   	 	
				   	 	</logic:iterate>
				   	 </logic:present>
				</pdk-html:select>		    
		    </TD>
		    <TD>
		    <DIV  style="display:none">
				<select name="hiddenSelectedPolluProbType" multiple="true" size="6" id="hiddenSelectedPolluProbType">
				     <logic:present name="selectedPollutionProblemTypes">
				   	 	<logic:iterate id="selectedPollutionProblemType" name="selectedPollutionProblemTypes" type="gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper">
							 <OPTION value="<bean:write name="selectedPollutionProblemType" property="pollutionProblemId"/>">
								       <bean:write name="selectedPollutionProblemType" property="pollutionProblemName"/>
							 </OPTION>	   	 	
				   	 	</logic:iterate>
				   	 </logic:present>
				</select>	
			</DIV>
			&nbsp;	    
		    </TD>		    
	     </TR>
    </TABLE>
    <pdk-html:hidden name="PollutionFormBean" styleId="commaDilimitedPollutionProblemIds" property="commaDilimitedPollutionProblemIds"/>
    
    <br/>	        
	
	<logic:equal name="PollutionFormBean" property="isUpdatable" value="Y">			  
		<A href="javascript:PollutionConfirmAndSubmit(this.form, 'hiddenSelectedPolluProbType', 'save')">
		<pdk-html:img page="/images/submit.gif" alt="save" border="0"/></A>				   
	   <FONT size="1">&nbsp;&nbsp;</FONT>			   
		<A href="javascript:PollutionConfirmAndSubmit(this.form, 'hiddenSelectedPolluProbType', 'reset')">
		<FONT size="1">
		<pdk-html:img page="/images/reset.gif" alt="Reset" border="0"/>
		</FONT>
		</A>
	</logic:equal>	
</pdk-html:form>