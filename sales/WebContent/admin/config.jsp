<%@page import="movistar.bean.Currency"%>
<%@page import="movistar.bean.Configuration"%>
<%@ include file="includes/validate.jsp" %>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
List<Currency> currencies = (List<Currency>)session.getAttribute("currencies");
for(Currency currency: currencies){
    if(currency.isLocal()){
		pageContext.setAttribute("currency", currency);
		break;
    }
} %>
<!DOCTYPE html>
<html>
<head>
<title>BIENVENIDO ${user.name} al Sistema</title>
<%@ include file="includes/head.jsp" %>
<script type="text/javascript">
function replaceStrings(){
	var currency = document.querySelector("#currency").value.split("||");
	var rows = document.querySelectorAll("#configTable tr");
	
	for(var i=0;i<rows.length;i++){
		rows[i].cells[2].innerHTML=rows[i].cells[2].innerHTML.replace("{0}",currency[2]);
	}
	
}
addEventListener("load",replaceStrings);
</script>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<input type="hidden" value='${currency.code}||${currency.name}||${currency.symbol}' id="currency"/>
<div class="subNav">
	<section style="background-image: url('img/cog24.png')">Configuraci&oacute;n de par&aacute;metros generales</section>
</div>
<div style="width:98%;height:auto;border:1px solid #003245;border-radius:10px;margin:0px auto;background-color:rgba(255,255,255,0.8);padding:1% 0%;">
	<form action="<%=request.getContextPath()%>/admin/configuration" method="post" enctype="multipart/form-data">
	<input  type="hidden" name="type" value="SAVE"  />
	<table style="border-collapse:separate;border-spacing:5px;" id="configTable">
		<tbody>
		<%
		List<Configuration> configurations = (List<Configuration>) session.getAttribute("configurations");
		for(Configuration configuration:configurations){%>
		<tr>
			<td style="font-size:14px;text-indent:10px;color:#006490;"><%=configuration.getName()%></td>
			<td style="text-indent:10px;"><input  autocomplete="off" type="text" name="<%=configuration.getCode()%>" value="<%=configuration.getValue()%>" style="border:1px solid #006490"  /></td>
			<td style="font-size:12px;text-indent:10px;color:#00c6d7;"><%=configuration.getDescription()%></td>
		</tr>
		<%}%>
		<tr>
			<td style="font-size:14px;text-indent:10px;color:#006490;">Descarga de archivos</td>
			<td style="text-indent:10px;"><input type="date" style="width:131px;border:1px solid #006490" name="dateDownloadFile" value="2015-09-06"/></td>
			<td style="font-size:12px;text-indent:10px;color:#00c6d7;"><input type="button" value="DESCARGAR" class="btnInsideForm"  style="background-image:url('img/download.png');"/></td>
		</tr>
		<tr>
			<td style="font-size:14px;text-indent:10px;color:#006490;">Subida de archivos</td>
			<td style="text-indent:10px;"><input type="file" name="uploadFile" accept=".txt,.dat,.zip" style="display:none" id="uploadFile"/><input class="btnInsideForm" style="background-image:url('img/upload.png');" type="button" value="SUBIR" onclick="document.querySelector('#uploadFile').click();" /></td>
			<td style="font-size:12px;text-indent:10px;color:#00c6d7;">Seleccionar el archivo a subir para ser procesado por el servidor</td>
		</tr>
		</tbody>
	</table>
	<input type="submit" value="GRABAR" class="btnForm" style="margin:1%;width:12%;background-image:url('img/save.png');"/>
	<input type="reset" value="LIMPIAR" class="btnForm	" style="margin:1%;width:12%;background-image:url('img/clear.png');"/>
	</form>
</div>
</body>
</html>