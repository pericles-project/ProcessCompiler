package eu.pericles.processcompiler.ermr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;

public class ERMRCommunicationsFake implements ERMRComm {

	private Model model;
	private Map<String, byte[]> files = new HashMap<>();

	public ERMRCommunicationsFake() {
		this(ModelFactory.createDefaultModel());
	}

	public ERMRCommunicationsFake(Model model) {
		this.model = model;
	}

	public void importModel(Model other) {
		this.model.add(other);
	}

	public void importModel(InputStream in, String format) {
		this.model.read(in, null, format);
	}

	public void importModel(String in, String format) {
		try (InputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"))) {
			this.model.read(is, null, format);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	public void importImplementation(String id, String content) {
		try {
			files.put(id, content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	// ------- GET ENTITIES ---------//

	public ArrayList<QuerySolution> query(String sparql) {
		System.out.println(sparql);
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet x = qexec.execSelect();
		try {
			ArrayList<QuerySolution> r = new ArrayList<>();
			while (x.hasNext()) {
				r.add(x.next());
				System.out.println(r.get(r.size() - 1));
			}
			return r;
		} finally {
			qexec.close();
		}
	}

	QuerySolution queryOne(String sparql) {
		ArrayList<QuerySolution> r = query(sparql);
		if (r.isEmpty())
			return null;
		return r.get(0);
	}

	@Override
	public AggregatedProcess getAggregatedProcessEntity(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		AggregatedProcess aggregatedProcess = new AggregatedProcess(getProcessEntity(repository, uri));
		aggregatedProcess.setProcessFlow(getProcessFlow(repository, uri));
		aggregatedProcess.setDataFlow(getDataFlow(repository, uri));
		return aggregatedProcess;
	}

	@Override
	public String getProcessFlow(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetProcessFlow(uri))) {
			return x.getLiteral("uri").getString();
		}
		return null;
	}

	@Override
	public String getDataFlow(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetDataFlow(uri))) {
			return x.getLiteral("uri").getString();
		}
		return null;
	}

	@Override
	public ProcessBase getProcessEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		ProcessBase process = getProcessAttributes(repository, uri);
		process.setInputSlots(getProcessInputSlots(repository, uri));
		process.setOutputSlots(getProcessOutputSlots(repository, uri));
		if (getImplementationURI(repository, uri) != null)
			process.setImplementation(getProcessImplementation(repository, uri));

		return process;
	}

	@Override
	public ProcessBase getProcessAttributes(String repository, String uri)
			throws ERMRClientException, JSONParserException {

		for (QuerySolution x : query(SPARQLQuery.createQueryGetProcessAttributes(uri))) {
			// ?name ?description ?version
			ProcessBase process = new ProcessBase();
			process.setId(uri);
			process.setName(x.getLiteral("name").getString());
			process.setDescription(x.getLiteral("description").getString());
			process.setVersion(x.getLiteral("version").getString());
			return process;
		}
		throw new ERMRClientException("Process definition not found: "+uri);
	}

	@Override
	public List<InputSlot> getProcessInputSlots(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		List<InputSlot> inputSlots = new ArrayList<InputSlot>();
		for (String inputSlotURI : getInputSlotURIList(repository, uri))
			inputSlots.add(getInputSlotEntity(repository, inputSlotURI));
		return inputSlots;
	}

	@Override
	public InputSlot getInputSlotEntity(String repository, String uri) throws ERMRClientException, JSONParserException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetInputSlotEntity(uri))) {
			// ?name ?description ?type ?optional

			InputSlot inputSlot = new InputSlot();
			inputSlot.setId(uri);
			inputSlot.setName(x.getLiteral("name").getString());
			inputSlot.setDescription(x.getLiteral("description").getString());
			inputSlot.setDataType("<" + x.getResource("type").getURI() + ">");
			inputSlot.setOptional(x.getLiteral("optional").getString().equals("true"));
			return inputSlot;
		}
		return null;
	}

	@Override
	public List<OutputSlot> getProcessOutputSlots(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		List<OutputSlot> outputSlots = new ArrayList<OutputSlot>();
		for (String outputSlotURI : getOutputSlotURIList(repository, uri))
			outputSlots.add(getOutputSlotEntity(repository, outputSlotURI));
		return outputSlots;
	}

	@Override
	public OutputSlot getOutputSlotEntity(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetOutputSlotEntity(uri))) {
			// ?name ?description ?type

			OutputSlot outputSlot = new OutputSlot();
			outputSlot.setId(uri);
			outputSlot.setName(x.getLiteral("name").getString());
			outputSlot.setDescription(x.getLiteral("description").getString());
			outputSlot.setDataType("<"+x.getResource("type").getURI()+">");
			return outputSlot;
		}
		return null;
	}

	@Override
	public Implementation getProcessImplementation(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		return getImplementationEntity(repository, getImplementationURI(repository, uri));
	}

	@Override
	public Implementation getImplementationEntity(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetImplementationEntity(uri))) {
			// ?version ?type ?location ?checksum

			Implementation implementation = new Implementation();
			implementation.setId(uri);
			implementation.setVersion(x.getLiteral("version").getString());
			implementation.setImplementationType(x.getLiteral("type").getString());
			implementation.setLocation(x.getLiteral("location").getString());
			implementation.setChecksum(x.getLiteral("checksum").getString());
			return implementation;
		}
		
		throw new ERMRClientException("Implementation entity not found:" + uri);
	}

	@Override
	public InputStream getProcessImplementationFile(String repository, String uri)
			throws ERMRClientException, JSONParserException {
		return getImplementationFile(getProcessImplementation(repository, uri).getLocation());
	}

	@Override
	public InputStream getImplementationFile(String uri) throws ERMRClientException, JSONParserException {
		if (files.containsKey(uri)) {
			return new ByteArrayInputStream(files.get(uri));
		} else {
			throw new ERMRClientException("Missing implementation: " + uri);
		}
	}

	// ---------- GET URIs ------------//

	@Override
	public List<String> getInputSlotURIList(String repository, String uri) throws ERMRClientException {
		List<String> uriList = new ArrayList<String>();
		for (QuerySolution x : query(SPARQLQuery.createQueryGetInputSlotURIList(uri))) {
			uriList.add("<" + x.getResource("inputs").getURI() + ">");
		}
		return uriList;
	}

	@Override
	public List<String> getOutputSlotURIList(String repository, String uri) throws ERMRClientException {
		List<String> uriList = new ArrayList<String>();
		for (QuerySolution x : query(SPARQLQuery.createQueryGetOutputSlotURIList(uri))) {
			// ?outputs
			uriList.add("<" + x.getResource("outputs").getURI() + ">");
		}
		return uriList;
	}

	@Override
	public String getImplementationURI(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetImplementationURI(uri))) {
			return "<" + x.getResource("uri").getURI() + ">";
		}
		return null;
	}

	@Override
	public String getParentEntityURI(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetParentEntityURI(uri))) {
			return "<" + x.getResource("uri").getURI() + ">";
		}
		return null;
	}

	public boolean isAggregatedProcess(String repository, String uri) throws ERMRClientException {
		return getEntityTypeURI(repository, uri).contains("AggregatedProcess");
	}

	@Override
	public String getEntityTypeURI(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetEntityTypeURI(uri))) {
			return "<" + x.getResource("uri").getURI() + ">";
		}
		return null;
	}

	@Override
	public String getSlotDataTypeURI(String repository, String uri) throws ERMRClientException {
		for (QuerySolution x : query(SPARQLQuery.createQueryGetSlotDataTypeURI(uri))) {
			return "<" + x.getResource("uri").getURI() + ">";
		}
		return null;
	}

	// ---------- CHECK FUNCTIONS ------------//

	@Override
	public boolean isSubclass(String repository, String parentClass, String childClass) throws ERMRClientException {
		while (childClass != null) {
			if (childClass.equals(parentClass))
				return true;
			childClass = getParentEntityURI(repository, childClass);
		}
		return false;
	}

	@Override
	public ERMRClientAPI getClient() {
		return null;
	}

	public Model getModel() {
		return model;
	}

}
