package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

class LineItem_632_3Test {

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    String name = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456789; // 123.456789 dollars
    String currencyCode = "USD";

    Class<?> clazz = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = clazz.getConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = constructor.newInstance(name, quantity, priceInMicros, currencyCode);

    String expected = String.format("(item: %s, qty: %s, price: %.2f %s)",
        name, quantity, priceInMicros / 1000000d, currencyCode);

    String actual = lineItem.toString();

    assertEquals(expected, actual);
  }
}