package org.ovirt.ec2.asn1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class BEREncoderTest extends TestCase {

	public void setUp() throws Exception {

	}

	public void tearDown() throws Exception {

	}

	public void testEncodeLength() throws Exception {
		BEREncoder encoder = new BEREncoder();

		byte[] bytes = encoder.encodeLength(43981);
		BERDecoder decoder = new BERDecoder(bytes);

		assertEquals(43981, decoder.decodeLength());
	}

	public void testEncodeInteger() throws Exception {
		BigInteger integer = new BigInteger("420420420420420");

		BEREncoder encoder = new BEREncoder();
		byte[] bytes = encoder.encode(integer);

		BERDecoder decoder = new BERDecoder(bytes);

		BigInteger integerOut = (BigInteger) decoder.decode();

		assertEquals(integer, integerOut);

	}

	public void testEncodeSequence() throws Exception {
		List<Object> sequence = new ArrayList<Object>();

		sequence.add(new BigInteger("22"));
		sequence.add(new BigInteger("42"));
		sequence.add(new BigInteger("420"));
		sequence.add(new BigInteger("420000"));

		BEREncoder encoder = new BEREncoder();
		byte[] bytes = encoder.encode(sequence);

		BERDecoder decoder = new BERDecoder(bytes);
		List<?> sequenceOut = (List<?>) decoder.decode();

		assertEquals(sequence.size(), sequenceOut.size());

		for (int i = 0; i < sequence.size(); ++i) {
			assertEquals(sequence.get(i), sequenceOut.get(i));
		}
	}

}
