package movistar.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import movistar.bean.Admin;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.dbacces.UserAccess;
import movistar.util.XMLUtil;

public class UserControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if("logout".equals(type)){
			request.getSession(true).invalidate();
			response.sendRedirect("index.jsp");
		}else if("DELETE".equals(type)){
			delete(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if("login".equals(type)){
			autenticar(request,response);
		}else if("ADD".equals(type)){
			add(request,response);
		}else if("FIND".equals(type)){
			find(request,response);
		}
	}

	private void autenticar(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=ISO-8859-1");
		response.addHeader("Cache-Control", "no-cache");
		String usercode = request.getParameter("txtUserCode");
		String password = request.getParameter("txtPassword");
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		
		User user = UserAccess.authUser(usercode, password,latitude,longitude);
		StringBuilder xml = new StringBuilder("<xml>");
		if(user==null){
			xml.append("<user><error>ERROR EN USUARIO O PASSWORD</error></user>");
		}else{
			try {
				XMLUtil.getXMLFromEntity(user, xml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		xml.append("</xml>");
		response.getWriter().write(xml.toString());
	}
	
	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String warehouseCode = request.getParameter("cboWarehouse"),
				code = request.getParameter("txtCode"),
				name = request.getParameter("txtName"),
				password = request.getParameter("txtPassword"),
				phonenumber = request.getParameter("txtPhoneNumber"),
				state = request.getParameter("chkState");
		
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		
		User user = new User();
		user.setCompany(admin.getCompany());
		user.setWarehouse(new Warehouse());
		user.getWarehouse().setCode(warehouseCode);
		user.setCode(code);
		user.setName(name);
		user.setPassword(password);
		user.setPhoneNumber(phonenumber);
		user.setState(state!=null?state:"INA");
		UserAccess.saveUser(user);
		request.getSession().setAttribute("users", UserAccess.getAllUsers(admin.getCompany().getCode()));
		response.sendRedirect("usuario.jsp");
	}
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String [] usercodes = request.getParameterValues("chkUserCode");
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		
		UserAccess.delete(admin.getCompany().getCode(),usercodes);
		request.getSession().setAttribute("users", UserAccess.getAllUsers(admin.getCompany().getCode()));
		response.sendRedirect("usuario.jsp");
	}
	private void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String warehouseCode = request.getParameter("cboWarehouse"),
				code = request.getParameter("txtCode"),
				name = request.getParameter("txtName"),
				state = request.getParameter("chkState"),
				phoneNumber = request.getParameter("txtPhoneNumber");
				
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		User user = new User();
		user.setCompany(admin.getCompany());
		user.setWarehouse(new Warehouse());
		user.getWarehouse().setCode(warehouseCode);
		user.setCode(code);
		user.setName(name);
		user.setPhoneNumber(phoneNumber);
		user.setState(state);
		
		List<User> userList = (List<User>) UserAccess.find(user);
		request.setAttribute("userSearched", user);
		request.setAttribute("userList", userList);
		getServletContext().getRequestDispatcher("/admin/usuario.jsp").forward(request, response);
	}
}