package eu.pericles.processcompiler.web.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import eu.pericles.processcompiler.web.ApiException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

	@Override
	public Response toResponse(ApiException e) {
		return Response.status(e.status).entity(e).build();
	}
	
}
