package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsDeserializationBenchmark_410_5Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new CollectionsDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to get the private method setUp
    Method setUpMethod = CollectionsDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);

    // Invoke the setUp method
    setUpMethod.invoke(benchmark);

    // Use reflection to access the private gson field
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue);
    assertTrue(gsonValue instanceof Gson);

    // Use reflection to access the private json field
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonValue = jsonField.get(benchmark);
    assertNotNull(jsonValue);
    assertTrue(jsonValue instanceof String);

    String jsonString = (String) jsonValue;
    assertFalse(jsonString.isEmpty());

    // Deserialize json string back to List<BagOfPrimitives> using Gson to check correctness
    Field listTypeField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE");
    listTypeField.setAccessible(true);
    Type listType = (Type) listTypeField.get(null);

    @SuppressWarnings("unchecked")
    List<?> bags = ((Gson) gsonValue).fromJson(jsonString, listType);
    assertNotNull(bags);
    assertEquals(100, bags.size());

    // Check first element fields are as expected
    Object firstBag = bags.get(0);
    assertNotNull(firstBag);
    Class<?> bagClass = firstBag.getClass();

    Field longField = bagClass.getDeclaredField("longValue");
    longField.setAccessible(true);
    assertEquals(10L, longField.getLong(firstBag));

    Field intField = bagClass.getDeclaredField("intValue");
    intField.setAccessible(true);
    assertEquals(1, intField.getInt(firstBag));

    Field booleanField = bagClass.getDeclaredField("booleanValue");
    booleanField.setAccessible(true);
    assertFalse(booleanField.getBoolean(firstBag));

    Field stringField = bagClass.getDeclaredField("stringValue");
    stringField.setAccessible(true);
    assertEquals("foo", stringField.get(firstBag));
  }
}