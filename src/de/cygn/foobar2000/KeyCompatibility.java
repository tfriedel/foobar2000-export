/*
 */
package de.cygn.foobar2000;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thomas Friedel
 */
public class KeyCompatibility {

	static final Map<String, Float> key_to_hz = initialise_key_to_hz();
	static final double semitone = Math.pow(2.0, 1.0 / 12.0);

	static final Map<String, Float> initialise_key_to_hz() {
		HashMap<String, Float> map = new HashMap<String, Float>();
		map.put("11B", 440.0f);  // A
		map.put("6B", 466.16f);  // Bb / A#
		map.put("1B", 493.88f);  // B
		map.put("8B", 532.25f);  // C
		map.put("3B", 554.37f);  // Db / C#
		map.put("10B", 587.33f); // D
		map.put("5B", 622.25f);  // Eb / D#
		map.put("12B", 659.26f); // E
		map.put("7B", 698.46f);  // F
		map.put("2B", 739.99f);  // Gb / F#
		map.put("9B", 783.99f);  // G
		map.put("4B", 830.61f);  // Ab / G#

		map.put("8A", 440.0f);  // A
		map.put("3A", 466.16f);  // Bb / A#
		map.put("10A", 493.88f);  // B
		map.put("5A", 532.25f);  // C
		map.put("12A", 554.37f);  // Db / C#
		map.put("7A", 587.33f); // D
		map.put("2A", 622.25f);  // Eb / D#
		map.put("9A", 659.26f); // E
		map.put("4A", 698.46f);  // F
		map.put("11A", 739.99f);  // Gb / F#
		map.put("6A", 783.99f);  // G
		map.put("1A", 830.61f);  // Ab / G#

		return map;
	}

	static {
		assert compatible("8A", "8A", 120.0f, 120.0f);
		assert compatible("8A", "7A", 120.0f, 120.0f);
		assert compatible("7A", "8A", 120.0f, 120.0f);
		assert compatible("7A", "12A", 120.0f, 126.0f);
		assert compatible("8A", "9A", 120.0f, 120.0f);
		assert compatible("8A", "8B", 120.0f, 120.0f);
		assert compatible("8B", "8A", 120.0f, 120.0f);
		assert compatible("10A", "9A", 116.0f, 116.0f);
		assert compatible("10B", "10B", 150.0f, 150.0f);
		assert compatible("10B", "10A", 150.0f, 150.0f);
		assert compatible("10B", "9B", 150.0f, 150.0f);
		assert compatible("10B", "11B", 150.0f, 150.0f);
		assert compatible("11B", "11B", 125.0f, 126.0f);
		assert compatible("11B", "10B", 125.0f, 126.0f);
		assert compatible("11B", "12B", 125.0f, 126.0f);
		assert compatible("11B", "11A", 125.0f, 126.0f);
		assert !compatible("11B", "8A", 125.0f, 126.0f);
	}

	/**
	 * calculate frequency of key if track is played at 120 BPM
	 */
	public static float normalizedHz(float bpm, float hz) {
		return hz * bpm / 120.0f;
	}

	/**
	 * calculate frequency of key if track is played at 120 BPM
	 */
	public static float normalizedHz(float bpm, String camelotKey) {
		return keyToHz(camelotKey) * bpm / 120.0f;
	}

	static float keyToHz(String camelotKey) {
		return key_to_hz.get(camelotKey);
	}

	/**
	 * calculate mirex score between keys a and b (camelot notation)
	 *
	 * MIREX score weightings Key relation Score Exact match (tonic) 1.0 Perfect
	 * Fifth (dominant) 0.5 Perfect fourth (subdominant) 0.5 Relative
	 * major/minor 0.3 Parallel major/minor 0.2
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static float calculateMirexScore(String a, String b) {
		if ((a == null) || (b == null)) {
			return 0;
		}

		char a_gender = a.toUpperCase().charAt(a.length() - 1);
		char b_gender = b.toUpperCase().charAt(b.length() - 1);
		if ((a_gender != 'A' && a_gender != 'B')
				|| (b_gender != 'A' && b_gender != 'B')) {
			return 0;
		}
		assert a.length() == 2 || a.length() == 3;
		assert b.length() == 2 || b.length() == 3;
		if (a.equals(b)) {
			return 1.0f;
		}

		int a_nr = Integer.valueOf(a.substring(0, a.length() - 1));
		int b_nr = Integer.valueOf(b.substring(0, b.length() - 1));

		if (a_nr == b_nr) {
			return 0.3f;
		} else if (((a_nr - b_nr) % 12 == 1 || (b_nr - a_nr) % 12 == 1) && a_gender == b_gender) {
			return 0.5f;
		} else if ((a_gender == 'A' && b_gender == 'B' && ((a_nr + 3) % 12) == (b_nr % 12))
				|| (a_gender == 'B' && b_gender == 'A' && (a_nr % 12) == ((b_nr + 3) % 12))) {
			return 0.2f;
		} else {
			return 0;
		}
	}

	public static boolean compatible(String a, String b) {
		return (calculateMirexScore(a, b) > 0);
	}

	public static boolean inAllowedRange(float hz_a, float hz_b) {
		try {
			float ratio = Math.max(hz_a, hz_b) / Math.min(hz_a, hz_b);
			if (ratio < 1.02 || (ratio / 2 < 1.02 && ratio / 2 >= 1.0)) {
				return true;
			} else {
				return false;
			}
		} catch (ArithmeticException e) {
			return false;
		}
	}

	public static float transpose(float hz, int semitones) {
		return (float) (hz * Math.pow(semitone, semitones));
	}

	public static boolean compatible(String a, String b, float bpm_a, float bpm_b) {
		if ((a == null) || (b == null)) {
			return false;
		}
		char a_gender = a.toUpperCase().charAt(a.length() - 1);
		char b_gender = b.toUpperCase().charAt(b.length() - 1);
		if ((a_gender != 'A' && a_gender != 'B')
				|| (b_gender != 'A' && b_gender != 'B')) {
			return false;
		}
		assert a.length() == 2 || a.length() == 3;
		assert b.length() == 2 || b.length() == 3;
		float a_hz;
		float b_hz;
		try {
			a_hz = normalizedHz(bpm_a, a);
			b_hz = normalizedHz(bpm_b, b);
		} catch (NullPointerException e) {
			return false;
		}

		int a_nr = Integer.valueOf(a.substring(0, a.length() - 1));
		int b_nr = Integer.valueOf(b.substring(0, b.length() - 1));
		if (inAllowedRange(a_hz, b_hz) && a_gender == b_gender) {
			return true;
		}

		// fifth dominant, 4th subdominant
		if ((a_gender == b_gender)
				&& // fifth up
				(inAllowedRange(a_hz, transpose(b_hz, 5))
				|| // fifth down
				inAllowedRange(a_hz, transpose(b_hz, -5)))) {
			return true;
			// relative keys 
		} else if ((a_gender == 'A' && b_gender == 'B'
				&& inAllowedRange(transpose(a_hz, 3), b_hz))
				|| (a_gender == 'B' && b_gender == 'A'
				&& inAllowedRange(a_hz, transpose(b_hz, 3)))) {
			return true;
		} else {
			return false;
		}
		//((a_nr + 3) % 12) == (b_nr % 12))
		//|| (a_gender == 'B' && b_gender == 'A' && (a_nr % 12) == ((b_nr + 3) % 12))) {
		// Furthermore, a song will be reasonably harmonically compatible if it is in:
		// The Sub-Dominant key of the Relative Major/Minor key
		// The Dominant key of the Relative Major/Minor key
		// http://www.mixshare.com/wiki/doku.php?id=mixing_harmonically
		// @todo add the option to include this

	}
}
