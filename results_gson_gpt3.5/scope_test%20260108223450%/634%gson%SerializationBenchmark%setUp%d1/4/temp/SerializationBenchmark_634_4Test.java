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

public class SerializationBenchmark_634_4Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testSetUp_withPrettyTrue() throws Exception {
    // Set private field 'pretty' to true using reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Validate gson is created with pretty printing (GsonBuilder)
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Validate bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testSetUp_withPrettyFalse() throws Exception {
    // Set private field 'pretty' to false using reflection
    Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, false);

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Validate gson is created as default Gson instance
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    Object gson = gsonField.get(benchmark);
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Validate bag is initialized correctly
    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    Object bag = bagField.get(benchmark);
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }
}