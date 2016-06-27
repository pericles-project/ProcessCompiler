package eu.pericles.processcompiler.cli;

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
		if (args.length >= 1) {
			if (args[0].equals("compile"))
				new CommandlineInterface().compile(args);
			else if (args[0].equals("validate_aggregation"))
				new CommandlineInterface().validateAggregation(args);
			else if (args[0].equals("validate_implementation"))
				new CommandlineInterface().validateImplementation(args);
			else {
				System.out.println("400 Bad Request");
				System.out.println("Unknown command: " + args[0]);
			}
		} else {
			System.out.println("400 Bad Request");
			System.out.println("Not enough arguments");
		}
	}

	private void validateAggregation(String[] args) {
		assert args[0].equals("validate_aggregation");

		if (validArgsForAggregation(args)) {
			try {
				String uri = args[1];
				String repository = args[2];
				String flag = args[3];

				ProcessCompiler compiler = new ProcessCompiler(uri);
				AggregatedProcess aggregatedProcess;
				
				if (flag.equals("-f")) {
					JSONObject input = JSONParserForCLI.parseFile(args[4]);
					aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) input.get("aggregated_process"));
				} else 
					aggregatedProcess = compiler.getAggregatedProcess(repository, args[4]);
				

				ValidationResult result = compiler.validateAggregation(repository, aggregatedProcess);

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
			System.out.println("Valid arguments: <command> <ermr_uri> <repository> <flag> <aggregated_process>");
			System.out.println("\t-f\taggregated process described by a JSON file");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -f aggregated_process.json");
			System.out.println("\t-i\tid of the aggregated process");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -i http://www.pericles-project.eu/ns/ecosystem#agpIngestArtwork");
		}
	}

	private boolean validArgsForAggregation(String[] args) {
		return (args.length == 5) && ((args[3].equals("-f")) || (args[3].equals("-i")));
	}

	private void validateImplementation(String[] args) {
		assert args[0].equals("validate_implementation");

		if (validArgsForImplementation(args)) {
			try {
				String uri = args[1];
				String repository = args[2];
				String flag = args[3];

				ProcessCompiler compiler = new ProcessCompiler(uri);
				Process process;
				
				if (flag.equals("-f")) {
					JSONObject input = JSONParserForCLI.parseFile(args[4]);
					process = JSONParserForCLI.parseProcess((JSONObject) input.get("process"));
				} else 
					process = compiler.getProcess(repository, args[4]);
				
				BPMNProcess bpmnProcess;
				flag = args[5];
				if (flag.equals("-f")) 
					bpmnProcess = JSONParserForCLI.parseBPMNProcess(args[6]);
				 else 
					bpmnProcess = compiler.getBPMNProcessFromImplementation(repository, args[6]);
				
				
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
			System.out.println("Valid arguments: <command> <ermr_uri> <repository> <flag> <aggregated_process> <flag> <implementation>");
			System.out.println("\t-f\taggregated process described by a JSON file");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -f aggregated_process.json");
			System.out.println("\t-i\tid of the aggregated process");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -i http://www.pericles-project.eu/ns/ecosystem#agpIngestArtwork");
		}
	}
	
	private boolean validArgsForImplementation(String[] args) {
		return (args.length == 7) && ((args[3].equals("-f")) || (args[3].equals("-i"))) && ((args[5].equals("-f")) || (args[5].equals("-i")));
	}

	private void compile(String[] args) {
		assert args[0].equals("compile");

		if (validArgsForCompilation(args)) {
			try {
				String uri = args[1];
				String repository = args[2];
				String flag = args[3];

				ProcessCompiler compiler = new ProcessCompiler(uri);
				AggregatedProcess aggregatedProcess;
				
				if (flag.equals("-f")) {
					JSONObject input = JSONParserForCLI.parseFile(args[4]);
					aggregatedProcess = JSONParserForCLI.parseAggregatedProcess((JSONObject) input.get("aggregated_process"));
				} else 
					aggregatedProcess = compiler.getAggregatedProcess(repository, args[4]);
				
				BPMNProcess bpmnProcess = compiler.compileAggregatedProcess(repository, aggregatedProcess);

				if (args.length == 6)
					new BPMNWriter().write(bpmnProcess, args[5]);
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
		} else {
			System.out.println("400 Bad Request");
			System.out.println("Valid arguments: <command> <ermr_uri> <repository> <flag> <aggregated_process> <implementation_file>");
			System.out.println("\t-f\taggregated process described by a JSON file");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -f aggregated_process.json output.bpmn2");
			System.out.println("\t-i\tid of the aggregated process");
			System.out
					.println("{\t\tvalidate_aggregation https://pericles1:PASSWORD@141.5.100.67/api myRepository -i http://www.pericles-project.eu/ns/ecosystem#agpIngestArtwork output.bpmn2");
			System.out.println("\t<implementation_file> is optional, if not specified, the result of compilation is displayed at console");
		}
	}
	
	private boolean validArgsForCompilation(String[] args) {
		return (args.length >= 5) && ((args[3].equals("-f")) || (args[3].equals("-i")));
	}

}
