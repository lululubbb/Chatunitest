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

public class GsonProguardExampleActivity_227_4Test {

  @Test
    @Timeout(8000)
  void testBuildCart() throws Exception {
    // Use reflection to load the class and create an instance
    Class<?> activityClass = Class.forName("com.google.gson.examples.android.GsonProguardExampleActivity");
    Object activity = null;

    // Since GsonProguardExampleActivity extends android.app.Activity, which is not available,
    // create a proxy subclass dynamically that overrides buildCart for testing
    // But here, we just instantiate via reflection assuming no-arg constructor is accessible
    activity = activityClass.getDeclaredConstructor().newInstance();

    Method buildCartMethod = activityClass.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);

    Object result = buildCartMethod.invoke(activity);
    assertNotNull(result);

    // Use reflection to verify the Cart class and its methods
    Class<?> cartClass = result.getClass();

    // Verify Cart properties using reflection
    Method getLineItemsMethod = cartClass.getMethod("getLineItems");
    Object lineItemsObj = getLineItemsMethod.invoke(result);
    assertNotNull(lineItemsObj);

    List<?> lineItems = (List<?>) lineItemsObj;
    assertEquals(1, lineItems.size());

    Object item = lineItems.get(0);
    Class<?> lineItemClass = item.getClass();

    Method getNameMethod = lineItemClass.getMethod("getName");
    Method getQuantityMethod = lineItemClass.getMethod("getQuantity");
    Method getPriceMethod = lineItemClass.getMethod("getPrice");
    Method getCurrencyMethod = lineItemClass.getMethod("getCurrency");

    assertEquals("hammer", getNameMethod.invoke(item));
    assertEquals(1, getQuantityMethod.invoke(item));
    assertEquals(12000000, getPriceMethod.invoke(item));
    assertEquals("USD", getCurrencyMethod.invoke(item));

    Method getBuyerMethod = cartClass.getMethod("getBuyer");
    Method getCreditCardMethod = cartClass.getMethod("getCreditCard");

    assertEquals("Happy Buyer", getBuyerMethod.invoke(result));
    assertEquals("4111-1111-1111-1111", getCreditCardMethod.invoke(result));
  }
}