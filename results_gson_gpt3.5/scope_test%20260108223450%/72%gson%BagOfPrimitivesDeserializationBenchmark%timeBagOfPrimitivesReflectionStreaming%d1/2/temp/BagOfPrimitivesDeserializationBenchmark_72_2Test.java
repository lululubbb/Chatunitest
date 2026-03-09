package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.stream.JsonReader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BagOfPrimitivesDeserializationBenchmark_72_2Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Use reflection to set private field 'json' with valid JSON string representing BagOfPrimitives
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_validJson_multipleReps() throws Exception {
    // Run with reps = 3 to cover multiple iterations
    benchmark.timeBagOfPrimitivesReflectionStreaming(3);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_zeroReps() throws Exception {
    // reps = 0 should do nothing but not fail
    benchmark.timeBagOfPrimitivesReflectionStreaming(0);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_unexpectedFieldType_throwsRuntimeException() throws Exception {
    // Set json with a field that does not match any expected type
    String jsonWithUnexpectedField = "{\"longValue\":123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\",\"unexpectedField\":1.23}";
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonWithUnexpectedField);

    // This will throw RuntimeException due to unexpectedField type double
    assertThrows(RuntimeException.class, () -> benchmark.timeBagOfPrimitivesReflectionStreaming(1));
  }
}