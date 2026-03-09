package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BagOfPrimitives_365_1Test {

  @Test
    @Timeout(8000)
  public void testHashCode_allFieldsDefault() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.longValue = 0L;
    bag.intValue = 0;
    bag.booleanValue = false;
    bag.stringValue = null;

    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 0) + 0) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_booleanTrue() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = true;
    bag.intValue = 0;
    bag.longValue = 0L;
    bag.stringValue = null;

    int expected = 31 * (31 * (31 * (31 * 1 + 1231) + 0) + 0) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_intValue() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 42;
    bag.longValue = 0L;
    bag.stringValue = null;

    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 42) + 0) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_longValue() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 0;
    bag.longValue = 0x123456789ABCDEFL;
    bag.stringValue = null;

    int longHash = (int)(bag.longValue ^ (bag.longValue >>> 32));
    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 0) + longHash) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 0;
    bag.longValue = 0L;
    bag.stringValue = null;

    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 0) + 0) + 0;
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_stringValueNonNull() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 0;
    bag.longValue = 0L;
    bag.stringValue = "testString";

    int expected = 31 * (31 * (31 * (31 * 1 + 1237) + 0) + 0) + bag.stringValue.hashCode();
    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_allFieldsSet() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789012345L, 987654321, true, "hashCodeTest");

    int longHash = (int)(bag.longValue ^ (bag.longValue >>> 32));
    int expected = 31 * (31 * (31 * (31 * 1 + 1231) + bag.intValue) + longHash) + bag.stringValue.hashCode();

    assertEquals(expected, bag.hashCode());
  }
}