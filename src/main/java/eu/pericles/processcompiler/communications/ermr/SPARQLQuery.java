package eu.pericles.processcompiler.communications.ermr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SPARQLQuery {

	public static String encode(String query) throws UnsupportedEncodingException {
		return URLEncoder.encode(query, "UTF-8");
	}
	
	public static String createQueryGetEntityByID(String id) throws UnsupportedEncodingException {
		String query = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"" + id + "\" . }";
		return encode(query);
	}

	public static String createQueryGetImplementationLocation(String id) throws UnsupportedEncodingException {
		String query = "SELECT DISTINCT ?location WHERE { ?process <http://www.pericles-project.eu/models#id> \"" + id + "\" ."
				+ "?process <http://www.pericles-project.eu/models#hasImplementation> ?implementation  ."
				+ "?implementation <http://www.pericles-project.eu/models#location> ?location . }";
		return encode(query);
	}

	public static String createQueryGetImplementationEntity(String id) throws UnsupportedEncodingException {
		String query = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"" + id + "\" . }";
		return encode(query);
	}

}
