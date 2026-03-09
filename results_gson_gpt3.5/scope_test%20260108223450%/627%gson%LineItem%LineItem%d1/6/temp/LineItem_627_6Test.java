package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class LineItem_627_6Test {

  @Test
    @Timeout(8000)
  void testConstructorAndGetters() throws Exception {
    String expectedName = "TestItem";
    int expectedQuantity = 5;
    long expectedPriceInMicros = 123456789L;
    String expectedCurrencyCode = "USD";

    // Using constructor normally
    com.google.gson.examples.android.model.LineItem item = new com.google.gson.examples.android.model.LineItem(expectedName, expectedQuantity, expectedPriceInMicros, expectedCurrencyCode);

    assertEquals(expectedName, item.getName());
    assertEquals(expectedQuantity, item.getQuantity());
    assertEquals(expectedPriceInMicros, item.getPriceInMicros());
    assertEquals(expectedCurrencyCode, item.getCurrencyCode());
  }

  @Test
    @Timeout(8000)
  void testPrivateFieldsViaReflection() throws Exception {
    Constructor<com.google.gson.examples.android.model.LineItem> constructor = com.google.gson.examples.android.model.LineItem.class.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    com.google.gson.examples.android.model.LineItem item = constructor.newInstance("RefItem", 10, 999999L, "EUR");

    Field nameField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("name");
    Field quantityField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("quantity");
    Field priceField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("priceInMicros");
    Field currencyField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("currencyCode");

    nameField.setAccessible(true);
    quantityField.setAccessible(true);
    priceField.setAccessible(true);
    currencyField.setAccessible(true);

    assertEquals("RefItem", nameField.get(item));
    assertEquals(10, quantityField.getInt(item));
    assertEquals(999999L, priceField.getLong(item));
    assertEquals("EUR", currencyField.get(item));
  }

  @Test
    @Timeout(8000)
  void testToStringNotNull() {
    com.google.gson.examples.android.model.LineItem item = new com.google.gson.examples.android.model.LineItem("Name", 1, 1000L, "GBP");
    String str = item.toString();
    assertNotNull(str);
    assertTrue(str.contains("Name"));
    assertTrue(str.contains("1"));
    assertTrue(str.contains("1000"));
    assertTrue(str.contains("GBP"));
  }
}