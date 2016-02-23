var ubication = {latitude:null,longitude:null};
var mobileConfiguration = {
		productCodeType:"number",
		productCodeLength:5,
		saleEditPrice:false,
		saleEditDiscount:false,
		saleValidateStock:false
};
navigator.geolocation.watchPosition(ubic);
function ubic(position){
	console.log(position);
	ubication.latitude  = position.coords.latitude;
	ubication.longitude = position.coords.longitude;
}
function ID(id){
	return document.getElementById(id);
}
function VALUE(id){
	return ID(id).value;
}
function SETVALUE(id,value){
	ID(id).value=value;
}
function HTML(id,value){
	ID(id).innerHTML=value;
}
function browse(url){
	window.location.href=url;
}
function setValues(route){
	localStorage.setItem("presale",JSON.stringify({
		clientCode:route.getAttribute("client"),
		clientAddressCode:route.getAttribute("address"),
		itineraryCode:route.getAttribute("itinerary"),
		priceListCode:null,
		currencyCode:null,
		saleTypeCode:null
	}));
	browse("detalleCliente.html");
}

function sendAsyncRequest(url, method, params, metodo,fallback) {
	var request = getRequest();
	request.open(method, url, true);
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=ISO-8859-1");
	request.setRequestHeader("X-Requested-With", "XMLHttpRequest");

	request.metodo = metodo;
	request.onreadystatechange = function() {
		if (request.readyState == 4){
			if(request.status == 200){
				metodo(request.responseText);
			}else if(fallback){
				fallback();
			}
		}
	};
	request.send(params);
}
function getRequest(){
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}else{
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
function getUserParams(){
	return "userCode="+user.code+"&warehouseCode="+user.warehouseCode+"&companyCode="+user.companyCode;
}
function animateMenu(){
	var div = ID("divMenuContent");
	if(div.style.display==""||div.style.display=="none"){
		div.style.display="block";
	}else{
		div.style.display="none";
	}
}
function clearElementsFrom(obj){
	if(obj)
	while(obj.hasChildNodes()){
		obj.removeChild(obj.lastChild);
	}
}
function salir(){
	dbSales.transaction(function(tx){
		tx.executeSql("UPDATE USER SET STATE='OUT'");
	},errorTransaction,function(){
		browse("index.html");
	});
}
function toStringDate(date,which){
	var string = "";
	if(which==FULL_DATE||which==ONLY_DATE){
		string += date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear() + " "; 
	}
	if(which==FULL_DATE||which==ONLY_TIME){
		string += date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds(); 
	}
	return string.trim();
}
var FULL_DATE = 0;
var ONLY_DATE = 1;
var ONLY_TIME = 2;

window.addEventListener("load",stackLoadPage);
function stackLoadPage(evt){
	if(typeof cargaPagina == 'function')
		cargaPagina();
	
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

function message(options){
	if(options==null){
		options={
				messageContent:"",
				messageTitle:"",
				messageType:-1,
				messageIcon:null,
				callback:null
				};
		console.log("null options");
	}
	var background = document.getElementsByClassName("background")[0];
	if(background==null){
		var body = document.getElementsByTagName("body")[0];
		background = document.createElement("div");
		background.setAttribute("class","background");
		body.appendChild(background);
	}
	var messageContainer = document.createElement("section");
	var messageTitle= document.createElement("div");
	var messageContent = document.createElement("div");
	var messageIcon = document.createElement("div");
	var messageData = document.createElement("div");
	var messageFooter = document.createElement("div");
	var messageButton = document.createElement("div");
	
	messageContainer.setAttribute("class", "messageContainer");
	messageTitle.setAttribute("class", "messageTitle");
	if(options.titleColor!=null && typeof options.titleColor!= "undefined" ){
		messageTitle.style.backgroundColor=options.titleColor;
	}
	
	messageContent.setAttribute("class", "messageContent");
	messageIcon.setAttribute("class", "messageIcon");
	messageData.setAttribute("class", "messageData");
	messageFooter.setAttribute("class", "messageFooter");
	messageButton.setAttribute("class", "messageButton");
	
	messageTitle.appendChild(document.createTextNode(options.messageTitle));
	if(options.messageIcon!=null && options.messageIcon != undefined){
		messageIcon.style.backgroundImage="url('"+options.messageIcon+"')";
	}
	messageData.appendChild(document.createTextNode(options.messageContent));
	messageButton.appendChild(document.createTextNode("OK"));
	
	messageContent.appendChild(messageIcon);
	messageContent.appendChild(messageData);
	messageFooter.appendChild(messageButton);
	messageContainer.appendChild(messageTitle);
	messageContainer.appendChild(messageContent);
	messageContainer.appendChild(messageFooter);
	messageButton.callback=options.callback;
	messageButton.addEventListener("click",function(evt){
		var background = document.getElementsByClassName("background")[0];
		var messageContainer = background.getElementsByClassName("messageContainer")[0];
		background.removeChild(messageContainer);
		background.style.display="none";
		console.log(this);
		console.log(this.callback);
		if(this.callback!=null&&typeof this.callback== "function")
			this.callback();
	});
	
	background.appendChild(messageContainer);
	background.style.display="block";
}
function infoMessage(textMessage,callback){
	var options={
			messageContent:textMessage,
			messageTitle:"INFO",
			messageType:-1,
			messageIcon:"img/info.png",
			callback:callback
			};
	message(options);
}
function errorMessage(textMessage,callback){
	var options={
			messageContent:textMessage,
			messageTitle:"ERROR",
			messageType:-1,
			messageIcon:"img/error.png",
			titleColor: "#c0392b",
			callback:callback
			};
	message(options);
}
function warningMessage(textMessage,callback){
	var options={
			messageContent:textMessage,
			messageTitle:"ADVERTENCIA",
			messageType:-1,
			messageIcon:"img/warning.png",
			titleColor:"#e67e22",
			callback:callback
			};
	message(options);
}
function confirmMessage(textMessage,callback,fallback,closeBackground){
	var options={
			messageContent:textMessage,
			messageTitle:"Confirmacion",
			messageType:-1,
			messageIcon:"img/interrogate.png",
			callback:null,
			fallback:null,
			closeBackground:closeBackground
			};
	

var background = document.getElementsByClassName("background")[0];
if(background==null){
	var body = document.getElementsByTagName("body")[0];
	background = document.createElement("div");
	background.setAttribute("class","background");
	body.appendChild(background);
}
var messageContainer = document.createElement("section");
var messageTitle= document.createElement("div");
var messageContent = document.createElement("div");
var messageIcon = document.createElement("div");
var messageData = document.createElement("div");
var messageFooter = document.createElement("div");
var okButton = document.createElement("div");
var cancelButton = document.createElement("div");

messageContainer.setAttribute("class", "messageContainer");
messageTitle.setAttribute("class", "messageTitle");
if(options.titleColor!=null && typeof options.titleColor!= "undefined" ){
	messageTitle.style.backgroundColor=options.titleColor;
}

messageContent.setAttribute("class", "messageContent");
messageIcon.setAttribute("class", "messageIcon");
messageData.setAttribute("class", "messageData");
messageFooter.setAttribute("class", "messageFooter");
okButton.setAttribute("class", "messageButton");
cancelButton.setAttribute("class", "messageButton");

messageTitle.appendChild(document.createTextNode(options.messageTitle));
if(options.messageIcon!=null && options.messageIcon != undefined){
	messageIcon.style.backgroundImage="url('"+options.messageIcon+"')";
}
messageData.appendChild(document.createTextNode(options.messageContent));
okButton.appendChild(document.createTextNode("OK"));
cancelButton.appendChild(document.createTextNode("CANCEL"));
messageContent.appendChild(messageIcon);
messageContent.appendChild(messageData);
messageFooter.appendChild(okButton);
messageFooter.appendChild(cancelButton);
messageContainer.appendChild(messageTitle);
messageContainer.appendChild(messageContent);
messageContainer.appendChild(messageFooter);
okButton.callback=options.callback;
cancelButton.fallback=options.fallback;
okButton.closeBackground=options.closeBackground;
cancelButton.closeBackground=options.closeBackground;
okButton.addEventListener("click",function(evt){
	var background = document.getElementsByClassName("background")[0];
	var messageContainer = background.getElementsByClassName("messageContainer")[0];
	background.removeChild(messageContainer);
	if(this.closeBackground)
		background.style.display="none";

	if(this.callback!=null&&typeof this.callback== "function")
		this.callback();
});
cancelButton.addEventListener("click",function(evt){
	var background = document.getElementsByClassName("background")[0];
	var messageContainer = background.getElementsByClassName("messageContainer")[0];
	background.removeChild(messageContainer);
	if(this.closeBackground)
		background.style.display="none";

	if(this.fallback!=null&&typeof this.fallback== "function")
		this.fallback();
});

background.appendChild(messageContainer);
background.style.display="block";
}

function xmlTagNodeValue(node,tagName){
	if(node &&	node.getElementsByTagName 
				&& node.getElementsByTagName(tagName).length>0
				&& node.getElementsByTagName(tagName)[0].firstChild)
		return node.getElementsByTagName(tagName)[0].firstChild.nodeValue;
	
	return null;
}

function sendToServer(){
	var sale = {
			code:null,
			itineraryCode:null,
			companyCode:null,
			clientCode:null,
			clientAddressCode:null,
			userCode:null,
			saleTypeCode:null,
			currencyCode:null,
			saleModeCode:null,
			noSaleCode:null,
			obs:null,
			priceListCode:null,
			deliveryDate:null,
			latitude:null,
			longitude:null,
			mobileDate:null,
			details:[]
	};
	dbSales.transaction(function(tx){
		tx.executeSql("SELECT * FROM SALE WHERE CODE=?",[saleCode],function(tx,results){
			if(results.rows.length>0){
						sale.code=results.rows.item(0).CODE;
						sale.itineraryCode=""+results.rows.item(0).ITINERARYCODE;
						sale.companyCode=results.rows.item(0).COMPANYCODE;
						sale.clientCode=results.rows.item(0).CLIENTCODE;
						sale.clientAddressCode=results.rows.item(0).CLIENTADDRESSCODE;
						sale.userCode=results.rows.item(0).USERCODE;
						sale.saleTypeCode=results.rows.item(0).SALETYPECODE;
						sale.currencyCode=results.rows.item(0).CURRENCYCODE;
						sale.saleModeCode=results.rows.item(0).SALEMODECODE;
						sale.noSaleCode=results.rows.item(0).NOSALECODE;
						sale.obs=results.rows.item(0).OBS;
						sale.priceListCode=results.rows.item(0).PRICELISTCODE;
						sale.deliveryDate=results.rows.item(0).DELIVERYDATE;
						sale.latitude=results.rows.item(0).LATITUDE;
						sale.longitude=results.rows.item(0).LONGITUDE;
						sale.mobileDate=results.rows.item(0).REGISTEREDDATE;				
			}
			
		});
		tx.executeSql("SELECT * FROM SALEDETAIL WHERE SALECODE = ?",[saleCode],function(tx,results){
			if(results.rows.length>0){
				for(var i=0;i<results.rows.length;i++){
					sale.details.push({
						item:results.rows.item(i).ITEM,
						productCode:results.rows.item(i).PRODUCTCODE,
						warehouseCode:results.rows.item(i).WAREHOUSECODE,
						productUnityCode:results.rows.item(i).PRODUCTUNITYCODE,
						quantity:results.rows.item(i).QUANTITY,
						price:results.rows.item(i).PRICE,
						editedPrice:results.rows.item(i).EDITEDPRICE,
						discount:results.rows.item(i).DISCOUNT,
						subtotal:results.rows.item(i).SUBTOTAL
					});
				}
			}
		});
	},errorTransaction,function(){
		var params = "type=SEND_SALE&userCode="+ user.code +
		"&warehouseCode="+user.warehouseCode+
		"&companyCode="+user.companyCode+"&sale="+JSON.stringify(sale);
		sendAsyncRequest("phone", "POST", params, confirmReceivedSale,errorReceivedSale);
	});
}


function confirmReceivedSale(txt){
	var xml = responseXML(txt);
	var code = xml.getElementsByTagName("code")[0];
	
	dbSales.transaction(function(tx){
				var servercode = code.lastChild.firstChild.nodeValue;
				var webcode = code.firstChild.firstChild.nodeValue;
				tx.executeSql("UPDATE SALE SET CODE = ? , STATE = 'SERVER' WHERE CODE = ?" ,
				[servercode,webcode],
				function(tx,results){},
				errorTransaction);
				tx.executeSql("UPDATE SALEDETAIL  SET SALECODE = ?  WHERE SALECODE = ?",
				[servercode,webcode],
				null,
				errorTransaction);
				tx.executeSql("UPDATE ITINERARY SET STATE = STATE||'|SERVER' WHERE CODE = ? AND CLIENTCODE=? AND CLIENTADDRESSCODE=?",
						[code.getAttribute("itineraryCode"),code.getAttribute("clientCode"),code.getAttribute("clientAddressCode")],
						null,
						errorTransaction);
	},null,function(){
		infoMessage("Se ha enviado la operacion y se ha actualizado localmente",function(){
			browse("ruta.html");
		});
	});
}
function errorReceivedSale(){
	errorMessage("Ha ocurrido un error de comunicacion",function(){
		browse("ruta.html");
	});
}