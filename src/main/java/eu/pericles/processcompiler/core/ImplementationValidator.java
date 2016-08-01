package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.omg.spec.bpmn._20100524.model.DataInput;
import org.omg.spec.bpmn._20100524.model.DataInputAssociation;
import org.omg.spec.bpmn._20100524.model.DataOutput;
import org.omg.spec.bpmn._20100524.model.DataOutputAssociation;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;

/**
 * The Implementation Validator validates a BPMN Process with the RDF-based
 * definition of it. This implies to validate the connections between data
 * inputs and outputs with external resources. Therefore, it validates the input
 * and output slots defined in the RDF-based process within the data in the BPMN
 * process.
 * 
 * An input(output) slot is VALID if:
 * - there is at least one data input(output) related to the input(output) slot
 * - all data inputs(outputs) related to the input(output) slot have one and the
 * same external resource associated
 * 
 * The validate() function returns an ImplementationValidationResult containing:
 * - when the validation is not valid: a message with the error/cause of
 * invalidation and the exception that caused it
 * - when the validation is valid: the valid message and the input and output
 * connections, each of them a hashmap where the key is the input(output) slot
 * and the value is the reference to the external resource
 * 
 */
public class ImplementationValidator implements Validator {
	private ProcessBase process;
	private BPMNProcess bpmnProcess;

	public static class ImplementationValidationResult extends ValidationResult {
		private HashMap<InputSlot, Object> inputConnections;
		private HashMap<OutputSlot, Object> outputConnections;

		public HashMap<InputSlot, Object> getInputConnections() {
			return inputConnections;
		}

		public void setInputConnections(HashMap<InputSlot, Object> inputConnections) {
			this.inputConnections = inputConnections;
		}

		public HashMap<OutputSlot, Object> getOutputConnections() {
			return outputConnections;
		}

		public void setOutputConnections(HashMap<OutputSlot, Object> outputConnections) {
			this.outputConnections = outputConnections;
		}
	}

	public ImplementationValidator(ProcessBase process, BPMNProcess bpmnProcess) {
		this.setProcess(process);
		this.setBPMNProcess(bpmnProcess);
	}

	@Override
	public ImplementationValidationResult validate() {
		ImplementationValidationResult result = new ImplementationValidationResult();
		try {
			result.setInputConnections(validateInputSlots());
			result.setOutputConnections(validateOutputSlots());
			result.setMessage(ValidationResult.VALID_MESSAGE);
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			result.setException(e);
		}
		return result;
	}

	private HashMap<InputSlot, Object> validateInputSlots() throws Exception {
		HashMap<InputSlot, Object> inputConnections = new HashMap<InputSlot, Object>();
		for (InputSlot inputSlot : process.getInputs())
			inputConnections.put(inputSlot, validateInputSlot(inputSlot));
		return inputConnections;
	}

	private HashMap<OutputSlot, Object> validateOutputSlots() throws Exception {
		HashMap<OutputSlot, Object> outputConnections = new HashMap<OutputSlot, Object>();
		for (OutputSlot outputSlot : process.getOutputs())
			outputConnections.put(outputSlot, validateOutputSlot(outputSlot));
		return outputConnections;
	}

	private Object validateInputSlot(InputSlot inputSlot) throws Exception {
		try {
			List<DataInput> dataInputs = findDataInputs(inputSlot);
			List<DataInputAssociation> dataInputAssociations = findDataInputAssociations(dataInputs);
			return findAssociatedInputResource(dataInputAssociations);
		} catch (Exception e) {
			throw new Exception("The input slot " + inputSlot.getName() + " is not valid: " + e.getMessage());
		}
	}

	private Object validateOutputSlot(OutputSlot outputSlot) throws Exception {
		try {
			List<DataOutput> dataOutputs = findDataOutputs(outputSlot);
			List<DataOutputAssociation> dataOutputAssociations = findDataOutputAssociations(dataOutputs);
			return findAssociatedOutputResource(dataOutputAssociations);
		} catch (Exception e) {
			throw new Exception("The output slot " + outputSlot.getName() + " is not valid: " + e.getMessage());
		}
	}

	private List<DataInput> findDataInputs(InputSlot inputSlot) throws Exception {
		List<DataInput> dataInputs = getDataInputs(inputSlot);
		if (dataInputs.isEmpty())
			throw new Exception("There is not data inputs associated to the input slot in the implementation file");
		return dataInputs;
	}

	private List<DataOutput> findDataOutputs(OutputSlot outputSlot) throws Exception {
		List<DataOutput> dataOutputs = getDataOutputs(outputSlot);
		if (dataOutputs.isEmpty())
			throw new Exception("There is not data outputs associated to the output slot in the implementation file");
		return dataOutputs;
	}

	private List<DataInputAssociation> findDataInputAssociations(List<DataInput> dataInputs) throws Exception {
		List<DataInputAssociation> dataInputAssociations = new ArrayList<DataInputAssociation>();
		for (DataInput dataInput : dataInputs)
			dataInputAssociations.addAll(findDataInputAssociations(dataInput));
		return dataInputAssociations;
	}

	private List<DataOutputAssociation> findDataOutputAssociations(List<DataOutput> dataOutputs) throws Exception {
		List<DataOutputAssociation> dataOutputAssociations = new ArrayList<DataOutputAssociation>();
		for (DataOutput dataOutput : dataOutputs)
			dataOutputAssociations.addAll(findDataOutputAssociations(dataOutput));
		return dataOutputAssociations;
	}

	private List<DataInputAssociation> findDataInputAssociations(DataInput dataInput) throws Exception {
		List<DataInputAssociation> dataInputAssociations = getDataInputAssociations(dataInput);
		if (dataInputAssociations.isEmpty())
			throw new Exception("The data input: " + dataInput.getName() + " has not associated resource in the implementation file");
		return dataInputAssociations;
	}

	private List<DataOutputAssociation> findDataOutputAssociations(DataOutput dataOutput) throws Exception {
		List<DataOutputAssociation> dataOutputAssociations = getDataOutputAssociations(dataOutput);
		if (dataOutputAssociations.isEmpty())
			throw new Exception("The data output: " + dataOutput.getName() + " has not associated resource in the implementation file");
		return dataOutputAssociations;
	}

	private Object findAssociatedInputResource(List<DataInputAssociation> dataInputAssociations) throws Exception {
		Object associatedResource = findAssociatedInputResource(dataInputAssociations.get(0));
		for (DataInputAssociation dataInputAssociation : dataInputAssociations)
			if (findAssociatedInputResource(dataInputAssociation).equals(associatedResource) == false)
				throw new Exception("There are more than one resource associated to the input slot");
		return associatedResource;
	}

	private Object findAssociatedInputResource(DataInputAssociation dataInputAssociation) throws Exception {
		if ((dataInputAssociation.getSourceReves().size() == 1))
			return dataInputAssociation.getSourceReves().get(0).getValue();
		else {
			if (dataInputAssociation.getSourceReves().isEmpty())
				throw new Exception("The data association: " + dataInputAssociation.getId() + " has not input resource");
			else
				throw new Exception("The data association: " + dataInputAssociation.getId() + " has more than one input resource");
		}
	}

	private Object findAssociatedOutputResource(List<DataOutputAssociation> dataOutputAssociations) throws Exception {
		Object associatedResource = findAssociatedOutputResource(dataOutputAssociations.get(0));
		for (DataOutputAssociation dataOutputAssociation : dataOutputAssociations)
			if (findAssociatedOutputResource(dataOutputAssociation).equals(associatedResource) == false)
				throw new Exception("There are more than one resource associated to the output slot");
		return associatedResource;
	}

	private Object findAssociatedOutputResource(DataOutputAssociation dataOutputAssociation) throws Exception {
		return dataOutputAssociation.getTargetRef();
	}

	private List<DataInput> getDataInputs(InputSlot inputSlot) {
		List<DataInput> dataInputs = new ArrayList<DataInput>();
		for (DataInput dataInput : bpmnProcess.getDataInputs())
			if (dataInput.getName().equals(inputSlot.getId()))
				dataInputs.add(dataInput);
		return dataInputs;
	}

	private List<DataOutput> getDataOutputs(OutputSlot outputSlot) {
		List<DataOutput> dataOutputs = new ArrayList<DataOutput>();
		for (DataOutput dataOutput : bpmnProcess.getDataOutputs())
			if (dataOutput.getName().equals(outputSlot.getId()))
				dataOutputs.add(dataOutput);
		return dataOutputs;
	}

	private List<DataInputAssociation> getDataInputAssociations(DataInput dataInput) {
		List<DataInputAssociation> dataInputAssociations = new ArrayList<DataInputAssociation>();
		for (DataInputAssociation dataInputAssociation : bpmnProcess.getDataInputAssociations())
			if (isDataInputAssociationRelatedToDataInput(dataInput, dataInputAssociation))
				dataInputAssociations.add(dataInputAssociation);
		return dataInputAssociations;
	}

	private List<DataOutputAssociation> getDataOutputAssociations(DataOutput dataOutput) {
		List<DataOutputAssociation> dataOutputAssociations = new ArrayList<DataOutputAssociation>();
		for (DataOutputAssociation dataOutputAssociation : bpmnProcess.getDataOutputAssociations())
			if (isDataOutputAssociationRelatedToDataOutput(dataOutput, dataOutputAssociation))
				dataOutputAssociations.add(dataOutputAssociation);
		return dataOutputAssociations;
	}

	private boolean isDataInputAssociationRelatedToDataInput(DataInput dataInput, DataInputAssociation dataInputAssociation) {
		return (dataInputAssociation.getTargetRef().equals(dataInput));
	}

	private boolean isDataOutputAssociationRelatedToDataOutput(DataOutput dataOutput, DataOutputAssociation dataOutputAssociation) {
		return ((dataOutputAssociation.getSourceReves().size() == 1) && (dataOutputAssociation.getSourceReves().get(0).getValue()
				.equals(dataOutput)));
	}

	// --------------- GETTERS AND SETTERS ----------------//

	public ProcessBase getProcess() {
		return process;
	}

	public void setProcess(ProcessBase process) {
		this.process = process;
	}

	public BPMNProcess getBPMNProcess() {
		return bpmnProcess;
	}

	public void setBPMNProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}

}
