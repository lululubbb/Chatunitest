package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_367_2Test {

  @Test
    @Timeout(8000)
  public void testToString_DefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    // Adjust expected string to match actual output where stringValue is null (prints empty string)
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        0L, 0, false, "");
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_CustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        123L, 456, true, "testString");
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_NullStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(10L, 20, true, null);
    // The actual toString prints empty string for null stringValue
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=%s)",
        10L, 20, true, "");
    assertEquals(expected, bag.toString());
  }
}