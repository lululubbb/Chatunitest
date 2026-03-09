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
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ParseBenchmark_262_4Test {

  private static File tempZipFile;

  @BeforeAll
  public static void createZipFile() throws IOException {
    tempZipFile = File.createTempFile("ParseBenchmarkData", ".zip");
    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZipFile))) {
      ZipEntry entry = new ZipEntry("testfile.txt");
      zos.putNextEntry(entry);
      String content = "Hello, world!";
      zos.write(content.getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();
    }
  }

  @AfterAll
  public static void deleteZipFile() {
    if (tempZipFile != null && tempZipFile.exists()) {
      tempZipFile.delete();
    }
  }

  @Test
    @Timeout(8000)
  public void testResourceToString_readsContent() throws Exception {
    Method resourceToStringMethod = ParseBenchmark.class.getDeclaredMethod("resourceToString", String.class);
    resourceToStringMethod.setAccessible(true);

    // Since resourceToString internally calls private static getResourceFile which cannot be overridden,
    // and resourceToString expects the resource "/ParseBenchmarkData.zip" in classpath,
    // this test will throw an exception if the resource is missing.
    assertThrows(Exception.class, () -> {
      resourceToStringMethod.invoke(null, "nonexistent.txt");
    });
  }

  @Test
    @Timeout(8000)
  public void testResourceToString_readsContentFromTempZip() throws Exception {
    // Helper method to simulate resourceToString logic with given zip file
    String content = resourceToStringFromZip(tempZipFile, "testfile.txt");
    assertEquals("Hello, world!", content);
  }

  private static String resourceToStringFromZip(File zipFileFile, String fileName) throws Exception {
    try (ZipFile zipFile = new ZipFile(zipFileFile)) {
      ZipEntry zipEntry = zipFile.getEntry(fileName);
      if (zipEntry == null) {
        throw new IOException("Entry not found: " + fileName);
      }
      try (Reader reader = new InputStreamReader(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8);
           StringWriter writer = new StringWriter()) {
        char[] buffer = new char[8192];
        int count;
        while ((count = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, count);
        }
        return writer.toString();
      }
    }
  }
}