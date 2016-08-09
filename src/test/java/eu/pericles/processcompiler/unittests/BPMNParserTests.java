package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.exceptions.BPMNParserException;

public class BPMNParserTests {
	private static String testPath = "src/test/resources/ingest_sba/";

	@Test
	public void validFileTest() {
		try {
			BPMNProcess bpmnProcess = new BPMNParser().parse(testPath + "EncapsulateDOMD.bpmn");
			assertEquals("atpEncapsulateDOMD", bpmnProcess.getId());
			assertEquals("http://www.jboss.org/drools", bpmnProcess.getTargetNamespace());
			assertEquals("itemDefDO", bpmnProcess.getItemDefinitions().get(0).getId());
			assertEquals("DigitalObject", bpmnProcess.getItemDefinitions().get(0).getStructureRef().getLocalPart());
			assertEquals("itemDefMD", bpmnProcess.getItemDefinitions().get(1).getId());
			assertEquals("Metadata", bpmnProcess.getItemDefinitions().get(1).getStructureRef().getLocalPart());
			assertEquals("itemDefPF", bpmnProcess.getItemDefinitions().get(2).getId());
			assertEquals("PackageFormat", bpmnProcess.getItemDefinitions().get(2).getStructureRef().getLocalPart());
			assertEquals("itemDefP", bpmnProcess.getItemDefinitions().get(3).getId());
			assertEquals("Package", bpmnProcess.getItemDefinitions().get(3).getStructureRef().getLocalPart());
			assertEquals("atpEncapsulateDOMD", bpmnProcess.getProcess().getId());
			assertEquals("isEncapsulateDOMDDO", bpmnProcess.getDataObjects().get(0).getId());
			assertEquals("itemDefDO", bpmnProcess.getDataObjects().get(0).getItemSubjectRef().getLocalPart());
			assertEquals("isEncapsulateDOMDMD", bpmnProcess.getDataObjects().get(1).getId());
			assertEquals("itemDefMD", bpmnProcess.getDataObjects().get(1).getItemSubjectRef().getLocalPart());
			assertEquals("isEncapsulateDOMDPF", bpmnProcess.getDataObjects().get(2).getId());
			assertEquals("itemDefPF", bpmnProcess.getDataObjects().get(2).getItemSubjectRef().getLocalPart());
			assertEquals("osEncapsulateDOMDP", bpmnProcess.getDataObjects().get(3).getId());
			assertEquals("itemDefP", bpmnProcess.getDataObjects().get(3).getItemSubjectRef().getLocalPart());
		} catch (BPMNParserException e) {
			fail("Error in validFileTest()");
		}
	}

	@Test
	public void missingFileTest() {
		try {
			new BPMNParser().parse(testPath + "XXXXX.bpmn");
			fail("Error in missingFileTest()");
		} catch (BPMNParserException e) {
			assertEquals("Error when parsing the BPMN file src/test/resources/ingest_sba/XXXXX.bpmn: src/test/resources/ingest_sba/XXXXX.bpmn (No such file or directory)", e.getMessage());
		}
	}

	@Test
	public void invalidFileTest() {
		try {
			new BPMNParser().parse(testPath + "Ecosystem.ttl");
			fail("Error in invalidFileTest()");
		} catch (BPMNParserException e) {
			assertEquals("Error when parsing the BPMN file src/test/resources/ingest_sba/Ecosystem.ttl: Unmarshalling error", e.getMessage());
		}
	}

}
