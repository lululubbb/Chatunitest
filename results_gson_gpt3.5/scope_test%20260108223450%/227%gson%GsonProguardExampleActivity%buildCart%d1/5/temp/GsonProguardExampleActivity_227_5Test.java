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
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.List;

public class GsonProguardExampleActivity_227_5Test {

  @Test
    @Timeout(8000)
  public void testBuildCart() throws Exception {
    // Mock the GsonProguardExampleActivity class to avoid Android dependencies
    GsonProguardExampleActivity activity = Mockito.mock(GsonProguardExampleActivity.class, Mockito.CALLS_REAL_METHODS);

    // Use reflection to access the private buildCart method
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);

    Object cart = buildCartMethod.invoke(activity);
    assertNotNull(cart);

    Class<?> cartClass = cart.getClass();

    Method getLineItemsMethod = cartClass.getDeclaredMethod("getLineItems");
    getLineItemsMethod.setAccessible(true);
    Object lineItemsObj = getLineItemsMethod.invoke(cart);
    assertNotNull(lineItemsObj);

    List<?> lineItems = (List<?>) lineItemsObj;
    assertEquals(1, lineItems.size());

    Object item = lineItems.get(0);
    Class<?> lineItemClass = item.getClass();

    Method getNameMethod = lineItemClass.getDeclaredMethod("getName");
    Method getQuantityMethod = lineItemClass.getDeclaredMethod("getQuantity");
    Method getPriceMethod = lineItemClass.getDeclaredMethod("getPrice");
    Method getCurrencyMethod = lineItemClass.getDeclaredMethod("getCurrency");

    getNameMethod.setAccessible(true);
    getQuantityMethod.setAccessible(true);
    getPriceMethod.setAccessible(true);
    getCurrencyMethod.setAccessible(true);

    assertEquals("hammer", getNameMethod.invoke(item));
    assertEquals(1, getQuantityMethod.invoke(item));
    assertEquals(12000000, getPriceMethod.invoke(item));
    assertEquals("USD", getCurrencyMethod.invoke(item));

    Method getBuyerMethod = cartClass.getDeclaredMethod("getBuyer");
    Method getCreditCardMethod = cartClass.getDeclaredMethod("getCreditCard");
    getBuyerMethod.setAccessible(true);
    getCreditCardMethod.setAccessible(true);

    assertEquals("Happy Buyer", getBuyerMethod.invoke(cart));
    assertEquals("4111-1111-1111-1111", getCreditCardMethod.invoke(cart));
  }
}