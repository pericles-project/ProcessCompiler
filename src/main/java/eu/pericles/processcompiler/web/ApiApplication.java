package eu.pericles.processcompiler.web;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class ApiApplication extends ResourceConfig {

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
		packages(getClass().getPackage().getName());
		register(JacksonJsonProvider.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(defaultConfig).to(ERMRConfig.class);
			}
		});

		for (Class<?> cls : getClasses())
			System.out.println(cls.toString());	
		for (Object obj : getInstances())
			System.out.println(obj.toString());
	}

}
