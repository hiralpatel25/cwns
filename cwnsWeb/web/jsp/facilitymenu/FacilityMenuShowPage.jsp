<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.utils.NameValue"%>

<%@ page import="gov.epa.owm.mtb.cwns.common.struts.CWNSEventUtils"%>
<%@ page import="gov.epa.owm.mtb.cwns.common.CWNSProperties"%>
<%@ page import="gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>

<%
			PortletRenderRequest pReq = (PortletRenderRequest) request
			.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	    	NameValue[] linkParams = new NameValue[1];
	    	String facility_id=pReq.getParameter("facilityId");
	    	PopulationHelper ph= (PopulationHelper)request.getAttribute("population");
	    	String dataAreaQueryParameters = (String)request.getAttribute("dataAreaQueryParameters");
	    	int expDocsSize = ((Integer)request.getAttribute("expiredDocsSize")).intValue();
	    	
	    	int presentResPop=0;
	    	int presentNonResPop=0;
	    	int projectedResPop=0;
	    	int projectedNonResPop=0;
	    	if(ph!=null){
	    		presentResPop=ph.getPresentResPopulation();
	    		presentNonResPop=ph.getPresentNonResPopulation();
	    		projectedResPop=ph.getProjectedResPopulation();
	    		projectedNonResPop=ph.getProjectedNonResPopulation();
	    	}
	    	
	    	//Facility fact sheet report
	    	String reportUrl = CWNSProperties.getProperty("report.url");
	    	String reportLocation = CWNSProperties.getProperty("report.location");
	    	String reportName = CWNSProperties.getProperty("report.factsheet");
	    	String reporthtmlFormat = CWNSProperties.getProperty("report.desformat.html");
	    	String reportpdfFormat = CWNSProperties.getProperty("report.desformat.pdf");
	    	String htmlReportUrl = reportUrl+"&desformat="+reporthtmlFormat+"&report="+reportLocation+"/"+reportName+"&facility_id=" 
	    							+facility_id+"&pUpStrResPop="+presentResPop+"&pUpStrNonResPop="+presentNonResPop
	    							+"&fUpStrResPop="+projectedResPop+"&fUpStrNonResPop="+projectedNonResPop;
	    	String pdfReportUrl = reportUrl+"&desformat="+reportpdfFormat+"&report="+reportLocation+"/"+reportName+"&facility_id=" 
	    							+facility_id+"&pUpStrResPop="+presentResPop+"&pUpStrNonResPop="+presentNonResPop
	    							+"&fUpStrResPop="+projectedResPop+"&fUpStrNonResPop="+projectedNonResPop;
	    							
	    	if (dataAreaQueryParameters!=null){
	    		htmlReportUrl += dataAreaQueryParameters;
	    		pdfReportUrl += dataAreaQueryParameters;
	    	}
	    	
	    	//small community form
	    	boolean isSmallCommunity = 	false;
	    	if (request.getAttribute("isSmallCommunity")!=null && 
	    		((String)request.getAttribute("isSmallCommunity")).equals("true")){	    		
	    		isSmallCommunity = true;
	    	}	    	
	    	
	    	String smallCommunityNeedsFormName = "";
	    	String smallCommunityNeedsFax = "";
	    	String htmlSmallCommunityNeedsUrl = "";
	    	String pdfSmallCommunityNeedsUrl = "";
	    	
	    	if (isSmallCommunity){
	    		smallCommunityNeedsFormName = CWNSProperties.getProperty("report.smallcommunityneeds");
	    		smallCommunityNeedsFax = CWNSProperties.getProperty("report.smallcommunityneeds.fax");
	    		htmlSmallCommunityNeedsUrl = reportUrl+"&desformat="+reporthtmlFormat+"&report="+reportLocation+"/"
	    								+smallCommunityNeedsFormName+"&facility_id=" 
	    								+facility_id+"&pUpStrResPop="+presentResPop+"&pUpStrNonResPop="+presentNonResPop
	    								+"&fUpStrResPop="+projectedResPop+"&fUpStrNonResPop="+projectedNonResPop
	    								+"&fax=" + smallCommunityNeedsFax+"&expDocsSize="+expDocsSize;
	    								    							
	    		pdfSmallCommunityNeedsUrl = reportUrl+"&desformat="+reportpdfFormat+"&report="+reportLocation+"/"
	    								+smallCommunityNeedsFormName+"&facility_id=" 
	    								+facility_id+"&pUpStrResPop="+presentResPop+"&pUpStrNonResPop="+presentNonResPop
	    								+"&fUpStrResPop="+projectedResPop+"&fUpStrNonResPop="+projectedNonResPop
	    								+"&fax=" + smallCommunityNeedsFax+"&expDocsSize="+expDocsSize;
	    	}
	    	
	    	//facility feedback form	
	    	String htmlFeedbackFacilityForm = "";
	    	String pdfFeedbackFacilityForm = "";
	    	if (request.getAttribute("isFeedbackFacility")!=null && 
	    		((String)request.getAttribute("isFeedbackFacility")).equals("true")){
	    		String f_facilityId = "";
	    		int f_presentResPop=0;
	    		int f_presentNonResPop=0;
	    		int f_projectedResPop=0;
	    		int f_projectedNonResPop=0;
	    		
	    		if (request.getAttribute("f_facilityId")!=null){
	    			f_facilityId = (String)request.getAttribute("f_facilityId");
	    		}
	    		
	    		if (request.getAttribute("f_population")!=null){
			    	PopulationHelper f_ph= (PopulationHelper)request.getAttribute("f_population");
	    			f_presentResPop=f_ph.getPresentResPopulation();
	    			f_presentNonResPop=f_ph.getPresentNonResPopulation();
	    			f_projectedResPop=f_ph.getProjectedResPopulation();
	    			f_projectedNonResPop=f_ph.getProjectedNonResPopulation();
	    		}
	    		
				reportName = CWNSProperties.getProperty("report.facilityfeedback");
	    		
	    		htmlFeedbackFacilityForm = reportUrl+"&desformat="+reporthtmlFormat+"&report="+reportLocation+"/"+reportName+"&facility_id=" 
	    								+f_facilityId+"&pUpStrResPop="+f_presentResPop+"&pUpStrNonResPop="+f_presentNonResPop
	    								+"&fUpStrResPop="+f_projectedResPop+"&fUpStrNonResPop="+f_projectedNonResPop;
	    		pdfFeedbackFacilityForm = reportUrl+"&desformat="+reportpdfFormat+"&report="+reportLocation+"/"+reportName+"&facility_id=" 
	    								+f_facilityId+"&pUpStrResPop="+f_presentResPop+"&pUpStrNonResPop="+f_presentNonResPop
	    								+"&fUpStrResPop="+f_projectedResPop+"&fUpStrNonResPop="+f_projectedNonResPop;
	    								
	    		if (request.getAttribute("f_dataAreaQueryParameters")!= null){
	    			String f_dataAreaQueryParameters = (String)request.getAttribute("f_dataAreaQueryParameters");
	    			htmlFeedbackFacilityForm += f_dataAreaQueryParameters;
	    			pdfFeedbackFacilityForm += f_dataAreaQueryParameters;
	    		}	    		
	    	}
%>
<SCRIPT type=text/javascript>
   function ShowReport(url){
     var w = 800;
     var h = 900;
	 var winl = (screen.width-w)/2;
	 var wint = (screen.height-h)/2;
	 var settings ='height='+h+',';
	 settings +='width='+w+',';
	 settings +='top='+wint+',';
	 settings +='left='+winl+',';
	 settings +='toolbar=no,titlebar=yes,menubar=no,scrollbars=yes,status=yes,resizable=yes,location=no';

     var w = window.open(url, null, settings);
     if(w != null) 
        w.focus();
   }
</SCRIPT>

<TABLE width="100%" border=0>
	<TBODY>
	  <logic:equal name="showReports" value="Y">
		<TR>
			<TD align=left width="60%">
			    <FONT class=PortletText1>Fact Sheet</FONT>
			</TD>
			<td align="right">
 				<A href="javascript:void(0);" onclick='ShowReport("<%=htmlReportUrl%>")'><pdk-html:img page="/images/html.gif" alt="Facility Fact Sheet - HTML Version" border="0"/></A>
 				&nbsp;&nbsp;&nbsp;
				<A href="javascript:void(0);" onclick='ShowReport("<%=pdfReportUrl%>")'><pdk-html:img page="/images/pdf.jpg" alt="Facility Fact Sheet - PDF Version" border="0"/></A>
			</TD>
		</TR>
		<logic:equal name="isSmallCommunity" value="true">
		<TR>
			<TD align=left>
			    <FONT class=PortletText1>Small Community Needs Form</FONT>
			</TD>
			<td align="right">
 				<A href="javascript:void(0);" onclick='ShowReport("<%=htmlSmallCommunityNeedsUrl%>")'><pdk-html:img page="/images/html.gif" alt="Small Community Needs Form - HTML Version" border="0"/></A>
 				&nbsp;&nbsp;&nbsp;
				<A href="javascript:void(0);" onclick='ShowReport("<%=pdfSmallCommunityNeedsUrl%>")'><pdk-html:img page="/images/pdf.jpg" alt="Small Community Needs Form - PDF Version" border="0"/></A>
			</TD>
		</TR>
		</logic:equal>
		<logic:equal name="isFeedbackFacility" value="true">
		<tr>
			<td><FONT class=PortletText1>Feedback Fact Sheet</FONT></td>
			<td align="right">
 				<A href="javascript:void(0);" onclick='ShowReport("<%=htmlFeedbackFacilityForm%>")'><pdk-html:img page="/images/html.gif" alt="Feedback Fact Sheet - HTML Version" border="0"/></A>
 				&nbsp;&nbsp;&nbsp;
				<A href="javascript:void(0);" onclick='ShowReport("<%=pdfFeedbackFacilityForm%>")'><pdk-html:img page="/images/pdf.jpg" alt="Feedback Fact Sheet - PDF Version" border="0"/></A>
			</td>
		</tr>
		</logic:equal>		
		</logic:equal>
		<logic:notEqual name="showReports" value="Y">
		    <tr>
			   <td colspan="2"><FONT class=PortletText1>Not authorized to view factsheet</FONT></td>		
		    </tr>
		</logic:notEqual>
	</TBODY>
</TABLE>
