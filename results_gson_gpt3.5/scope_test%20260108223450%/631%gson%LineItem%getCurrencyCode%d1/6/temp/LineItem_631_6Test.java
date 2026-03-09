package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_631_6Test {

  @Test
    @Timeout(8000)
  void testGetCurrencyCode() throws Exception {
    String expectedCurrencyCode = "USD";

    // Load the LineItem class by name using reflection
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance("itemName", 2, 1000L, expectedCurrencyCode);

    // Invoke getCurrencyCode method via reflection
    String actualCurrencyCode = (String) lineItemClass.getMethod("getCurrencyCode").invoke(lineItem);

    assertEquals(expectedCurrencyCode, actualCurrencyCode);
  }
}