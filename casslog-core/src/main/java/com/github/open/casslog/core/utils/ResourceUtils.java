package com.github.open.casslog.core.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class ResourceUtils {
    private static final String CLASSPATH_PREFIX = "classpath:";

    public ResourceUtils() {
    }

    public static URL getResourceUrl(String resource) throws IOException {
        if (resource.startsWith(CLASSPATH_PREFIX)) {
            String path = resource.substring(CLASSPATH_PREFIX.length());
            ClassLoader classLoader = ResourceUtils.class.getClassLoader();
            URL url = classLoader != null ? classLoader.getResource(path) : ClassLoader.getSystemResource(path);
            if (url == null) {
                throw new FileNotFoundException("Resource [" + resource + "] does not exist");
            } else {
                return url;
            }
        } else {
            try {
                return new URL(resource);
            } catch (MalformedURLException var4) {
                return (new File(resource)).toURI().toURL();
            }
        }
    }

    public static URL getResourceUrl(ClassLoader loader, String resource) throws IOException {
        URL url = null;
        if (loader != null) {
            url = loader.getResource(resource);
        }

        if (url == null) {
            url = ClassLoader.getSystemResource(resource);
        }

        if (url == null) {
            throw new IOException("Could not find resource " + resource);
        } else {
            return url;
        }
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader loader = ResourceUtils.class.getClassLoader();
        return getResourceAsStream(loader, resource);
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = null;
        if (loader != null) {
            in = loader.getResourceAsStream(resource);
        }

        if (in == null) {
            in = ClassLoader.getSystemResourceAsStream(resource);
        }

        if (in == null) {
            throw new IOException("Could not find resource " + resource);
        } else {
            return in;
        }
    }

    public static Properties getResourceAsProperties(String resource) throws IOException {
        ClassLoader loader = ResourceUtils.class.getClassLoader();
        return getResourceAsProperties(loader, resource);
    }

    public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = getResourceAsStream(loader, resource);
        props.load(in);
        try {
            in.close();
        } catch (IOException ignored) {
        }
        return props;
    }

    public static InputStreamReader getResourceAsReader(String resource, String charsetName) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource), charsetName);
    }

    public static Reader getResourceAsReader(ClassLoader loader, String resource, String charsetName) throws IOException {
        return new InputStreamReader(getResourceAsStream(loader, resource), charsetName);
    }

    public static File getResourceAsFile(String resource) throws IOException {
        return new File(getResourceUrl(resource).getFile());
    }

    public static File getResourceAsFile(URL url) {
        return new File(url.getFile());
    }

    public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
        return new File(getResourceUrl(loader, resource).getFile());
    }
}
