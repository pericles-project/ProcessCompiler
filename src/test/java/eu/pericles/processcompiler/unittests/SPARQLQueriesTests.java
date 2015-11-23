package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import eu.pericles.processcompiler.communications.ermr.SPARQLQuery;

public class SPARQLQueriesTests {

	@Test
	public void encodeQuery() {
		String decodedQuery = "SELECT ?uri WHERE { ?uri <http://www.pericles-project.eu/models#id> \"6d4c7d23-d1cc-4ebd-915e-4b465b494ab3\" . }";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";
		try {
			String encodedQuery = SPARQLQuery.encode(decodedQuery);
			assertEquals(expectedQuery, encodedQuery);
		} catch (UnsupportedEncodingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void createQueryGetEntityByID() {
		String id = "6d4c7d23-d1cc-4ebd-915e-4b465b494ab3";
		String expectedQuery = "SELECT+%3Furi+WHERE+%7B+%3Furi+%3Chttp%3A%2F%2Fwww.pericles-project.eu%2Fmodels%23id%3E+%226d4c7d23-d1cc-4ebd-915e-4b465b494ab3%22+.+%7D";

		try {
			String query = SPARQLQuery.createQueryGetEntityByID(id);
			assertEquals(expectedQuery, query);
		} catch (UnsupportedEncodingException e) {
			fail(e.getMessage());
		}
	}

}
