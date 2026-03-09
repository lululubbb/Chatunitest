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
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class ParseBenchmark_262_1Test {

  private static File testZipFile;

  @BeforeAll
  static void createTestZip() throws Exception {
    // Create a temporary zip file with a known entry
    testZipFile = File.createTempFile("ParseBenchmarkData", ".zip");
    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testZipFile))) {
      ZipEntry entry = new ZipEntry("testfile.txt");
      zos.putNextEntry(entry);
      String content = "Hello, world!";
      zos.write(content.getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();
    }
  }

  @AfterAll
  static void cleanup() {
    if (testZipFile != null && testZipFile.exists()) {
      testZipFile.delete();
    }
  }

  /**
   * Use Mockito to mock the private static method 'getResourceFile' of ParseBenchmark
   * to return our test zip file, then invoke the private static method 'resourceToString'.
   */
  private static String invokeResourceToString(String fileName) throws Exception {
    Method resourceToStringMethod = ParseBenchmark.class.getDeclaredMethod("resourceToString", String.class);
    resourceToStringMethod.setAccessible(true);

    try (MockedStatic<ParseBenchmark> mocked = Mockito.mockStatic(ParseBenchmark.class, Mockito.CALLS_REAL_METHODS)) {
      // Mock getResourceFile(String) using reflection to bypass private access
      mocked.when(() -> ParseBenchmark.getResourceFile(Mockito.anyString())).thenReturn(testZipFile);

      try {
        return (String) resourceToStringMethod.invoke(null, fileName);
      } catch (InvocationTargetException e) {
        throw (e.getCause() != null) ? (Exception) e.getCause() : e;
      }
    }
  }

  @Test
    @Timeout(8000)
  void testResourceToString_readsContentCorrectly() throws Exception {
    String content = invokeResourceToString("testfile.txt");
    assertEquals("Hello, world!", content);
  }

  @Test
    @Timeout(8000)
  void testResourceToString_fileNotFound_throwsException() throws Exception {
    Exception ex = assertThrows(IllegalArgumentException.class, () -> {
      invokeResourceToString("nonexistent.txt");
    });
    assertEquals("File not found in zip: nonexistent.txt", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testResourceToString_nullFileName_throwsNullPointerException() throws Exception {
    assertThrows(NullPointerException.class, () -> {
      invokeResourceToString(null);
    });
  }
}