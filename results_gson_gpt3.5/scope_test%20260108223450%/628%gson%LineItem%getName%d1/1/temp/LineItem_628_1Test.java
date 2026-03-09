package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_628_1Test {

  @Test
    @Timeout(8000)
  void testGetName() throws Exception {
    String expectedName = "Test Item";

    // Use LineItem.class directly instead of Class.forName to avoid ClassNotFoundException
    Class<?> lineItemClass = LineItem.class;
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(expectedName, 5, 1000L, "USD");

    String actualName = (String) lineItemClass.getMethod("getName").invoke(lineItem);
    assertEquals(expectedName, actualName);
  }
}