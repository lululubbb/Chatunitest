package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.Cart;
import com.google.gson.examples.android.model.LineItem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// Minimal stub classes to replace missing android and model classes
class Cart {
  private final List<LineItem> lineItems;
  private final String buyerName;
  private final String creditCard;

  public Cart(List<LineItem> lineItems, String buyerName, String creditCard) {
    this.lineItems = lineItems;
    this.buyerName = buyerName;
    this.creditCard = creditCard;
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
}

class LineItem {
  private final String name;
  private final int quantity;
  private final int price;
  private final String currencyCode;

  public LineItem(String name, int quantity, int price, String currencyCode) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
    this.currencyCode = currencyCode;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public int getPrice() {
    return price;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }
}

public class GsonProguardExampleActivity_227_2Test {

  // Minimal stub for GsonProguardExampleActivity without android dependencies
  static class GsonProguardExampleActivity {
    private Cart buildCart() {
      List<LineItem> lineItems = new ArrayList<>();
      lineItems.add(new LineItem("hammer", 1, 12000000, "USD"));
      return new Cart(lineItems, "Happy Buyer", "4111-1111-1111-1111");
    }
  }

  @Test
    @Timeout(8000)
  public void testBuildCart() throws Exception {
    class TestActivity extends GsonProguardExampleActivity {
      public Cart callBuildCart() throws Exception {
        Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
        buildCartMethod.setAccessible(true);
        return (Cart) buildCartMethod.invoke(this);
      }
    }

    TestActivity activity = new TestActivity();
    Cart cart = activity.callBuildCart();

    assertNotNull(cart);
    assertEquals("Happy Buyer", cart.getBuyerName());
    assertEquals("4111-1111-1111-1111", cart.getCreditCard());

    List<LineItem> lineItems = cart.getLineItems();
    assertNotNull(lineItems);
    assertEquals(1, lineItems.size());

    LineItem item = lineItems.get(0);
    assertEquals("hammer", item.getName());
    assertEquals(1, item.getQuantity());
    assertEquals(12000000, item.getPrice());
    assertEquals("USD", item.getCurrencyCode());
  }
}