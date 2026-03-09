package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class BagOfPrimitivesDeserializationBenchmark_69_5Test {

  BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Invoke package-private method setUp() via reflection
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp");
    method.setAccessible(true);
    method.invoke(benchmark);

    // Check gson field is initialized
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Gson gson = (Gson) gsonField.get(benchmark);
    assertNotNull(gson);

    // Check json field is initialized and contains expected JSON string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String json = (String) jsonField.get(benchmark);
    assertNotNull(json);
    assertTrue(json.contains("\"longValue\":10"));
    assertTrue(json.contains("\"intValue\":1"));
    assertTrue(json.contains("\"booleanValue\":false"));
    assertTrue(json.contains("\"stringValue\":\"foo\""));
  }
}