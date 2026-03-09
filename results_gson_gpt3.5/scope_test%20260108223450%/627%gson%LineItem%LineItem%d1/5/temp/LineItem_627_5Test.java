package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class LineItem_627_5Test {

  @Test
    @Timeout(8000)
  void testLineItemConstructorAndGetters() throws Exception {
    String name = "Test Item";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    Method getName = lineItemClass.getMethod("getName");
    Method getQuantity = lineItemClass.getMethod("getQuantity");
    Method getPriceInMicros = lineItemClass.getMethod("getPriceInMicros");
    Method getCurrencyCode = lineItemClass.getMethod("getCurrencyCode");

    assertEquals(name, getName.invoke(lineItem));
    assertEquals(quantity, getQuantity.invoke(lineItem));
    assertEquals(priceInMicros, getPriceInMicros.invoke(lineItem));
    assertEquals(currencyCode, getCurrencyCode.invoke(lineItem));
  }

  @Test
    @Timeout(8000)
  void testToStringNotNull() throws Exception {
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance("Item", 1, 1000L, "EUR");

    Method toStringMethod = lineItemClass.getMethod("toString");
    String toStringResult = (String) toStringMethod.invoke(lineItem);

    assertNotNull(toStringResult);
    assertTrue(toStringResult.contains("Item"));
  }
}