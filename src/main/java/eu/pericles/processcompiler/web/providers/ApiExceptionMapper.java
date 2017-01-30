package eu.pericles.processcompiler.web.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.pericles.processcompiler.web.ApiException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {
	static final Logger log = LoggerFactory.getLogger(ApiExceptionMapper.class);

	@Override
	public Response toResponse(ApiException e) {
		log.error("Service exception", e.getCause() != null ? e.getCause() : e);
		return Response.status(e.status).entity(e).build();
	}
	
}
