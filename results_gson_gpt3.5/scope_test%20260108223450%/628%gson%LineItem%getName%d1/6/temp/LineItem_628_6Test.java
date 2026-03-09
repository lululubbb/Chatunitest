package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_628_6Test {

  @Test
    @Timeout(8000)
  void testGetName() throws Exception {
    String expectedName = "Test Item";

    Constructor<LineItem> constructor = LineItem.class.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    LineItem lineItem = constructor.newInstance(expectedName, 5, 1000L, "USD");

    String actualName = lineItem.getName();

    assertEquals(expectedName, actualName);
  }
}