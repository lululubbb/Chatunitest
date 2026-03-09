package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_629_3Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    String name = "Test Item";
    int quantity = 5;
    long priceInMicros = 100000L;
    String currencyCode = "USD";

    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    int actualQuantity = (int) clazz.getMethod("getQuantity").invoke(lineItem);

    assertEquals(quantity, actualQuantity);
  }
}