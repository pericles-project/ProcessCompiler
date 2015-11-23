package eu.pericles.processcompiler.testutils;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class GenerateNewUUIDsTest {

	@Test
	public void patternMatchingTest() {
		String sequence = "<bpmn2:sequenceFlow id=\"_3737d65d-7ace-414a-8f1b-e24ce8cd8c96\" sourceRef=\"_0bd5fa0d-47c0-41dd-82c2-75f9d6b084d0\" targetRef=\"_920f24ce-a6e6-4df2-bb0f-8ca1afc6fc34\"/>";
		Pattern pattern = Pattern.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
		Matcher matcher = pattern.matcher(sequence);
		while (matcher.find()) {
			String matchedSubsequence = matcher.group();
			System.out.println(">> " + matchedSubsequence);
		}
	}

	@Test
	public void createUUIDMappingTest() {
		Map<String, String> map = new HashMap<String, String>();
		String sequence = "<bpmn2:sequenceFlow id=\"_3737d65d-7ace-414a-8f1b-e24ce8cd8c96\" sourceRef=\"_0bd5fa0d-47c0-41dd-82c2-75f9d6b084d0\" targetRef=\"_920f24ce-a6e6-4df2-bb0f-8ca1afc6fc34\"/>";
		Pattern pattern = Pattern.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
		Matcher matcher = pattern.matcher(sequence);
		while (matcher.find()) {
			String matchedSubsequence = matcher.group();
			if (!map.containsKey(matchedSubsequence))
				map.put(matchedSubsequence, "_" + UUID.randomUUID().toString());
			System.out.println(">> " + matchedSubsequence + " -> " + map.get(matchedSubsequence));
		}
	}

	@Test
	public void replaceUUIDsTest() {
		Map<String, String> map = new HashMap<String, String>();
		String sequence = "<bpmn2:sequenceFlow id=\"_3737d65d-7ace-414a-8f1b-e24ce8cd8c96\" sourceRef=\"_0bd5fa0d-47c0-41dd-82c2-75f9d6b084d0\" targetRef=\"_920f24ce-a6e6-4df2-bb0f-8ca1afc6fc34\"/>";
		Pattern pattern = Pattern.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
		Matcher matcher = pattern.matcher(sequence);
		while (matcher.find()) {
			String matchedSubsequence = matcher.group();
			if (!map.containsKey(matchedSubsequence))
				map.put(matchedSubsequence, "_" + UUID.randomUUID().toString());
			System.out.println(">> " + matchedSubsequence + " -> " + map.get(matchedSubsequence));
		}
		System.out.println("Original sequence: " + sequence);
		for (String oldUUID : map.keySet()) {
			pattern = Pattern.compile(oldUUID);
			matcher = pattern.matcher(sequence);
			sequence = matcher.replaceAll(map.get(oldUUID));
			System.out.println(">> " + sequence);
		}
	}

	@Test
	public void replaceUUIDinBPMNFileTest() {
		try {
			Utils.replaceUUIDsInFile("src/test/resources/bpmnfiles/completeprocess/Output.bpmn2");
			Utils.fileContentEquals("src/test/resources/bpmnfiles/completeprocess/Output.bpmn2", "src/test/resources/bpmnfiles/completeprocess/Test.bpmn2");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
