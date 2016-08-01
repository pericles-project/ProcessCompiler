package eu.pericles.processcompiler.ng.testutils;

import java.util.List;

import eu.pericles.processcompiler.ng.ecosystem.Fixity;
import eu.pericles.processcompiler.ng.ecosystem.Implementation;
import eu.pericles.processcompiler.ng.ecosystem.InputSlot;
import eu.pericles.processcompiler.ng.ecosystem.OutputSlot;

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

}
