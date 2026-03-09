package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class CollectionsDeserializationBenchmark_412_6Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Set the private field 'json' via reflection
    String json = "[" +
        "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}," +
        "{\"longValue\":9876543210987,\"intValue\":24,\"booleanValue\":false,\"stringValue\":\"example\"}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  void testTimeCollectionsStreaming_validReps_runsWithoutException() {
    assertDoesNotThrow(() -> benchmark.timeCollectionsStreaming(3));
  }

  @Test
    @Timeout(8000)
  void testTimeCollectionsStreaming_zeroReps_runsWithoutException() {
    assertDoesNotThrow(() -> benchmark.timeCollectionsStreaming(0));
  }

  @Test
    @Timeout(8000)
  void testTimeCollectionsStreaming_unexpectedName_throwsIOException() throws Exception {
    // Inject JSON with unexpected field name to trigger IOException
    String jsonWithUnexpectedName = "[" +
        "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"s\",\"unexpectedName\":5}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonWithUnexpectedName);

    IOException thrown = assertThrows(IOException.class, () -> benchmark.timeCollectionsStreaming(1));
    // Optionally verify exception message contains "Unexpected name"
    assert(thrown.getMessage().contains("Unexpected name"));
  }
}