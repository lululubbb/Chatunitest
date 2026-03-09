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

public class Excluder_444_6Test {

  @Test
    @Timeout(8000)
  public void testWithModifiers_noModifiers() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers();
    assertNotNull(result);
    assertNotSame(excluder, result);
    // default modifiers are TRANSIENT | STATIC, after withModifiers() with no args modifiers should be 0
    // use reflection to verify private field modifiers
    int modifiers = getModifiers(result);
    assertEquals(0, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_singleModifier() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers(Modifier.FINAL);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiers = getModifiers(result);
    assertEquals(Modifier.FINAL, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_multipleModifiers() {
    Excluder excluder = new Excluder();
    int combined = Modifier.FINAL | Modifier.PUBLIC | Modifier.PROTECTED;
    Excluder result = excluder.withModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.PROTECTED);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiers = getModifiers(result);
    assertEquals(combined, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_cloneCalled() {
    // Since clone() is protected, we test indirectly that a new instance is returned and fields are reset
    Excluder excluder = new Excluder();
    int originalModifiers = getModifiers(excluder);
    Excluder result = excluder.withModifiers(Modifier.FINAL);
    assertNotSame(excluder, result);
    int newModifiers = getModifiers(result);
    assertNotEquals(originalModifiers, newModifiers);
    assertEquals(Modifier.FINAL, newModifiers);
  }

  private int getModifiers(Excluder excluder) {
    try {
      Field modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      return modifiersField.getInt(excluder);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}