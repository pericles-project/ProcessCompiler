package eu.pericles.processcompiler.unittests;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.pericles.processcompiler.unittests.ermr.CreateCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.CreateRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.DeleteRepositoryTest;
import eu.pericles.processcompiler.unittests.ermr.GetCollectionTest;
import eu.pericles.processcompiler.unittests.ermr.GetRepositoryTest;

@RunWith(Suite.class)
@SuiteClasses({ CreateCollectionTest.class, GetCollectionTest.class, DeleteCollectionTest.class, CreateRepositoryTest.class,
		GetRepositoryTest.class, DeleteRepositoryTest.class })
public class ERMRRequestsTests {
	static String collection = "NoaCollectionTest";
	static String repository = "NoaRepositoryTest";

	@BeforeClass
	static public void init() {
		CreateCollectionTest.setCollection(collection);
		GetCollectionTest.setCollection(collection);
		DeleteCollectionTest.setCollection(collection);
		CreateRepositoryTest.setRepository(repository);
		GetRepositoryTest.setRepository(repository);
		DeleteRepositoryTest.setRepository(repository);
	}
}
