//Functions to perform AJAX calls

function createXMLHttpRequest() {
   try { return new ActiveXObject("Msxml2.XMLHTTP"); } catch (e) {}
   try { return new ActiveXObject("Microsoft.XMLHTTP"); } catch (e) {}
   try { return new XMLHttpRequest(); } catch(e) {}
   alert("XMLHttpRequest not supported");
   return null;
 }
 
 //Function to send a message to the server
 function echo(url, text) {
   var xhr = createXMLHttpRequest();
   xhr.open("GET","/echo/"+text,true);
   xhr.send(null);
 }
 
 function getResponse(url){
   var xhr = createXMLHttpRequest();
   xhr.open("GET",url,false);
   xhr.send(null);
   return xhr.responseXML;
 }