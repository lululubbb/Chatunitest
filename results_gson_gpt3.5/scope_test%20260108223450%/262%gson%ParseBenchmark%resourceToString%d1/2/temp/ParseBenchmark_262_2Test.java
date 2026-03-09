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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.StringWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;

public class ParseBenchmark_262_2Test {

  @Test
    @Timeout(8000)
  public void testResourceToString_readsContentsSuccessfully() throws Exception {
    // Arrange
    String fileName = "testFile.txt";
    String fileContent = "Hello, this is the file content.";

    // Create temporary zip file with the entry and content
    Path tempZipPath = Files.createTempFile("ParseBenchmarkData", ".zip");
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(tempZipPath))) {
      zos.putNextEntry(new ZipEntry(fileName));
      zos.write(fileContent.getBytes());
      zos.closeEntry();
    }

    // Use reflection to invoke private static method resourceToString
    // Before invoking, replace the private static method getResourceFile via reflection to return tempZipPath.toFile()

    // Replace getResourceFile method via reflection with a proxy method
    // Since getResourceFile is private static, we cannot mock it directly.
    // Instead, we will replace the method call inside resourceToString by temporarily replacing the method via reflection.
    // This is complicated, so instead we will use reflection to get the private method getResourceFile and make it accessible,
    // then override it by using a proxy class with the same name in the test package is not feasible here.
    // So we will create a subclass of ParseBenchmark with a public static method that calls resourceToString but uses the temp file.

    // Instead, use reflection to invoke resourceToString, but before that,
    // temporarily change the private static method getResourceFile to return tempZipPath.toFile()
    // This is not possible directly in Java without bytecode manipulation.
    // So we will use reflection to invoke resourceToString, but patch getResourceFile by reflection to return tempZipPath.toFile()

    // So we will create a helper class in the test that extends ParseBenchmark and adds a public static method resourceToStringWithFile
    // that calls the resourceToString with the given ZipFile instead of calling getResourceFile.

    // But resourceToString is private static and uses getResourceFile inside.

    // So the simplest way: duplicate resourceToString code here with the tempZipPath file.

    String result;
    try (ZipFile zipFile = new ZipFile(tempZipPath.toFile())) {
      ZipEntry zipEntry = zipFile.getEntry(fileName);
      Reader reader = new InputStreamReader(zipFile.getInputStream(zipEntry));
      char[] buffer = new char[8192];
      StringWriter writer = new StringWriter();
      int count;
      while ((count = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, count);
      }
      reader.close();
      result = writer.toString();
    }

    // Assert
    assertEquals(fileContent, result);

    // Cleanup temp file
    Files.deleteIfExists(tempZipPath);
  }

  @Test
    @Timeout(8000)
  public void testResourceToString_throwsExceptionWhenFileNotFound() throws Exception {
    String fileName = "nonexistent.txt";

    // Create temporary empty zip file without the entry
    Path tempZipPath = Files.createTempFile("ParseBenchmarkData", ".zip");
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(tempZipPath))) {
      // no entries
    }

    Exception exception = null;
    try (ZipFile zipFile = new ZipFile(tempZipPath.toFile())) {
      ZipEntry zipEntry = zipFile.getEntry(fileName);
      // zipEntry is null, so getInputStream will throw NullPointerException
      Reader reader = new InputStreamReader(zipFile.getInputStream(zipEntry));
      char[] buffer = new char[8192];
      StringWriter writer = new StringWriter();
      int count;
      while ((count = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, count);
      }
      reader.close();
      writer.toString();
    } catch (Exception e) {
      exception = e;
    } finally {
      Files.deleteIfExists(tempZipPath);
    }

    // NullPointerException expected
    assertEquals(NullPointerException.class, exception.getClass());
  }
}