package com.deveuge.webdl.downloader.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class ScreenshotDownloader extends DownloaderBase implements IDownloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotDownloader.class);

	private static final String FILENAME = "screenshot-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	private static final String[] ACCEPT_I18N = new String[] {"accept", "aceptar", "accettare", "accepte", "aceitar"};
	private static final String CSS_SELECTOR = "[aria-label*='%s' i]";
	
	public ScreenshotDownloader() {
		super(FILENAME);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading page '{}' as screenshot", url);
		WebDriver driver = initDriver();
		driver.get(url);
		LOGGER.info("INFO: Successfully connected");
		Thread.sleep(2000);
		acceptCookiesPopup(driver);
		LOGGER.info("INFO: Taking screenshot from '{}'", url);
		takeScreenshot(driver, url);
		LOGGER.info("INFO: Saving PNG file '{}'", getDownloadFolder() + FILE_SEPARATOR + FILENAME + ".png");
		driver.close();
		driver.quit();
	}
	
	private void acceptCookiesPopup(WebDriver driver) {
		try {
			StringBuilder sb = new StringBuilder();
			for(String accept : ACCEPT_I18N) {
				sb.append(String.format(CSS_SELECTOR, accept));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() -1);
			driver.findElement(By.cssSelector(sb.toString())).click();
		} catch(Exception ex) {}
	}
	
	private void takeScreenshot(WebDriver driver, String url) throws IOException {
		Shutterbug.shootPage(driver, Capture.FULL, 1000).withName(FILENAME).save(getDownloadFolder());
	}

}
