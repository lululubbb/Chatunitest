package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LineItem_629_1Test {

  @Test
    @Timeout(8000)
  void testGetQuantity() throws Exception {
    // Using constructor to set quantity
    LineItem lineItem = new LineItem("TestItem", 5, 123456L, "USD");
    assertEquals(5, lineItem.getQuantity());

    // Using reflection to invoke getQuantity (even if public, per instructions)
    Method getQuantityMethod = LineItem.class.getDeclaredMethod("getQuantity");
    int quantity = (int) getQuantityMethod.invoke(lineItem);
    assertEquals(5, quantity);
  }
}