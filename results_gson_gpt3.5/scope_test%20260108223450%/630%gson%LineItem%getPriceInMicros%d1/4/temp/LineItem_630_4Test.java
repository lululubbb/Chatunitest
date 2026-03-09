package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_630_4Test {

  @Test
    @Timeout(8000)
  void testGetPriceInMicros() throws Exception {
    String name = "Test Item";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    long actualPriceInMicros = (long) lineItemClass.getMethod("getPriceInMicros").invoke(lineItem);

    assertEquals(priceInMicros, actualPriceInMicros);
  }
}