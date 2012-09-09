package de.cygn.foobar2000;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Utils {
	static int MAX_STRING_LENGTH=1000000;
	public static String readStringAtPointer(ByteBuffer bb, int string_table_offset) throws IOException {
		int offset = bb.getInt();
		int old_position = bb.position();
		bb.position(string_table_offset + offset);
		String s = readNullTerminatedString(bb);
		bb.position(old_position);
		return s;
	}

	/** true if string empty or null */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static String cleanFoldername(String foldername) {
		if (foldername.startsWith("file://")) {
			foldername = foldername.substring(7);
		}
		return foldername;
	}

	/* first reads length of string from ByteBuffer, then reads string of that length */
	public static String getString(ByteBuffer bb) throws IOException {
		int len = bb.getInt();
		if (len>MAX_STRING_LENGTH)
			throw new IOException(String.format("string length too big: %d. Position: %d",len,bb.position()-4));
		byte[] b = new byte[len];
		bb.get(b, 0, len);
		String result = new String(b);
		return result;
	}

	/**
	 * Convert windows 64-bit FILETIME to Java's 64-bit date.
	 *
	 * @param fileTime The FILETIME to convert.
	 * @return the Java-time equivalent.
	 */
	public static long fileTimeToMillis(long fileTime) {
		return (fileTime / 10000L) - 11644473600000L;
	}
	
	public static String readNullTerminatedString(ByteBuffer bb) throws IOException {
		StringBuilder sb = new StringBuilder(32);
		while (bb.hasRemaining()) // safer  
		{  
			char c = (char)bb.get();  
			if (c == '\0') break;  
			sb.append(c);  
		}  
		return sb.toString();
	}
}
