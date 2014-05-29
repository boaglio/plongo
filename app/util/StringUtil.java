package util;

import type.Verbosity;

public class StringUtil {

	public static boolean hasDateInfo(String line) {
		boolean hasDate = false;

		if (line != null && line.length() >= 19) {

			// 20/06/2011 00:19:37
			int positionFirstColon = line.indexOf(":");
			int positionSecondColon = line.indexOf(":",positionFirstColon + 1);
			if (positionFirstColon > 1 && positionSecondColon > -1 && positionSecondColon - positionFirstColon == 3) {
				hasDate = true;
			}
		}
		return hasDate;
	}

	public static Verbosity getVerbosity(String line) {

		Verbosity verbosity = Verbosity.NONE;

		if (line != null && line.length() > 10) {
			String verbosityStr = line.substring(0,10);
			for (Verbosity v : Verbosity.values()) {
				if (verbosityStr.indexOf(v.name()) > -1) {
					verbosity = v;
					break;
				}
			}
		}

		return verbosity;
	}

}
