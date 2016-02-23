package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import movistar.bean.Company;
import movistar.bean.Warehouse;
import movistar.util.DBConnection;

public class WarehouseAccess {
	public static List<Warehouse> getWarehouses(){
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			cnx = DBConnection.getConnection();
			ps =  cnx.prepareStatement("SELECT COMPANYCODE,CODE,NAME FROM WAREHOUSE WHERE STATE = 'ACT'");
			rs = ps.executeQuery();
			List<Warehouse> warehouses = new ArrayList<Warehouse>();
			while(rs.next()){
				Warehouse warehouse = new Warehouse();
				warehouse.setCompany(new Company());
				warehouse.getCompany().setCode(rs.getString(1));
				warehouse.setCode(rs.getString(2));
				warehouse.setName(rs.getString(3));
				warehouses.add(warehouse);
			}
			return warehouses;
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
