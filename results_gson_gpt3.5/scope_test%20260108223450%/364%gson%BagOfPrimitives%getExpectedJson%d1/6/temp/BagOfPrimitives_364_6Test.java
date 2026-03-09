package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_364_6Test {

  @Test
    @Timeout(8000)
  void testGetExpectedJson() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expectedJson = "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"testString\"}";
    assertEquals(expectedJson, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJsonWithDefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.longValue = BagOfPrimitives.DEFAULT_VALUE;
    bag.intValue = 0;
    bag.booleanValue = false;
    bag.stringValue = null;
    String expectedJson = "{\"longValue\":0,\"intValue\":0,\"booleanValue\":false,\"stringValue\":\"null\"}";
    assertEquals(expectedJson, bag.getExpectedJson());
  }
}