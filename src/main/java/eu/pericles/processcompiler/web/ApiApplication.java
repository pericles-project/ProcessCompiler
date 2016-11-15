package eu.pericles.processcompiler.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;
import eu.pericles.processcompiler.web.providers.TemplateBodyWriter;
import eu.pericles.processcompiler.web.resources.BaseResource;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;

public class ApiApplication extends ResourceConfig {
	static final Logger log = LoggerFactory.getLogger(ApiApplication.class);

	public static class ERMRConfig {
		public String ermr;
		public String store;
		
		public ERMRConfig() {
		}

		public ERMRConfig(String ermr, String store) {
			this.ermr = ermr;
			this.store = store;
		}

	}

	public ApiApplication(final ERMRConfig defaultConfig) {

		final Configuration tplconfig = new Configuration(Configuration.VERSION_2_3_23);
		tplconfig.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_23).build());
		tplconfig.loadBuiltInEncodingMap();
		tplconfig.setDefaultEncoding("utf-8");
		tplconfig.setURLEscapingCharset("utf-8");
		tplconfig.setClassForTemplateLoading(getClass(), "/templates");

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(defaultConfig).to(ERMRConfig.class);
				bind(tplconfig).to(Configuration.class);
			}
		});

		packages(getClass().getPackage().getName());
		register(JacksonJsonProvider.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

		
		for (Class<?> cls : getClasses())
			log.debug("Loaded class:", cls);
		for (Object obj : getInstances())
			log.debug("Loaded singleton:", obj);
	}

	
	public static void startServer(int port, ERMRConfig ermrc) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        Server jettyServer = new Server(port);
        jettyServer.setHandler(context);

        context.setContextPath("/");
        context.setResourceBase(ApiApplication.class.getClassLoader().getResource("webapp").toExternalForm());
        ServletHolder x = context.addServlet(DefaultServlet.class, "/static/*");
        x.setInitParameter("pathInfoOnly", "true");
        
        ApiApplication rest = new ApiApplication(ermrc);
        ServletContainer jerseyc = new ServletContainer(rest);
        context.addServlet(new ServletHolder(jerseyc),  "/*");
 
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
        	log.error("Could not start server", e);
        } finally {
            jettyServer.destroy();
        }
	}
}
