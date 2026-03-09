package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;

public class CollectionsDeserializationBenchmark_412_3Test {

  private CollectionsDeserializationBenchmark benchmark;

  private static final String VALID_JSON = "[" +
      "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"test1\"}," +
      "{\"longValue\":789,\"intValue\":1011,\"booleanValue\":false,\"stringValue\":\"test2\"}" +
      "]";

  private static final String JSON_WITH_UNEXPECTED_FIELD = "[" +
      "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"test1\",\"unexpected\":0}" +
      "]";

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Use reflection to set private field 'json'
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, VALID_JSON);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_validJson_parsesAllObjects() throws IOException, Exception {
    // reps = 2 to run loop multiple times
    benchmark.timeCollectionsStreaming(2);
    // No exception means success, but we can also verify internal state via reflection if needed

    // Verify that json field is unchanged
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    assertEquals(VALID_JSON, jsonField.get(benchmark));
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_zeroReps_noException() throws IOException {
    benchmark.timeCollectionsStreaming(0);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_unexpectedField_throwsIOException() throws Exception {
    // Set json field to JSON with unexpected field to trigger IOException
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, JSON_WITH_UNEXPECTED_FIELD);

    IOException thrown = assertThrows(IOException.class, () -> benchmark.timeCollectionsStreaming(1));
    assertTrue(thrown.getMessage().contains("Unexpected name"));
  }
}