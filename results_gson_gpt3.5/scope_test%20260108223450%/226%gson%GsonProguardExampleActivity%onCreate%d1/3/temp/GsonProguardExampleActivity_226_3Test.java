package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.LineItem;

import static org.mockito.Mockito.*;

import android.os.Bundle;
import android.widget.TextView;
import com.google.gson.examples.android.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonProguardExampleActivity_226_3Test {

  private GsonProguardExampleActivity activity;
  private Bundle mockBundle;
  private TextView mockTextView;

  @BeforeEach
  void setUp() {
    activity = spy(new GsonProguardExampleActivity());
    mockBundle = mock(Bundle.class);
    mockTextView = mock(TextView.class);

    // Mock findViewById to return our mock TextView
    doReturn(mockTextView).when(activity).findViewById(anyInt());

    // Mock setContentView to do nothing
    doNothing().when(activity).setContentView(anyInt());
  }

  @Test
    @Timeout(8000)
  void testOnCreate() {
    // Call onCreate with the mocked Bundle
    activity.onCreate(mockBundle);

    // Verify setContentView called once with R.layout.main
    verify(activity).setContentView(R.layout.main);

    // Verify findViewById called with R.id.tv
    verify(activity).findViewById(R.id.tv);

    // Verify setText and invalidate called on the TextView
    verify(mockTextView).setText(anyString());
    verify(mockTextView).invalidate();
  }

  @Test
    @Timeout(8000)
  void testBuildCartUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);

    Cart cart = (Cart) buildCartMethod.invoke(activity);
    // Basic assertions to check cart is not null
    assert cart != null;
  }
}