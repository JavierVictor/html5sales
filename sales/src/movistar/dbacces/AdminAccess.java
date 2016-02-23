package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import movistar.bean.Admin;
import movistar.bean.Company;
import movistar.util.DBConnection;

public class AdminAccess {
	public static Admin authenticate(String code, String password, String companyCode){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps =  cnx.prepareStatement("SELECT C.CODE,C.NAME,A.CODE,A.NAME,A.PASSWORD FROM COMPANY C INNER JOIN ADMIN A ON C.CODE=A.COMPANYCODE WHERE C.CODE=? AND A.CODE = ? AND A.PASSWORD = ?");
			ps.setString(1, companyCode);
			ps.setString(2, code);
			ps.setString(3, password);
			rs = ps.executeQuery();
			if(rs.next()){
				Admin admin = new Admin();
				admin.setCompany(new Company());
				admin.getCompany().setCode(rs.getString(1));
				admin.getCompany().setName(rs.getString(2));
				admin.setCode(rs.getString(3));
				admin.setName(rs.getString(4));
				admin.setPassword(rs.getString(5));
				admin.setState("ACT");
				return admin;
			}
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
}
