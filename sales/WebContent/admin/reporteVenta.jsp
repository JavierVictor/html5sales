<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@ include file="includes/validate.jsp" %>
<%@page import="movistar.bean.Warehouse"%>
<%@page import="movistar.bean.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><!DOCTYPE html>
<html>
<head>
<title>Reporte por ventas</title>
<%@ include file="includes/head.jsp" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAViVBxlcq7NOzari8F19UWWqADoK7SOoA"></script>
<script type="text/javascript">
function initialize(pos){
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
		    	document.querySelector("#usermap").style.display="none";
		    	document.querySelector("#firstStep").style.display="block";
		    });
}
addEventListener("load",loadPage,true);
function loadPage(evt){
	var theFunction = function(evt){
		document.forms[0].export_type.value=evt.srcElement.getAttribute("value");
		document.forms[0].submit();
	};
	document.querySelector("#subExcelReport").addEventListener("click",theFunction);
	document.querySelector("#subPDFReport").addEventListener("click",theFunction);
	document.querySelector("#subShowReport").addEventListener("click",theFunction);
	var divs = document.querySelectorAll(".closeIcon");
	if(divs.length>1){
		
	theFunction = function(evt){
		if(evt.srcElement.parentNode.id=="firstStep"){
			document.querySelector("#background").style.display="none";
			document.querySelector("#firstStep").style.display="none";
		}else {
			document.querySelector("#firstStep").style.display="block";
		}
		document.querySelector("#secondStep").style.display="none";
		document.querySelector("#usermap").style.display="none";
	};
	divs[0].addEventListener("click",theFunction);
	divs[1].addEventListener("click",theFunction);
	}
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
	  marcador = new google.maps.Marker({map:mapa,position: pos,animation: google.maps.Animation.DROP,icon: document.querySelector("#contextPath").value + '/admin/img/location-user.png'});
	  document.querySelector("#firstStep").style.display="none";
	  document.querySelector("#secondStep").style.display="none";
	  document.querySelector("#usermap").style.display="block";
}
	var mapa=null;
	var marcador = null;
	function showSalesFromUser(startDate,endDate,warehouseCode,userCode){
		//SALESBYUSER
		var params = "type=SALESBYUSER&startDate=" + encodeURI(startDate)+"&endDate="+encodeURI(endDate)+"&";
		params+="warehouseCode="+encodeURI(warehouseCode)+"&userCode="+encodeURI(userCode);
		sendAsyncRequest("sale","POST",params,populateSalesByUser);
	}
	function populateSalesByUser(text){
		var xml = responseXML(text);
		console.log(xml);
		var sales = xml.getElementsByTagName("sale");
		var tabla = document.querySelector("#background #firstStep table tbody");
		clearElementsFrom(tabla);
		
		for(var i=0;i<sales.length;i++){
			var sale = sales[i];
			var tr = document.createElement("tr");
			var td = document.createElement("td");
			var element = null;
			var ca = sale.getElementsByTagName("clientaddress")[0];//reutilizar para la comparación!!
			var client = ca.getElementsByTagName("client")[0];
			var saletypecode = sale.getElementsByTagName("saletype")[0].getElementsByTagName("code")[0].firstChild.nodeValue;

			if(saletypecode=="V"){
				td.style.cursor="pointer";
				element = document.createElement("a");
				element.setAttribute("href","javascript:void(0);");
				td.setAttribute("onClick",
						"showSaleDetail(this.firstChild.firstChild.nodeValue,'"+client.getElementsByTagName('ruc')[0].firstChild.nodeValue+"','"  
								+ client.getElementsByTagName('name')[0].firstChild.nodeValue + "','" 
								+ sale.getElementsByTagName('currency')[0].getElementsByTagName('name')[0].firstChild.nodeValue 
								+ "','"+sale.getElementsByTagName('salemode')[0].getElementsByTagName('name')[0].firstChild.nodeValue  
								+ "','"+sale.getElementsByTagName('deliverydate')[0].firstChild.nodeValue+"','X','" 
								+ sale.getElementsByTagName('total')[0].firstChild.nodeValue+"')");
				element.appendChild(document.createTextNode(sale.getElementsByTagName("code")[0].firstChild.nodeValue));
			}else{
				element = document.createTextNode(sale.getElementsByTagName("code")[0].firstChild.nodeValue);
			}
			td.appendChild(element);
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(client.getElementsByTagName("name")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(sale.getElementsByTagName("saletype")[0].getElementsByTagName("name")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(sale.getElementsByTagName("comment")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			
			element = document.createElement("img");
			element.setAttribute("src","img/location.png");
			element.style.cursor="pointer";
			element.setAttribute("latitude",sale.getElementsByTagName("latitude")[0].firstChild.nodeValue);
			element.setAttribute("longitude",sale.getElementsByTagName("longitude")[0].firstChild.nodeValue);
			element.addEventListener("click",function(evt){
				centrarMarcar(this.getAttribute("latitude"),this.getAttribute("longitude"));
			});
			td.appendChild(element);
			
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(
					saletypecode=="V"?sale.getElementsByTagName("currency")[0].getElementsByTagName("name")[0].firstChild.nodeValue:""));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(sale.getElementsByTagName("total")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(
					saletypecode=="V"?sale.getElementsByTagName("salemode")[0].getElementsByTagName("name")[0].firstChild.nodeValue:""));
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.appendChild(document.createTextNode(
					saletypecode!="N"?sale.getElementsByTagName("deliverydate")[0].firstChild.nodeValue:""));
			tr.appendChild(td);
			tabla.appendChild(tr);
		}
		document.querySelector("#background").style.display="block";
		document.querySelector("#firstStep").style.display="block";
	}
	function showSaleDetail(saleCode,clientRUC,clientName,currency,saleMode,deliverydate,priceList,total){
		var params = "type=DETAILSBYSALE&saleCode="+encodeURI(saleCode);
		sendAsyncRequest("sale","POST",params,populateDetailsBySale);
		document.querySelector("#tdSale").innerHTML=saleCode;
		document.querySelector("#tdClientRUC").innerHTML=clientRUC;
		document.querySelector("#tdClientName").innerHTML=clientName;
		document.querySelector("#tdCurrency").innerHTML=currency;
		document.querySelector("#tdSaleMode").innerHTML=saleMode;
		document.querySelector("#tdDeliverydate").innerHTML=deliverydate;
		document.querySelector("#tdPriceList").innerHTML=priceList;
		document.querySelector("#tdTotal").innerHTML=total;
	}
	function populateDetailsBySale(text){
		var xml = responseXML(text);
		var table = document.querySelector("#secondStep .body table tbody");
		var details = xml.getElementsByTagName("saledetail");
		clearElementsFrom(table);
		for(var i=0;i<details.length;i++){
			var detail = details[i];
			var tr = document.createElement("tr");
			var td = document.createElement("td");
			var temp = detail.getElementsByTagName("product")[0];
			
			td.appendChild(document.createTextNode(detail.getElementsByTagName("item")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(temp.getElementsByTagName("code")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(temp.getElementsByTagName("name")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(detail.getElementsByTagName("productunity")[0].getElementsByTagName("name")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(detail.getElementsByTagName("quantity")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(detail.getElementsByTagName("editedprice")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(detail.getElementsByTagName("discount")[0].firstChild.nodeValue));
			tr.appendChild(td);
			td = document.createElement("td");
			td.appendChild(document.createTextNode(detail.getElementsByTagName("subtotal")[0].firstChild.nodeValue));
			tr.appendChild(td);
			table.appendChild(tr);
			
		}
		document.querySelector("#firstStep").style.display="none";
		document.querySelector("#secondStep").style.display="block";
	}
	
</script>
<style type="text/css">

</style>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<%
pageContext.setAttribute("defaultDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
%>
<div class="subNav">
	<section style="width:15%">Reporte de ventas</section>
<form style="width:auto;display:inline-block;top:25%" action="report" method="post">
<input type="hidden" name="type" value="SALE"/>
<input type="hidden" name="export_type" value=""/>
	<section>
	<span>FECHA INICIO</span>
	<input type="date" name="startDate" value="${startDate!=null?startDate:defaultDate}"/>
	</section>
	<section>
	<span>FECHA FINAL</span>
	<input type="date" name="endDate" value="${endDate!=null?endDate:defaultDate}"/>
	</section>
	<section>
	<span>ALMAC&Eacute;N</span>
	<select name="cboWarehouse">
			<option value="">TODOS</option>
			<%
			String warehouseCode = (String)request.getAttribute("warehouseCode");
			List<Warehouse> warehouses = (List<Warehouse>) application.getAttribute("warehouses"); 
					for(Warehouse warehouse : warehouses){%>
					<option value="<%=warehouse.getCode()%>"<%=warehouseCode!=null&&warehouseCode.equals(warehouse.getCode())?" selected='selected'":"" %>><%= warehouse.getName()%></option>
			<%}%>
	</select>
	</section>
	<section>
	<span>USUARIO</span>
	<select name="cboUser">
		<option value="">TODOS</option>
		<%
		String userCode = (String)request.getAttribute("userCode");
		List<User> users = (List<User>) session.getAttribute("users"); 
			for(User user: users){%>
					<option value="<%=user.getCode()%>"<%=userCode!=null&&userCode.equals(user.getCode())?" selected='selected'":"" %> ><%= user.getName()%></option>
			<%}%>
	</select>
	</section>
	
</form>
	<div id="subExcelReport" value="EXCEL">EXCEL</div>
	<div id="subPDFReport" value="PDF">PDF</div>
	<div id="subShowReport"value="">MOSTRAR</div>
</div>
	<%List<Map> report = (List<Map>)request.getAttribute("report");if(report!=null){%>
	<div class="listado" style="height:62%;">
<table>
<thead>
<tr>
	<th rowspan="2">DISTRIBUIDORA</th>
	<th colspan="2" style="border-bottom:1px solid #FFF;">VENDEDOR</th>
	<th rowspan="2"># PEDIDOS</th>
	<th rowspan="2">MONTO</th>
</tr>
<tr>
<th>CODIGO</th>
<th>NOMBRE</th>
</tr>
</thead>
<tbody>
    <%for(Map map : report){%>
<tr style="cursor:pointer" onclick="showSalesFromUser('${startDate}','${endDate}','<%=map.get("warehouseCode")%>','<%=map.get("userCode")%>')">
	<td><%=map.get("warehouseName") %></td>
	<td><%=map.get("userCode") %></td>
	<td><%=map.get("userName") %></td>
	<td><a href="javascript:void(0)"><%=map.get("totalSales") %></a></td>
	<td><%=map.get("totalAmount") %></td>
</tr>
<%} %>
</tbody>
</table>
</div>
<div id="background">

<div id="firstStep">
<div class="closeIcon"></div>
<div style="width:25px;height:25px;background-image:url('img/realExcel.png');float:left;margin-left:1%;margin-top:5px;cursor:pointer;"></div>
<div style="width:25px;height:25px;background-image:url('img/realPDF.png');float:left;margin-left:1%;margin-top:5px;cursor:pointer;"></div>
<table>
<thead>
	<tr>
		<th style="width:60px;">VENTA</th>
		<th style="width:200px;">CLIENTE</th>
		<th style="width:90px;">TIPO</th>
		<th style="width:200px;">OBS</th>
		<th style="width:40px;">LUGAR</th>
		<th>MONEDA</th>
		<th>TOTAL</th>
		<th>MODO VENTA</th>
		<th>FECHA ENTREGA</th>
	</tr>
</thead>
<tbody>
</tbody>
</table>
</div>
<div id="secondStep">
<div class="closeIcon"></div>
	<div class="header">
		<table >
			<tbody>
				<tr>
					<td>VENTA</td>
					<td id="tdSale"></td>
					<td>RUC/DNI</td>
					<td id="tdClientRUC"></td>
				</tr>
				<tr>
					<td>RAZON SOCIAL</td>
					<td id="tdClientName"></td>
					<td>MONEDA</td>
					<td id="tdCurrency"></td>
				</tr>
				<tr>
					<td>MODO DE VENTA</td>
					<td id="tdSaleMode"></td>
					<td>FECHA DE ENTREGA</td>
					<td id="tdDeliverydate"></td>
				</tr>
				<tr>
					<td>LISTA PRECIO</td>
					<td id="tdPriceList"></td>
					<td>TOTAL</td>
					<td id="tdTotal"></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="body">
		<table>
			<thead>
			<tr>
				<th>ITEM</th>
				<th>PRODUCTO</th>
				<th>DESCRIPCI&Oacute;N</th>
				<th>UNIDAD</th>
				<th>CANTIDAD</th>
				<th>PRECIO</th>
				<th>DESCUENTO</th>
				<th>SUBTOTAL</th>
			</tr>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</div>
</div>
<section id="usermap" style="width:90%;height:70%;top:15%;left:5%;">
	<div id="closeMap"></div>
</section>
</div>
<%} %>
</body>
</html>