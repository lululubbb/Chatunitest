package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_631_1Test {

  @Test
    @Timeout(8000)
  void testGetCurrencyCode() throws Exception {
    String expectedCurrencyCode = "USD";

    // Load the LineItem class by name via reflection
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);

    // Create instance via reflection
    Object lineItem = constructor.newInstance("ItemName", 5, 123456L, expectedCurrencyCode);

    // Invoke getCurrencyCode method via reflection
    String actualCurrencyCode = (String) lineItemClass.getMethod("getCurrencyCode").invoke(lineItem);

    assertEquals(expectedCurrencyCode, actualCurrencyCode);
  }
}