package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.Cart;
import com.google.gson.examples.android.model.LineItem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.List;

public class GsonProguardExampleActivity_227_6Test {

  @Test
    @Timeout(8000)
  void testBuildCart() throws Exception {
    // Use reflection to create instance without calling Android Activity lifecycle
    Class<?> activityClass = Class.forName("com.google.gson.examples.android.GsonProguardExampleActivity");
    Object activity = activityClass.getDeclaredConstructor().newInstance();

    Method buildCartMethod = activityClass.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    Object cartObj = buildCartMethod.invoke(activity);

    assertNotNull(cartObj);

    Class<?> cartClass = Class.forName("com.google.gson.examples.android.model.Cart");
    Class<?> lineItemClass = Class.forName("com.google.gson.examples.android.model.LineItem");

    Object cart = cartClass.cast(cartObj);

    Method getBuyerMethod = cartClass.getMethod("getBuyer");
    Method getCreditCardMethod = cartClass.getMethod("getCreditCard");
    Method getLineItemsMethod = cartClass.getMethod("getLineItems");

    assertEquals("Happy Buyer", getBuyerMethod.invoke(cart));
    assertEquals("4111-1111-1111-1111", getCreditCardMethod.invoke(cart));

    @SuppressWarnings("unchecked")
    List<?> lineItems = (List<?>) getLineItemsMethod.invoke(cart);
    assertNotNull(lineItems);
    assertEquals(1, lineItems.size());

    Object item = lineItems.get(0);

    Method getNameMethod = lineItemClass.getMethod("getName");
    Method getQuantityMethod = lineItemClass.getMethod("getQuantity");
    Method getPriceMethod = lineItemClass.getMethod("getPrice");
    Method getCurrencyCodeMethod = lineItemClass.getMethod("getCurrencyCode");

    assertEquals("hammer", getNameMethod.invoke(item));
    assertEquals(1, ((Number)getQuantityMethod.invoke(item)).intValue());
    assertEquals(12000000, ((Number)getPriceMethod.invoke(item)).intValue());
    assertEquals("USD", getCurrencyCodeMethod.invoke(item));
  }
}