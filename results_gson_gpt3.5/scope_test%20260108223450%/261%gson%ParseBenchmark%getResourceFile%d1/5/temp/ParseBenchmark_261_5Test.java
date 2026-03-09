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

public class ParseBenchmark_261_5Test {

  @Test
    @Timeout(8000)
  void testGetResourceFile_validFile() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    // Use a resource that is known to be a file and accessible as a file URL.
    // Using the class file path directly may not work if running from a jar.
    // Instead, use a resource that is guaranteed to be a file in the test resources.
    String resourcePath = "/com/google/gson/metrics/ParseBenchmark.class";

    URL resourceUrl = ParseBenchmark.class.getResource(resourcePath);
    assertNotNull(resourceUrl, "Resource URL should not be null");

    if (!"file".equals(resourceUrl.getProtocol())) {
      // Skip test if resource is not accessible as a file URL
      // because the method under test requires a file URL.
      // This avoids test failure in environments like jars.
      return;
    }

    File file = (File) method.invoke(null, resourcePath);

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
  }

  @Test
    @Timeout(8000)
  void testGetResourceFile_resourceDoesNotExist() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    String resourcePath = "/nonexistent/resource.file";

    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(null, resourcePath);
    });

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Resource " + resourcePath + " does not exist", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void testGetResourceFile_resourceIsNotFile() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    String resourcePath = "/";

    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(null, resourcePath);
    });

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Resource " + resourcePath + " is not a file", cause.getMessage());
  }
}