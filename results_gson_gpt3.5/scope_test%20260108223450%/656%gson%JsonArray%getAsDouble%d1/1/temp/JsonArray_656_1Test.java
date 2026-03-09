package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_656_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_singleElement() {
    // Add one JsonPrimitive element with a double value
    jsonArray.add(new JsonPrimitive(3.14));

    double result = jsonArray.getAsDouble();

    assertEquals(3.14, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_emptyArray_throws() throws Exception {
    // Use reflection to invoke private getAsSingleElement method on empty array
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    }, "Expected InvocationTargetException due to IllegalStateException inside");
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_multipleElements_throws() throws Exception {
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    // getAsSingleElement throws if size != 1, so test that behavior
    assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    }, "Expected InvocationTargetException due to IllegalStateException inside");
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_returnsElement() throws Exception {
    jsonArray.add(new JsonPrimitive(42.0));

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement singleElement = (JsonElement) method.invoke(jsonArray);

    assertEquals(new JsonPrimitive(42.0), singleElement);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_elementReturnsDouble() {
    // Create a JsonElement subclass that implements deepCopy()
    JsonElement element = new JsonElement() {
      @Override
      public double getAsDouble() {
        return 9.81;
      }

      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    jsonArray.add(element);

    double result = jsonArray.getAsDouble();

    assertEquals(9.81, result, 0.000001);
  }
}