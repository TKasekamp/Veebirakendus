package com.codepump.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class CacheController {
	public static void updateCacheManifest() {
		String path = Paths.get("").toAbsolutePath().toString();
		path += "/src/main/webapp/cache.manifest";
		try {
			String text = readFile(path, StandardCharsets.UTF_8);
			java.util.Date date = new java.util.Date();
			String time = new Timestamp(date.getTime()).toString();
			text = text.replaceAll("[~].*[~]", "~" + time + "~");
			writeToFile(path, text);
			System.out.println("Cache manifest updated.");
		} catch (IOException e) {
			System.err.println("Couldn't read cache.manifest: "
					+ e.getMessage());
		}

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	static void writeToFile(String path, String text) {
		BufferedWriter out = null;
		try {
			FileWriter fstream = new FileWriter(path, false);
			out = new BufferedWriter(fstream);
			out.write(text);
		} catch (IOException e) {
			System.err.println("Couldn't write to cache.manifest: "
					+ e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
