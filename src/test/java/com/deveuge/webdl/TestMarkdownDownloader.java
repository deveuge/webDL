package com.deveuge.webdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.impl.MarkdownDownloader;

public class TestMarkdownDownloader {
	
	ClassLoader classLoader = getClass().getClassLoader();

    //@Test
    public void testMarkdownDownloaderOK() throws Exception {
    	String url = "https://en.wikipedia.org/wiki/English_Baroque";
        new MarkdownDownloader().convert(url);
        File file = new File(new DownloaderBase("").getFilePath("English Baroque - Wikipedia.md"));
        File originalFile = new File(classLoader.getResource("Example.md").getFile());
        assertTrue(file.exists());
        assertEquals("The files aren't equal", 
        		FileUtils.readFileToString(file, "utf-8"), 
        	    FileUtils.readFileToString(originalFile, "utf-8"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkdownDownloaderException() throws Exception {
    	new MarkdownDownloader().convert("https://google.com");
    }
}
