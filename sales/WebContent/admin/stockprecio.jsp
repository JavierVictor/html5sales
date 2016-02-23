<%@ include file="includes/validate.jsp" %>
<%@page import="movistar.bean.ProductPrice"%>
<%@page import="movistar.bean.PriceList"%>
<%@page import="movistar.bean.Currency"%>
<%@page import="movistar.bean.Product"%>
<%@page import="movistar.bean.Category"%>
<%@page import="movistar.bean.Warehouse"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><!DOCTYPE html>
<html>
<head>
<title>BIENVENIDO ${user.name} al Sistema</title>
<%@ include file="includes/head.jsp" %>
<script type="text/javascript">
window.addEventListener("load",function(evt){
});
</script>
</head>
<body>
<%@ include file="includes/header.jsp"%>
<section style="width:98%;margin: 10px auto;height:15%;border:1px solid #003245;border-radius:10px;background-color:rgba(255,255,255,0.8);">Stock y precio de producto:<br/>Codigo : ${product.code} / Categoria : ${product.category.name} / Descripcion : ${product.name}</section>
<section style="width:98%;margin: 10px auto;height:auto;border:1px solid #003245;border-radius:10px;background-color:rgba(255,255,255,0.8);">
<form method="post" action="<%=request.getContextPath()%>/admin/product">
<input type="hidden" name="productCode" value="${product.code}" />
<input type="hidden" name="type" value="SAVE_CONFIG"/>
<section style="display:inline-block;width:20%;margin-left:1%;">
	<label style="display:block;width:100%;color:#006490">Almac&eacute;n</label>
	<select style="width:100%;" name="cboWarehouse">
	<%List<Warehouse> warehouses = (List<Warehouse>) application.getAttribute("warehouses"); 
					for(Warehouse warehouse : warehouses){%>
					<option value="<%=warehouse.getCode()%>"><%= warehouse.getName()%></option>
			<%}%>
	</select>
</section>
<section style="display:inline-block;width:20%;margin-left:1%;">
	<label style="display:block;width:100%;;color:#006490">C&oacute;digo</label>
	<input type="text" style="width:100%;" name="txtCode"/>
</section>
<section style="display:inline-block;width:20%;margin-left:1%;">
	<label style="display:block;width:100%;;color:#006490">Descripci&oacute;n</label>
	<input type="text" style="width:100%;" name="txtName"/>
</section>
<section style="display:inline-block;width:20%;margin-left:1%;">
	<label style="display:block;width:100%;;color:#006490">Stock</label>
	<input type="text" style="width:100%;" name="txtStock"/>
</section>
<%
List<Currency> currencies = (List<Currency>)session.getAttribute("currencies");
List<PriceList> priceLists = (List<PriceList>)request.getAttribute("priceLists");
%>
<section style="width:100%;margin:1%;">
	<%for(PriceList priceList:priceLists){ %>
		<div style="width:20%;display:inline-block;border:1px solid #808080;margin-left:1%;border-radius:5px;overflow:hidden;">
			<div style="background-color:#D5EEF3;color:#003245;text-align:center;border-bottom:1px solid #003245;">Precios para <%=priceList.getName()%></div>
			<%for(Currency currency:currencies){%>
				<div style="width:100%;">
					<label style="width:80%;display:block;margin: 0px auto;color:#006490;font-size:12px;"><%=currency.getName()%></label>
					<input style="display:block;width:80%;margin:5px auto;" type="text" name="<%=priceList.getCode()+"||" + currency.getCode() %>" placeholder="<%=currency.getSymbol()%>" />
				</div>
			<%}%>
		</div>
	<%}%>
</section>
<input type="submit" style="display:inline-block;width:10%;height:30px;background-color:rgba(213,238,243,0.8);border:1px solid #003245;color:#003245;font-weight:bold;text-align:center;border-radius:5px;margin:1%;cursor:pointer;" onclick="document.forms[0].submit();" value="GRABAR"/>
<input type="button" style="display:inline-block;width:10%;height:30px;background-color:rgba(213,238,243,0.8);border:1px solid #003245;color:#003245;font-weight:bold;text-align:center;border-radius:5px;margin:1%;cursor:pointer;" onclick="window.location.href='<%=request.getContextPath() %>/product?type=ALLPRODUCTS';" value="CANCELAR"/>
</form>
</section>
<section class="listado">
	<div class="content">
		<table>
			<thead>
				<tr>
					<th>ALMAC&Eacute;N</th>
					<th>UNIDAD</th>
					<th>STOCK</th>
					<th>Lista de Precio</th>
					<th>MONEDA</th>
					<th>PRECIO</th>
					<th>EDITAR</th>
				</tr>
			</thead>
			<tbody>
				<%List<ProductPrice> productPrices = (List<ProductPrice>)request.getAttribute("productPrices");
					for(ProductPrice pp : productPrices){
				%>
					<tr>
						<td><%=pp.getProductUnity().getWarehouse().getName() %></td>
						<td><%=pp.getProductUnity().getCode() %></td>
						<td><%=pp.getProductUnity().getStock()%></td>
						<td><%=pp.getPriceList().getName() %></td>
						<td><%=pp.getCurrency().getName() %></td>
						<td><%=pp.getPrice() %></td>
						<td><img alt="Modificar" src="<%=request.getContextPath()%>/admin/img/pen-point-tip.png" onclick="populateForm(this.parentNode.parentNode)"></td>
					</tr>
				<%}%>
			</tbody>
		</table>
	</div>
</section>
</body>
</html>