package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.SPARQLQuery;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class SPARQLQueriesTests {

	@Test
	public void createQueryGetImplementationEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#impVirusCheck>";
		String expectedQuery = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#>  SELECT DISTINCT ?version ?type ?location ?checksum WHERE { <http://www.pericles-project.eu/ns/ecosystem#impVirusCheck> dem:version ?version . <http://www.pericles-project.eu/ns/ecosystem#impVirusCheck> dem:implementationType ?type . <http://www.pericles-project.eu/ns/ecosystem#impVirusCheck> dem:location ?location . <http://www.pericles-project.eu/ns/ecosystem#impVirusCheck> dem:checksum ?checksum .  }";
		try {
			String query = SPARQLQuery.createQueryGetImplementationEntity(uri);
			assertEquals(expectedQuery, query);
		} catch (ERMRClientException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createQueryGetInputSlotEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW>";
		String expectedQuery = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#> SELECT DISTINCT ?name ?description ?type ?optional WHERE {<http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW> dem:name ?name . <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW> dem:description ?description . <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW> dem:dataType ?type . <http://www.pericles-project.eu/ns/ecosystem#isIngestAWSWAW> dem:isOptional ?optional .  }";
		try {
			String query = SPARQLQuery.createQueryGetInputSlotEntity(uri);
			assertEquals(expectedQuery, query);
		} catch (ERMRClientException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createQueryGetInputSlotURIList() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>";
		String expectedQuery = "PREFIX dem:<http://www.pericles-project.eu/ns/dem#>  SELECT DISTINCT ?inputs WHERE { <http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW> dem:hasInputSlot ?inputs  .  }";
		try {
			String query = SPARQLQuery.createQueryGetInputSlotURIList(uri);
			assertEquals(expectedQuery, query);
		} catch (ERMRClientException e) {
			fail(e.getMessage());
		}
	}
}
