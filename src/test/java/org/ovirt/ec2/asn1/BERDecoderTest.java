package org.ovirt.ec2.asn1;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.List;

import junit.framework.TestCase;

import org.jboss.util.Base64;

public class BERDecoderTest extends TestCase {

	public void setUp() throws Exception {

	}

	public void tearDown() throws Exception {

	}

	public void x_testDecodeLength_shortForm() throws Exception {
		byte[] bytes = new byte[] { 42 };

		BERDecoder decoder = new BERDecoder(bytes);

		int len = decoder.decodeLength();
		assertEquals(42, len);
	}

	public void x_testDecodeLength_longForm() throws Exception {
		byte[] bytes = new byte[] { (byte) 0x82, (byte) 0xAB, (byte) 0xCD };

		BERDecoder decoder = new BERDecoder(bytes);
		int len = decoder.decodeLength();
		assertEquals(43981, len);

		BEREncoder encoder = new BEREncoder();
		byte[] outBytes = encoder.encodeLength(43981);

		assertEquals(bytes.length, outBytes.length);

		for (int i = 0; i < outBytes.length; ++i) {
			assertEquals(bytes[i], outBytes[i]);
		}

	}

	public void testDecodeKey() throws Exception {
		FileInputStream in = new FileInputStream("/Users/bob/.ssh/test.key.rsa");
		byte[] base64bytes = new byte[4096];
		int len = in.read(base64bytes);
		byte[] bytes = Base64.decode(base64bytes, 0, len);
		BERDecoder decoder = new BERDecoder(bytes, 0, len);
		List<?> result = (List<?>) decoder.decode();

		BEREncoder encoder = new BEREncoder();
		byte[] encoded = encoder.encode(result);

		decoder = new BERDecoder(encoded);
		List<?> resultToo = (List<?>) decoder.decode();

		assertEquals(result.size(), resultToo.size());

		for (int i = 0; i < result.size(); ++i) {
			assertEquals(result.get(i), resultToo.get(i));
		}
	}

	public void testEdgeSize() throws Exception {
		BigInteger i = new BigInteger(
				"165182530437229536797363797101804208638311090394082583312721445707726573187326083423233357481858205250009151758408576195909826023925198973250493756557612244285780189593951367675184231156574011295394119667465571062452814886266908117939770039961102687720345464682461170389190184115741345132169635418455104040199");

		BEREncoder encoder = new BEREncoder();

		byte[] encoded = encoder.encode(i);

		BERDecoder decoder = new BERDecoder(encoded);

		BigInteger i2 = (BigInteger) decoder.decode();

		assertEquals( i, i2 );
	}

}
