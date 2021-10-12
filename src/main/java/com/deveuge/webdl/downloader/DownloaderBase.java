package com.deveuge.webdl.downloader;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.deveuge.webdl.config.ConfigProperties;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DownloaderBase {
	
	public static final String USER_AGENT = "Mozilla";
	public static final int TIMEOUT = 10 * 1000;
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private ConfigProperties props;
	private String downloadFolder;
	private String filename;

	public DownloaderBase(String filename) {
		super();
		this.props = new ConfigProperties();
		this.filename = filename;
		this.downloadFolder = props.getDownloadFolder();
	}

	public String getDownloadFolder() {
		return downloadFolder;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getImagesFolder(String folderName) {
		StringBuilder sb = new StringBuilder();
		sb.append(getDownloadFolder())
			.append(FILE_SEPARATOR)
			.append(folderName)
			.append(FILE_SEPARATOR);
		return sb.toString();
	}
	
	public String getFilePath(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append(getDownloadFolder())
			.append(FILE_SEPARATOR)
			.append(title)
			.append(getFilename());
		return sb.toString();
	}
	
	public WebDriver initDriver() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		
        WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		options.addArguments("user-agent=" + USER_AGENT);
		options.addArguments("start-maximized");
		options.addArguments(String.format("--window-size=%dx%d", size.width, size.height));
        options.addArguments("--disable-gpu", "--run-all-compositor-stages-before-draw", "--mute-audio");
        
        return new ChromeDriver(options);
	}
}
