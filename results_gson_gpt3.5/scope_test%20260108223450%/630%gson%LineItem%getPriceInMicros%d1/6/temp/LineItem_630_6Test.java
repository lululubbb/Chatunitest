package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class LineItem_630_6Test {

  @Test
    @Timeout(8000)
  public void testGetPriceInMicros() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    long result = (long) lineItemClass.getMethod("getPriceInMicros").invoke(lineItem);

    assertEquals(priceInMicros, result);
  }
}