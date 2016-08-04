package eu.pericles.processcompiler.ng.unittests;

import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.pericles.processcompiler.unittests.ermr.AddTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.CreateCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.CreateDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.CreateRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.GetCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.GetDigitalObjectTest;
import eu.pericles.processcompiler.unittests.ermr.GetRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.GetTriplesTest;
import eu.pericles.processcompiler.unittests.ermr.QueryRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.UpdateDigitalObjectTest;

@RunWith(Suite.class)
@SuiteClasses({ CreateCollectionTest.class, 
	CreateDigitalObjectTest.class, GetDigitalObjectTest.class,
	UpdateDigitalObjectTest.class, DeleteDigitalObjectTest.class,
	GetCollectionTest.class, DeleteCollectionTest.class,
	CreateRepositoryTest.class,
	GetRepositoryTest.class, AddTriplesTest.class, GetTriplesTest.class,
	QueryRepositoryTest.class, DeleteTriplesTest.class, 
	DeleteRepositoryTest.class })

public class ERMRClientAPITests {
	static String collection = "NoaCollectionTest_2/";
	static String repository = "NoaRepositoryTest_2";
	static String digitalObjectPath = collection + "HelloWorld.bpmn2";
	static String digitalObject = "src/test/resources/ermr/basicrequests/HelloWorld.bpmn2";
	static String triples = "src/test/resources/ermr/basicrequests/Triples.txt";
	static String expectedTriples = "src/test/resources/ermr/basicrequests/ExpectedTriples.txt";
	static String expectedQueryResult = "src/test/resources/ermr/basicrequests/ExpectedQueryResult.txt";
	static String query = "select%20?s%20?p%20?o%20%7B?s%20?p%20?o%7D";
	static String doType = MediaType.APPLICATION_XML;
	static String textPlain = MediaType.TEXT_PLAIN;

	@BeforeClass
	static public void objectStoreTests() {
		CreateCollectionTest.setVariables(collection);
		CreateDigitalObjectTest.setVariables(digitalObjectPath, digitalObject, doType);
		GetDigitalObjectTest.setVariables(digitalObjectPath, digitalObject);
		UpdateDigitalObjectTest.setVariables(digitalObjectPath, digitalObject, doType);
		GetCollectionTest.setVariables(collection);
		DeleteDigitalObjectTest.setVariables(digitalObjectPath);
		DeleteCollectionTest.setVariables(collection);
	}
	
	@BeforeClass
	static public void tripleStoreTests() {
		CreateRepositoryTest.setVariables(repository);
		AddTriplesTest.setVariables(repository, triples, textPlain);
		GetRepositoryTest.setVariables(repository);
		QueryRepositoryTest.setVariables(repository, query, expectedQueryResult);
		GetTriplesTest.setVariables(repository, expectedTriples);
		DeleteTriplesTest.setVariables(repository);
		DeleteRepositoryTest.setVariables(repository);
	}

}
