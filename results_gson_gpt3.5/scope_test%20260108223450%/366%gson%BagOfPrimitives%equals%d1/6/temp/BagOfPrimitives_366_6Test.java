package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BagOfPrimitives_366_6Test {

  @Test
    @Timeout(8000)
  void testEquals_sameObject() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(bag.equals(bag));
  }

  @Test
    @Timeout(8000)
  void testEquals_nullObject() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(bag.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(bag.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentBooleanValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, false, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentIntValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 3, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentLongValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(2L, 2, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueBothNull() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, null);
    assertTrue(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueThisNullOtherNot() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, null);
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueThisNotNullOtherNull() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, null);
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueEquals() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "test");
    assertTrue(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueNotEquals() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "test1");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "test2");
    assertFalse(bag1.equals(bag2));
  }
}