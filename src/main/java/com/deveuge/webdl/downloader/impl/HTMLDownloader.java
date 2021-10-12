package com.deveuge.webdl.downloader.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class HTMLDownloader extends DownloaderBase implements IDownloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HTMLDownloader.class);
	
	private static final String FILENAME = ".html";

	public HTMLDownloader() {
		super(FILENAME);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading page '{}' as HTML", url);
		Document document = Jsoup.connect(url)
				.userAgent(USER_AGENT)
				.timeout(TIMEOUT)
				.get();
		
		LOGGER.info("INFO: Successfully connected");
		final File outFile = new File(getFilePath(document.title()));
		LOGGER.info("INFO: Saving HTML file '{}'", outFile.getPath());
		FileUtils.writeStringToFile(outFile, document.outerHtml(), StandardCharsets.UTF_8);
	}

}
