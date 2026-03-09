package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BagOfPrimitivesDeserializationBenchmark_72_5Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Use reflection to set private field 'json' with a valid JSON string matching BagOfPrimitives fields
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Example JSON string with all expected fields of BagOfPrimitives: longValue, intValue, booleanValue, stringValue
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";
    jsonField.set(benchmark, json);

    // Set private field 'gson' to null or a mock if needed (not used in tested method)
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, null);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_withReps1() throws Exception {
    // Invoke the method with reps = 1
    Method method = BagOfPrimitivesDeserializationBenchmark.class
        .getDeclaredMethod("timeBagOfPrimitivesReflectionStreaming", int.class);
    method.setAccessible(true);

    assertDoesNotThrow(() -> method.invoke(benchmark, 1));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_withReps0() throws Exception {
    // reps = 0 should do nothing but not fail
    Method method = BagOfPrimitivesDeserializationBenchmark.class
        .getDeclaredMethod("timeBagOfPrimitivesReflectionStreaming", int.class);
    method.setAccessible(true);

    assertDoesNotThrow(() -> method.invoke(benchmark, 0));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesReflectionStreaming_unexpectedFieldType() throws Exception {
    // Modify json to include a field not declared in BagOfPrimitives or with unexpected type to trigger RuntimeException
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Add a field with a type not handled, e.g. doubleValue (double.class)
    String jsonWithUnexpectedField = "{\"longValue\":123,\"intValue\":1,\"booleanValue\":true,\"stringValue\":\"test\",\"doubleValue\":1.23}";
    jsonField.set(benchmark, jsonWithUnexpectedField);

    Method method = BagOfPrimitivesDeserializationBenchmark.class
        .getDeclaredMethod("timeBagOfPrimitivesReflectionStreaming", int.class);
    method.setAccessible(true);

    RuntimeException thrown = null;
    try {
      method.invoke(benchmark, 1);
    } catch (Exception e) {
      if (e.getCause() instanceof RuntimeException) {
        thrown = (RuntimeException) e.getCause();
      } else {
        throw e;
      }
    }
    // Assert that RuntimeException with expected message was thrown
    assert thrown != null;
    assert thrown.getMessage().startsWith("Unexpected: type:");
  }
}