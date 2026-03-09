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

public class Cart_58_2Test {

  private Cart cart;
  private final List<com.google.gson.examples.android.model.LineItem> lineItems = Collections.emptyList();
  private final String buyerName = "John Doe";
  private final String creditCard = "4111111111111111";

  @BeforeEach
  public void setUp() {
    cart = new Cart(lineItems, buyerName, creditCard);
  }

  @Test
    @Timeout(8000)
  public void testGetCreditCard() {
    assertEquals(creditCard, cart.getCreditCard());
  }

  @Test
    @Timeout(8000)
  public void testGetCreditCardViaReflection() throws Exception {
    Field creditCardField = Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    String ccValue = (String) creditCardField.get(cart);
    assertEquals(creditCard, ccValue);
  }
}