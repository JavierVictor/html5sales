package movistar.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import movistar.bean.Admin;
import movistar.bean.Configuration;
import movistar.dbacces.ConfigurationAccess;
import movistar.util.FileInterface;

public class ConfigurationControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	if (ServletFileUpload.isMultipartContent(request)) {
	    save(request, response);
	}
    }

    protected void save(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	try {
	    DiskFileItemFactory dfif = new DiskFileItemFactory(1,
		    (File) getServletContext().getAttribute(
			    "javax.servlet.context.tempdir"));
	    List<FileItem> items = new ServletFileUpload(dfif)
		    .parseRequest(request);
	    List<Configuration> configurations = new ArrayList<Configuration>();
	    Admin admin = (Admin) request.getSession().getAttribute("admin");

	    for (FileItem item : items) {
		if (item.isFormField()) {
		    Configuration config = new Configuration();
		    config.setCode(item.getFieldName());
		    config.setValue(item.getString());
		    configurations.add(config);
		}else if(item.getSize()!=0){
		    File path = new File(getServletContext().getAttribute("app.filecontext.dir")+ admin.getCompany().getCode() );
		    path.mkdirs();
		    File target = new File(path.getAbsolutePath()+File.separatorChar+item.getName());
		    item.write(target);
		    FileInterface.processFile(admin.getCompany(), target);
		}
	    }
	    
	    if (ConfigurationAccess.save(admin.getCompany().getCode(),
		    configurations)) {
		request.getSession().setAttribute(
			"configurations",
			ConfigurationAccess.getAllConfiguration(admin
				.getCompany().getCode()));
	    }
	    response.sendRedirect(request.getContextPath()
		    + "/admin/config.jsp");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
