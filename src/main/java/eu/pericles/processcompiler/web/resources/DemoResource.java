package eu.pericles.processcompiler.web.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.pericles.processcompiler.core.ProcessCompiler;
import eu.pericles.processcompiler.ermr.ERMRCommunicationsFake;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.web.ApiException;
import eu.pericles.processcompiler.web.providers.Template;
import eu.pericles.processcompiler.web.resources.WorkspaceState.WSFile;

@Path("/demo")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class DemoResource extends BaseResource {

	@GET
	public Template index() throws IOException {
		WorkspaceState workspace = new WorkspaceState();
		ObjectNode json = om.createObjectNode();
		ObjectNode files = json.putObject("files");

		for (String name : new String[] { "Ecosystem.ttl", "EncapsulateDOMD.bpmn", "ExtractMD.bpmn", "VirusCheck.bpmn",
				"IngestAWSW.bpmn" }) {
			InputStream io = getClass().getResourceAsStream("/webapp/demo/" + name);
			InputStream iodesc = getClass().getResourceAsStream("/webapp/demo/" + name + ".txt");
			
			WSFile wsf = new WSFile();
			wsf.text = IOUtils.toString(io);
			wsf.desc = iodesc == null ? null : IOUtils.toString(iodesc);
			workspace.files.put(name, wsf);
			IOUtils.closeQuietly(io);
			IOUtils.closeQuietly(iodesc);
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
		Model model = ModelFactory.createDefaultModel();
		model.read(WorkspaceState.class.getClassLoader().getResourceAsStream("webapp/demo/Ecosystem.ttl"), "", "TTL");
		java.nio.file.Path tmp = Files.createTempDirectory("mcdemo").toAbsolutePath();

		try {

			for (Entry<String, WSFile> f : state.files.entrySet()) {
				java.nio.file.Path path = tmp.resolve(f.getKey()).toAbsolutePath();
				WSFile file = f.getValue();

				if (!path.startsWith(tmp))
					throw new ApiException(400, "Invalid file name: " + f.getKey());

				Files.createDirectories(path.getParent());
				Files.write(path, file.text.getBytes("utf-8"));

				if (path.getFileName().toString().endsWith(".ttl")) {
					try (InputStream s = Files.newInputStream(path)) {
						model.read(s, "", "TTL");
					}
				}
			}

			ERMRCommunicationsFake comm = new ERMRCommunicationsFake(model, tmp);
			state.files.clear();

			if ("validate".equals(action))
				doValidate(state, tmp, comm);
			else if ("compile".equals(action))
				doCompile(state, tmp, comm);
			else if ("run".equals(action))
				doRun(state, tmp, comm);
			else
				throw new ApiException(404, "Action not implemented");
			
			return state;
			
		} finally {
			FileUtils.deleteDirectory(tmp.toFile());
		}
	}

	private void doValidate(WorkspaceState state, java.nio.file.Path tmp, ERMRCommunicationsFake comm) {
	}

	private void doCompile(WorkspaceState state, java.nio.file.Path tmp, ERMRCommunicationsFake comm)
			throws ERMRClientException {
		ProcessCompiler pc = new ProcessCompiler(comm);
		WSFile newFile = new WSFile();
		newFile.output=true;
		newFile.text="YAY!!!";
		newFile.desc="DESC";
		state.files.put("result.txt", newFile);
	}

	private void doRun(WorkspaceState state, java.nio.file.Path tmp, ERMRCommunicationsFake comm) {
	}

}
