package eu.pericles.processcompiler.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.DataConnection;
import eu.pericles.processcompiler.ecosystem.DataFlowNode;
import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.ecosystem.Sequence;
import eu.pericles.processcompiler.exceptions.BPMNParseException;

public class JSONParserForCLI {
	
	public static JSONObject parseFile(String file) throws FileNotFoundException, IOException, ParseException  {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
		return jsonObject;
	}

	public static HashMap<String, String> parseConfigurationData(JSONObject jsonObject) {
		HashMap<String, String> parameters = new HashMap<String, String>(); 
		List<String> keys = Arrays.asList("user", "password", "tripleStore", "objectStore", "findApi", "repository");
		for (String key : keys) 
			if (jsonObject.containsKey(key))
				parameters.put(key, (String) jsonObject.get(key));
		return parameters;
	}

	public static AggregatedProcess parseAggregatedProcess(JSONObject jsonObject) {	
		AggregatedProcess aggregatedProcess = new AggregatedProcess(parseProcess(jsonObject));
		aggregatedProcess.setSequence(parseSequence((JSONObject) jsonObject.get("sequence")));
		return aggregatedProcess;
	}

	public static Process parseProcess(JSONObject jsonObject) {
		Process process = new Process();
		process.setId((String) jsonObject.get("id"));
		process.setName((String) jsonObject.get("name"));
		process.setDescription((String) jsonObject.get("description"));
		process.setVersion((String) jsonObject.get("version"));
		process.setInputs(parseInputSlots((JSONArray) jsonObject.get("input_slots")));
		process.setOutputs(parseOutputSlots((JSONArray) jsonObject.get("output_slots")));
		return process;
	}
	
	public static List<OutputSlot> parseOutputSlots(JSONArray jsonArray) {
		List<OutputSlot> outputSlots =  new ArrayList<OutputSlot>();
		for (int object = 0; object < jsonArray.size(); object ++)
			outputSlots.add(parseOutputSlot((JSONObject) jsonArray.get(object)));
		return outputSlots;
	}

	public static OutputSlot parseOutputSlot(JSONObject jsonObject) {
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId((String) jsonObject.get("id")); 
		outputSlot.setName((String) jsonObject.get("name"));
		outputSlot.setDescription((String) jsonObject.get("description")); 
		outputSlot.setType((String) jsonObject.get("type")); 
		return outputSlot;
	}

	public static List<InputSlot> parseInputSlots(JSONArray jsonArray) {
		List<InputSlot> inputSlots =  new ArrayList<InputSlot>();
		for (int object = 0; object < jsonArray.size(); object ++)
			inputSlots.add(parseInputSlot((JSONObject) jsonArray.get(object)));
		return inputSlots;
	}
	
	public static InputSlot parseInputSlot(JSONObject jsonObject) {
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId((String) jsonObject.get("id")); 
		inputSlot.setName((String) jsonObject.get("name"));
		inputSlot.setDescription((String) jsonObject.get("description")); 
		inputSlot.setType((String) jsonObject.get("type")); 
		inputSlot.setOptional((boolean) jsonObject.get("optional")); 
		return inputSlot;
	}

	public static Sequence parseSequence(JSONObject jsonObject) {
		Sequence sequence = new Sequence();
		sequence.setId((String) jsonObject.get("id"));
		sequence.setProcessFlow(parseProcessFlow((JSONArray) jsonObject.get("process_flow")));
		sequence.setDataFlow(parseDataFlow((JSONArray) jsonObject.get("data_flow")));
		return sequence;
	}

	public static List<String> parseProcessFlow(JSONArray jsonArray) {
		List<String> processFlow = new ArrayList<String>();
		for (int object = 0; object < jsonArray.size(); object ++)
			processFlow.add((String) jsonArray.get(object));
		return processFlow;
	}

	public static List<DataConnection> parseDataFlow(JSONArray jsonArray) {
		List<DataConnection> dataFlow = new ArrayList<DataConnection>();
		for (int object = 0; object < jsonArray.size(); object ++)
			dataFlow.add(parseDataConnection((JSONObject) jsonArray.get(object)));
		return dataFlow;
	}

	public static DataConnection parseDataConnection(JSONObject jsonObject) {
		JSONObject input = (JSONObject) jsonObject.get("input");
		JSONObject resource = (JSONObject) jsonObject.get("resource");
		DataConnection dataConnection = new DataConnection(parseDataFlowNode(input), parseDataFlowNode(resource));
		return dataConnection;
	}

	public static DataFlowNode parseDataFlowNode(JSONObject jsonObject) {
		DataFlowNode dataFlowNode = new DataFlowNode(Integer.parseInt((String) jsonObject.get("step")), (String) jsonObject.get("slot"));
		return dataFlowNode;
	}

	public static Implementation parseImplementation(JSONObject jsonObject) {
		Implementation implementation = new Implementation();
		implementation.setId((String) jsonObject.get("id"));
		implementation.setVersion((String) jsonObject.get("version"));
		implementation.setType((String) jsonObject.get("type"));
		implementation.setLocation((String) jsonObject.get("location"));
		implementation.setFixity(parseFixity((JSONObject) jsonObject.get("fixity")));
		return implementation;
	}
	
	public static Fixity parseFixity(JSONObject jsonObject) {
		Fixity fixity = new Fixity();
		fixity.setAlgorithm((String) jsonObject.get("algorithm"));
		fixity.setChecksum((String) jsonObject.get("checksum"));
		return fixity;
	}

	public static BPMNProcess parseBPMNProcess(String file) throws BPMNParseException {
		BPMNProcess bpmnProcess = new BPMNParser().parse(file);
		return bpmnProcess;
	}

}
