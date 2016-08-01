package eu.pericles.processcompiler.cli;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.bpmn.BPMNWriter;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.Validator.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.BPMNParseException;
import eu.pericles.processcompiler.exceptions.BPMNWriteException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.ProcessDataFlowException;
import eu.pericles.processcompiler.exceptions.ProcessProcessFlowException;
import eu.pericles.processcompiler.exceptions.ValidationException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class CommandlineInterface {

	static ObjectMapper om = new ObjectMapper();

	public static class ConfigBean {
		public ConfigBean() {
		}
		
		@JsonProperty("process")
		public ProcessBase process;

		@JsonProperty("aggregated_process")
		public AggregatedProcess aggregatedProcess;
		
		@JsonAnySetter
		public void set(String key, Object value) {
			System.err.println("Unknown property: " + key);
			
		}

		@JsonProperty("repository")
		public String unusedRepositoryStuff;
	}

	public static enum Cmd {
		COMPILE, VALIDATE_IMPL, VALIDATE_AGGR
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
				.setDefault("cmd", Cmd.VALIDATE_AGGR);
		vaParser.addArgument("APROC")
				.help("Either a json file on disk or the ID of an already defined aggregated process.");

		Subparser viParser = parser.addSubparsers().addParser("validate_implementation")
				.help("Validate an implementation").setDefault("cmd", Cmd.VALIDATE_IMPL);
		viParser.addArgument("PROC")
				.help("Either a json file on disk or the ID of an already defined process.");
		viParser.addArgument("IMPL")
				.help("Either a json file on disk or the ID of an already defined process.");

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
		case VALIDATE_AGGR:
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
				ConfigBean config = parseConfig(new File(input));
				aggregatedProcess = config.aggregatedProcess;
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
		} catch (IOException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when parsing input file: " + e.getMessage());
		}

		return 2;
	}

	private ConfigBean parseConfig(File src) throws IOException {
		return om.readValue(src, ConfigBean.class);
	}

	private int validateImplementation(Namespace ns) {

		try {
			String proc = ns.getString("PROC");
			String impl = ns.getString("IMPL");
			ProcessBase process;
			if (new File(proc).exists()) {
				ConfigBean config = parseConfig(new File(proc));
				process = config.process;
			} else
				process = compiler.getProcess(repo, proc);

			BPMNProcess bpmnProcess;

			if (new File(impl).exists()) {
				bpmnProcess = new BPMNParser().parse(impl);
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
		} catch (IOException e) {
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
				ConfigBean config = parseConfig(new File(input));
				aggregatedProcess = config.aggregatedProcess;
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
		} catch (IOException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when parsing input file: " + e.getMessage());
		} catch (BPMNWriteException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when writing BPMN file: " + e.getMessage());
		}
		return 1;
	}

}
