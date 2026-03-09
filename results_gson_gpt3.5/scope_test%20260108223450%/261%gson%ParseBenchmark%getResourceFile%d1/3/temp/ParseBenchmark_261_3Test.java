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
import java.net.URISyntaxException;
import java.net.URL;

class ParseBenchmark_261_3Test {

  @Test
    @Timeout(8000)
  void getResourceFile_validResource_returnsFile() throws Exception {
    // Use a known existing resource that is a file in the classpath
    String resourcePath = "/com/google/gson/metrics/ParseBenchmark.class";

    // We need to handle the case where the resource URL is not a "file:" URL but "jar:" URL
    // So we test if the resource URL protocol is "file", else skip this test
    URL url = ParseBenchmark.class.getResource(resourcePath);
    if (url == null) {
      fail("Resource " + resourcePath + " does not exist");
    }
    if (!"file".equals(url.getProtocol())) {
      // Skip test if resource is inside a jar or not accessible as a file
      // This prevents URI is not hierarchical error
      return;
    }

    File file = invokeGetResourceFile(resourcePath);
    assertNotNull(file);
    assertTrue(file.isFile());
  }

  @Test
    @Timeout(8000)
  void getResourceFile_resourceDoesNotExist_throwsIllegalArgumentException() {
    String resourcePath = "/nonexistent/resource.file";
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeGetResourceFile(resourcePath);
    });
    assertTrue(thrown.getMessage().contains("does not exist"));
  }

  @Test
    @Timeout(8000)
  void getResourceFile_resourceIsNotFile_throwsIllegalArgumentException() throws Exception {
    // Use a resource path that points to a directory in the classpath
    String resourcePath = "/com/google/gson/metrics/";

    // Similar to above, check if resource URL exists and is "file" protocol
    URL url = ParseBenchmark.class.getResource(resourcePath);
    if (url == null) {
      fail("Resource " + resourcePath + " does not exist");
    }
    if (!"file".equals(url.getProtocol())) {
      // Skip test if resource is inside a jar or not accessible as a file
      return;
    }

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeGetResourceFile(resourcePath);
    });
    assertTrue(thrown.getMessage().contains("is not a file"));
  }

  private static File invokeGetResourceFile(String path) throws Exception {
    java.lang.reflect.Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);
    try {
      return (File) method.invoke(null, path);
    } catch (java.lang.reflect.InvocationTargetException e) {
      // unwrap the cause for easier testing
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else if (cause instanceof Exception) {
        throw (Exception) cause;
      } else {
        throw e;
      }
    }
  }
}