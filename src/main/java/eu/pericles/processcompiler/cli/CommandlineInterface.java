package eu.pericles.processcompiler.cli;

import java.util.HashMap;

import org.json.simple.JSONObject;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;

public class CommandlineInterface {	

	public static void main(String[] args) throws Exception {
		JSONObject jsonObject = JSONParserForCLI.parseFile(args[1]);
		if (jsonObject.get("cmd").equals("compile"))
			new CommandlineInterface().compile(jsonObject);
	}

	private void compile(JSONObject jsonObject) throws Exception {
		assert jsonObject.get("cmd").equals("compile");
		JSONObject params = (JSONObject) jsonObject.get("params");
		HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) params.get("config_data"));
		AggregatedProcess aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) params.get("aggregated_process"));
		print(aggregatedProcess);
		ProcessCompiler compiler = new ProcessCompiler(configuration);
		BPMNProcess bpmnProcess = compiler.compileAggregatedProcess((String) params.get("repository"), aggregatedProcess);
		new BPMNWriter().write(bpmnProcess, (String) params.get("output"));
	}
	
	private static void print(AggregatedProcess aggregatedProcess) {
		System.out.println("ID: " + aggregatedProcess.getId());
		System.out.println("Name: " + aggregatedProcess.getName());
		System.out.println("Description: " + aggregatedProcess.getDescription());
		System.out.println("Version: " + aggregatedProcess.getVersion());
		System.out.println("Input Slots:");
		for (InputSlot inputSlot : aggregatedProcess.getInputs()) 
			System.out.println("\t" + inputSlot.getId());
		System.out.println("Output Slots:");
		for (OutputSlot outputSlot : aggregatedProcess.getOutputs()) 
			System.out.println("\t" + outputSlot.getId());
		System.out.println("Process Flow:");
		for (String process : aggregatedProcess.getSequence().getProcessFlow())
			System.out.println("\t" + process);
		System.out.println("Data Flow:");
		for (DataConnection connection : aggregatedProcess.getSequence().getDataFlow())
			System.out.println("\tData Connection: " + connection.getSequenceStep() + " " + connection.getSlot() + " " + connection.getResource());
		/*System.out.println("Implementation: " + aggregatedProcess.getImplementation().getId());
		System.out.println("\t" + aggregatedProcess.getImplementation().getVersion() + " " + aggregatedProcess.getImplementation().getType() + " " + aggregatedProcess.getImplementation().getLocation());
		System.out.println("\tFixity: " + aggregatedProcess.getImplementation().getFixity().getAlgorithm() + " " + aggregatedProcess.getImplementation().getFixity().getChecksum());*/
	}

}
