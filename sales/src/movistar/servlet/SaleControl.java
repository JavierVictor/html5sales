package movistar.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Admin;
import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.Itinerary;
import movistar.bean.NoSale;
import movistar.bean.Sale;
import movistar.bean.SaleDetail;
import movistar.bean.SaleMode;
import movistar.bean.SaleType;
import movistar.bean.User;
import movistar.dbacces.SaleAccess;
import movistar.util.XMLUtil;


public class SaleControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");

		if("resumeSale".equals(type)){
			resumeSale(request,response);
		}else if("saveSale".equals(type)){
			saveSale(request, response);
		}else if("saveNoSale".equals(type)){
			saveNoSale(request, response);
		}else if("SALESBYUSER".equals(type)){
		    salesByUser(request,response);
		}else if("DETAILSBYSALE".equals(type)){
		    detailsBySale(request,response);
		}
	}
	private void detailsBySale(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/xml;charset=ISO-8859-1");
	    response.addHeader("Cache-Control", "no-cache");
	    String saleCode = request.getParameter("saleCode");
	    Admin admin = (Admin) request.getSession().getAttribute("admin");
	    List<SaleDetail> details = SaleAccess.getDetailsBySale(admin.getCompany().getCode(),saleCode);
	    StringBuilder sb = new StringBuilder("<details>");
	    for(SaleDetail detail: details){
		try{
		    XMLUtil.getXMLFromEntity(detail, sb);
		}catch(Exception e){
		    e.printStackTrace();
		}
	    }
	    sb.append("</details>");
	    response.getWriter().write(sb.toString());
	}


	private void salesByUser(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/xml;charset=ISO-8859-1");
	    response.addHeader("Cache-Control", "no-cache");

	    String warehouseCode = request.getParameter("warehouseCode");
	    String userCode = request.getParameter("userCode");
	    String startDate = request.getParameter("startDate");
	    String endDate = request.getParameter("endDate");
	    Admin admin = (Admin) request.getSession().getAttribute("admin");
	    List<Sale> sales = SaleAccess.getSalesByUser(admin.getCompany().getCode(), warehouseCode, userCode, startDate, endDate);
	    StringBuilder sb = new StringBuilder("<sales>");
	    for(Sale sale: sales){
		try{
		    XMLUtil.getXMLFromEntity(sale, sb);
		}catch(Exception e){
		    e.printStackTrace();
		}
	    }
	    sb.append("</sales>");
	    System.out.println(sb);
	    response.getWriter().write(sb.toString());
	}
	private void saveNoSale(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String noSaleCode = request.getParameter("noSaleCode");
		String comment = request.getParameter("comment");
		String saleTypeCode=request.getParameter("saleTypeCode");
		String clientCode=request.getParameter("clientCode");
		String clientAddressCode=request.getParameter("clientAddressCode");
		User user = (User) request.getSession().getAttribute("user");
		
		Sale sale = new Sale();
		sale.setClientAddress(new ClientAddress());
		sale.getClientAddress().setCode(clientAddressCode);
		sale.getClientAddress().setClient(new Client());
		sale.getClientAddress().getClient().setCode(clientCode);
		sale.setNoSale(new NoSale());
		sale.getNoSale().setCode(noSaleCode);
		sale.setSaleType(new SaleType());
		sale.getSaleType().setCode(saleTypeCode);
		sale.setComment(comment);
		sale.setUser(user);
		//TODO hacer algo con el valor recuperado de la venta
		String key = SaleAccess.saveSale(sale);
		
		List<Itinerary> itineraries = (List<Itinerary>) request.getSession().getAttribute("itineraries");
		for(Itinerary itinerary:itineraries){
			if(itinerary.getClientAddress().getClient().getCode().equals(clientCode) 
					&& itinerary.getClientAddress().getCode().equals(clientAddressCode)){
				itinerary.setState(sale.getSaleType().getCode());
				break;
			}
		}
		
		response.sendRedirect("ruta.jsp");
	}

	private void saveSale(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		try {
		Sale sale = (Sale) request.getSession().getAttribute("sale");
		String deliveryClientAddressCode = request.getParameter("deliveryClientAddressCode");
		String clientAddressCode = request.getParameter("clientAddressCode");
		String saleModeCode = request.getParameter("saleModeCode");
		String deliveryDate = request.getParameter("deliveryDate");
		String comment = request.getParameter("comment");
		
		
		sale.getClientAddress().setCode(deliveryClientAddressCode);
		sale.setSaleMode(new SaleMode());
		sale.getSaleMode().setCode(saleModeCode);
		try{
			sale.setDeliveryDate(SimpleDateFormat.getDateInstance().parse(deliveryDate));
		}catch (Exception e) {
			e.printStackTrace();
		}
		sale.setComment(comment);
		
		//TODO hacer algo con el key generado
		String key = SaleAccess.saveSale(sale);
		List<Itinerary> itineraries = (List<Itinerary>) request.getSession().getAttribute("itineraries");
		for(Itinerary itinerary:itineraries){
			if(itinerary.getClientAddress().getClient().getCode().equals(sale.getClientAddress().getClient().getCode()) 
					&& itinerary.getClientAddress().getCode().equals(clientAddressCode)){
				itinerary.setState(sale.getSaleType().getCode());
				break;
			}
		}
		
		request.getSession().removeAttribute("sale");
		response.sendRedirect("ruta.jsp");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void resumeSale(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
	}



}
