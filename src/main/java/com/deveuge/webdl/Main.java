package com.deveuge.webdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deveuge.webdl.protocol.ProtocolArgument;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		try {
			ProtocolArgument arg = new ProtocolArgument(args);
			arg.getType().convert(arg.getUrl());
		} catch(Exception ex) {
			LOGGER.error("INTERNAL ERROR", ex);
			ex.printStackTrace();
		}
	}

}
