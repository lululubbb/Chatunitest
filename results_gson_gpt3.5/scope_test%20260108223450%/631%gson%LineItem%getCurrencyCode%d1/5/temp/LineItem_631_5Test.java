package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class LineItem_631_5Test {

  @Test
    @Timeout(8000)
  void testGetCurrencyCode() throws Exception {
    String expectedCurrencyCode = "USD";

    // Directly use LineItem class instead of reflection
    LineItem lineItem = new LineItem("itemName", 5, 10000L, expectedCurrencyCode);

    // Use reflection to access private field 'currencyCode'
    Field currencyCodeField = LineItem.class.getDeclaredField("currencyCode");
    currencyCodeField.setAccessible(true);
    String actualCurrencyCodeField = (String) currencyCodeField.get(lineItem);
    assertEquals(expectedCurrencyCode, actualCurrencyCodeField);

    // Invoke public getCurrencyCode() method directly
    String actualCurrencyCode = lineItem.getCurrencyCode();
    assertEquals(expectedCurrencyCode, actualCurrencyCode);
  }
}