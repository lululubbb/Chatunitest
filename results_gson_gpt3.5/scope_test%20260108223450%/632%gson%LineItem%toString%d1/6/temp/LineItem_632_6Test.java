package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_632_6Test {

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    LineItem item = createLineItem("apple", 3, 2500000L, "USD");
    String expected = "(item: apple, qty: 3, price: 2.50 USD)";
    assertEquals(expected, item.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_zeroQuantityAndPrice() throws Exception {
    LineItem item = createLineItem("banana", 0, 0L, "EUR");
    String expected = "(item: banana, qty: 0, price: 0.00 EUR)";
    assertEquals(expected, item.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_nullNameAndCurrency() throws Exception {
    LineItem item = createLineItem(null, 5, 123456789L, null);
    String expected = "(item: null, qty: 5, price: 123.46 null)";
    assertEquals(expected, item.toString());
  }

  private LineItem createLineItem(String name, int quantity, long priceInMicros, String currencyCode) throws Exception {
    Class<LineItem> clazz = (Class<LineItem>) Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<LineItem> constructor = clazz.getConstructor(String.class, int.class, long.class, String.class);
    return constructor.newInstance(name, quantity, priceInMicros, currencyCode);
  }
}