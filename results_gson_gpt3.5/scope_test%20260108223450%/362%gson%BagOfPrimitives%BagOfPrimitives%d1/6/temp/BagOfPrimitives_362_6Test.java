package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_362_6Test {

  @Test
    @Timeout(8000)
  void testConstructorAndFields() throws Exception {
    long longVal = 123L;
    int intVal = 456;
    boolean boolVal = true;
    String strVal = "testString";

    BagOfPrimitives bag = new BagOfPrimitives(longVal, intVal, boolVal, strVal);

    // Validate fields set by constructor
    assertEquals(longVal, bag.longValue);
    assertEquals(intVal, bag.intValue);
    assertEquals(boolVal, bag.booleanValue);
    assertEquals(strVal, bag.stringValue);
  }
}