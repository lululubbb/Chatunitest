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

public class Cart_57_3Test {

  private Cart cart;
  private String expectedBuyerName = "John Doe";

  @BeforeEach
  public void setUp() throws Exception {
    List<LineItem> emptyLineItems = Collections.emptyList();
    String creditCard = "1234-5678-9012-3456";
    cart = new Cart(emptyLineItems, expectedBuyerName, creditCard);
  }

  @Test
    @Timeout(8000)
  public void testGetBuyerName_returnsCorrectName() {
    assertEquals(expectedBuyerName, cart.getBuyerName());
  }

  @Test
    @Timeout(8000)
  public void testGetBuyerName_reflectionAccessPrivateField() throws Exception {
    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    String buyerNameValue = (String) buyerNameField.get(cart);
    assertEquals(expectedBuyerName, buyerNameValue);
  }
}