function delAttribute(form, availableName, selectedName){
   var availableList=form[availableName];	   
   var selectedList=form[selectedName];	   
   var selIndex = selectedList.selectedIndex;
   if(selIndex < 0)
      return;
   if(selectedList.options.item(selIndex).value!='0'&&selectedList.options.item(selIndex).value.charAt(0)!='H') {
     availableList.appendChild(selectedList.options.item(selIndex));
   } else {
     selectedList.removeChild(selectedList.options.item(selIndex));
   }
   selectNone(selectedList,availableList);
}

function addAttribute(form, availableName, selectedName){
   var availableList=form[availableName];	   
   var selectedList=form[selectedName];	   
   var addIndex = availableList.selectedIndex;
   if(addIndex < 0)
      return;
   selectedList.appendChild( 
      availableList.options.item(addIndex));
   selectNone(selectedList,availableList);
}

function setTop(top){
	document.getElementById
      ('someLayer').style.top = top;
}

function setLayerTop(lyr,top){
	lyr.style.top = top;
}

function getSize(list){
    /* Mozilla ignores whitespace, 
       IE doesn't - count the elements 
       in the list */
    var len = list.childNodes.length;
    var nsLen = 0;
    //nodeType returns 1 for elements
    for(i=0; i<len; i++){
        if(list.childNodes.item(i).nodeType==1)
            nsLen++;
    }
    return nsLen;
}

function delAll(form, availableName, selectedName){
   var availableList=form[availableName];	   
   var selectedList=form[selectedName];	   
    var len = selectedList.length -1;
    for(i=len; i>=0; i--){
    	if (selectedList.item(i).value!='0')
	        availableList.appendChild(selectedList.item(i));
	    else
		    selectedList.removeChild(selectedList.item(i));
    }
    selectNone(selectedList,availableList);
}

function addAll(form, availableName, selectedName){
   var availableList=form[availableName];	   
   var selectedList=form[selectedName];
   
    var len = availableList.length -1;
    for(i=len; i>=0; i--){
        selectedList.appendChild(availableList.item(i));
    }
    selectNone(selectedList,availableList);
}

function selectAll(form, listName) {
	var list=form[listName];
	for (i=0;i<list.length;i++) {
		list.options[i].selected=true;
	}
	return true;
}

function selectNone(list1, list2) {
	list1.selectedIndex = -1;
    list2.selectedIndex = -1;
    return true;
}

function arrangeList(list, down) {
	sl = list.selectedIndex;
	if (sl != -1) {
		oText = list.options[sl].text;
		oValue = list.options[sl].value;
		if (oValue == "-1") {		
			return false;
		}	
		if (sl > 0 && down == 0) {
			list.options[sl].text = list.options[sl-1].text;
			list.options[sl].value = list.options[sl-1].value;
			list.options[sl-1].text = oText;
			list.options[sl-1].value = oValue;
			list.selectedIndex--;
		} else if (sl < list.length-1 && down == 1 && list.options[sl+1].value != "-1") {
			list.options[sl].text = list.options[sl+1].text;
			list.options[sl].value = list.options[sl+1].value;
			list.options[sl+1].text = oText;
			list.options[sl+1].value = oValue;
			list.selectedIndex++;
		}
	} 
	return false;
}

function addPageBreak(form, selectedName) {
   var selectedList=form[selectedName];
   selectedList[getSize(selectedList)]=new Option('--Page Break--','0');
   return true;
}

function addHeading(form, inputName, selectedName) {
   var selectedList=form[selectedName];
   var headingName=form[inputName];
   if (headingName.value=='') {
     alert('Section Heading cannot be blank.');
     return false;
   }
   selectedList[getSize(selectedList)]=new Option('['+headingName.value+']*','H'+headingName.value);
   return true;
}

function findDuplicate(selectObjectSource, destvalue) 
{
    for (var i = 0; i < selectObjectSource.length; i++) 
    {
          if ((selectObjectSource.options[i].value == destvalue)) {
            return true;
          }
    }
            return false;
}

function addOptionToSelectedList(selectedList, str_value, str_name) 
{

    // check to see if there are duplicates

    if (!findDuplicate(selectedList, str_value)) 
    {
		for(var ii = selectedList.options.length; ii > 0; ii--)
		{
			selectedList.options[ii] = new Option(selectedList.options[ii-1].text, selectedList.options[ii-1].value);
		}
		
        selectedList.options[0] = new Option(str_name, str_value);

         return true;

    }
            return false;      
}

function moveOptions(srcListName, targetListName)
{
   //alert("moveOptions");
   var SelectFrom=window.document.getElementById(srcListName);	   
   var SelectTo=window.document.getElementById(targetListName);
   //alert(SelectFrom.options.length);
   
            for (var Current=0;Current < SelectFrom.options.length;Current++) 
            {
              if (SelectFrom.options[Current].selected) {

                        var selval = SelectFrom.options[Current].value;

                        var seltext = SelectFrom.options[Current].text;     

                        addOptionToSelectedList(SelectTo, selval, seltext);
               }
            }

            deleteFromList(SelectFrom.options);

            //sortOptions(SelectTo.options, 0);
}

function addSelectedOptions(srcListName, targetListName)
{
   var SelectFrom=window.document.getElementById(srcListName);	   
   var SelectTo=window.document.getElementById(targetListName);
   
            for (var Current=0;Current < SelectFrom.options.length;Current++) 
            {
              if (SelectFrom.options[Current].selected) 
              {
                        var selval = SelectFrom.options[Current].value;

                        var seltext = SelectFrom.options[Current].text;     

                        addOptionToSelectedList(SelectTo, selval, seltext);
              }
            }
 }

function mergeOptions(srcListName, targetListName, mergedListName)
{
   var SelectFrom=window.document.getElementById(srcListName);	   
   var SelectTo=window.document.getElementById(targetListName);
   var mergedList=window.document.getElementById(mergedListName);
   
   mergedList.options.length = 0;
   
    for (var Current=0;Current < SelectFrom.options.length;Current++) 
    {
       var selval = SelectFrom.options[Current].value;
       var seltext = SelectFrom.options[Current].text;     

       if(findDuplicate(SelectTo, selval))
       {
       		addOptionToSelectedList(mergedList, selval, seltext);
       }
    }
 }

function deleteFromList(selectObject) {

  var total = selectObject.length;

  for (var i = 0 ; i < total ; i++) 
  {
		if (selectObject.selectedIndex > -1) {
        	selectObject[selectObject.selectedIndex] = null;   
        }

  }
}

function deleteSrcSelectedFromTarget(srcListName, targetListName, checkSelected) 
{
   var SelectFrom=window.document.getElementById(srcListName);	   
   var SelectTo=window.document.getElementById(targetListName);
   
            for (var Current=0;Current < SelectFrom.options.length;Current++) 
            {
              if (!checkSelected || SelectFrom.options[Current].selected) 
              {

                      var selval = SelectFrom.options[Current].value;

					  var total = SelectTo.length;
					
					  for (var i = total - 1; i >=0  ; i--) 
					  {
					
					            if (SelectTo.options[i].value == selval) 
					            {
					                        SelectTo.options[i] = null;   
					            }
					  }      
               }
            }
 }

function sortOptions(selectBox, startIndex){

            var index = 0;

            var lastIndex = selectBox.length - 1;

            var tempOption = new Option();

            var options = selectBox.options;

            var x = startIndex;

            while(x <= lastIndex){

                        for(index = lastIndex; index > x; index--){

                                    if(options[index].text.toLowerCase() < options[index - 1].text.toLowerCase()){

                                                tempOption = new Option(options[index].text, options[index].value);

                                                options[index] = new Option(options[index - 1].text, options[index - 1].value);

                                                options[index - 1] = tempOption;

                                    }

                        }

                        x++;

            }
}

