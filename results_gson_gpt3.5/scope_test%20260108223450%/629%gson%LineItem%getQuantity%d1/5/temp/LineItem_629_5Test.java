package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LineItem_629_5Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    String name = "Test Product";
    int quantity = 5;
    long priceInMicros = 100000L;
    String currencyCode = "USD";

    // Use reflection to create LineItem instance because constructor is not visible
    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    var constructor = clazz.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    int result = (int) clazz.getMethod("getQuantity").invoke(lineItem);

    assertEquals(quantity, result);
  }
}