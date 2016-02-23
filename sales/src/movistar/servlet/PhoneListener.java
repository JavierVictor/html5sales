package movistar.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.Company;
import movistar.bean.Currency;
import movistar.bean.Entity;
import movistar.bean.Itinerary;
import movistar.bean.NoSale;
import movistar.bean.PriceList;
import movistar.bean.Product;
import movistar.bean.ProductPrice;
import movistar.bean.ProductUnity;
import movistar.bean.Sale;
import movistar.bean.SaleDetail;
import movistar.bean.SaleMode;
import movistar.bean.SaleType;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.dbacces.ClientAccess;
import movistar.dbacces.ProductAccess;
import movistar.dbacces.SaleAccess;
import movistar.dbacces.UserAccess;
import movistar.util.XMLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhoneListener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");

		if ("AUTH".equals(type)) {
			auth(request, response);
		} else if ("UPDATE_ALL".equals(type)) {
			updateAll(request, response);
		} else if ("UPDATE_IN".equals(type)) {
			updateIn(request, response);
		} else if ("UPDATE_OUT".equals(type)) {
			updateOut(request, response);
		} else if ("SEND_SALE".equals(type)) {
			sendSale(request, response);
		} else if ("UPDATE_STOCK".equals(type)) {
			updateStock(request, response);
		} else if ("FIND_CLIENT".equals(type)) {
			findClient(request, response);
		}else if ("FIND_ANOTHER_CLIENT".equals(type)) {
			findAnotherClient(request, response);
		}
	}

	private void auth(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO VALIDAR Y ENVIAR UN NUMERO DE STATUS DE RESPUESTA
		try {
			String usercode = request.getParameter("usercode");
			String password = request.getParameter("password");
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
			
			User u = UserAccess.authUser(usercode, password,latitude,longitude);
			response.getOutputStream().write(u.toByteArray());
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	private void updateIn(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/xml;charset=ISO-8859-1");
			response.addHeader("Cache-Control", "no-cache");

			User u = getUserFromRequest(request);
			
			List<Entity> categories = ProductAccess.getAllCategories(u, null);
			List<Entity> priceLists = ProductAccess.getAllPriceLists(u, null);
			List<Entity> currencies = ProductAccess.getAllCurrencies(u, null);
			List<Entity> saleModes = SaleAccess.getAllSaleModes(u);
			List<ProductPrice> productPrices = ProductAccess.getAllProductsFromSynchro(u, categories, priceLists, currencies);
			List<Itinerary> itineraries = ClientAccess.getAllClientsfromSynchro(u);
			List<NoSale> noSales = SaleAccess.getAllNosales(u);
			
			StringBuilder xml = new StringBuilder();
			xml.append("<UPDATEIN>");

			xml.append("<categories>");
			for(Entity entity:categories){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</categories>");

			xml.append("<pricelists>");
			for(Entity entity:priceLists){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</pricelists>");
			xml.append("<currencies>");
			for(Entity entity:currencies){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</currencies>");
			xml.append("<salemodes>");
			for(Entity entity:saleModes){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</salemodes>");
			xml.append("<nosales>");
			for(Entity entity:noSales){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</nosales>");
			xml.append("<itineraries>");
			for(Entity entity:itineraries){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</itineraries>");

			xml.append("<productprices>");
			for(Entity entity:productPrices){
				XMLUtil.getXMLFromEntity(entity, xml);
			}
			xml.append("</productprices>");
			xml.append("</UPDATEIN>");

			response.getWriter().write(xml.toString());
			response.getWriter().flush();
			System.out.println(xml.length());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateOut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		User user = getUserFromRequest(request);
		System.out.println(request.getContentLength());	
		JSONArray sales = new JSONArray(request.getParameter("sales"));
		StringBuilder sb = new StringBuilder();
		sb.append("<result>");
		sb.append("<sales>");
		for(int i=0;i<sales.length();i++){
			JSONObject object = sales.getJSONObject(i);
			Sale sale = new Sale();
			sale.setCode(object.getLong("code") +"");
			sale.setComment(object.getString("obs"));
			sale.setSaleType(new SaleType());
			sale.getSaleType().setCode(object.getString("saleTypeCode"));
			sale.setClientAddress(new ClientAddress());
			sale.getClientAddress().setClient(new Client());
			sale.getClientAddress().getClient().setCode(object.getString("clientCode"));
			sale.getClientAddress().setCode(object.getString("clientAddressCode"));
			sale.setItinerary(object.getString("itineraryCode"));
			sale.setLatitude(object.get("latitude").toString());
			sale.setLongitude(object.get("longitude").toString());
			sale.setMobileDate(object.getString("mobileDate"));
			sale.setUser(user);
			if("N".equals(sale.getSaleType().getCode())){
				sale.setNoSale(new NoSale());
				sale.getNoSale().setCode(object.getString("noSaleCode"));
			}else if("V".equals(sale.getSaleType().getCode())){
				sale.setCurrency(new Currency());
				sale.setSaleMode(new SaleMode());
				sale.setPriceList(new PriceList());
				sale.getCurrency().setCode(object.getString("currencyCode"));
				sale.getSaleMode().setCode(object.getString("saleModeCode"));
				sale.getPriceList().setCode(object.getString("priceListCode"));
				sale.setDeliveryDate(sdf.parse(object.getString("deliveryDate")));
				sale.setSaleDetails(new ArrayList<SaleDetail>());
				JSONArray array = object.getJSONArray("details");
				for(int j=0;j<array.length();j++){
					JSONObject detail = array.getJSONObject(j);
					SaleDetail saleDetail = new SaleDetail();
					saleDetail.setItem(detail.getInt("item"));
					saleDetail.setDiscount(detail.getDouble("discount"));
					saleDetail.setPrice(detail.getDouble("price"));
					saleDetail.setEditedPrice(detail.getDouble("editedPrice"));
					saleDetail.setQuantity(detail.getInt("quantity"));
					saleDetail.setProductUnity(new ProductUnity());
					saleDetail.getProductUnity().setProduct(new Product());
					saleDetail.getProductUnity().setWarehouse(new Warehouse());
					saleDetail.getProductUnity().setCode(detail.getString("productUnityCode"));
					saleDetail.getProductUnity().getWarehouse().setCode(detail.getString("warehouseCode"));
					saleDetail.getProductUnity().getProduct().setCode(detail.getString("productCode"));
					saleDetail.setSubtotal(detail.getDouble("subtotal"));
					sale.getSaleDetails().add(saleDetail);
				}
			}
			sb.append("<code itineraryCode='"+sale.getItinerary()+"' clientCode='" + sale.getClientAddress().getClient().getCode() + "' clientAddressCode='" + sale.getClientAddress().getCode() + "'><webcode>").append(sale.getCode()).append("</webcode><servercode>");
			SaleAccess.saveSale(sale);
			sb.append(sale.getCode()).append("</servercode></code>");
		}
		sb.append("</sales></result>");
		response.getWriter().write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendSale(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/xml;charset=ISO-8859-1");
	    response.addHeader("Cache-Control", "no-cache");
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    User user = getUserFromRequest(request);
	    
	    
	    JSONObject object = new JSONObject(request.getParameter("sale"));
		Sale sale = new Sale();
		sale.setCode(object.getString("code"));
		sale.setComment(object.getString("obs"));
		sale.setSaleType(new SaleType());
		sale.getSaleType().setCode(object.getString("saleTypeCode"));
		sale.setClientAddress(new ClientAddress());
		sale.getClientAddress().setClient(new Client());
		sale.getClientAddress().getClient().setCode(object.getString("clientCode"));
		sale.getClientAddress().setCode(object.getString("clientAddressCode"));
		sale.setItinerary(object.getString("itineraryCode"));
		sale.setLatitude(object.get("latitude").toString());
		sale.setLongitude(object.get("longitude").toString());
		sale.setMobileDate(object.getString("mobileDate"));
		sale.setUser(user);
		if("N".equals(sale.getSaleType().getCode())){
			sale.setNoSale(new NoSale());
			sale.getNoSale().setCode(object.getString("noSaleCode"));
		}else if("V".equals(sale.getSaleType().getCode())){
			sale.setCurrency(new Currency());
			sale.setSaleMode(new SaleMode());
			sale.setPriceList(new PriceList());
			sale.getCurrency().setCode(object.getString("currencyCode"));
			sale.getSaleMode().setCode(object.getString("saleModeCode"));
			sale.getPriceList().setCode(object.getString("priceListCode"));
			try {
			    sale.setDeliveryDate(sdf.parse(object.getString("deliveryDate")));
			}catch (ParseException e) {
			    e.printStackTrace();
			}
			sale.setSaleDetails(new ArrayList<SaleDetail>());
			JSONArray array = object.getJSONArray("details");
			for(int j=0;j<array.length();j++){
				JSONObject detail = array.getJSONObject(j);
				SaleDetail saleDetail = new SaleDetail();
				saleDetail.setItem(detail.getInt("item"));
				saleDetail.setDiscount(detail.getDouble("discount"));
				saleDetail.setPrice(detail.getDouble("price"));
				saleDetail.setEditedPrice(detail.getDouble("editedPrice"));
				saleDetail.setQuantity(detail.getInt("quantity"));
				saleDetail.setProductUnity(new ProductUnity());
				saleDetail.getProductUnity().setProduct(new Product());
				saleDetail.getProductUnity().setWarehouse(new Warehouse());
				saleDetail.getProductUnity().setCode(detail.getString("productUnityCode"));
				saleDetail.getProductUnity().getWarehouse().setCode(detail.getString("warehouseCode"));
				saleDetail.getProductUnity().getProduct().setCode(detail.getString("productCode"));
				saleDetail.setSubtotal(detail.getDouble("subtotal"));
				sale.getSaleDetails().add(saleDetail);
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<code itineraryCode='"+sale.getItinerary()+"' clientCode='" + sale.getClientAddress().getClient().getCode() + "' clientAddressCode='" + sale.getClientAddress().getCode() + "'><webcode>").append(sale.getCode()).append("</webcode><servercode>");
		SaleAccess.saveSale(sale);
		sb.append(sale.getCode()).append("</servercode></code>");
		response.getWriter().write(sb.toString());
	}

	private void updateStock(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	private void findClient(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void findAnotherClient(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=ISO-8859-1");
		response.addHeader("Cache-Control", "no-cache");
		try {
			
			User user = getUserFromRequest(request);
			StringBuilder sb = new StringBuilder("<result>");
			String filter = request.getParameter("filter");
			List<Client> lista = ClientAccess.findClientsByCtriteria(user, filter);
			
			for(Client client:lista){
				XMLUtil.getXMLFromEntity(client, sb);
			}

			sb.append("</result>");

			response.getWriter().append(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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