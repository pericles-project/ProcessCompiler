package eu.pericles.processcompiler.testutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

public class Utils {

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
		String content = readFile(path);
		Map<String, String> map = getUUIDMap(content);
		content = replaceUUIDs(content, map);
		writeFile(path,content);
	}
	
	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static void writeFile(String path, String content) throws IOException {
		byte[] encoded = content.getBytes();
		Files.write(Paths.get(path), encoded, StandardOpenOption.WRITE);
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
	
	public static void writeInputStream(InputStream is, String outputFile) throws IOException {
		OutputStream os = new FileOutputStream(new File(outputFile));
		IOUtils.copy(is,os);
		is.close();
		os.close();
	}

}
