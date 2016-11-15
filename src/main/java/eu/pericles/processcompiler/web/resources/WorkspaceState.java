package eu.pericles.processcompiler.web.resources;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Heavy object used for demo only. It represents an entire workspace (files,
 * results, ...)
 */
public class WorkspaceState {
	private static SecureRandom random = new SecureRandom();

	public static class WSFile {
		public String text = "";
		public String desc = "";
		public String image;
		public boolean output = false;
	}

	public String ermrURI;
	public String repoName;
	public String name;
	public Map<String, WSFile> files = new HashMap<>();

	public WorkspaceState() {
		repoName = "Demo_" + new BigInteger(8 * 16, random).toString(16);
	}

}
