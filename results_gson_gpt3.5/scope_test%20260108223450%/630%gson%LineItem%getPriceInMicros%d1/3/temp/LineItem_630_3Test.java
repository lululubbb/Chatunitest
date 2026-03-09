package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LineItem_630_3Test {

  @Test
    @Timeout(8000)
  void testGetPriceInMicros() throws Exception {
    String name = "Test Item";
    int quantity = 2;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    // Use reflection to create an instance of LineItem
    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    // Invoke getPriceInMicros method
    long result = (long) clazz.getMethod("getPriceInMicros").invoke(lineItem);

    assertEquals(priceInMicros, result);
  }
}