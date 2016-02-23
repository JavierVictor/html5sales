<%@ include file="includes/validate.jsp" %>
 <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><!DOCTYPE html>
<html>
<head>
<%@ include file="includes/head.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/gauge.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/canvasjs.min.js"></script>
<title>BIENVENIDO ${user.name} al Sistema</title>
</head>
<body>
<%@include file="includes/header.jsp" %>
<section style="width:98%;height:74%;background-color:rgba(255,255,255,0.75);border:1px solid #003245;margin:10px auto;border-radius:10px;">
		<div class="contentHome" >
			<span>Clientes en ruta atendidos %</span>
			<canvas id="percentSale"></canvas>
			<span id="textFieldPercent"></span>
			<table>
				<tbody>
					<tr>
						<td>Clientes visitados</td>
						<td>&nbsp;</td>
						<td>540</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Clientes con venta</td>
						<td>250</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Clientes con no venta</td>
						<td>290</td>
					</tr>
					
					<tr>
						<td>Clientes no visitados</td>
						<td>&nbsp;</td>
						<td>490</td>
					</tr>
					<tr>
						<td>Clientes en Ruta</td>
						<td>&nbsp;</td>
						<td><strong>1030</strong></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="contentHome" >
			<span>Total recaudado en soles </span>
			<canvas id="totalSale"></canvas>
			<span id="textFieldTotal"></span>
			<table>
				<tbody>
					<tr>
						<td>Chocolates</td>
						<td>S/.98327</td>
					</tr>
					<tr>
						<td>Bebidas</td>
						<td>S/.6687</td>
					</tr>
					<tr>
						<td>Lacteos</td>
						<td>S/.2727</td>
					</tr>
					<tr>
						<td><strong>TOTAL</strong></td>
						<td><strong>s/.1500.00</strong></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="contentHome">
			<div id="chartContainer" style="width:100%;height:100%;display:block;overflow:hidden;">
		</div>
		</div>	
</section>
<script type="text/javascript">
function loadHome(evt){
	var opts = {
			  lines: 12, // The number of lines to draw
			  angle: 0, //The length of each line
			  lineWidth: 0.4, // The line thickness
			  pointer: {
			    length: 0.75, // The radius of the inner circle
			    strokeWidth: 0.04, // The rotation offset
			    color: '#79b800' // Fill color
			  },
			  limitMax: 'false',   // If true, the pointer will not go past the end of the gauge
			  colorStart: '#003245',   // Colors
			  colorStop: '#00c6d7',    // just experiment with them
			  strokeColor: '#d5eef3',   // to see which ones work best for you
			  generateGradient: true,
			  maxValue:100,
			  minValue:0,
			  animationSpeed: 16 // set animation speed (32 is default value)
			};
	var opts2 = {
			  lineWidth: 0.1, 
			  angle: 0.4, // The length of each line
			  colorStart: '#003245',   // Colors
			  colorStop: '#00c6d7',    // just experiment with them
			  strokeColor: '#d5eef3',   // to see which ones work best for you
			  limitMax: true,
			  animationSpeed: 16 // set animation speed (32 is default value)
			};
	
	var gauge = new Gauge(document.querySelector('#percentSale')).setOptions(opts); // create sexy gauge!
	gauge.set(20);
	gauge.setTextField(document.querySelector("#textFieldPercent"));
	
	var donut = new Donut(document.querySelector("#totalSale")).setOptions(opts2);
	donut.setTextField(document.querySelector("#textFieldTotal"));
	donut.maxValue=3000;
	donut.set(1500);

	CanvasJS.addColorSet("movistar",
            ["#00496c",
            "#00c6d7",
            "#D5EEF3",
            "#3CB371",
            "#90EE90"]);

	var chart = new CanvasJS.Chart("chartContainer",
			{
				colorSet:"movistar",
				title:{
					text: "Distribución de ventas",
					fontColor: "#006490",
					fontFamily: "Movistar Text",
					fontSize: "14"
				},
		                animationEnabled: false,
				legend:{
					verticalAlign: "bottom",
					horizontalAlign: "center"
				},
				data: [
				{        
					indexLabelFontSize: 20,
					indexLabelFontFamily: "Movistar Text",       
					indexLabelFontColor: "#006490", 
					indexLabelLineColor: "#79b800",
					indexLabelPlacement: "outside",
					type: "pie",
					showInLegend: true,
					toolTipContent: "{y} - <strong>#percent%</strong>",
					dataPoints: [
						{  y: 24, legendText:"Ventas", exploded: true, indexLabel: "Ventas" },
						{  y: 21, legendText:"No Ventas", indexLabel: "No Ventas" },
						{  y: 10, legendText:"Pendientes", indexLabel: "Pendientes" },
					]
				}
				]
			});
			chart.render();
}
window.addEventListener("load",loadHome);
</script>
</body>
</html>