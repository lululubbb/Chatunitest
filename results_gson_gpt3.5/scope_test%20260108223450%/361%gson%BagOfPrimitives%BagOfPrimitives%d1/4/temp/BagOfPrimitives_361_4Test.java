package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_361_4Test {

  @Test
    @Timeout(8000)
  public void testDefaultConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    assertEquals(BagOfPrimitives.DEFAULT_VALUE, bag.longValue);
    assertEquals(0, bag.intValue);
    assertFalse(bag.booleanValue);
    assertEquals("", bag.stringValue);
  }

  @Test
    @Timeout(8000)
  public void testParameterizedConstructorAndGetters() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "test");
    assertEquals(123L, bag.longValue);
    assertEquals(456, bag.intValue);
    assertTrue(bag.booleanValue);
    assertEquals("test", bag.stringValue);
    assertEquals(456, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "abc");
    String expectedJson = bag.getExpectedJson();
    assertNotNull(expectedJson);
    // Basic checks for JSON structure
    assertTrue(expectedJson.contains("\"longValue\":1"));
    assertTrue(expectedJson.contains("\"intValue\":2"));
    assertTrue(expectedJson.contains("\"booleanValue\":true"));
    assertTrue(expectedJson.contains("\"stringValue\":\"abc\""));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    BagOfPrimitives bag1 = new BagOfPrimitives(5L, 10, true, "x");
    BagOfPrimitives bag2 = new BagOfPrimitives(5L, 10, true, "x");
    BagOfPrimitives bag3 = new BagOfPrimitives(5L, 11, true, "x");

    assertEquals(bag1, bag2);
    assertEquals(bag1.hashCode(), bag2.hashCode());

    assertNotEquals(bag1, bag3);
    assertNotEquals(bag1.hashCode(), bag3.hashCode());

    assertNotEquals(bag1, null);
    assertNotEquals(bag1, "some string");
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    BagOfPrimitives bag = new BagOfPrimitives(7L, 8, false, "str");
    String str = bag.toString();
    assertNotNull(str);
    assertTrue(str.contains("longValue=7"));
    assertTrue(str.contains("intValue=8"));
    assertTrue(str.contains("booleanValue=false"));
    assertTrue(str.contains("stringValue=str"));
  }
}