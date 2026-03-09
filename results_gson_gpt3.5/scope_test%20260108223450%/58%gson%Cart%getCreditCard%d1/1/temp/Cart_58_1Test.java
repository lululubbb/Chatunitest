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

public class Cart_58_1Test {

  private Object cart;
  private List<?> lineItems;
  private String buyerName;
  private String creditCard;

  @BeforeEach
  public void setUp() throws Exception {
    lineItems = Collections.emptyList();
    buyerName = "John Doe";
    creditCard = "1234-5678-9876-5432";

    Class<?> cartClass = Class.forName("com.google.gson.examples.android.model.Cart");
    Constructor<?> constructor = cartClass.getDeclaredConstructor(List.class, String.class, String.class);
    constructor.setAccessible(true);
    cart = constructor.newInstance(lineItems, buyerName, creditCard);
  }

  @Test
    @Timeout(8000)
  public void testGetCreditCard() throws Exception {
    Class<?> cartClass = cart.getClass();
    String actualCreditCard = (String) cartClass.getMethod("getCreditCard").invoke(cart);
    assertEquals(creditCard, actualCreditCard);
  }
}