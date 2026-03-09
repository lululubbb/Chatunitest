package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class LineItem_630_1Test {

  @Test
    @Timeout(8000)
  void getPriceInMicros_returnsCorrectValue() throws Exception {
    String name = "TestItem";
    int quantity = 2;
    long priceInMicros = 123456L;
    String currencyCode = "USD";

    // Use Class.forName with fully qualified name to load LineItem class
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    Method getPriceInMicrosMethod = lineItemClass.getMethod("getPriceInMicros");
    long result = (long) getPriceInMicrosMethod.invoke(lineItem);

    assertEquals(priceInMicros, result);
  }
}