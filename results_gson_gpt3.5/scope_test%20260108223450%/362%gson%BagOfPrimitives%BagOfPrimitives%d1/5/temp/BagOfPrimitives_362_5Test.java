package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_362_5Test {

  @Test
    @Timeout(8000)
  void testConstructorAndFields() {
    long longValue = 123L;
    int intValue = 456;
    boolean booleanValue = true;
    String stringValue = "testString";

    BagOfPrimitives bag = new BagOfPrimitives(longValue, intValue, booleanValue, stringValue);

    assertEquals(longValue, bag.longValue);
    assertEquals(intValue, bag.intValue);
    assertEquals(booleanValue, bag.booleanValue);
    assertEquals(stringValue, bag.stringValue);
  }
}