package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.examples.android.model.LineItem;

import static org.mockito.Mockito.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GsonProguardExampleActivity_226_5Test {

  private GsonProguardExampleActivity activity;
  private TextView mockTextView;

  @BeforeEach
  public void setUp() {
    activity = spy(new GsonProguardExampleActivity());

    // Mock findViewById to return a mocked TextView
    mockTextView = mock(TextView.class);
    doReturn(mockTextView).when(activity).findViewById(anyInt());
  }

  @Test
    @Timeout(8000)
  public void testOnCreate() throws Exception {
    // Mock Bundle parameter
    Bundle mockBundle = mock(Bundle.class);

    // Spy on activity to mock super.onCreate and setContentView
    doNothing().when(activity).superOnCreate(any());
    doNothing().when(activity).setContentView(anyInt());

    // Mock buildCart() to return a controlled Cart instance via reflection
    Cart mockCart = mock(Cart.class);
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    doReturn(mockCart).when(activity, buildCartMethod);

    // Mock Cart.toString() for consistent output
    when(mockCart.toString()).thenReturn("mockCartToString");

    // Mock R.layout.main and R.id.tv constants
    int mockLayoutMain = 12345;
    int mockIdTv = 54321;

    // Use reflection to set R.layout.main and R.id.tv fields if possible
    // Otherwise, mock their usage by stubbing setContentView and findViewById with these values

    // To avoid compile errors related to R, create dummy constants:
    // (Alternatively you can create a dummy R class in test sources.)

    // Prepare Gson mock to control toJson and fromJson
    try (MockedStatic<Gson> gsonStaticMock = Mockito.mockStatic(Gson.class)) {
      Gson gson = spy(new Gson());
      gsonStaticMock.when(Gson::new).thenReturn(gson);

      // Mock gson.toJson(cart)
      doReturn("mockJson").when(gson).toJson(mockCart);

      // Mock gson.fromJson(json, Cart.class)
      Cart fromJsonCart = mock(Cart.class);
      when(fromJsonCart.toString()).thenReturn("mockFromJsonCartToString");
      doReturn(fromJsonCart).when(gson).fromJson(anyString(), eq(Cart.class));

      // When setContentView is called with any int, do nothing
      doNothing().when(activity).setContentView(anyInt());

      // When findViewById is called with any int, return mockTextView
      doReturn(mockTextView).when(activity).findViewById(anyInt());

      // Call onCreate
      activity.onCreate(mockBundle);

      // Verify calls
      verify(activity).superOnCreate(mockBundle);
      verify(activity).setContentView(anyInt());
      verify(activity).findViewById(anyInt());
      verify(mockTextView).setText(org.mockito.ArgumentMatchers.contains("mockCartToString"));
      verify(mockTextView).invalidate();
    }
  }

  // Helper to call private buildCart() via reflection
  @Test
    @Timeout(8000)
  public void testBuildCart() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    Cart cart = (Cart) buildCartMethod.invoke(activity);
    // Assert cart is not null
    assert cart != null;
  }
}