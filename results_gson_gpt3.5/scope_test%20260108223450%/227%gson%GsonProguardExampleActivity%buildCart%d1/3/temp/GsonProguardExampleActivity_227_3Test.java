package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.Cart;
import com.google.gson.examples.android.model.LineItem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class GsonProguardExampleActivity_227_3Test {

  @Test
    @Timeout(8000)
  void testBuildCart() throws Exception {
    // Use reflection to load the Activity class to avoid Android dependencies at compile time
    Class<?> activityClass = Class.forName("com.google.gson.examples.android.GsonProguardExampleActivity");
    Object activity = activityClass.getDeclaredConstructor().newInstance();

    Method buildCartMethod = activityClass.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);

    Object result = buildCartMethod.invoke(activity);
    assertNotNull(result);

    // Use reflection to verify the returned object's properties without importing Cart and LineItem
    Class<?> cartClass = result.getClass();

    Method getBuyerNameMethod = cartClass.getMethod("getBuyerName");
    String buyerName = (String) getBuyerNameMethod.invoke(result);
    assertEquals("Happy Buyer", buyerName);

    Method getCardNumberMethod = cartClass.getMethod("getCardNumber");
    String cardNumber = (String) getCardNumberMethod.invoke(result);
    assertEquals("4111-1111-1111-1111", cardNumber);

    Method getLineItemsMethod = cartClass.getMethod("getLineItems");
    Object lineItemsObj = getLineItemsMethod.invoke(result);
    assertNotNull(lineItemsObj);

    // lineItemsObj is a List<LineItem>
    // Use reflection to check list size and properties
    Class<?> listClass = lineItemsObj.getClass();
    Method sizeMethod = listClass.getMethod("size");
    int size = (int) sizeMethod.invoke(lineItemsObj);
    assertEquals(1, size);

    Method getMethod = listClass.getMethod("get", int.class);
    Object lineItem = getMethod.invoke(lineItemsObj, 0);
    assertNotNull(lineItem);

    Class<?> lineItemClass = lineItem.getClass();

    Method getNameMethod = lineItemClass.getMethod("getName");
    String name = (String) getNameMethod.invoke(lineItem);
    assertEquals("hammer", name);

    Method getQuantityMethod = lineItemClass.getMethod("getQuantity");
    int quantity = (int) getQuantityMethod.invoke(lineItem);
    assertEquals(1, quantity);

    Method getPriceMethod = lineItemClass.getMethod("getPrice");
    int price = (int) getPriceMethod.invoke(lineItem);
    assertEquals(12000000, price);

    Method getCurrencyCodeMethod = lineItemClass.getMethod("getCurrencyCode");
    String currencyCode = (String) getCurrencyCodeMethod.invoke(lineItem);
    assertEquals("USD", currencyCode);
  }
}