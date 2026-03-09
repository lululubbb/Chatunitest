package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_365_4Test {

  @Test
    @Timeout(8000)
  void testHashCode_allFieldsSet() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, "testString");
    int expected = 31 * (31 * (31 * (31 * 1 + 1231) + 42) + (int) (123456789L ^ (123456789L >>> 32)))
        + "testString".hashCode();
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_booleanFalse() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, false, "testString");
    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 42) + (int) (123456789L ^ (123456789L >>> 32)))
        + "testString".hashCode();
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, null);
    int expected = 31 * (31 * (31 * (31 * 1 + 1231) + 42) + (int) (123456789L ^ (123456789L >>> 32))) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_defaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    int expected = 31 * (31 * (31 * (31 * 1 + (bag.booleanValue ? 1231 : 1237)) + bag.intValue)
        + (int) (bag.longValue ^ (bag.longValue >>> 32))) + 0;
    assertEquals(expected, bag.hashCode());
  }
}