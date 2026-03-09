package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BagOfPrimitives_366_5Test {

  @Test
    @Timeout(8000)
  void testEquals_sameObject() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(obj.equals(obj));
  }

  @Test
    @Timeout(8000)
  void testEquals_nullObject() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj.equals("a string"));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentBooleanValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, false, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentIntValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 3, true, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentLongValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(2L, 2, true, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueBothNull() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, null);
    assertTrue(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueOneNull() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueDifferent() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test1");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test2");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueSame() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(obj1.equals(obj2));
  }
}