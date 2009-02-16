package org.ovirt.ec2.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;

import junit.framework.TestCase;

public class RSAKeyWriterTest extends TestCase {

	private KeyPair keyPair;

	public void setUp() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		keyPair = generator.generateKeyPair();
	}

	public void tearDown() throws Exception {

	}

	public void testWritePrivate() throws Exception {
		RSAKeyWriter writer = new RSAKeyWriter();
		String base64private = writer.writePrivateKey((RSAPrivateCrtKey) keyPair.getPrivate());
		//System.err.println(base64private);
		
		String base64public = writer.writePublicKey((RSAPublicKey) keyPair.getPublic());
		//System.err.println(base64public);
	}

}
