package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class BagOfPrimitivesDeserializationBenchmark_69_2Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to invoke package-private method setUp()
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp");
    method.setAccessible(true);
    method.invoke(benchmark);

    // Validate gson field is initialized
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue);
    assertEquals("com.google.gson.Gson", gsonValue.getClass().getName());

    // Validate json field is initialized with expected json string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue);
    String jsonString = (String) jsonValue;
    // The expected json string for BagOfPrimitives(10L, 1, false, "foo")
    // We expect something like {"longValue":10,"intValue":1,"booleanValue":false,"stringValue":"foo"}
    // But since BagOfPrimitives class is not provided, we check partial content
    assertTrue(jsonString.contains("10"));
    assertTrue(jsonString.contains("1"));
    assertTrue(jsonString.contains("false"));
    assertTrue(jsonString.contains("foo"));
  }
}