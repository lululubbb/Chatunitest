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

class Cart_57_4Test {

  private com.google.gson.examples.android.model.Cart cart;
  private List<com.google.gson.examples.android.model.LineItem> emptyLineItems;

  @BeforeEach
  void setUp() throws Exception {
    emptyLineItems = Collections.emptyList();
    Constructor<com.google.gson.examples.android.model.Cart> constructor = com.google.gson.examples.android.model.Cart.class
        .getDeclaredConstructor(List.class, String.class, String.class);
    constructor.setAccessible(true);
    cart = constructor.newInstance(emptyLineItems, "TestBuyer", "1234-5678-9012-3456");
  }

  @Test
    @Timeout(8000)
  void testGetBuyerName() {
    assertEquals("TestBuyer", cart.getBuyerName());
  }
}