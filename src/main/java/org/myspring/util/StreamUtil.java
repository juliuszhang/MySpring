package org.myspring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class StreamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream is) {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            LOG.error("read input stream data failure.", e);
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

}
