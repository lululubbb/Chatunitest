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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ParseBenchmark_262_5Test {

  @Test
    @Timeout(8000)
  void testResourceToString_readsFileContents() throws Exception {
    // Arrange
    String testFileName = "testFile.json";
    String expectedContent = "test content from zip";

    // Mock ZipEntry
    ZipEntry mockZipEntry = mock(ZipEntry.class);

    // Mock ZipFile
    ZipFile mockZipFile = mock(ZipFile.class);

    // Mock InputStream from ZipFile.getInputStream
    InputStream mockInputStream = new java.io.ByteArrayInputStream(expectedContent.getBytes());

    // When zipFile.getEntry(testFileName) called, return mockZipEntry
    when(mockZipFile.getEntry(testFileName)).thenReturn(mockZipEntry);

    // When zipFile.getInputStream(mockZipEntry) called, return mockInputStream
    when(mockZipFile.getInputStream(mockZipEntry)).thenReturn(mockInputStream);

    // Mock File
    File dummyFile = mock(File.class);

    // Use reflection to access private static method getResourceFile
    Method getResourceFileMethod = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    getResourceFileMethod.setAccessible(true);

    // Use reflection to access private static method resourceToString
    Method method = ParseBenchmark.class.getDeclaredMethod("resourceToString", String.class);
    method.setAccessible(true);

    try (MockedConstruction<ZipFile> mockedZipFileConstruction = Mockito.mockConstruction(ZipFile.class,
        (mock, context) -> {
          when(mock.getEntry(testFileName)).thenReturn(mockZipEntry);
          when(mock.getInputStream(mockZipEntry)).thenReturn(mockInputStream);
          doNothing().when(mock).close();
        });
        MockedStatic<ParseBenchmark> utilities = Mockito.mockStatic(ParseBenchmark.class)) {

      // Mock getResourceFile to call real method via reflection to bypass private access
      utilities.when(() -> ParseBenchmark.getResourceFile("/ParseBenchmarkData.zip"))
          .then(invocation -> getResourceFileMethod.invoke(null, invocation.getArgument(0)));

      // Mock new ZipFile(dummyFile) to return mockZipFile
      utilities.when(() -> new ZipFile(dummyFile)).thenReturn(mockZipFile);

      // Also mock getResourceFile("/ParseBenchmarkData.zip") to return dummyFile
      utilities.when(() -> ParseBenchmark.getResourceFile("/ParseBenchmarkData.zip"))
          .thenReturn(dummyFile);

      // Act
      String actual = (String) method.invoke(null, testFileName);

      // Assert
      assertEquals(expectedContent, actual);

      // Verify close() called on ZipFile
      verify(mockZipFile).close();
    }
  }

  @Test
    @Timeout(8000)
  void testResourceToString_fileNotFound_throwsException() throws Exception {
    String fileName = "nonexistent.json";

    File dummyFile = mock(File.class);

    // Mock ZipFile
    ZipFile mockZipFile = mock(ZipFile.class);

    // Use reflection to access private static method getResourceFile
    Method getResourceFileMethod = ParseBenchmark.class.getDeclaredMethod("getResourceFile", String.class);
    getResourceFileMethod.setAccessible(true);

    // Use reflection to access private static method resourceToString
    Method method = ParseBenchmark.class.getDeclaredMethod("resourceToString", String.class);
    method.setAccessible(true);

    try (MockedStatic<ParseBenchmark> utilities = Mockito.mockStatic(ParseBenchmark.class)) {
      // Mock getResourceFile to call real method via reflection to bypass private access
      utilities.when(() -> ParseBenchmark.getResourceFile("/ParseBenchmarkData.zip"))
          .then(invocation -> getResourceFileMethod.invoke(null, invocation.getArgument(0)));

      // Mock new ZipFile(dummyFile) to return mockZipFile
      utilities.when(() -> new ZipFile(dummyFile)).thenReturn(mockZipFile);

      // Also mock getResourceFile("/ParseBenchmarkData.zip") to return dummyFile
      utilities.when(() -> ParseBenchmark.getResourceFile("/ParseBenchmarkData.zip"))
          .thenReturn(dummyFile);

      // zipFile.getEntry returns null to simulate missing file
      when(mockZipFile.getEntry(fileName)).thenReturn(null);

      doNothing().when(mockZipFile).close();

      Exception thrown = assertThrows(Exception.class, () -> {
        try {
          method.invoke(null, fileName);
        } catch (java.lang.reflect.InvocationTargetException e) {
          // unwrap the cause
          throw e.getCause();
        }
      });

      // Verify zipFile.close() is called even on exception
      verify(mockZipFile).close();
    }
  }
}