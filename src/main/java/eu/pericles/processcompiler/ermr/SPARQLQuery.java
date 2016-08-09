package eu.pericles.processcompiler.ermr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class SPARQLQuery {

	public static String encode(String query) throws ERMRClientException {
		try {
			return URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ERMRClientException("Error creating SPARQL query: " + e.getMessage());
		}
	}
	
	//--------------------------- GET ENTITIES --------------------------------//
	
	public static String createQueryGetProcessAttributes(String process) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ " SELECT DISTINCT ?name ?description ?version WHERE {"
				+ process + " ecosystem:name ?name . "
				+ process + " ecosystem:description ?description . "
				+ process + " ecosystem:version ?version . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationEntity(String implementation) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
			   + " SELECT DISTINCT ?version ?type ?location ?checksum ?algorithm WHERE { "
			   + implementation + " ecosystem:version ?version . "
			   + implementation + " ecosystem:type ?type . "
			   + implementation + " ecosystem:location ?location . "
			   + implementation + " ecosystem:hasFixity ?fixity . "
			   + "?fixity ecosystem:checksum ?checksum . "
			   + "?fixity ecosystem:algorithm ?algorithm . "
			   + " }";
		return encode(query);
	}

	public static String createQueryGetInputSlotEntity(String inputSlot) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?name ?description ?type ?optional WHERE {"
				+ inputSlot + " ecosystem:name ?name . "
				+ inputSlot + " ecosystem:description ?description . "
				+ inputSlot + " ecosystem:type ?type . "
				+ inputSlot + " ecosystem:isOptional ?optional . "
				+ " }";
		return encode(query);
	}
	
	public static String createQueryGetOutputSlotEntity(String outputSlot) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?name ?description ?type WHERE {"
				+ outputSlot + " ecosystem:name ?name . "
				+ outputSlot + " ecosystem:description ?description . "
				+ outputSlot + " ecosystem:type ?type . "
				+ " }";
		return encode(query);
	}
	
	public static String createQueryGetSequenceEntity(String sequence) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?processFlow ?dataFlow WHERE {"
				+ sequence + " ecosystem:processFlow ?processFlow . "
				+ sequence + " ecosystem:dataFlow ?dataFlow . "
				+ " }";
	return encode(query);
	}
	
	public static String createQueryGetDataFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "ecosystem:dataFlow");
	}

	public static String createQueryGetProcessFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "ecosystem:processFlow");
	}
	
	//--------------------------- GET URIs --------------------------------//

	public static String createQueryGetInputSlotURIList(String process) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> "
				+ " SELECT DISTINCT ?inputs WHERE { "
				+ process + " ecosystem:hasInputSlot ?inputs  . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetOutputSlotURIList(String process) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> "
				+ " SELECT DISTINCT ?outputs WHERE { "
				+ process + " ecosystem:hasOutputSlot ?outputs  . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationURI(String process) throws ERMRClientException {
		return createQueryGetURI(process, "ecosystem:hasImplementation");
	}
	
	public static String createQueryGetSequenceURI(String process) throws ERMRClientException {
		return createQueryGetURI(process, "ecosystem:hasSequence");
	}

	public static String createQueryGetEntityTypeURI(String entity) throws ERMRClientException {
		return createQueryGetURI(entity, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
	}

	public static String createQueryGetDataTypeURI(String entity) throws ERMRClientException {
		return createQueryGetURI(entity, "ecosystem:type");
	}

	public static String createQueryGetParentEntityURI(String childEntity) throws ERMRClientException {
		return createQueryGetURI(childEntity, "<http://www.w3.org/2000/01/rdf-schema#subClassOf>");
	}
	
	public static String createQueryGetURI(String subject, String predicate) throws ERMRClientException {
		String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT ?uri WHERE {"
				+ subject + " " + predicate + " ?uri . "
				+ " }";
		return encode(query);
	}
}

/**
 * QUERY TEMPLATE
 *  String query = "PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> " 
				+ "SELECT DISTINCT WHERE {"
				+ " }";
	return encode(query);
*/
