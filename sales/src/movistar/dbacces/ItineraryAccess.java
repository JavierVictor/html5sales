package movistar.dbacces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import movistar.bean.Company;
import movistar.bean.Itinerary;
import movistar.bean.UserItinerary;
import movistar.util.DBConnection;

public class ItineraryAccess {

    public static void setItineraries(Company company, List<UserItinerary> userItineraries) {
	Connection cnx = null;
	PreparedStatement ps = null, psUpdate = null, psInsert = null;
	try {
	    cnx = DBConnection.getConnection();
	    cnx.setAutoCommit(false);
	    ps = cnx.prepareStatement("INSERT INTO USERITINERARY VALUES(?,?,?,SYSDATE())");
	    psUpdate = cnx.prepareStatement("UPDATE ITINERARY SET STATE = 'INA' WHERE COMPANYCODE = ? AND CODE = ?");
	    psInsert = cnx.prepareStatement("INSERT INTO ITINERARY VALUES(?,?,?,?,?,?,?,'ACT')");
	    
	    for(UserItinerary userItinerary : userItineraries){
		ps.setString(1, company.getCode());
		ps.setString(2, userItinerary.getUser().getCode());
		ps.setString(3, userItinerary.getCode());
		ps.executeUpdate();
		psUpdate.setString(1, company.getCode());
		psUpdate.setString(2, userItinerary.getCode());
		psUpdate.executeUpdate();
		for(Itinerary itinerary : userItinerary.getItineraries()){
		    psInsert.setString(1, company.getCode());
		    psInsert.setString(2, itinerary.getCode());
		    psInsert.setString(3, itinerary.getClientAddress().getClient().getCode());
		    psInsert.setString(4, itinerary.getClientAddress().getCode());
		    psInsert.setShort(5, itinerary.getDay());
		    psInsert.setString(6, itinerary.getFrequency());
		    psInsert.setDate(7, new java.sql.Date(itinerary.getDate().getTime()));
		    psInsert.executeUpdate();
		    psInsert.clearParameters();
		}
		psUpdate.clearParameters();
		ps.clearParameters();
	    }
	    cnx.commit();
	} catch (Exception e) {
	    try {
		cnx.rollback();
	    } catch (Exception e2) {
	    }
	    e.printStackTrace();
	}finally {
		try {
		    if(ps!=null)ps.close();
		    if(cnx!=null)cnx.close();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    
	}
	
    }

    
    
}
