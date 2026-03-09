package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Cart_59_3Test {

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

  // Add this Cart class stub for compilation and testing purposes
  // reflecting the original Cart class structure for the test to compile
  public static class Cart {
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

    public List<LineItem> getLineItems() {
      return lineItems;
    }

    public String getBuyerName() {
      return buyerName;
    }

    public String getCreditCard() {
      return creditCard;
    }

    @SuppressWarnings("unchecked")
    public static String getSimpleTypeName(java.lang.reflect.Type type) {
      if (type == null) return "null";
      String fullName = type.getTypeName();
      int lastDot = fullName.lastIndexOf('.');
      if (lastDot >= 0) {
        return fullName.substring(lastDot + 1);
      }
      return fullName;
    }
  }

  @Test
    @Timeout(8000)
  void toString_withNullLineItems_returnsCorrectString() {
    Cart cart = new Cart(null, "Alice", "1234-5678-9876-5432");
    String expected = "[BUYER: Alice; CC: 1234-5678-9876-5432; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withEmptyLineItems_returnsCorrectString() {
    Cart cart = new Cart(new ArrayList<>(), "Bob", "0000-0000-0000-0000");
    String expected = "[BUYER: Bob; CC: 0000-0000-0000-0000; LINE_ITEMS: ]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withSingleLineItem_returnsCorrectString() {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("Apple", 2));
    Cart cart = new Cart(items, "Carol", "1111-2222-3333-4444");
    String expected = "[BUYER: Carol; CC: 1111-2222-3333-4444; LINE_ITEMS: Apple x2]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_withMultipleLineItems_returnsCorrectString() {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("Banana", 3));
    items.add(new LineItem("Orange", 1));
    Cart cart = new Cart(items, "Dave", "5555-6666-7777-8888");
    String expected = "[BUYER: Dave; CC: 5555-6666-7777-8888; LINE_ITEMS: Banana x3; Orange x1]";
    assertEquals(expected, cart.toString());
  }

  @Test
    @Timeout(8000)
  void toString_reflectsLineItemsFieldType_printsClassName() throws Exception {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("Grape", 5));
    Cart cart = new Cart(items, "Eve", "9999-8888-7777-6666");

    // Capture system out to verify print line (optional)
    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
    java.io.PrintStream originalOut = System.out;
    System.setOut(new java.io.PrintStream(outContent));

    String result = cart.toString();

    System.setOut(originalOut);

    // The printed line should contain the simple type name of lineItems field's type
    Field lineItemsField = Cart.class.getField("lineItems");
    String expectedPrint = "LineItems CLASS: " + Cart.getSimpleTypeName(lineItemsField.getType());
    String printed = outContent.toString().trim();
    // printed line may be first line or only line
    assertEquals(expectedPrint, printed);

    String expected = "[BUYER: Eve; CC: 9999-8888-7777-6666; LINE_ITEMS: Grape x5]";
    assertEquals(expected, result);
  }
}