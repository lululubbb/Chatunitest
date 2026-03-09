package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class LineItem_627_3Test {

  @Test
    @Timeout(8000)
  void testConstructorAndGetters() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
    // Use reflection to get the constructor
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);

    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    assertEquals(name, lineItemClass.getMethod("getName").invoke(lineItem));
    assertEquals(quantity, lineItemClass.getMethod("getQuantity").invoke(lineItem));
    assertEquals(priceInMicros, lineItemClass.getMethod("getPriceInMicros").invoke(lineItem));
    assertEquals(currencyCode, lineItemClass.getMethod("getCurrencyCode").invoke(lineItem));
  }

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);

    Object lineItem = constructor.newInstance("ItemName", 2, 5000L, "EUR");
    String toStringResult = (String) lineItemClass.getMethod("toString").invoke(lineItem);
    assertNotNull(toStringResult);
    assertTrue(toStringResult.contains("ItemName"));
    assertTrue(toStringResult.contains("2"));
    assertTrue(toStringResult.contains("5000"));
    assertTrue(toStringResult.contains("EUR"));
  }
}