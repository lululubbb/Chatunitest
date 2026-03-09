package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BagOfPrimitives_361_6Test {

  @Test
    @Timeout(8000)
  public void testNoArgConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives();
    assertEquals(BagOfPrimitives.DEFAULT_VALUE, bag.longValue);
    assertEquals(0, bag.intValue);
    assertFalse(bag.booleanValue);
    assertEquals("", bag.stringValue);
  }

  @Test
    @Timeout(8000)
  public void testAllArgsConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives(123L, 456, true, "test");
    assertEquals(123L, bag.longValue);
    assertEquals(456, bag.intValue);
    assertTrue(bag.booleanValue);
    assertEquals("test", bag.stringValue);
  }

  @Test
    @Timeout(8000)
  public void testGetIntValue() throws Exception {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 789, false, "");
    int intValue = bag.getIntValue();
    assertEquals(789, intValue);
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson() throws Exception {
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
  public void testHashCodeAndEquals() {
    BagOfPrimitives bag1 = new BagOfPrimitives(1L, 2, true, "str");
    BagOfPrimitives bag2 = new BagOfPrimitives(1L, 2, true, "str");
    BagOfPrimitives bag3 = new BagOfPrimitives(2L, 3, false, "other");

    assertEquals(bag1.hashCode(), bag2.hashCode());
    assertTrue(bag1.equals(bag2));
    assertFalse(bag1.equals(bag3));
    assertFalse(bag1.equals(null));
    assertFalse(bag1.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    BagOfPrimitives bag = new BagOfPrimitives(10L, 20, true, "hello");
    String str = bag.toString();
    assertNotNull(str);
    assertTrue(str.contains("10"));
    assertTrue(str.contains("20"));
    assertTrue(str.contains("true"));
    assertTrue(str.contains("hello"));
  }
}