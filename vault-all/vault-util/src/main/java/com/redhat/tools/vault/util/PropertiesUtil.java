package com.redhat.tools.vault.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jboss.logging.Logger;

public class PropertiesUtil {
    protected static final Logger logger = Logger.getLogger(DateUtil.class);

    private PropertiesUtil() {
    }

    public static Properties readProperties(String filename) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getResourceAsStream(filename));
        } catch (FileNotFoundException e) {
            logger.error("file " + filename + " can not be found!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("error happens while reading " + filename + " file!");
            return null;
        }
        return properties;
    }

}
