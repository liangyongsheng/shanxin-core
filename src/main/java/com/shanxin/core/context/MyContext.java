package com.shanxin.core.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

public class MyContext implements Filter, ServletContextAware, ApplicationContextAware {
	private static ServletRequest servletRequest;
	private static ServletResponse servletResponse;
	private static ServletContext servletContext;
	private static ApplicationContext applicationContext;
	private static Map<String, ApplicationContext> applicationContextForClassPathXmls = new HashMap<String, ApplicationContext>();

	// Filter
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	// Filter
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		MyContext.servletRequest = arg0;
		MyContext.servletResponse = arg1;
		arg2.doFilter(arg0, arg1);
	}

	// Filter
	@Override
	public void destroy() {
	}

	// ServletContextAware
	@Override
	public void setServletContext(ServletContext arg0) {
		MyContext.servletContext = arg0;
	}

	// ApplicationContextAware
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		MyContext.applicationContext = arg0;
	}

	// here...
	public static ServletContext getServletContext() {
		ServletContext servletContext = null;

		// way 1: use ServletContextAware, but you should configure it in beans like "<bean id="MyContext" class="*.MyContext" />"
		if (MyContext.servletContext != null)
			servletContext = MyContext.servletContext;
		if (servletContext != null)
			return servletContext;

		// way 2: use org.springframework.web.context.request.RequestContextListener, but you should configure it in web.xml
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			servletContext = attr.getRequest().getServletContext();
		} catch (Throwable ex) {
		}
		if (servletContext != null)
			return servletContext;

		return servletContext;
	}

	public static ApplicationContext getRootApplicationContext() {
		ApplicationContext applicationContext = null;
		// way 1: use ApplicationContextAware, but you should configure it in beans like "<bean id="MyContext" class="*.MyContext" />"
		applicationContext = MyContext.applicationContext;
		if (applicationContext != null)
			return applicationContext;

		// way 2: use ServletContextAware, but you should configure it in beans like "<bean id="MyContext" class="*.MyContext" />"
		if (MyContext.servletContext != null)
			applicationContext = (ApplicationContext) MyContext.servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		if (applicationContext != null)
			return applicationContext;

		// way 3 : use org.springframework.web.context.request.RequestContextListener, but you should configure it in web.xml
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			applicationContext = (ApplicationContext) (attr.getRequest().getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
		} catch (Throwable ex) {
		}
		if (applicationContext != null)
			return applicationContext;

		return applicationContext;
	}

	public static List<ApplicationContext> getApplicationContexts() {
		List<ApplicationContext> rs = new ArrayList<ApplicationContext>();
		ServletContext servletContext = getServletContext();
		if (servletContext != null) {
			Enumeration<String> es = servletContext.getAttributeNames();
			while (es.hasMoreElements()) {
				try {
					String name = es.nextElement();
					if (name != null) {
						Object obj = servletContext.getAttribute(name);
						if (obj instanceof ApplicationContext)
							rs.add((ApplicationContext) obj);
					}
				} catch (Throwable ex) {
				}
			}
		}
		return rs;
	}

	public static ApplicationContext getClassPathXmlApplicationContext(String classPathFileName) {
		ApplicationContext applicationContext = null;

		// way 4 : use ClassPathXmlApplicationContext
		if (classPathFileName != null && !classPathFileName.equals("")) {
			if (applicationContextForClassPathXmls.get(classPathFileName) != null)
				applicationContext = applicationContextForClassPathXmls.get(classPathFileName);
			else {
				applicationContext = new ClassPathXmlApplicationContext("classpath:" + classPathFileName);
				applicationContextForClassPathXmls.put(classPathFileName, applicationContext);
			}
		}
		if (applicationContext != null)
			return applicationContext;

		return applicationContext;
	}

	/**
	 * different client has different HttpServletRequest
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest httpServletRequest = null;

		// way 1: use this as Filter,you should configure it in web.xml
		if (MyContext.servletRequest != null) {
			try {
				httpServletRequest = (HttpServletRequest) MyContext.servletRequest;
			} catch (Throwable ex) {
			}
		}
		if (httpServletRequest != null)
			return httpServletRequest;

		// way 2: use org.springframework.web.context.request.RequestContextListener, but you should configure it in web.xml
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			httpServletRequest = attr.getRequest();
		} catch (Throwable ex) {
		}
		if (httpServletRequest != null)
			return httpServletRequest;

		return httpServletRequest;
	}

	/**
	 * BUG it is always NULL, the result always be MockHttpServletResponse
	 * 
	 * @return
	 */
	public static HttpServletResponse getHttpServletResponse() {
		HttpServletResponse httpServletResponse = null;

		// way 1:
		if (MyContext.servletResponse != null) {
			try {
				httpServletResponse = (HttpServletResponse) MyContext.servletResponse;
			} catch (Throwable ex) {
			}
		}
		if (httpServletResponse != null)
			return httpServletResponse;

		// way 2:
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			ServletWebRequest servletWebRequest = new ServletWebRequest(attr.getRequest());
			httpServletResponse = servletWebRequest.getResponse();
		} catch (Throwable ex) {
		}
		if (httpServletResponse != null)
			return httpServletResponse;

		return httpServletResponse;
	}

	/**
	 * different client has different HttpSession
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getHttpSession() {
		HttpSession httpSession = null;

		// way 1: use org.springframework.web.context.request.RequestContextListener, but you should configure it in web.xml
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			httpSession = attr.getRequest().getSession();
		} catch (Throwable ex) {
		}
		return httpSession;
	}
}
