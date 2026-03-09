package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_637_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() throws Exception {
    // Use constructor with capacity parameter
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    jsonArray = constructor.newInstance(5);
  }

  @Test
    @Timeout(8000)
  public void testConstructorInitializesElementsWithCapacity() throws Exception {
    // Access private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertNotNull(elements);
    // The ArrayList capacity is not directly accessible, but size should be 0 initially
    assertEquals(0, elements.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithZeroCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray zeroCapacityArray = constructor.newInstance(0);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(zeroCapacityArray);
    assertNotNull(elements);
    assertEquals(0, elements.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithNegativeCapacityThrows() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance(-1);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }
}