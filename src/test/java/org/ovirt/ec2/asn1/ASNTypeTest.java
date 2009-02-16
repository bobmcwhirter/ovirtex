package org.ovirt.ec2.asn1;

import junit.framework.TestCase;

public class ASNTypeTest extends TestCase {
	
	public void setUp() {
		
	}
	
	public void tearDown() {
		
	}
	
	public void testDecode() throws Exception {
		assertEquals( ASNType.decode( (byte) 48 ), ASNType.SEQUENCE );
	}
}
