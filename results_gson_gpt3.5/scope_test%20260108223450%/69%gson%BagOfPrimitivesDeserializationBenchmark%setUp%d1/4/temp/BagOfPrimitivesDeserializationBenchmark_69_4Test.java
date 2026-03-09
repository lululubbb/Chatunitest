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

class BagOfPrimitivesDeserializationBenchmark_69_4Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to invoke the private setUp method
    Field setUpMethod = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp").getDeclaringClass().getDeclaredField("json");
    setUpMethod.setAccessible(true);

    // Invoke the setUp method via reflection
    BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp").invoke(benchmark);

    // Verify gson field is initialized
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue, "gson field should be initialized");

    // Verify json field is initialized and contains expected JSON string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String jsonValue = (String) jsonField.get(benchmark);
    assertNotNull(jsonValue, "json field should be initialized");
    assertTrue(jsonValue.contains("\"longValue\":10"), "json should contain longValue 10");
    assertTrue(jsonValue.contains("\"intValue\":1"), "json should contain intValue 1");
    assertTrue(jsonValue.contains("\"booleanValue\":false"), "json should contain booleanValue false");
    assertTrue(jsonValue.contains("\"stringValue\":\"foo\""), "json should contain stringValue foo");
  }
}