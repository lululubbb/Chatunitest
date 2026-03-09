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
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URISyntaxException;

public class ParseBenchmark_261_6Test {

  @Test
    @Timeout(8000)
  public void testGetResourceFile_validFile() throws Exception {
    // Use reflection to invoke private method
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    // Use a resource that exists in the test resources folder and is guaranteed to be a file
    // Use a resource file known to be accessible as a file in the file system, not inside a jar
    String resourcePath = "/com/google/gson/metrics/ParseBenchmarkTest.class";

    // Act
    File resourceFile = (File) method.invoke(null, resourcePath);

    // Assert
    assertNotNull(resourceFile);
    assertTrue(resourceFile.isFile());
    assertTrue(resourceFile.getName().endsWith("ParseBenchmarkTest.class"));
  }

  @Test
    @Timeout(8000)
  public void testGetResourceFile_resourceDoesNotExist() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, "/nonexistent_resource.txt");
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Resource /nonexistent_resource.txt does not exist", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetResourceFile_resourceIsNotFile() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, "/com/google/gson/metrics");
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Resource /com/google/gson/metrics is not a file", thrown.getMessage());
  }
}