package eu.pericles.processcompiler.cli;

import java.io.File;
import java.net.URL;

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
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class CommandlineInterface {

	public static enum Cmd {
		COMPILE, VALIDATE_IMPL, VALUDATE_AGGR
	}

	public static void main(String[] args) {
		System.exit(new CommandlineInterface().call(args));
	}

	private URL ermr;
	private String repo;
	private ProcessCompiler compiler;

	public int call(String[] args) {

		ArgumentParser parser = ArgumentParsers.newArgumentParser("modelcompiler").defaultHelp(true)
				.description("Compile or validate aggregated processes.");

		parser.addArgument("-s").metavar("URL").required(true).type(URL.class)
				.help("URL (including credentials) to the ERMR");
		parser.addArgument("-r").metavar("REPO").required(true).help("Repository name");

		Subparser cmdParser = parser.addSubparsers().addParser("compile").help("Compile an aggregated process")
				.setDefault("cmd", Cmd.COMPILE);
		cmdParser.addArgument("-o").metavar("FILE").help("Output file (defaults to stdout)");
		cmdParser.addArgument("APROC")
				.help("Either a json file on disk or the ID of an already defined aggregated process.");

		Subparser vaParser = parser.addSubparsers().addParser("validate_aggregation").help("Validate an aggregation")
				.setDefault("cmd", Cmd.VALUDATE_AGGR);
		vaParser.addArgument("APROC")
				.help("Either a json file on disk or the ID of an already defined aggregated process.");

		Subparser viParser = parser.addSubparsers().addParser("validate_implementation")
				.help("Validate an implementation").setDefault("cmd", Cmd.VALIDATE_IMPL);
		viParser.addArgument("APROC")
				.help("Either a json file on disk or the ID of an already defined aggregated process.");
		viParser.addArgument("IMPL")
				.help("Either a json file on disk or the ID of an already defined aggregated process.");

		Namespace ns = null;
		try {
			ns = parser.parseArgs(args);
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			return 2;
		}

		/*
		 * Parse general parameters
		 */

		ermr = ns.<URL>get("s");
		repo = ns.<String>get("r");

		try {
			compiler = new ProcessCompiler(ermr.toString());
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
			return 2;
		}

		switch (ns.<Cmd>get("cmd")) {
		case COMPILE:
			return compile(ns);
		case VALIDATE_IMPL:
			return validateImplementation(ns);
		case VALUDATE_AGGR:
			return validateAggregation(ns);
		default:
			return 2;
		}
	}

	private int validateAggregation(Namespace ns) {

		try {
			String input = ns.getString("APROC");
			AggregatedProcess aggregatedProcess;
			if (new File(input).exists()) {
				JSONObject node = JSONParserForCLI.parseFile(input);
				aggregatedProcess = JSONParserForCLI
						.parseAggregatedProcess((JSONObject) node.get("aggregated_process"));
			} else
				aggregatedProcess = compiler.getAggregatedProcess(repo, input);

			ValidationResult result = compiler.validateAggregation(repo, aggregatedProcess);

			System.out.println("200 OK");
			if (result.isValid()) {
				System.out.println("Valid aggregation");
				return 0;
			} else {
				System.out.println("Invalid aggregation: " + result.getMessage());
				return 1;
			}

		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (JSONFileParseException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when parsing input file: " + e.getMessage());
		}

		return 2;
	}

	private int validateImplementation(Namespace ns) {

		try {
			String aproc = ns.getString("APROC");
			String impl = ns.getString("IMPL");
			Process process;
			if (new File(aproc).exists()) {
				JSONObject node = JSONParserForCLI.parseFile(aproc);
				process = JSONParserForCLI.parseProcess((JSONObject) node.get("process"));
			} else
				process = compiler.getProcess(repo, aproc);

			BPMNProcess bpmnProcess;

			if (new File(impl).exists()) {
				bpmnProcess = JSONParserForCLI.parseBPMNProcess(impl);
			} else
				bpmnProcess = compiler.getBPMNProcessFromImplementation(repo, impl);

			ValidationResult result = compiler.validateImplementation(process, bpmnProcess);

			System.out.println("200 OK");
			if (result.isValid()) {
				System.out.println("Valid implementation");
				return 0;
			} else {
				System.out.println("Invalid implementation: " + result.getMessage());
				return 1;
			}
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
		return 2;
	}

	private int compile(Namespace ns) {
		try {
			String input = ns.getString("APROC");
			AggregatedProcess aggregatedProcess;
			if (new File(input).exists()) {
				JSONObject node = JSONParserForCLI.parseFile(input);
				aggregatedProcess = JSONParserForCLI
						.parseAggregatedProcess((JSONObject) node.get("aggregated_process"));
			} else
				aggregatedProcess = compiler.getAggregatedProcess(repo, input);

			BPMNProcess bpmnProcess = compiler.compileAggregatedProcess(repo, aggregatedProcess);

			if (ns.get("FILE") != null)
				new BPMNWriter().write(bpmnProcess, ns.<String>get("FILE"));
			else
				new BPMNWriter().write(bpmnProcess, System.out);

			System.out.println("201 Created");
			return 0;

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
		return 1;
	}

}
