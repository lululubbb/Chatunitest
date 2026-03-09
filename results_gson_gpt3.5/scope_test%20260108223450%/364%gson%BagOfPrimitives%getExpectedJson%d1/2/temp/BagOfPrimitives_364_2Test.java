package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_364_2Test {

  @Test
    @Timeout(8000)
  void testGetExpectedJson_defaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    // Expected JSON with default values: longValue=0, intValue=0, booleanValue=false, stringValue=""
    String expected = "{\"longValue\":0,\"intValue\":0,\"booleanValue\":false,\"stringValue\":\"\"}";
    assertEquals(expected, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson_customValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");
    String expected = "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"testString\"}";
    assertEquals(expected, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson_nullStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, null);
    String expected = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"null\"}";
    assertEquals(expected, bag.getExpectedJson());
  }
}