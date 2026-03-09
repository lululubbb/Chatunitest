package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_361_5Test {

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
    assertEquals(456, bag.getIntValue());
    assertNotNull(bag.getExpectedJson());
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "a");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "a");
    BagOfPrimitives bag3 = new BagOfPrimitives(2L, 3, false, "b");

    assertEquals(bag1, bag2);
    assertEquals(bag1.hashCode(), bag2.hashCode());

    assertNotEquals(bag1, bag3);
    assertNotEquals(bag1.hashCode(), bag3.hashCode());

    assertNotEquals(bag1, null);
    assertNotEquals(bag1, "some string");
  }

  @Test
    @Timeout(8000)
  void testToString() {
    BagOfPrimitives bag = new BagOfPrimitives(10L, 20, true, "hello");
    String toString = bag.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("10"));
    assertTrue(toString.contains("20"));
    assertTrue(toString.contains("true"));
    assertTrue(toString.contains("hello"));
  }
}