package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_367_3Test {

  @Test
    @Timeout(8000)
  void testToString_withDefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.longValue = BagOfPrimitives.DEFAULT_VALUE;
    bag.intValue = 0;
    bag.booleanValue = false;
    bag.stringValue = null;

    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        bag.longValue, bag.intValue, bag.booleanValue, bag.stringValue);

    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_withCustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");

    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        123L, 456, true, "testString");

    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_withNullStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(789L, 1011, false, null);

    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        789L, 1011, false, null);

    assertEquals(expected, bag.toString());
  }
}