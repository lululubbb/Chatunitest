package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagOfPrimitives_363_1Test {

  private BagOfPrimitives bag;

  @BeforeEach
  void setUp() {
    bag = new BagOfPrimitives();
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_DefaultConstructor() {
    // default intValue should be 0
    assertEquals(0, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_ParameterizedConstructor() {
    bag = new BagOfPrimitives(123L, 456, true, "test");
    assertEquals(456, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetIntValue_ModifyFieldDirectly() {
    bag.intValue = 789;
    assertEquals(789, bag.getIntValue());
  }
}