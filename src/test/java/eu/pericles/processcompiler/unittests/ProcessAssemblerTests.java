package eu.pericles.processcompiler.unittests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.test.JBPMHelper;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;

import antlr.collections.List;
import eu.pericles.processcompiler.ng.AssembledProcess;
import eu.pericles.processcompiler.ng.DataObject;
import eu.pericles.processcompiler.ng.ProcessAssembler;
import eu.pericles.processcompiler.ng.Subprocess;

public class ProcessAssemblerTests extends JbpmJUnitBaseTestCase {

	private static boolean dataSourceExists = false;
	private PrintStream defaultOutputStream;
	private ByteArrayOutputStream outputStream;
	private KieSession ksession;
	private RuntimeEngine engine;
	private RuntimeManager manager;

	@Before
	public void setRepository() {

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase("kbase");

		manager = createRuntimeManager(kbase);
		engine = manager.getRuntimeEngine(null);
		ksession = engine.getKieSession();

		defaultOutputStream = System.out;
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}

	@After
	public void deleteRepository() {

		manager.disposeRuntimeEngine(engine);
		manager.close();

		System.setOut(defaultOutputStream);
	}

	@Test
	public void testAtomicProcess() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isExtractMDDO", "myDO");

		ProcessInstance processInstance = ksession.startProcess("assembler.atpExtractMD", params);
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Extract Metadata process to: myDO"));
		assertTrue(output.contains("Metadata created: myMD"));
	}

	@Test
	public void testAggregatedProcess() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isIngestDO", "myDO");

		ProcessInstance processInstance = ksession.startProcess("assembler.agpIngest", params);
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Extract Metadata process to: myDO"));
		assertTrue(output.contains("Metadata created: myMD"));
		assertTrue(output.contains("Ingest Input: myDO"));
		assertTrue(output.contains("Ingest Output: myMD"));
	}

	private static RuntimeManager createRuntimeManager(KieBase kbase) {
		JBPMHelper.startH2Server();
		if (!dataSourceExists) {
			JBPMHelper.setupDataSource();
			dataSourceExists = true;
		}
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf)
				.knowledgeBase(kbase);
		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(builder.get(), "com.sample:example:1.0");
	}

	@Test
	public void testAssembledProcessIsRunnable() {
		ArrayList<DataObject> dataObjects = new ArrayList<DataObject>();
		dataObjects.add(new DataObject("isIngestDO", "Digital Object", "java.lang.String"));
		dataObjects.add(new DataObject("osIngestMD", "Metadata", "java.lang.String"));
		
		HashMap<DataObject, DataObject> dataInputMap = new HashMap<DataObject, DataObject>();
		dataInputMap.put(new DataObject("isIngestDO", "Digital Object", "java.lang.String"), new DataObject("isExtractMDDO", "Digital Object", "java.lang.String") );
		HashMap<DataObject, DataObject> dataOutputMap = new HashMap<DataObject, DataObject>();
		dataOutputMap.put(new DataObject("osIngestMD", "Metadata", "java.lang.String"), new DataObject("osExtractMDMD", "Metadata", "java.lang.String") );
		ArrayList<Subprocess> subprocesses = new ArrayList<Subprocess>();
		subprocesses.add(new Subprocess("assembler.atpExtractMD", dataInputMap, dataOutputMap));
		
		AssembledProcess assembledProcess = new AssembledProcess();
		assembledProcess.setId("assembler.aggregatedProcess");
		assembledProcess.setName("Aggregated Process");
		assembledProcess.setType("Private");
		assembledProcess.setDataObjects(dataObjects);
		assembledProcess.setSubprocesses(subprocesses);
		
		try {
			new ProcessAssembler().assemble(assembledProcess, "src/test/resources/assembler/output.bpmn");
		} catch (Exception e) {
			fail(e.getMessage());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isIngestDO", "myDO");

		ProcessInstance processInstance = ksession.startProcess("assembler.aggregatedProcess", params);
		RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance)processInstance;
		Object variable = rfpi.getVariable("osIngestMD");
		
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertEquals("myMD",variable);

		String output = outputStream.toString();
		defaultOutputStream.println(output);
		assertTrue(output.contains("Executing Extract Metadata process to: myDO"));
		assertTrue(output.contains("Metadata created: myMD"));
	}
}
