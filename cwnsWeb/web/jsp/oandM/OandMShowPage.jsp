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
	import="gov.epa.owm.mtb.cwns.oandm.OandMForm"
	import="java.util.Collection"
	import="java.util.Iterator"
	import="gov.epa.owm.mtb.cwns.oandm.OandMListHelper"
	import="gov.epa.owm.mtb.cwns.oandm.OandMCategoryHelper"
	import="gov.epa.owm.mtb.cwns.common.CWNSProperties"
	%>

<%
			PortletRenderRequest prr = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

		    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

   			OandMForm oandMForm = (OandMForm)request.getAttribute("oandMForm");	
   			Collection oandMExcludedList0	= (Collection)request.getAttribute("oandMExcludedList");
   			Collection oandMCategoryList0 = (Collection)request.getAttribute("oandMCategoryList");   			
%>

<LINK REL=stylesheet TYPE="text/css"
	HREF="<%=HttpPortletRendererUtil.absoluteLink(prr,
							"../../css/cwns.css")%>">

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
function OandMConfirmAndSubmit(action, catId, year)
{
	if(action == "edit" || action == "delete")
	{
	   setOandMCatId(catId);
	   setOandMYear(year);
    }else if(action == "mark delete"){
    	setOandMCatId(catId);
    	setOandMYear(year);
     	hidden_field = window.document.getElementById("OandMAct");
     	hidden_field.value=action;     	
     	window.document.OandMFormBean.submit();
     	return true;
   }
    
    if(action == "save")
    {
    
      // verify year not just total
      
      // verify category mandatory
	  if(window.document.getElementById("oandMCatIdSelectCtrl").value == "")
	  {
		alert("Error: Category is required.");
		window.document.getElementById("oandMCatIdSelectCtrl").focus();
		return;    	  	
	  }

      window.document.getElementById("oandMCatIdCtrl").value = window.document.getElementById("oandMCatIdSelectCtrl").value;
         
      // verify cost & cost are positive integers
      var plantCostText = "Plant";
      var colCostText = "Collection";
      
      <% 
      	if(oandMForm.getIsNPS().equals("Y"))
      	{
      %>
	      plantCostText = "Monitoring";
	      colCostText = "Maintenance";
      <% 
        }
      %> 
      
      var colCost = window.document.getElementById("oandMCollectionCostCtrl");
      var plantCost = window.document.getElementById("oandMPlantCostCtrl");
      
      if(trim(colCost.value) == "") colCost.value = 0; // it's OK
      if(trim(plantCost.value) == "") plantCost.value = 0; // it's OK
      
      if(colCost.value.indexOf(",") >=0 || colCost.value.indexOf(".") >=0){
		alert("Error: " + colCostText + " Cost can contain numbers only.");
		colCost.focus();
		return;      	
      }
      
      if(!isNumber(colCost.value) ||colCost.value < 0 || colCost.value > 99999999)
      {
		alert("Error: " + colCostText + " Cost must be a integer greater than or equal to 0 and less than or equal to 99999999.");
		colCost.focus();
		return;      	
      }      
      
     if(plantCost.value.indexOf(",") >=0 || plantCost.value.indexOf(".") >=0){
		alert("Error: " + plantCostText + " Cost can contain numbers only.");
		plantCost.focus();
		return;      	
      }
            
      if(!isNumber(plantCost.value) || plantCost.value < 0 || plantCost.value > 99999999 )
      {
		alert("Error: " + plantCostText + " Cost must be a integer greater than or equal to 0 and less than or equal to 99999999.");
		plantCost.focus();
		return;      	
      }
      
      // verify cost + cost > 0
            
      if(plantCost.value + colCost.value == 0)
      {
		alert("Error: " + plantCostText + " Cost and/or " + colCostText + " Cost " + "is required.");
		plantCost.focus();
		return;          	
      }

    } // if save
    
      	// edit, just save
       hidden_field = window.document.getElementById("OandMAct");
	   hidden_field.value=action;
	   
	   window.document.OandMFormBean.submit(); 
	   
	   return;
}

var numb = "0123456789";
function isNumber(parm) {
	for (i = 0; i < parm.length; i++) {
		if (numb.indexOf(parm.charAt(i), 0) == -1) {
			return false;
		}
	}
	return true;
}

function setOandMCatId(catId)
{
	window.document.getElementById("oandMCatIdCtrl").value = catId;
}

function setOandMYear(year)
{
	window.document.getElementById("oandMYearCtrl").value = year;
}

// Show or Hide the add comment area of the Portlet
function showOrHideOandMDetails(id, mode)
{
     var d = window.document.getElementById(id);
     
     if(d!=null)
       d.style.display=mode;
}

function oandMShowHideClassification(noClassification, form)
{
	  	if(noClassification == "N")
  		{
  			window.document.getElementById("oandMClassificationShowHide").style.display = "block";
  			window.document.getElementById("oandMClassificationTextOnly").style.display = "none";
  		}
  		else if(noClassification == "Y")
  		{
  			window.document.getElementById("oandMClassificationShowHide").style.display = "none";
  			window.document.getElementById("oandMClassificationTextOnly").style.display = "block";  				
  		}
  		
  		form['oandMSSO'].options[0].selected = "1";
}

function formatCurrency(num) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
	//return (((sign)?'':'-') + '$' + num + '.' + cents);	
	return num;
}

function oandMFilterCat(form)
{
	oandMInitCategoryList(form);

  		var yearList=form['oandMYearCtrl'];
  		var yearSelected = yearList.options[yearList.selectedIndex].value;
  		//alert(yearSelected+"");

  		var catList=form['oandMCatIdSelectCtrl'];
  		
  		var catLength = catList.options.length;
  		
  		for(var iii = catLength-1; iii >=0; iii--)
  		{
  			var yearCatStr = yearSelected+":"+catList.options[iii].value;
			for(var j = 0; j<yearCatExcludeArray.length; j++)
  			{
  				if(yearCatStr == yearCatExcludeArray[j])
  				{
  					catList.options[iii]=null;
  					break;
  				}
  			}
  		}
}

function oandMInitCategoryList(form)
{

	var catList=form['oandMCatIdSelectCtrl'];
	
	catList.options.length = 0;

	catList.options[0] = new Option("Select a Category", "");
	catList.options[0].selected = "1";

	<%
	if(oandMCategoryList0 != null)
	{
		Iterator iter1 = oandMCategoryList0.iterator();
		
		int catI = 1;

	   	while ( iter1.hasNext() ) 
	   	{		
	   		OandMCategoryHelper omch = (OandMCategoryHelper)iter1.next();
	%>
		 catList.options[<%=catI%>] = new Option("<%=omch.getCategoryName()%>", "<%=omch.getCategoryId()%>");
	<%
		catI++;
		}
	}			 
	%>
}

   var yearCatExcludeArray = new Array();
   
	<%
      if(oandMExcludedList0!=null)
	  {
		Iterator iter0 = oandMExcludedList0.iterator();
		
		int catIndex = 0;

	   	while ( iter0.hasNext() ) 
	   	{
	   	    String yce = (String) iter0.next();

	%>
			yearCatExcludeArray[<%=catIndex%>] = "<%=yce%>";	
	<%
			catIndex++;
	    }
	  }
	%>


</SCRIPT>

<pdk-html:form 	name="OandMFormBean" styleId="OandMFormBeanId"
				type="gov.epa.owm.mtb.cwns.oandm.OandMForm"
				action="oandM.do">
	
	<!-- pdk-html:hidden tag does not work correctly in a Portal environment. (MNC) -->	
	<DIV id="hidden_fields" style="display:none">	
		<pdk-html:text styleId="oandMAct" property="oandMAct" value="<%=oandMForm.getOandMAct()%>"/>
		<pdk-html:text styleId="oandMFacilityId" property="oandMFacilityId" value="<%=oandMForm.getOandMFacilityId()%>"/>
		<pdk-html:text styleId="oandMIsAddAction" property="oandMIsAddAction" value="<%=oandMForm.getIsAddAction()%>"/>
		<pdk-html:text styleId="oandMCatIdCtrl" property="categoryId" value="<%=oandMForm.getCategoryId()%>"/>
	</DIV>
	
<TABLE class="PortletText1" cellspacing="2" cellpadding="2" width="100%"><TR><TD>
<FONT size="4"><STRONG>&nbsp;Operation & Maintenance</STRONG> </FONT>
</TD></TR></TABLE>
<FONT size="2">
<logic:equal name="feedbackCopyExists" value="Y">
				<p align="left" class="WarningText" colspan="14">
	     			<pdk-html:img page="/images/warn24.gif" alt="Data Area Warnings" border="0"/>	
        			&nbsp;<bean:message key="error.general.feedback.warning"/><br>        			
        		</p>	        
	        </logic:equal>
	        </FONT>
<TABLE class="PortletText1" cellspacing="3" cellpadding="2" width="100%"
	style="BORDER-BOTTOM: rgb(122,150,223) thin solid">

	<TR>
		<TD class="PortletHeaderColor">
			<P align="center">
				<STRONG> <FONT color="#ffffff"> Year
				</FONT>
				</STRONG>
			</P>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG>Category</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> 
				<STRONG>
					<logic:equal name="OandMFormBean" property="isNPS" value="Y">
					    Monitoring Cost	($)			
				    </logic:equal>		    
					<logic:equal name="OandMFormBean" property="isNPS" value="N">				
						Plant Cost ($)		
				    </logic:equal>
				</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">

				<FONT color="#ffffff"> 
				<STRONG>
					<logic:equal name="OandMFormBean" property="isNPS" value="Y">
					    Maintenance Cost ($)		
				    </logic:equal>		    
					<logic:equal name="OandMFormBean" property="isNPS" value="N">				
						Collection Cost ($) 		
				    </logic:equal>				
				</STRONG>
				</FONT>
			</DIV>
		</TD>
		<TD class="PortletHeaderColor">
			<DIV align="center">
				<FONT color="#ffffff"> <STRONG> Total Cost ($) </STRONG>
				</FONT>
			</DIV>
		</TD>
        <logic:equal name="OandMFormBean" property="isUpdatable" value="Y">
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Edit </STRONG>
					</FONT>
				</DIV>
			</TD>
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Delete </STRONG>
					</FONT>
				</DIV>
			</TD>
         </logic:equal>
         <logic:notEqual name="OandMFormBean" property="isUpdatable" value="Y">
			<logic:equal name="stateUser" value="false">
			<TD class="PortletHeaderColor">
				<DIV align="center">
					<FONT color="#ffffff"> <STRONG> Delete </STRONG>
					</FONT>
				</DIV>
			</TD>
			</logic:equal>
         </logic:notEqual>
	</TR>

    <c:set var="i" value="1" />
	<logic:iterate id="oandMHelper" scope="request" name="oandMList" type="gov.epa.owm.mtb.cwns.oandm.OandMListHelper">
		 <bean:define id="omListCategoryId" name="oandMHelper" property="categoryId"/> 
		 <bean:define id="omListHasDetailRecord" name="oandMHelper" property="hasDetailRecord"/> 	
			<logic:equal name="oandMHelper" property="categoryId" value="99">				
				    <c:set var="class" value="highlightedBoldRow" />
		    </logic:equal>
			<logic:notEqual name="oandMHelper" property="categoryId" value="99">				
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor" />   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
		    </logic:notEqual>		    
			<logic:equal name="oandMHelper" property="categoryName" value="<%=oandMForm.getCategoryName()%>">				
			<logic:equal name="oandMHelper" property="year" value="<%=oandMForm.getYear()%>">				
				    <c:set var="class" value="RowHighlighted" />
		    </logic:equal>
		    </logic:equal>

	<TR class="<c:out value="${class}"/>">
		<TD align="center">
			<P align="center">
				<bean:write name="oandMHelper" property="year"/>
			</P>
		</TD>
		<TD align="left">
			<P align="left">
				<bean:write name="oandMHelper" property="categoryName"/>
			</P>
		</TD>		
		<TD align="right">
			<P align="right" >
			<fmt:formatNumber type="currency" pattern="#,##0">
				      <bean:write name="oandMHelper" property="plantCost"/>
			</fmt:formatNumber>		      
			</P>                                                                                                                                                                                                                                    
		</TD>
		<TD align="right">
	      <fmt:formatNumber type="currency" pattern="#,##0">
			<bean:write name="oandMHelper" property="collectionCost"/>
	      </fmt:formatNumber>					
		</TD>
		<TD align="right">
	      <fmt:formatNumber type="currency" pattern="#,##0">
			<bean:write name="oandMHelper" property="totalCost"/>
	      </fmt:formatNumber>					
		</TD>
        <logic:equal name="OandMFormBean" property="isUpdatable" value="Y">
		 <%
		 	if(!((String)omListCategoryId).equals("99") || ((String)omListHasDetailRecord).equals("N"))
		 	{
		  %>
		    <TD align="center">
				<P align="center">
				<A href="javascript:OandMConfirmAndSubmit('edit', '<bean:write name="oandMHelper" property="categoryId"/>', '<bean:write name="oandMHelper" property="year"/>')">
					<pdk-html:img page="/images/edit.gif" alt="Edit" border="0"/>
				</A>
				</P>
			</TD>
			<TD align="center">
			<logic:equal name="stateUser" value="true">
				<A href="javascript:OandMConfirmAndSubmit('delete', '<bean:write name="oandMHelper" property="categoryId"/>', '<bean:write name="oandMHelper" property="year"/>')">
					<pdk-html:img page="/images/delete.gif" alt="Delete" border="0"/>
				</A>
			</logic:equal>
			<logic:equal name="stateUser" value="false">
					<bean:define id="clickCheckBox">
						javascript:OandMConfirmAndSubmit('mark delete', '<bean:write name="oandMHelper" property="categoryId"/>', '<bean:write name="oandMHelper" property="year"/>')
					</bean:define>								
				    <pdk-html:checkbox name="oandMHelper" property="feedbackDeleteFlag" value="Y" onclick="<%=clickCheckBox%>">
				    </pdk-html:checkbox>		
			</logic:equal>
			</TD>
		 <%
			}
			else
			{
		  %>
		  <TD></TD><TD></TD>
		 <%
			}
		  %>		  
         </logic:equal>
         
         <logic:notEqual name="OandMFormBean" property="isUpdatable" value="Y">
         <logic:equal name="stateUser" value="false">
		 <%
		 	if(!((String)omListCategoryId).equals("99") || ((String)omListHasDetailRecord).equals("N"))
		 	{
		  %>
		   <TD align="center">
			    <pdk-html:checkbox name="oandMHelper" property="feedbackDeleteFlag" value="Y" disabled="true">
			    </pdk-html:checkbox>		
		   </TD>
		   <%
		     }
		   %> 
		 </logic:equal>		  
         </logic:notEqual>
	</TR>
		<c:set var="i" value="${i+1}" />	
    </logic:iterate>

<%
    String showDetail = "none";
	String showAddLink = "block";
	
    if((oandMForm.getDetailEditExpand()).equals("Y"))
    {
      showDetail = "block";
      showAddLink = "none";
    }
%>
	
        <logic:equal name="OandMFormBean" property="isUpdatable" value="Y">
		<TR>
			<TD colspan="7" align="left">
				 <DIV id="addNewOandMLink" style="display:<%=showAddLink%>" class="PortletText1">
					<a href="javascript: OandMConfirmAndSubmit('new', '', '');">
						<br/>Add Operation & Maintenance Cost
					</a>
				 </DIV>
			</TD>
		</TR>
        </logic:equal>
        <logic:equal name="OandMFormBean" property="isUpdatable" value="N">
		<TR>
			<TD colspan="5" align="left">
					<br/>
			</TD>
		</TR>
        </logic:equal>        
</TABLE>

<DIV id="oandMDetails" style="display:<%=showDetail%>; background-color: #cccccc; padding: 5"  class="PortletText1">
<BR>
		<P>&nbsp;<Strong>
		   <FONT size="4">
					  Add/Edit Operation & Maintenance Cost&nbsp;
		   </FONT>&nbsp;</Strong>
		</P>

		<TABLE border="0" class="PortletText1" cellspacing="0" cellpadding="1" width="100%" 
		          style="BORDER-BOTTOM: rgb(122,150,223) thin solid">
			<TR>
				<TD>
					<span class="required">*</span><STRONG> Year: </STRONG>
				</TD>
				<TD>
					<logic:equal name="OandMFormBean" property="isAddAction" value="Y">				
						<pdk-html:select name="OandMFormBean" 
										 styleId="oandMYearCtrl" 
										 property="year" 
										 onchange="oandMFilterCat(this.form)"
										 size="1">
								<%
									for (long ii = oandMForm.getCurrentYear()-20; ii<=oandMForm.getCurrentYear()+20;ii++)
									{
								 %>

										<OPTION <%=(ii==oandMForm.getCurrentYear())?"SELECTED":""%> value="<%=ii%>">
											<%=ii%>
										</OPTION>	
								<%
								    }
								 %>
						</pdk-html:select>
					</logic:equal>
					<logic:equal name="OandMFormBean" property="isAddAction" value="N">	
					    <pdk-html:hidden name="OandMFormBean" property="year" styleId="oandMYearCtrl"/>			
						<bean:write name="OandMFormBean" property="year" />
					</logic:equal>				
				</TD>
				<TD width="50%"></TD>
			</TR>
			<TR>										
				<TD>
					<span class="required">*</span><STRONG> Category: </STRONG>
				</TD>
				<TD>
				    <logic:notEqual name="OandMFormBean" property="categoryId" value="99">	
						<select id="oandMCatIdSelectCtrl" name="oandMCatIdSelectCtrl"
								size="1" 
								style="width: 200px">
						<OPTION value="">
							Select a Category
						</OPTION>							
						<logic:iterate id="oandMCategoryHelper" scope="request" name="oandMCategoryList" type="gov.epa.owm.mtb.cwns.oandm.OandMCategoryHelper">	
						
						<logic:equal name="oandMCategoryHelper" property="categoryId" value="<%=oandMForm.getCategoryId()%>">
										<OPTION SELECTED value="<bean:write name="oandMCategoryHelper" property="categoryId"/>">
											<bean:write name="oandMCategoryHelper" property="categoryName" />
										</OPTION>	
						</logic:equal>
						<logic:notEqual name="oandMCategoryHelper" property="categoryId" value="<%=oandMForm.getCategoryId()%>">
										<OPTION value="<bean:write name="oandMCategoryHelper" property="categoryId"/>">
											<bean:write name="oandMCategoryHelper" property="categoryName" />
										</OPTION>	
						</logic:notEqual>											
						</logic:iterate>					
						</select>
						<logic:equal name="OandMFormBean" property="isAddAction" value="Y">
						<script language="javascript">
							oandMFilterCat(window.document.OandMFormBean);
						</script>
						</logic:equal>
					</logic:notEqual>
					<logic:equal name="OandMFormBean" property="categoryId" value="99">	
						<bean:write name="OandMFormBean" property="categoryName" />
						<INPUT TYPE=HIDDEN NAME="oandMCatIdSelectCtrl" id="oandMCatIdSelectCtrl" value="<%=oandMForm.getCategoryId()%>">
					</logic:equal>
					<pdk-html:hidden name="OandMFormBean" property="oldCategoryId" styleId="OldOandMCatIdCtrl"/>							
				</TD>
				<TD width="50%"></TD>				
			</TR>
			<TR>
				<TD>
					<span class="required">*</span>
					<logic:equal name="OandMFormBean" property="isNPS" value="Y">
					    <STRONG> Monitoring Cost ($): </STRONG>				
				    </logic:equal>		    
					<logic:equal name="OandMFormBean" property="isNPS" value="N">				
						<STRONG> Plant Cost ($): </STRONG>		
				    </logic:equal>					
				</TD>
				<TD>	
						<pdk-html:text name="OandMFormBean" property="plantCost" maxlength="11" styleId="oandMPlantCostCtrl"/>
				</TD>
			</TR>
			<TR>
				<TD>
					<span class="required">*</span>
					<logic:equal name="OandMFormBean" property="isNPS" value="Y">
					    <STRONG> Maintenance Cost ($): </STRONG>				
				    </logic:equal>		    
					<logic:equal name="OandMFormBean" property="isNPS" value="N">				
						<STRONG> Collection Cost ($): </STRONG>	
				    </logic:equal>						
				</TD>
				<TD>	
						<pdk-html:text name="OandMFormBean" property="collectionCost" maxlength="11" styleId="oandMCollectionCostCtrl"/>
				</TD>
				<TD width="50%"></TD>				
			</TR>		
			<TR>
				<TD colspan="3">
				<div align="left">
				<logic:equal name="OandMFormBean" property="isUpdatable" value="Y">			  
				    <INPUT type="button" value="Save" 
					alt="Save"
					onclick="javascript:OandMConfirmAndSubmit('save', '', '')"/>										   
				   <FONT size="1">&nbsp;&nbsp;</FONT>
				</logic:equal>	
				    <INPUT type="button" value="Cancel" 
					alt="Cancel"
					onclick="javascript:{showOrHideOandMDetails('addNewOandMLink', 'block'); showOrHideOandMDetails('oandMDetails', 'none')}"/>								   
				</div>			   
   			   </TD>
			</TR>
		</TABLE>
</DIV>

</pdk-html:form>
