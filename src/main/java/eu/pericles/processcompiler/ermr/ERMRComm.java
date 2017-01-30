package eu.pericles.processcompiler.ermr;

import java.io.InputStream;
import java.util.List;

import eu.pericles.processcompiler.ecosystem.AggregatedProcess;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.exceptions.ERMRClientException;
import eu.pericles.processcompiler.exceptions.JSONParserException;

public interface ERMRComm {

	ERMRClientAPI getClient();

	AggregatedProcess getAggregatedProcessEntity(String repository, String uri)
			throws ERMRClientException, JSONParserException;

	String getProcessFlow(String repository, String uri) throws ERMRClientException;

	String getDataFlow(String repository, String uri) throws ERMRClientException;

	ProcessBase getProcessEntity(String repository, String uri) throws ERMRClientException, JSONParserException;

	ProcessBase getProcessAttributes(String repository, String uri) throws ERMRClientException, JSONParserException;

	List<InputSlot> getProcessInputSlots(String repository, String uri) throws ERMRClientException, JSONParserException;

	InputSlot getInputSlotEntity(String repository, String uri) throws ERMRClientException, JSONParserException;

	List<OutputSlot> getProcessOutputSlots(String repository, String uri)
			throws ERMRClientException, JSONParserException;

	OutputSlot getOutputSlotEntity(String repository, String uri) throws ERMRClientException, JSONParserException;

	Implementation getProcessImplementation(String repository, String uri)
			throws ERMRClientException, JSONParserException;

	Implementation getImplementationEntity(String repository, String uri)
			throws ERMRClientException, JSONParserException;

	InputStream getProcessImplementationFile(String repository, String uri)
			throws ERMRClientException, JSONParserException;

	InputStream getImplementationFile(String uri) throws ERMRClientException, JSONParserException;

	List<String> getInputSlotURIList(String repository, String uri) throws ERMRClientException;

	List<String> getOutputSlotURIList(String repository, String uri) throws ERMRClientException;

	String getImplementationURI(String repository, String uri) throws ERMRClientException;

	String getParentEntityURI(String repository, String uri) throws ERMRClientException;

	String getEntityTypeURI(String repository, String uri) throws ERMRClientException;

	String getSlotDataTypeURI(String repository, String uri) throws ERMRClientException;

	boolean isSubclass(String repository, String parentClass, String childClass) throws ERMRClientException;

	boolean isAggregatedProcess(String repository, String processId) throws ERMRClientException;
	
	boolean existsEntity(String repository, String entityId);

}