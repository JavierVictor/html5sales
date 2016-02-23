package movistar.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Admin;
import movistar.bean.Category;
import movistar.bean.Company;
import movistar.bean.Currency;
import movistar.bean.PriceList;
import movistar.bean.Product;
import movistar.bean.ProductPrice;
import movistar.bean.ProductUnity;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.dbacces.ProductAccess;

public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if("ALLPRODUCTS".equals(type)){
		    getAllProducts(request,response);
		}else if("DELETE".equals(type)){
		    delete(request,response);
		}else if("CONFIG".equals(type)){
		    configurate(request,response);
		}
	}
	private void configurate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	    Admin admin = (Admin)request.getSession().getAttribute("admin");
	    String productCode = request.getParameter("productCode");
	    
	    List<Currency> currencies = ProductAccess.getAllCurrencies(admin.getCompany().getCode());
	    List<PriceList> priceLists = ProductAccess.getAllPriceLists(admin.getCompany().getCode());
	    List<Category> categories = ProductAccess.getAllCategories(admin.getCompany().getCode());
	    
	    Product product = new Product();
	    product.setCompany(admin.getCompany());
	    product.setCode(productCode);
	    List products = ProductAccess.find(product, categories);  
	    List productPrices = ProductAccess.getProductPricesFromProduct(product);
	    product = (Product) products.get(0);
	    request.setAttribute("currencies", currencies);
	    request.setAttribute("priceLists", priceLists);
	    request.setAttribute("productPrices", productPrices);
	    request.setAttribute("product", product);
	    getServletContext().getRequestDispatcher("/admin/stockprecio.jsp").forward(request, response);
	    
	}
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String [] productcodes = request.getParameterValues("chkProductCode");
		
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		ProductAccess.delete(admin.getCompany().getCode(),productcodes);
		response.sendRedirect("product?type=ALLPRODUCTS");
	}
	
	private void getAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Admin admin = (Admin )request.getSession().getAttribute("admin");
		String companyCode = admin.getCompany().getCode();
		List<Category> categories = ProductAccess.getAllCategories(companyCode);
		List<Product> list = ProductAccess.getAllProducts(companyCode,categories);
		request.setAttribute("productList", list);
		request.setAttribute("categories", categories);
		getServletContext().getRequestDispatcher("/admin/producto.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if("asynchroGETALL".equals(type)){
		    asynchroGETALL(request, response);
		}else if("findProductsByCriteria".equals(type)){
		    findProductsByCriteria(request,response);
		}else if("ADD".equals(type)){
		    add(request,response);
		}else if("FIND".equals(type)){
		    find(request,response);
		}else if("SAVE_CONFIG".equals(type)){
		    saveConfig(request,response);
		}
	}
	
	private void saveConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String warehouse = request.getParameter("cboWarehouse"),
		    code = request.getParameter("txtCode"),
		    name = request.getParameter("txtName"),
		    stock = request.getParameter("txtStock"),
		    product = request.getParameter("productCode");
	    Admin admin = (Admin)request.getSession().getAttribute("admin");
	    
	    ProductUnity productUnity = new ProductUnity();
	    productUnity.setCompany(admin.getCompany());
	    productUnity.setCode(code);
	    productUnity.setName(name);
	    productUnity.setProduct(new Product());
	    productUnity.getProduct().setCode(product);
	    productUnity.setStock(Double.parseDouble(stock));
	    productUnity.setWarehouse(new Warehouse());
	    productUnity.getWarehouse().setCode(warehouse);
	    
	    List<Currency> currencies = ProductAccess.getAllCurrencies(admin.getCompany().getCode());
	    List<PriceList> priceLists = ProductAccess.getAllPriceLists(admin.getCompany().getCode());
	    
	    List<ProductPrice> productPrices = new ArrayList<ProductPrice>();
	    for(Currency currency:currencies){
		for(PriceList priceList:priceLists){
		    String value = request.getParameter(priceList.getCode() + "||" + currency.getCode());
		    if("".equals(value.trim()))
			continue;
		    ProductPrice productPrice = new ProductPrice();
		    productPrice.setProductUnity(productUnity);
		    productPrice.setCurrency(currency);
		    productPrice.setPrice(Double.parseDouble(value));
		    productPrice.setPriceList(priceList);
		    productPrices.add(productPrice);
		}
	    }
	    ProductAccess.saveConfig(productUnity,productPrices);
	    response.sendRedirect("product?type=ALLPRODUCTS");
	    
	}
	
	private void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("txtCode"),
				name = request.getParameter("txtName"),
				state = request.getParameter("chkState"),
				trademark = request.getParameter("txtTrademark"),
				category = request.getParameter("cboCategory");
		Admin admin = (Admin)request.getSession().getAttribute("admin");
		Product product = new Product();
		product.setCompany(admin.getCompany());
		product.setCode(code);
		product.setName(name);
		product.setState(state!=null?state:"INA");
		product.setCategory(new Category());
		product.getCategory().setCode(category);
		product.setTradeMark(trademark);
		
		List<Category> categories = ProductAccess.getAllCategories(admin.getCompany().getCode());
		List<Product> userList = (List<Product>) ProductAccess.find(product,categories);
		request.setAttribute("productList", userList);
		request.setAttribute("categories", categories);
		request.setAttribute("productSearched", product);
		getServletContext().getRequestDispatcher("/admin/producto.jsp").forward(request, response);
	}

	private void add(HttpServletRequest request, HttpServletResponse response) {
		
		
	}

	private void findProductsByCriteria(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=ISO-8859-1");
		response.addHeader("Cache-Control", "no-cache");
		String productName = request.getParameter("productName");
		String categoryCode = request.getParameter("categoryCode");
		
		User user = getUserFromRequest(request);
		
		List<ProductUnity> products = ProductAccess.findProductsByCriteria(user, productName, categoryCode);
		StringBuilder sb = new StringBuilder();
		sb.append("<products>");
		for(ProductUnity pu : products){
			sb.append("<product>");
			sb.append("<code>" + pu.getProduct().getCode() + "</code>");
			sb.append("<name>" + pu.getProduct().getName() + "</name>");
			sb.append("<trademark>" + pu.getProduct().getTradeMark() + "</trademark>");
			sb.append("<categoryCode>" + pu.getProduct().getCategory().getCode() + "</categoryCode>");
			sb.append("<categoryName>" + pu.getProduct().getCategory().getName() + "</categoryName>");
			sb.append("<unityCode>" + pu.getCode() + "</unityCode>");
			sb.append("<unityName>" + pu.getName() + "</unityName>");
			sb.append("<stock>" + pu.getStock()+ "</stock>");
			sb.append("</product>");
		}
		sb.append("</products>");
		response.getWriter().write(sb.toString());
		
	}

	private void asynchroGETALL(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=ISO-8859-1");
		response.addHeader("Cache-Control", "no-cache");
		String currencyCode = request.getParameter("currencyCode");
		String priceListCode = request.getParameter("priceListCode");
		
		//<![CDATA[]]>
		User user = (User) request.getSession().getAttribute("user");
		List<ProductPrice> productsList = ProductAccess.getProductsFromCurrencyAndPriceList(user, currencyCode,priceListCode);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<products>");
		for(ProductPrice pp : productsList){
			sb.append("<product>");
			sb.append("<code>" + pp.getProductUnity().getProduct().getCode() + "</code>");
			sb.append("<name>" + pp.getProductUnity().getProduct().getName() + "</name>");
			sb.append("<trademark>" + pp.getProductUnity().getProduct().getTradeMark() + "</trademark>");
			sb.append("<categoryCode>" + pp.getProductUnity().getProduct().getCategory().getCode() + "</categoryCode>");
			sb.append("<categoryName>" + pp.getProductUnity().getProduct().getCategory().getName() + "</categoryName>");
			sb.append("<unityCode>" + pp.getProductUnity().getCode() + "</unityCode>");
			sb.append("<unityName>" + pp.getProductUnity().getName() + "</unityName>");
			sb.append("<price>" + pp.getPrice()+ "</price>");
			sb.append("<stock>" + pp.getProductUnity().getStock()+ "</stock>");
			sb.append("</product>");
		}
		sb.append("</products>");
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