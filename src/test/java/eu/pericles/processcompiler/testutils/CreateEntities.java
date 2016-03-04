package eu.pericles.processcompiler.testutils;

import java.util.List;

import eu.pericles.processcompiler.communications.ermr.JSONParser;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Sequence;

public class CreateEntities {
	
	public static Implementation createImplementation(List<String> values) {
		Implementation implementation = new Implementation();
		implementation.setId(values.get(0));
		implementation.setVersion(values.get(1));
		implementation.setType(values.get(2));
		implementation.setLocation(values.get(3));
		implementation.setFixity(new Fixity());
		implementation.getFixity().setAlgorithm(values.get(4));
		implementation.getFixity().setChecksum(values.get(5));
		
		return implementation;
	}
	
	public static InputSlot createInputSlot(List<String> values, boolean optional) {
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId(values.get(0));
		inputSlot.setName(values.get(1));
		inputSlot.setDescription(values.get(2));
		inputSlot.setType(values.get(3));
		inputSlot.setOptional(optional);
		
		return inputSlot;
	}
	
	public static OutputSlot createOutputSlot(List<String> values) {
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId(values.get(0));
		outputSlot.setName(values.get(1));
		outputSlot.setDescription(values.get(2));
		outputSlot.setType(values.get(3));
		
		return outputSlot;
	}
	
	public static Sequence createSequence(String processFlow, String dataFlow) {
		Sequence sequence = new Sequence();
		sequence.setProcessFlow(JSONParser.parseProcessFlow(processFlow));
		sequence.setDataFlow(JSONParser.parseDataFlow(dataFlow));
		
		return sequence;
	}
	
	public static List<DataConnection> createDataFlow(String dataFlow) {
		return JSONParser.parseDataFlow(dataFlow);
	}

}
