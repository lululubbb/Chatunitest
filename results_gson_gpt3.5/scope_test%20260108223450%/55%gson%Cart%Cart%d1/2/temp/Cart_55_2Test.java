package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Cart_55_2Test {

  @Test
    @Timeout(8000)
  void testCartConstructorAndGetters() throws Exception {
    LineItem item1 = new LineItem("item1", 2, 10.0);
    LineItem item2 = new LineItem("item2", 1, 5.5);
    List<LineItem> lineItems = Arrays.asList(item1, item2);
    String buyerName = "John Doe";
    String creditCard = "1234-5678-9012-3456";

    // Fully qualified name to fix "cannot find symbol Cart"
    com.google.gson.examples.android.model.Cart cart = new com.google.gson.examples.android.model.Cart(lineItems, buyerName, creditCard);

    // Verify fields via getters
    assertEquals(lineItems, cart.getLineItems());
    assertEquals(buyerName, cart.getBuyerName());
    assertEquals(creditCard, cart.getCreditCard());

    // Verify final fields via reflection
    Field lineItemsField = com.google.gson.examples.android.model.Cart.class.getField("lineItems");
    assertEquals(lineItems, lineItemsField.get(cart));

    Field buyerNameField = com.google.gson.examples.android.model.Cart.class.getDeclaredField("buyerName");
    buyerNameField.setAccessible(true);
    assertEquals(buyerName, buyerNameField.get(cart));

    Field creditCardField = com.google.gson.examples.android.model.Cart.class.getDeclaredField("creditCard");
    creditCardField.setAccessible(true);
    assertEquals(creditCard, creditCardField.get(cart));
  }
}

// Supporting class to allow compilation
class LineItem {
  private final String name;
  private final int quantity;
  private final double price;

  public LineItem(String name, int quantity, double price) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  // equals and hashCode to allow assertEquals on lists
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LineItem)) return false;
    LineItem that = (LineItem) o;
    return quantity == that.quantity &&
        Double.compare(that.price, price) == 0 &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = name.hashCode();
    result = 31 * result + quantity;
    temp = Double.doubleToLongBits(price);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}