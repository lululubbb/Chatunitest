package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_627_1Test {

  @Test
    @Timeout(8000)
  void testLineItemConstructorAndGetters() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = clazz.getConstructor(String.class, int.class, long.class, String.class);
    Object item = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    // Use reflection to call getters
    assertEquals(name, clazz.getMethod("getName").invoke(item));
    assertEquals(quantity, clazz.getMethod("getQuantity").invoke(item));
    assertEquals(priceInMicros, clazz.getMethod("getPriceInMicros").invoke(item));
    assertEquals(currencyCode, clazz.getMethod("getCurrencyCode").invoke(item));
  }

  @Test
    @Timeout(8000)
  void testToStringNotNullAndContainsFields() throws Exception {
    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = clazz.getConstructor(String.class, int.class, long.class, String.class);
    Object item = constructor.newInstance("Name", 1, 1000L, "EUR");

    String toString = item.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("Name"));
    assertTrue(toString.contains("1"));
    assertTrue(toString.contains("1000"));
    assertTrue(toString.contains("EUR"));
  }
}