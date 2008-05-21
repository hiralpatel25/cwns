<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page contentType="text/html; charset=windows-1252"
   import="gov.epa.owm.mtb.cwns.model.FacilityPermit"
%>   

<SCRIPT type=text/javascript>
    function closepopup(){
  	window.close();
	}
</SCRIPT>	
<TABLE class="PortletText1" border="0" cellpadding="2" cellspacing="3"
	width="100%">
	<TR>
		<TD width="20%">
			<FONT size="4"> <STRONG> NPDES Permit Number:</STRONG> </FONT>
		</TD>
		<TD width="55%">
		   <bean:write name="permit" property="efNpdesPermitNumber"/>
		</TD>
	</TR>
	<TR>
		<TD width="10%">
		   <pdk-html:checkbox name="permit" property="inactiveCode" value="A" disabled="true"></pdk-html:checkbox>
			<STRONG> Active </STRONG>
		</TD>
		<TD>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD width="10%">
			<STRONG> NPDES Facility Name </STRONG> :
		</TD>
		<TD width="55%">
		    <bean:write name="permit" property="facilityName"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Receiving Waters </STRONG> :
		</TD>
		<TD>
		    <bean:write name="permit" property="receivingWaters"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> City </STRONG> :
		</TD>
		<TD>
		    <bean:write name="permit" property="facilityLocationCity"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Latitude </STRONG> :
		</TD>
		<TD>
		    <bean:write name="permit" property="latitudeDecimalDegree"/>&nbsp;
		    <bean:write name="permit" property="latitudeDirection"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Longitude </STRONG> :
		</TD>
		<TD>
		    <bean:write name="permit" property="longitudeDecimalDegree"/>&nbsp;
		    <bean:write name="permit" property="longitudeDirection"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Location&nbsp;Description&nbsp; </STRONG> :
		</TD>
		<TD>
		    <logic:present name="permit" property="locationDescriptionRef">
		       <bean:write name="permit" property="locationDescriptionRef.name"/>
		    </logic:present>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Scale </STRONG> :
		</TD>
		<TD>
		    <bean:write name="permit" property="scale"/>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Method </STRONG> :
		</TD>
		<TD>
		    <logic:present name="permit" property="horizontalCllctnMethodRef">
		       <bean:write name="permit" property="horizontalCllctnMethodRef.name"/>
		    </logic:present>
		</TD>
	</TR>
	<TR>
		<TD>
			<STRONG> Datum </STRONG> :
		</TD>
		<TD>
		    <logic:present name="permit" property="horizontalCoordntDatumRef">
		       <bean:write name="permit" property="horizontalCoordntDatumRef.name"/>
		    </logic:present>
	    </TD>
	</TR>
	<logic:equal name="isLocalUser" value="false">
	<TR>
		<TD colspan="6">
			<TABLE class="PortletText1" cellspacing="1" cellpadding="1"
				width="100%">
				<TR>
		          <TD>
			        <FONT size="4"> <STRONG> Facilities Associated with this Permit</STRONG> </FONT>
		          </TD>
	            </TR>
				<TR>
			      <TD colspan="6">
				    
			      </TD>
		        </TR>
				<TR class="PortletHeaderColor">
                  <TD class="PortletHeaderText" align="center">
			         CWNS Number
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Facility Name
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Review Status
		          </TD>
		          <TD class="PortletHeaderText" align="center">
			         Use Permit Data
		          </TD>
               </TR>
               <logic:iterate id="facPermit" name="facPermits" type="FacilityPermit">
                 <TR >
                  <TD align="center">
			         <bean:write name="facPermit" property="facility.cwnsNbr"/>
		          </TD>
		          <TD align="center">
			         <bean:write name="facPermit" property="facility.name"/>
		          </TD>
		          <TD align="center">
			         <bean:write name="facPermit" property="facility.reviewStatusRef.name"/>
		          </TD>
		          <TD align="center">
			         <bean:write name="facPermit" property="usedForFacilityLocatnFlag"/>
		          </TD>
               </TR>
               </logic:iterate>
            </TABLE>
        </TD>
    </TR>
    </logic:equal>    
    <TR>
	   <TD>
	     <input type="Button" name="Close" onclick="javascript:closepopup()" value="Close">
	   </TD>
	</TR>
</TABLE>	
