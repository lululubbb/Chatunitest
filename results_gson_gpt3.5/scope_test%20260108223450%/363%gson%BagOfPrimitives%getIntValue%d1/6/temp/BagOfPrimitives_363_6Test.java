package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_363_6Test {

  @Test
    @Timeout(8000)
  void testGetIntValue_DefaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    // default intValue is 0
    assertEquals(0, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_ParameterizedConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "test");
    assertEquals(456, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_AfterFieldSet() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.intValue = 789;
    assertEquals(789, bag.getIntValue());
  }
}