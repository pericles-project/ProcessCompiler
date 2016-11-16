package eu.pericles.processcompiler.web.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ermr.ERMRCommunicationsFake;
import eu.pericles.processcompiler.exceptions.BPMNParserException;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;
import eu.pericles.processcompiler.exceptions.PCException;
import eu.pericles.processcompiler.web.ApiException;
import eu.pericles.processcompiler.web.providers.Template;
import eu.pericles.processcompiler.web.resources.WorkspaceState.WSFile;

@Path("/demo")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class DemoResource extends BaseResource {

	public static final Charset UTF8 = Charset.forName("UTF-8");

	private static final String SCENARIO_PATH = "/Scenarios/";
	public static final List<String> scenarios = new ArrayList<>();

	static List<String> findScenarios() throws IOException {
		if (scenarios.isEmpty()) {
			synchronized (scenarios) {
				scenarios.clear();
				URI uri = URI.create(DemoResource.class.getResource(SCENARIO_PATH).toString());
				try (FileSystem fileSystem = (uri.getScheme().equals("jar")
						? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null);
						DirectoryStream<java.nio.file.Path> ds = Files.newDirectoryStream(Paths.get(uri))) {
					for (java.nio.file.Path p : ds) {
						if (!Files.isDirectory(p))
							continue;
						scenarios.add(p.getFileName().toString());
					}
				}
			}
		}
		return scenarios;
	}

	static WorkspaceState loadScenario(String name) throws IOException {
		WorkspaceState workspace = new WorkspaceState();
		workspace.name = name;
		URI uri = URI.create(DemoResource.class.getResource(SCENARIO_PATH).toString());

		try (FileSystem fileSystem = (uri.getScheme().equals("jar")
				? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null);
				DirectoryStream<java.nio.file.Path> ds = Files.newDirectoryStream(Paths.get(uri).resolve(name))) {
			
			for (java.nio.file.Path p : ds) {
				if (Files.isDirectory(p))
					continue;

				String filename = p.getFileName().toString();

				// TODO: Add images as separate files
				if (filename.endsWith(".ttl") || filename.endsWith(".bpmn")) {
					WSFile wsf = new WSFile();
					wsf.text = new String(Files.readAllBytes(p), UTF8);
					java.nio.file.Path desc = p.resolveSibling(filename + ".txt");
					wsf.desc = Files.exists(desc) ? new String(Files.readAllBytes(desc), UTF8) : null;
					workspace.files.put(filename, wsf);
				} else if(filename.endsWith(".jpg") || filename.endsWith(".png")) {
					WSFile wsf = new WSFile();
					wsf.binary = true;
					java.nio.file.Path desc = p.resolveSibling(filename + ".txt");
					wsf.desc = Files.exists(desc) ? new String(Files.readAllBytes(desc), UTF8) : null;
					workspace.files.put(filename, wsf);
				} else {
					continue;
				}
			}
		}
		return workspace;
	}

	@GET
	public Template index() throws IOException, URISyntaxException {
		return new Template("index.ftlh").put("scenarios", findScenarios());
	}

	@GET
	@Path("{scenario}")
	public Template editor(@PathParam("scenario") String scenario) throws IOException, URISyntaxException {
		String sjson = om.writeValueAsString(loadScenario(scenario));
		return new Template("edit.ftlh").put("filesJson", sjson).put("scenarios", findScenarios());
	}

	@GET
	@Path("{scenario}/_files/{filename}")
	public Response getFile(@PathParam("scenario") String scenario, @PathParam("filename") String filename)
			throws IOException, URISyntaxException {
		URI baseuri = URI.create(DemoResource.class.getResource(SCENARIO_PATH).toString());
		URI uri = baseuri.resolve(scenario+"/").resolve(filename).normalize();
		if (!uri.toString().startsWith(baseuri.toString()))
			return Response.status(Status.NOT_FOUND).build();

		try (FileSystem fileSystem = (uri.getScheme().equals("jar")
				? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
			java.nio.file.Path path = java.nio.file.Paths.get(uri);
			if (!Files.exists(path) || !Files.isRegularFile(path))
				return Response.status(Status.NOT_FOUND).build();

			if (filename.endsWith(".jpg")) {
				return Response.ok(Files.newInputStream(path), "image/jpeg").build();
			} else if (filename.endsWith(".png")) {
				return Response.ok(Files.newInputStream(path), "image/png").build();
			}
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		model.read(WorkspaceState.class.getClassLoader().getResourceAsStream("webapp/demo/Ecosystem.ttl"), "", "TTL");
		Query query = QueryFactory.create("PREFIX ecosystem:<http://www.pericles-project.eu/ns/ecosystem#> "
				+ " SELECT DISTINCT ?name ?description ?version WHERE {"
				+ "ecosystem:agpIngestAWSW ecosystem:name ?name . "
				+ "ecosystem:agpIngestAWSW ecosystem:description ?description . "
				+ "ecosystem:agpIngestAWSW ecosystem:version ?version . " + " }");
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet x = qexec.execSelect();
		System.out.println(x.next());
		qexec.close();
	}

	@POST
	@Path("{action}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WorkspaceState runAction(@PathParam("action") String action, WorkspaceState state)
			throws ApiException, ERMRClientException, IOException {
		ERMRCommunicationsFake comm = new ERMRCommunicationsFake();

		for (Entry<String, WSFile> f : state.files.entrySet()) {
			String name = f.getKey();
			WSFile file = f.getValue();

			if (name.endsWith(".ttl")) {
				comm.importModel(file.text, "TTL");
			} else {
				comm.importImplementation(name, file.text);
			}
		}

		state.files.clear();
		try {
			if ("validate".equals(action))
				doValidate(state, comm);
			else if ("compile".equals(action))
				doCompile(state, comm);
			else if ("run".equals(action))
				doRun(state, comm);
			else
				throw new ApiException(404, "Action not implemented");

		} catch (ERMRClientException | JSONParserException | PCException | BPMNParserException e) {
			throw new ApiException(400, e.getMessage());
		}
		return state;

	}

	private void doValidate(WorkspaceState state, ERMRCommunicationsFake comm) {
	}

	private void doCompile(WorkspaceState state, ERMRCommunicationsFake comm) throws ERMRClientException, JSONParserException, PCException, BPMNParserException, IOException {
		ProcessCompiler pc = new ProcessCompiler(comm);

		Map<String, String> results = new HashMap<>();

		for (QuerySolution qs : comm.query(
				"SELECT DISTINCT ?uri WHERE { ?uri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.pericles-project.eu/ns/dem#AggregatedProcess> . }")) {
			String ag = qs.get("uri").asResource().getURI();
			results.putAll(pc.compileRecursively("", "<" + ag + ">"));
		}

		for (Entry<String, String> r : results.entrySet()) {
			WSFile newFile = new WSFile();
			newFile.output = true;
			newFile.text = r.getValue();
			newFile.desc = "DESC";
			state.files.put(r.getKey()+".bpmn", newFile);
			// TODO: Look for txt and jpg and png files.

		}

		// pc.compileRecursively(repository, processId)

	}

	private void doRun(WorkspaceState state, ERMRCommunicationsFake comm) {
	}

}
