package movistar.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Company;

public class Validator extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	System.out.println("GET");

    }

    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	System.out.println("POST");
	String type = request.getParameter("type");
	System.out.println("TYPE ::: " + type);
	if ("VALIDATE".equals(type)) {
	    validate(request, response);
	}
    }

    private void validate(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	try {
	    String[] companies = request.getParameterValues("company");
	    DataOutputStream dos = new DataOutputStream(response.getOutputStream());
	    if(companies!=null&&companies.length>0){
		Company company = new Company();
		company.setCode(companies[0]);
		company.setState("ACT");
		dos.writeInt(1);
		dos.write(company.toByteArray());
	    }else{
		dos.writeInt(0);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
}