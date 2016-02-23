package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import movistar.bean.Category;
import movistar.bean.Company;
import movistar.bean.Currency;
import movistar.bean.Entity;
import movistar.bean.PriceList;
import movistar.bean.Product;
import movistar.bean.ProductPrice;
import movistar.bean.ProductUnity;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.util.DBConnection;

//PROPAGAR EXCEPCIONES
public final class ProductAccess {
    
   private final static Logger logger = Logger.getLogger(ProductAccess.class);
    
	// revisar y pulir, la recuperaci�n de datos y consultas deber�an de iterar
	// la busqueda sobre el array de productprices
	public static List<ProductPrice> getAllProductsFromSynchro(User user, List<Entity> categories, List<Entity> priceLists, List<Entity> currencies) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT P.CODE,P.NAME,P.TRADEMARK,P.CATEGORYCODE,PU.CODE,PU.NAME,PU.STOCK,PP.PRICELISTCODE,PP.CURRENCYCODE,PP.PRICE FROM "
					+ "PRODUCTPRICE PP INNER JOIN PRODUCTUNITY PU ON PP.COMPANYCODE=PU.COMPANYCODE AND PP.PRODUCTUNITYCODE=PU.CODE AND PP.PRODUCTCODE = PU.PRODUCTCODE AND PU.WAREHOUSECODE=PP.WAREHOUSECODE "
					+ "INNER JOIN PRODUCT P ON PU.COMPANYCODE = P.COMPANYCODE AND PU.PRODUCTCODE=P.CODE WHERE PP.COMPANYCODE = ? AND PU.WAREHOUSECODE = ?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, user.getWarehouse().getCode());
			rs = ps.executeQuery();

			List<Entity> products = new ArrayList<Entity>();
			List<ProductUnity> productUnitys = new ArrayList<ProductUnity>();
			List<ProductPrice> productPrices = new ArrayList<ProductPrice>();

			while (rs.next()) {
				Product p = (Product) Entity.findEntity(products, rs.getString(1));
				if (p == null) {
					p = new Product();
					p.setCode(rs.getString(1));
					p.setName(rs.getString(2));
					p.setTradeMark(rs.getString(3));
					p.setCategory((Category) Entity.findEntity(categories,
							rs.getString(4)));
					products.add(p);
				}
				ProductUnity pu = findProductUnity(productUnitys, user
						.getWarehouse().getCode(), p.getCode(), rs.getString(5));
				if (pu == null) {
					pu = new ProductUnity();
					pu.setCode(rs.getString(5));
					pu.setName(rs.getString(6));
					pu.setStock(rs.getDouble(7));
					pu.setProduct(p);
					pu.setWarehouse(user.getWarehouse());
					productUnitys.add(pu);
				}
				ProductPrice pp = new ProductPrice();
				pp.setProductUnity(pu);
				pp.setPriceList((PriceList) Entity.findEntity(priceLists,
						rs.getString(8)));
				pp.setCurrency((Currency) Entity.findEntity(currencies,
						rs.getString(9)));
				pp.setPrice(rs.getDouble(10));
				productPrices.add(pp);
			}
			return productPrices;
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}

		return null;
	}

	public static List<Entity> getAllCategories(User user, Connection cnxParam) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT C.CODE,C.NAME,C.STATE FROM CATEGORY C WHERE C.COMPANYCODE = ? AND C.STATE=?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, "ACT");
			rs = ps.executeQuery();

			List<Entity> categories = new ArrayList<Entity>();
			while (rs.next()) {
				Category c = new Category();
				c.setCode(rs.getString(1));
				c.setName(rs.getString(2));
				c.setState(rs.getString(3));
				categories.add(c);
			}

			return categories;
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null && cnxParam == null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<Category> getAllCategories(String companyCode) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT C.CODE,C.NAME,C.STATE FROM CATEGORY C WHERE C.COMPANYCODE = ?");
			ps.setString(1, companyCode);
			rs = ps.executeQuery();

			List<Category> categories = new ArrayList<Category>();
			while (rs.next()){
				Category c = new Category();
				c.setCode(rs.getString(1));
				c.setName(rs.getString(2));
				c.setState(rs.getString(3));
				categories.add(c);
			}
			return categories;
		} catch (Exception x){
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<Entity> getAllPriceLists(User user, Connection cnxParam) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT PL.CODE, PL.NAME, PL.STATE FROM PRICELIST PL WHERE PL.COMPANYCODE = ? AND PL.STATE=?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, "ACT");
			rs = ps.executeQuery();

			List<Entity> priceLists = new ArrayList<Entity>();
			while (rs.next()) {
				PriceList pl = new PriceList();
				pl.setCode(rs.getString(1));
				pl.setName(rs.getString(2));
				pl.setState(rs.getString(3));
				
				priceLists.add(pl);
			}

			return priceLists;
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null && cnxParam == null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<Entity> getAllCurrencies(User user, Connection cnxParam) {
		Connection cnx = cnxParam;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT C.CODE, C.NAME, C.SYMBOL, C.LOCAL, C.STATE FROM CURRENCY C WHERE C.COMPANYCODE = ? AND C.STATE=?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, "ACT");
			rs = ps.executeQuery();

			List<Entity> currencies = new ArrayList<Entity>();

			while (rs.next()) {
				Currency currency = new Currency();
				currency.setCode(rs.getString(1));
				currency.setName(rs.getString(2));
				currency.setSymbol(rs.getString(3));
				currency.setLocal(rs.getBoolean(4));
				currency.setState(rs.getString(5));
				currencies.add(currency);
			}

			return currencies;
		} catch (Exception x){
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null && cnxParam == null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	

	private static ProductUnity findProductUnity(List<ProductUnity> list,
			String warehouseCode, String productCode, String code) {
		for (ProductUnity pu : list) {
			if (pu.getWarehouse().getCode().equals(warehouseCode)
					&& pu.getProduct().getCode().equals(productCode)
					&& pu.getCode().equals(code))
				return pu;
		}
		return null;
	}

	public static List<ProductPrice> getProductsFromCurrencyAndPriceList(User user,
			String currencyCode, String priceListCode) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT P.CODE,P.NAME,P.TRADEMARK,C.CODE,C.NAME,PU.CODE,PU.NAME,PU.STOCK,PP.PRICE FROM " +
					"PRODUCT P INNER JOIN CATEGORY C " +
					"ON P.COMPANYCODE = C.COMPANYCODE AND C.CODE=P.CATEGORYCODE " +
					"INNER JOIN PRODUCTUNITY PU " +
					"ON P.COMPANYCODE=PU.COMPANYCODE AND P.CODE = PU.PRODUCTCODE AND PU.WAREHOUSECODE = ? " +
					"INNER JOIN PRODUCTPRICE PP " +
					"ON PU.COMPANYCODE = PP.COMPANYCODE AND PU.WAREHOUSECODE = PP.WAREHOUSECODE AND PP.PRODUCTCODE = PU.PRODUCTCODE " +
					"AND PU.CODE = PP.PRODUCTUNITYCODE AND PP.CURRENCYCODE = ? AND PRICELISTCODE = ? " +
					"WHERE P.COMPANYCODE = ?");
			
			ps.setString(1, user.getWarehouse().getCode());
			ps.setString(2, currencyCode);
			ps.setString(3, priceListCode);
			ps.setString(4, user.getCompany().getCode());
			rs = ps.executeQuery();
			List<ProductPrice> productsList = new ArrayList<ProductPrice>();

			while (rs.next()) {
				Product p = new Product();
				p.setCode(rs.getString(1));
				p.setName(rs.getString(2));
				p.setTradeMark(rs.getString(3));
				p.setCategory(new Category());
				p.getCategory().setCode(rs.getString(4));
				p.getCategory().setName(rs.getString(5));
				ProductUnity pu =new ProductUnity();
				pu.setProduct(p);
				pu.setCode(rs.getString(6));
				pu.setName(rs.getString(7));
				pu.setStock(rs.getDouble(8));
				ProductPrice pp = new ProductPrice();
				pp.setPrice(rs.getDouble(9));
				pp.setProductUnity(pu);
				productsList.add(pp);
			}

			return productsList;

		} catch (Exception x){
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;

	}
	
	public static List<ProductUnity> findProductsByCriteria(User user, String productName, String categoryCode){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT P.CODE,P.NAME,P.TRADEMARK,C.CODE,C.NAME,PU.CODE,PU.NAME,PU.STOCK FROM PRODUCT P" +
					" INNER JOIN CATEGORY C ON P.COMPANYCODE = C.COMPANYCODE AND C.CODE=P.CATEGORYCODE" +
					" INNER JOIN PRODUCTUNITY PU ON P.COMPANYCODE=PU.COMPANYCODE AND P.CODE = PU.PRODUCTCODE AND PU.WAREHOUSECODE = ?" +
					" WHERE P.COMPANYCODE = ? AND P.NAME LIKE ?";
			boolean isCategory = categoryCode!=null && categoryCode.trim().length()>0;  
			cnx = DBConnection.getConnection();
			
			if(isCategory)sql += " AND C.CODE = ? ";
			ps = cnx.prepareStatement(sql);
			ps.setString(1, user.getWarehouse().getCode());
			ps.setString(2, user.getCompany().getCode());
			ps.setString(3, productName + "%");
			
			if(isCategory)ps.setString(4, categoryCode);
			
			rs = ps.executeQuery();
			
			List<ProductUnity> products = new ArrayList<ProductUnity>();
						
			while(rs.next()){
				Product product = new Product();
				product.setCode(rs.getString(1));
				product.setName(rs.getString(2));
				product.setTradeMark(rs.getString(3));
				Category category = new Category();
				category.setCode(rs.getString(4));
				category.setName(rs.getString(5));
				product.setCategory(category);
				ProductUnity p = new ProductUnity();
				p.setProduct(product);
				p.setCode(rs.getString(6));
				p.setName(rs.getString(7));
				p.setStock(rs.getDouble(8));
				products.add(p);
			}
			return products;
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null)rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	public static List<Product> getAllProducts(String companyCode,List<Category> categories){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT P.CODE,P.NAME,P.TRADEMARK,P.CATEGORYCODE FROM PRODUCT P WHERE COMPANYCODE = ? AND STATE = 'ACT'");
			ps.setString(1, companyCode);
			rs = ps.executeQuery();
			List<Product> products = new ArrayList<Product>();
			while(rs.next()){
				Product p = new Product();
				p.setCode(rs.getString(1));
				p.setName(rs.getString(2));
				p.setTradeMark(rs.getString(3));
				String categoryCode = rs.getString(4);
				for (Category category : categories) {
					if(category.getCode().equals(categoryCode)){
						p.setCategory(category);
						break;
					}
				}
				products.add(p);
			}
			return products;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				if(rs!=null)rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public static int delete(String companyCode, String[] productcodes) {
		Connection cnx = null;
		PreparedStatement ps = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("UPDATE PRODUCT SET STATE='INA' WHERE COMPANYCODE=? AND CODE=?");
			int c = 0;
			for(String product:productcodes){
				ps.setString(1, companyCode);
				ps.setString(2, product);
				c += ps.executeUpdate();
				ps.clearParameters();
			}
			return c;
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			try{
				if(ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public static List<Product> find(Product product,List<Category> categories){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			cnx = DBConnection.getConnection();
			String sql = "SELECT P.CODE,P.NAME,P.TRADEMARK,P.CATEGORYCODE FROM PRODUCT P WHERE P.COMPANYCODE = ?";
			if(product.getCode().length()!=0)
			    sql += " AND P.CODE LIKE ?";
			if(product.getName().length()!=0)
			    sql += " AND P.NAME LIKE ?";
			if(product.getTradeMark().length()!=0)
			    sql += " AND P.TRADEMARK LIKE ?";
			if(product.getCategory()!=null && product.getCategory().getCode().length()!=0)
			    sql += " AND P.CATEGORYCODE = ?";
			
			sql += " AND P.STATE = ?";
			ps=cnx.prepareStatement(sql);
			int c=1;
			ps.setString(c++, product.getCompany().getCode());
			if(product.getCode().length()!=0)
				ps.setString(c++, product.getCode()+"%");
			if(product.getName().length()!=0)
				ps.setString(c++, "%"+product.getName()+"%");
			if(product.getTradeMark().length()!=0)
				ps.setString(c++,  "%"+product.getTradeMark()+"%");
			if(product.getCategory()!=null && product.getCategory().getCode().length()!=0)
				ps.setString(c++, product.getCategory().getCode());
			ps.setString(c, product.getState());
			List<Product> products = new ArrayList<Product>();
			rs = ps.executeQuery();
			while(rs.next()){
				Product p = new Product();
				p.setCode(rs.getString(1));
				p.setName(rs.getString(2));
				p.setTradeMark(rs.getString(3));
				String categoryCode = rs.getString(4);
				for (Category category : categories) {
					if(category.getCode().equals(categoryCode)){
						p.setCategory(category);
						break;
					}
				}
				products.add(p);
			}
			return products;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				if(rs!=null)rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public static List<PriceList> getAllPriceLists(String companyCode) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		    	cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT PL.CODE, PL.NAME, PL.STATE FROM PRICELIST PL WHERE PL.COMPANYCODE = ? AND PL.STATE='ACT'");
			ps.setString(1, companyCode);
			rs = ps.executeQuery();

			List<PriceList> priceLists = new ArrayList<PriceList>();
			while (rs.next()) {
				PriceList pl = new PriceList();
				pl.setCode(rs.getString(1));
				pl.setName(rs.getString(2));
				pl.setState(rs.getString(3));
				priceLists.add(pl);
			}
			return priceLists;
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<Currency> getAllCurrencies(String companyCode) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT C.CODE, C.NAME, C.SYMBOL, C.LOCAL, C.STATE FROM CURRENCY C WHERE C.COMPANYCODE = ? AND C.STATE='ACT'");
			ps.setString(1,companyCode);
			
			rs = ps.executeQuery();
			List<Currency> currencies = new ArrayList<Currency>();
			while (rs.next()) {
				Currency currency = new Currency();
				currency.setCode(rs.getString(1));
				currency.setName(rs.getString(2));
				currency.setSymbol(rs.getString(3));
				currency.setLocal(rs.getBoolean(4));
				currency.setState(rs.getString(5));
				currencies.add(currency);
			}
			return currencies;
		} catch (Exception x){
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<ProductPrice> getProductPricesFromProduct(Product product){
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try {
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement("SELECT PL.CODE,PL.NAME,C.CODE,C.SYMBOL,C.NAME,PU.CODE,PU.NAME,PU.STOCK,W.CODE,W.NAME,PP.PRICE  FROM PRODUCTPRICE PP INNER JOIN " +
				"PRODUCTUNITY PU ON PU.COMPANYCODE=PP.COMPANYCODE AND PU.PRODUCTCODE=PP.PRODUCTCODE AND PU.WAREHOUSECODE = PP.WAREHOUSECODE AND PU.CODE = PP.PRODUCTUNITYCODE INNER JOIN " +
				"PRICELIST PL ON PL.COMPANYCODE = PP.COMPANYCODE AND PL.CODE = PP.PRICELISTCODE INNER JOIN " +
				"CURRENCY C ON C.COMPANYCODE = PP.COMPANYCODE AND C.CODE = PP.CURRENCYCODE INNER JOIN " +
				"WAREHOUSE W ON W.COMPANYCODE = PU.COMPANYCODE AND W.CODE = PU.WAREHOUSECODE " +
				"WHERE PU.COMPANYCODE = ? AND PU.PRODUCTCODE = ?");
		
		ps.setString(1, product.getCompany().getCode());
		ps.setString(2, product.getCode());
		rs = ps.executeQuery();
		List<ProductPrice> productPrices = new ArrayList<ProductPrice>();
		while(rs.next()){
		    ProductPrice pp = new ProductPrice();
		    pp.setPriceList(new PriceList());
		    pp.setCurrency(new Currency());
		    pp.setProductUnity(new ProductUnity());
		    pp.getProductUnity().setWarehouse(new Warehouse());

		    pp.getPriceList().setCode(rs.getString(1));
		    pp.getPriceList().setName(rs.getString(2));
		    pp.getCurrency().setCode(rs.getString(3));
		    pp.getCurrency().setSymbol(rs.getString(4));
		    pp.getCurrency().setName(rs.getString(5));
		    pp.getProductUnity().setCode(rs.getString(6));
		    pp.getProductUnity().setName(rs.getString(7));
		    pp.getProductUnity().setStock(rs.getDouble(8));
		    pp.getProductUnity().getWarehouse().setCode(rs.getString(9));
		    pp.getProductUnity().getWarehouse().setName(rs.getString(10));
		    pp.setPrice(rs.getDouble(11));
		    productPrices.add(pp);
		}
		return productPrices;
	    } catch (Exception e) {
		e.printStackTrace();
	    }finally{
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (cnx != null)
				cnx.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	    }
	    
	    return null;
	}

	public static void saveConfig(ProductUnity productUnity, List<ProductPrice> productPrices) {
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    PreparedStatement psDetail = null;
	    
	    try{
		
		cnx = DBConnection.getConnection();
		cnx.setAutoCommit(false);
		cnx.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		ps = cnx.prepareStatement("INSERT INTO PRODUCTUNITY VALUES(?,?,?,?,?,?,?)");
		ps.setString(1, productUnity.getCompany().getCode());
		ps.setString(2, productUnity.getProduct().getCode());
		ps.setString(3, productUnity.getWarehouse().getCode());
		ps.setString(4, productUnity.getCode());
		ps.setString(5, productUnity.getName());
		ps.setDouble(6, productUnity.getStock());
		ps.setString(7, productUnity.getState());
		
		ps.executeUpdate();
		psDetail = cnx.prepareStatement("INSERT INTO PRODUCTPRICE VALUES(?,?,?,?,?,?,?,?)");
		for (ProductPrice productPrice : productPrices) {
		    psDetail.setString(1, productUnity.getCompany().getCode());
		    psDetail.setString(2, productPrice.getPriceList().getCode());
		    psDetail.setString(3, productUnity.getCode());
		    psDetail.setString(4, productUnity.getProduct().getCode());
		    psDetail.setString(5, productUnity.getWarehouse().getCode());
		    psDetail.setString(6, productPrice.getCurrency().getCode());
		    psDetail.setDouble(7, productPrice.getPrice());
		    psDetail.setString(8, productPrice.getState());
		    psDetail.executeUpdate();
		    psDetail.clearParameters();
		}
		cnx.commit();
	    }catch(Exception ex){
		if(cnx!=null)
		    try{
			cnx.rollback();
		    }catch(SQLException e){
			e.printStackTrace();
		    }
		ex.printStackTrace();
	    }finally{
		try {
		    if (psDetail != null)
			psDetail.close();
		    if (ps != null)
			ps.close();
		    if (cnx != null)
			cnx.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	    }
	}

	public static int setCategories(String companyCode, List<Category> categories) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM CATEGORY WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO CATEGORY VALUES(?,?,?,null,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE CATEGORY SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (Category category : categories) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, category.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, category.getCode());
			psInsert.setString(3, category.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, category.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, category.getCode());
			c+= psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    psSelect.clearParameters();
		}
		return c;
	    } catch (Exception e) {
		e.printStackTrace();
		return -1;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(psInsert!=null)psInsert.close();
		    if(psSelect!=null)psSelect.close();
		    if(psUpdate!=null)psUpdate.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	}
	
	public static int setCurrencies(String companyCode, List<Currency> currencies) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM CURRENCY WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO CURRENCY VALUES(?,?,?,?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE CURRENCY SET NAME = ?, SYMBOL = ?, LOCAL = ?, EXCHANGE = ? WHERE COMPANYCODE = ? AND CODE = ?");
		int c =0;
		for (Currency currency : currencies) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, currency.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, currency.getCode());
			psInsert.setString(3, currency.getName());
			psInsert.setString(4, currency.getSymbol());
			psInsert.setBoolean(5, currency.isLocal());
			psInsert.setDouble(6, currency.getExchange());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, currency.getName());
			psUpdate.setString(2, currency.getSymbol());
			psUpdate.setBoolean(3, currency.isLocal());
			psUpdate.setDouble(4, currency.getExchange());
			psUpdate.setString(5, companyCode);
			psUpdate.setString(6, currency.getCode());
			c+= psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    psSelect.clearParameters();
		}
		return c;
	    } catch (Exception e) {
		e.printStackTrace();
		return -1;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(psInsert!=null)psInsert.close();
		    if(psSelect!=null)psSelect.close();
		    if(psUpdate!=null)psUpdate.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	}

	public static int setPriceLists(String companyCode, List<PriceList> priceLists) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		cnx.setAutoCommit(false);
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM PRICELIST WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO PRICELIST VALUES(?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE PRICELIST SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (PriceList priceList: priceLists) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, priceList.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, priceList.getCode());
			psInsert.setString(3, priceList.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, priceList.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, priceList.getCode());
			c+= psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    psSelect.clearParameters();
		}
		cnx.commit();
		return c;
	    } catch (Exception e) {
		e.printStackTrace();
		try {
		    if(cnx!=null)
			cnx.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return -1;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(psInsert!=null)psInsert.close();
		    if(psSelect!=null)psSelect.close();
		    if(psUpdate!=null)psUpdate.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	    
	}

	public static String setProducts(Company company, List<Product> products) {
	    Connection cnx = null;
	    PreparedStatement psQuery = null, psInsert = null, psUpdate = null;
	    ResultSet rs = null;
	    
	    try {
		cnx = DBConnection.getConnection();
		cnx.setAutoCommit(false);
		psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM PRODUCT WHERE COMPANYCODE=? AND CODE = ?");
		psInsert = cnx.prepareStatement("INSERT INTO PRODUCT VALUES(?,?,?,?,?,'ACT')");
		psUpdate = cnx.prepareStatement("UPDATE PRODUCT SET NAME = ?, TRADEMARK = ?, CATEGORYCODE = ? WHERE COMPANYCODE = ? AND CODE = ?");
		boolean newProduct;
		for(Product product : products){
		    
		    psQuery.setString(1, company.getCode());
		    psQuery.setString(2, product.getCode());
		    rs =  psQuery.executeQuery();
		    newProduct = rs.next() && rs.getInt(1)==0;
		    rs.close();
		    psQuery.clearParameters();
		    if(newProduct){
			psInsert.setString(1, company.getCode());
			psInsert.setString(2, product.getCode());
			psInsert.setString(3, product.getName());
			psInsert.setString(4, product.getTradeMark());
			psInsert.setString(5, product.getCategory().getCode());
			psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, product.getName());
			psUpdate.setString(2, product.getTradeMark());
			psUpdate.setString(3, product.getCategory().getCode());
			psUpdate.setString(4, company.getCode());
			psUpdate.setString(5, product.getCode());
			psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    if(product.getProductUnities()!=null && product.getProductUnities().size()>0){
			product.setCompany(company);
			setProductUnities(product,  newProduct, cnx);
		    }
		}
		
		cnx.commit();
	    } catch (Exception e) {
		try {
		    cnx.rollback();
		} catch (Exception e2) {
		    e2.printStackTrace();
		}
		e.printStackTrace();
	    }finally{
		try{
		    if(psQuery!=null) psQuery.close();
		    if(cnx!=null) cnx.close();
		}catch(Exception e){
		    e.printStackTrace();
		}
	    }
	    return null;
	}
	public static String setProductUnities(Product product, boolean newProduct, Connection cnx){
	    PreparedStatement psQuery = null , psInsert = null, psUpdate =null;
	    ResultSet rs = null;
	    try {
		psInsert = cnx.prepareStatement("INSERT INTO PRODUCTUNITY VALUES(?,?,?,?,?,?,'ACT')");
		if(!newProduct){
		    psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM PRODUCTUNITY WHERE COMPANYCODE = ? AND PRODUCTCODE = ? AND WAREHOUSECODE = ? AND CODE = ?");
		    psUpdate = cnx.prepareStatement("UPDATE PRODUCTUNITY SET NAME = ?, STOCK = ? WHERE COMPANYCODE = ? AND PRODUCTCODE = ? AND WAREHOUSECODE = ? AND CODE = ?");
		}
		
		boolean newProductUnity;
		for(ProductUnity productUnity : product.getProductUnities()){
		    
		    newProductUnity = newProduct;
		    if(!newProductUnity){
			psQuery.setString(1, product.getCompany().getCode());
			psQuery.setString(2, product.getCode());
			psQuery.setString(3, productUnity.getWarehouse().getCode());
			psQuery.setString(4, productUnity.getCode());
			rs = psQuery.executeQuery();
			newProductUnity = rs.next() && rs.getInt(1)==0;
			rs.close();
			psQuery.clearParameters();
		    }
		    if(newProductUnity){
			psInsert.setString(1, product.getCompany().getCode());
			psInsert.setString(2, product.getCode());
			psInsert.setString(3, productUnity.getWarehouse().getCode());
			psInsert.setString(4, productUnity.getCode());
			psInsert.setString(5, productUnity.getName());
			psInsert.setDouble(6, productUnity.getStock());
			psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, productUnity.getWarehouse().getCode());
			psUpdate.setString(2, productUnity.getCode());
			psUpdate.setString(3, productUnity.getName());
			psUpdate.setDouble(4, productUnity.getStock());
			psUpdate.setString(5, product.getCompany().getCode());
			psUpdate.setString(6, product.getCode());
			psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    if(productUnity.getProductPrices()!=null && productUnity.getProductPrices().size()>0){
			productUnity.setProduct(product);
			setProductPrices(productUnity, newProductUnity, cnx);
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }finally{
		    try {
			if(psQuery!=null) psQuery.close();
			if(psInsert!=null) psInsert.close();
			if(psUpdate!=null) psUpdate.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
	    }
	    return null;
	}
	public static String setProductPrices(ProductUnity productUnity, boolean newProductUnity, Connection cnx){
	    PreparedStatement psInsert = null,psQuery = null,psUpdate = null;
	    ResultSet rs = null;
	    try {
		psInsert = cnx.prepareStatement("INSERT INTO PRODUCTPRICE VALUES(?,?,?,?,?,?,?,'ACT')");
		if(!newProductUnity){
		    psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM PRODUCTPRICE WHERE COMPANYCODE = ? AND PRICELISTCODE = ? AND PRODUCTUNITYCODE = ? AND PRODUCTCODE = ? AND WAREHOUSECODE = ? AND CURRENCYCODE = ?");
		    psUpdate = cnx.prepareStatement("UPDATE PRODUCTUNITY SET PRICE = ? WHERE COMPANYCODE = ? AND PRICELISTCODE = ? AND PRODUCTUNITYCODE = ? AND PRODUCTCODE = ? AND WAREHOUSECODE = ? AND CURRENCYCODE = ?");
		}
		boolean insert;
		for(ProductPrice productPrice:productUnity.getProductPrices()){
		    insert = newProductUnity;
		    if(!insert){
			psQuery.setString(1, productUnity.getProduct().getCompany().getCode());
			psQuery.setString(2, productPrice.getPriceList().getCode());
			psQuery.setString(3, productUnity.getCode());
			psQuery.setString(4, productUnity.getProduct().getCode());
			psQuery.setString(5, productUnity.getWarehouse().getCode());
			psQuery.setString(6, productPrice.getCurrency().getCode());
			rs = psQuery.executeQuery();
			insert = rs.next() && rs.getInt(1)==0;
			rs.close();
			psQuery.clearParameters();
		    }
		    if(insert){
			psInsert.setString(1, productUnity.getProduct().getCompany().getCode());
			psInsert.setString(2, productPrice.getPriceList().getCode());
			psInsert.setString(3, productUnity.getCode());
			psInsert.setString(4, productUnity.getProduct().getCode());
			psInsert.setString(5, productUnity.getWarehouse().getCode());
			psInsert.setString(6, productPrice.getCurrency().getCode());
			psInsert.setDouble(7, productPrice.getPrice());
			psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setDouble(1, productPrice.getPrice());
			psUpdate.setString(2, productUnity.getProduct().getCompany().getCode());
			psUpdate.setString(3, productPrice.getPriceList().getCode());
			psUpdate.setString(4, productUnity.getCode());
			psUpdate.setString(5, productUnity.getProduct().getCode());
			psUpdate.setString(6, productUnity.getWarehouse().getCode());
			psUpdate.setString(7, productPrice.getCurrency().getCode());
			psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		}
	    } catch (Exception e) {
		logger.error("Error al procesar precios de producto ",e);
	    }finally{
		 try {
			if(psQuery!=null) psQuery.close();
			if(psInsert!=null) psInsert.close();
			if(psUpdate!=null) psUpdate.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
	    }
	    return null;
	}
}