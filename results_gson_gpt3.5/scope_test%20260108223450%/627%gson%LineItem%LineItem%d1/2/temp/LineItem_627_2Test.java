package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class LineItem_627_2Test {

  @Test
    @Timeout(8000)
  void testConstructorAndGetters() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    Field nameField = lineItemClass.getDeclaredField("name");
    nameField.setAccessible(true);
    assertEquals(name, nameField.get(lineItem));

    Field quantityField = lineItemClass.getDeclaredField("quantity");
    quantityField.setAccessible(true);
    assertEquals(quantity, quantityField.get(lineItem));

    Field priceField = lineItemClass.getDeclaredField("priceInMicros");
    priceField.setAccessible(true);
    assertEquals(priceInMicros, priceField.get(lineItem));

    Field currencyField = lineItemClass.getDeclaredField("currencyCode");
    currencyField.setAccessible(true);
    assertEquals(currencyCode, currencyField.get(lineItem));

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
    Object lineItem = constructor.newInstance("ItemX", 2, 1000L, "EUR");

    String toStringResult = lineItemClass.getMethod("toString").invoke(lineItem).toString();
    assertNotNull(toStringResult);
    assertTrue(toStringResult.contains("ItemX"));
    assertTrue(toStringResult.contains("2"));
    assertTrue(toStringResult.contains("1000"));
    assertTrue(toStringResult.contains("EUR"));
  }
}