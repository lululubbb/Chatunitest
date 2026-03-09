package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_362_4Test {

  @Test
    @Timeout(8000)
  void testConstructorAndFields() {
    long longVal = 123L;
    int intVal = 456;
    boolean boolVal = true;
    String strVal = "testString";

    BagOfPrimitives bag = new BagOfPrimitives(longVal, intVal, boolVal, strVal);

    assertEquals(longVal, bag.longValue);
    assertEquals(intVal, bag.intValue);
    assertEquals(boolVal, bag.booleanValue);
    assertEquals(strVal, bag.stringValue);
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructorAndDefaults() {
    BagOfPrimitives bag = new BagOfPrimitives();

    assertEquals(BagOfPrimitives.DEFAULT_VALUE, bag.longValue);
    assertEquals(0, bag.intValue);
    assertFalse(bag.booleanValue);
    assertEquals("", bag.stringValue);
  }
}