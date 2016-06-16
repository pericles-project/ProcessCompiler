package eu.pericles.processcompiler.testutils;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RegenerateFilesUUIDs {
	
	static String[] files = {};//{"ExtractMD.bpmn2", "GetMD.bpmn2", "UpdateMD.bpmn2", "StoreMD.bpmn2"};
	private String path;
	
	public RegenerateFilesUUIDs(String file) {
		this.path = "src/test/resources/narrative2/" + file;
	}
	
	@Parameters(name="{0}")
    public static Collection<String> files() {
        return Arrays.asList(files);
    }

	@Test
	public void regenerateFilesUUIDs() {
		try {
			Utils.replaceUUIDsInFile(path);
		} catch (IOException e) {
			fail("regenerateFilesUUIDs() " + e.getMessage());
		}
	}

}
