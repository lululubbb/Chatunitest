package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BagOfPrimitives_366_3Test {

  @Test
    @Timeout(8000)
  void testEquals_SameObject() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(obj.equals(obj));
  }

  @Test
    @Timeout(8000)
  void testEquals_NullObject() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentClass() {
    BagOfPrimitives obj = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentBooleanValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, false, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentIntValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 3, true, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentLongValue() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(2L, 2, true, "test");
    assertFalse(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_StringValueBothNull() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, null);
    assertTrue(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_StringValueOneNull() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(obj1.equals(obj2));

    BagOfPrimitives obj3 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj4 = new BagOfPrimitives(1L, 2, true, null);
    assertFalse(obj3.equals(obj4));
  }

  @Test
    @Timeout(8000)
  void testEquals_StringValueEqual() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(obj1.equals(obj2));
  }

  @Test
    @Timeout(8000)
  void testEquals_StringValueDifferent() {
    BagOfPrimitives obj1 = new BagOfPrimitives(1L, 2, true, "test1");
    BagOfPrimitives obj2 = new BagOfPrimitives(1L, 2, true, "test2");
    assertFalse(obj1.equals(obj2));
  }
}