package com.deveuge.webdl.enums;

import com.deveuge.webdl.downloader.IDownloader;
import com.deveuge.webdl.downloader.impl.HTMLDownloader;
import com.deveuge.webdl.downloader.impl.ImageDownloader;
import com.deveuge.webdl.downloader.impl.MarkdownDownloader;
import com.deveuge.webdl.downloader.impl.PDFDownloader;
import com.deveuge.webdl.downloader.impl.ScreenshotDownloader;
import com.deveuge.webdl.downloader.impl.VideoDownloader;

public enum DownloadType implements IDownloader {
	HTML {
        @Override
        public void convert(String url) throws Exception {
        	new HTMLDownloader().convert(url);
		}
    },
	PDF {
        @Override
        public void convert(String url) throws Exception {
        	new PDFDownloader().convert(url);
		}
    },
	SS {
        @Override
        public void convert(String url) throws Exception {
        	new ScreenshotDownloader().convert(url);
		}
    },
	IMG {
    	@Override
        public void convert(String url) throws Exception {
			new ImageDownloader().convert(url);
			
		}
    },
	VID {
    	@Override
        public void convert(String url) throws Exception {
			new VideoDownloader().convert(url);
			
		}
    },
	MD {
    	@Override
        public void convert(String url) throws Exception {
			new MarkdownDownloader().convert(url);
			
		}
    };

	@Override
	public void convert(String url) throws Exception {
		throw new UnsupportedOperationException();
	}
}
