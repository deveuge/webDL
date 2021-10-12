package com.deveuge.webdl.downloader.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class PDFDownloader extends DownloaderBase implements IDownloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PDFDownloader.class);

	private static final String FILENAME = ".pdf";

	public PDFDownloader() {
		super(FILENAME);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading page '{}' as PDF", url);
		generatePDF(url);
	}

	public void generatePDF(String url) throws Exception {
		ChromeDriver driver = (ChromeDriver) initDriver();
		driver.get(url);
		LOGGER.info("INFO: Successfully connected");
		Map<String, Object> output = driver.executeCdpCommand("Page.printToPDF", new HashMap<>());

		try {
			String path = getFilePath(driver.getTitle());
			LOGGER.info("INFO: Saving PDF file '{}'", path);
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			byte[] byteArray = java.util.Base64.getDecoder().decode((String) output.get("data"));
			fileOutputStream.write(byteArray);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		driver.close();
		driver.quit();
	}
}
