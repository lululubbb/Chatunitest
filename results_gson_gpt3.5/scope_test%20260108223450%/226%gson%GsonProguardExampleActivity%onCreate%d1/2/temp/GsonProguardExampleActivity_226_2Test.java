package com.google.gson.examples.android;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.examples.android.model.LineItem;

import static org.mockito.Mockito.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.examples.android.model.Cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GsonProguardExampleActivity_226_2Test {

  GsonProguardExampleActivity activity;
  Bundle bundle;

  @BeforeEach
  void setUp() {
    activity = Mockito.spy(new GsonProguardExampleActivity());
    bundle = mock(Bundle.class);
  }

  @Test
    @Timeout(8000)
  void testOnCreate() throws Exception {
    // Mock methods setContentView and findViewById
    doNothing().when(activity).setContentView(anyInt());

    TextView mockTextView = mock(TextView.class);
    doReturn(mockTextView).when(activity).findViewById(anyInt());

    // Use reflection to invoke private buildCart method and verify it returns a Cart instance
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    Cart cart = (Cart) buildCartMethod.invoke(activity);
    assertNotNull(cart);

    // Call onCreate
    activity.onCreate(bundle);

    // Verify setContentView called with any int (R.layout.main)
    verify(activity).setContentView(anyInt());

    // Verify findViewById called with any int (R.id.tv)
    verify(activity).findViewById(anyInt());

    // Capture the text set on the TextView
    ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
    verify(mockTextView).setText(captor.capture());
    verify(mockTextView).invalidate();

    CharSequence setText = captor.getValue();
    assertNotNull(setText);
    String text = setText.toString();

    // Check that the text contains expected substrings
    assertTrue(text.contains("Gson.toJson() example:"));
    assertTrue(text.contains("Cart Object:"));
    assertTrue(text.contains("Cart JSON:"));
    assertTrue(text.contains("Gson.fromJson() example:"));
    assertTrue(text.contains("Cart JSON: {buyer:'Happy Camper'"));
    assertTrue(text.contains("Cart Object:"));
  }
}