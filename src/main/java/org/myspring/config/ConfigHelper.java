package org.myspring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public final class ConfigHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigHelper.class);

    private static final Properties CONFIG_PROPS = new Properties();

    static {
        try {
            CONFIG_PROPS.load(ConfigHelper.class.getResourceAsStream(ConfigConstant.CONFIG_FILE));
        } catch (IOException e) {
            LOG.error("load start up config exception.", e);
        }
    }

    public static String getJdbcDriver() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_PASSWORD);
    }

    public static String getBasePackage() {
        return CONFIG_PROPS.getProperty(ConfigConstant.BASE_PACKAGE);
    }

    public static String getJspPath() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JSP_PATH);
    }

    public static String getAssetPath() {
        return CONFIG_PROPS.getProperty(ConfigConstant.ASSET_PATH);
    }

}
