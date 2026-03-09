package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class LineItem_629_2Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    String name = "Test Item";
    int quantity = 5;
    long priceInMicros = 100000L;
    String currencyCode = "USD";

    // Load LineItem class via reflection
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");

    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    Method getQuantityMethod = lineItemClass.getMethod("getQuantity");
    int result = (int) getQuantityMethod.invoke(lineItem);

    assertEquals(quantity, result);
  }
}