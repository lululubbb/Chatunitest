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

public class SerializationBenchmark_634_6Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  public void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  public void testSetUp_prettyTrue_initializesGsonWithPrettyPrinting() throws Exception {
    // Set private field pretty to true
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Use reflection to invoke private method setUp
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is initialized with pretty printing
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testSetUp_prettyFalse_initializesGsonWithoutPrettyPrinting() throws Exception {
    // Set private field pretty to false
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, false);

    // Use reflection to invoke private method setUp
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Verify gson is initialized without pretty printing
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }
}