<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="gov.epa.owm.mtb.cwns.common.Entity"
	import="oracle.portal.provider.v2.ParameterDefinition"
    import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"
    import="oracle.portal.utils.NameValue"
    import="gov.epa.owm.mtb.cwns.model.EfDmrMeasurement"
    import="gov.epa.owm.mtb.cwns.npdes.NPDESPermitFlowDataForm"
	%>

<%
	PortletRenderRequest pRequest = (PortletRenderRequest) request
		.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    NameValue[] linkParams = null;

	NPDESPermitFlowDataForm permitFlowDataForm = (NPDESPermitFlowDataForm)request.getAttribute("permitFlowDataForm");
	String selectedPipeSchedId = permitFlowDataForm.getEfPipeSchedId();	
	float totalMaxLimit = 0;
	float totalAvgLimit = 0;				
	int maxSize = 0;
	int avgSize = 0;
	String showCalStartDate =null;
	String showCalEndDate =null;
	String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	String popurl = url +"/javascript/popcalendar.js";
	String dateurl = url +"/javascript/date.js";
	String imgurl = url +"/images/";
	showCalStartDate = "showCalendar(this,document.npdesPermitFlowDataFormId.startDate, 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";
	showCalEndDate = "showCalendar(this,document.npdesPermitFlowDataFormId.endDate, 'm/d/yyyy','en',1, -1, -1,'"+ imgurl +  "'); return false;";		
	
	String helpKey = (String)request.getAttribute("helpKey");
%> 

<SCRIPT type=text/javascript>

function  submitPermitFlowDataForm(action) {
  var startdate = document.npdesPermitFlowDataForm.startDate.value;
  var enddate = document.npdesPermitFlowDataForm.endDate.value;
  /*
  if(startdate !=""){
	if(!validateDate(startdate,'u','a')){
  		//alert("Please enter start date in MM/DD/YYYY format");
  		return false;
 	}
  }
  if(enddate !=""){
	if(!validateDate(enddate,'u','a')){
  		//alert("Please enter end date in MM/DD/YYYY format");
  		return false;
 	}
  }*/
  var form = document.getElementById('npdesPermitFlowDataFormId');   
  if(form!=undefined){
     if(!validateNpdesPermitFlowDataForm(form)){
       return;
     }
  } 
	window.document.getElementById('permitFlowDataAction').value    = action;
	window.document.npdesPermitFlowDataForm.submit();
	
}

function showPermitFlowDataPopUp(url){
     var w = 800;
     var h = 500;
	 var winl = (screen.width-w)/2;
	 var wint = (screen.height-h)/2;
	 var settings ='height='+h+',';
	 settings +='width='+w+',';
	 settings +='top='+wint+',';
	 settings +='left='+winl+',';
	 settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

     var w = window.open(url, 'PermitFlowDataHelp', settings);
     if(w != null) 
        w.focus();
   }
    
</SCRIPT>
<script type="text/javascript" language="JavaScript" src="<%=popurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=dateurl %>"></script>
<SCRIPT type=text/javascript>
	setImgDir('<%=imgurl%>');
</script>
<pdk-html:form method="post" 
	name="npdesPermitFlowDataForm" 
	styleId="npdesPermitFlowDataFormId"
	type="gov.epa.owm.mtb.cwns.npdes.NPDESPermitFlowDataForm" 
	action="NPDESPermitFlowData.do">
	<pdk-html:hidden styleId="permitFlowDataAction" name="npdesPermitFlowDataForm" property="action"/>	
<DIV align="right">
   <FONT class="PortletText1">
       <A href="javascript:void(0);" onclick='showPermitFlowDataPopUp("<%=url%><bean:message key="<%=helpKey%>"/>")'>Help</A>
   </FONT>
</DIV>
<TABLE class="PortletText1" border="0" cellpadding="2" cellspacing="3"
	width="100%">
	<TR>
		<TD colspan="2">
			<STRONG> NPDES Permit Number:
			      <%--
			     <%
 					linkParams[0] = new NameValue("permitNbr", permitFlowDataForm.getEfNpdesPermitNumber());
 					linkParams[1] = new NameValue("facilityId", permitFlowDataForm.getFacilityId().toString());
 					String eventPopupWinowUrl = CWNSEventUtils.constructEventLink(pRequest,"ViewPermitDetailsPage", linkParams,true, true);
 				 %>
 				 <A href="javascript:void(0);" onclick='ShowPermitDetails("<%=eventPopupWinowUrl%>")' title="Envirofacts">
					<bean:write name="permitFlowDataForm" property="efNpdesPermitNumber" />
				 </A>
				 --%>
				 <bean:write name="npdesPermitFlowDataForm" property="efNpdesPermitNumber" />
				 
			</STRONG> 
		</TD>
		
	</TR>
	<TR>
		<TD width="20%">
		    <STRONG> Pipe:</STRONG> 
		</TD>
		<TD>
		    <logic:equal name="npdesPermitFlowDataForm" property="displayOnly" value="true">
		        <logic:iterate id="efPipe" name="efPipesData" type="Entity">
		        <bean:write name="efPipe" property="value"/>
		        </logic:iterate>
		    </logic:equal>
		    <logic:notEqual name="npdesPermitFlowDataForm" property="displayOnly" value="true">
		    <pdk-html:select name="npdesPermitFlowDataForm" property="efPipeSchedId">
		       <logic:iterate id="efPipe" name="efPipesData" type="Entity">
		        <logic:match name="efPipe" property="key" value='<%=selectedPipeSchedId%>'>
		         <option value="<%=efPipe.getKey()%>" selected="selected"> 
		            <%=efPipe.getValue()%>
		         </option>
		        </logic:match>
		        <logic:notMatch name="efPipe" property="key" value='<%=selectedPipeSchedId%>'>
		          <option value="<%=efPipe.getKey()%>"> <%=efPipe.getValue()%> </option>
		        </logic:notMatch> 
		      </logic:iterate>
		    </pdk-html:select>
		    </logic:notEqual>
		</TD>
	</TR>
	<TR>
		<TD width="10%">
			<STRONG> Start Date </STRONG> :
		</TD>
		<TD width="55%">
		    <pdk-html:text name="npdesPermitFlowDataForm" property="startDate" styleId="startDate"></pdk-html:text>
		    <a href="javascript: void(0);" onclick="<%=showCalStartDate%>"><pdk-html:img page="/images/cal.jpg" border="0" /></a>
							
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> End Date </STRONG> :
		</TD>
		<TD>
		   <pdk-html:text name="npdesPermitFlowDataForm" property="endDate" styleId="endDate"></pdk-html:text>
		   <a href="javascript: void(0);" onclick="<%=showCalEndDate%>"><pdk-html:img page="/images/cal.jpg" border="0" /></a>
							 
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Search </STRONG> :
		</TD>
		<TD>
		    <logic:notEmpty name="efPipesData">
		        <A href="javascript: submitPermitFlowDataForm('search')"><pdk-html:img page="/images/search.gif" border="0" alt="search"/></A>
		    </logic:notEmpty>
		    <logic:empty name="efPipesData">
		       <pdk-html:img page="/images/search.gif" border="0" alt="search"/>
		    </logic:empty>
		</TD>
	</TR>
	<TR>
		<TD colspan="6">
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">
				<TR>
			      <TD colspan="4">
				  </TD>
		        </TR>
				<TR class="PortletHeaderColor">
                  <TD class="PortletHeaderText" align="center">
			         Date
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Maximum<BR>Quantity
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Average<BR>Quantity
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Unit of<BR>Measure
		          </TD>
		        </TR>
		        <logic:present name="permitFlowData">
               <logic:iterate id="flowData" name="permitFlowData" type="EfDmrMeasurement">
               <TR>
                 <TD align="center">
                    <%=flowData.getId().getMonitoringPeriodEndDate()%>                                                                                                                                                                                                                                                 
                 </TD>
                 <TD align="center">
                    <%=flowData.getConcentrMax() %>
                    <logic:equal name="npdesPermitFlowDataForm" property="isUnitOfMeasureSame" value="Y">
                     <%if (flowData.getConcentrMax()!=null && !"".equals(flowData.getConcentrMax())){
                        try{
                        totalMaxLimit = totalMaxLimit + (new Float(flowData.getConcentrMax())).floatValue();
                        avgSize = avgSize+1;
                        }
                        catch (NumberFormatException e)
                        {
                              totalMaxLimit = totalMaxLimit + 0;
                        }
                       } %>
                    </logic:equal>
                 </TD>
                 <TD align="center">
                    <%=flowData.getConcentrAvg() %>
                    <logic:equal name="npdesPermitFlowDataForm" property="isUnitOfMeasureSame" value="Y">
                     <%if (flowData.getConcentrAvg()!=null && !"".equals(flowData.getConcentrAvg())){
                        try{
                        totalAvgLimit = totalAvgLimit + (new Float(flowData.getConcentrAvg())).floatValue();
                        maxSize = maxSize+1;
                        }
                        catch (NumberFormatException e)
                        {
                         totalAvgLimit = totalAvgLimit + 0;
                        }
                      } %>
                    </logic:equal>  
                 </TD>
                 <TD align="center">
                    <%=flowData.getConcentrationUnitCode() %> 
                 </TD>
               </TR>
               
               </logic:iterate>
               <logic:equal name="npdesPermitFlowDataForm" property="isUnitOfMeasureSame" value="Y">
               <TR>
                  <TD align="center">
			         Average
		          </TD>
		          <TD align="center">
			         <%if (maxSize>0) {%>
			         <%=totalMaxLimit/maxSize %>
			         <%}%>
		          </TD>
		          <TD align="center">
		          <%if (avgSize>0) {%>
		             <%=totalAvgLimit/avgSize %>
		             <%} %>
			      </TD>
			      <TD>&nbsp;</TD>
		       </TR>
		       </logic:equal>
		       </logic:present>
		       <TR>
		          <TD colspan="4" align="center">
		             Present Design Flow:<bean:write name="npdesPermitFlowDataForm" property="preDesignFlowRate"/>
		          </TD> 
		       </TR>
            </TABLE>
        </TD>
    </TR>    
</TABLE>
</pdk-html:form>
<pdk-html:javascript formName="npdesPermitFlowDataForm" />	
