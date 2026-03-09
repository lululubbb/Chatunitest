package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Cart_55_4Test {

  @Test
    @Timeout(8000)
  void testCartConstructorAndGetters() throws NoSuchFieldException, IllegalAccessException {
    // Prepare mock LineItem list
    LineItem lineItem1 = Mockito.mock(LineItem.class);
    LineItem lineItem2 = Mockito.mock(LineItem.class);
    List<LineItem> lineItems = Arrays.asList(lineItem1, lineItem2);

    String buyerName = "John Doe";
    String creditCard = "1234-5678-9012-3456";

    // Create Cart instance
    Cart cart = new Cart(lineItems, buyerName, creditCard);

    // Verify public final field lineItems
    assertEquals(lineItems, cart.lineItems);

    // Use reflection to access private final buyerName field
    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    assertEquals(buyerName, buyerNameField.get(cart));

    // Use reflection to access private final creditCard field
    Field creditCardField = Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    assertEquals(creditCard, creditCardField.get(cart));

    // Verify getter methods
    assertEquals(lineItems, cart.getLineItems());
    assertEquals(buyerName, cart.getBuyerName());
    assertEquals(creditCard, cart.getCreditCard());
  }

  @Test
    @Timeout(8000)
  void testCartConstructorWithEmptyLineItems() throws NoSuchFieldException, IllegalAccessException {
    List<LineItem> emptyList = Collections.emptyList();
    String buyerName = "Alice";
    String creditCard = "0000-0000-0000-0000";

    Cart cart = new Cart(emptyList, buyerName, creditCard);

    assertEquals(emptyList, cart.lineItems);

    Field buyerNameField = Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    assertEquals(buyerName, buyerNameField.get(cart));

    Field creditCardField = Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    assertEquals(creditCard, creditCardField.get(cart));

    assertEquals(emptyList, cart.getLineItems());
    assertEquals(buyerName, cart.getBuyerName());
    assertEquals(creditCard, cart.getCreditCard());
  }
}