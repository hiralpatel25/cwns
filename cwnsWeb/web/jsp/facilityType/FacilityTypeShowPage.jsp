<%@ taglib uri="/WEB-INF/pdk-struts-html.tld" prefix="pdk-html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c-1_0.tld" prefix="c"%>

<%@page contentType="text/html; charset=windows-1252"
	import="oracle.portal.provider.v2.render.PortletRenderRequest"
	import="oracle.portal.provider.v2.http.HttpCommonConstants"
	import="oracle.portal.provider.v2.PortletDefinition"
	import="oracle.portal.provider.v2.ParameterDefinition"
	import="gov.epa.owm.mtb.cwns.common.struts.CWNSStrutsUtils"
	import="oracle.portal.provider.v2.render.PortletRendererUtil"
	import="gov.epa.owm.mtb.cwns.facilityType.DeleteDataAreaHelper"
	%>

<%
	PortletRenderRequest prr = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String listboxurl = url +"/javascript/listbox.js";
    String validationurl = url +"/javascript/validation.js";
    String ajaxurl = url +"/javascript/prototype.js";
    String deleteActionUrl = CWNSStrutsUtils.createHref(request,"deleteFacilityType.do");
    String editActionUrl = CWNSStrutsUtils.createHref(request,"editFacilityType.do");
    String addActionUrl = CWNSStrutsUtils.createHref(request,"addFacilityType.do");
    String facilityTypeParam = PortletRendererUtil.portletParameter(prr, "facilityTypeId");
    String confirmDeleteParam = PortletRendererUtil.portletParameter(prr, "confirmDelete");
%>

<script type="text/javascript" language="JavaScript" src="<%=listboxurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=validationurl %>"></script>
<script type="text/javascript" language="JavaScript" src="<%=ajaxurl %>"></script>
<logic:present name="facilityTypeForm" property="mode">
<logic:notEqual name="facilityTypeForm" property="mode" value="list">
	<SCRIPT type="text/javascript">
    
    //set all the changes
    var changes = new Array();
    <logic:iterate id="change" name="changeTypes" indexId="ichange">
       changes[<bean:write name="change" property="changeTypeId"/> +'']='<bean:write name="change" property="name"/>';
    </logic:iterate>		
    
    //set all change rules
    var changeRules = new Array();
	<logic:iterate id="changeId" name="changeTypeRules" indexId="ind">
		changeRules[<%=ind%>]=new Array();
		changeRules[<%=ind%>][0] = <bean:write name="changeId" property="id.changeTypeId1" />;
        changeRules[<%=ind%>][1] = <bean:write name="changeId" property="id.changeTypeId2" />; 
	</logic:iterate>	
	
    var facilityTypeChangeRules = new Array();
	<logic:iterate id="facilityTypeChangeId" name="facilityTypeChangeRules" indexId="indRules">
		facilityTypeChangeRules[<%=indRules%>]=new Array();
		facilityTypeChangeRules[<%=indRules%>][0] = <bean:write name="facilityTypeChangeId" property="id.facilityTypeId" />;
        facilityTypeChangeRules[<%=indRules%>][1] = '<bean:write name="facilityTypeChangeId" property="presentFutureCode" />'; 
        facilityTypeChangeRules[<%=indRules%>][2] = <bean:write name="facilityTypeChangeId" property="id.changeTypeId" />; 
	</logic:iterate>	
	
	
	//set associated allowable changes for a given change
	var changeTypes = new Array();
	for(var i=0;i<changeRules.length;i++){
	    var changeType = changeTypes[changeRules[i][0]+''];
	    if(typeof(changeType)=="undefined"){ 
	        //alert('creating new array for ' + changeRules[i][0]);
	        changeType = new Array();
	    }	
	    changeType[changeType.length]=changeRules[i][1];
	    changeTypes[changeRules[i][0]+'']=changeType;
	}
	
	//Change Types based on facilityType and Status
	var facilityTypeChangeTypes = new Array();
	for(var i=0;i<facilityTypeChangeRules.length;i++){
	    var facilityTypeChangeType = facilityTypeChangeTypes[facilityTypeChangeRules[i][0]+'-'+facilityTypeChangeRules[i][1]];
	    if(typeof(facilityTypeChangeType)=="undefined"){ 
	        //alert('creating new array for ' + changeRules[i][0]);
	        facilityTypeChangeType = new Array();
	    }	
	    facilityTypeChangeType[facilityTypeChangeType.length]=facilityTypeChangeRules[i][2];
	    facilityTypeChangeTypes[facilityTypeChangeRules[i][0]+ '-' + facilityTypeChangeRules[i][1]]=facilityTypeChangeType;
	}
	
	function setChangeTypes(form){
   		var selectedList=form['selectedChanges'];
   		clearAll(selectedList);
	    filterAvailableRules (form, 'availableChangesId','selectedChanges', 'facilityType', 'facilityTypeStatusId');
	}

	function setNPSStatus(form){
	    //check if treatmentPlant
	    var facilityType=form['facilityType'].value;
	    var npsStatusDiv=document.getElementById('npsStatusDiv');
	    if(facilityType>10 && facilityType < 22){
	       npsStatusDiv.style.display='none';   //***temp blocked for now
	    }else{
	       npsStatusDiv.style.display='none'; 
	    }
	
	}

	function setTPType(form){
	    //check if treatmentPlant
	    var facilityType=form['facilityType'].value;
	    var tpTypeDiv=document.getElementById('tpType');
	    if(facilityType=='1'){
	       tpTypeDiv.style.display='block';
	       var status= getRadioValue(form['facilityTypeStatusId']); 
	       if(status=='F'){
	          setClearAndDisableRadioValue(form['presentTPTypeId']);
	          setEnabledRadioValue(form['projectedTPTypeId'],'F');
	       }else if(status=='P'){
	          setEnabledRadioValue(form['presentTPTypeId'],'P');
	       	  setClearAndDisableRadioValue(form['projectedTPTypeId']);	       
	       }else{
	          setEnabledRadioValue(form['presentTPTypeId'],'P');
	          setEnabledRadioValue(form['projectedTPTypeId'],'F');
	       }
	    }else{
	       // whole area should be blocked
	       tpTypeDiv.style.display='none';
	    }
	}
	
	function clearAddForm(form){
	    var selectedList=form['selectedChanges'];
   		clearAll(selectedList);
	    var availableList=form['availableChangesId'];
   		clearAll(availableList);  
   		var radioObj = form['facilityTypeStatusId'];
   		radioObj[2].checked =true;		
	    form['facilityType'].value ='';
	}
    
	function filterAvailableRules (form, availableName, selectedName, facilityTypeName, statusName){
   		var availableList=form[availableName];	   
   		var selectedList=form[selectedName];
   		var status= getRadioValue(form[statusName]);   		   
   		var facilityType=form[facilityTypeName].value;   		
   		if(facilityType == '' && status == ''){
   		   //alert("Select Facility Type and Status");
   		   return false;
   		}
   		
   	    //get available list
   	    var allowedChanges= facilityTypeChangeTypes[facilityType + '-' + status];
    	var availableIds = new Array();
    	if(selectedList != undefined && selectedList.length > 0){ 
    	  for(i=0; i< selectedList.length; i++){
    	    var validChangeType = changeTypes[selectedList.item(i).value];
    	    
    	    //var vtypesStr ='';
    	    //for(var dj=0;dj<validChangeType.length;dj++){
    	    //    vtypesStr = vtypesStr + '-' +validChangeType[dj];
    	    //}
    	    //alert('valid changes for ' + selectedList.item(i).value + '-->' + vtypesStr);
    	        	    
    	    if(validChangeType != undefined){
    	     if(i==0){
    	        availableIds = validChangeType;
    	     }else{
    	        var intersectionArray = new Array();
    	        ll=0;
    	       	 for(var j=0;j<validChangeType.length;j++){
    	       	    for(var k=0;k<availableIds.length;k++){
    	        	   if(validChangeType[j]==availableIds[k]){
    	        	       intersectionArray[ll]=availableIds[k];
    	        	       //alert('available Id: ' + ll + '--> ' + intersectionArray[ll]);
    	        	       ll++;
    	        	   }
    	        	}   
    	       	}     	       	
    	       	availableIds =intersectionArray;
    	       	//alert('Number of available changes:' + intersectionArray.length);
    	     } 
    	   } 
    	  }
    	  //narrow the available list based on availableIds + further narrow down based on vailable change types based on facility type and status
    	  clearAll(availableList);
    	  var aLength = availableIds.length;
    	  if(aLength != undefined){
    	   var opt = 0;
    	   for(var m=0;m<availableIds.length;m++){
    	     var bLength = allowedChanges.length;
    	     if(bLength != undefined){
    	      for(var mc=0;mc<allowedChanges.length;mc++){
    	        if(availableIds[m]==allowedChanges[mc]){
				   var option = new Option(changes[availableIds[m]+''], availableIds[m]);
    	     	   availableList.options[opt] = option;
    	     	   opt++;
    	        }
    	      }
    	     }else{
    	       break;
    	     }   
    	   }
    	  }
    	  //remove any that don't apply    	  
    	}else{
    	    clearAll(availableList);    
    	    if(allowedChanges != undefined){	    
    	      for(var m=0;m<allowedChanges.length;m++){
    	        var option = new Option(changes[allowedChanges[m]+''], allowedChanges[m]);
    	        availableList.options[m] = option;    	     
    	      }
    	    }  
    	}      	
    	selectNone(selectedList,availableList);
	}
	
	function getRadioValue(radioObj){
	  var radioLength = radioObj.length;
	  if(radioLength != undefined){   
	    for(var i = 0; i < radioLength; i++) {
		 if(radioObj[i].checked) {
			return radioObj[i].value;
		 }
	    }
	  }  
	}
	
	function setClearAndDisableRadioValue(radioObj){
	  var radioLength = radioObj.length;
	  if(radioLength != undefined){   
	    for(var i = 0; i < radioLength; i++) {
	    radioObj[i].checked=false;
	    radioObj[i].disabled=true;
	    radioObj[i].style.color='#000000';
	    radioObj[i].style.background='#cccccc';
	    }
	  }  
	}
	
	function setEnabledRadioValue(radioObj,status){
	  var radioLength = radioObj.length;
	  var c;
	  
	  if(status=='F'){
	    c = '<bean:write  name="facilityTypeForm"  property="projectedTPType"/>';
	  }else if(status=='P'){
	  c = '<bean:write  name="facilityTypeForm"  property="presentTPType"/>';
	  }
	  	  
	  if(radioLength != undefined){   
	    for(var i = 0; i < radioLength; i++) {
	         radioObj[i].disabled=false;
	         if(c == radioObj[i].value){
	           radioObj[i].checked=true;
	         }  
	         radioObj[i].style.color='';
	         radioObj[i].style.background='';
	    }
	  }  
	}
	
	function formValid(form){
	  if(validateFacilityTypeForm(form)){
	     if (!bCancel){
	     	var selectedList=form['selectedChanges'];
	     	if(selectedList == undefined || selectedList.length < 1){
	        	alert('<bean:message key="prompt.selectedChanges"/>'); 
	        	return false;
	     	}
	     }
	     selectAll(form, 'selectedChanges');
	     selectAll(form, 'availableChangesId');
	     return true;
	  }
	  return false;
	}
	
	function clearAll(list){
	   var len = list.length -1;
	   for(i=len; i>=0; i--){
		  list.removeChild(list.item(i));
       }
	}

	function submitForm(deleteStatus){
	   var form =document.getElementById('facilityTypeFormId');
	   if(formValid(form)){
	      var confirmDelete = form["confirmDeleteId"];
	      if(confirmDelete !=null){
	         confirmDelete.value=deleteStatus;
	      }
	      form.submit();
	   }
	   
	}
	    
	</SCRIPT>
 </logic:notEqual>	
</logic:present>

<script type="text/javascript">

	


</script>

<BR>

<logic:equal name="isFacility" value="false">
   <STRONG> <FONT size="4" face="arial"> Project Type </FONT></STRONG>
</logic:equal>
<logic:notEqual name="isFacility" value="false">
   <STRONG> <FONT size="4" face="arial"> Facility Type </FONT></STRONG>
</logic:notEqual>

<logic:present name="errors">
    <div class="error"> 
      The following errors occured:
    <logic:iterate id="error" name="errors">
       <bean:write name="error" property="value"/>
    </logic:iterate>
    </div>
</logic:present>
<logic:notPresent name="errors">
  <logic:present name="warnings">
      <div class="error"> 
      This action will:
    <logic:iterate id="warning" name="warnings">
       <bean:write name="warning" property="value"/>
    </logic:iterate>
    Are you sure you want to proceed?
    </div>
  </logic:present>
</logic:notPresent>

<logic:present name="warnDataAreaDelete"> 
	<div class="MessageZone">
	     <table width="100%" style="border: thin; border-style: dashed; border-color: red" cellpadding="0" cellspacing="0">
	     	<tr>
	     		<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/confirmEclam.gif" alt="Confirm to proceed with Data deletion" border="0"/>
	     		</td>
	     		<td class="ErrorText" align="left" valign="middle">
	     		 The following data will be deleted:<br/>
    				<logic:iterate id="dataArea" name="warnDataAreaDelete"  property="dataAreas">
       					&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;<bean:write name="dataArea" property="value"/><br/>
    				</logic:iterate>    
    				<bean:define id="facTypeId"><bean:write name="warnDataAreaDelete" property="facilityType"/></bean:define>
    			 Are you sure you want to proceed?
    			 <A href="<%=deleteActionUrl%>&<%=facilityTypeParam %>=<%=facTypeId%>&<%=confirmDeleteParam %>=Y">Yes</A>&nbsp;&nbsp;
    			 <A href="<%=deleteActionUrl%>&<%=facilityTypeParam %>=<%=facTypeId%>&<%=confirmDeleteParam %>=N">No</A></br>
	     		</td>
	     	</tr>
	     </table>
	</div>
</logic:present>


<TABLE cellspacing="2" cellpadding="3" border="0" width="100%" class="PortletText1">
	<TR class="PortletHeaderText" align="center">
		<TD class="PortletHeaderColor">
			Type
		</TD>
		<TD class="PortletHeaderColor">
			Present
		</TD>
		<TD class="PortletHeaderColor">
			Projected
		</TD>
		<TD class="PortletHeaderColor">
			Change
		</TD>
	    <logic:present name="updatable">
		      <logic:equal name="updatable" value="true">
				<TD class="PortletHeaderColor">
					Edit		
				</TD>
				<TD class="PortletHeaderColor">
					Delete
				</TD>		
		      </logic:equal>
		      <logic:notEqual name="updatable" value="true">
		        <logic:equal name="isFeedback" value="true">
		          <TD class="PortletHeaderColor">
					 Delete
				  </TD>
		        </logic:equal>
		      </logic:notEqual>
		</logic:present>			
	</TR>
	<logic:present name="facilityTypes">
	<c:set var="i" value="1" />
	<logic:iterate id="fType" name="facilityTypes">
	    <logic:present name="selectedFacilityTypeId">
	        <bean:define id="sId" name="selectedFacilityTypeId"></bean:define>
	        <logic:equal name="fType" property="facilityTypeRef.facilityTypeId" value="<%=sId %>">
	           <c:set var="class" value="RowHighlighted"/>
	        </logic:equal>	    	
			<logic:notEqual name="fType" property="facilityTypeRef.facilityTypeId" value="<%=sId %>">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor"/>   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>
		    </logic:notEqual>	        
	    </logic:present>
	    <logic:notPresent name="selectedFacilityTypeId">
				<c:choose>
					<c:when test='${i%2=="0"}'>
						<c:set var="class" value="PortletSubHeaderColor"/>   
					</c:when>
					<c:otherwise>
						<c:set var="class" value="" />
					</c:otherwise>
				</c:choose>	    
	    </logic:notPresent>	 
		<c:set var="i" value="${i+1}" />
		<TR class="<c:out value="${class}"/>">	
			<TD>
				<bean:write name="fType" property="facilityTypeRef.name"/>
			</TD>
			<TD align="center">
			    <logic:equal name="fType" property="presentFlag" value="Y">
			    	<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			    </logic:equal>				
			</TD>
			<TD align="center">
			    <logic:equal name="fType" property="projectedFlag" value="Y">
			    	<pdk-html:img page="/images/checkmark.gif" alt="Checked" border="0"/>
			    </logic:equal>				
			</TD>
			<TD>
			    <logic:iterate id="fTypeChange" name="fType" property="facilityTypeChanges">   
			        <bean:write name="fTypeChange" property="changeTypeRef.name"/>
			        <br>
			    </logic:iterate>
			</TD>
		    <logic:present name="updatable">
		      <logic:equal name="updatable" value="true">
		        <bean:define id="facilityTypeId"><bean:write name="fType" property="facilityTypeRef.facilityTypeId"/></bean:define>  
				<TD align="center">
					<A href="<%=editActionUrl%>&<%=facilityTypeParam %>=<%=facilityTypeId%>"><pdk-html:img page="/images/edit.gif" alt="edit" border="0"/></A>
				</TD>
				<TD align="center">
				    <logic:equal name="isFeedback" value="true">
				         <bean:define id="dUrl">location.href='<%=deleteActionUrl%>&<%=facilityTypeParam %>=<%=facilityTypeId%>';</bean:define>
				         <logic:equal name="fType" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" checked="checked" onclick="<%=dUrl%>">
				         </logic:equal>
				         <logic:notEqual name="fType" property="feedbackDeleteFlag" value="Y">
				            <input type="checkbox" name="delete" onclick="<%=dUrl%>">
				         </logic:notEqual>
				    </logic:equal> 
				    <logic:notEqual name="isFeedback" value="true">
					    <A href="<%=deleteActionUrl%>&<%=facilityTypeParam %>=<%=facilityTypeId%>"><pdk-html:img page="/images/delete.gif" alt="delete" border="0"/></A>				    
				    </logic:notEqual> 
				</TD>					      
		      </logic:equal>
		      <logic:notEqual name="updatable" value="true">
		        <logic:equal name="isFeedback" value="true">
		        <TD align="center">
		           <logic:equal name="fType" property="feedbackDeleteFlag" value="Y">
				       <input type="checkbox" name="delete" checked="checked" disabled="disabled" >
				   </logic:equal>
				   <logic:notEqual name="fType" property="feedbackDeleteFlag" value="Y">
				       <input type="checkbox" name="delete" disabled="disabled">
				   </logic:notEqual>
				   </TD>
				</logic:equal>
		      </logic:notEqual>
		      
			</logic:present>	
		</TR>	
	</logic:iterate>
	</logic:present>   
</TABLE>

<logic:present name="updatable">
<logic:equal name="updatable" value="true">

<logic:present name="availablefacilityTypes">
<logic:notEmpty name="availablefacilityTypes">
 <logic:equal name="facilityTypeForm" property="mode" value="list">
  <DIV class="PortletText1">
	&nbsp;&nbsp;<A href="<%=addActionUrl%>">Add Type</A>
  </DIV>	
 </logic:equal>
</logic:notEmpty>
</logic:present>

<logic:present name="facilityTypeForm" property="mode">
<logic:notEqual name="facilityTypeForm" property="mode" value="list">
<DIV style="background-color: #cccccc;padding: 5"  class="PortletText1">
	<pdk-html:form name="facilityTypeForm" method="get" styleId="facilityTypeFormId" type="org.apache.struts.action.DynaActionForm" action="saveFacilityType.do" onsubmit="return formValid(this);">
	<STRONG><FONT size="2">Add/Edit Type</FONT></STRONG><br><br>
	<pdk-html:errors/><br>
	<STRONG><span class="required">*</span>Type</STRONG>
	
	<logic:equal name="facilityTypeForm" property="mode" value="edit">
	<logic:present name="warnDataAreaDeleteOnEdit"> 
		<div class="MessageZone">
	     <table width="100%" style="border: thin; border-style: dashed; border-color: red" cellpadding="0" cellspacing="0">
	     	<tr>
	     		<td align="center" valign="middle" width="40">
	     			<pdk-html:img page="/images/confirmEclam.gif" alt="Confirm to proceed with Data deletion" border="0"/>
	     		</td>
	     		<td class="ErrorText" align="left" valign="middle">
	     		 The following data will be deleted:<br/>
    				<logic:iterate id="dataArea" name="warnDataAreaDeleteOnEdit"  property="dataAreas">
       					&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;<bean:write name="dataArea" property="value"/><br/>
    				</logic:iterate>    
    			 Are you sure you want to proceed?
    			 <A href="javascript:void(0);" onclick="submitForm('Y');">Yes</A>&nbsp;&nbsp;
    			 <A href="javascript:void(0);" onclick="submitForm('N');">No</A></br>
	     		</td>
	     	</tr>
	     </table>
		</div>
	</logic:present>		
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="facilityTypeForm"  styleId="facilityType" property="facilityType"/>
	  <pdk-html:text name="facilityTypeForm"  property="mode" styleId="modeId"/>
	  <pdk-html:text name="facilityTypeForm" property="confirmDelete" styleId="confirmDeleteId"/>
	 </div>  
	 : <bean:write  name="facilityTypeRef"  property="name"/><br></br>
	</logic:equal>
	
	<logic:notEqual name="facilityTypeForm" property="mode" value="edit">
	 <div style="DISPLAY: none;"> 
	  <pdk-html:text name="facilityTypeForm"  property="mode" styleId="modeId"/>
	 </div>  
	<pdk-html:select name="facilityTypeForm"  styleId="facilityType" property="facilityType" onchange="setChangeTypes(this.form); setTPType(this.form); setNPSStatus(this.form);">
	     <option value="" selected="selected"></option>
	   	 <pdk-html:options collection="availablefacilityTypes" property="facilityTypeId" labelProperty="name"/>
	</pdk-html:select><br><br>
	</logic:notEqual>
	
	<STRONG>Status</STRONG><br>
	  <pdk-html:radio name="facilityTypeForm" property="status" value="P" styleId="facilityTypeStatusId" onclick="setChangeTypes(this.form); setTPType(this.form);">Present &nbsp;</pdk-html:radio>
	  <pdk-html:radio name="facilityTypeForm" property="status" value="F" styleId="facilityTypeStatusId" onclick="setChangeTypes(this.form); setTPType(this.form);">Projected &nbsp;</pdk-html:radio>
	  <pdk-html:radio name="facilityTypeForm" property="status" value="B" styleId="facilityTypeStatusId" onclick="setChangeTypes(this.form);setTPType(this.form);">Present and Projected</pdk-html:radio>
	<BR><BR>
	
	<div id="tpType" style="DISPLAY: block;">
	<TABLE border="0" width="500" class="PortletText1">
		<TR>
			<TD>
				<STRONG>Present Treatment Plant Type</STRONG><br>
				<TABLE width="225" class="PortletText1" style=" border:thin; border-style:ridge;">
					<TR>
						<TD>
				  			<pdk-html:radio style="background-style:solid;" name="facilityTypeForm" property="presentTPType" value="M" styleId="presentTPTypeId">Mechanical &nbsp;</pdk-html:radio>
				  			<pdk-html:radio name="facilityTypeForm" property="presentTPType" value="L" styleId="presentTPTypeId">Lagoon &nbsp;</pdk-html:radio>
				  			<pdk-html:radio name="facilityTypeForm" property="presentTPType" value="N" styleId="presentTPTypeId">None</pdk-html:radio>			
	  					</TD>
	  				</TR>
	  			</TABLE>
			</TD>
			<TD>
			  &nbsp;
			</TD>
			<TD>
				<STRONG>Projected Treatment Plant Type</STRONG><br>
				<TABLE width="225" class="PortletText1" style="border:thin; border-style:ridge;">
					<TR>
						<TD>
				  			<pdk-html:radio name="facilityTypeForm" property="projectedTPType" value="M" styleId="projectedTPTypeId">Mechanical &nbsp;</pdk-html:radio>
				  			<pdk-html:radio name="facilityTypeForm" property="projectedTPType" value="L" styleId="projectedTPTypeId">Lagoon &nbsp;</pdk-html:radio>
				  			<pdk-html:radio name="facilityTypeForm" property="projectedTPType" value="N" styleId="projectedTPTypeId">None</pdk-html:radio>			
	  					</TD>
	  				</TR>
	  			</TABLE>
			</TD>
		</TR>
	</TABLE>	
	<br>
	</div>
	
	<div id="npsStatusDiv" style="DISPLAY: none;">	
	   <STRONG>NPS Status</STRONG>&nbsp;&nbsp;&nbsp;
	   <pdk-html:select name="facilityTypeForm"  styleId="npsStatusId" property="npsStatus">
	     <pdk-html:option value="PI">Phase I</pdk-html:option>	     
	     <pdk-html:option value="PII">Phase II</pdk-html:option>
	     <pdk-html:option value="NT">Non-Traditional</pdk-html:option>
	   </pdk-html:select>	    
	   <br><br>
	</div>
		
	<STRONG>Changes</STRONG>
	<TABLE border="0" width="400" class="PortletText1">
		<TR>
			<TD>
				Available Changes
			</TD>
			<TD></TD>
			<TD>
				 <span class="required">*</span>Selected Changes 
			</TD>
		</TR>
		<TR>
			<TD width="50%">
			   <%--
			    <pdk-html:select name="facilityTypeForm"  styleId="availableChangesId" property="availableChanges" multiple="true" size="8" style="width:250;">
	             	<pdk-html:options collection="changeTypes" property="changeTypeId" labelProperty="name"/>
	          	</pdk-html:select>
				 --%> 
	          	 
	          	<pdk-html:select name="facilityTypeForm"  styleId="availableChangesId" property="availableChanges"  size="8" style="width:250;">
	          	   	<logic:present name="availableChangeTypeRefs">
						<pdk-html:options collection="availableChangeTypeRefs" property="changeTypeId" labelProperty="name"/>	          	   		
	          	   	</logic:present>
	          	</pdk-html:select>
	          	
			</TD>
			<TD width="20">
				<INPUT type="button" name="remove" value="&lt;" onclick="delAttribute(this.form, 'availableChangesId','selectedChanges');filterAvailableRules (this.form, 'availableChangesId','selectedChanges', 'facilityType', 'facilityTypeStatusId')"/>
				<INPUT type="button" name="add" value="&gt;" onclick="addAttribute(this.form, 'availableChangesId','selectedChanges');filterAvailableRules (this.form, 'availableChangesId','selectedChanges', 'facilityType', 'facilityTypeStatusId')"/>
			</TD>
			<TD width="150">
			    <pdk-html:select name="facilityTypeForm"  styleId="selectedChanges" property="selectedChanges" multiple="true" size="8" style="width:250;">
			        <logic:present name="selectedChangeTypeRefs">
			            <pdk-html:options collection="selectedChangeTypeRefs" property="changeTypeId" labelProperty="name"/>
			        </logic:present>
				</pdk-html:select>
			</TD>
		</TR>
		</TABLE><br>
		<pdk-html:submit value="Save"/>
		<pdk-html:cancel value="Cancel"/> 
	</pdk-html:form>	
	<script type="text/javascript">
	     var ftform =document.getElementById('facilityTypeFormId');
	     filterAvailableRules (ftform, 'availableChangesId','selectedChanges', 'facilityType', 'facilityTypeStatusId'); 
	     setTPType(ftform);
	     setNPSStatus(ftform);
	</script>
	<pdk-html:javascript formName="facilityTypeForm" staticJavascript="false"/>		
	</DIV>
 </logic:notEqual>
 </logic:present>
 
</logic:equal>
</logic:present>