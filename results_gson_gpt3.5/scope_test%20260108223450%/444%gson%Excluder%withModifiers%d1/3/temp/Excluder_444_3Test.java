package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public class Excluder_444_3Test {

  @Test
    @Timeout(8000)
  public void testWithModifiers_noModifiers() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers();
    assertNotNull(result);
    assertNotSame(excluder, result);
    // By default modifiers is TRANSIENT | STATIC
    // withModifiers with no args resets to 0
    // Use reflection to verify private field modifiers
    int modifiers = getModifiers(result);
    assertEquals(0, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_singleModifier() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers(Modifier.PUBLIC);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiers = getModifiers(result);
    assertEquals(Modifier.PUBLIC, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_multipleModifiers() {
    Excluder excluder = new Excluder();
    int mod1 = Modifier.PUBLIC;
    int mod2 = Modifier.FINAL;
    int mod3 = Modifier.VOLATILE;
    Excluder result = excluder.withModifiers(mod1, mod2, mod3);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiers = getModifiers(result);
    assertEquals(mod1 | mod2 | mod3, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_cloneCalled() {
    Excluder excluderSpy = spy(new Excluder());
    // Call withModifiers and verify clone() called once
    excluderSpy.withModifiers(Modifier.PUBLIC);
    verify(excluderSpy, times(1)).clone();
  }

  private int getModifiers(Excluder excluder) {
    try {
      Field modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      return modifiersField.getInt(excluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
      return -1;
    }
  }
}