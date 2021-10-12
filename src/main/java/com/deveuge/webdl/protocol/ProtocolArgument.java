package com.deveuge.webdl.protocol;

import java.net.URISyntaxException;

import org.openqa.selenium.InvalidArgumentException;

import com.deveuge.webdl.enums.DownloadType;

public class ProtocolArgument {

	private String url;
	private DownloadType type;
	
	public ProtocolArgument(String[] args) throws URISyntaxException {
		super();
		if(args.length != 1) {
            throw new InvalidArgumentException("Invalid number of arguments");
        }
		
		this.url = ProtocolUtils.decode(args[0]);
		try {
			this.type = ProtocolUtils.getType(url);
		} catch(NullPointerException ex) {
			throw new InvalidArgumentException("You must supply a valid type for downloading");
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public DownloadType getType() {
		return type;
	}
	
	public void setType(DownloadType type) {
		this.type = type;
	}
	
}
