package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class LineItem_629_4Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");

    Constructor<?> constructor = lineItemClass.getDeclaredConstructor(String.class, int.class, long.class, String.class);
    constructor.setAccessible(true);
    Object lineItem = constructor.newInstance("TestItem", 5, 1000L, "USD");

    Method method = lineItemClass.getDeclaredMethod("getQuantity");
    method.setAccessible(true);

    int quantity = (int) method.invoke(lineItem);

    assertEquals(5, quantity);
  }
}