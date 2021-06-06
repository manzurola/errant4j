package edu.guym.errantj.lang.en.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.stream.Stream;

public class IOUtils {

    public static Stream<String> loadResourceAsLineStream(Object targetObject, String path) {
        InputStream inputStream = targetObject.getClass().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines();
    }

    public static Stream<String> loadResourceAsLineStream(Class<?> targetClass, String path, Charset charset) {
        InputStream inputStream = targetClass.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        return reader.lines();
    }

    public static Reader loadResourceAsReader(Object targetObject, String path) {
        InputStream inputStream = targetObject.getClass().getResourceAsStream(path);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static Reader loadResourceAsReader(Class<?> targetClass, String path) {
        InputStream inputStream = targetClass.getClassLoader().getResourceAsStream(path);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static InputStream loadResourceAsInputStream(Class<?> targetClass, String path) {
        return targetClass.getClassLoader().getResourceAsStream(path);
    }

}
