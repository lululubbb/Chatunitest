package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Cart_58_5Test {

  private Object cart;
  private List<?> lineItems;
  private String buyerName;
  private String creditCard;

  @BeforeEach
  public void setUp() {
    lineItems = Collections.emptyList();
    buyerName = "John Doe";
    creditCard = "1234-5678-9012-3456";
  }

  @Test
    @Timeout(8000)
  public void testGetCreditCard() throws Exception {
    Class<?> cartClass = Cart.class;
    Constructor<?> constructor = cartClass.getDeclaredConstructor(List.class, String.class, String.class);
    constructor.setAccessible(true);
    cart = constructor.newInstance(lineItems, buyerName, creditCard);

    String actualCreditCard = (String) cartClass.getMethod("getCreditCard").invoke(cart);

    assertEquals(creditCard, actualCreditCard);
  }

  @Test
    @Timeout(8000)
  public void testGetCreditCardWithReflectionSet() throws Exception {
    Class<?> cartClass = Cart.class;
    Constructor<?> constructor = cartClass.getDeclaredConstructor(List.class, String.class, String.class);
    constructor.setAccessible(true);
    cart = constructor.newInstance(lineItems, buyerName, "initialCard");

    Field creditCardField = cartClass.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    creditCardField.set(cart, creditCard);

    String actualCreditCard = (String) cartClass.getMethod("getCreditCard").invoke(cart);

    assertEquals(creditCard, actualCreditCard);
  }
}