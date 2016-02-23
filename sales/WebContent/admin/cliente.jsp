<%@ include file="includes/validate.jsp" %>
<%@page import="movistar.bean.Client"%>
<%@page import="movistar.bean.ClientType"%>
<%@page import="movistar.bean.Warehouse"%>
<%@page import="movistar.bean.User"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Bienvenido ${user.name} al Sistema</title>
<%@ include file="includes/head.jsp" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAViVBxlcq7NOzari8F19UWWqADoK7SOoA&sensor=true"></script>
<script type="text/javascript">
window.addEventListener("load",function(evt){
	document.querySelector("#chkSelectAll").addEventListener("change",function(e){
		var checks = document.querySelectorAll(".listado .content table tbody input");
		for(var i=0;i<checks.length;i++){
			checks[i].checked=this.checked;
		}
	});
	document.querySelector("#subDelete").addEventListener("click",function(e){
		var checks = document.querySelectorAll(".listado .content table tbody input");
		var queryString = document.querySelector("#contextPath").value + "/admin/client?type=DELETE";
		for(var i=0;i<checks.length;i++){
			if(checks[i].checked){
				queryString += "&"+checks[i].name+"="+checks[i].value;
			}
		}
		window.location.href=queryString;
	});
	document.querySelector("#closeModalWindow").addEventListener("click",function(e){
		document.saveForm.type.value="ADD";
		document.saveForm.hdnAddressCode.value="";
		document.saveForm.hdnAddressType.value="";
		document.saveForm.hdnAddressName.value="";
		document.saveForm.hdnLatitude.value="";
		document.saveForm.hdnLongitude.value="";
		document.querySelector("#spnAddress").innerHTML="Ubicacion del cliente (click derecho para establecer ubicaci&oacute;n dentro del mapa)";
		document.saveForm.reset();
		closeBackgroundWindow('saveForm');
	});
});

function populateForm(tr,addressCode,addressType,lat,lng){
	var form = document.saveForm;
	form.txtCode.value=tr.cells[1].innerHTML;
	form.txtRuc.value=tr.cells[2].innerHTML;
	form.txtName.value=tr.cells[3].innerHTML;
	form.txtPhone.value=tr.cells[4].innerHTML;
	form.type.value="EDIT";
	form.hdnAddressCode.value=addressCode;
	form.hdnAddressType.value=addressType;

	var opts = form.cboClientType.options;
	var compareTo = tr.cells[5].getAttribute("code");

	for(var i=0; i<opts.length;i++){
		if(opts[i].value==compareTo){
			opts[i].selected=true;
			break;
		}
	}
	if(addressCode!=""){
		geoCodeLatLng( {lat: Number(lat), lng: Number(lng)});
	}
	centrarMarcar(lat,lng,18);
	form.txtName.focus();
}
function initialize(pos,zoom) {
	var mapOptions = {
	  backgroundColor:"#FFF",
	  mapMarker:true,
      center: pos,
      zoom: zoom,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      panControl: false,
      scrollwheel: true,
      draggable: true,
      mapTypeControl: false,
      streetViewControl: false,
      zoomControl: true,
      disableDoubleClickZoom : true
    };
    mapa = new google.maps.Map(document.querySelector("#clientMap"),
        mapOptions);
    google.maps.event.addListener(mapa,"rightclick",function(event){
    	marcador.setPosition(event.latLng);
    	document.saveForm.hdnLatitude.value=event.latLng.lat();
    	document.saveForm.hdnLongitude.value=event.latLng.lng();
    	geoCodeLatLng(event.latLng);
    });
    
}
function geoCodeLatLng(latLng){
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'latLng':latLng},function(results,status){
		if (status == google.maps.GeocoderStatus.OK){
			var address = "";
			for(var i = 0;i<results.length;i++){
				var formattedAddress = results[i].formatted_address;
				var pos = formattedAddress.indexOf(",");
				address += (formattedAddress.substring(0,pos!=-1?pos:formattedAddress.length)) + " ";
			}
			document.saveForm.hdnAddressName.value = address;
			document.querySelector("#spnAddress").innerHTML=address;
		}
	});
}
  function centrarMarcar(lat,lon,zoom){
	  if(lat==0||lon==0){
		alert("Aun no existe posicion del usuario");
		return;
	  }
	  var pos = new google.maps.LatLng(lat,lon);
	  if(mapa==null)
		  initialize(pos,zoom);
	  else{
		 mapa.setCenter(pos);
		 mapa.setZoom(zoom);
	  }
	  
	  if(marcador!=null)
		  marcador.setMap(null);
	  showBackgroundWindow('saveForm');
	  marcador = new google.maps.Marker({map:mapa,position: pos,animation: google.maps.Animation.DROP,icon: document.querySelector("#contextPath").value + '/admin/img/location-user.png'});
	  
  }
	var mapa=null;
	var marcador = null;
</script>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<div class="subNav">
	<section>Mantenimiento de clientes</section>
	<div id="subAdd" onclick="centrarMarcar(<%=application.getAttribute("app.client.defaultLatitude")%>,<%=application.getAttribute("app.client.defaultLongitude")%>,<%=application.getAttribute("app.client.defaultMapZoom")%>)">AGREGAR</div>
	<div id="subListAll" onclick="window.location.href='<%=request.getContextPath() %>/admin/client?type=ALLCLIENTS';">LISTAR</div>
	<div id="subDelete">ELIMINAR</div>
</div>
<div class="listFilter">
	<form action="<%=request.getContextPath()%>/admin/client" method="post" name="findForm">
	<input type="hidden" name="type" value="FIND"/>
	<section>
		<label for="txtFindCode">C&oacute;digo</label>
		<input id="txtFindCode" name="txtCode" type="text" maxlength="<%=application.getAttribute("app.client.maxCodeLength")%>" value="${clientSearched.code }" />
	</section>
	<section>
		<label for="txtFindName">Nombre</label>
		<input id="txtFindName" name="txtName" type="text" maxlength="<%=application.getAttribute("app.client.maxNameLength")%>" value="${clientSearched.name}"/>
	</section>
	<section>
		<label for="cboFindClientType">Tipo</label>
		<select id="cboFindClientType" name="cboClientType">
			<option value="">SELECCIONAR</option>
			<%
			Client clientSearched = (Client) request.getAttribute("clientSearched");
			List<ClientType> clientTypes = (List<ClientType>) application.getAttribute("clientTypes");
			if(clientTypes!=null)
					for(ClientType clientType : clientTypes){
						if(clientSearched!=null && clientSearched.getClientType().getCode().equals(clientType.getCode())){
						    clientSearched.getClientType().setName(clientType.getName());%>
						    <option value="<%=clientType.getCode()%>" selected="selected"><%= clientType.getName()%></option>
						<%}else{%>
							<option value="<%=clientType.getCode()%>"><%= clientType.getName()%></option>
						<%}	%>
			<%}%>
		</select>
	</section>
	<section>
		<label for="txtFindRuc">RUC</label>
		<input id="txtFindRuc" name="txtRuc" type="text" maxlength="<%=application.getAttribute("app.client.maxRucLength")%>" value="${clientSearched.ruc }" class="integer" />
	</section>
	<section>
		<label for="txtFindPhone">Tel&eacute;fono</label>
		<input id="txtFindPhone" name="txtPhone" type="text" maxlength="<%=application.getAttribute("app.client.maxPhoneLength")%>" value="${clientSearched.phone}" class="integer"/>
	</section>
	<section>
		<label for="chkFindEnabled">habilitado</label>
		<input type="checkbox" name="chkState" checked="checked" id="chkFindEnabled" value="ACT" ${clientSearched==null||clientSearched.state=="ACT"?"checked='checked'":""}/>
	</section>
	<div id="btnFind" onclick="document.findForm.submit();">BUSCAR</div>
	</form>
</div>
<div class="listado">
<div class="content">
<table> 
	<thead>
		<tr>
			<th><input type="checkbox" id="chkSelectAll" /></th>
			<th>C&Oacute;DIGO</th>
			<th>RUC</th>
			<th>RAZ SOCIAL</th>
			<th>PHONE</th>
			<th>TIPO</th>
			<th>EDITAR</th>
		</tr>
	</thead>
	<tbody>
	<%List<Client> list = (List<Client>) request.getAttribute("clientList");
	if(list!=null)
		for(Client client:list){
	%>
		<tr style="height:35px;">
			<td><input type="checkbox" name="chkClientCode" value="<%=client.getCode()%>"/></td>
			<td><%=client.getCode() %></td>
			<td><%=client.getRuc() %></td>
			<td><%=client.getName() %></td>
			<td><%=client.getPhone()%></td>
			<td code="<%=client.getClientType().getCode()%>"><%=client.getClientType().getName()%></td>
			<td><img alt="Modificar"
				src="<%=request.getContextPath()%>/admin/img/pen-point-tip.png"
				<%if(client.getClientAddresses()!=null && client.getClientAddresses().size()!=0){%>
					onclick="populateForm(this.parentNode.parentNode,'<%=client.getClientAddresses().get(0).getCode()%>','<%=client.getClientAddresses().get(0).getType()%>','<%=client.getClientAddresses().get(0).getLatitude()%>','<%=client.getClientAddresses().get(0).getLongitude()%>')"
				<%}else{ %>	onclick="populateForm(this.parentNode.parentNode,'','','<%=application.getAttribute("app.client.defaultLatitude")%>','<%=application.getAttribute("app.client.defaultLongitude")%>')"
				<%} %>
					></td>
		</tr>
		<%} %>
	</tbody>
</table>
</div>
</div>

<div id="background">
	<section id="saveForm">
	<form action="<%=request.getContextPath()%>/client" method="post" name="saveForm" accept-charset="UTF-8">
	<input name="type" type="hidden" value="ADD" />
	<input name="hdnAddressCode" type="hidden" value=""/>
	<input name="hdnAddressType" type="hidden" value=""/>
	<input name="hdnLatitude" type="hidden" value=""/>
	<input name="hdnLongitude" type="hidden" value=""/>
	<input name="hdnAddressName" type="hidden" value=""/>
		<div class="formTitle">GRABAR CLIENTE<img id="closeModalWindow" src="<%=request.getContextPath()%>/img/close.png"/></div>
		<div class="formContent" style="overflow:auto;">
			<section>
				<label>C&oacute;digo</label>
				<input type="text" name="txtCode" maxlength="<%=application.getAttribute("app.client.maxCodeLength")%>"/>
			</section>
			<section>
				<label>RUC</label>
				<input type="text" name="txtRuc" maxlength="<%=application.getAttribute("app.client.maxRucLength")%>"/>
			</section>
			<section>
				<label>Nombre</label>
				<input type="text" name="txtName" maxlength="<%=application.getAttribute("app.client.maxNameLength")%>"/>
			</section>
			<section>
				<label>Tipo</label>
				<select name="cboClientType">
				<%if(clientTypes!=null)
				for(ClientType clientType:clientTypes){%>
					<option value="<%=clientType.getCode()%>"><%=clientType.getName()%></option>
				<%}%>
				</select>
			</section>
			<section>
				<label>Tel&eacute;fono</label>
				<input type="text" name="txtPhone" maxlength="<%=application.getAttribute("app.client.maxPhoneLength")%>"/>
			</section>
			<section>
				<label>Habilitado</label>
				<input type="checkbox" name="chkState" value="ACT" checked="checked" />
			</section>
			<div style="width:98%;margin:10px auto;height:90%;border:1px solid #00c6d7;">
				<span id="spnAddress" style="text-indent:10px;height:12%;display:block;width:100%;font-size:14px;color:white;background-color:#00c6d7;line-height:2em">Ubicacion del cliente (click derecho para establecer ubicaci&oacute;n dentro del mapa)</span>
				<div id="clientMap" style="width:100%;height:88%;"></div>
			</div>
		</div>
		<div class="formSubmit"><input type="submit" value="GUARDAR"/></div>
		</form>
	</section>
	
</div>
</body>
</html>