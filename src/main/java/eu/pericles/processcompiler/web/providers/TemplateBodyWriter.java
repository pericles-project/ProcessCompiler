package eu.pericles.processcompiler.web.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Provider
@Produces("text/html")
public class TemplateBodyWriter implements MessageBodyWriter<Template> {

	private Configuration cfg;

	@Inject
	public TemplateBodyWriter(Configuration cfg) {
		this.cfg = cfg;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Template t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Template t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try {
			cfg.getTemplate(t.getTemplate()).process(t.getNamespace(), new OutputStreamWriter(entityStream));
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
