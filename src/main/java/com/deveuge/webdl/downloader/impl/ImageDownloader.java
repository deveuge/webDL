package com.deveuge.webdl.downloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class ImageDownloader extends DownloaderBase implements IDownloader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloader.class);
	
	private String folderName;
	
	public ImageDownloader() {
		super(null);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading all images from page '{}'", url);
		Document document = Jsoup.connect(url)
				.userAgent(USER_AGENT)
				.timeout(TIMEOUT)
				.get();
		LOGGER.info("INFO: Successfully connected");
		folderName = document.title();
		String folder = getImagesFolder(folderName);
		new File(folder).mkdirs();
		LOGGER.info("INFO: Saving images to '{}'", folder);
		downloadAllImages(document);
	}
	
	private void downloadAllImages(Document document) {
		for (Element imageElement : document.select("img")) {
			downloadImage(imageElement.attr("abs:src"));
		}
	}

	private void downloadImage(String strImageURL) {
		String strImageName = strImageURL.substring(strImageURL.lastIndexOf("/") + 1);
		LOGGER.info("INFO: Saving: '{}' from '{}'", strImageName, strImageURL);

		try {
			URL urlImage = new URL(strImageURL);
			InputStream in = urlImage.openStream();

			byte[] buffer = new byte[4096];
			int n = -1;

			OutputStream os = new FileOutputStream(getImagesFolder(folderName) + strImageName);
			while ((n = in.read(buffer)) != -1) {
				os.write(buffer, 0, n);
			}
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
