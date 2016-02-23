package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import movistar.bean.Configuration;
import movistar.util.DBConnection;

public class ConfigurationAccess {
    public static List<Configuration> getAllConfiguration(String companyCode){
	Connection cnx = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try{
	    cnx = DBConnection.getConnection();
	    ps = cnx.prepareStatement("SELECT C.CODE,C.DISPLAYNAME,C.DESCRIPTION,C.VALUE FROM CONFIGURATION C WHERE C.COMPANYCODE = ? AND C.STATE='ACT'");
	    ps.setString(1, companyCode);
	    rs = ps.executeQuery();
	    List<Configuration> configurations = new ArrayList<Configuration>();
	    while(rs.next()){
		Configuration config = new Configuration();
		config.setCode(rs.getString(1));
		config.setName(rs.getString(2));
		config.setDescription(rs.getString(3));
		config.setValue(rs.getString(4));
		configurations.add(config);
	    }
	    return configurations;
	}catch(Exception e){
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

    public static boolean save(String companyCode, List<Configuration> configurations) {
	Connection cnx = null;
	PreparedStatement ps = null;
	try {
	    cnx = DBConnection.getConnection();
	    ps = cnx.prepareStatement("UPDATE CONFIGURATION SET VALUE = ? WHERE COMPANYCODE=? AND CODE = ?");
	    int c = 0;
	    for(Configuration configuration:configurations){
		ps.setString(1, configuration.getValue());
		ps.setString(2, companyCode);
		ps.setString(3, configuration.getCode());
		c += ps.executeUpdate();
		ps.clearParameters();
			
	    }
	    
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}finally{
	    try {
		if(ps!=null)ps.close();
		if(cnx!=null)cnx.close();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	
    }
}
