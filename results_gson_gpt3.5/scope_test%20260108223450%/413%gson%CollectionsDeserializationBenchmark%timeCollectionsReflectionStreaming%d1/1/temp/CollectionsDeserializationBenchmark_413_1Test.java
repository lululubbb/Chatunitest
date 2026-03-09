package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CollectionsDeserializationBenchmark_413_1Test {

  private CollectionsDeserializationBenchmark benchmark;

  private static final String TEST_JSON = "[" +
      "{\"longValue\":1234567890123,\"intValue\":123456,\"booleanValue\":true,\"stringValue\":\"test1\"}," +
      "{\"longValue\":9876543210987,\"intValue\":654321,\"booleanValue\":false,\"stringValue\":\"test2\"}" +
      "]";

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Use reflection to set the private field 'json' to TEST_JSON
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, TEST_JSON);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_runsWithoutException() throws Exception {
    // Invoke the public method timeCollectionsReflectionStreaming with reps=1
    assertDoesNotThrow(() -> benchmark.timeCollectionsReflectionStreaming(1));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsReflectionStreaming_multipleReps() throws Exception {
    // Invoke the public method timeCollectionsReflectionStreaming with reps=3
    assertDoesNotThrow(() -> benchmark.timeCollectionsReflectionStreaming(3));
  }

  @Test
    @Timeout(8000)
  public void testPrivateFieldsInBagOfPrimitives() throws Exception {
    // Reflectively verify BagOfPrimitives fields are present and accessible
    Class<?> bagClass = Class.forName("com.google.gson.metrics.BagOfPrimitives");
    Field longField = bagClass.getDeclaredField("longValue");
    Field intField = bagClass.getDeclaredField("intValue");
    Field booleanField = bagClass.getDeclaredField("booleanValue");
    Field stringField = bagClass.getDeclaredField("stringValue");

    longField.setAccessible(true);
    intField.setAccessible(true);
    booleanField.setAccessible(true);
    stringField.setAccessible(true);
  }
}