package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_365_6Test {

  @Test
    @Timeout(8000)
  void testHashCode_allFieldsNonNullBooleanTrue() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, "testString");
    int expected = 1;
    final int prime = 31;
    expected = prime * expected + 1231;
    expected = prime * expected + 42;
    expected = prime * expected + (int) (123456789L ^ (123456789L >>> 32));
    expected = prime * expected + "testString".hashCode();
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_allFieldsNonNullBooleanFalse() {
    BagOfPrimitives bag = new BagOfPrimitives(987654321L, -100, false, "anotherTest");
    int expected = 1;
    final int prime = 31;
    expected = prime * expected + 1237;
    expected = prime * expected + (-100);
    expected = prime * expected + (int) (987654321L ^ (987654321L >>> 32));
    expected = prime * expected + "anotherTest".hashCode();
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 0, true, null);
    int expected = 1;
    final int prime = 31;
    expected = prime * expected + 1231;
    expected = prime * expected + 0;
    expected = prime * expected + (int) (0L ^ (0L >>> 32));
    expected = prime * expected + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_defaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    int expected = 1;
    final int prime = 31;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + ((bag.stringValue == null) ? 0 : bag.stringValue.hashCode());
    assertEquals(expected, bag.hashCode());
  }
}