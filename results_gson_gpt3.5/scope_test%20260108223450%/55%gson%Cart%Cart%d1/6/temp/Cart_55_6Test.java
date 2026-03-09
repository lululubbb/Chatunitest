package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

class Cart_55_6Test {

  // Minimal LineItem stub class to allow compilation
  static class LineItem {
    private final String name;
    private final int quantity;
    private final double price;

    LineItem(String name, int quantity, double price) {
      this.name = name;
      this.quantity = quantity;
      this.price = price;
    }

    // equals and hashCode to support assertEquals on lists
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof LineItem)) return false;
      LineItem that = (LineItem) o;
      return quantity == that.quantity &&
             Double.compare(that.price, price) == 0 &&
             (name == null ? that.name == null : name.equals(that.name));
    }

    @Override
    public int hashCode() {
      int result;
      long temp;
      result = name != null ? name.hashCode() : 0;
      result = 31 * result + quantity;
      temp = Double.doubleToLongBits(price);
      result = 31 * result + (int) (temp ^ (temp >>> 32));
      return result;
    }
  }

  @Test
    @Timeout(8000)
  void testCartConstructorAndGetters() throws Exception {
    List<LineItem> items = new ArrayList<>();
    LineItem item1 = new LineItem("item1", 2, 10.0);
    LineItem item2 = new LineItem("item2", 1, 20.0);
    items.add(item1);
    items.add(item2);

    String buyer = "John Doe";
    String cc = "1234-5678-9012-3456";

    // Directly instantiate Cart class without reflection
    Cart cart = new Cart(items, buyer, cc);

    // Use getters directly
    List<LineItem> lineItems = cart.getLineItems();
    String buyerName = cart.getBuyerName();
    String creditCard = cart.getCreditCard();

    assertNotNull(cart);
    assertEquals(items, lineItems);
    assertEquals(buyer, buyerName);
    assertEquals(cc, creditCard);
  }
}