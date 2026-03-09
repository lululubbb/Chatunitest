package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.net.URL;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class ParseBenchmark_261_2Test {

  @Test
    @Timeout(8000)
  void testGetResourceFile_validResource() throws Exception {
    // Use a known resource path that exists in the test classpath
    String resourcePath = "/com/google/gson/metrics/ParseBenchmark.class";
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    URL url = ParseBenchmark.class.getResource(resourcePath);
    assertNotNull(url, "Resource URL should not be null");

    // Only test if URL protocol is "file" (skip if inside jar)
    if (!"file".equals(url.getProtocol())) {
      // Skip test if resource is not a file on disk (e.g., inside jar)
      return;
    }

    File expectedFile = new File(url.toURI());
    assertTrue(expectedFile.isFile(), "Expected resource is not a file");

    File file = (File) method.invoke(null, resourcePath);

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
  }

  @Test
    @Timeout(8000)
  void testGetResourceFile_resourceDoesNotExist() throws Exception {
    String resourcePath = "/nonexistent/resource.file";
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, resourcePath);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Resource " + resourcePath + " does not exist", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void testGetResourceFile_resourceIsNotAFile() throws Exception {
    // Use a resource path that points to a directory (usually the root of classpath)
    String resourcePath = "/";
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, resourcePath);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Resource " + resourcePath + " is not a file", cause.getMessage());
  }
}