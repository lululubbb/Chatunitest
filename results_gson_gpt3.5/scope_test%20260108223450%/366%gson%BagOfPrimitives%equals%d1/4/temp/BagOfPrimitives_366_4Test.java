package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BagOfPrimitives_366_4Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    BagOfPrimitives bag = new BagOfPrimitives();
    assertTrue(bag.equals(bag));
  }

  @Test
    @Timeout(8000)
  void testEquals_nullObject() {
    BagOfPrimitives bag = new BagOfPrimitives();
    assertFalse(bag.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    BagOfPrimitives bag = new BagOfPrimitives();
    Object other = new Object();
    assertFalse(bag.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_allFieldsEqual() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, true, "test");
    assertTrue(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentBooleanValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, false, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentIntValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 6, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentLongValue() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(11L, 5, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueNullBoth() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, null);
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, true, null);
    assertTrue(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueNullThis() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, null);
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, true, "test");
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueNullOther() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, true, null);
    assertFalse(bag1.equals(bag2));
  }

  @Test
    @Timeout(8000)
  void testEquals_stringValueDifferent() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 5, true, "test1");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 5, true, "test2");
    assertFalse(bag1.equals(bag2));
  }
}