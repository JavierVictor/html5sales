<%@ include file="includes/validate.jsp" %>
<%@page import="movistar.bean.Warehouse"%>
<%@page import="movistar.bean.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><!DOCTYPE html>
<html>
<head>
<title>BIENVENIDO ${user.name} al Sistema</title>
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
		var queryString = "user?type=DELETE";
		for(var i=0;i<checks.length;i++){
			if(checks[i].checked){
				queryString += "&"+checks[i].name+"="+checks[i].value;
			}
		}
		window.location.href=queryString;
	});
	document.querySelector("#closeModalWindow").addEventListener("click",function(e){
		closeBackgroundWindow('saveForm');
		document.saveForm.reset();
	});
});
function populateForm(tr){
	var form = document.saveForm;
	form.txtCode.value=tr.cells[1].innerHTML;
	form.txtName.value=tr.cells[3].innerHTML;
	form.txtPhoneNumber.value=tr.cells[4].innerHTML;
	var opts = form.cboWarehouse.options;
	var compareTo = tr.cells[2].innerHTML;
	for(var i=0; i<opts.length;i++){
		if(opts[i].value==compareTo){
			opts[i].selected=true;
			break;
		}
	}
	showBackgroundWindow('saveForm');
	form.txtPassword.focus();
}
function initialize(pos) {
	var mapOptions = {
      center: pos ,
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      panControl: false,
      scrollwheel: true,
      draggable: true,
      mapTypeControl: false,
      streetViewControl: false,
      zoomControl: false,
      noClear:true
    };
    mapa = new google.maps.Map(document.querySelector("#usermap"),
        mapOptions);
    document.querySelector("#closeMap").addEventListener("click",function(evt){
    	closeBackgroundWindow("usermap");
    });
}
  function centrarMarcar(lat,lon){
	  if(lat==0||lon==0){
		alert("Aun no existe posicion del usuario");
		return;
	  }
	  var pos = new google.maps.LatLng(lat,lon);
	  if(mapa==null)
		  initialize(pos);
	  else
		  mapa.setCenter(pos);
	  
	  if(marcador!=null)
		  marcador.setMap(null);
	  showBackgroundWindow('usermap');
	  marcador = new google.maps.Marker(
			  {map:mapa,
				  position: pos,
				  animation: google.maps.Animation.DROP,
				  icon: document.querySelector("#contextPath").value + '/admin/img/location-user.png'
				  });
	  
  }
	var mapa=null;
	var marcador = null;

</script>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<div class="subNav">
	<section>Mantenimiento de usuarios</section>
	<div id="subAdd" onclick="showBackgroundWindow('saveForm')">AGREGAR</div>
	<div id="subListAll" onclick="window.location.href='<%=request.getContextPath() %>/admin/usuario.jsp';">LISTAR</div>
	<div id="subDelete">ELIMINAR</div>
</div>
<div class="listFilter">
	<form action="<%=request.getContextPath()%>/admin/user" method="post" name="findForm">
	<input type="hidden" name="type" value="FIND"/>
	<section>
		<label for="cboFindWarehouse">Almac&eacute;n</label>
		<select id="cboFindWarehouse" name="cboWarehouse">
			<option value="">SELECCIONAR</option>
			<%
			User userSearched=(User)request.getAttribute("userSearched");
			List<Warehouse> warehouses = (List<Warehouse>) application.getAttribute("warehouses"); 
					for(Warehouse warehouse : warehouses){%>
					<option value="<%=warehouse.getCode()%>" <%=userSearched!=null&&userSearched.getWarehouse().getCode().equals(warehouse.getCode())?"selected='selected'":""%> ><%= warehouse.getName()%></option>
			<%}%>
		</select>
	</section>
	<section>
		<label for="txtFindLogin">Login</label>
		<input id="txtFindLogin" name="txtCode" type="text" value="${userSearched.code }" />
	</section>
	<section>
		<label for="txtFindName">Nombre</label>
		<input id="txtFindName" name="txtName" type="text" value="${userSearched.name }"/>
	</section>
	<section>
		<label for="txtFindPhone">Tel&eacute;fono</label>
		<input id="txtFindPhone" name="txtPhoneNumber" type="text" value="${userSearched.phoneNumber }" class="integer"/>
	</section>
	<section>
		<label for="chkFindEnabled">habilitado</label>
		<input type="checkbox" name="chkState" checked="checked" id="chkFindEnabled" value="ACT" ${userSearched!=null && userSearched.state=="ACT"?"checked='checked'":""}/>
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
			<th>LOGIN</th>
			<th>ALMAC&Eacute;N</th>
			<th>NOMBRE</th>
			<th>NUMERO</th>
			<th>ULTIMA CONEXI&Oacute;N</th>
			<th>EDITAR</th>
		</tr>
	</thead>
	<tbody>
	<%List<User> list = (List<User>) request.getAttribute("userList");
	if(list==null)
	    list = (List<User>) session.getAttribute("users");
		for(User user:list){
	%>
		<tr style="height:35px;">
			<td><input type="checkbox" name="chkUserCode" value="<%=user.getWarehouse().getCode()+"-"+user.getCode()%>"/></td>
			<td><%=user.getCode() %></td>
			<td><%=user.getWarehouse().getName() %></td>
			<td><%=user.getName()%></td>
			<td><%=user.getPhoneNumber() %></td>
			<td><img alt="Ubicacion" src="<%=request.getContextPath()%>/admin/img/location-pin.png" onclick="centrarMarcar(<%=user.getLatitude() +", "+ user.getLongitude() %>)"></td>
			<td><img alt="Modificar" src="<%=request.getContextPath()%>/admin/img/pen-point-tip.png" onclick="populateForm(this.parentNode.parentNode)"></td>
		</tr>
		<%} %>
	</tbody>
</table>
</div>
</div>

<div id="background">
	<section id="saveForm">
	<form action="<%=request.getContextPath()%>/admin/user" method="post" name="saveForm">
	<input name="type" type="hidden" value="ADD" />
	
		<div class="formTitle">GRABAR USUARIO<img id="closeModalWindow" src="<%=request.getContextPath()%>/img/close.png"/></div>
		<div class="formContent">
			<section>
				<label>Almac&eacute;n</label>
				<select name="cboWarehouse">
				<%for(Warehouse warehouse : warehouses){%>
					<option value="<%=warehouse.getCode()%>"><%= warehouse.getName()%></option>
				<%}%>
				</select>
			</section>
			<section>
				<label>Nombre</label>
				<input type="text" name="txtName" />
			</section>
			<section>
				<label>Login</label>
				<input type="text" name="txtCode"/>
			</section>
			<section>
				<label>Password</label>
				<input type="password" name="txtPassword"/>
			</section>
			<section>
				<label>Tel&eacute;fono</label>
				<input type="text" name="txtPhoneNumber" class="integer"/>
			</section>
			<section>
				<label>Habilitado</label>
				<input type="checkbox" name="chkState" value="ACT" checked="checked" />
			</section>
		</div>
		<div class="formSubmit"><input type="submit" value="GUARDAR"/></div>
		</form>
	</section>
	<section id="usermap"><div id="closeMap"></div></section>
</div>
</body>
</html>