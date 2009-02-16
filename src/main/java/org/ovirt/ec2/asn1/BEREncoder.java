package org.ovirt.ec2.asn1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BEREncoder {

	public BEREncoder() {

	}

	public byte[] encode(BigInteger integer) {
		List<Byte> bytes = new ArrayList<Byte>();
		
		BigInteger oneByte = new BigInteger( "255" );
		
		while ( ! integer.equals( BigInteger.ZERO ) ) {
			byte octet = integer.and(oneByte).byteValue();
			integer = integer.shiftRight(8);
			bytes.add( new Byte( octet ) );
		}
		
		byte[] lenBytes = encodeLength( bytes.size() );
		
		byte[] encoded = new byte[ lenBytes.length + bytes.size() + 1 ];
		encoded[0] = ASNType.INTEGER.getByte();
		
		int cur = 1;
		
		for ( int i = 0 ; i < lenBytes.length; ++i ) {
			encoded[cur] = lenBytes[i];
			++cur;
		}
		
		Object[] dataBytes = bytes.toArray();
		
		for ( int i = dataBytes.length-1 ; i >= 0; --i ) {
			encoded[cur] = ((Byte)dataBytes[i]).byteValue();
			++cur;
		}
		
		return encoded;
	}

	public byte[] encode(List<?> sequence) {
		List<byte[]> chunks = new ArrayList<byte[]>();
		
		for ( Object o : sequence ) {
			if ( o instanceof BigInteger ) {
				chunks.add( encode( (BigInteger) o ) );
			}
		}
		
		int len = 0;
		
		for ( byte[] chunk : chunks ) {
			len += chunk.length;
		}
		
		byte[] lenBytes = encodeLength( len );
		
		byte[] bytes = new byte[ 1 + lenBytes.length + len ];
		
		bytes[0] = ASNType.SEQUENCE.getByte();
		
		int cur = 1;
		
		for ( int i = 0 ; i < lenBytes.length ; ++i ) {
			bytes[cur] = lenBytes[i];
			++cur;
		}
		
		for ( byte[] chunk : chunks ) {
			for ( int i = 0 ; i < chunk.length ; ++i ) {
				bytes[cur] = chunk[i];
				++cur;
			}
		}
		
		return bytes;
	}

	private static int LENGTH_LONG_FORM_MASK = Integer.parseInt("10000000", 2);

	protected byte[] encodeLength(final int origLen) {

		if (origLen < 128) {
			return new byte[] { (byte) origLen };
		}

		int lengthLen = 0;

		int len = origLen;

		while (len != 0) {
			len = len >>> 8;
			++lengthLen;
		}

		byte[] bytes = new byte[lengthLen + 1];

		bytes[0] = (byte) (LENGTH_LONG_FORM_MASK + lengthLen);

		len = origLen;

		for (int i = (lengthLen-1); i >= 0; --i) {
			int octet = 0xFF & len;
			len = len >>> 8;
			bytes[i+1] = (byte) octet;
		}

		return bytes;
	}

}
