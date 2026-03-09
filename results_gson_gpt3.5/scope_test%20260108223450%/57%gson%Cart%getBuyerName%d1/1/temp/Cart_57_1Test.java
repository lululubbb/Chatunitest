package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.examples.android.model.Cart;
import com.google.gson.examples.android.model.LineItem;

class Cart_57_1Test {

  private Cart cart;

  @BeforeEach
  void setUp() throws Exception {
    // Create Cart instance with dummy data
    List<LineItem> emptyLineItems = Collections.emptyList();
    cart = new Cart(emptyLineItems, "John Doe", "1234-5678-9012-3456");

    // Use reflection to verify private field buyerName is set correctly
    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    String buyerNameValue = (String) buyerNameField.get(cart);
    assertEquals("John Doe", buyerNameValue);
  }

  @Test
    @Timeout(8000)
  void testGetBuyerName() {
    // Directly test the public getter
    String result = cart.getBuyerName();
    assertEquals("John Doe", result);
  }
}