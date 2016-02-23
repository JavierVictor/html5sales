function sendAsyncRequest(url, method, params, callback,fallback) {
	var request = getRequest();
	request.open(method, url, true);
	request.setRequestHeader("Content-Type",	"application/x-www-form-urlencoded;charset=ISO-8859-1");
	request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
	request.callback = callback;
	request.fallback = fallback;
	request.onreadystatechange = function() {
		if (request.readyState == 4){
			if(request.status == 200){
				if(callback)
					callback(request.responseText);
			}else if(fallback){
				fallback();
			}
		}
	};
	request.send(params);
}
function getRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else {
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}
function responseXML(txt) {
	if (window.DOMParser) {
		parser = new DOMParser();
		xmlDoc = parser.parseFromString(txt, "text/xml");
	} else {
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(txt);
	}
	return xmlDoc;
}

function getParameters(form) {
	var params = "";
	for ( var i = 0; i < form.elements.length; i++) {
		var elemento = form.elements[i];

		if (elemento.name == ""
				|| ((elemento.type == "radio" || elemento.type == "checkbox") && !elemento.checked))
			continue;

		params += elemento.name + "=" + encodeURIComponent(elemento.value)
				+ "&";
	}
	return params.length > 0 ? params.substring(0, params.length - 1) : "";
}
function closeBackgroundWindow(id){
	document.saveForm.type.value="ADD";
	document.querySelector("#background").style.display="none";
	document.querySelector("#"+id).style.display="none";
}
function showBackgroundWindow(id){
	document.querySelector("#background").style.display="block";
	document.querySelector("#"+id).style.display="block";
}
function menuHeader(){
	document.querySelector("#navMenuMaintenance").addEventListener("mouseover",function(evt){
		var subMenu = document.querySelector("#subMenu");
		var cells = subMenu.getElementsByTagName("table")[0].getElementsByTagName("td");
		var contextPath = document.querySelector("#contextPath").value;
		cells[0].innerHTML="<a href='"+contextPath+"/admin/usuario.jsp'>USUARIOS</a>";
		cells[1].innerHTML="<a href='"+contextPath+"/admin/client?type=ALLCLIENTS'>CLIENTES</a>";
		cells[2].innerHTML="<a href='"+contextPath+"/admin/product?type=ALLPRODUCTS'>PRODUCTOS</a>";
		cells[3].innerHTML="<a href='"+contextPath+"/admin/route.jsp'>RUTAS</a>";
		
		document.querySelector("#subMenu").style.display="block";
		
	},false);
	document.querySelector("#navMenuHome").addEventListener("mouseover",function(evt){
		
	});
	document.querySelector("#subMenu").addEventListener("mouseout",function(evt){
		setTimeout("document.querySelector('#subMenu').style.display='none';",1000);
	});
	document.querySelector("#navMenuReport").addEventListener("mouseover",function(evt){
		var subMenu = document.querySelector("#subMenu");
		var cells = subMenu.getElementsByTagName("table")[0].getElementsByTagName("td");
		var contextPath = document.querySelector("#contextPath").value;
		cells[0].innerHTML="<a href='"+contextPath+"/admin/reporteVenta.jsp'>POR VENTAS</a>";
		cells[1].innerHTML="<a href='"+contextPath+"/admin/track.jsp'>TRACK</a>";
		cells[2].innerHTML="<a href='"+contextPath+"/admin/product?type=ALLPRODUCTS'>POR PRODUCTOS</a>";
		cells[3].innerHTML="<a href='"+contextPath+"/admin/route.jsp'>VISITAS</a>";
		
		document.querySelector("#subMenu").style.display="block";
	},false);
	document.querySelector("#navMenuConfig").addEventListener("mouseover",function(evt){
		
	});
	
	var txts = document.querySelectorAll(".integer");
	for(var i=0;i<txts.length;i++){
		txts[i].addEventListener("keypress",integerKeyTextField,false);
	}
}
function integerKeyTextField(evt){
	 if(!/^\d$/.test(String.fromCharCode(evt.keyCode))){
		 evt.preventDefault();
	 }
}
function doubleKeyTextField(evt){
	return /^[\d|.]$/.test(String.fromCharCode(evt.keyCode));
}
function clearElementsFrom(obj){
	if(obj){
		if(typeof obj == "string"){
			obj = document.querySelector("#"+obj.trim());
		}
		if(obj)
		while(obj.hasChildNodes()){
			obj.removeChild(obj.lastChild);
		}
	}
}
window.addEventListener("load",menuHeader);