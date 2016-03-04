package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.core.ImplementationValidator;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.testutils.CreateEntities;

public class ImplementationValidationTests {

	// ----------------------------- TESTS ----------------------------------

	@Test
	public void validProcessImplementation() {
		try {
			Process process = this.createProcess();
			String file = "src/test/resources/core/implementationvalidation/ValidImplementation.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			boolean validationResult = new ImplementationValidator(process, bpmnProcess).validate().isValid();
			assertTrue(validationResult);
		} catch (Exception e) {
			fail("validProcessImplementation(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputSlotMissing() {
		try {
			Process process = this.createProcess();
			String file = "src/test/resources/core/implementationvalidation/InputSlotMissing.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			boolean validationResult = new ImplementationValidator(process, bpmnProcess).validate().isValid();
			assertFalse(validationResult);
		} catch (Exception e) {
			fail("inputSlotMissing(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputSlotWithDifferentResources() {
		try {
			Process process = this.createProcess();
			String file = "src/test/resources/core/implementationvalidation/InputSlotWithDifferentResources.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			boolean validationResult = new ImplementationValidator(process, bpmnProcess).validate().isValid();
			assertFalse(validationResult);
		} catch (Exception e) {
			fail("inputSlotWithDifferentResources(): " + e.getMessage());
		}
	}
	
	@Test
	public void inputAssociationMissing() {
		try {
			Process process = this.createProcess();
			String file = "src/test/resources/core/implementationvalidation/InputAssociationMissing.bpmn2";
			BPMNProcess bpmnProcess = new BPMNParser().parse(file);
			boolean validationResult = new ImplementationValidator(process, bpmnProcess).validate().isValid();
			assertFalse(validationResult);
		} catch (Exception e) {
			fail("inputAssociationMissing(): " + e.getMessage());
		}
	}

	// ----------------------- HELP FUNCTIONS ----------------------------------

	private Process createProcess() {
		Process process = new Process();
		process.setId("<http://www.pericles-project.eu/ns/ecosystem#atpEncapsulateDOMD>");
		process.setName("Encapsulate Digital Object and its Metadata");
		process.setDescription("Atomic process that encapsulate a digital object and its metadata together in a package of a specific format");
		process.setVersion("1");
		process.setImplementation(CreateEntities.createImplementation(new ArrayList<String>(Arrays.asList(
				"<http://www.pericles-project.eu/ns/ecosystem#impEncapsulateDOMD>", "1", "BPMN",
				"src/test/resources/core/processaggregationwithdata/EncapsulateDOMDProcess.bpmn2", "sha256",
				"ad0dec12cb47c4b3856929f803c6d40d76fffd3cf90681bf9a7bf65d63ca7f80"))));
		process.setInputs(new ArrayList<InputSlot>());
		process.getInputs().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDDO>",
								"Digital Material InputSlot", "Input slot corresponding to the digital object",
								"<http://www.pericles-project.eu/ns/ecosystem#DigitalObject>")), false));
		process.getInputs().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDMD>",
								"Metadata InputSlot", "Input slot corresponding to the metadata",
								"<http://www.pericles-project.eu/ns/ecosystem#Metadata>")), false));
		process.getInputs().add(
				CreateEntities.createInputSlot(
						new ArrayList<String>(Arrays.asList("<http://www.pericles-project.eu/ns/ecosystem#isEncapsulateDOMDPF>",
								"Package Format InputSlot",
								"Input slot corresponding to the package format used to encapsulate the digital object and its metadata",
								"<http://www.pericles-project.eu/ns/ecosystem#PackageFormat>")), false));
		process.setOutputs(new ArrayList<OutputSlot>());
		process.getOutputs().add(
				CreateEntities.createOutputSlot(new ArrayList<String>(Arrays.asList(
						"<http://www.pericles-project.eu/ns/ecosystem#osEncapsulateDOMDP>", "Package OutputSlot",
						"Output slot corresponding to the package resulting of encapsulate a digital object and its metadata",
						"<http://www.pericles-project.eu/ns/ecosystem#Package>"))));
		return process;
	}
}
