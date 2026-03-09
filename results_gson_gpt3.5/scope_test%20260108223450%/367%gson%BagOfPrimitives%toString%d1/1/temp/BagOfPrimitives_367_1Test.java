package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_367_1Test {

  @Test
    @Timeout(8000)
  public void testToString_withDefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    String expected = String.format(
        "(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        bag.longValue, bag.intValue, bag.booleanValue, bag.stringValue);
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withCustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expected = String.format(
        "(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        123L, 456, true, "testString");
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withNullStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(789L, 321, false, null);
    String expected = String.format(
        "(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        789L, 321, false, null);
    assertEquals(expected, bag.toString());
  }
}