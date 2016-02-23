package movistar.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import movistar.bean.Admin;
import movistar.bean.Sale;
import movistar.dbacces.SaleAccess;
import movistar.util.ReportExport;
import movistar.util.ReportExport.ExportType;

/**
 * Servlet implementation class ReportControl
 */
public class ReportControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String type = request.getParameter("type");
		if("TRACK".equals(type)){
		    track(request,response);
		}else if("SALE".equals(type)){
		    sale(request,response);
		}
	}
	private void track(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	    String userCode = request.getParameter("cboUser");
	    String date = request.getParameter("txtDate");
	    Admin admin = (Admin)request.getSession().getAttribute("admin");
	    List<Sale> sales = SaleAccess.getSales(admin.getCompany().getCode(),userCode,date);
	    
	    request.setAttribute("sales", sales);
	    getServletContext().getRequestDispatcher("/admin/track.jsp").forward(request, response);
	}
	private void sale(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	    String warehouseCode = request.getParameter("cboWarehouse");
	    String userCode = request.getParameter("cboUser");
	    String startDate = request.getParameter("startDate");
	    String endDate = request.getParameter("endDate");
	    String export_type=request.getParameter("export_type");
	    
	    Admin admin = (Admin)request.getSession().getAttribute("admin");
	    
	    List<Map> report = SaleAccess.saleReport(admin.getCompany().getCode(),startDate, endDate,warehouseCode, userCode);
	    try {
		if(!"".equals(export_type)){
		    String[] columNames = new String[]{"warehouseCode","warehouseName","userCode","userName",
			    "totalSales","totalAmount"};
		    if("PDF".equals(export_type)){
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=export.pdf");
			ReportExport.export(ExportType.PDF, response.getOutputStream(), columNames , report);
		    }else if("EXCEL".equals(export_type)){
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=export.xls");
			ReportExport.export(ExportType.EXCEL, response.getOutputStream(), columNames , report);
		    }
		    return;
		}
	    }catch (Exception e){
		e.printStackTrace();
	    }
	    
	    
	    request.setAttribute("report", report);
	    request.setAttribute("warehouseCode", warehouseCode);
	    request.setAttribute("startDate", startDate);
	    request.setAttribute("endDate", endDate);
	    request.setAttribute("userCode", userCode);
	    getServletContext().getRequestDispatcher("/admin/reporteVenta.jsp").forward(request, response);
	}

}
