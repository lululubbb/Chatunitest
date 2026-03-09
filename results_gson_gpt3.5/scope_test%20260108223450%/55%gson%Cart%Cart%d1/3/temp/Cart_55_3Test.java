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

class Cart_55_3Test {

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

    // Use reflection to create Cart instance because Cart class is in the same package but not imported explicitly
    Class<?> cartClass = Class.forName("com.google.gson.examples.android.model.Cart");
    Constructor<?> constructor = cartClass.getConstructor(List.class, String.class, String.class);
    Object cart = constructor.newInstance(items, buyerName, creditCard);

    assertNotNull(cart);

    // Use reflection to invoke getters
    @SuppressWarnings("unchecked")
    List<LineItem> lineItems = (List<LineItem>) cartClass.getMethod("getLineItems").invoke(cart);
    String actualBuyerName = (String) cartClass.getMethod("getBuyerName").invoke(cart);
    String actualCreditCard = (String) cartClass.getMethod("getCreditCard").invoke(cart);

    assertEquals(items, lineItems);
    assertEquals(buyerName, actualBuyerName);
    assertEquals(creditCard, actualCreditCard);
  }
}

// Minimal LineItem class to allow compilation
class LineItem {
  private final String name;
  private final int quantity;
  private final double price;

  public LineItem(String name, int quantity, double price) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public String getName() {
    return name;
  }
  public int getQuantity() {
    return quantity;
  }
  public double getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LineItem)) return false;
    LineItem that = (LineItem) o;
    return quantity == that.quantity &&
        Double.compare(that.price, price) == 0 &&
        (name != null ? name.equals(that.name) : that.name == null);
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