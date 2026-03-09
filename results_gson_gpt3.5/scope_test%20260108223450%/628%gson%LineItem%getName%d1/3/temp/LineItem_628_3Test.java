package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_628_3Test {

  @Test
    @Timeout(8000)
  void testGetName_returnsCorrectName() throws Exception {
    String expectedName = "TestItem";
    int quantity = 5;
    long priceInMicros = 100000L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(expectedName, quantity, priceInMicros, currencyCode);

    String actualName = (String) lineItemClass.getMethod("getName").invoke(lineItem);

    assertEquals(expectedName, actualName);
  }
}