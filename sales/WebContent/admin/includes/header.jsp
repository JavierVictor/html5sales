<input type="hidden" id="contextPath" value="<%=request.getContextPath()%>"/>
<div class="windowHead">
<div class="userOptions" onclick="window.location.href='<%=request.getContextPath()%>/admin/adminUser?type=LOGOUT';">
<img src="<%=request.getContextPath()%>/admin/img/user.png"/>
<span>${admin.name}</span>
</div>
</div>
<nav>
<div id="navMenuHome" onclick="window.location.href='<%=request.getContextPath()%>/admin/home.jsp';">
	<div></div>
	<span>INICIO</span>
</div>
<div id="navMenuMaintenance">
	<div></div>
	<span>MANTENIMIENTO</span>
</div>
<div id="navMenuConfig" onclick="window.location.href='<%=request.getContextPath()%>/admin/config.jsp';">
	<div></div>
	<span>CONFIGURACI&Oacute;N</span>
</div>
<div id="navMenuReport">
	<div></div>
	<span>REPORTES</span>
</div>
<section id="subMenu">
	<table>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
</section>
</nav>