package eu.pericles.processcompiler.testutils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import eu.pericles.processcompiler.ecosystem.Fixity;
import eu.pericles.processcompiler.ecosystem.Implementation;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;

public class Utils {
	
	public static Implementation createImplementation(List<String> values) {
		Implementation implementation = new Implementation();
		implementation.setId(values.get(0));
		implementation.setVersion(values.get(1));
		implementation.setType(values.get(2));
		implementation.setLocation(values.get(3));
		implementation.setFixity(new Fixity());
		implementation.getFixity().setAlgorithm(values.get(4));
		implementation.getFixity().setChecksum(values.get(5));
		
		return implementation;
	}
	
	public static InputSlot createInputSlot(List<String> values, boolean optional) {
		InputSlot inputSlot = new InputSlot();
		inputSlot.setId(values.get(0));
		inputSlot.setName(values.get(1));
		inputSlot.setDescription(values.get(2));
		inputSlot.setType(values.get(3));
		inputSlot.setOptional(optional);
		
		return inputSlot;
	}
	
	public static OutputSlot createOutputSlot(List<String> values) {
		OutputSlot outputSlot = new OutputSlot();
		outputSlot.setId(values.get(0));
		outputSlot.setName(values.get(1));
		outputSlot.setDescription(values.get(2));
		outputSlot.setType(values.get(3));
		
		return outputSlot;
	}

	public static boolean fileContentEquals(String outputFileName, String testFileName) {
		try {
			String a = FileUtils.readFileToString(new File(testFileName));
			String b = FileUtils.readFileToString(new File(outputFileName));
			XMLCompare.assertEquals(a, b);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

		return true;
	}
	
	public static void replaceUUIDsInFile(String path) throws IOException {
		String content = FileUtils.readFileToString(new File(path));
		Map<String, String> map = getUUIDMap(content);
		content = replaceUUIDs(content, map);
		FileUtils.writeStringToFile(new File(path), content);
	}
	
	public static Map<String, String> getUUIDMap(String sequence) {
		Map<String, String> map = new HashMap<String, String>();
		Pattern pattern = Pattern.compile("_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
		Matcher matcher = pattern.matcher(sequence);
		while (matcher.find()) {
			String matchedSubsequence = matcher.group();
			if (!map.containsKey(matchedSubsequence))
				map.put(matchedSubsequence, "_" + UUID.randomUUID().toString());
		}
		return map;
	}
	
	public static String replaceUUIDs(String sequence, Map<String, String> map) {
		String replacedSequence = sequence;
		for (String oldUUID : map.keySet()) {
			Pattern pattern = Pattern.compile(oldUUID);
			Matcher matcher = pattern.matcher(replacedSequence);
			replacedSequence = matcher.replaceAll(map.get(oldUUID));
		}
		return replacedSequence;
	}

}
