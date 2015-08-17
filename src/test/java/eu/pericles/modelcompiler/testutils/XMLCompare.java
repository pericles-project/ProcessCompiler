package eu.pericles.modelcompiler.testutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Utility class to compare two XML structures. The comparison allows attributes to be different as long as they are consistent.
 */
public class XMLCompare {
	private Node left;
	private Node right;
	private Map<String, String> mapping = new HashMap<>();
	
	public static void assertEquals(String a, String b) {
		new XMLCompare(a,  b).assertEqual();
	}
	
	public static void assertEquals(InputStream a, InputStream b) {
		new XMLCompare(a,  b).assertEqual();
	}

	public XMLCompare(InputStream a, InputStream b){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			left = factory.newDocumentBuilder().parse(a).getDocumentElement();
			right = factory.newDocumentBuilder().parse(b).getDocumentElement();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			Assert.fail(e.getMessage());
		}
	}

	public XMLCompare(String a, String b){
		this(new ByteArrayInputStream(a.getBytes()), new ByteArrayInputStream(
				b.getBytes()));
	}

	private List<String> getPath() {
		List<String> path = new LinkedList<String>();
		Node node = left;
		while (node != null) {
			path.add(0, node.getNodeName());
			node = node.getParentNode();
		}
		return path;
	}

	private String msg(String msg) throws TransformerException {
		return msg + "\nPath: " + getPath() + "\n";
	}

	public void assertEqual() {
		try {
			while (left != null) {
				Assert.assertNotNull(msg("Missing node"), right);
				Assert.assertEquals(msg("Different element"), left.getNodeName(), right.getNodeName());

				if (left instanceof Element) {
					Node aa, bb;
					for (int i = 0; i < left.getAttributes().getLength(); i++) {
						aa = left.getAttributes().item(i);
						bb = right.getAttributes().getNamedItem(aa.getNodeName());
						Assert.assertNotNull(msg("Missing attribute:" + aa.getNodeName()), bb);
						compareAttribute(aa.getNodeName(), aa.getNodeValue(),
								bb.getNodeValue());
					}
				} else if (left instanceof CharacterData) {
					compareAttribute(left.getNodeName(), left.getNodeValue().trim(), right.getNodeValue().trim());
					//Assert.assertEquals(msg("Value difference"), left.getNodeValue().trim(), right.getNodeValue().trim());
				} else {
					Assert.assertEquals(msg("Value difference"), left.getNodeValue(), right.getNodeValue());
				}

				// Traverse the tree depth first
				if (left.hasChildNodes()) {
					left = left.getFirstChild();
					right = right.getFirstChild();
					continue;
				} else if (left.getNextSibling() != null) {
					left = left.getNextSibling();
					right = right.getNextSibling();
					continue;
				} else {
					while (left != null) {
						if (left.getNextSibling() != null) {
							left = left.getNextSibling();
							right = right.getNextSibling();
							break;
						}
						left = left.getParentNode();
						right = right.getParentNode();
					}
				}
			}
		} catch (DOMException | TransformerException e) {
			Assert.fail(e.getMessage());
		}
	}

	private void compareAttribute(String name, String aValue, String bValue) throws TransformerException {
		System.out.println(name + ':' + aValue + ':' + bValue);
		if (mapping.containsKey(name + ':' + aValue)) {
			Assert.assertEquals(msg("Mismatch in mapped value"), mapping.get(name + ':' + aValue), bValue);
		} else {
			Assert.assertNotNull(msg("Missing attribute"), bValue);
			mapping.put(name + ':' + aValue, bValue);
		}
	}
}
