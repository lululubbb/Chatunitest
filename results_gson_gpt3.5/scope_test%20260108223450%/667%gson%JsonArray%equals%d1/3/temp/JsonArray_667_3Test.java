package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class JsonArray_667_3Test {

  @Test
    @Timeout(8000)
  public void testEquals_SameReference() {
    JsonArray array = new JsonArray();
    assertTrue(array.equals(array));
  }

  @Test
    @Timeout(8000)
  public void testEquals_NullObject() {
    JsonArray array = new JsonArray();
    assertFalse(array.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentClass() {
    JsonArray array = new JsonArray();
    Object other = new Object();
    assertFalse(array.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_EmptyArrays() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    assertTrue(array1.equals(array2));
    assertTrue(array2.equals(array1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_SameElements() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    JsonPrimitive element1 = new JsonPrimitive("test");
    JsonPrimitive element2 = new JsonPrimitive("test");
    array1.add(element1);
    array2.add(element2);
    assertTrue(array1.equals(array2));
    assertTrue(array2.equals(array1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentElements() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    JsonPrimitive element1 = new JsonPrimitive("test1");
    JsonPrimitive element2 = new JsonPrimitive("test2");
    array1.add(element1);
    array2.add(element2);
    assertFalse(array1.equals(array2));
    assertFalse(array2.equals(array1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentSize() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    JsonPrimitive element1 = new JsonPrimitive("test");
    array1.add(element1);
    assertFalse(array1.equals(array2));
    assertFalse(array2.equals(array1));
  }
}