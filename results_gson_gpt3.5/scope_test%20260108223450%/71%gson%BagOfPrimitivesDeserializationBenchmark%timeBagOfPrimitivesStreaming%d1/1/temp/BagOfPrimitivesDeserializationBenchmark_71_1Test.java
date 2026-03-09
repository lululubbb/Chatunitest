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
import java.lang.reflect.InvocationTargetException;

public class BagOfPrimitivesDeserializationBenchmark_71_1Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Set private field 'json' via reflection
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // A valid JSON string matching the expected fields
    String jsonValue = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";
    jsonField.set(benchmark, jsonValue);

    // Set private field 'gson' to null or a mock if needed, here null is fine since not used in focal method
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, null);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_zeroReps() throws Exception {
    // reps = 0, loop body never executed, no exception expected
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);
    method.invoke(benchmark, 0);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_oneRep() throws Exception {
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);
    method.invoke(benchmark, 1);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_multipleReps() throws Exception {
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);
    method.invoke(benchmark, 5);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_unexpectedNameThrowsIOException() throws Exception {
    // Set json field to JSON with unexpected property name
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String badJson = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"unexpectedName\":3}";
    jsonField.set(benchmark, badJson);

    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesStreaming", int.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(benchmark, 1));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().contains("Unexpected name"));
  }
}