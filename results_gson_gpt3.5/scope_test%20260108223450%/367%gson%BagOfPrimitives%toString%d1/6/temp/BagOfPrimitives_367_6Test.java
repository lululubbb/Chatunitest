package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_367_6Test {

  @Test
    @Timeout(8000)
  void testToString_DefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        0L, 0, false, "");
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_CustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        123L, 456, true, "testString");
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_StringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, null);
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        1L, 2, true, "null");
    assertEquals(expected, bag.toString());
  }
}