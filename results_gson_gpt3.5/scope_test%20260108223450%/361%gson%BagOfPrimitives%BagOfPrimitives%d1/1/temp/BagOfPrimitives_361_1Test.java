package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_361_1Test {

  @Test
    @Timeout(8000)
  void testNoArgConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    assertEquals(BagOfPrimitives.DEFAULT_VALUE, bag.longValue);
    assertEquals(0, bag.intValue);
    assertFalse(bag.booleanValue);
    assertEquals("", bag.stringValue);
  }

  @Test
    @Timeout(8000)
  void testAllArgsConstructorAndGetters() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "test");
    assertEquals(123L, bag.longValue);
    assertEquals(456, bag.intValue);
    assertTrue(bag.booleanValue);
    assertEquals("test", bag.stringValue);
  }

  @Test
    @Timeout(8000)
  void testGetIntValue() {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 42, false, "");
    assertEquals(42, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  void testGetExpectedJson() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "abc");
    String expectedJson = bag.getExpectedJson();
    assertNotNull(expectedJson);
    assertTrue(expectedJson.contains("\"longValue\":1"));
    assertTrue(expectedJson.contains("\"intValue\":2"));
    assertTrue(expectedJson.contains("\"booleanValue\":true"));
    assertTrue(expectedJson.contains("\"stringValue\":\"abc\""));
  }

  @Test
    @Timeout(8000)
  void testHashCodeAndEquals() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "abc");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "abc");
    BagOfPrimitives bag3 = new BagOfPrimitives(2L, 3, false, "def");

    assertEquals(bag1.hashCode(), bag2.hashCode());
    assertEquals(bag1, bag2);
    assertNotEquals(bag1, bag3);
    assertNotEquals(bag1.hashCode(), bag3.hashCode());
    assertNotEquals(bag1, null);
    assertNotEquals(bag1, new Object());
  }

  @Test
    @Timeout(8000)
  void testToString() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "abc");
    String s = bag.toString();
    assertNotNull(s);
    assertTrue(s.contains("longValue=1"));
    assertTrue(s.contains("intValue=2"));
    assertTrue(s.contains("booleanValue=true"));
    assertTrue(s.contains("stringValue=abc"));
  }
}