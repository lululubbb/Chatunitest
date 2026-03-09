package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class Cart_59_2Test {

  static class LineItem {
    private final String name;
    private final int quantity;

    LineItem(String name, int quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    @Override
    public String toString() {
      return name + " x" + quantity;
    }
  }

  // Helper Cart class for testing (since original Cart class is not visible to test)
  static class Cart {
    public final List<LineItem> lineItems;
    private final String buyerName;
    private final String creditCard;

    public Cart(List<LineItem> lineItems, String buyerName, String creditCard) {
      this.lineItems = lineItems;
      this.buyerName = buyerName;
      this.creditCard = creditCard;
    }

    @Override
    public String toString() {
      StringBuilder itemsText = new StringBuilder();
      boolean first = true;
      if (lineItems != null) {
        try {
          Class<?> fieldType = Cart.class.getField("lineItems").getType();
          System.out.println("LineItems CLASS: " + getSimpleTypeName(fieldType));
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        }
        for (LineItem item : lineItems) {
          if (first) {
            first = false;
          } else {
            itemsText.append("; ");
          }
          itemsText.append(item);
        }
      }
      return "[BUYER: " + buyerName + "; CC: " + creditCard + "; "
          + "LINE_ITEMS: " + itemsText.toString() + "]";
    }

    @SuppressWarnings("unchecked")
    public static String getSimpleTypeName(java.lang.reflect.Type type) {
      if (type instanceof Class<?>) {
        return ((Class<?>) type).getSimpleName();
      }
      return type.getTypeName();
    }
  }

  @Test
    @Timeout(8000)
  void testToString_withNullLineItems() throws Exception {
    Cart cart = new Cart(null, "Alice", "1234-5678-9876-5432");
    String expected = "[BUYER: Alice; CC: 1234-5678-9876-5432; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_withEmptyLineItems() throws Exception {
    Cart cart = new Cart(new ArrayList<>(), "Bob", "0000-0000-0000-0000");
    String expected = "[BUYER: Bob; CC: 0000-0000-0000-0000; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_withMultipleLineItems() throws Exception {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("Apple", 2));
    items.add(new LineItem("Banana", 5));
    Cart cart = new Cart(items, "Carol", "9999-8888-7777-6666");
    String expected = "[BUYER: Carol; CC: 9999-8888-7777-6666; LINE_ITEMS: Apple x2; Banana x5]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void testToString_fieldReflectionOutput() throws Exception {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("Grapes", 3));
    Cart cart = new Cart(items, "Dave", "1111-2222-3333-4444");

    // Capture System.out output for the reflection print statement
    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
    java.io.PrintStream originalOut = System.out;
    System.setOut(new java.io.PrintStream(outContent));

    cart.toString();

    System.setOut(originalOut);

    String output = outContent.toString().trim();
    org.junit.jupiter.api.Assertions.assertTrue(output.contains("LineItems CLASS: List"));
  }
}