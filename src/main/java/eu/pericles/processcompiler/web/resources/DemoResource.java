package eu.pericles.processcompiler.web.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

	@GET
	public Template index() throws IOException, URISyntaxException {
		return editor("Narrative_2");
	}
	
	@GET
	@Path("{scenario}")
	public Template editor(@PathParam("scenario") String scenario) throws IOException, URISyntaxException {
		WorkspaceState workspace = new WorkspaceState();
		ObjectNode json = om.createObjectNode();
		ObjectNode files = json.putObject("files");
		
		URI uri = getClass().getResource("/webapp/Scenarios/").toURI();
		
        try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
            java.nio.file.Path myPath = Paths.get(uri).resolve(scenario);
            for(java.nio.file.Path p: Files.newDirectoryStream(myPath)) {
            	if(Files.isDirectory(p)) continue;

    			InputStream io = Files.newInputStream(p);
    			java.nio.file.Path desc = p.resolveSibling(p.getFileName().toString()+".txt");
    			InputStream iodesc = Files.exists(desc) ? Files.newInputStream(desc) : null;

    			WSFile wsf = new WSFile();
    			wsf.text = IOUtils.toString(io);
    			wsf.desc = iodesc == null ? null : IOUtils.toString(iodesc);
    			workspace.files.put(p.getFileName().toString(), wsf);
    			IOUtils.closeQuietly(io);
    			IOUtils.closeQuietly(iodesc);            	
            }
        }
		
		
		for (String name : new String[] { "Ecosystem.ttl", "EncapsulateDOMD.bpmn", "ExtractMD.bpmn", "VirusCheck.bpmn",
				"IngestAWSW.bpmn" }) {
		}

		String sjson = om.writeValueAsString(workspace);

		return new Template("index.ftlh").put("filesJson", sjson);
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
			
		} catch (ERMRClientException e) {
			throw new ApiException(400, e.getMessage());
		}
		return state;


	}

	private void doValidate(WorkspaceState state, ERMRCommunicationsFake comm) {
	}

	private void doCompile(WorkspaceState state, ERMRCommunicationsFake comm) throws ERMRClientException {
		ProcessCompiler pc = new ProcessCompiler(comm);
		
		Map<String, String> results = new HashMap<>();

		for (QuerySolution qs : comm.query(
				"SELECT DISTINCT ?uri WHERE { ?uri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.pericles-project.eu/ns/dem#AggregatedProcess> . }")) {
			String ag = qs.get("uri").asResource().getURI();
			try {
				results.putAll(pc.compileRecursively("", "<"+ag+">"));
			} catch (JSONParserException | PCException | BPMNParserException | IOException e) {
				e.printStackTrace();
			}
		}
		
		for(Entry<String, String> r: results.entrySet()) {
			WSFile newFile = new WSFile();
			newFile.output = true;
			newFile.text = r.getValue();
			newFile.desc = "DESC";
			state.files.put(r.getKey(), newFile);
			
		}

		// pc.compileRecursively(repository, processId)

	}

	private void doRun(WorkspaceState state, ERMRCommunicationsFake comm) {
	}

}
