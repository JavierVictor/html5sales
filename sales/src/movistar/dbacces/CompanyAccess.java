package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import movistar.bean.Company;
import movistar.bean.Warehouse;
import movistar.util.DBConnection;

public class CompanyAccess {
	public static List<Company> getCompanies(){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps =  cnx.prepareStatement("SELECT * FROM COMPANY WHERE STATE = 'ACT'");
			rs = ps.executeQuery();
			List<Company> companies = new ArrayList<Company>();
			while(rs.next()){
				Company company = new Company();
				company.setCode(rs.getString(1));
				company.setName(rs.getString(2));
				company.setState("ACT");
				companies.add(company);
			}
			return companies;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			if(rs!=null) rs.close();
			if(ps!=null)ps.close();
			if(cnx!=null)cnx.close();
			}catch(Exception x){x.printStackTrace();}
		}
		return null;
	}

	public static int setWarehouses(String companyCode,	List<Warehouse> warehouses) {
	    Connection cnx = null;
	    PreparedStatement psInsert = null, psSelect=null, psUpdate = null;
	    ResultSet rs = null;
	    try {
		cnx = DBConnection.getConnection();
		psSelect = cnx.prepareStatement("SELECT COUNT(0) FROM PRICELIST WHERE COMPANYCODE=? AND CODE=?");
		psInsert = cnx.prepareStatement("INSERT INTO PRICELIST VALUES(?,?,?,'ACT')");
		psUpdate= cnx.prepareStatement("UPDATE PRICELIST SET NAME = ? WHERE COMPANYCODE=? AND CODE=?");
		int c =0;
		for (Warehouse warehouse: warehouses) {
		    psSelect.setString(1, companyCode);
		    psSelect.setString(2, warehouse.getCode());
		    rs = psSelect.executeQuery();
		    if(rs.next()&&rs.getInt(1)==0){
			psInsert.setString(1, companyCode);
			psInsert.setString(2, warehouse.getCode());
			psInsert.setString(3, warehouse.getName());
			c+= psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, warehouse.getName());
			psUpdate.setString(2, companyCode);
			psUpdate.setString(3, warehouse.getCode());
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

	public static void turnDownCompany(Company company) {
	    Connection cnx = null;
		PreparedStatement ps = null;
		try{
			cnx = DBConnection.getConnection();
			ps =  cnx.prepareStatement("UPDATE COMPANY SET STATE = ? WHERE CODE = ?");
			ps.setString(1, company.getState());
			ps.setString(2, company.getCode());
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			if(ps!=null)ps.close();
			if(cnx!=null)cnx.close();
			}catch(Exception x){x.printStackTrace();}
		}
	}
}
