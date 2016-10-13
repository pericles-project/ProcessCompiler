package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.pericles.processcompiler.ermr.SPARQLQuery;
import eu.pericles.processcompiler.exceptions.ERMRClientException;

public class SPARQLQueriesTests {

	@Test
	public void encodeQuery() {
		String decodedQuery = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"6d4c7d23-d1cc-4ebd-915e-4b465b494ab3\" . }";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";
		try {
			String encodedQuery = SPARQLQuery.encode(decodedQuery);
			assertEquals(expectedQuery, encodedQuery);
		} catch (ERMRClientException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void createQueryGetImplementationEntity() {
		String uri = "<http://www.pericles-project.eu/ns/ecosystem#impVirusCheck>";
		String expectedQuery = "PREFIX+ecosystem%3A%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23%3E++SELECT+DISTINCT+%3Fversion+%3Ftype+%3Flocation+%3Fchecksum+WHERE+%7B+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23impVirusCheck%3E+ecosystem%3Aversion+%3Fversion+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23impVirusCheck%3E+ecosystem%3Atype+%3Ftype+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23impVirusCheck%3E+ecosystem%3Alocation+%3Flocation+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23impVirusCheck%3E+ecosystem%3Achecksum+%3Fchecksum+.++%7D";
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
		String expectedQuery = "PREFIX+ecosystem%3A%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23%3E+SELECT+DISTINCT+%3Fname+%3Fdescription+%3Ftype+%3Foptional+WHERE+%7B%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23isIngestAWSWAW%3E+ecosystem%3Aname+%3Fname+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23isIngestAWSWAW%3E+ecosystem%3Adescription+%3Fdescription+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23isIngestAWSWAW%3E+ecosystem%3Atype+%3Ftype+.+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23isIngestAWSWAW%3E+ecosystem%3AisOptional+%3Foptional+.++%7D";
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
		String expectedQuery = "PREFIX+ecosystem%3A%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23%3E++SELECT+DISTINCT+%3Finputs+WHERE+%7B+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fns%2Fecosystem%23agpIngestAWSW%3E+ecosystem%3AhasInputSlot+%3Finputs++.++%7D";
		try {
			String query = SPARQLQuery.createQueryGetInputSlotURIList(uri);
			assertEquals(expectedQuery, query);
		} catch (ERMRClientException e) {
			fail(e.getMessage());
		}
	}
}
