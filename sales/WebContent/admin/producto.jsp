<%@ include file="includes/validate.jsp" %>
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
	document.querySelector("#chkSelectAll").addEventListener("change",function(e){
		var checks = document.querySelectorAll(".listado .content table tbody input");
		for(var i=0;i<checks.length;i++){
			checks[i].checked=this.checked;
		}
	});
	document.querySelector("#subDelete").addEventListener("click",function(e){
		var checks = document.querySelectorAll(".listado .content table tbody input");
		var queryString = "product?type=DELETE";
		for(var i=0;i<checks.length;i++){
			if(checks[i].checked){
				queryString += "&"+checks[i].name+"="+checks[i].value;
			}
		}

		window.location.href=document.querySelector("#contextPath").value+"/admin/"+queryString;
	});
	document.querySelector("#subconfig").addEventListener("click",function(e){
		var checks = document.querySelectorAll(".listado .content table tbody input");
		var check = null;
		var c = 0;
		for(var i=0;i<checks.length;i++){
			if(checks[i].checked){
				c++;
				check = checks[i];
			}
		}
		if(c==0)
			alert("Seleccione un producto a configurar.");
		else if(c>1){
			alert("Solo debe seleccionar un producto.");
		}else{
			window.location.href=document.querySelector("#contextPath").value+"/admin/"+"product?type=CONFIG&productCode="+check.value;
		}
	});
	document.querySelector("#closeModalWindow").addEventListener("click",function(e){
		closeBackgroundWindow('saveForm');
		document.saveForm.reset();
	});
});
function populateForm(tr){
	var form = document.saveForm;
	form.txtCode.value=tr.cells[1].innerHTML;
	form.txtName.value=tr.cells[2].innerHTML;
	form.txtTrademark.value=tr.cells[3].innerHTML;
	var opts = form.cboCategory.options;
	var compareTo = tr.cells[4].innerHTML;
	for(var i=0; i<opts.length;i++){
		if(opts[i].text==compareTo){
			opts[i].selected=true;
			break;
		}
	}
	showBackgroundWindow('saveForm');
	form.txtName.focus();
}
</script>
</head>
<body>
<%@ include file="includes/header.jsp" %>
<div class="subNav">
	<section>Mantenimiento de Productos</section>
	<div id="subAdd" onclick="showBackgroundWindow('saveForm')">AGREGAR</div>
	<div id="subListAll" onclick="window.location.href='<%=request.getContextPath()%>/admin/product?type=ALLPRODUCTS';">LISTAR</div>
	<div id="subDelete">ELIMINAR</div>
	<div id="subconfig" style="width:13%;min-width:175px;">STOCK Y PRECIO</div>
</div>
<div class="listFilter">
	<form action="<%=request.getContextPath()%>/admin/product" method="post" name="findForm">
	<input type="hidden" name="type" value="FIND"/>
	<section>
		<label for="txtFindCode">C&oacute;digo</label>
		<input id="txtFindCode" name="txtCode" type="text" value="${productSearched.code}"/>
	</section>
	<section>
		<label for="txtFindName">Nombre</label>
		<input id="txtFindName" name="txtName" type="text" value="${productSearched.name}"/>
	</section>
	<section>
		<label for="txtFindTrademark">Marca</label>
		<input id="txtFindTrademark" name="txtTrademark" type="text" value="${productSearched.tradeMark}"/>
	</section>
	<section>
		<label for="cboFindCategory">Categor&iacute;a</label>
		<select id="cboFindCategory" name="cboCategory">
			<option value="">SELECCIONAR</option>
			<%List<Category> categories = (List<Category>) request.getAttribute("categories");
				Product productSearched = (Product)request.getAttribute("productSearched");
				if(categories!=null)
					for(Category category : categories){%>
					<option value="<%=category.getCode()%>"<%=productSearched!=null&&productSearched.getCategory().getCode().equals(category.getCode())?" selected='selected'":""%>><%= category.getName()%></option>
			<%}%>
		</select>
	</section>
	<section>
		<label for="chkFindEnabled">habilitado</label>
		<input type="checkbox" name="chkState" id="chkFindEnabled" value="ACT" ${productSearched==null||productSearched.state=="ACT"?"checked='checked'":""} />
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
			<th>NOMBRE</th>
			<th>MARCA</th>
			<th>CATEGOR&Iacute;A</th>
			<th>EDITAR</th>
		</tr>
	</thead>
	<tbody>
	<%List<Product> list = (List<Product>) request.getAttribute("productList");
		if(list!=null)
		for(Product product:list){
	%>
		<tr style="height:35px;">
			<td><input type="checkbox" name="chkProductCode" value="<%=product.getCode()%>"/></td>
			<td><%=product.getCode()%></td>
			<td><%=product.getName()%></td>
			<td><%=product.getTradeMark()%></td>
			<td><%=product.getCategory().getName()%></td>
			<td><img alt="Modificar" src="<%=request.getContextPath()%>/admin/img/pen-point-tip.png" onclick="populateForm(this.parentNode.parentNode)"></td>
		</tr>
		<%}%>
	</tbody>
</table>
</div>
</div>
<div id="background">
	<section id="saveForm">
	<form action="<%=request.getContextPath()%>/admin/product" method="post" name="saveForm">
	<input name="type" type="hidden" value="ADD" />
		<div class="formTitle">GRABAR USUARIO<img id="closeModalWindow" src="<%=request.getContextPath()%>/img/close.png"/></div>
		<div class="formContent">
			<section>
				<label>C&oacute;digo</label>
				<input type="text" name="txtCode"/>
			</section>
			<section>
				<label>Nombre</label>
				<input type="text" name="txtName" />
			</section>
			<section>
				<label>Marca</label>
				<input type="text" name="txtTrademark"/>
			</section>
			<section>
				<label>Categor&iacute;a</label>
				<select name="cboCategory">
				<%if(categories!=null)for(Category category: categories){%>
					<option value="<%=category.getCode()%>"><%=category.getName()%></option>
				<%}%>
				</select>
			</section>
			<section>
				<label>Habilitado</label>
				<input type="checkbox" name="chkState" value="ACT" checked="checked" />
			</section>
		</div>
		<div class="formSubmit"><input type="submit" value="GUARDAR"/></div>
		</form>
	</section>
</div>
</body>
</html>