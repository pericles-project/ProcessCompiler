package eu.pericles.processcompiler.testutils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

}
