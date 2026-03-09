package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Cart_59_6Test {

  static class LineItem {
    private final String name;
    private final int quantity;

    public LineItem(String name, int quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    @Override
    public String toString() {
      return "LineItem{name='" + name + "', quantity=" + quantity + "}";
    }
  }

  // Minimal Cart implementation for testing purposes
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
      if (type == null) {
        return "null";
      }
      String typeName = type.getTypeName();
      if (typeName.contains(".")) {
        return typeName.substring(typeName.lastIndexOf('.') + 1);
      }
      return typeName;
    }
  }

  @Test
    @Timeout(8000)
  void toString_withNullLineItems_shouldReturnStringWithoutItems() {
    Cart cart = new Cart(null, "Alice", "1234-5678-9876-5432");
    String expected = "[BUYER: Alice; CC: 1234-5678-9876-5432; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withEmptyLineItems_shouldReturnStringWithoutItems() {
    Cart cart = new Cart(Collections.emptyList(), "Bob", "0000-1111-2222-3333");
    String expected = "[BUYER: Bob; CC: 0000-1111-2222-3333; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withSingleLineItem_shouldReturnStringWithOneItem() {
    LineItem item = new LineItem("Book", 2);
    List<LineItem> items = Collections.singletonList(item);
    Cart cart = new Cart(items, "Carol", "4444-5555-6666-7777");
    String expected = "[BUYER: Carol; CC: 4444-5555-6666-7777; LINE_ITEMS: " + item.toString() + "]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withMultipleLineItems_shouldReturnStringWithItemsSeparatedBySemicolon() {
    LineItem item1 = new LineItem("Pen", 5);
    LineItem item2 = new LineItem("Notebook", 3);
    List<LineItem> items = Arrays.asList(item1, item2);
    Cart cart = new Cart(items, "Dave", "8888-9999-0000-1111");
    String expected = "[BUYER: Dave; CC: 8888-9999-0000-1111; LINE_ITEMS: " + item1.toString() + "; " + item2.toString() + "]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_invokesGetSimpleTypeNameAndPrintsLineItemsClass() throws Exception {
    LineItem item = new LineItem("Gadget", 1);
    List<LineItem> items = Collections.singletonList(item);
    Cart cart = new Cart(items, "Eve", "2222-3333-4444-5555");

    // Use reflection to capture System.out output
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.PrintStream ps = new java.io.PrintStream(baos);
    java.io.PrintStream oldOut = System.out;
    System.setOut(ps);

    try {
      String result = cart.toString();
      ps.flush();
      String printed = baos.toString().trim();
      assertEquals("LineItems CLASS: List", printed);
      String expected = "[BUYER: Eve; CC: 2222-3333-4444-5555; LINE_ITEMS: " + item.toString() + "]";
      assertEquals(expected, result);
    } finally {
      System.setOut(oldOut);
    }
  }
}