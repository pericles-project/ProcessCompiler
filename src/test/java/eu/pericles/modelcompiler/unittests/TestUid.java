package eu.pericles.modelcompiler.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.pericles.modelcompiler.common.Uid;

public class TestUid {
	
	@Test
	public void createEmptyUid() {
		Uid uid = new Uid();
		assertTrue(uid.isValid());
	}
	@Test
	public void setUidWithRightValue() {
		Uid uid = new Uid();		
		String value = "6F810F99-058E-4EAC-9B9C-2D473301CDC3";
		
		assertTrue(uid.checkAndSetUid(value));
	}
	@Test
	public void createUidWithWrongValue() {
		Uid uid = new Uid();
		String value = "F810F99-058E-4EAC-9B9C-2D473301CDC3";
		
		assertFalse(uid.checkAndSetUid(value));
	}
}
