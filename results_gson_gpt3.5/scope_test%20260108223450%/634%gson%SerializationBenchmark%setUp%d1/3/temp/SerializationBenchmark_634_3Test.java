package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SerializationBenchmark_634_3Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp_withPrettyTrue() throws Exception {
    // Set pretty to true via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is created with pretty printing
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonObj = gsonField.get(benchmark);
    assertNotNull(gsonObj);
    assertEquals("com.google.gson.Gson", gsonObj.getClass().getName());

    // Verify bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bagObj = bagField.get(benchmark);
    assertNotNull(bagObj);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bagObj.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testSetUp_withPrettyFalse() throws Exception {
    // Set pretty to false via reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, false);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is created without pretty printing
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gsonObj = gsonField.get(benchmark);
    assertNotNull(gsonObj);
    assertEquals("com.google.gson.Gson", gsonObj.getClass().getName());

    // Verify bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bagObj = bagField.get(benchmark);
    assertNotNull(bagObj);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bagObj.getClass().getName());
  }
}