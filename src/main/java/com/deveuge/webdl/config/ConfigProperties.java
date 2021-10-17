package com.deveuge.webdl.config;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ConfigProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProperties.class);
    private static final String CONFIG_FILE_PATH = "\\WebDL\\application.properties";

    private String downloadFolder = System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads";

    public ConfigProperties() {
    	try {
	    	String installationDirectory = getInstallationDirectory();
	        FileInputStream file = new FileInputStream(installationDirectory + CONFIG_FILE_PATH);
	        Properties properties = new Properties();
	        properties.load(file);
	        file.close();
	        
	        if(StringUtils.hasLength(properties.getProperty("download.folder")))
	        	downloadFolder = properties.getProperty("download.folder");
	        LOGGER.info("INFO: Download folder '{}'", downloadFolder);
    	} catch(Exception ex) {
			LOGGER.error("ERROR: Cannot read configuration file 'application.properties'. Setting default values");
    	}
    }
    
    private String getInstallationDirectory() {
    	boolean is64bit = (System.getenv("ProgramFiles(x86)") != null);
    	String key = is64bit 
    			? "HKLM\\SOFTWARE\\Wow6432Node\\WebDL"
				: "HKLM\\SOFTWARE\\WebDL";
    	return WindowsRegistry.readRegistry(key, "Path");
    }

	public String getDownloadFolder() {
		return downloadFolder;
	}
	
}
