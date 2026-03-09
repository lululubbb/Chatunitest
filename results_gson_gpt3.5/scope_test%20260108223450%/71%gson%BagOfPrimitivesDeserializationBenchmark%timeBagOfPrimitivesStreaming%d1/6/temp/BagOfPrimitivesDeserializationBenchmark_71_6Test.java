package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class BagOfPrimitivesDeserializationBenchmark_71_6Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Use reflection to set the private 'json' field
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Provide valid JSON matching the expected fields
    jsonField.set(benchmark, "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}");
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesStreaming_zeroReps() throws Exception {
    // reps = 0, no iteration, no exception
    benchmark.timeBagOfPrimitivesStreaming(0);
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesStreaming_oneRep() throws Exception {
    // reps = 1, should parse JSON once without exception
    benchmark.timeBagOfPrimitivesStreaming(1);
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesStreaming_multipleReps() throws Exception {
    // reps = 5, multiple iterations
    benchmark.timeBagOfPrimitivesStreaming(5);
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesStreaming_unexpectedNameThrows() throws Exception {
    // Set json with an unexpected field to trigger IOException
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"str\",\"unexpected\":\"value\"}");

    IOException thrown = assertThrows(IOException.class, () -> benchmark.timeBagOfPrimitivesStreaming(1));
    assertTrue(thrown.getMessage().contains("Unexpected name"));
  }

  @Test
    @Timeout(8000)
  void testPrivateGsonFieldExists() throws Exception {
    // Just verify private Gson field exists and is accessible
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    assertNotNull(gsonField);
  }

  @Test
    @Timeout(8000)
  void testInvokeTimeBagOfPrimitivesStreamingUsingReflection() throws Exception {
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);

    // reps = 2
    method.invoke(benchmark, 2);
  }
}