package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.Currency;
import movistar.bean.Entity;
import movistar.bean.NoSale;
import movistar.bean.PriceList;
import movistar.bean.Product;
import movistar.bean.ProductUnity;
import movistar.bean.Sale;
import movistar.bean.SaleDetail;
import movistar.bean.SaleMode;
import movistar.bean.SaleType;
import movistar.bean.User;
import movistar.util.DBConnection;

public class SaleAccess {
	public static List<NoSale> getAllNosales(User user){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT NS.CODE, NS.NAME, NS.STATE FROM NOSALE NS WHERE NS.COMPANYCODE = ?");
			ps.setString(1, user.getCompany().getCode());
			rs = ps.executeQuery();
			List<NoSale> noSales = new ArrayList<NoSale>();
			while(rs.next()){
				NoSale noSale = new NoSale();
				noSale.setCode(rs.getString(1));
				noSale.setName(rs.getString(2));
				noSale.setState(rs.getString(3));
				noSales.add(noSale);
			}
			return noSales;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(rs!=null) rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
				}catch(Exception x){}
		}
	}
	
	public static List<SaleType> getAllSaleTypes(){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT ST.CODE, ST.NAME FROM SALETYPE ST");
			
			rs = ps.executeQuery();
			List<SaleType> saleTypes = new ArrayList<SaleType>();
			while(rs.next()){
				SaleType saleType = new SaleType();
				saleType.setCode(rs.getString(1));
				saleType.setName(rs.getString(2));
				saleTypes.add(saleType);
			}
			return saleTypes;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(rs!=null) rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
				}catch(Exception x){}
		}
	}
	
	public static List<SaleMode> getSaleModesFromClient(User user, String clientCode){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT SM.CODE,SM.NAME FROM SALEMODE SM INNER JOIN SALEMODECLIENT SMC " +
					"ON SM.COMPANYCODE = SMC.COMPANYCODE AND SM.CODE=SMC.SALEMODECODE AND SMC.CLIENTCODE=? WHERE SM.COMPANYCODE = ?");
			ps.setString(1, clientCode);
			ps.setString(2, user.getCompany().getCode());
			rs = ps.executeQuery();
			
			List<SaleMode> saleModes = new ArrayList<SaleMode>();
			while(rs.next()){
				SaleMode saleMode = new SaleMode();
				saleMode.setCode(rs.getString(1));
				saleMode.setName(rs.getString(2));
				saleModes.add(saleMode);
			}
			return saleModes;			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(rs!=null) rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
				}catch(Exception x){}
		}
	}
	
	public static List<Entity> getAllSaleModes(User user){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT SM.CODE,SM.NAME,SM.TYPE FROM SALEMODE SM WHERE SM.COMPANYCODE=? AND STATE='ACT'");
			ps.setString(1, user.getCompany().getCode());
			rs = ps.executeQuery();
			
			List<Entity> saleModes = new ArrayList<Entity>();
			while(rs.next()){
				SaleMode saleMode = new SaleMode();
				saleMode.setCode(rs.getString(1));
				saleMode.setName(rs.getString(2));
				saleMode.setType(rs.getString(3));
				saleModes.add(saleMode);
			}
			return saleModes;			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(rs!=null) rs.close();
				if(ps!=null)ps.close();
				if(cnx!=null)cnx.close();
				}catch(Exception x){}
		}
	}
	
	public static String saveSale(Sale sale){
		Connection cnx  = null;
		PreparedStatement psKey = null,
			psSale = null,
			psDetail = null,
			psQuery=null,
			psUpdate=null;
		ResultSet rsKey = null, rsQuery = null;
		try {
			cnx = DBConnection.getConnection();
			cnx.setAutoCommit(false);
			psKey = cnx.prepareStatement("SELECT IFNULL(MAX(CODE),0)+1 FROM SALE");
			rsKey = psKey.executeQuery();
			rsKey.next();
			String key = rsKey.getString(1);
			sale.setCode(key);
			psSale = cnx.prepareStatement("INSERT INTO SALE VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),STR_TO_DATE(?,'%d/%m/%Y %T'))");
			psSale.setString(1, sale.getCode());
			psSale.setString(2, sale.getUser().getCompany().getCode());
			psSale.setString(3, sale.getClientAddress().getClient().getCode());
			psSale.setString(4, sale.getClientAddress().getCode());
			psSale.setString(5, sale.getUser().getCode());
			psSale.setString(6, sale.getSaleType().getCode());
			psSale.setString(7, sale.getCurrency()!=null?sale.getCurrency().getCode():null);
			psSale.setString(8, sale.getSaleMode()!=null?sale.getSaleMode().getCode():null);
			psSale.setString(9, sale.getNoSale()!=null?sale.getNoSale().getCode():null);
			psSale.setString(10, sale.getComment());
			psSale.setString(11,sale.getPriceList()!=null?sale.getPriceList().getCode():null);
			psSale.setString(12,sale.getDeliveryDate()!=null? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sale.getDeliveryDate()):null);
			psSale.setString(13, sale.getLatitude());
			psSale.setString(14, sale.getLongitude());
			psSale.setString(15, "ACT");
			psSale.setString(16, sale.getMobileDate());
			int c = psSale.executeUpdate();
			if(c > 0 && sale.getSaleDetails()!=null && sale.getSaleDetails().size()!=0){
			    	psQuery = cnx.prepareStatement("SELECT (STOCK - ?),STOCK FROM PRODUCTUNITY WHERE COMPANYCODE=? AND WAREHOUSECODE = ? AND PRODUCTCODE = ? AND CODE = ?");
				psDetail = cnx.prepareStatement("INSERT INTO SALEDETAIL VALUES(?,?,?,?,?,?,?,?,?,?,?)");
				SaleDetail saleDetail = null;
				c = 0;
				for(int i=0;i<sale.getSaleDetails().size();i++){
				    saleDetail = sale.getSaleDetails().get(i);
				    psQuery.setDouble(1, saleDetail.getQuantity());
				    psQuery.setString(2, sale.getUser().getCompany().getCode()); 
				    psQuery.setString(3, sale.getUser().getWarehouse().getCode());
				    psQuery.setString(4, saleDetail.getProductUnity().getProduct().getCode());
				    psQuery.setString(5,saleDetail.getProductUnity().getCode());
				    
				    rsQuery = psQuery.executeQuery();
				    rsQuery.next();
				    double resto = rsQuery.getDouble(1);
				    if(resto<0){
				        throw new Exception(String.format("El producto %s con unidad %s y almacen %s de la empresa %s, exceden el límite : stock actual %s, stock solicitado %s" ,
				        	saleDetail.getProductUnity().getProduct().getCode(),
				        	saleDetail.getProductUnity().getCode(),
				        	sale.getUser().getWarehouse().getCode(),
				        	sale.getUser().getCompany().getCode(),
				        	String.valueOf(rsQuery.getDouble(2)),
				        	String.valueOf(saleDetail.getQuantity()*1.0)));
				    }
				    psDetail.setString(1,sale.getUser().getCompany().getCode());
				    psDetail.setString(2, sale.getCode());
				    psDetail.setInt(3, i+1);
				    psDetail.setString(4, saleDetail.getProductUnity().getProduct().getCode());
				    psDetail.setString(5, saleDetail.getProductUnity().getWarehouse().getCode());
				    psDetail.setString(6, saleDetail.getProductUnity().getCode());
				    psDetail.setInt(7, saleDetail.getQuantity());
				    psDetail.setDouble(8, saleDetail.getPrice());
				    psDetail.setDouble(9, saleDetail.getEditedPrice());
				    psDetail.setDouble(10, saleDetail.getDiscount());
				    psDetail.setDouble(11,saleDetail.getSubtotal());
				    c += psDetail.executeUpdate();
				    //actualizar stock
				    psUpdate = cnx.prepareStatement("UPDATE PRODUCTUNITY SET STOCK = ? WHERE COMPANYCODE=? AND WAREHOUSECODE = ? AND PRODUCTCODE = ? AND CODE = ?");
				    psUpdate.setDouble(1, resto);
				    psUpdate.setString(2, sale.getUser().getCompany().getCode()); 
				    psUpdate.setString(3, sale.getUser().getWarehouse().getCode());
				    psUpdate.setString(4, saleDetail.getProductUnity().getProduct().getCode());
				    psUpdate.setString(5,saleDetail.getProductUnity().getCode());
				    psUpdate.executeUpdate();

				    psQuery.clearParameters();
				    psUpdate.clearParameters();
				    psDetail.clearParameters();
				}
				if(c!=sale.getSaleDetails().size()){
				    throw new Exception("No se guardo completamente el detalle de venta"); 
				}
			}
			cnx.commit();
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(cnx!=null)
					cnx.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}finally{
			try {
			    	if(rsQuery!=null)rsQuery.close();
				if(rsKey!=null)rsKey.close();
				if(psQuery!=null)psQuery.close();
				if(psUpdate!=null)psUpdate.close();
				if(psKey!=null)psKey.close();
				if(psSale!=null)psSale.close();
				if(psDetail!=null)psDetail.close();
				if(cnx!=null)cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;		
	}

	public static int setNoSales(String companyCode, List<NoSale> noSales) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM NOSALE WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO NOSALE VALUES(?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE NOSALE SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (NoSale noSale: noSales) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, noSale.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, noSale.getCode());
			psInsert.setString(3, noSale.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, noSale.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, noSale.getCode());
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

	public static List<Sale> getSales(String code, String userCode,
		String date) {
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement("SELECT S.CODE,C.CODE,C.RUC,C.NAME,CA.CODE,CA.NAME,ST.CODE,ST.NAME,NS.CODE,NS.NAME,PL.CODE,PL.NAME,S.OBS,S.DELIVERYDATE, S.LATITUDE,S.LONGITUDE,date_format(S.MOBILEDATE,'%d/%m/%Y %T') FROM " +
				"SALE S INNER JOIN SALETYPE ST ON S.COMPANYCODE = ST.COMPANYCODE AND S.SALETYPECODE = ST.CODE " +
				"INNER JOIN CLIENT C ON S.COMPANYCODE = C.COMPANYCODE AND S.CLIENTCODE = C.CODE " +
				"INNER JOIN CLIENTADDRESS CA ON C.COMPANYCODE=CA.COMPANYCODE AND C.CODE=CA.CLIENTCODE AND S.CLIENTADDRESSCODE = CA.CODE LEFT JOIN PRICELIST PL ON S.COMPANYCODE = PL.COMPANYCODE AND S.PRICELISTCODE = PL.CODE " +
				"LEFT JOIN NOSALE NS ON S.COMPANYCODE = NS.COMPANYCODE AND S.NOSALECODE = NS.CODE");
		rs = ps.executeQuery();
		List<Sale> sales = new ArrayList<Sale>();
		while(rs.next()){
		    Sale sale = new Sale();
		    sale.setCode(rs.getString(1));
		    sale.setClientAddress(new ClientAddress());
		    sale.getClientAddress().setClient(new Client());
		    sale.getClientAddress().getClient().setCode(rs.getString(2));
		    sale.getClientAddress().getClient().setRuc(rs.getString(3));
		    sale.getClientAddress().getClient().setName(rs.getString(4));
		    sale.getClientAddress().setCode(rs.getString(5));
		    sale.getClientAddress().setName(rs.getString(6));
		    sale.setSaleType(new SaleType());
		    sale.getSaleType().setCode(rs.getString(7));
		    sale.getSaleType().setName(rs.getString(8));
		    sale.setNoSale(new NoSale());
		    sale.getNoSale().setCode(rs.getString(9));
		    sale.getNoSale().setName(rs.getString(10));
		    sale.setPriceList(new PriceList());
		    sale.getPriceList().setCode(rs.getString(11));
		    sale.getPriceList().setName(rs.getString(12));
		    sale.setComment(rs.getString(13));
		    sale.setDeliveryDate(rs.getDate(14));
		    sale.setLatitude(rs.getString(15));
		    sale.setLongitude(rs.getString(16));
		    sale.setMobileDate(rs.getString(17));
		    sales.add(sale);
		}
		return sales;
	    } catch (Exception e) {
		e.printStackTrace();
		return null;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(ps!=null)ps.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }

	}

	public static int setSaleTypes(String companyCode, List<SaleType> saleTypes) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM SALETYPE WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO SALETYPE VALUES(?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE SALETYPE SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (SaleType saleType: saleTypes) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, saleType.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, saleType.getCode());
			psInsert.setString(3, saleType.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, saleType.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, saleType.getCode());
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
	
	public static int setSaleModes(String companyCode, List<SaleMode> saleModes) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM SALEMODE WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO SALEMODE VALUES(?,?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE SALEMODE SET NAME = ?, TYPE = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (SaleMode saleMode: saleModes) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, saleMode.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, saleMode.getCode());
			psInsert.setString(3, saleMode.getName());
			psInsert.setString(4, saleMode.getType());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, saleMode.getName());
			psUpdate.setString(2, saleMode.getType());
			psUpdate.setString(3, companyCode);
			psUpdate.setString(4, saleMode.getCode());
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
	public static List<Map> saleReport(String companyCode, String firstDate,String lastDate, String warehouseCode, String userCode){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		    cnx = DBConnection.getConnection();
		    ps=cnx.prepareStatement("SELECT W.CODE,W.NAME,U.CODE,U.NAME,COUNT(DISTINCT S.CODE), ROUND(SUM(SD.SUBTOTAL),3) " + 
                                "FROM SALE S INNER JOIN USER U "+ 
                                "ON U.COMPANYCODE=S.COMPANYCODE AND U.CODE = S.USERCODE INNER JOIN WAREHOUSE W "+ 
                                "ON W.COMPANYCODE=U.COMPANYCODE AND W.CODE=U.WAREHOUSECODE LEFT JOIN SALEDETAIL SD "+
                                "ON S.COMPANYCODE=SD.COMPANYCODE AND S.CODE=SD.SALECODE "+
		    		"WHERE U.COMPANYCODE=? AND S.MOBILEDATE BETWEEN ? AND ? AND W.CODE LIKE ? AND U.CODE LIKE ? "+
		    		"GROUP BY W.CODE,U.CODE ");
		    ps.setString(1, companyCode);
		    ps.setString(2, firstDate);
		    ps.setString(3, lastDate);
		    ps.setString(4, "".equals(warehouseCode)?"%":warehouseCode);
		    ps.setString(5, "".equals(userCode)?"%":userCode);
		    rs = ps.executeQuery();
		    List<Map> lista = new ArrayList<Map>();
		    while(rs.next()){
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("warehouseCode", rs.getString(1));
			map.put("warehouseName", rs.getString(2));
			map.put("userCode", rs.getString(3));
			map.put("userName", rs.getString(4));
			map.put("totalSales", rs.getString(5));
			map.put("totalAmount", rs.getString(6));

			lista.add(map);
		    }
		    return lista;
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}finally{
		    try {
			    if(rs!=null)rs.close();
			    if(ps!=null)ps.close();
			    if(cnx!=null)cnx.close();
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
		}
	    }

	public static List<Sale> getSalesByUser(String companyCode,String warehouseCode, String userCode, String startDate, String endDate) {
	    	Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	    try{
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement("SELECT S.CODE,S.OBS,S.DELIVERYDATE,S.LATITUDE,S.LONGITUDE, " +
				"DATE_FORMAT(S.MOBILEDATE,'%d/%m/%Y %T'),C.CODE,C.NAME,C.RUC,CA.CODE,CA.NAME,CA.LATITUDE,CA.LONGITUDE,ST.CODE,ST.NAME,CU.CODE,CU.NAME,SM.CODE,SM.NAME,ROUND(SUM(SD.SUBTOTAL),3) FROM SALE S " +
				"INNER JOIN CLIENT C ON C.COMPANYCODE = S.COMPANYCODE AND C.CODE=S.CLIENTCODE " +
				"INNER JOIN CLIENTADDRESS CA ON C.COMPANYCODE = CA.COMPANYCODE AND C.CODE = CA.CLIENTCODE AND " +
				"CA.CODE = S.CLIENTADDRESSCODE INNER JOIN SALETYPE ST ON ST.COMPANYCODE = S.COMPANYCODE AND ST.CODE = S.SALETYPECODE " +
				"LEFT JOIN CURRENCY CU ON CU.COMPANYCODE = S.COMPANYCODE AND CU.CODE = S.CURRENCYCODE LEFT JOIN SALEMODE SM " +
				"ON SM.COMPANYCODE = S.COMPANYCODE AND SM.CODE = S.SALEMODECODE LEFT JOIN NOSALE NS ON NS.COMPANYCODE = S.COMPANYCODE " +
				"AND NS.CODE = S.NOSALECODE LEFT JOIN PRICELIST PL ON PL.COMPANYCODE = S.COMPANYCODE AND PL.CODE = S.PRICELISTCODE " +
				"LEFT JOIN SALEDETAIL SD ON S.CODE = SD.SALECODE " +
				"WHERE S.COMPANYCODE = ? AND S.USERCODE = ? AND S.MOBILEDATE BETWEEN ? AND ? GROUP BY S.CODE");
		ps.setString(1, companyCode);
		ps.setString(2, userCode);
		ps.setString(3, startDate);
		ps.setString(4, endDate);
		rs = ps.executeQuery();
		List<Sale> sales = new ArrayList<Sale>();
		while(rs.next()){
		    Sale sale = new Sale();
		    sale.setCode(rs.getString(1));
		    sale.setComment(rs.getString(2));
		    sale.setDeliveryDate(rs.getDate(3));
		    sale.setLatitude(rs.getString(4));
		    sale.setLongitude(rs.getString(5));
		    sale.setMobileDate(rs.getString(6));
		    sale.setClientAddress(new ClientAddress());
		    sale.getClientAddress().setClient(new Client());
		    sale.getClientAddress().getClient().setCode(rs.getString(7));
		    sale.getClientAddress().getClient().setName(rs.getString(8));
		    sale.getClientAddress().getClient().setRuc(rs.getString(9));
		    sale.getClientAddress().setCode(rs.getString(10));
		    sale.getClientAddress().setName(rs.getString(11));
		    sale.getClientAddress().setLatitude(rs.getString(12));
		    sale.getClientAddress().setLongitude(rs.getString(13));
		    sale.setSaleType(new SaleType());
		    sale.getSaleType().setCode(rs.getString(14));
		    sale.getSaleType().setName(rs.getString(15));
		    sale.setCurrency(new Currency());
		    sale.getCurrency().setCode(rs.getString(16));
		    sale.getCurrency().setName(rs.getString(17));
		    sale.setSaleMode(new SaleMode());
		    sale.getSaleMode().setCode(rs.getString(18));
		    sale.getSaleMode().setName(rs.getString(19));
		    sale.setTotal(rs.getDouble(20));
		    sales.add(sale);
		}
		return sales;
	    }catch(Exception e){
		e.printStackTrace();
	    }finally{
		    try {
			    if(rs!=null)rs.close();
			    if(ps!=null)ps.close();
			    if(cnx!=null)cnx.close();
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
		}
	    return null;
	}

	public static List<SaleDetail> getDetailsBySale(String companyCode,String saleCode) {
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try {
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement("SELECT P.CODE,P.NAME,PU.CODE,PU.NAME,SD.ITEM,SD.QUANTITY,SD.EDITEDPRICE,SD.PRICE,SD.DISCOUNT,SD.SUBTOTAL " +
				"FROM SALEDETAIL SD INNER JOIN PRODUCT P " +
				"ON SD.COMPANYCODE = P.COMPANYCODE AND P.CODE=SD.PRODUCTCODE " +
				"INNER JOIN PRODUCTUNITY PU " +
				"ON PU.COMPANYCODE=SD.COMPANYCODE AND PU.PRODUCTCODE = SD.PRODUCTCODE AND PU.CODE = SD.PRODUCTUNITYCODE " +
				"WHERE SD.COMPANYCODE = ? AND SD.SALECODE = ?");
		ps.setString(1, companyCode);
		ps.setString(2, saleCode);
		System.out.println("com " + companyCode + " - sale : " + saleCode);
		rs = ps.executeQuery();
		List<SaleDetail> details = new ArrayList<SaleDetail>();
		while(rs.next()){
		    Product p = new Product();
		    p.setCode(rs.getString(1));
		    p.setName(rs.getString(2));
		    ProductUnity pu = new ProductUnity();
		    pu.setCode(rs.getString(3));
		    pu.setName(rs.getString(4));
		    pu.setProduct(p);
		    SaleDetail detail = new  SaleDetail();
		    detail.setProductUnity(pu);
		    detail.setItem(rs.getInt(5));
		    detail.setQuantity(rs.getInt(6));
		    detail.setEditedPrice(rs.getDouble(7));
		    detail.setPrice(rs.getDouble(8));
		    detail.setDiscount(rs.getDouble(9));
		    detail.setSubtotal(rs.getDouble(10));
		    details.add(detail);
		}
		return details;
	    } catch (Exception e) {
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
}
