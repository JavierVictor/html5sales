package movistar.listener;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class AppFilter
 */
public class AppFilter implements Filter {


    public AppFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest req = (HttpServletRequest) request;
	    if(!"/sales/admin/".equals(req.getRequestURI())&&
		    !"/sales/admin".equals(req.getRequestURI())&&
		    !"/sales/admin/index.jsp".equals(req.getRequestURI())&&
		    !"/sales/admin/adminUser".equals(req.getRequestURI()) && 
		    req.getSession().getAttribute("admin")==null){
		((HttpServletResponse)response).sendRedirect(req.getContextPath()+"/admin");
		return;
	    }
	    chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
