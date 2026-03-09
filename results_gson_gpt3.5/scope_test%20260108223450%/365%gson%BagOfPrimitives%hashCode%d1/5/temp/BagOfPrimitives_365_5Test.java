package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BagOfPrimitives_365_5Test {

  @Test
    @Timeout(8000)
  void hashCode_allFieldsSet() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, "testString");

    int expected = 1;
    expected = 31 * expected + (true ? 1231 : 1237);
    expected = 31 * expected + 42;
    expected = 31 * expected + (int) (123456789L ^ (123456789L >>> 32));
    expected = 31 * expected + "testString".hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void hashCode_booleanFalse() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, false, "testString");

    int expected = 1;
    expected = 31 * expected + (false ? 1231 : 1237);
    expected = 31 * expected + 42;
    expected = 31 * expected + (int) (123456789L ^ (123456789L >>> 32));
    expected = 31 * expected + "testString".hashCode();

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void hashCode_stringValueNull() {
    BagOfPrimitives bag = new BagOfPrimitives(123456789L, 42, true, null);

    int expected = 1;
    expected = 31 * expected + (true ? 1231 : 1237);
    expected = 31 * expected + 42;
    expected = 31 * expected + (int) (123456789L ^ (123456789L >>> 32));
    expected = 31 * expected + 0;

    assertEquals(expected, bag.hashCode());
  }

  @Test
    @Timeout(8000)
  void hashCode_defaultValues() {
    BagOfPrimitives bag = new BagOfPrimitives();

    int expected = 1;
    expected = 31 * expected + (false ? 1231 : 1237);
    expected = 31 * expected + 0;
    expected = 31 * expected + (int) (0L ^ (0L >>> 32));
    expected = 31 * expected + 0;

    assertEquals(expected, bag.hashCode());
  }
}