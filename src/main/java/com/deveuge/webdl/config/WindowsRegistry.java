package com.deveuge.webdl.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Utility class for reading Windows registry 
 * @author deveuge
 *
 */
public class WindowsRegistry {

	public static final String readRegistry(String location, String key) {
		try {
			Process process = Runtime.getRuntime().exec("reg query " + '"' + location + "\" /v " + key);

			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();

			String[] parsed = reader.getResult().split("    ");
			if (parsed.length > 1) {
				return parsed[parsed.length - 1].trim();
			}
		} catch (Exception e) {
		}

		return null;
	}

	private static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw = new StringWriter();

		public StreamReader(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1) {
					sw.write(c);
				}
			} catch (IOException e) {
			}
		}

		public String getResult() {
			return sw.toString();
		}
	}
}
