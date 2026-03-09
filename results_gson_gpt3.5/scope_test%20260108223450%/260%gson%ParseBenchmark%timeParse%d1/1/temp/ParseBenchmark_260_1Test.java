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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

class ParseBenchmark_260_1Test {

  private ParseBenchmark parseBenchmark;

  // Change Parser to the correct type: com.google.gson.metrics.ParseBenchmark.Parser
  // Since Parser is a private inner interface/class, we get it via reflection
  private Object mockParser;

  private Object mockDocument;

  private char[] sampleText = "sample text".toCharArray();

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    parseBenchmark = new ParseBenchmark();

    try {
      // Set 'parser' field
      Field parserField = ParseBenchmark.class.getDeclaredField("parser");
      parserField.setAccessible(true);
      Class<?> parserClass = parserField.getType();
      mockParser = mock(parserClass);
      parserField.set(parseBenchmark, mockParser);

      // Set 'document' field
      Field documentField = ParseBenchmark.class.getDeclaredField("document");
      documentField.setAccessible(true);
      Class<?> documentClass = documentField.getType();
      mockDocument = mock(documentClass);
      documentField.set(parseBenchmark, mockDocument);

      // Set 'text' field
      Field textField = ParseBenchmark.class.getDeclaredField("text");
      textField.setAccessible(true);
      textField.set(parseBenchmark, sampleText);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void timeParse_invokesParserParse_correctNumberOfTimes() throws Exception {
    int reps = 5;

    parseBenchmark.timeParse(reps);

    // Use reflection to get the 'parse' method from parser mock and verify invocation
    verify(mockParser, times(reps)).getClass()
            .getMethod("parse", char[].class, mockDocument.getClass())
            .invoke(mockParser, any(char[].class), eq(mockDocument));
    // But above won't work directly in verify, so use Mockito verify with arg matchers instead:

    verify(mockParser, times(reps)).parse(any(char[].class), eq(mockDocument));
  }

  @Test
    @Timeout(8000)
  void timeParse_zeroReps_noInvocation() throws Exception {
    int reps = 0;

    parseBenchmark.timeParse(reps);

    verify(mockParser, never()).parse(any(char[].class), any());
  }

  @Test
    @Timeout(8000)
  void timeParse_oneRep_invokesOnce() throws Exception {
    int reps = 1;

    parseBenchmark.timeParse(reps);

    verify(mockParser, times(1)).parse(any(char[].class), eq(mockDocument));
  }
}