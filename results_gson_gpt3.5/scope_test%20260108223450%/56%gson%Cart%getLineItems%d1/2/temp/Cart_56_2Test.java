package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Cart_56_2Test {

  private Cart cart;
  private List<Cart.LineItem> lineItems;

  @BeforeEach
  void setUp() throws Exception {
    lineItems = new ArrayList<>();
    lineItems.add(new Cart.LineItem("item1", 2, 10.0));
    lineItems.add(new Cart.LineItem("item2", 1, 5.0));
    cart = new Cart(lineItems, "John Doe", "1234-5678-9012-3456");
  }

  @Test
    @Timeout(8000)
  void testGetLineItems() throws Exception {
    // invoke public method getLineItems
    List<Cart.LineItem> result = cart.getLineItems();
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("item1", result.get(0).getName());
    assertEquals(2, result.get(0).getQuantity());
    assertEquals(10.0, result.get(0).getPrice());
    assertEquals("item2", result.get(1).getName());
    assertEquals(1, result.get(1).getQuantity());
    assertEquals(5.0, result.get(1).getPrice());

    // Use reflection to access the public final field lineItems and compare
    Field lineItemsField = Cart.class.getField("lineItems");
    Object fieldValue = lineItemsField.get(cart);
    assertSame(result, fieldValue);
  }
}