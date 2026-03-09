package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class CollectionsDeserializationBenchmark_410_6Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new CollectionsDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    Method setUpMethod = CollectionsDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue, "gson field should be initialized");

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue, "json field should be initialized");
    assertTrue(jsonValue instanceof String, "json field should be a String");

    String jsonString = (String) jsonValue;
    assertTrue(jsonString.startsWith("[") && jsonString.endsWith("]"), "json string should be a JSON array");

    // Additional check: json string should contain serialized BagOfPrimitives with expected values
    assertTrue(jsonString.contains("\"longValue\":10"), "json string should contain longValue 10");
    assertTrue(jsonString.contains("\"intValue\":1"), "json string should contain intValue 1");
    assertTrue(jsonString.contains("\"booleanValue\":false"), "json string should contain booleanValue false");
    assertTrue(jsonString.contains("\"stringValue\":\"foo\""), "json string should contain stringValue foo");
  }
}