package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import movistar.bean.Company;
import movistar.bean.User;
import movistar.bean.Warehouse;
import movistar.util.DBConnection;

public class UserAccess {
	public static String saveUser(User user) {
		Connection cnx = null;
		PreparedStatement psValidate = null, ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			psValidate = cnx
					.prepareStatement("select CODE from USER WHERE COMPANYCODE=? AND WAREHOUSECODE = ? AND CODE=?");
			psValidate.setString(1, user.getCompany().getCode());
			psValidate.setString(2, user.getWarehouse().getCode());
			psValidate.setString(3, user.getCode());
			rs = psValidate.executeQuery();
			if (!rs.next()) {
				ps = cnx.prepareStatement("INSERT INTO USER VALUES(?,?,?,?,?,?,NULL,NULL,?)");
				ps.setString(1, user.getCompany().getCode());
				ps.setString(2, user.getWarehouse().getCode());
				ps.setString(3, user.getCode());
				ps.setString(4, user.getName());
				ps.setString(5, user.getPhoneNumber());
				ps.setString(6, user.getPassword());
				ps.setString(7, user.getState());
			} else {
				ps = cnx.prepareStatement("UPDATE USER SET WAREHOUSECODE=?, NAME = ?, PHONENUMBER = ?, PASSWORD = ?, STATE = ? WHERE COMPANYCODE = ? AND CODE =?");
				ps.setString(1, user.getWarehouse().getCode());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPhoneNumber());
				ps.setString(4, user.getPassword());
				ps.setString(5, user.getState());
				ps.setString(6, user.getCompany().getCode());
				ps.setString(7, user.getCode());
			}
			ps.executeUpdate();
			return user.getCode();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (psValidate != null)
					psValidate.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static User authUser(String usercode, String password,
			String latitude, String longitude) {
		Connection cnx = null;
		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT C.CODE,C.NAME,C.STATE,W.CODE,W.NAME,W.STATE,U.CODE,U.NAME,U.PHONENUMBER,U.PASSWORD,U.STATE FROM USER U INNER JOIN COMPANY C ON C.CODE = U.COMPANYCODE INNER JOIN WAREHOUSE W ON U.WAREHOUSECODE = W.CODE WHERE "
					+ "U.CODE = ? AND U.PASSWORD = ?");
			ps.setString(1, usercode);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()) {
				psUpdate = cnx
						.prepareStatement("UPDATE USER SET LATITUDE=?,LONGITUDE=? WHERE CODE=? AND PASSWORD=?");
				psUpdate.setString(1, latitude);
				psUpdate.setString(2, longitude);
				psUpdate.setString(3, usercode);
				psUpdate.setString(4, password);
				psUpdate.executeUpdate();

				User user = new User();
				user.setCompany(new Company());
				user.getCompany().setCode(rs.getString(1));
				user.getCompany().setName(rs.getString(2));
				user.getCompany().setState(rs.getString(3));
				user.setWarehouse(new Warehouse());
				user.getWarehouse().setCode(rs.getString(4));
				user.getWarehouse().setName(rs.getString(5));
				user.getWarehouse().setState(rs.getString(6));
				user.getWarehouse().setCompany(user.getCompany());
				user.setCode(rs.getString(7));
				user.setName(rs.getString(8));
				user.setPhoneNumber(rs.getString(9));
				user.setPassword(rs.getString(10));
				user.setState(rs.getString(11));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (psUpdate != null)
					psUpdate.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		return null;
	}

	public static List<User> getAllUsers(String companyCode) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT W.CODE,W.NAME,U.CODE,U.NAME,U.PHONENUMBER,ifnull(U.LATITUDE,0),ifnull(U.LONGITUDE,0) FROM WAREHOUSE W INNER JOIN USER U ON W.COMPANYCODE=U.COMPANYCODE AND W.CODE=U.WAREHOUSECODE WHERE W.COMPANYCODE=? AND U.STATE = 'ACT'");
			ps.setString(1, companyCode);
			rs = ps.executeQuery();
			List<User> list = new ArrayList<User>();
			while (rs.next()) {
				User user = new User();
				user.setWarehouse(new Warehouse());
				user.getWarehouse().setCode(rs.getString(1));
				user.getWarehouse().setName(rs.getString(2));
				user.setCode(rs.getString(3));
				user.setName(rs.getString(4));
				user.setPhoneNumber(rs.getString(5));
				user.setLatitude(rs.getString(6));
				user.setLongitude(rs.getString(7));
				list.add(user);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	public static User getUser(String companyCode, String userCode) {

		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("SELECT W.CODE,W.NAME,U.CODE,U.NAME,U.PHONENUMBER FROM WAREHOUSE W INNER JOIN USER U ON W.COMPANYCODE=U.COMPANYCODE AND W.CODE=U.WAREHOUSECODE WHERE COMPANYCODE=? AND USERCODE=?");
			ps.setString(1, companyCode);
			ps.setString(2, userCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setWarehouse(new Warehouse());
				user.getWarehouse().setCode(rs.getString(1));
				user.getWarehouse().setName(rs.getString(2));
				user.setCode(rs.getString(3));
				user.setName(rs.getString(4));
				user.setPhoneNumber(rs.getString(5));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public static int delete(String companyCode, String[] usercodes) {
		Connection cnx = null;
		PreparedStatement ps = null;
		try {
			cnx = DBConnection.getConnection();
			ps = cnx.prepareStatement("UPDATE USER SET STATE='INA' WHERE COMPANYCODE=? AND WAREHOUSECODE=? AND CODE=?");
			int c = 0;
			for(String user:usercodes){
				ps.setString(1, companyCode);
				ps.setString(2, user.substring(0,user.indexOf('-')));
				ps.setString(3, user.substring(user.indexOf('-')+1));
				c += ps.executeUpdate();
				ps.clearParameters();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<User> find(User user) {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cnx = DBConnection.getConnection();
			String sql = "SELECT W.CODE,W.NAME,U.CODE,U.NAME,U.PHONENUMBER,ifnull(U.LATITUDE,0),ifnull(U.LONGITUDE,0) FROM WAREHOUSE W INNER JOIN USER U ON W.COMPANYCODE=U.COMPANYCODE AND W.CODE=U.WAREHOUSECODE WHERE W.COMPANYCODE=?";
			if(user.getWarehouse().getCode().length()!=0)
				sql += " AND W.CODE=?";
			if(user.getCode().length()!=0)
				sql += " AND U.CODE LIKE ?";
			if(user.getName().length()!=0)
				sql += " AND U.NAME LIKE ?";
			if(user.getPhoneNumber().length()!=0)
				sql += " AND U.PHONENUMBER LIKE ?";
			sql += " AND U.STATE = ?";
			
			ps = cnx.prepareStatement(sql);
			
			int c = 1;
			ps.setString(c++, user.getCompany().getCode());
			if(user.getWarehouse().getCode().length()!=0)
				ps.setString(c++, user.getWarehouse().getCode());
			if(user.getCode().length()!=0)
				ps.setString(c++, user.getCode()+"%");
			if(user.getName().length()!=0)
				ps.setString(c++,"%" + user.getName() + "%");
			if(user.getPhoneNumber().length()!=0)
				ps.setString(c++,"%" + user.getPhoneNumber() + "%");
			ps.setString(c++, user.getState());
			
			rs = ps.executeQuery();
			List<User> list = new ArrayList<User>();
			while (rs.next()) {
				User bean = new User();
				bean.setWarehouse(new Warehouse());
				bean.getWarehouse().setCode(rs.getString(1));
				bean.getWarehouse().setName(rs.getString(2));
				bean.setCode(rs.getString(3));
				bean.setName(rs.getString(4));
				bean.setPhoneNumber(rs.getString(5));
				bean.setLatitude(rs.getString(6));
				bean.setLongitude(rs.getString(7));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cnx != null)
					cnx.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public static String setUsers(Company company, List<User> users){
	    Connection cnx = null;
	    PreparedStatement psQuery=null, psUpdate = null,psInsert = null;
	    ResultSet rs = null;
	    
	    try {
		cnx = DBConnection.getConnection();
		cnx.setAutoCommit(false);
		psQuery = cnx.prepareStatement("SELECT COUNT(0) FROM USER WHERE COMPANYCODE = ? AND WAREHOUSECODE = ? AND USERCODE = ?");
		psInsert = cnx.prepareStatement("INSERT INTO USER VALUES(?,?,?,?,?,?,null,null,'ACT')");
		psUpdate = cnx.prepareStatement("UPDATE USER SET NAME=?, PHONENUMBER =?,PASSWORD=? WHERE COMPANYCODE = ? AND WAREHOUSECODE = ? AND CODE = ?");
		int found = 0;
		
		for(User user:users){
		    psQuery.setString(1, company.getCode());
		    psQuery.setString(2, user.getWarehouse().getCode());
		    psQuery.setString(3, user.getCode());
		    rs =   psQuery.executeQuery();
		    rs.next();
		    found = rs.getInt(1);
		    rs.close();
		    if(found==0){
			psInsert.setString(1, company.getCode());
			psInsert.setString(2, user.getWarehouse().getCode());
			psInsert.setString(3, user.getCode());
			psInsert.setString(4, user.getName());
			psInsert.setString(5, user.getPhoneNumber());
			psInsert.setString(6, user.getPassword());
			psInsert.executeUpdate();
			psInsert.clearParameters();
		    }else{
			psUpdate.setString(1, user.getName());
			psUpdate.setString(2, user.getPhoneNumber());
			psUpdate.setString(3, user.getPassword());
			psUpdate.setString(4, company.getCode());
			psUpdate.setString(5, user.getWarehouse().getCode());
			psUpdate.setString(6, user.getCode());
			psUpdate.executeUpdate();
			psUpdate.clearParameters();
		    }
		}
	    }catch(Exception e){
		try {
		    if(cnx!=null)
			cnx.rollback();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    } finally {
		try{
		    if(rs!=null)rs.close();
		    if(psQuery!=null) psQuery.close();
		    if(psUpdate!=null) psUpdate.close();
		    if(psInsert!=null) psInsert.close();
		    if(cnx!=null) cnx.close();
		}catch(Exception x){
		    x.printStackTrace();
		}
		
	    }
	    return null;
	}

}