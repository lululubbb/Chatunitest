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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

public class ParseBenchmark_262_6Test {

  private static final String ZIP_RESOURCE_PATH = "/ParseBenchmarkData.zip";
  private static final String TEST_FILE_NAME = "test.json";
  private static final String TEST_CONTENT = "{\"key\":\"value\"}";

  private Method resourceToStringMethod;

  @BeforeEach
  public void setUp() throws Exception {
    resourceToStringMethod = ParseBenchmark.class.getDeclaredMethod("resourceToString", String.class);
    resourceToStringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testResourceToString_readsContentFromZipEntry() throws Exception {
    try (MockedConstruction<ZipFile> mockedZipFile = Mockito.mockConstruction(ZipFile.class,
        (mock, context) -> {
          ZipEntry zipEntry = new ZipEntry(TEST_FILE_NAME);
          when(mock.getEntry(TEST_FILE_NAME)).thenReturn(zipEntry);

          InputStream inputStream = new ByteArrayInputStream(TEST_CONTENT.getBytes());
          when(mock.getInputStream(zipEntry)).thenReturn(inputStream);

          doNothing().when(mock).close();
        })) {

      // Invoke the private static method resourceToString via reflection
      String result = (String) resourceToStringMethod.invoke(null, TEST_FILE_NAME);

      assertEquals(TEST_CONTENT, result);
    }
  }

  @Test
    @Timeout(8000)
  public void testResourceToString_closesZipFileOnException() throws Exception {
    try (MockedConstruction<ZipFile> mockedZipFile = Mockito.mockConstruction(ZipFile.class,
        (mock, context) -> {
          when(mock.getEntry(TEST_FILE_NAME)).thenThrow(new IOException("forced exception"));
          doNothing().when(mock).close();
        })) {

      Exception thrown = assertThrows(Exception.class, () -> {
        resourceToStringMethod.invoke(null, TEST_FILE_NAME);
      });

      assertFalse(mockedZipFile.constructed().isEmpty(), "No ZipFile instances constructed");
      ZipFile mockZipFile = mockedZipFile.constructed().get(0);
      verify(mockZipFile).close();

      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("forced exception", thrown.getCause().getMessage());
    }
  }

}