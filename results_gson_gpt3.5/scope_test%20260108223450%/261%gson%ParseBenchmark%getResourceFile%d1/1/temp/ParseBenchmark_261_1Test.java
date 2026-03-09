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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;

public class ParseBenchmark_261_1Test {

  @Test
    @Timeout(8000)
  void getResourceFile_validResource_returnsFile() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    // Use a known existing resource path relative to the classpath root
    String resourcePath = "/com/google/gson/metrics/ParseBenchmark.class";

    Object result = method.invoke(null, resourcePath);
    // Unwrap InvocationTargetException if thrown
    File file;
    if (result instanceof File) {
      file = (File) result;
    } else {
      fail("Expected File return type");
      return;
    }

    assertNotNull(file);
    assertTrue(file.exists());
    assertTrue(file.isFile());
  }

  @Test
    @Timeout(8000)
  void getResourceFile_resourceDoesNotExist_throwsIllegalArgumentException() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    String resourcePath = "/nonexistent/resource.file";

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
  void getResourceFile_resourceIsNotAFile_throwsIllegalArgumentException() throws Exception {
    Method method = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    method.setAccessible(true);

    // Use a resource path that points to a directory, e.g. package folder
    String resourcePath = "/com/google/gson/metrics";

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, resourcePath);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Resource " + resourcePath + " is not a file", cause.getMessage());
  }
}