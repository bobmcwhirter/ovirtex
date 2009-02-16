package org.ovirt.ec2.asn1;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BERDecoder {

	private byte[] bytes;
	private int cur;
	private int len;

	public BERDecoder(byte[] bytes) {
		this(bytes, 0, -1);
	}

	public BERDecoder(byte[] bytes, int offset, int len) {
		this.bytes = bytes;
		this.cur = offset;
		if (len < 0) {
			this.len = this.bytes.length;
		} else {
			this.len = len;
		}
	}

	public Object decode() throws IOException {
		Object result = null;

		int b = la();

		ASNType type = ASNType.decode(b);

		if (type == null) {
			throw new IOException("unable to determine type");
		}

		consume();
		int len = decodeLength();

		if (type == ASNType.SEQUENCE) {
			return decodeSequence(len);
		} else if (type == ASNType.INTEGER) {
			return decodeInteger(len);
		}

		return result;
	}

	public List<Object> decodeSequence(int len) throws IOException {
		int stop = this.cur + len;

		List<Object> sequence = new ArrayList<Object>();

		while (this.cur < stop) {
			Object o = decode();
			sequence.add(o);
		}

		return sequence;
	}

	public BigInteger decodeInteger(int len) throws IOException {
		BigInteger integer = new BigInteger( "0" );
		for ( int i = 0 ; i < len ; ++i ) {
			integer = integer.shiftLeft(8);
			integer = integer.add( new BigInteger( consume() + "" ) );
		}
		
		return integer;
	}

	public byte[] decodeOctetString(int len) {
		return null;
	}

	private static int LENGTH_LONG_FORM_MASK = Integer.parseInt("10000000", 2);

	int decodeLength() throws IOException {
		int b = la();
		int len = 0;
		if ((b & LENGTH_LONG_FORM_MASK) != 0) {
			int lengthLen = b ^ LENGTH_LONG_FORM_MASK;
			consume();
			len = decodeLengthLongForm(lengthLen);
		} else {
			consume();
			len = b;
		}
		return len;
	}

	int decodeLengthLongForm(int lengthLen) throws IOException {
		int len = 0;
		for (int i = 0; i < (lengthLen); ++i) {
			int octet = consume();
			len <<= 8;
			len += octet;
		}
		return len;
	}

	// ----------------------------------------

	private int la() throws IOException {
		if (this.cur > this.len) {
			throw new IOException("end of stream encountered: " + this.cur + " of " + this.len);
		}
		byte b = this.bytes[this.cur];
		int i = b;

		if (b < 0) {
			i = 256 + b;
		}

		return i;
	}

	private int consume() throws IOException {
		int la = la();
		++this.cur;
		return la;
	}

	private byte[] consumeBytes(int len) {
		byte[] b = new byte[len];
		for (int i = 0; i < len; ++i) {
			b[0] = this.bytes[this.cur];
			++this.cur;
		}
		
		return b;
	}

}
