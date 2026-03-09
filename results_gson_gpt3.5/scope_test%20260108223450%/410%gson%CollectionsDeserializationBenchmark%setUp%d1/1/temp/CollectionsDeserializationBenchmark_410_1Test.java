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

class CollectionsDeserializationBenchmark_410_1Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new CollectionsDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to get the private method setUp()
    Method setUpMethod = CollectionsDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);

    // Invoke the private setUp method
    setUpMethod.invoke(benchmark);

    // Verify the gson field is initialized
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue, "gson field should be initialized");

    // Verify the json field is initialized and is a non-empty string
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue, "json field should be initialized");
    assertTrue(jsonValue instanceof String, "json field should be a String");
    String jsonString = (String) jsonValue;
    assertFalse(jsonString.isEmpty(), "json string should not be empty");

    // Additionally verify the json string starts with '[' and ends with ']' indicating a JSON array
    assertTrue(jsonString.startsWith("["), "json string should start with '['");
    assertTrue(jsonString.endsWith("]"), "json string should end with ']'");
  }
}