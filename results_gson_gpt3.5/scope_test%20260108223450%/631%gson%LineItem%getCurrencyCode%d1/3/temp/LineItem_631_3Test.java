package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class LineItem_631_3Test {

  @Test
    @Timeout(8000)
  void testGetCurrencyCode() throws Exception {
    String expectedCurrencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance("TestItem", 5, 1000L, expectedCurrencyCode);

    String actualCurrencyCode = (String) lineItemClass.getMethod("getCurrencyCode").invoke(lineItem);

    assertEquals(expectedCurrencyCode, actualCurrencyCode);
  }
}