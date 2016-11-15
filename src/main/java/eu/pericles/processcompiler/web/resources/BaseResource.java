package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;
import eu.pericles.processcompiler.web.ApiException;

public class BaseResource {
	static ObjectMapper om = new ObjectMapper();
	static {
		om.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	static final Logger log = LoggerFactory.getLogger(BaseResource.class);

	public static class ConfigBean {
		public ConfigBean() {
		}

		@JsonProperty("process")
		public ProcessBase process;

		@JsonProperty("aggregated_process")
		public AggregatedProcess aggregatedProcess;
	}

	public static class BaseRequestBean {
		public String ermr;
		public String store;
	}

	@Inject
	protected ERMRConfig defaultConfig;
	
	// Resolve a string path against the current working directory.
	protected Path getRelativeFile(String path) {
		Path p = Paths.get(path).normalize();
		if(p.isAbsolute()) {
			p = p.relativize(p.getRoot());
		}
		Path wd = Paths.get(".").toAbsolutePath().normalize(); 
		p = wd.resolve(p).normalize();
		
		if(!p.startsWith(wd)) {
			log.warn("Path {} is not within {}.", path, wd);
			return null;
		}
		
		return p;
	}
	

	protected void assertEntity(BaseRequestBean obj) throws ApiException {
		if (obj == null) {
			throw new ApiException(400, "Missing request entity.");
		}
		if (obj.ermr == null)
			obj.ermr = defaultConfig.ermr;
		if (obj.store == null)
			obj.store = defaultConfig.store;
	}

	protected void assertTrue(boolean test, String msg, Object[]... args) throws ApiException {
		if (!test) {
			for (Object arg : args) {
				msg = StringUtils.replaceOnce(msg, "{}", arg.toString());
			}
			throw new ApiException(400, msg);
		}
	}

	protected ConfigBean parseConfig(File src) throws IOException {
		return om.readValue(src, ConfigBean.class);
	}

}
