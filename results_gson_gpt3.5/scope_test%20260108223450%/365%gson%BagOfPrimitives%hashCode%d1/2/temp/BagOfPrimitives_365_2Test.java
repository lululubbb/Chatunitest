package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BagOfPrimitives_365_2Test {

  @Test
    @Timeout(8000)
  public void testHashCode_allFieldsDefault() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 0;
    bag.longValue = 0L;
    bag.stringValue = null;

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + 0;

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

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + 0;

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withIntLongString() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 12345;
    bag.longValue = 9876543210L;
    bag.stringValue = "testString";

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + bag.stringValue.hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 10;
    bag.longValue = 20L;
    bag.stringValue = null;

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + 0;

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_stringValueEmpty() {
    BagOfPrimitives bag = new BagOfPrimitives();
    bag.booleanValue = false;
    bag.intValue = 10;
    bag.longValue = 20L;
    bag.stringValue = "";

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + bag.stringValue.hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_usingConstructor() {
    BagOfPrimitives bag = new BagOfPrimitives(5L, 6, true, "abc");

    int prime = 31;
    int expected = 1;
    expected = prime * expected + (bag.booleanValue ? 1231 : 1237);
    expected = prime * expected + bag.intValue;
    expected = prime * expected + (int) (bag.longValue ^ (bag.longValue >>> 32));
    expected = prime * expected + bag.stringValue.hashCode();

    assertEquals(expected, bag.hashCode());
  }
}