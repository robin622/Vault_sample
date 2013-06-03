package com.redhat.tools.vault.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.jboss.logging.Logger;

public class PropertiesUtil {
    protected static final Logger logger = Logger.getLogger(DateUtil.class);

    private PropertiesUtil() {
    }

    public static Properties readProperties(String filename) {
        Properties properties = new Properties();
        try {
        	InputStream inputStream=new FileInputStream(new File(filename));
            properties.load(inputStream);
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
