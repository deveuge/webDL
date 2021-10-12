package com.deveuge.webdl.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.zip.InflaterInputStream;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.deveuge.webdl.enums.DownloadType;

public class ProtocolUtils {
	
	private static final String PROTOCOL = "webdl://";
    private static final String CHARSET = StandardCharsets.UTF_8.name();
    private static final String TYPE_PARAM = "type";

	public static String decode(String url) {
        url = url.replace(PROTOCOL, "");
        byte[] urlArray = DatatypeConverter.parseBase64Binary(url);
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(urlArray));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return "http://" + new String(baos.toByteArray(), CHARSET);
        } catch (IOException e) {
            return "http://" + url;
        }
    }
	
	public static DownloadType getType(String url) throws URISyntaxException {
		URI uri = new URI(url);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri);
        UriComponents uriComponents = uriComponentsBuilder.build();
        String strType = uriComponents.getQueryParams().getFirst(TYPE_PARAM);
        return DownloadType.valueOf(strType);
	}
}
