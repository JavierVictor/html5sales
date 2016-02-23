package movistar.util;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import movistar.bean.Company;
import movistar.dbacces.CompanyAccess;

public class LicenseValidator {
    private static Verificator verificator = null; 
    public static void initValidate(List<Company> companies){
	verificator = new Verificator(companies);
	verificator.start();
    } 
    public static void stop(){
	if(verificator!=null){
	    verificator.isAlive=false;
	    verificator.interrupt();
	}
    }
   static class Verificator extends Thread{
	private List<Company> companies;
	private boolean isAlive;
	public Verificator(List<Company> companies) {
	    isAlive=true;
	  this.companies=companies;
	}
	public void run() {
	    while(isAlive){
		try {
		    URL urlValidator = new URL("http://localhost:8080/sales/validator");
		    HttpURLConnection cnx = (HttpURLConnection) urlValidator.openConnection();
		    cnx.setRequestMethod("POST");
		    cnx.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		    cnx.setDoInput(true);
		    cnx.setDoOutput(true);
		    
		    OutputStream os = cnx.getOutputStream();
		    
		    String queryString = "type=VALIDATE";
		    for(Company company:companies){
			queryString += "&company=" + company.getCode();
		    }
		    os.write(queryString.getBytes());
		    
		    DataInputStream dis = new DataInputStream(cnx.getInputStream());
		    int integer = dis.readInt();
		    Company company = null;
		    for(int i=0;i<integer;i++){
			company = new Company(dis,false);
			if("OUT".equals(company.getState())){
			    CompanyAccess.turnDownCompany(company);
			}
		    }
		    dis.close();
		    os.close();
		    cnx.disconnect();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		try {
		    sleep(86400000);
		} catch (Exception e) {
		}
	    }
	}	
    }
}