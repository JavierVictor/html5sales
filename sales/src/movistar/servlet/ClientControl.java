package movistar.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Admin;
import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.ClientType;
import movistar.bean.Company;
import movistar.bean.CreditClient;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.dbacces.ClientAccess;


public class ClientControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    String type = request.getParameter("type");
	    if("findClientsByCriteria".equals(type)){
		findClientsByCriteria(request, response);
	    }else if("ADD".equals(type)){
		add(request,response);
	    }else if("EDIT".equals(type)){
		edit(request,response);
	    }else if("FIND".equals(type)){
		find(request,response);
	    }
	}
	private void find(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException{
	    String code = request.getParameter("txtCode"),
		    name = request.getParameter("txtName"),
		    clientType = request.getParameter("cboClientType"),
		    ruc = request.getParameter("txtRuc"),
		    phone = request.getParameter("txtPhone"),
		    state = request.getParameter("chkState"); 
	    
	    Admin admin = (Admin)request.getSession().getAttribute("admin");
	    Client client =  new Client();
	    client.setCompany(admin.getCompany());
	    client.setCode(code);
	    client.setName(name);
	    client.setPhone(phone);
	    client.setRuc(ruc);
	    client.setClientType(new ClientType());
	    client.getClientType().setCode(clientType);
	    if(state==null) client.setState("INA");
	    
	    List<Client> list = ClientAccess.find(client);
	    request.setAttribute("clientList", list);
	    request.setAttribute("clientSearched",client);
	    getServletContext().getRequestDispatcher("/admin/cliente.jsp").forward(request, response);
	    
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	    String type = request.getParameter("type");
	    if("ADD".equals(type)){
		add(request,response);
	    }else if("ALLCLIENTS".equals(type)){
		getAllClients(request,response);
	    }
	}
	
	private void getAllClients(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
	    Admin admin = (Admin) request.getSession().getAttribute("admin");
	    List<Client> list = ClientAccess.getAllClients(admin.getCompany().getCode());
	    request.setAttribute("clientList", list);
	    getServletContext().getRequestDispatcher("/admin/cliente.jsp").forward(request, response);
	}
	
	private void add(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
	    String code = request.getParameter("txtCode"),
		    name = request.getParameter("txtName"),
		    ruc = request.getParameter("txtRuc"),
		    clientType = request.getParameter("cboClientType"),
		    phone = request.getParameter("txtPhone"),
		    state = request.getParameter("chkState"),
		    latitude = request.getParameter("hdnLatitude"),
		    longitude = request.getParameter("hdnLongitude"),
		    addressName = request.getParameter("hdnAddressName");

	    Admin admin = (Admin)request.getSession().getAttribute("admin");

	    Client client = new Client();
	    client.setCode(code);
	    client.setCompany(admin.getCompany());
	    client.setName(name);
	    client.setRuc(ruc);
	    client.setPhone(phone);
	    client.setState(state!=null?state:"INA");
	    client.setClientType(new ClientType());
	    client.getClientType().setCode(clientType);
	    
	    ClientAddress clientAddress = null;
	    if(latitude!=null && latitude.trim().length()!=0){
		client.setClientAddresses(new ArrayList<ClientAddress>());
		client.getClientAddresses().add(clientAddress);
		clientAddress = new ClientAddress();
		clientAddress.setName(addressName);
		clientAddress.setCode("1");
		clientAddress.setType("P");
		clientAddress.setLatitude(latitude);
		clientAddress.setLongitude(longitude);
	    }
	    
	    ClientAccess.add(client, clientAddress);
	    List<Client> list = new ArrayList<Client>();
	    list.add(client);
	    request.setAttribute("clientList", list);
	    request.setAttribute("clientSearched",client);
	    getServletContext().getRequestDispatcher("/admin/cliente.jsp").forward(request, response);
	}
	private void edit(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
	    String code = request.getParameter("txtCode"),
		    name = request.getParameter("txtName"),
		    ruc = request.getParameter("txtRuc"),
		    clientType = request.getParameter("cboClientType"),
		    phone = request.getParameter("txtPhone"),
		    state = request.getParameter("chkState"),
		    addressCode = request.getParameter("hdnAddressCode"),
		    addressType = request.getParameter("hdnAddressType"),
		    latitude = request.getParameter("hdnLatitude"),
		    longitude = request.getParameter("hdnLongitude"),
		    addressName = request.getParameter("hdnAddressName");

	    Admin admin = (Admin)request.getSession().getAttribute("admin");

	    Client client = new Client();
	    client.setCode(code);
	    client.setCompany(admin.getCompany());
	    client.setName(name);
	    client.setRuc(ruc);
	    client.setPhone(phone);
	    client.setState(state!=null?state:"INA");
	    client.setClientType(new ClientType());
	    client.getClientType().setCode(clientType);
	    ClientAddress clientAddress = null;
	    if(latitude!=null && latitude.length()!=0){
		
        	    if(addressCode==null || addressCode.length()==0){
        		addressCode = "1";
        		addressType = "P";
        	    }
        	    clientAddress = new ClientAddress();
        	    clientAddress.setName(addressName);
        	    clientAddress.setCode(addressCode);
        	    clientAddress.setType(addressType);
        	    clientAddress.setLatitude(latitude);
        	    clientAddress.setLongitude(longitude);
        	    client.setClientAddresses(new ArrayList<ClientAddress>());
        	    client.getClientAddresses().add(clientAddress);
	    }
	    ClientAccess.edit(client,clientAddress);
	    List<Client> list = new ArrayList<Client>();
	    list.add(client);
	    request.setAttribute("clientList", list);
	    request.setAttribute("clientSearched",client);
	    getServletContext().getRequestDispatcher("/admin/cliente.jsp").forward(request, response);
	    //response.sendRedirect("admin/client?type=ALLCLIENTS");
	}

	protected void findClientsByCriteria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/xml;charset=ISO-8859-1");
		response.addHeader("Cache-Control", "no-cache");

		String clientName=request.getParameter("clientName");
		String clientCode=request.getParameter("clientCode");
		String ruc=request.getParameter("ruc");
		
		clientName = clientName!=null&&clientName.trim().length()==0?null:clientName;
		clientCode = clientCode!=null&&clientCode.trim().length()==0?null:clientCode;
		ruc = ruc!=null&&ruc.trim().length()==0?null:ruc;
		
		User user = getUserFromRequest(request);
		List<CreditClient> clients = ClientAccess.findClientsByCriteria(user,clientCode,ruc,clientName);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<clients>");
		for(CreditClient cc : clients){
			sb.append("<client>");
			sb.append("<code>" + cc.getClient().getCode() + "</code>");
			sb.append("<name>" + cc.getClient().getName() + "</name>");
			sb.append("<ruc>" + cc.getClient().getRuc() + "</ruc>");
			sb.append("<currencyCode>" + cc.getCurrency().getCode() + "</currencyCode>");
			sb.append("<currencyName>" + cc.getCurrency().getName() + "</currencyName>");
			sb.append("<currencySymbol>" + cc.getCurrency().getSymbol() + "</currencySymbol>");
			sb.append("<clientTypeCode>" + cc.getClient().getClientType().getCode() + "</clientTypeCode>");
			sb.append("<clientTypeName>" + cc.getClient().getClientType().getName() + "</clientTypeName>");
			sb.append("<amount>" + cc.getAmount() + "</amount>");
			sb.append("</client>");
		}
		sb.append("</clients>");
		response.getWriter().write(sb.toString());
	}
	private User getUserFromRequest(HttpServletRequest request){
		if(request.getParameter("userCode")!=null){
			User user = new User();
			user.setCode(request.getParameter("userCode"));
			user.setCompany(new Company());
			user.getCompany().setCode(request.getParameter("companyCode"));
			user.setWarehouse(new Warehouse());
			user.getWarehouse().setCode(request.getParameter("warehouseCode"));
			return user;
		}
		return null;
	}
}
