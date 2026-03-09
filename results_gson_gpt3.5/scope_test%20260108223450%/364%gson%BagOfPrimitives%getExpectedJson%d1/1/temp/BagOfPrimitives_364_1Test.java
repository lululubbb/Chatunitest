package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_364_1Test {

  @Test
    @Timeout(8000)
  void testGetExpectedJson_DefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.longValue = 0L;
    bag.intValue = 0;
    bag.booleanValue = false;
    bag.stringValue = null;

    String expected = "{\"longValue\":0,\"intValue\":0,\"booleanValue\":false,\"stringValue\":\"null\"}";
    assertEquals(expected, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson_CustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");

    String expected = "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"testString\"}";
    assertEquals(expected, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson_EmptyStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "");

    String expected = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"\"}";
    assertEquals(expected, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson_SpecialCharactersInString() {
    BagOfPrimitives bag = new BagOfPrimitives(10L, 20, false, "line1\nline2\t\"quote\"");

    String expected = "{\"longValue\":10,\"intValue\":20,\"booleanValue\":false,\"stringValue\":\"line1\nline2\t\"quote\"\"}";
    assertEquals(expected, bag.getExpectedJson());
  }
}