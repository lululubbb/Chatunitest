package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_361_3Test {

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
    BagOfPrimitives bag = new BagOfPrimitives(123L, 42, true, "test");
    assertEquals(123L, bag.longValue);
    assertEquals(42, bag.intValue);
    assertTrue(bag.booleanValue);
    assertEquals("test", bag.stringValue);

    assertEquals(42, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson() {
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
  public void testEqualsAndHashCode() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "abc");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "abc");
    BagOfPrimitives bag3 = new BagOfPrimitives(2L, 3, false, "xyz");

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
    BagOfPrimitives bag = new BagOfPrimitives(10L, 20, true, "hello");
    String str = bag.toString();
    assertNotNull(str);
    assertTrue(str.contains("longValue=10"));
    assertTrue(str.contains("intValue=20"));
    assertTrue(str.contains("booleanValue=true"));
    assertTrue(str.contains("stringValue=hello"));
  }
}