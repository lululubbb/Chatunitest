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

class BagOfPrimitivesDeserializationBenchmark_69_3Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

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

    // Verify gson field is initialized
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue);
    assertEquals(com.google.gson.Gson.class, gsonValue.getClass());

    // Verify json field is initialized with expected JSON string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue);
    String expectedJson = "{\"longValue\":10,\"intValue\":1,\"booleanValue\":false,\"stringValue\":\"foo\"}";
    assertEquals(expectedJson, jsonValue);
  }
}