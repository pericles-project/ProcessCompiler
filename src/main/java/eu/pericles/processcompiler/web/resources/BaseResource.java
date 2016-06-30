package eu.pericles.processcompiler.web.resources;

import org.apache.commons.lang3.StringUtils;

import eu.pericles.processcompiler.web.ApiException;

public class BaseResource {
	
	protected void assertEntity(Object obj) throws ApiException {
		if(obj == null) {
			throw new ApiException(400, "Missing request entity.");
		}
	}
	
	protected void assertTrue(boolean test, String msg, Object[] ...args) throws ApiException {
		if(!test) {
			for(Object arg: args) {
				msg = StringUtils.replaceOnce(msg, "{}", arg.toString());
			}
			throw new ApiException(400, msg);
		}
	}

}
