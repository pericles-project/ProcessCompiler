package eu.pericles.processcompiler.cli;

import java.util.HashMap;

import org.json.simple.JSONObject;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.Validator.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Process;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.BPMNWriteException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONFileParseException;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;
import eu.pericles.processcompiler.exceptions.ProcessProcessFlowException;
import eu.pericles.processcompiler.exceptions.ValidationException;

public class CommandlineInterface {

	public static void main(String[] args) {
		if (args.length > 1) {
			if (args[1].equals("compile"))
				new CommandlineInterface().compile(args);
			else if (args[1].equals("validate_aggregation"))
				new CommandlineInterface().validateAggregation(args);

			else if (args[1].equals("validate_implementation"))
				new CommandlineInterface().validateImplementation(args);
			else {
				System.out.println("400 Bad Request");
				System.out.println("Unknown command");
			}
		} else {
			System.out.println("400 Bad Request");
			System.out.println("Not enough arguments");
		}
	}

	private void validateAggregation(String[] args) {
		assert args[1].equals("validate_aggregation");

		if (args.length > 2) {
			try {
				JSONObject input = JSONParserForCLI.parseFile(args[2]);
				HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) input.get("config_data"));
				AggregatedProcess aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) input.get("aggregated_process"));

				ProcessCompiler compiler = new ProcessCompiler(configuration);
				ValidationResult result = compiler.validateAggregation((String) input.get("repository"), aggregatedProcess);

				System.out.println("200 OK");
				if (result.isValid())
					System.out.println("Valid aggregation");
				else
					System.out.println("Invalid aggregation: " + result.getMessage());

			} catch (ERMRClientException e) {
				System.out.println("500 Internal Server Error");
				System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
			} catch (JSONFileParseException e) {
				System.out.println("400 Bad Request");
				System.out.println("Error when parsing input file: " + e.getMessage());
			}
		} else {
			System.out.println("400 Bad Request");
			System.out.println("Missing input file");
		}
	}

	private void validateImplementation(String[] args) {
		assert args[1].equals("validate_process");

		if (args.length > 3) {
			try {
				JSONObject input = JSONParserForCLI.parseFile(args[2]);
				HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) input.get("config_data"));
				Process process = JSONParserForCLI.parseProcess((JSONObject) input.get("process"));
				BPMNProcess bpmnProcess = JSONParserForCLI.parseBPMNProcess(args[3]);

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
				System.out.println("Error when parsing BPMN file: " + e.getMessage());
			} catch (JSONFileParseException e) {
				System.out.println("400 Bad Request");
				System.out.println("Error when parsing input file: " + e.getMessage());
			}
		} else {
			System.out.println("400 Bad Request");
			System.out.println("Missing input and/or bpmn file");
		}
	}

	private void compile(String[] args) {
		assert args[1].equals("compile");

		if (args.length > 2) {
			try {
				JSONObject input = JSONParserForCLI.parseFile(args[2]);
				HashMap<String, String> configuration = JSONParserForCLI.parseConfigurationData((JSONObject) input.get("config_data"));
				AggregatedProcess aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) input.get("aggregated_process"));

				ProcessCompiler compiler = new ProcessCompiler(configuration);
				BPMNProcess bpmnProcess = compiler.compileAggregatedProcess((String) input.get("repository"), aggregatedProcess);

				if (args.length > 3)
					new BPMNWriter().write(bpmnProcess, args[3]);
				else
					new BPMNWriter().write(bpmnProcess, System.out);
				System.out.println("201 Created");

			} catch (ERMRClientException e) {
				System.out.println("500 Internal Server Error");
				System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
			} catch (BPMNParseException | ValidationException | ProcessDataFlowException | ProcessProcessFlowException e) {
				System.out.println("404 Not Found");
				System.out.println("Error when compiling BPMN file: " + e.getMessage());
			} catch (JSONFileParseException e) {
				System.out.println("400 Bad Request");
				System.out.println("Error when parsing input file: " + e.getMessage());
			} catch (BPMNWriteException e) {
				System.out.println("400 Bad Request");
				System.out.println("Error when writing BPMN file: " + e.getMessage());
			}
		}
	}

}
