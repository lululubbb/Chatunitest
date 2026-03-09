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

class ParseBenchmark_260_4Test {

  private ParseBenchmark parseBenchmark;
  private Object mockParser;
  private char[] sampleText;
  private Object mockDocument;

  @BeforeEach
  void setUp() throws Exception {
    parseBenchmark = new ParseBenchmark();

    // Create mocks for Parser and Document using reflection because they are private inner classes
    Class<?> parseBenchmarkClass = ParseBenchmark.class;

    // Get the Parser class (private inner class)
    Class<?> parserClass = null;
    for (Class<?> innerClass : parseBenchmarkClass.getDeclaredClasses()) {
      if ("Parser".equals(innerClass.getSimpleName())) {
        parserClass = innerClass;
        break;
      }
    }
    if (parserClass == null) {
      // fallback: just mock Object to avoid NPE
      mockParser = mock(Object.class);
    } else {
      mockParser = mock(parserClass);
    }

    // Get the Document class (private inner class)
    Class<?> documentClass = null;
    for (Class<?> innerClass : parseBenchmarkClass.getDeclaredClasses()) {
      if ("Document".equals(innerClass.getSimpleName())) {
        documentClass = innerClass;
        break;
      }
    }
    if (documentClass == null) {
      mockDocument = mock(Object.class);
    } else {
      mockDocument = mock(documentClass);
    }

    // Set the private field 'parser' to the mockParser
    Field parserField = parseBenchmarkClass.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parseBenchmark, mockParser);

    // Set the private field 'text' with some sample char array
    sampleText = "sample text".toCharArray();
    Field textField = parseBenchmarkClass.getDeclaredField("text");
    textField.setAccessible(true);
    textField.set(parseBenchmark, sampleText);

    // Set the @Param Document field 'document' with mockDocument
    Field documentField = parseBenchmarkClass.getDeclaredField("document");
    documentField.setAccessible(true);
    documentField.set(parseBenchmark, mockDocument);
  }

  @Test
    @Timeout(8000)
  void testTimeParse_withRepsZero_doesNotCallParse() throws Exception {
    parseBenchmark.timeParse(0);

    // Verify parser.parse is never called when reps=0
    verify(mockParser, never()).getClass()
        .getMethod("parse", char[].class, mockDocument.getClass())
        .invoke(mockParser, any(char[].class), any());
  }

  @Test
    @Timeout(8000)
  void testTimeParse_withPositiveReps_callsParseCorrectNumberOfTimes() throws Exception {
    int reps = 5;
    parseBenchmark.timeParse(reps);

    // Verify parser.parse is called exactly reps times with correct arguments
    verify(mockParser, times(reps)).getClass()
        .getMethod("parse", char[].class, mockDocument.getClass())
        .invoke(mockParser, sampleText, mockDocument);
  }

  @Test
    @Timeout(8000)
  void testTimeParse_withOneRep_callsParseOnce() throws Exception {
    parseBenchmark.timeParse(1);

    verify(mockParser, times(1)).getClass()
        .getMethod("parse", char[].class, mockDocument.getClass())
        .invoke(mockParser, sampleText, mockDocument);
  }
}