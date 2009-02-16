package org.ovirt.ec2.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import org.jboss.util.Base64;
import org.ovirt.ec2.asn1.BEREncoder;

public class RSAKeyWriter {

	public String writePrivateKey(RSAPrivateCrtKey privateKey) {
		List<BigInteger> sequence = new ArrayList<BigInteger>();
		
		sequence.add( BigInteger.ZERO );
		sequence.add( privateKey.getModulus() );
		sequence.add( privateKey.getPublicExponent() );
		sequence.add( privateKey.getPrivateExponent() );
		sequence.add( privateKey.getPrimeP() );
		sequence.add( privateKey.getPrimeQ() );
		sequence.add( privateKey.getPrimeExponentP() );
		sequence.add( privateKey.getPrimeExponentQ() );
		sequence.add( privateKey.getCrtCoefficient() );
		
		BEREncoder encoder = new BEREncoder();
		byte[] bytes =  encoder.encode( sequence );
		String base64 = Base64.encodeBytes( bytes );
		String pem = "-----BEGIN RSA PRIVATE KEY-----\n" + base64 + "\n-----END RSA PRIVATE KEY-----\n";
		return pem;
	}

	public String writePublicKey(RSAPublicKey publicKey) {
		
		byte[] prologBytes = new byte[] { 's', 's', 'h', '-', 'r', 's', 'a' };
		byte[] publicExpBytes = publicKey.getPublicExponent().toByteArray();
		byte[] modulusBytes = publicKey.getModulus().toByteArray();
		
		byte[] bytes = new byte[ 4 + prologBytes.length + 4 + publicExpBytes.length + 4 + modulusBytes.length ];
		
		int cur = 0;
		
		cur = copy( bytes, cur, encodeLength( prologBytes.length) );
		cur = copy( bytes, cur, prologBytes );
		cur = copy( bytes, cur, encodeLength( publicExpBytes.length) );
		cur = copy( bytes, cur, publicExpBytes );
		cur = copy( bytes, cur, encodeLength( modulusBytes.length) );
		cur = copy( bytes, cur, modulusBytes );
		
		return "ssh-rsa " + Base64.encodeBytes( bytes, Base64.DONT_BREAK_LINES );
	}
	
	public String writePublicKeyFingerprint(RSAPublicKey publicKey) throws NoSuchAlgorithmException {
		
		byte[] prologBytes = new byte[] { 's', 's', 'h', '-', 'r', 's', 'a' };
		byte[] modulusBytes = publicKey.getModulus().toByteArray();
		byte[] publicExpBytes = publicKey.getPublicExponent().toByteArray();
		
		byte[] bytes = new byte[ 4 + prologBytes.length + 4 + publicExpBytes.length + 4 + modulusBytes.length ];
		
		int cur = 0;
		cur = copy( bytes, cur, encodeLength( prologBytes.length) );
		cur = copy( bytes, cur, prologBytes );
		cur = copy( bytes, cur, encodeLength( publicExpBytes.length) );
		cur = copy( bytes, cur, publicExpBytes );
		cur = copy( bytes, cur, encodeLength( modulusBytes.length) );
		cur = copy( bytes, cur, modulusBytes );
		
		MessageDigest digest = MessageDigest.getInstance("MD5" );
		digest.update( bytes );
		
		byte[] hash = digest.digest();
		
		StringBuilder builder = new StringBuilder();
		
		boolean first = true;
		for ( int i = 0 ; i < hash.length ; ++i ) {
			int octet = hash[i];
			if ( octet < 0 ) {
				octet = 128 + Math.abs( octet );
			}
			if ( first ) {
				first = false;
			} else {
				builder.append( ":" );
			}
			builder.append( Integer.toHexString( octet ) );
		}
		
		return builder.toString();
	}
	
	int copy(byte[] dest, int offset, byte[] src) {
		int cur = offset;
		for ( int i = 0; i < src.length ; ++i ) {
			dest[cur] = src[i];
			++cur;
		}
		
		return cur;
	}
	
	byte[] encodeLength(int len) {
		byte[] lenBytes = new byte[4];
		
		for ( int i = 3; i >= 0 ; --i ) {
			int octet = ( len & 0xFF);
			lenBytes[i] = (byte) octet;
			len = len >> 8;
		}
		
		return lenBytes;
	}
	

	
	

}
