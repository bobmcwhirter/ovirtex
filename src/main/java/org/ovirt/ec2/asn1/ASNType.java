package org.ovirt.ec2.asn1;

import java.util.HashMap;
import java.util.Map;

public class ASNType {
	private static Map<Integer,ASNType> typeMap = new HashMap<Integer,ASNType>();
	
	//public static ASNType EOC          = new ASNType("EOC", 0x00);
	//public static ASNType BOOLEAN      = new ASNType("BOOLEAN", 0x01);
	public static ASNType INTEGER      = new ASNType("INTEGER", 0x02);
	//public static ASNType BIT_STRING   = new ASNType("BIT STRING", 0x03);
	//public static ASNType OCTET_STRING = new ASNType("OCTECT STRING", 0x04);
	//public static ASNType NULL         = new ASNType("NULL", 0x05);
	
	public static ASNType SEQUENCE = new ASNType("SEQUENCE", 0x10);
	
	private String type;
	private int flag;

	ASNType(String type, int flag) {
		this.type = type;
		this.flag = flag;
		typeMap.put( flag, this );
	}
	
	public String toString() {
		return this.type + " (0x" + Integer.toHexString( this.flag ) + ")";
	}
	
	public int getFlag() {
		return this.flag;
	}
	
	private static int CONSTRUCTED = Integer.parseInt( "00100000", 2 );
	
	public byte getByte() {
		if ( this == ASNType.SEQUENCE ) {
			return (byte) (this.flag + CONSTRUCTED);
		} else {
			return (byte) this.flag;
		}
		
		//return (byte) this.flag;
	}

	public static ASNType decode(int b) {
		int flag = FLAG_MASK & b;
		
		//System.err.println( "---> " + Integer.toHexString( flag ) );
		return typeMap.get(flag);
	}
	
	private static final int FLAG_MASK = Integer.parseInt( "00011111", 2);
	
}
