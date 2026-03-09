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
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class GsonProguardExampleActivity_226_6Test {

  private GsonProguardExampleActivity activity;
  private Bundle mockBundle;
  private TextView mockTextView;

  @BeforeEach
  public void setUp() {
    activity = Mockito.spy(new GsonProguardExampleActivity());
    mockBundle = mock(Bundle.class);
    mockTextView = mock(TextView.class);

    // Mock findViewById to return our mock TextView
    doReturn(mockTextView).when(activity).findViewById(2131230723);
    // Mock setContentView to do nothing
    doNothing().when(activity).setContentView(anyInt());
  }

  @Test
    @Timeout(8000)
  public void testOnCreate() throws Exception {
    // Because onCreate calls buildCart() which is private, we let it run normally

    // Call onCreate
    activity.onCreate(mockBundle);

    // Verify onCreate called on spy
    verify(activity).onCreate(mockBundle);

    // Verify setContentView called with R.layout.main
    verify(activity).setContentView(2131427356);

    // Verify findViewById called with R.id.tv
    verify(activity).findViewById(2131230723);

    // Verify that setText and invalidate called on TextView
    verify(mockTextView).setText(anyString());
    verify(mockTextView).invalidate();
  }

  @Test
    @Timeout(8000)
  public void testBuildCartViaReflection() throws Exception {
    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);
    Cart cart = (Cart) buildCartMethod.invoke(activity);
    assert cart != null;
  }
}