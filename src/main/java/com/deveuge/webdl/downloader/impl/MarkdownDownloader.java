package com.deveuge.webdl.downloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.webdl.downloader.DownloaderBase;
import com.deveuge.webdl.downloader.IDownloader;

public class MarkdownDownloader extends DownloaderBase implements IDownloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MarkdownDownloader.class);

	private static final String FILENAME = ".md";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	private static final String LINK_MD = "[%s](https://%s)";
	private static final String IMAGE_MD = "![%s](https:%s)";
	
	private String url;

	public MarkdownDownloader() {
		super(FILENAME);
	}

	@Override
	public void convert(String url) throws Exception {
		LOGGER.info("INFO: Downloading page '{}' as Markdown", url);
		this.url = url;
		if(!url.contains("wiki")) {
			throw new UnsupportedOperationException("The 'MD' type is only supported for Wikipedia pages");
		}
		
		Document document = Jsoup.connect(url)
				.userAgent(USER_AGENT)
				.timeout(TIMEOUT)
				.get();
		LOGGER.info("INFO: Successfully connected");
		
		downloadAsMarkdown(document);
	}
	
	private void downloadAsMarkdown(Document document) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("# " + document.select("#firstHeading").text());
		sb.append(LINE_SEPARATOR);
		
		for (Element el : document.select(".mw-parser-output > *:not(#toc):not(.mw-authority-control)")) {
			formatElement(el, sb);
			sb.append(LINE_SEPARATOR);
		}
		
		String path = getFilePath(document.title());
		LOGGER.info("INFO: Saving Markdown file '{}'", path);
		FileOutputStream out = (new FileOutputStream(new File(path)));
		out.write(sb.toString().getBytes());
		out.close();
	}
	
	private void formatElement(Element el, StringBuilder sb) {
		switch(el.tag().getName()) {
		case "p":
			sb.append(normalizeText(el));
			break;
		case "ul":
			formatList(el, sb);
			break;
		case "h2":
			sb.append("## ");
			sb.append(normalizeText(el));
			break;
		case "h3":
			sb.append("### ");
			sb.append(normalizeText(el));
			break;
		default: 
			formatList(el, sb);
			formatImage(el, sb);
		}
	}
	
	private String normalizeText(Element el) {
		el.select(".mw-editsection").remove();		// Remove edit sections
		el.select("[href^=#cite_note]").remove();	// Remove reference notes
		formatLink(el);
		
		Safelist sl = new Safelist();
		sl.addTags("b", "strong", "i", "em");
		String str = Jsoup.clean(el.html(), sl);

		return str
				.replaceAll("\u200B", "")							// Remove "&ZeroWidthSpace;" chars
				.replaceAll("&nbsp;", " ")							// Replace "&nbsp;" chars
				.replaceAll("<b>|</b>|<strong>|</strong>", "**")	// Bold text
				.replaceAll("<i>|</i>|<em>|</em>", "*");			// Italic text

	}
	
	private void formatList(Element el, StringBuilder sb) {
		for(Element li : el.select("li")) {
			el.select("span.mw-cite-backlink").remove();
			sb.append("* ");
			sb.append(normalizeText(li));
			sb.append(LINE_SEPARATOR);
		}
	}
	
	private void formatImage(Element el, StringBuilder sb) {
		for(Element img : el.select("img")) {
			sb.append(String.format(IMAGE_MD, img.attr("alt"), img.attr("src")));
			sb.append(LINE_SEPARATOR);
		}
	}
	
	private void formatLink(Element el) {
		String host = getHost();
		for(Element a : el.select("a")) {
			String link = String.format(LINK_MD, a.text(), host + a.attr("href"));
			a.replaceWith(new TextNode(link));
		}
	}
	
	private String getHost() {
		try {
			return new URL(url).getHost();
		} catch (MalformedURLException e) {
			return "";
		}
	}

}
