package movistar.listener;

import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import movistar.bean.Company;
import movistar.dbacces.ClientAccess;
import movistar.dbacces.CompanyAccess;
import movistar.dbacces.SaleAccess;
import movistar.dbacces.WarehouseAccess;
import movistar.util.LicenseValidator;



public class ApplicationListener implements ServletContextListener {
	ResourceBundle resource = null;
 
	public ApplicationListener() {
        resource = ResourceBundle.getBundle("movistar.util.appConfig");
    }

	
    public void contextInitialized(ServletContextEvent sce) {

    	Enumeration<String> keys = resource.getKeys();
    	String key = null;
    	while(keys.hasMoreElements()){
    		key = keys.nextElement();
    		sce.getServletContext().setAttribute(key, resource.getString(key));
    	}
    	List<Company> companies = CompanyAccess.getCompanies();
    	sce.getServletContext().setAttribute("clientTypes",ClientAccess.getAllClientTypes());
    	sce.getServletContext().setAttribute("saleTypes",SaleAccess.getAllSaleTypes());
    	sce.getServletContext().setAttribute("companies", companies);
    	sce.getServletContext().setAttribute("warehouses", WarehouseAccess.getWarehouses());
    	
    	LicenseValidator.initValidate(companies);
    }

	
    public void contextDestroyed(ServletContextEvent sce) {
        LicenseValidator.stop();
    }
}
