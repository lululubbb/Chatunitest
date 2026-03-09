package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_629_6Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    // Arrange
    String name = "Test Item";
    int quantity = 5;
    long priceInMicros = 100000L;
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    // Act
    int result = (int) lineItemClass.getMethod("getQuantity").invoke(lineItem);

    // Assert
    assertEquals(quantity, result);
  }
}