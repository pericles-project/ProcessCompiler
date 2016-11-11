package eu.pericles.processcompiler.cli;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.pericles.processcompiler.bpmn.BPMNParser;
import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.core.ProcessCompiler.ValidationResult;
import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.BPMNParserException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.web.ApiApplication;
import eu.pericles.processcompiler.web.ApiApplication.ERMRConfig;

public class CommandlineInterface {

	static ObjectMapper om = new ObjectMapper();
	private URL ermr;
	private String repo;
	private ProcessCompiler compiler;

	public static enum Cmd {
		COMPILE, VALIDATE_IMPL, VALIDATE_AGGR, SERVER
	}

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

	public static void main(String[] args) {
		System.exit(new CommandlineInterface().call(args));
	}

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

		Subparser srvParser = parser.addSubparsers().addParser("server").help("Start a server process")
				.setDefault("cmd", Cmd.SERVER);
		srvParser.addArgument("-p").metavar("PORT").setDefault(8080).type(Integer.class).help("Network port (always localhost)");
		
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
		case SERVER:
			return startServer(ns);
		default:
			return 2;
		}
	}

	private int startServer(Namespace ns) {
        ERMRConfig ermrc = new ERMRConfig(ermr.toString(), repo);
        int port = ns.getInt("p");
        ApiApplication.startServer(port, ermrc);
        return 0;
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
			System.out.println(result.getMessage());
			if (result.isValid()) 
				return 0;
			else 
				return 1;
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (IOException | JSONParserException e) {
			System.out.println("400 Bad Request");
			System.out.println(e.getMessage());
		} 
		return 2;
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
				bpmnProcess = compiler.getBPMNProcess(repo, impl);

			ValidationResult result = compiler.validateImplementation(process, bpmnProcess);

			System.out.println("200 OK");
			System.out.println(result.getMessage());
			if (result.isValid()) 
				return 0;
			else 
				return 1;
		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (BPMNParserException | IOException | JSONParserException e) {
			System.out.println("400 Bad Request");
			System.out.println(e.getMessage());
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

			String result = compiler.compile(repo, aggregatedProcess);
			
			String file = ns.<String>get("o");
			if (file != null)
				FileUtils.writeStringToFile(new File(file), result);
			else
				System.out.println(result);

			System.out.println("200 OK");
			return 0;

		} catch (ERMRClientException e) {
			System.out.println("500 Internal Server Error");
			System.out.println("ERMR (Triplestore) is not available: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when writing output file: " + e.getMessage());
		} catch (PCException | JSONParserException e) {
			System.out.println("400 Bad Request");
			System.out.println("Error when compiling process: " + e.getMessage());
		} 
		return 1;
	}
	
	private ConfigBean parseConfig(File src) throws IOException {
		return om.readValue(src, ConfigBean.class);
	}
}
