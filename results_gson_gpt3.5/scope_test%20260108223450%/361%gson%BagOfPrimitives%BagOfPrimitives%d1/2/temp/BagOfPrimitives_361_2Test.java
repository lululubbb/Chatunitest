package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BagOfPrimitives_361_2Test {

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
  public void testAllArgsConstructorAndGetters() {
    long longVal = 123L;
    int intVal = 456;
    boolean boolVal = true;
    String strVal = "test";

    BagOfPrimitives bag = new BagOfPrimitives(longVal, intVal, boolVal, strVal);
    assertEquals(longVal, bag.longValue);
    assertEquals(intVal, bag.intValue);
    assertEquals(boolVal, bag.booleanValue);
    assertEquals(strVal, bag.stringValue);
  }

  @Test
    @Timeout(8000)
  public void testGetIntValue() {
    BagOfPrimitives bag = new BagOfPrimitives(0L, 789, false, "");
    assertEquals(789, bag.getIntValue());
  }

  @Test
    @Timeout(8000)
  public void testGetExpectedJson() {
    BagOfPrimitives bag = new BagOfPrimitives(1L, 2, true, "abc");
    String expected = bag.getExpectedJson();
    assertNotNull(expected);
    assertTrue(expected.contains("\"longValue\":1"));
    assertTrue(expected.contains("\"intValue\":2"));
    assertTrue(expected.contains("\"booleanValue\":true"));
    assertTrue(expected.contains("\"stringValue\":\"abc\""));
  }

  @Test
    @Timeout(8000)
  public void testHashCodeAndEquals() {
    BagOfPrimitives bag1 = new BagOfPrimitives(10L, 20, true, "x");
    BagOfPrimitives bag2 = new BagOfPrimitives(10L, 20, true, "x");
    BagOfPrimitives bag3 = new BagOfPrimitives(11L, 20, true, "x");

    assertEquals(bag1.hashCode(), bag2.hashCode());
    assertEquals(bag1, bag2);
    assertNotEquals(bag1, bag3);
    assertNotEquals(bag1.hashCode(), bag3.hashCode());

    assertNotEquals(bag1, null);
    assertNotEquals(bag1, "some string");
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    BagOfPrimitives bag = new BagOfPrimitives(5L, 6, false, "str");
    String str = bag.toString();
    assertNotNull(str);
    assertTrue(str.contains("longValue=5"));
    assertTrue(str.contains("intValue=6"));
    assertTrue(str.contains("booleanValue=false"));
    assertTrue(str.contains("stringValue=str"));
  }
}