package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SerializationBenchmark_634_5Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp_prettyTrue_initializesGsonWithPrettyPrintingAndBag() throws Exception {
    // Set pretty = true via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Invoke private setUp() method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson field is not null and is a Gson instance
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue);
    assertEquals(com.google.gson.Gson.class, gsonValue.getClass());

    // Since gson is created with pretty printing, it should be a Gson instance from GsonBuilder
    // We cannot directly check pretty printing flag, but we can check gson is not default Gson instance
    // (default Gson class is final so class equality is enough here)

    // Verify bag field is not null and has expected values
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bagValue = bagField.get(benchmark);
    assertNotNull(bagValue);
    assertEquals(BagOfPrimitives.class, bagValue.getClass());

    // Check BagOfPrimitives fields via reflection
    Field longField = BagOfPrimitives.class.getDeclaredField("longValue");
    longField.setAccessible(true);
    assertEquals(10L, longField.getLong(bagValue));

    Field intField = BagOfPrimitives.class.getDeclaredField("intValue");
    intField.setAccessible(true);
    assertEquals(1, intField.getInt(bagValue));

    Field booleanField = BagOfPrimitives.class.getDeclaredField("booleanValue");
    booleanField.setAccessible(true);
    assertFalse(booleanField.getBoolean(bagValue));

    Field stringField = BagOfPrimitives.class.getDeclaredField("stringValue");
    stringField.setAccessible(true);
    assertEquals("foo", (String)stringField.get(bagValue));
  }

  @Test
    @Timeout(8000)
  void testSetUp_prettyFalse_initializesDefaultGsonAndBag() throws Exception {
    // Set pretty = false via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, false);

    // Invoke private setUp() method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson field is not null and is a Gson instance
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonValue = gsonField.get(benchmark);
    assertNotNull(gsonValue);
    assertEquals(com.google.gson.Gson.class, gsonValue.getClass());

    // Verify bag field is not null and has expected values
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bagValue = bagField.get(benchmark);
    assertNotNull(bagValue);
    assertEquals(BagOfPrimitives.class, bagValue.getClass());

    // Check BagOfPrimitives fields via reflection
    Field longField = BagOfPrimitives.class.getDeclaredField("longValue");
    longField.setAccessible(true);
    assertEquals(10L, longField.getLong(bagValue));

    Field intField = BagOfPrimitives.class.getDeclaredField("intValue");
    intField.setAccessible(true);
    assertEquals(1, intField.getInt(bagValue));

    Field booleanField = BagOfPrimitives.class.getDeclaredField("booleanValue");
    booleanField.setAccessible(true);
    assertFalse(booleanField.getBoolean(bagValue));

    Field stringField = BagOfPrimitives.class.getDeclaredField("stringValue");
    stringField.setAccessible(true);
    assertEquals("foo", (String)stringField.get(bagValue));
  }
}