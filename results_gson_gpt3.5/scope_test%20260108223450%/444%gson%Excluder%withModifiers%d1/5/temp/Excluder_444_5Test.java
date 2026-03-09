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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class Excluder_444_5Test {

  @Test
    @Timeout(8000)
  void testWithModifiers_noModifiers() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers();
    assertNotNull(result);
    assertNotSame(excluder, result);
    // Default modifiers are TRANSIENT | STATIC, after withModifiers() with no args, modifiers=0
    // So result.modifiers should be 0
    int modifiersField = getModifiersFieldValue(result);
    assertEquals(0, modifiersField);
  }

  @Test
    @Timeout(8000)
  void testWithModifiers_singleModifier() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers(Modifier.FINAL);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiersField = getModifiersFieldValue(result);
    assertEquals(Modifier.FINAL, modifiersField);
  }

  @Test
    @Timeout(8000)
  void testWithModifiers_multipleModifiers() {
    Excluder excluder = new Excluder();
    int combined = Modifier.FINAL | Modifier.PUBLIC;
    Excluder result = excluder.withModifiers(Modifier.FINAL, Modifier.PUBLIC);
    assertNotNull(result);
    assertNotSame(excluder, result);
    int modifiersField = getModifiersFieldValue(result);
    assertEquals(combined, modifiersField);
  }

  private int getModifiersFieldValue(Excluder excluder) {
    try {
      java.lang.reflect.Field field = Excluder.class.getDeclaredField("modifiers");
      field.setAccessible(true);
      return field.getInt(excluder);
    } catch (Exception e) {
      fail("Failed to get 'modifiers' field value via reflection: " + e.getMessage());
      return -1;
    }
  }
}