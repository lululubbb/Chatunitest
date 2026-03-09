package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_631_4Test {

  @Test
    @Timeout(8000)
  void testGetCurrencyCode() throws Exception {
    // Directly use the LineItem class without Class.forName to avoid ClassNotFoundException
    Constructor<LineItem> constructor = LineItem.class.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    LineItem lineItem = constructor.newInstance("itemName", 2, 1000L, "USD");

    String actualCurrencyCode = lineItem.getCurrencyCode();
    assertEquals("USD", actualCurrencyCode);
  }
}