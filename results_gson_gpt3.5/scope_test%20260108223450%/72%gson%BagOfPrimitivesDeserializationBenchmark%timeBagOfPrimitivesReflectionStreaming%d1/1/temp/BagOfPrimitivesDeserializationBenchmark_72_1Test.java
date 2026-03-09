package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagOfPrimitivesDeserializationBenchmark_72_1Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesReflectionStreaming_validJson() throws Exception {
    // Prepare JSON string with all fields of BagOfPrimitives
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";
    // Use reflection to set private field 'json'
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    // Run with reps=1, should parse without exceptions
    benchmark.timeBagOfPrimitivesReflectionStreaming(1);
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesReflectionStreaming_unexpectedFieldType_throws() throws Exception {
    // Prepare JSON with a field that does not match expected types
    String json = "{\"longValue\":123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\",\"unknownField\":{}}";
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    // We expect RuntimeException due to unexpected field type
    assertThrows(RuntimeException.class, () -> benchmark.timeBagOfPrimitivesReflectionStreaming(1));
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesReflectionStreaming_multipleReps() throws Exception {
    String json = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":false,\"stringValue\":\"abc\"}";
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    // Run with multiple repetitions to cover loop
    benchmark.timeBagOfPrimitivesReflectionStreaming(5);
  }
}