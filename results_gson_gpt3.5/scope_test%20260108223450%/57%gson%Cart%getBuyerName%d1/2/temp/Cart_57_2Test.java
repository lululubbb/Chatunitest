package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Cart_57_2Test {

  private Object cart;
  private String buyerName = "John Doe";

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to get the LineItem class
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");
    List<?> lineItems = Collections.emptyList();

    // Use reflection to get the Cart class and constructor
    Class<?> cartClass = Class.forName("com.google.gson.examples.android.model.Cart");
    Constructor<?> constructor = cartClass.getConstructor(List.class, String.class, String.class);
    cart = constructor.newInstance(lineItems, buyerName, "1234-5678-9012-3456");
  }

  @Test
    @Timeout(8000)
  void testGetBuyerName() throws Exception {
    // Use reflection to call getBuyerName()
    String actualBuyerName = (String) cart.getClass().getMethod("getBuyerName").invoke(cart);
    assertEquals(buyerName, actualBuyerName);
  }
}