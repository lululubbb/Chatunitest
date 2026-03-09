package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_630_2Test {

  @Test
    @Timeout(8000)
  void testGetPriceInMicros() throws Exception {
    String name = "TestItem";
    int quantity = 2;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    long result = (long) lineItemClass.getMethod("getPriceInMicros").invoke(lineItem);

    assertEquals(priceInMicros, result);
  }
}