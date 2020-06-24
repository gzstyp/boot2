package com.fwtai.config;

import com.fwtai.tool.ToolProperties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;

public final class InitListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent event){
	    final ServletContext context = event.getServletContext();
        final int code_open = ToolProperties.getInstance("config/config.properties").getInteger("code_open","0");
        context.setAttribute("code_open",code_open);
		context.setAttribute("platformName","服务台");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event){
		final ServletContext context = event.getServletContext();
		final Enumeration<String> e = context.getAttributeNames();
		while(e.hasMoreElements()){
			final String key = e.nextElement();
            context.removeAttribute(key);
		}
	}
}