package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_367_4Test {

  @Test
    @Timeout(8000)
  void toString_ShouldReturnFormattedString_WithAllFieldValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expected = "(longValue=123,intValue=456,booleanValue=true,stringValue=testString)";
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void toString_ShouldHandleNullStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 0, false, null);
    String expected = "(longValue=0,intValue=0,booleanValue=false,stringValue=null)";
    assertEquals(expected, bag.toString());
  }

  @Test
    @Timeout(8000)
  void toString_ShouldHandleDefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    String expected = String.format("(longValue=%d,intValue=%d,booleanValue=%b,stringValue=)",
        BagOfPrimitives.DEFAULT_VALUE, 0, false);
    assertEquals(expected, bag.toString());
  }
}