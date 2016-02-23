package movistar.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Admin;
import movistar.dbacces.AdminAccess;
import movistar.dbacces.ConfigurationAccess;
import movistar.dbacces.ProductAccess;
import movistar.dbacces.UserAccess;

public class AdminControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String type = request.getParameter("type");
	    if("LOGOUT".equals(type)){
		logout(request,response);
	    }
	}
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.getSession().invalidate();
	    response.sendRedirect(request.getContextPath()+"/admin");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if("AUTHENTICATE".equals(type)){
			authenticate(request, response);
		}
	}
	protected void authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("txtCode");
		String password = request.getParameter("txtPassword");
		String companyCode = request.getParameter("cboCompanyCode");
		
		Admin admin = AdminAccess.authenticate(code, password, companyCode);
		if(admin!=null){
			request.getSession().setAttribute("admin",admin);
			request.getSession().setAttribute("configurations", ConfigurationAccess.getAllConfiguration(admin.getCompany().getCode()));
			request.getSession().setAttribute("currencies", ProductAccess.getAllCurrencies(admin.getCompany().getCode()));
			request.getSession().setAttribute("users", UserAccess.getAllUsers(admin.getCompany().getCode()));
			response.sendRedirect("home.jsp");
		}else{
			request.setAttribute("message", "Usuario y/o clave incorrectos");
			getServletContext().getRequestDispatcher("/admin/").forward(request, response);
		}
	}
	
}