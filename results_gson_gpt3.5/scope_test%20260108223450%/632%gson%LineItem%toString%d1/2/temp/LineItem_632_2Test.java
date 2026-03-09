package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

class LineItem_632_2Test {

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    String name = "Apple";
    int quantity = 3;
    long priceInMicros = 2500000L; // 2.5 in normal units
    String currencyCode = "USD";

    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> ctor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    Object lineItem = ctor.newInstance(name, quantity, priceInMicros, currencyCode);

    String expected = String.format("(item: %s, qty: %s, price: %.2f %s)",
        name, quantity, priceInMicros / 1000000d, currencyCode);

    String actual = lineItem.toString();
    assertEquals(expected, actual);
  }
}