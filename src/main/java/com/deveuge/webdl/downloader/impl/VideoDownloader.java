package com.deveuge.webdl.downloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class VideoDownloader extends DownloaderBase implements IDownloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoDownloader.class);

	private static final String FILENAME = "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	public VideoDownloader() {
		super(FILENAME);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading video from page '{}'", url);
		WebDriver driver = initDriver();
		driver.get(url);
		LOGGER.info("INFO: Successfully connected");
		Document document = Jsoup.parse(driver.getPageSource());
		downloadFirstValidVideo(document);
		driver.close();
		driver.quit();
	}
	
	private void downloadFirstValidVideo(Document document) throws IOException {
		for (Element videoElement : document.select("video, video > source")) {
			String src = videoElement.attr("src");
			if(StringUtils.hasLength(src)) {
				downloadVideo(src, document.title());
				return;
			}
		}
	}

	private void downloadVideo(String src, String title) throws IOException {
		String path = getFilePath(title) + "." + FilenameUtils.getExtension(src);
		LOGGER.info("INFO: Saving video '{}'", path);
		FileOutputStream out = (new FileOutputStream(new File(path)));
		out.write(Jsoup.connect(src).ignoreContentType(true).execute().bodyAsBytes());
		out.close();
	}

}
