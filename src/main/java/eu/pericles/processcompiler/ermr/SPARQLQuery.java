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
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ " SELECT DISTINCT ?name ?description ?version WHERE {"
				+ process + " dem:name ?name . "
				+ process + " dem:description ?description . "
				+ process + " dem:version ?version . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationEntity(String implementation) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
			   + " SELECT DISTINCT ?version ?type ?location ?checksum WHERE { "
			   + implementation + " dem:version ?version . "
			   + implementation + " dem:implementationType ?type . "
			   + implementation + " dem:location ?location . "
			   + implementation + " dem:checksum ?checksum . "
			   + " }";
		return encode(query);
	}

	public static String createQueryGetInputSlotEntity(String inputSlot) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?name ?description ?type ?optional WHERE {"
				+ inputSlot + " dem:name ?name . "
				+ inputSlot + " dem:description ?description . "
				+ inputSlot + " dem:dataType ?type . "
				+ inputSlot + " dem:isOptional ?optional . "
				+ " }";
		return encode(query);
	}
	
	public static String createQueryGetOutputSlotEntity(String outputSlot) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?name ?description ?type WHERE {"
				+ outputSlot + " dem:name ?name . "
				+ outputSlot + " dem:description ?description . "
				+ outputSlot + " dem:dataType ?type . "
				+ " }";
		return encode(query);
	}
	
	public static String createQueryGetSequenceEntity(String sequence) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?processFlow ?dataFlow WHERE {"
				+ sequence + " dem:processFlow ?processFlow . "
				+ sequence + " dem:dataFlow ?dataFlow . "
				+ " }";
	return encode(query);
	}
	
	public static String createQueryGetDataFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "dem:dataFlow");
	}

	public static String createQueryGetProcessFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "dem:processFlow");
	}
	
	//--------------------------- GET URIs --------------------------------//

	public static String createQueryGetInputSlotURIList(String process) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> "
				+ " SELECT DISTINCT ?inputs WHERE { "
				+ process + " dem:hasInputSlot ?inputs  . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetOutputSlotURIList(String process) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> "
				+ " SELECT DISTINCT ?outputs WHERE { "
				+ process + " dem:hasOutputSlot ?outputs  . "
				+ " }";
		return encode(query);
	}

	public static String createQueryGetImplementationURI(String process) throws ERMRClientException {
		return createQueryGetURI(process, "dem:hasImplementation");
	}
	
	public static String createQueryGetEntityTypeURI(String entity) throws ERMRClientException {
		return createQueryGetURI(entity, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
	}

	public static String createQueryGetSlotDataTypeURI(String entity) throws ERMRClientException {
		return createQueryGetURI(entity, "dem:dataType");
	}

	public static String createQueryGetParentEntityURI(String childEntity) throws ERMRClientException {
		return createQueryGetURI(childEntity, "<http://www.w3.org/2000/01/rdf-schema#subClassOf>");
	}
	
	public static String createQueryGetURI(String subject, String predicate) throws ERMRClientException {
		String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?uri WHERE {"
				+ subject + " " + predicate + " ?uri . "
				+ " }";
		return encode(query);
	}
}

/**
 * QUERY TEMPLATE
 *  String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT WHERE {"
				+ " }";
	return encode(query);
*/
