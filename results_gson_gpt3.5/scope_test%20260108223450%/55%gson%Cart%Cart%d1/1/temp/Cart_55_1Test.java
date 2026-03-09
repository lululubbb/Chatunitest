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
import java.lang.reflect.Constructor;

class Cart_55_1Test {

  @Test
    @Timeout(8000)
  void testCartConstructorAndGetters() throws Exception {
    List<LineItem> items = new ArrayList<>();
    LineItem item1 = new LineItem("item1", 2, 10.0);
    LineItem item2 = new LineItem("item2", 1, 20.0);
    items.add(item1);
    items.add(item2);

    String buyerName = "John Doe";
    String creditCard = "1234-5678-9876-5432";

    // Use reflection to create Cart instance
    Class<?> cartClass = Class.forName("com.google.gson.examples.android.model.Cart");
    Constructor<?> constructor = cartClass.getConstructor(List.class, String.class, String.class);
    Object cart = constructor.newInstance(items, buyerName, creditCard);

    // Access public final field lineItems via reflection
    @SuppressWarnings("unchecked")
    List<LineItem> lineItems = (List<LineItem>) cartClass.getField("lineItems").get(cart);

    assertNotNull(lineItems);
    assertEquals(items, lineItems);

    // Invoke getters via reflection
    String gotBuyerName = (String) cartClass.getMethod("getBuyerName").invoke(cart);
    String gotCreditCard = (String) cartClass.getMethod("getCreditCard").invoke(cart);

    assertEquals(buyerName, gotBuyerName);
    assertEquals(creditCard, gotCreditCard);
  }

  // Minimal LineItem stub for compilation
  private static class LineItem {
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
      LineItem other = (LineItem) o;
      return quantity == other.quantity &&
             Double.compare(other.price, price) == 0 &&
             (name == null ? other.name == null : name.equals(other.name));
    }

    @Override
    public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      long temp = Double.doubleToLongBits(price);
      result = 31 * result + quantity;
      result = 31 * result + (int) (temp ^ (temp >>> 32));
      return result;
    }
  }
}