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

class Cart_57_6Test {

  private Cart cart;
  private List<LineItem> emptyLineItems;

  @BeforeEach
  void setUp() {
    emptyLineItems = Collections.emptyList();
    cart = new Cart(emptyLineItems, "John Doe", "1234-5678-9012-3456");
  }

  @Test
    @Timeout(8000)
  void testGetBuyerName() throws Exception {
    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    String buyerNameValue = (String) buyerNameField.get(cart);
    assertEquals(buyerNameValue, cart.getBuyerName());
  }

  @Test
    @Timeout(8000)
  void testGetBuyerNameWithDifferentValue() throws Exception {
    Cart customCart = new Cart(emptyLineItems, "Alice Smith", "9876-5432-1098-7654");
    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    String buyerNameValue = (String) buyerNameField.get(customCart);
    assertEquals(buyerNameValue, customCart.getBuyerName());
  }
}