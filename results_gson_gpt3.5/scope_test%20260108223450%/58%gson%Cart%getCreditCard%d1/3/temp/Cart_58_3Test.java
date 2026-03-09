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

class Cart_58_3Test {

  private Cart cart;
  private List<LineItem> lineItems;

  @BeforeEach
  void setUp() throws Exception {
    lineItems = Collections.emptyList();
    cart = new Cart(lineItems, "buyerNameValue", "creditCardValue");
  }

  @Test
    @Timeout(8000)
  void testGetCreditCard() throws Exception {
    // Validate that getCreditCard returns the correct creditCard value

    // 1. Direct call
    assertEquals("creditCardValue", cart.getCreditCard());

    // 2. Change private creditCard field via reflection and test again
    Field creditCardField = Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    creditCardField.set(cart, "newCreditCardValue");
    assertEquals("newCreditCardValue", cart.getCreditCard());
  }
}