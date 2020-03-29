package org.myspring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class CodecUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CodecUtil.class);

    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

    public static String encodeURL(String source) {
        try {
            return URLEncoder.encode(source, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOG.error("encode url failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static String decodeURL(String source) {
        try {
            return URLDecoder.decode(source, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOG.error("decode url failure.", e);
            throw new RuntimeException(e);
        }
    }

}
