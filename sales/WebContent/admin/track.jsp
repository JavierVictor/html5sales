<%@page import="movistar.bean.Sale"%>
<%@page import="movistar.bean.User"%>
<%@page import="movistar.bean.Currency"%>
<%@page import="movistar.bean.Configuration"%>
<%@ include file="includes/validate.jsp" %>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>BIENVENIDO ${user.name} al Sistema</title>
<%@ include file="includes/head.jsp" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAViVBxlcq7NOzari8F19UWWqADoK7SOoA&sensor=true"></script>
<script type="text/javascript">
function initialize() {
	var mapOptions = {
      zoom: 17,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      panControl: false,
      scrollwheel: true,
      draggable: true,
      mapTypeControl: false,
      streetViewControl: false,
      zoomControl: false
    };
    mapa = new google.maps.Map(document.querySelector("#reportMap"),
        mapOptions);
    
    var lineCoordinatesPath = new google.maps.Polyline({
    	  path: [
    	         {lat:-12.0266383,lng:-76.9877792},
    	         {lat:-12.0266583,lng:-76.9837792},
    	         {lat:-12.0226383,lng:-77.9877782},
    	         {lat:-12.4266383,lng:-76.9877792},
    	         {lat:-12.0266583,lng:-75.9837792},
    	         {lat:-12.5226383,lng:-76.0877782}
    	         ],
    	  geodesic: true,
    	  strokeColor: '#003245 ',
    	  strokeOpacity: 0.50,
    	  strokeWeight: 4
    	});
    //lineCoordinatesPath.setMap(mapa);
  	if(typeof loadItinerary == "function")loadItinerary();  
}

	var mapa=null;
	addEventListener("load",initialize);
</script>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<div style="position:absolute;top: 23%;left:1%;width:20%;height:75%;border:1px solid #003245;border-radius:10px;background-color:rgba(255,255,255,0.8);">
<form action="<%=request.getContextPath() %>/admin/report" method="post">
<input type="hidden" name="type" value="TRACK"/>

<section style="width:100%;">
	<span style="width:90%;display:block;margin:0px auto;color:#006490">USUARIO</span>
	<select style="width:90%;display:block;margin:0px auto;" name="cboUser">
	<option value="">SELECCIONAR</option>
	<%List<User> users = (List<User>)session.getAttribute("users");
		for(User user:users){%>
		<option value="<%=user.getWarehouse().getCode()+"-"+user.getCode()%>"><%=user.getName() %></option>
		<%}%>
	</select>
</section>
<section style="width:100%;">
	<span style="width:90%;display:block;margin:0px auto;;color:#006490" >FECHA</span>
	<input type="date" name="txtDate" style="width:90%;display:block;margin:0px auto;" value="2015-09-09" >
</section>
<section style="width:100%;margin-top:5%;">
	<label style="width:90%;display:block;margin:0px auto;color:#006490;margin-bottom:5%;">mostrar track<input type="checkbox" name="chkTrack" checked="checked" /></label>
	<input  class="btnForm" type="submit" value="VER TRACK "/>
</section>
</form> 
</div>
<div id="reportMap" style="width:77%;height:75%;border:1px solid #003245;border-radius:10px;background-color:rgba(255,255,255,0.8);position:absolute;top: 23%;left:22%">
</div>
<%List<Sale> sales = (List<Sale>) request.getAttribute("sales");
if(sales!=null){ %>
<script type="text/javascript">
var marker = null;
var contentString = "<div class='contentString'>"+
"<table><thead><tr><th colspan='2'>{clientName}</th></tr></thead " +
"<tbody><tr><td>Ruc</td><td>{ruc}</td></tr>"+
"<tr><td>Monto</td><td>{amount}</td></tr>"+
"<tr><td>Lugar</td><td>{address}</td></tr>"+
"<tr><td>Fecha</td><td>{date}</td></tr></tbody></table></div>";

function loadItinerary(){
	var infowindow = null;
	var geocoder = new google.maps.Geocoder();
<%for(Sale sale:sales){%>
	geocoder.geocode({'latLng':new google.maps.LatLng(<%=sale.getLatitude()%>,<%=sale.getLongitude()%>)},function(results,status){
	console.log(results);
	console.log(status);
		if (status == google.maps.GeocoderStatus.OK){
			var address = "";
			for(var i = 0;i<results.length;i++){
				var formattedAddress = results[i].formatted_address;
				var pos = formattedAddress.indexOf(",");
				address += (formattedAddress.substring(0,pos!=-1?pos:formattedAddress.length)) + " ";
			}
			infowindow=new google.maps.InfoWindow({
				  content: contentString.replace("{clientName}",'<%=sale.getClientAddress().getClient().getName()%>').
				  replace("{ruc}",'<%=sale.getClientAddress().getClient().getRuc()%>').
				  replace("{amount}","0.00").
				  replace("{address}",address).
				  replace("{date}","<%=sale.getMobileDate()%>")
				});
			marker = new google.maps.Marker({
			  position: new google.maps.LatLng(<%=sale.getLatitude()%>,<%=sale.getLongitude()%>),
			  map: mapa,
			  icon: document.querySelector("#contextPath").value + '/admin/img/<%=sale.getNoSale().getCode()==null?"sale.png":"nosale.png"%>',
			  title: '<%=sale.getClientAddress().getClient().getName()%>'
			});
			marker.infowindow=infowindow;
			marker.addListener('click', function() {
			  this.infowindow.open(mapa, this);
			});
			
			if(!mapa.getCenter())mapa.setCenter(marker.getPosition());
		}
	});
<%}%>
}
</script>
<%}%>
</body>
</html>