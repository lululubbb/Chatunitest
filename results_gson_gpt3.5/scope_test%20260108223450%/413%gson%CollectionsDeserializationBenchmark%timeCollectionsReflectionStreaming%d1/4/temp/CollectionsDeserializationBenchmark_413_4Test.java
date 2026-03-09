package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CollectionsDeserializationBenchmark_413_4Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Use reflection to set the private 'json' field with a valid JSON string for testing
    String json = "[" +
        "{" +
        "\"longValue\":1234567890123," +
        "\"intValue\":42," +
        "\"booleanValue\":true," +
        "\"stringValue\":\"test string\"" +
        "}," +
        "{" +
        "\"longValue\":9876543210987," +
        "\"intValue\":7," +
        "\"booleanValue\":false," +
        "\"stringValue\":\"another string\"" +
        "}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_zeroReps() throws Exception {
    // reps = 0, method should complete without exceptions and do no iterations
    benchmark.timeCollectionsReflectionStreaming(0);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_multipleReps() throws Exception {
    // reps = 1 to test normal deserialization flow
    benchmark.timeCollectionsReflectionStreaming(1);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_multipleReps_twice() throws Exception {
    // reps = 2 to test multiple iterations
    benchmark.timeCollectionsReflectionStreaming(2);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_invalidFieldType_throwsRuntimeException() throws Exception {
    // Modify the json to include an unsupported field with a JSON number containing a decimal point
    String jsonWithExtraField = "[" +
        "{" +
        "\"longValue\":1234567890123," +
        "\"intValue\":42," +
        "\"booleanValue\":true," +
        "\"stringValue\":\"test string\"," +
        "\"unsupportedField\":123.456" +
        "}" +
        "]";

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonWithExtraField);

    // We expect a RuntimeException due to unsupported field type in BagOfPrimitives, so assertThrows:
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> benchmark.timeCollectionsReflectionStreaming(1));
    assertTrue(thrown.getMessage().contains("Unexpected: type"));
  }
}