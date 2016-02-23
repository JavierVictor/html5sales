<%@page import="movistar.bean.Admin"%>
<%
	Admin admin = (Admin) session.getAttribute("admin");
	if(admin==null){
	    response.sendRedirect(request.getContextPath()+"/admin");
	    return;
	}
%>