package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_628_5Test {

  @Test
    @Timeout(8000)
  void testGetName() throws Exception {
    String expectedName = "TestItem";

    // Use reflection to create instance since constructor is not fully shown as public in snippet
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    Object lineItem = constructor.newInstance(expectedName, 5, 1000L, "USD");

    String actualName = (String) lineItemClass.getMethod("getName").invoke(lineItem);

    assertEquals(expectedName, actualName);
  }
}