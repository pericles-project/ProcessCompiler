package eu.pericles.processcompiler.unittests;

import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.pericles.processcompiler.unittests.ermr.AddTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.CreateDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.FindTest;
import eu.pericles.processcompiler.unittests.ermr.GetCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.GetDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.GetRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.GetTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.QueryRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.UpdateDigitalObjectTest;

@RunWith(Suite.class)
@SuiteClasses({ GetCollectionTest.class, CreateDigitalObjectTest.class, GetDigitalObjectTest.class, FindTest.class,
		UpdateDigitalObjectTest.class, DeleteDigitalObjectTest.class, GetRepositoryTest.class, AddTriplesTest.class, GetTriplesTest.class,
		QueryRepositoryTest.class, DeleteTriplesTest.class })
/*
 * @SuiteClasses({ CreateCollectionTest.class, GetCollectionTest.class,
 * CreateDigitalObjectTest.class, GetDigitalObjectTest.class,
 * UpdateDigitalObjectTest.class, DeleteDigitalObjectTest.class,
 * DeleteCollectionTest.class, CreateRepositoryTest.class,
 * GetRepositoryTest.class, AddTriplesTest.class, GetTriplesTest.class,
 * DeleteTriplesTest.class, DeleteRepositoryTest.class })
 */
public class ERMRRequestsTests {
	static String collection = "NoaCollectionTest";
	static String repository = "NoaRepositoryTest";
	static String digitalObjectPath = collection + "/" + "Input.bpmn2";
	static String digitalObject = "src/test/resources/helloworld/Input.bpmn2";
	static String triples = "src/test/resources/helloworld/Ecosystem.txt";
	static String expectedTriples = "src/test/resources/helloworld/Triples.txt";
	static String expectedFindResult = "src/test/resources/helloworld/ExpectedFindResult.txt";
	static String expectedQueryResult = "src/test/resources/helloworld/ExpectedQueryResult.txt";
	static String findTerm = "bpmn2";
	static String query = "select%20?s%20?p%20?o%20%7B?s%20?p%20?o%7D";
	static String digitalObjectMediaType = MediaType.APPLICATION_XML;
	static String triplesMediaType = MediaType.TEXT_PLAIN;

	@BeforeClass
	static public void init() {
		GetCollectionTest.setVariables(collection);
		GetRepositoryTest.setVariables(repository);
		CreateDigitalObjectTest.setVariables(digitalObjectPath, digitalObject, digitalObjectMediaType);
		GetDigitalObjectTest.setVariables(digitalObjectPath, digitalObject);
		FindTest.setVaribles(findTerm, expectedFindResult);
		UpdateDigitalObjectTest.setVariables(digitalObjectPath, digitalObject, digitalObjectMediaType);
		DeleteDigitalObjectTest.setVariables(digitalObjectPath);
		AddTriplesTest.setVariables(repository, triples, triplesMediaType);
		QueryRepositoryTest.setVariables(repository, query, expectedQueryResult);
		GetTriplesTest.setVariables(repository, expectedTriples);
		DeleteTriplesTest.setVariables(repository);
		/*
		 * CreateCollectionTest.setVariables(collection);
		 * DeleteCollectionTest.setVariables(collection);
		 * CreateRepositoryTest.setVariables(repository);
		 * DeleteRepositoryTest.setVariables(repository);
		 */
	}
}
