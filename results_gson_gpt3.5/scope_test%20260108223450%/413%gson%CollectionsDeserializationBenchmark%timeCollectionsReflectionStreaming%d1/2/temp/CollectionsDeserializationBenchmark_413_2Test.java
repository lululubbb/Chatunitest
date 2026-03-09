package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Field;

public class CollectionsDeserializationBenchmark_413_2Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Use reflection to set private field 'json' with a valid JSON array matching BagOfPrimitives fields
    // BagOfPrimitives fields: longValue (long), intValue (int), booleanValue (boolean), stringValue (String)
    // We'll create JSON with one object with all fields
    String json = "["
        + "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"},"
        + "{\"longValue\":9876543210987,\"intValue\":100,\"booleanValue\":false,\"stringValue\":\"hello\"}"
        + "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_withMultipleReps() throws Exception {
    // Test with 2 repetitions to cover the loop
    int reps = 2;

    // The method does not return anything and no fields are changed, so we just verify no exceptions thrown
    benchmark.timeCollectionsReflectionStreaming(reps);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_withZeroReps() throws Exception {
    // Test with 0 repetitions to cover the loop boundary
    int reps = 0;

    benchmark.timeCollectionsReflectionStreaming(reps);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_withEmptyJsonArray() throws Exception {
    // Set json to empty array to cover branch where jr.hasNext() is false immediately
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "[]");

    int reps = 1;
    benchmark.timeCollectionsReflectionStreaming(reps);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_withUnexpectedFieldType_throwsRuntimeException() throws Exception {
    // Create a JSON with an unexpected field to trigger exception
    String json = "[{\"unexpectedField\":1.23}]";

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      benchmark.timeCollectionsReflectionStreaming(1);
    });
    assertTrue(exception.getMessage().contains("Unexpected: type:"));

    // Also verify that the exception message contains the unexpected field name
    assertTrue(exception.getMessage().contains("unexpectedField"));
  }
}