package io.bcaas.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String CHARSET_UTF8 = "utf-8";

    // Log Name for appender
    public static final String LOG_INFO = "log.info";
    public static final String LOG_DEBUG = "log.debug";

    public static final Logger LOGGER_INFO = LoggerFactory.getLogger(LOG_INFO);
    public static final Logger LOGGER_DEBUG = LoggerFactory.getLogger(LOG_DEBUG);

    public static final String WEBSITE_START = " --- [Start]";
    public static final String WEBSITE_END = " --- [End]";
}
