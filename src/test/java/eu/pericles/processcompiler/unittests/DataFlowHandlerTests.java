package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.spec.bpmn._20100524.model.TDataObject;

import eu.pericles.processcompiler.communications.ermr.ERMRClientAPI;
import eu.pericles.processcompiler.core.DataConnectionsHandler;
import eu.pericles.processcompiler.core.ProcessAggregator.SequenceSubprocess;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;

public class DataFlowHandlerTests {
	static String repository = "NoaRepositoryTest";
	static String triplesMediaType = MediaType.TEXT_PLAIN;
	private String ecosystem = "src/test/resources/core/processaggregationwithdata/Ecosystem.txt";

	@Before
	public void setRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.addTriples(repository, ecosystem, triplesMediaType);
			assertEquals(201, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("setRepository(): " + e.getMessage());
		}
	}

	@After
	public void deleteRepository() {
		try {
			ERMRClientAPI client = new ERMRClientAPI();
			Response response = client.deleteTriples(repository);
			// TODO Error in the ERMR design: this should be 204 NO CONTENT
			assertEquals(200, response.getStatus());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			fail("deleteRepository(): " + e.getMessage());
		}
	}

	@Test
	public void getIndividualInputConnectionsTest() {
		try {
			ProcessCompiler compiler = new ProcessCompiler();
			AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository,
					"<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
			List<SequenceSubprocess> sequenceSubprocesses = compiler.validateAndGetSequenceSubprocess(repository, aggregatedProcess);
			HashMap<DataFlowNode, Object> connections = DataConnectionsHandler.getIndividualInputConnections(sequenceSubprocesses);

			String expectedConnections = "2 <http://www.pericles-project.eu/ns/ecosystem#isExtractMDDM> _9e4241fb-f1ab-4307-b3c5-26089f7ab0db 1 <http://www.pericles-project.eu/ns/ecosystem#isVirusCheckDM> _3f7edde4-ea01-4c1d-9280-c48c0794733b 3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD> _bd0a0504-ea17-4d26-9282-3e4db3eeb333 3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF> _3545d1fa-de3d-4ac2-943f-f5eb0f90b695 3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO> _9bfa891c-1c1d-4ea2-9046-ea6685af96d2 ";
			String actualConnections = connectionsToString(connections);
			assertEquals(expectedConnections, actualConnections);
		} catch (Exception e) {
			fail("getIndividualInputConnectionsTest(): " + e.getMessage());
		}
	}

	@Test
	public void getIndividualOutputConnectionsTest() {
		try {
			ProcessCompiler compiler = new ProcessCompiler();
			AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository,
					"<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
			List<SequenceSubprocess> sequenceSubprocesses = compiler.validateAndGetSequenceSubprocess(repository, aggregatedProcess);
			HashMap<DataFlowNode, Object> connections = DataConnectionsHandler.getIndividualOutputConnections(sequenceSubprocesses);

			String expectedConnections = "3 <http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP> _4667f420-f6d9-4017-bf01-f2a6f4f32730 2 <http://www.pericles-project.eu/ns/ecosystem#osExtractMDMD> _549a617e-89e3-4e1f-a291-978ec1c98c20 ";
			String actualConnections = connectionsToString(connections);
			assertEquals(expectedConnections, actualConnections);
		} catch (Exception e) {
			fail("getIndividualOutputConnectionsTest(): " + e.getMessage());
		}
	}

	@Test
	public void getAggregatedConnectionsTest() {
		try {
			ProcessCompiler compiler = new ProcessCompiler();
			AggregatedProcess aggregatedProcess = compiler.getAggregatedProcess(repository,
					"<http://www.pericles-project.eu/ns/ecosystem#agpIngestAWSW>");
			List<SequenceSubprocess> sequenceSubprocesses = compiler.validateAndGetSequenceSubprocess(repository, aggregatedProcess);
			HashMap<DataFlowNode, Object> connections = DataConnectionsHandler.getAggregatedConnections(aggregatedProcess.getSequence()
					.getDataFlow(), sequenceSubprocesses);

			String expectedConnections = "2 <http://www.pericles-project.eu/ns/ecosystem#isExtractMDDM> _3f7edde4-ea01-4c1d-9280-c48c0794733b 3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD> _549a617e-89e3-4e1f-a291-978ec1c98c20 3 <http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO> _3f7edde4-ea01-4c1d-9280-c48c0794733b ";
			String actualConnections = connectionsToString(connections);
			assertEquals(expectedConnections, actualConnections);
		} catch (Exception e) {
			fail("getAggregatedConnectionsTest(): " + e.getMessage());
		}
	}

	private String connectionsToString(HashMap<DataFlowNode, Object> connections) {
		String connectionsString = "";
		for (Entry<DataFlowNode, Object> connection : connections.entrySet())
			connectionsString = connectionsString + connection.getKey().getSequenceStep() + " " + connection.getKey().getProcessSlot()
					+ " " + ((TDataObject) connection.getValue()).getId() + " ";
		return connectionsString;
	}

}
