package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class LineItem_627_4Test {

  @Test
    @Timeout(8000)
  void testConstructorAndGetters() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    // Use constructor normally
    com.google.gson.examples.android.model.LineItem item = new com.google.gson.examples.android.model.LineItem(name, quantity, priceInMicros, currencyCode);

    assertEquals(name, item.getName());
    assertEquals(quantity, item.getQuantity());
    assertEquals(priceInMicros, item.getPriceInMicros());
    assertEquals(currencyCode, item.getCurrencyCode());

    // Use reflection to verify private final fields
    Field nameField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("name");
    nameField.setAccessible(true);
    assertEquals(name, nameField.get(item));

    Field quantityField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("quantity");
    quantityField.setAccessible(true);
    assertEquals(quantity, quantityField.getInt(item));

    Field priceField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("priceInMicros");
    priceField.setAccessible(true);
    assertEquals(priceInMicros, priceField.getLong(item));

    Field currencyField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("currencyCode");
    currencyField.setAccessible(true);
    assertEquals(currencyCode, currencyField.get(item));
  }

  @Test
    @Timeout(8000)
  void testToString() {
    com.google.gson.examples.android.model.LineItem item = new com.google.gson.examples.android.model.LineItem("Apple", 10, 999999L, "EUR");
    String toStringResult = item.toString();
    assertNotNull(toStringResult);
    assertTrue(toStringResult.contains("Apple"));
    assertTrue(toStringResult.contains("10"));
    assertTrue(toStringResult.contains("999999"));
    assertTrue(toStringResult.contains("EUR"));
  }

  @Test
    @Timeout(8000)
  void testConstructorUsingReflection() throws Exception {
    Constructor<com.google.gson.examples.android.model.LineItem> constructor = com.google.gson.examples.android.model.LineItem.class.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);

    com.google.gson.examples.android.model.LineItem item = constructor.newInstance("Banana", 3, 500000L, "GBP");

    assertEquals("Banana", item.getName());
    assertEquals(3, item.getQuantity());
    assertEquals(500000L, item.getPriceInMicros());
    assertEquals("GBP", item.getCurrencyCode());
  }
}