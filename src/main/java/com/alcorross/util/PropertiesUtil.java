package com.alcorross.util;

import com.alcorross.exceptions.DictionaryLoadException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            loadProperties();
        } catch (DictionaryLoadException e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    private PropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() throws DictionaryLoadException {
        try (var is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(is);
        } catch (IOException e) {
            log.error("Failed to read file", e);
            throw new DictionaryLoadException("Failed to load properties");
        }
    }
}
