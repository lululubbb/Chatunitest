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

import org.junit.jupiter.api.Test;

class Cart_57_5Test {

  static class LineItem {}

  @Test
    @Timeout(8000)
  void testGetBuyerName() throws Exception {
    List<LineItem> lineItems = Collections.emptyList();
    String buyerName = "John Doe";
    String creditCard = "1234-5678-9876-5432";

    // Directly use Cart.class instead of Class.forName to avoid ClassNotFoundException
    Class<Cart> cartClass = Cart.class;

    Constructor<Cart> constructor = cartClass.getConstructor(List.class, String.class, String.class);
    Cart cart = constructor.newInstance(lineItems, buyerName, creditCard);

    String result = cart.getBuyerName();

    assertEquals(buyerName, result);
  }
}