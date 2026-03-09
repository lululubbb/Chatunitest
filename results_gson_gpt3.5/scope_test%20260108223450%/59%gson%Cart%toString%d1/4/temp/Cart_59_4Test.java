package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Cart_59_4Test {

  @Test
    @Timeout(8000)
  public void testToString_withNullLineItems() {
    Cart cart = new Cart(null, "Alice", "1234-5678-9012-3456");
    String expected = "[BUYER: Alice; CC: 1234-5678-9012-3456; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withEmptyLineItems() {
    Cart cart = new Cart(Collections.emptyList(), "Bob", "0000-0000-0000-0000");
    String expected = "[BUYER: Bob; CC: 0000-0000-0000-0000; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withSingleLineItem() {
    LineItem item = mock(LineItem.class);
    when(item.toString()).thenReturn("Item1");
    List<LineItem> items = Collections.singletonList(item);
    Cart cart = new Cart(items, "Carol", "1111-2222-3333-4444");
    String expected = "[BUYER: Carol; CC: 1111-2222-3333-4444; LINE_ITEMS: Item1]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withMultipleLineItems() {
    LineItem item1 = mock(LineItem.class);
    LineItem item2 = mock(LineItem.class);
    when(item1.toString()).thenReturn("Item1");
    when(item2.toString()).thenReturn("Item2");
    List<LineItem> items = Arrays.asList(item1, item2);
    Cart cart = new Cart(items, "Dave", "5555-6666-7777-8888");
    String expected = "[BUYER: Dave; CC: 5555-6666-7777-8888; LINE_ITEMS: Item1; Item2]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_invokesGetSimpleTypeNamePrintsLineItemsClass() throws Exception {
    // To capture System.out output, redirect output stream
    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
    java.io.PrintStream originalOut = System.out;
    System.setOut(new java.io.PrintStream(outContent));

    try {
      List<LineItem> items = Collections.emptyList();
      Cart cart = new Cart(items, "Eve", "9999-8888-7777-6666");
      cart.toString();

      Field lineItemsField = Cart.class.getField("lineItems");
      String expectedPrint = "LineItems CLASS: " + Cart.getSimpleTypeName(lineItemsField.getType());

      String output = outContent.toString().trim();
      assertTrue(output.contains(expectedPrint));
    } finally {
      System.setOut(originalOut);
    }
  }
}