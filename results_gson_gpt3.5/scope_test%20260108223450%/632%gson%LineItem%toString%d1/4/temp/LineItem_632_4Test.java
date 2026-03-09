package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class LineItem_632_4Test {

  @Test
    @Timeout(8000)
  void testToString() throws Exception {
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    Constructor<?> constructor = lineItemClass.getConstructor(String.class, int.class, long.class, String.class);
    Object item = constructor.newInstance("apple", 5, 123456789L, "USD");

    String expected = "(item: apple, qty: 5, price: 123.46 USD)";
    String actual = item.toString();

    assertEquals(expected, actual);
  }
}