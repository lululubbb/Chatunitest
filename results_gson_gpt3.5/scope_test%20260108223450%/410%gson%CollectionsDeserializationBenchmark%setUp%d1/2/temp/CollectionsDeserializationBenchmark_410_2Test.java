package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;

class CollectionsDeserializationBenchmark_410_2Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new CollectionsDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to invoke private method setUp()
    Method setUpMethod = CollectionsDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson field is initialized and not null
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Gson gsonValue = (Gson) gsonField.get(benchmark);
    assertNotNull(gsonValue, "gson field should be initialized");

    // Verify json field is initialized and contains expected JSON string
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String jsonValue = (String) jsonField.get(benchmark);
    assertNotNull(jsonValue, "json field should be initialized");
    assertFalse(jsonValue.isEmpty(), "json field should not be empty");

    // Access private static final field LIST_TYPE using setAccessible(true)
    Field listTypeField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE");
    listTypeField.setAccessible(true);
    Type listType = (Type) listTypeField.get(null);

    List<?> deserializedList = gsonValue.fromJson(jsonValue, listType);
    assertNotNull(deserializedList, "Deserialized list should not be null");
    assertEquals(100, deserializedList.size(), "Deserialized list should contain 100 elements");
  }
}