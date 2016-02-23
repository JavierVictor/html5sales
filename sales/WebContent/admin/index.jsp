<%@page import="movistar.bean.Entity"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LOGIN</title>
<link href="<%=request.getContextPath()%>/style/css.css" rel="stylesheet" />
</head>
<body>
	 <div style="background-color:#FFF;position:relative;width:300px;height:200px;border: 2px solid #003245;top:50%;left:50%;margin-left:-150px;margin-top:-100px;border-radius:15px;box-shadow:5px 5px 15px 0px rgba(100,100,100,0.5);">
		<form action="<%=request.getContextPath()%>/admin/adminUser" method="post">
			<input type="hidden" name="type" value="AUTHENTICATE"/>
			<input type="hidden" name="message" value="${message}"/>
			<select name="cboCompanyCode" style="width:90%;margin-left:5%;margin-top:10%;height:15%;border-radius:5px;border-color:#003245;text-align:center;">
				<%List<Entity> list = (List<Entity>) application.getAttribute("companies");
					for(Entity entity:list){
				%>
				<option value="<%=entity.getCode()%>"><%=entity.getName()%></option>
				<%}%>
			</select>
	 		<input type="text" name="txtCode" placeholder="USUARIO" style="width:90%;margin-left:5%;margin-top:2%;height:15%;border-radius:5px;border-color:#003245;text-align:center;"/>
	 		<input type="password" name="txtPassword" placeholder="CLAVE" style="width:90%;margin-left:5%;margin-top:2%;height:15%;border-radius:5px;border-color:#003245;text-align:center;"/>
	 		<input type="submit" value="ENTRAR" style="width:43%;margin-left:5%;margin-top:2%;height:14%;background-color:#FFF;color:#003245;border-color:#003245;font-weight:bold;"/>
	 		<input type="reset" value="LIMPIAR" style="width:43%;margin-left:4%;margin-top:2%;height:14%;background-color:#FFF;color:#003245;border-color:#003245;font-weight:bold;"/>
		</form>
 	</div>
 	
</body>
</html>