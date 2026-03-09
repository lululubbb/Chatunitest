package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class LineItem_628_4Test {

  @Test
    @Timeout(8000)
  void testGetName() throws Exception {
    String expectedName = "TestItem";
    int quantity = 5;
    long priceInMicros = 123456L;
    String currencyCode = "USD";

    // Fully qualified class name to fix "cannot find symbol" error
    com.google.gson.examples.android.model.LineItem lineItem =
        new com.google.gson.examples.android.model.LineItem(expectedName, quantity, priceInMicros, currencyCode);

    // Direct call to public method getName()
    String actualName = lineItem.getName();
    assertEquals(expectedName, actualName);

    // Additionally, use reflection to invoke private final field 'name'
    Field nameField = com.google.gson.examples.android.model.LineItem.class.getDeclaredField("name");
    nameField.setAccessible(true);
    String reflectedName = (String) nameField.get(lineItem);
    assertEquals(expectedName, reflectedName);
  }
}