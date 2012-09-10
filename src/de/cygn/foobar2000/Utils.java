package de.cygn.foobar2000;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Utils {

	static int MAX_STRING_LENGTH = 1000000;

	public static String readStringAtPointer(ByteBuffer bb, int string_table_offset) throws IOException {
		int offset = bb.getInt();
		int old_position = bb.position();
		bb.position(string_table_offset + offset);
		String s = readNullTerminatedString(bb);
		bb.position(old_position);
		return s;
	}

	/**
	 * true if string empty or null
	 */
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
		if (len > MAX_STRING_LENGTH) {
			throw new IOException(String.format("string length too big: %d. Position: %d", len, bb.position() - 4));
		}
		byte[] b = new byte[len];
		bb.get(b, 0, len);
		String result = new String(b, "UTF-8");
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

	public static String formatIntoHHMMSS(int secsIn) {
		int hours = secsIn / 3600;
		int remainder = secsIn % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		String result = "";
		if (hours>0)
			result = String.valueOf(hours) + ":";
		if (hours==0)
			result = String.valueOf(minutes) + ":";
		else
			result += ((minutes < 10 ? "0" : "") + minutes + ":");
		if (result.length()==0)
			result = String.valueOf(seconds);
		else
			result += ((seconds < 10 ? "0" : "") + seconds);
		return result;
	}

	public static String readNullTerminatedString(ByteBuffer bb) throws IOException {
		int start = bb.position();
		int count = 0;
		while (bb.get() != 0) {
			count++;
		}
		int current_position = bb.position();
		int len = Math.min(count, MAX_STRING_LENGTH);
		byte[] b = new byte[len];
		bb.position(start);
		bb.get(b);
		bb.position(current_position);
		String result = new String(b, "UTF-8");
		return result;
	}
}
