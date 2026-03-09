package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_365_3Test {

  @Test
    @Timeout(8000)
  void testHashCode_allFieldsSet() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, "testString");
    int expected = 1;
    int prime = 31;
    expected = prime * expected + (true ? 1231 : 1237);
    expected = prime * expected + 42;
    expected = prime * expected + (int) (123456789L ^ (123456789L >>> 32));
    expected = prime * expected + "testString".hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_booleanFalse() {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 0, false, "abc");
    int expected = 1;
    int prime = 31;
    expected = prime * expected + (false ? 1231 : 1237);
    expected = prime * expected + 0;
    expected = prime * expected + (int) (0L ^ (0L >>> 32));
    expected = prime * expected + "abc".hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives(999L, -10, true, null);
    int expected = 1;
    int prime = 31;
    expected = prime * expected + (true ? 1231 : 1237);
    expected = prime * expected + (-10);
    expected = prime * expected + (int) (999L ^ (999L >>> 32));
    expected = prime * expected + 0;

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_defaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    int expected = 1;
    int prime = 31;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + (bag.stringValue == null ? 0 : bag.stringValue.hashCode());

    assertEquals(expected, bag.hashCode());
  }
}