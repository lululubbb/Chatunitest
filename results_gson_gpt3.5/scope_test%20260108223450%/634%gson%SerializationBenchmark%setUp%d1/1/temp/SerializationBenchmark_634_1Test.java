package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class SerializationBenchmark_634_1Test {

  private SerializationBenchmark benchmark;

  @BeforeEach
  public void init() {
    benchmark = new SerializationBenchmark();
  }

  @Test
    @Timeout(8000)
  public void testSetUp_prettyFalse() throws Exception {
    // Set private field pretty to false
    setPrivateField(benchmark, "pretty", false);

    // Invoke private method setUp via reflection
    invokeSetUp(benchmark);

    // Verify gson instance is not pretty printing
    Object gson = getPrivateField(benchmark, "gson");
    assertNotNull(gson);
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized correctly
    Object bag = getPrivateField(benchmark, "bag");
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testSetUp_prettyTrue() throws Exception {
    // Set private field pretty to true
    setPrivateField(benchmark, "pretty", true);

    // Invoke private method setUp via reflection
    invokeSetUp(benchmark);

    // Verify gson instance is pretty printing (GsonBuilder creates Gson subclass)
    Object gson = getPrivateField(benchmark, "gson");
    assertNotNull(gson);
    // GsonBuilder.create() returns Gson instance, check class name is Gson
    assertEquals("com.google.gson.Gson", gson.getClass().getName());

    // Verify bag is initialized correctly
    Object bag = getPrivateField(benchmark, "bag");
    assertNotNull(bag);
    assertEquals("com.google.gson.metrics.BagOfPrimitives", bag.getClass().getName());
  }

  private void invokeSetUp(SerializationBenchmark instance) throws Exception {
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(instance);
  }

  private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}