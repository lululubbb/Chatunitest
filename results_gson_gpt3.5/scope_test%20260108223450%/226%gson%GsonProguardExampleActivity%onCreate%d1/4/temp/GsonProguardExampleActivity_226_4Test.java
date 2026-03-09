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

import java.lang.reflect.Method;

public class GsonProguardExampleActivity_226_4Test {

  private GsonProguardExampleActivity activity;
  private TextView mockTextView;

  @BeforeEach
  public void setUp() {
    activity = spy(new GsonProguardExampleActivity());
    mockTextView = mock(TextView.class);

    doNothing().when(activity).setContentView(anyInt());
    doReturn(mockTextView).when(activity).findViewById(anyInt());
  }

  @Test
    @Timeout(8000)
  public void testOnCreate() throws Exception {
    Bundle mockBundle = mock(Bundle.class);

    Cart dummyCart = new Cart();

    Method buildCartMethod = GsonProguardExampleActivity.class.getDeclaredMethod("buildCart");
    buildCartMethod.setAccessible(true);

    doReturn(dummyCart).when(activity).buildCart();

    activity.onCreate(mockBundle);

    verify(activity).setContentView(anyInt());
    verify(activity).findViewById(anyInt());
    verify(mockTextView).setText(anyString());
    verify(mockTextView).invalidate();
  }
}