// This function checks the mouse event
function checkEvent(e, id, text) 
{
 if (!e) 
 {
    e = window.event;
 }
 if (e.target) 
 {
    targ = e.target;
 }
 else if (e.srcElement) 
 {
 	targ = e.srcElement;
 }
 showHideToolTip(text, id, e, e.type);
}
// This function shows/hides the tooltip
function showHideToolTip(text, id, e, eType)
{
 var toolTipObj = new Object();
 toolTipObj = document.getElementById(id);
 
 if(toolTipObj == null)
   return;
 
 toolTipObj.className = 'tooltipCWNS';
 toolTipObj.innerHTML = text;
 if(eType == "mouseout")
 {
  toolTipObj.style.display = "none";
 }
 else
 {
 
	 if(text == "")
	   return; 
 
  var tipobj = toolTipObj;
  tipobj.style.display = "inline";

	var offsetxpoint=-60; //Customize x offset of tooltip
	var offsetypoint=20; //Customize y offset of tooltip

	var curX=event.clientX+ietruebody().scrollLeft;
	var curY=event.clientY+ietruebody().scrollTop;
	//Find out how close the mouse is to the corner of the window
	var rightedge=ietruebody().clientWidth-event.clientX-offsetxpoint;
	var bottomedge=ietruebody().clientHeight-event.clientY-offsetypoint;
	
	var leftedge=(offsetxpoint<0)? offsetxpoint*(-1) : -1000;
	
	//if the horizontal distance isn't enough to accomodate the width of the context menu
	if (rightedge<tipobj.offsetWidth)
	{
		//move the horizontal position of the menu to the left by it's width
		tipobj.style.left=ietruebody().scrollLeft+event.clientX-tipobj.offsetWidth+"px";
	}
	else if (curX<leftedge)
	{
		tipobj.style.left="5px";
	}
	else
	{
		//position the horizontal position of the menu where the mouse is positioned
		tipobj.style.left=curX+offsetxpoint+"px";
	}
	
	//same concept with the vertical position
	if (bottomedge<tipobj.offsetHeight)
	{
	  tipobj.style.top=ietruebody().scrollTop+event.clientY-tipobj.offsetHeight-offsetypoint+"px";
	}
	else
	{
	  tipobj.style.top=curY+offsetypoint+"px";
	}  

 }
}

function ietruebody()
{
    return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

