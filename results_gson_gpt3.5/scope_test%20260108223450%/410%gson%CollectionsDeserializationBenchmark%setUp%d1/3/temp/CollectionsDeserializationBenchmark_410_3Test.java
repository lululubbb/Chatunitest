package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;

public class CollectionsDeserializationBenchmark_410_3Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new CollectionsDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp() throws Exception {
    // Use reflection to get the private setUp method annotated with @BeforeExperiment
    Method setUpMethod = CollectionsDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);

    // Invoke the setUp method
    setUpMethod.invoke(benchmark);

    // Access private field gson
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonObj = gsonField.get(benchmark);
    assertNotNull(gsonObj);
    assertEquals(Gson.class, gsonObj.getClass());

    // Access private field json
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    Object jsonObj = jsonField.get(benchmark);
    assertNotNull(jsonObj);
    assertTrue(jsonObj instanceof String);

    String json = (String) jsonObj;
    assertTrue(json.startsWith("["));
    assertTrue(json.endsWith("]"));

    // Use Gson instance to deserialize with the original BagOfPrimitives class
    Gson gson = (Gson) gsonObj;
    Field listTypeField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE");
    listTypeField.setAccessible(true);
    Type listType = (Type) listTypeField.get(null);

    @SuppressWarnings("unchecked")
    List<?> bags = gson.fromJson(json, listType);
    assertNotNull(bags);
    assertEquals(100, bags.size());

    Object sample = bags.get(0);

    // Use reflection to access private fields of BagOfPrimitives
    Class<?> bagClass = sample.getClass();

    Field aLongField = null;
    Field anIntField = null;
    Field aBooleanField = null;
    Field aStringField = null;

    for (Field f : bagClass.getDeclaredFields()) {
      f.setAccessible(true);
      Class<?> type = f.getType();
      if (type == Long.class && aLongField == null) {
        aLongField = f;
      } else if (type == long.class && aLongField == null) {
        aLongField = f;
      } else if (type == int.class && anIntField == null) {
        anIntField = f;
      } else if (type == boolean.class && aBooleanField == null) {
        aBooleanField = f;
      } else if (type == String.class && aStringField == null) {
        aStringField = f;
      }
    }

    assertNotNull(aLongField, "aLong field not found");
    assertNotNull(anIntField, "anInt field not found");
    assertNotNull(aBooleanField, "aBoolean field not found");
    assertNotNull(aStringField, "aString field not found");

    // Fix: unbox Long if necessary, or convert Number to long
    Object longValueObj = aLongField.get(sample);
    long aLongValue;
    if (longValueObj instanceof Number) {
      aLongValue = ((Number) longValueObj).longValue();
    } else {
      aLongValue = aLongField.getLong(sample);
    }
    assertEquals(10L, aLongValue);

    int anIntValue = anIntField.getInt(sample);
    assertEquals(1, anIntValue);

    boolean aBooleanValue = aBooleanField.getBoolean(sample);
    assertFalse(aBooleanValue);

    String aStringValue = (String) aStringField.get(sample);
    assertEquals("foo", aStringValue);
  }
}