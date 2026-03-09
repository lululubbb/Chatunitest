package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_363_5Test {

  @Test
    @Timeout(8000)
  public void testGetIntValue_DefaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    // default intValue should be 0
    assertEquals(0, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  public void testGetIntValue_ParameterizedConstructor() {
    int expectedInt = 42;
    BagOfPrimitives bag = new BagOfPrimitives(10L, expectedInt, true, "test");
    assertEquals(expectedInt, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  public void testGetIntValue_ModifyFieldDirectly() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.intValue = 99;
    assertEquals(99, bag.getIntValue());
  }
}