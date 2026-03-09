package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.examples.android.model.Cart;
import com.google.gson.examples.android.model.LineItem;

public class GsonProguardExampleActivity_227_1Test {

  @Test
    @Timeout(8000)
  public void testBuildCart() throws Exception {
    // Use reflection to access the private buildCart method without subclassing
    GsonProguardExampleActivity activity = new GsonProguardExampleActivity();
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    Cart cart = (Cart) buildCartMethod.invoke(activity);

    assertNotNull(cart);
    assertEquals("Happy Buyer", cart.getBuyer());
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