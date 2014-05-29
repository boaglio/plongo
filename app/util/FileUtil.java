package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Log;
import type.Verbosity;

public class FileUtil {

	public static List<Log> convertFileToLogList(File file) {
		List<Log> listLog = new ArrayList<Log>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder sb = new StringBuilder();
			while ( (line = br.readLine()) != null) {
				if (StringUtil.hasDateInfo(line)) {
					Log log = convertLineToLog(sb.toString());
					sb = new StringBuilder();
					if (log != null && log.content != null) {
						listLog.add(log);
					}
				}

				sb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listLog;
	}

	private static Log convertLineToLog(String line) {
		if (line == null || line.length() == 0) { return null; }
		Log log = new Log();
		if (StringUtil.hasDateInfo(line)) {
			log.timestamp = line.substring(0,19);
			log.content = line.substring(19);
		} else {
			log.timestamp = "";
			log.content = line;
		}
		Verbosity v = StringUtil.getVerbosity(line);
		log.verbosity = v.name();

		return log;
	}

}
