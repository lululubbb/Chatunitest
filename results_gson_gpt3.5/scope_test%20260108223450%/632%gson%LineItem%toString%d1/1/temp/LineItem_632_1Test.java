package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_632_1Test {

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 12345678L; // 12.345678 dollars
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    String expected = String.format("(item: %s, qty: %s, price: %.2f %s)", name, quantity, priceInMicros / 1000000d, currencyCode);

    String actual = lineItem.toString();

    assertEquals(expected, actual);
  }

}