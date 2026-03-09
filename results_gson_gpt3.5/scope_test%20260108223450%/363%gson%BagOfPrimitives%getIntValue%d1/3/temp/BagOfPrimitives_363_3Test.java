package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_363_3Test {

  @Test
    @Timeout(8000)
  void testGetIntValue_DefaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    // default intValue should be 0
    assertEquals(0, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_ParameterizedConstructor() {
    int expectedIntValue = 42;
    BagOfPrimitives bag = new BagOfPrimitives(123L, expectedIntValue, true, "test");
    assertEquals(expectedIntValue, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_ModifyFieldDirectly() throws Exception {
    BagOfPrimitives bag = new BagOfPrimitives();
    // Use reflection to set private intValue field
    var intValueField = BagOfPrimitives.class.getDeclaredField("intValue");
    intValueField.setAccessible(true);
    intValueField.setInt(bag, 99);

    assertEquals(99, bag.getIntValue());
  }
}