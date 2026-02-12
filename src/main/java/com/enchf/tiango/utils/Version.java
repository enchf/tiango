package com.enchf.tiango.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.enchf.tiango.exception.InitializationException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Version {
    public String current() {
        try (InputStream is = Version.class.getResourceAsStream("/version.properties")) {
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("version");
        } catch (IOException e) {
            throw new InitializationException("Failed to load version.properties", e);
        }
    }
}
