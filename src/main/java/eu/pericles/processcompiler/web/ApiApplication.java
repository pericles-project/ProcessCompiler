package eu.pericles.processcompiler.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class ApiApplication extends ResourceConfig {
	
	@SuppressWarnings("rawtypes")
	public ApiApplication() {
		packages(getClass().getPackage().getName());
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		
		for(Class cls: getClasses())
			System.out.println(cls.toString());
	}
	

	public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
 
        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);
        
        context.addServlet(new ServletHolder(new ServletContainer(new ApiApplication())),  "/*");
 
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
	}

}
