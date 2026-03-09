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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class ParseBenchmark_260_5Test {

  private ParseBenchmark parseBenchmark;
  private ParseBenchmark.Parser parserMock;
  private Object documentMock;
  private Class<?> documentClass;

  @BeforeEach
  void setUp() throws Exception {
    parseBenchmark = new ParseBenchmark();

    // Use reflection to set private 'parser' field
    parserMock = mock(ParseBenchmark.Parser.class);
    Field parserField = ParseBenchmark.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parseBenchmark, parserMock);

    // Use reflection to set private 'text' field
    char[] sampleText = "sample text".toCharArray();
    Field textField = ParseBenchmark.class.getDeclaredField("text");
    textField.setAccessible(true);
    textField.set(parseBenchmark, sampleText);

    // Use reflection to get Document class and create mock
    documentClass = null;
    for (Class<?> innerClass : ParseBenchmark.class.getDeclaredClasses()) {
      if ("Document".equals(innerClass.getSimpleName())) {
        documentClass = innerClass;
        break;
      }
    }
    if (documentClass == null) {
      throw new ClassNotFoundException("Document class not found inside ParseBenchmark");
    }
    documentMock = mock(documentClass);

    Field documentField = ParseBenchmark.class.getDeclaredField("document");
    documentField.setAccessible(true);
    documentField.set(parseBenchmark, documentMock);
  }

  @Test
    @Timeout(8000)
  void timeParse_callsParserParse_repsTimes() throws Exception {
    int reps = 5;

    parseBenchmark.timeParse(reps);

    // Cast eq(documentMock) to the exact Document type via reflection
    verify(parserMock, times(reps)).parse(any(char[].class), (Object) argThat(argument -> documentClass.isInstance(argument) && argument.equals(documentMock)));
  }
}