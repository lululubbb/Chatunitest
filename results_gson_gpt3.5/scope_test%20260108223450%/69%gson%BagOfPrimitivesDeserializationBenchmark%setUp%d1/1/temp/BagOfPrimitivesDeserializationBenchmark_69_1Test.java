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

import java.lang.reflect.Method;
import java.lang.reflect.Field;

class BagOfPrimitivesDeserializationBenchmark_69_1Test {

  BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to invoke package-private method setUp()
    Method setUpMethod = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Validate gson field is initialized
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue, "gson field should be initialized");

    // Validate json field is initialized and contains expected json string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue, "json field should be initialized");
    String jsonStr = (String) jsonValue;
    assertTrue(jsonStr.contains("\"longValue\":10"), "json should contain longValue 10");
    assertTrue(jsonStr.contains("\"intValue\":1"), "json should contain intValue 1");
    assertTrue(jsonStr.contains("\"booleanValue\":false"), "json should contain booleanValue false");
    assertTrue(jsonStr.contains("\"stringValue\":\"foo\""), "json should contain stringValue foo");
  }
}