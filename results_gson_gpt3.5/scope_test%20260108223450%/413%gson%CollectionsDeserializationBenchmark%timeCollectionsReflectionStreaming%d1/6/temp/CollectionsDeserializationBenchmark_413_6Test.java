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

public class CollectionsDeserializationBenchmark_413_6Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Use reflection to set private field 'json' with a valid JSON array string representing List<BagOfPrimitives>
    String json = "[" +
        "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"test1\"}," +
        "{\"longValue\":3,\"intValue\":4,\"booleanValue\":false,\"stringValue\":\"test2\"}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    // Set gson field (not used in focal method but set for completeness)
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_zeroReps() throws Exception {
    // reps = 0, should do no iterations and no exceptions
    benchmark.timeCollectionsReflectionStreaming(0);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_oneRep() throws Exception {
    // reps = 1, valid JSON input, should parse two BagOfPrimitives objects without exceptions
    benchmark.timeCollectionsReflectionStreaming(1);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_multipleReps() throws Exception {
    // reps = 3, multiple iterations
    benchmark.timeCollectionsReflectionStreaming(3);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_unexpectedFieldType() throws Exception {
    // Modify json field to include a field with unexpected name to trigger RuntimeException

    // Create JSON with an unexpected field name that doesn't match any field in BagOfPrimitives
    String jsonWithUnexpectedField = "[" +
        "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"test1\",\"unexpectedField\":123.45}" +
        "]";

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonWithUnexpectedField);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      benchmark.timeCollectionsReflectionStreaming(1);
    });
    assertTrue(thrown.getMessage().contains("Unexpected"));
  }
}