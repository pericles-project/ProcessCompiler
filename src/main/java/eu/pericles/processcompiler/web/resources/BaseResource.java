package eu.pericles.processcompiler.web.resources;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;
import eu.pericles.processcompiler.web.ApiException;

public class BaseResource {
	static ObjectMapper om = new ObjectMapper();

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
