package movistar.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Company;
import movistar.bean.User;
import movistar.bean.Warehouse;

public class ItineraryControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

        public ItineraryControl() {
            super();
        }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String type = request.getParameter("type");
	}

	private void findItinerary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		
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
