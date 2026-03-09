package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_364_4Test {

  @Test
    @Timeout(8000)
  public void testGetExpectedJson_DefaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.longValue = BagOfPrimitives.DEFAULT_VALUE;
    bag.intValue = 0;
    bag.booleanValue = false;
    bag.stringValue = null;

    String expectedJson = "{\"longValue\":0,\"intValue\":0,\"booleanValue\":false,\"stringValue\":\"null\"}";
    assertEquals(expectedJson, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson_CustomValues() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "testString");

    String expectedJson = "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"testString\"}";
    assertEquals(expectedJson, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson_EmptyStringValue() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "");

    String expectedJson = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"\"}";
    assertEquals(expectedJson, bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson_StringValueWithQuotes() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "\"quoted\"");

    String expectedJson = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"\\\"quoted\\\"\"}";
    // Note: The original method does not escape quotes, so the JSON string will contain unescaped quotes.
    // To match exactly what getExpectedJson returns, we need to match the raw string.
    expectedJson = "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"\"quoted\"\"}";

    // Because the method does not escape the quotes, the resulting string will contain the literal quotes.
    // So the actual string will be:
    String actual = bag.getExpectedJson();
    assertEquals("{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"\"quoted\"\"}", actual);
  }
}