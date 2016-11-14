package eu.pericles.processcompiler.ermr;

import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class SPARQLQuery {
	
	//--------------------------- GET ENTITIES --------------------------------//
	
	public static String createQueryGetProcessAttributes(String process) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ " SELECT DISTINCT ?name ?description ?version WHERE {"
				+ process + " dem:name ?name . "
				+ process + " dem:description ?description . "
				+ process + " dem:version ?version . "
				+ " }";
	}

	public static String createQueryGetImplementationEntity(String implementation) throws ERMRClientException {
		return"PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
			   + " SELECT DISTINCT ?version ?type ?location ?checksum WHERE { "
			   + implementation + " dem:version ?version . "
			   + implementation + " dem:implementationType ?type . "
			   + implementation + " dem:location ?location . "
			   + implementation + " dem:checksum ?checksum . "
			   + " }";
	}

	public static String createQueryGetInputSlotEntity(String inputSlot) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?name ?description ?type ?optional WHERE {"
				+ inputSlot + " dem:name ?name . "
				+ inputSlot + " dem:description ?description . "
				+ inputSlot + " dem:dataType ?type . "
				+ inputSlot + " dem:isOptional ?optional . "
				+ " }";
	}
	
	public static String createQueryGetOutputSlotEntity(String outputSlot) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?name ?description ?type WHERE {"
				+ outputSlot + " dem:name ?name . "
				+ outputSlot + " dem:description ?description . "
				+ outputSlot + " dem:dataType ?type . "
				+ " }";
	}
	
	public static String createQueryGetSequenceEntity(String sequence) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?processFlow ?dataFlow WHERE {"
				+ sequence + " dem:processFlow ?processFlow . "
				+ sequence + " dem:dataFlow ?dataFlow . "
				+ " }";
	}
	
	public static String createQueryGetDataFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "dem:dataFlow");
	}

	public static String createQueryGetProcessFlow(String process) throws ERMRClientException {
		return createQueryGetURI(process, "dem:processFlow");
	}
	
	//--------------------------- GET URIs --------------------------------//

	public static String createQueryGetInputSlotURIList(String process) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> "
				+ " SELECT DISTINCT ?inputs WHERE { "
				+ process + " dem:hasInputSlot ?inputs  . "
				+ " }";
	}

	public static String createQueryGetOutputSlotURIList(String process) throws ERMRClientException {
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> "
				+ " SELECT DISTINCT ?outputs WHERE { "
				+ process + " dem:hasOutputSlot ?outputs  . "
				+ " }";
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
		return "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT ?uri WHERE {"
				+ subject + " " + predicate + " ?uri . "
				+ " }";
	}
}

/**
 * QUERY TEMPLATE
 *  String query = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> " 
				+ "SELECT DISTINCT WHERE {"
				+ " }";
	return encode(query);
*/
