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

class SerializationBenchmark_634_2Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp_prettyFalse_initializesFieldsCorrectly() throws Exception {
    // Set pretty to false via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, false);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is instance of Gson (not GsonBuilder's Gson)
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized properly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testSetUp_prettyTrue_initializesFieldsCorrectly() throws Exception {
    // Set pretty to true via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is instance of Gson created by GsonBuilder (class name still Gson)
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized properly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }
}