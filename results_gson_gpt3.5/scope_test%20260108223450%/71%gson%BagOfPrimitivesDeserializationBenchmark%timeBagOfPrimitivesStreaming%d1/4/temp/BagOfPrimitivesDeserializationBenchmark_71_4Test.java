package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.StringReader;

class BagOfPrimitivesDeserializationBenchmark_71_4Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
    // Use reflection to set private field 'json'
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Set a valid JSON string matching expected fields
    jsonField.set(benchmark, "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}");
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesStreaming_repsZero_noException() throws Exception {
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);
    // reps = 0, loop never runs, no exception expected
    method.invoke(benchmark, 0);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesStreaming_repsOne_parsesCorrectly() throws Exception {
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);
    // reps = 1, should parse once without exception
    method.invoke(benchmark, 1);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json field to contain an unexpected field name to trigger IOException
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"unexpectedField\":1}");

    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);

    Throwable thrown = assertThrows(Exception.class, () -> method.invoke(benchmark, 1));
    // InvocationTargetException wraps IOException
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertTrue(thrown.getCause().getMessage().contains("Unexpected name"));
  }
}