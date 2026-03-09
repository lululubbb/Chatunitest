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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParseBenchmark_260_3Test {

  private ParseBenchmark parseBenchmark;

  @Mock
  private Object parser;

  @Mock
  private Object document;

  @BeforeEach
  void setUp() throws Exception {
    parseBenchmark = new ParseBenchmark();
    // Use reflection to set private fields parser, document, and text
    java.lang.reflect.Field parserField = ParseBenchmark.class.getDeclaredField("parser");
    parserField.setAccessible(true);
    parserField.set(parseBenchmark, parser);

    java.lang.reflect.Field documentField = ParseBenchmark.class.getDeclaredField("document");
    documentField.setAccessible(true);
    documentField.set(parseBenchmark, document);

    java.lang.reflect.Field textField = ParseBenchmark.class.getDeclaredField("text");
    textField.setAccessible(true);
    textField.set(parseBenchmark, "some text".toCharArray());
  }

  @Test
    @Timeout(8000)
  void timeParse_invokesParserParse_correctNumberOfTimes() throws Exception {
    int reps = 5;

    parseBenchmark.timeParse(reps);

    verify(parser, times(reps)).parse(any(char[].class), eq(document));
  }

  @Test
    @Timeout(8000)
  void timeParse_zeroReps_noInvocation() throws Exception {
    int reps = 0;

    parseBenchmark.timeParse(reps);

    verify(parser, never()).parse(any(char[].class), any());
  }

  @Test
    @Timeout(8000)
  void timeParse_negativeReps_noInvocation() throws Exception {
    int reps = -1;

    parseBenchmark.timeParse(reps);

    verify(parser, never()).parse(any(char[].class), any());
  }
}