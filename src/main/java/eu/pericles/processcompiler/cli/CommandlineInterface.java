package eu.pericles.processcompiler.cli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.json.simple.JSONObject;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.Validator.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;
import eu.pericles.processcompiler.exceptions.ProcessProcessFlowException;
import eu.pericles.processcompiler.exceptions.ValidationException;

import eu.pericles.processcompiler.ecosystem.Process;

public class CommandlineInterface {	

	public static void main(String[] args) {
		try {
			JSONObject jsonObject = JSONParserForCLI.parseFile(args[1]);
			if (jsonObject.get("cmd").equals("compile"))
				new CommandlineInterface().compile(jsonObject);
			if (jsonObject.get("cmd").equals("validate_aggregation"))
				new CommandlineInterface().validateAggregation(jsonObject);
			if (jsonObject.get("cmd").equals("validate_implementation"))
				new CommandlineInterface().validateImplementation(jsonObject);
		} catch (Exception e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when parsing input file: " + e.getMessage());
		}
	}

	private void validateAggregation(JSONObject jsonObject) {
		assert jsonObject.get("cmd").equals("validate_aggregation");
		
		JSONObject params = (JSONObject) jsonObject.get("params");
		HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) params.get("config_data"));
		AggregatedProcess aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) params.get("aggregated_process"));
		
		try {
			ProcessCompiler compiler = new ProcessCompiler(configuration);
			ValidationResult result = compiler.validateAggregation((String) params.get("repository"), aggregatedProcess);
			System.out.println("200 OK");
			if (result.isValid())
				System.out.println("Valid aggregation");
			else
				System.out.println("Invalid aggregation: " + result.getMessage());
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		}
	}

	private void validateImplementation(JSONObject jsonObject) {
		assert jsonObject.get("cmd").equals("validate_process");
		
		JSONObject params = (JSONObject) jsonObject.get("params");
		HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) params.get("config_data"));
		Process process = JSONParserForCLI.parseProcess((JSONObject) params.get("process"));
		try {
			BPMNProcess bpmnProcess = JSONParserForCLI.parseBPMNProcess((String) params.get("bpmn_process"));
			ProcessCompiler compiler = new ProcessCompiler(configuration);
			ValidationResult result = compiler.validateImplementation(process, bpmnProcess);
			System.out.println("200 OK");
			if (result.isValid())
				System.out.println("Valid implementation");
			else
				System.out.println("Invalid implementation: " + result.getMessage());
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (BPMNParseException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when parsing input file: " + e.getMessage());
		}		
	}

	private void compile(JSONObject jsonObject) {
		assert jsonObject.get("cmd").equals("compile");
		
		JSONObject params = (JSONObject) jsonObject.get("params");
		HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) params.get("config_data"));
		AggregatedProcess aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) params.get("aggregated_process"));
		
		try {
			ProcessCompiler compiler = new ProcessCompiler(configuration);
			BPMNProcess bpmnProcess = compiler.compileAggregatedProcess((String) params.get("repository"), aggregatedProcess);
			
			new BPMNWriter().write(bpmnProcess, (String) params.get("output"));
			System.out.println("201 Created");
			
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (BPMNParseException | ValidationException | ProcessDataFlowException | ProcessProcessFlowException e) {
			System.out.println("404 Not Found");
			System.out.println("Error when compiling BPMN file: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("404 Not Found");
			System.out.println("Error when writing BPMN file: " + e.getMessage());
		} catch (JAXBException | IOException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("Error when writing BPMN file: " + e.getMessage());
		}
	}

}
