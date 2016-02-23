package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.ClientType;
import movistar.bean.Company;
import movistar.bean.CreditClient;
import movistar.bean.Currency;
import movistar.bean.Entity;
import movistar.bean.Itinerary;
import movistar.bean.PriceList;
import movistar.bean.SaleMode;
import movistar.bean.User;
import movistar.util.DBConnection;

//Propagar excepciones
public class ClientAccess {
	public static List<Itinerary> getAllClientsfromSynchro(User user){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT I.CODE,I.DAY,I.FREQUENCY,I.DATE,C.CODE,C.NAME,C.RUC,C.PHONE,C.CLIENTTYPECODE,CA.CODE,CA.NAME,CA.TYPE " +
					"FROM ITINERARY I INNER JOIN CLIENT C ON C.COMPANYCODE = I.COMPANYCODE AND C.CODE=I.CLIENTCODE " +
					"INNER JOIN CLIENTADDRESS CA ON C.COMPANYCODE=CA.COMPANYCODE AND C.CODE=CA.CLIENTCODE AND CA.CODE=I.CLIENTADDRESSCODE " +
					"INNER JOIN USERITINERARY UI ON UI.COMPANYCODE = I.COMPANYCODE AND I.CODE=UI.ITINERARYCODE " +
					"WHERE UI.COMPANYCODE = ? AND UI.USERCODE = ? AND " +
					"UI.ASSIGNEDDATE=(SELECT MAX(UI2.ASSIGNEDDATE) FROM USERITINERARY UI2 WHERE UI2.COMPANYCODE=UI.COMPANYCODE AND UI2.USERCODE=UI.USERCODE) " +
					"AND I.DAY IN (DAYOFWEEK(SYSDATE())-1,9)");
			
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, user.getCode());
			rs = ps.executeQuery();
			List<Entity> clientTypes = getAllClientTypes(user, cnx);
			List<Itinerary> itineraries = new ArrayList<Itinerary>();
			while(rs.next()){
				Itinerary i = new Itinerary();
				i.setCode(rs.getString(1));
				i.setDay(rs.getShort(2));
				i.setFrequency(rs.getString(3));
				i.setDate(rs.getDate(4));
				Client c = new Client();
				c.setCode(rs.getString(5));
				c.setName(rs.getString(6));
				c.setRuc(rs.getString(7));
				c.setPhone(rs.getString(8));
				c.setClientType((ClientType)Entity.findEntity(clientTypes, rs.getString(9)));
				c.setSaleModes(getAllSaleModes(user, c.getCode(), cnx));
				c.setPriceLists(getAllPriceLists(user, c.getCode(), cnx));
				ClientAddress ca = new ClientAddress();
				ca.setCode(rs.getString(10));
				ca.setName(rs.getString(11));
				ca.setType(rs.getString(12));
				ca.setClient(c);
				
				i.setClientAddress(ca);
				itineraries.add(i);
			}
			return itineraries;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				if(cnx != null)cnx.close();
			}catch(Exception x){
				x.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static List<Entity> getAllClientTypes(User user, Connection cnxParam) {
		Connection cnx = cnxParam;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT CT.CODE, CT.NAME, CT.STATE FROM CLIENTTYPE CT WHERE CT.COMPANYCODE = ? AND CT.STATE=?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, "ACT");
			rs = ps.executeQuery();

			List<Entity> clientTypes = new ArrayList<Entity>();
			
			while (rs.next()) {
				ClientType ct = new ClientType();
				ct.setCode(rs.getString(1));
				ct.setName(rs.getString(2));
				ct.setState(rs.getString(3));
				clientTypes.add(ct);
			}

			return clientTypes;
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

	public static List<SaleMode> getAllSaleModes(User user, String clientCode, Connection cnxParam){
		Connection cnx = cnxParam;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT SM.CODE,SM.NAME,SM.TYPE,SM.STATE FROM SALEMODE SM " +
					"INNER JOIN SALEMODECLIENT SMC ON SM.COMPANYCODE=SMC.COMPANYCODE AND SM.CODE=SMC.SALEMODECODE " +
					"WHERE SM.COMPANYCODE = ? AND SMC.CLIENTCODE = ? AND SMC.STATE=?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, clientCode);
			ps.setString(3, "ACT");
			rs = ps.executeQuery();

			List<SaleMode> saleModes = new ArrayList<SaleMode>();
			
			while (rs.next()) {
				SaleMode ct = new SaleMode();
				ct.setCode(rs.getString(1));
				ct.setName(rs.getString(2));
				ct.setType(rs.getString(3));
				ct.setState(rs.getString(4));
				saleModes.add(ct);
			}

			return saleModes;
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

	public static List<PriceList> getAllPriceLists(User user, String clientCode, Connection cnxParam){
		Connection cnx = cnxParam;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (cnxParam == null || cnxParam.isClosed())
				cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT PL.CODE,PL.NAME,PL.STATE FROM PRICELIST PL " +
					"INNER JOIN PRICELISTCLIENT PLC ON PL.COMPANYCODE=PLC.COMPANYCODE AND PL.CODE=PLC.PRICELISTCODE " +
					"WHERE PL.COMPANYCODE = ? AND PLC.CLIENTCODE = ? AND PLC.STATE=?");
			
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, clientCode);
			ps.setString(3, "ACT");
			rs = ps.executeQuery();

			List<PriceList> priceLists = new ArrayList<PriceList>();
			
			while (rs.next()) {
				PriceList ct = new PriceList();
				ct.setCode(rs.getString(1));
				ct.setName(rs.getString(2));
				ct.setState(rs.getString(3));
				priceLists.add(ct);
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
	
	public static List<Itinerary> getAllClientsFromSearch(User user,String itinerary,String date){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT I.CODE,I.DAY,I.FREQUENCY,I.DATE,C.CODE,C.NAME,C.RUC,C.PHONE,C.CLIENTTYPECODE,CA.CODE,CA.NAME,CA.TYPE " +
					"FROM ITINERARY I INNER JOIN CLIENT C ON C.COMPANYCODE = I.COMPANYCODE AND C.CODE=I.CLIENTCODE " +
					"INNER JOIN CLIENTADDRESS CA ON C.COMPANYCODE=CA.COMPANYCODE AND C.CODE=CA.CLIENTCODE AND CA.CODE=I.CLIENTADDRESSCODE " +
					"INNER JOIN USERITINERARY UI ON UI.COMPANYCODE = I.COMPANYCODE AND I.CODE=UI.ITINERARYCODE " +
					"WHERE UI.COMPANYCODE = ? AND UI.USERCODE = ? AND " +
					"UI.ASSIGNEDDATE=(SELECT MAX(UI2.ASSIGNEDDATE) FROM USERITINERARY UI2 WHERE UI2.COMPANYCODE=UI.COMPANYCODE AND UI2.USERCODE=UI.USERCODE)");
			
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, user.getCode());
			rs = ps.executeQuery();
			List<Entity> clientTypes = getAllClientTypes(user, cnx);
			List<Itinerary> itineraries = new ArrayList<Itinerary>();
			while(rs.next()){
				Itinerary i = new Itinerary();
				i.setCompany(user.getCompany());
				i.setCode(rs.getString(1));
				i.setDay(rs.getShort(2));
				i.setFrequency(rs.getString(3));
				i.setDate(rs.getDate(4));
				Client c = new Client();
				c.setCompany(user.getCompany());
				c.setCode(rs.getString(5));
				c.setName(rs.getString(6));
				c.setRuc(rs.getString(7));
				c.setPhone(rs.getString(8));
				c.setClientType((ClientType)Entity.findEntity(clientTypes, rs.getString(9)));
				c.setSaleModes(getAllSaleModes(user, c.getCode(), cnx));
				c.setPriceLists(getAllPriceLists(user, c.getCode(), cnx));
				ClientAddress ca = new ClientAddress();
				ca.setCompany(user.getCompany());
				ca.setCode(rs.getString(10));
				ca.setName(rs.getString(11));
				ca.setType(rs.getString(12));
				ca.setClient(c);
				
				i.setClientAddress(ca);
				itineraries.add(i);
			}
			return itineraries;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				if(cnx != null)cnx.close();
			}catch(Exception x){
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<ClientAddress> getAllAddressesFromClient(User user, String clientCode, Connection cnx){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection localCnx = cnx;
		try {
			if(cnx==null)
				localCnx = DBConnection.getConnection();

			ps = localCnx.prepareStatement("SELECT CA.CODE,CA.NAME,CA.TYPE FROM CLIENTADDRESS CA WHERE CA.COMPANYCODE = ? AND CA.CLIENTCODE = ? AND CA.STATE = ?");
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, clientCode);
			ps.setString(3, "ACT");
			
			rs = ps.executeQuery();

			List<ClientAddress> clientAddresses = new ArrayList<ClientAddress>();
			
			while (rs.next()) {
				ClientAddress clientAddress = new ClientAddress();
				clientAddress.setCode(rs.getString(1));
				clientAddress.setName(rs.getString(2));
				clientAddress.setType(rs.getString(3));
				clientAddresses.add(clientAddress);
			}

			return clientAddresses;
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx == null && localCnx!=null)
					localCnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}


	public static List<CreditClient> findClientsByCriteria(User user,String clientCode,
			String ruc, String clientName) {
		
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			cnx = DBConnection.getConnection();
			String sql = "SELECT C.CODE,C.NAME,C.RUC,CT.CODE,CT.NAME,IFNULL(CR.CODE,'-'),IFNULL(CR.NAME,'-'),IFNULL(CR.SYMBOL,'-'),IFNULL(CC.AMOUNT,0) FROM " +
					"CLIENT C INNER JOIN CLIENTTYPE CT " +
					"ON C.COMPANYCODE = CT.COMPANYCODE AND CT.CODE = C.CLIENTTYPECODE "+
					"LEFT JOIN CREDITCLIENT CC "+
					"ON C.COMPANYCODE = CC.COMPANYCODE AND C.CODE=CC.CLIENTCODE "+
					"LEFT JOIN CURRENCY CR "+
					"ON CR.COMPANYCODE = CC.COMPANYCODE AND CR.CODE = CC.CURRENCYCODE " +
					"WHERE C.COMPANYCODE = ?";
			if(clientCode!=null) sql += " AND C.CODE LIKE ?";
			if(ruc!=null) sql += " AND C.RUC LIKE ?";
			if(clientName!=null) sql += " AND C.NAME LIKE ?";
			
			
			int index = 2;
			ps = cnx.prepareStatement(sql);
			ps.setString(1, user.getCompany().getCode());
			if(clientCode!=null) ps.setString(index++, clientCode + "%");
			if(ruc!=null) ps.setString(index++, ruc+"%");
			if(clientName!=null) ps.setString(index, clientName+"%");
			
			rs = ps.executeQuery();
			
			List<CreditClient> clients = new ArrayList<CreditClient>();
			
			while(rs.next()){
				CreditClient cc = new CreditClient();
				Client c = new Client();
				c.setCode(rs.getString(1));
				c.setName(rs.getString(2));
				c.setRuc(rs.getString(3));
				c.setClientType(new ClientType());
				c.getClientType().setCode(rs.getString(4));
				c.getClientType().setName(rs.getString(5));
				Currency currency = new Currency();
				currency.setCode(rs.getString(6));
				currency.setName(rs.getString(7));
				currency.setSymbol(rs.getString(8));
				cc.setClient(c);
				cc.setCurrency(currency);
				cc.setAmount(rs.getDouble(9));
				clients.add(cc);
			}
			return clients;
			
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
	
	public static List<Client> findClientsByCtriteria(User user,String filter){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT C.CODE,C.NAME,C.RUC,C.PHONE,C.CLIENTTYPECODE " +
					"FROM CLIENT C WHERE C.COMPANYCODE=? AND C.STATE<>'INA' AND (C.CODE LIKE ? OR C.NAME LIKE ? OR C.RUC LIKE ?)");
			
			ps.setString(1, user.getCompany().getCode());
			ps.setString(2, filter + "%");
			ps.setString(3, "%" + filter + "%");
			ps.setString(4, filter + "%");
			rs = ps.executeQuery();
			List<Entity> clientTypes = getAllClientTypes(user, cnx);
			List<Client> clients = new ArrayList<Client>();
			while(rs.next()){
				
				Client c = new Client();
				c.setCompany(user.getCompany());
				c.setCode(rs.getString(1));
				c.setName(rs.getString(2));
				c.setRuc(rs.getString(3));
				c.setPhone(rs.getString(4));
				c.setClientType((ClientType)Entity.findEntity(clientTypes, rs.getString(5)));
				c.setSaleModes(getAllSaleModes(user, c.getCode(), cnx));
				c.setPriceLists(getAllPriceLists(user, c.getCode(), cnx));
				c.setClientAddresses(getAllAddressesFromClient(user, c.getCode(),cnx));
				clients.add(c);
				
			}
			return clients;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				if(cnx != null)cnx.close();
			}catch(Exception x){
				x.printStackTrace();
			}
		}
		return null;
	}


	public static int add(Client client, ClientAddress clientAddress) {
	    Connection cnx = null;
	    PreparedStatement psClient = null, psAddress = null;
	    
	    try{
		cnx = DBConnection.getConnection();
		psClient = cnx.prepareStatement("INSERT INTO CLIENT VALUES(?,?,?,?,?,?,?)");
		psClient.setString(1, client.getCompany().getCode());
		psClient.setString(2, client.getCode());
		psClient.setString(3, client.getName());
		psClient.setString(4, client.getRuc());
		psClient.setString(5, client.getPhone());
		psClient.setString(6, client.getClientType().getCode());
		psClient.setString(7, client.getState());
		int c = psClient.executeUpdate();
		if(c>0 && clientAddress!=null){
		    psAddress =  cnx.prepareStatement("INSERT INTO CLIENTADDRESS VALUES(?,?,?,?,?,?,?,?)");
		    psAddress.setString(1, client.getCompany().getCode());
		    psAddress.setString(2, client.getCode());
		    psAddress.setString(3, clientAddress.getCode());
		    psAddress.setString(4, clientAddress.getName());
		    psAddress.setString(5, clientAddress.getType());
		    psAddress.setString(6, clientAddress.getLatitude());
		    psAddress.setString(7, clientAddress.getLongitude());
		    psAddress.setString(8, clientAddress.getState());
		    
		    c += psAddress.executeUpdate();
		}
		return c;
	    }catch(Exception e){
		e.printStackTrace();
		return -1;
	    }finally{
		try {
		    if(psClient!=null) psClient.close();
		    if(psAddress!=null) psAddress.close();
		    if(cnx!=null) cnx.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}


	public static List<Client> find(Client client) {
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try {
		
		String sql = "SELECT C.CODE,C.NAME,C.RUC,C.PHONE,CT.CODE,CT.NAME,CA.CODE,CA.NAME,CA.TYPE,CA.LATITUDE,CA.LONGITUDE " +
				"FROM CLIENT C INNER JOIN CLIENTTYPE CT ON CT.COMPANYCODE = C.COMPANYCODE AND CT.CODE = C.CLIENTTYPECODE "+
				"LEFT JOIN CLIENTADDRESS CA ON C.COMPANYCODE=CA.COMPANYCODE AND C.CODE = CA.CLIENTCODE AND CA.STATE='ACT' AND CA.TYPE='P' WHERE C.COMPANYCODE = ?";
		
		if(client.getCode().length()!=0)
		    sql += " AND C.CODE LIKE ?";
		if(client.getName().length()!=0)
		    sql += " AND C.NAME LIKE ?";
		if(client.getRuc().length()!=0)
		    sql += " AND C.RUC LIKE ?";
		if(client.getPhone().length()!=0)
		    sql += " AND C.PHONE LIKE ?";
		if(client.getClientType().getCode().length()!=0)
		    sql += " AND CT.CODE LIKE ?";
		sql += " AND C.STATE = ?";
		
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement(sql);

		int c = 1;
		ps.setString(c++, client.getCompany().getCode());
		if(client.getCode().length()!=0)
		    ps.setString(c++, client.getCode() + "%");
		if(client.getName().length()!=0)
		    ps.setString(c++, client.getName() + "%");
		if(client.getRuc().length()!=0)
		    ps.setString(c++, client.getRuc() + "%");
		if(client.getPhone().length()!=0)
		    ps.setString(c++, client.getPhone() + "%");
		if(client.getClientType().getCode().length()!=0)
		    ps.setString(c++, client.getClientType().getCode());
		ps.setString(c, client.getState());
		
		rs = ps.executeQuery();
		List<Client> list = new ArrayList<Client>();
		while(rs.next()){
		    Client cl = new Client();
		    cl.setCode(rs.getString(1));
		    cl.setName(rs.getString(2));
		    cl.setRuc(rs.getString(3));
		    cl.setPhone(rs.getString(4));
		    cl.setClientType(new ClientType());
		    cl.getClientType().setCode(rs.getString(5));
		    cl.getClientType().setName(rs.getString(6));
		    cl.setClientAddresses(new ArrayList<ClientAddress>());
		    ClientAddress ca = new ClientAddress();
		    ca.setCode(rs.getString(7));
		    ca.setName(rs.getString(8));
		    ca.setType(rs.getString(9));
		    ca.setLatitude(rs.getString(10));
		    ca.setLongitude(rs.getString(11));
		    cl.getClientAddresses().add(ca);
		    list.add(cl);
		}
		return list;
	    } catch (Exception e){
		e.printStackTrace();
		return null;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(ps!=null)ps.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    
	    
	}


	public static List<Client> getAllClients(String companyCode) {
	    
	    Connection cnx = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try {
		cnx = DBConnection.getConnection();
		ps = cnx.prepareStatement("SELECT C.CODE,C.NAME,C.RUC,C.PHONE,CT.CODE,CT.NAME,CA.CODE,CA.NAME,CA.TYPE,CA.LATITUDE,CA.LONGITUDE " +
				"FROM CLIENT C INNER JOIN CLIENTTYPE CT ON CT.COMPANYCODE = C.COMPANYCODE AND CT.CODE = C.CLIENTTYPECODE "+
				"LEFT JOIN CLIENTADDRESS CA ON C.COMPANYCODE=CA.COMPANYCODE AND C.CODE = CA.CLIENTCODE " +
				"AND CA.STATE='ACT' AND CA.TYPE='P'" +
				"WHERE C.COMPANYCODE = ? AND C.STATE = ? ");

		ps.setString(1, companyCode);
		ps.setString(2, "ACT");
		
		rs = ps.executeQuery();
		List<Client> list = new ArrayList<Client>();
		while(rs.next()){
		    Client cl = new Client();
		    cl.setCode(rs.getString(1));
		    cl.setName(rs.getString(2));
		    cl.setRuc(rs.getString(3));
		    cl.setPhone(rs.getString(4));
		    cl.setClientType(new ClientType());
		    cl.getClientType().setCode(rs.getString(5));
		    cl.getClientType().setName(rs.getString(6));
		    cl.setClientAddresses(new ArrayList<ClientAddress>());
		    ClientAddress ca = new ClientAddress();
		    ca.setCode(rs.getString(7));
		    ca.setName(rs.getString(8));
		    ca.setType(rs.getString(9));
		    ca.setLatitude(rs.getString(10));
		    ca.setLongitude(rs.getString(11));
		    cl.getClientAddresses().add(ca);
		    list.add(cl);
		}
		return list;
	    } catch (Exception e){
		e.printStackTrace();
		return null;
	    }finally{
		try {
		    if(rs!=null)rs.close();
		    if(ps!=null)ps.close();
		    if(cnx!=null)cnx.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}


	public static List<Entity> getAllClientTypes() {
	    Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();

			ps = cnx.prepareStatement("SELECT CT.CODE, CT.NAME, CT.STATE FROM CLIENTTYPE CT WHERE CT.STATE=?");
			ps.setString(1, "ACT");
			rs = ps.executeQuery();

			List<Entity> clientTypes = new ArrayList<Entity>();
			
			while (rs.next()) {
				ClientType ct = new ClientType();
				ct.setCode(rs.getString(1));
				ct.setName(rs.getString(2));
				ct.setState(rs.getString(3));
				clientTypes.add(ct);
			}

			return clientTypes;
		}catch(Exception x){
			x.printStackTrace();
		}finally{
			try{
				if(rs != null)
				    rs.close();
				if(ps != null)
				    ps.close();
				if(cnx != null)
				    cnx.close();
			}catch(Exception x){
			    x.printStackTrace();
			}
		}
		return null;
	}


	public static int setClientTypes(String companyCode, List<ClientType> clientTypes) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM CLIENTTYPE WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO CLIENTTYPE VALUES(?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE CLIENTTYPE SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (ClientType clientType: clientTypes) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, clientType.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, clientType.getCode());
			psInsert.setString(3, clientType.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, clientType.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, clientType.getCode());
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


	public static void setClients(Company company, List<Client> clients) {
	    Connection cnx = null;
	    PreparedStatement psQuery = null, psInsert = null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		cnx.setAutoCommit(false);
		psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM CLIENT WHERE COMPANYCODE = ? AND CLIENTCODE = ?");
		psInsert = cnx.prepareStatement("INSERT INTO CLIENT VALUES(?,?,?,?,?,?,'ACT')");
		psUpdate = cnx.prepareStatement("UPDATE CLIENT SET NAME = ? AND RUC = ? AND PHONE = ? AND CLIENTTYPECODE = ? WHERE COMPANYCODE = ? AND CLIENTCODE = ?");
		boolean newClient;
		for(Client client:clients){
		    psQuery.setString(1, company.getCode());
		    psQuery.setString(2, client.getCode());
		    rs = psQuery.executeQuery();
		    newClient = rs.next() && rs.getInt(1)==0;
		    if(newClient){
			psInsert.setString(1, company.getCode());
			psInsert.setString(2, client.getCode());
			psInsert.setString(3, client.getName());
			psInsert.setString(4, client.getRuc());
			psInsert.setString(5, client.getPhone());
			psInsert.setString(6, client.getClientType().getCode());
			psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, client.getName());
			psUpdate.setString(2, client.getRuc());
			psUpdate.setString(3, client.getPhone());
			psUpdate.setString(4, client.getClientType().getCode());
			psUpdate.setString(5, company.getCode());
			psUpdate.setString(6, client.getCode());
			psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		    if(client.getClientAddresses()!=null && client.getClientAddresses().size()>0){
			client.setCompany(company);
			setClientAddresses(client,newClient,cnx);
		    }
		}
		cnx.commit();
	    } catch (Exception e) {
		try{
		    cnx.rollback();  
		}catch(Exception e2){
		    e2.printStackTrace();
		}
	    }finally{
		try{
		    if(psQuery!=null) psQuery.close();
		    if(psInsert!=null) psInsert.close();
		    if(psUpdate!=null) psUpdate.close();
		}catch(Exception e2) {
		    e2.printStackTrace();
		}
	    }
	}


	private static void setClientAddresses(Client client, boolean newClient,
		Connection cnx) throws Exception {
	    PreparedStatement psQuery = null,psInsert =null ,psUpdate = null;
	    ResultSet rs = null;
	    try {
		psInsert = cnx.prepareStatement("INSERT INTO CLIENTADDRESS VALUES(?,?,?,?,?,?;?,'ACT')");
		if(!newClient){
		    psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM CLIENTADDRESS WHERE COMPANYCODE = ? AND CLIENTCODE = ? AND CODE = ?");
		    psUpdate = cnx.prepareStatement("UPDATE CLIENTADDRESS SET NAME = ?, TYPE = ?, LATITUDE = ?, LONGITUDE = ? WHERE COMPANYCODE = ? AND CLIENTCODE = ? AND CODE = ?");
		}
		boolean insert;
		for(ClientAddress clientAddress:client.getClientAddresses()){
		    insert = newClient;
		    if(!insert){
			psQuery.setString(1, client.getCompany().getCode());
			psQuery.setString(2, client.getCode());
			psQuery.setString(3, clientAddress.getCode());
			rs = psQuery.executeQuery();
			insert = rs.next() && rs.getInt(1)==0;
			rs.close();
			psQuery.clearParameters();
		    }
		    if(insert){
			psInsert.setString(1, client.getCompany().getCode());
		    	psInsert.setString(2, client.getCode());
		    	psInsert.setString(3, clientAddress.getCode());
		    	psInsert.setString(4, clientAddress.getName());
		    	psInsert.setString(4, clientAddress.getType());
		    	psInsert.setString(6, clientAddress.getLatitude());
		    	psInsert.setString(7, clientAddress.getLongitude());
		    	psInsert.executeUpdate();
		    	psInsert.clearParameters();
		    }else{
		    	psUpdate.setString(1, clientAddress.getCode());
		    	psUpdate.setString(2, clientAddress.getName());
		    	psUpdate.setString(3, clientAddress.getType());
		    	psUpdate.setString(4, clientAddress.getLatitude());
		    	psUpdate.setString(5, clientAddress.getLongitude());
		    	psUpdate.setString(6, client.getCompany().getCode());
		    	psUpdate.setString(7, client.getCode());
		    	psUpdate.executeUpdate();
		    	psUpdate.clearParameters();
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
		throw e;
	    }finally{
		try {
		    if(psInsert!=null)psInsert.close();
		    if(psUpdate!=null)psUpdate.close();
		    if(psQuery!=null)psQuery.close();
		} catch (Exception e) {
		    throw e;
		}
	    }
	    
	}


	public static int edit(Client client, ClientAddress clientAddress) {
	    
	    Connection cnx = null;
	    PreparedStatement psClient = null, psAddress = null;
	    
	    try{
		cnx = DBConnection.getConnection();
		psClient = cnx.prepareStatement("UPDATE CLIENT SET NAME = ? ,RUC = ? ,PHONE = ? ,CLIENTTYPECODE = ?, STATE = ? WHERE COMPANYCODE = ? AND CODE = ?");
		psClient.setString(1, client.getName());
		psClient.setString(2, client.getRuc());
		psClient.setString(3, client.getPhone());
		psClient.setString(4, client.getClientType().getCode());
		psClient.setString(5, client.getState());
		psClient.setString(6, client.getCompany().getCode());
		psClient.setString(7, client.getCode());
		int c = psClient.executeUpdate();
		if(c>0 && clientAddress!=null){
		    psAddress =  cnx.prepareStatement("INSERT INTO CLIENTADDRESS VALUES(?,?,?,?,?,?,?,?) ON DUPLICATE  KEY UPDATE NAME = VALUES(NAME), TYPE = VALUES(TYPE), LATITUDE = VALUES(LATITUDE), LONGITUDE = VALUES(LONGITUDE)");
		    psAddress.setString(1, client.getCompany().getCode());
		    psAddress.setString(2, client.getCode());
		    psAddress.setString(3, clientAddress.getCode());
		    psAddress.setString(4, clientAddress.getName());
		    psAddress.setString(5, clientAddress.getType());
		    psAddress.setString(6, clientAddress.getLatitude());
		    psAddress.setString(7, clientAddress.getLongitude());
		    psAddress.setString(8, clientAddress.getState());
		    
		    c += psAddress.executeUpdate();
		}
		return c;
	    }catch(Exception e){
		e.printStackTrace();
		return -1;
	    }finally{
		try {
		    if(psClient!=null) psClient.close();
		    if(psAddress!=null) psAddress.close();
		    if(cnx!=null) cnx.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
}