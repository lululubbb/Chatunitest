package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_362_3Test {

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

  @Test
    @Timeout(8000)
  void testDefaultConstructor() throws Exception {
    BagOfPrimitives bag = new BagOfPrimitives();

    // Using reflection to verify default values of fields
    var longField = BagOfPrimitives.class.getField("longValue");
    var intField = BagOfPrimitives.class.getField("intValue");
    var booleanField = BagOfPrimitives.class.getField("booleanValue");
    var stringField = BagOfPrimitives.class.getField("stringValue");

    assertEquals(0L, longField.getLong(bag));
    assertEquals(0, intField.getInt(bag));
    assertFalse(booleanField.getBoolean(bag));
    assertEquals("", stringField.get(bag));
  }
}