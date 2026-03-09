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

class Cart_58_6Test {

  private Cart cart;
  private List<LineItem> mockLineItems;

  @BeforeEach
  void setUp() {
    mockLineItems = Collections.emptyList();
    cart = new Cart(mockLineItems, "BuyerNameExample", "4111111111111111");
  }

  @Test
    @Timeout(8000)
  void testGetCreditCard_returnsCorrectCreditCard() {
    // Direct call to public method getCreditCard()
    String creditCard = cart.getCreditCard();
    assertEquals("4111111111111111", creditCard);
  }

  @Test
    @Timeout(8000)
  void testGetCreditCard_reflectionAccess_privateField() throws Exception {
    // Access private field creditCard by reflection to verify value
    Field creditCardField = Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    String creditCardValue = (String) creditCardField.get(cart);
    assertEquals("4111111111111111", creditCardValue);
  }

  @Test
    @Timeout(8000)
  void testConstructor_andGetters() {
    assertEquals(mockLineItems, cart.getLineItems());
    assertEquals("BuyerNameExample", cart.getBuyerName());
  }
}