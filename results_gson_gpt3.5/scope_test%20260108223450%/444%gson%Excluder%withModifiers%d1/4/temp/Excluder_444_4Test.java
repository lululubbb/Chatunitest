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

public class Excluder_444_4Test {

  @Test
    @Timeout(8000)
  public void testWithModifiers_noModifiers_returnsCloneWithZeroModifiers() {
    Excluder excluder = new Excluder();
    Excluder result = excluder.withModifiers();
    assertNotSame(excluder, result);
    // modifiers should be 0
    int modifiersField = getModifiersField(result);
    assertEquals(0, modifiersField);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_singleModifier_setsThatModifier() {
    Excluder excluder = new Excluder();
    int modifier = Modifier.FINAL;
    Excluder result = excluder.withModifiers(modifier);
    assertNotSame(excluder, result);
    int modifiersField = getModifiersField(result);
    assertEquals(modifier, modifiersField);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_multipleModifiers_combinesModifiers() {
    Excluder excluder = new Excluder();
    int modifier1 = Modifier.FINAL;
    int modifier2 = Modifier.PUBLIC;
    int modifier3 = Modifier.STATIC;
    Excluder result = excluder.withModifiers(modifier1, modifier2, modifier3);
    assertNotSame(excluder, result);
    int modifiersField = getModifiersField(result);
    int expected = modifier1 | modifier2 | modifier3;
    assertEquals(expected, modifiersField);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_originalExcluderModifiersUnchanged() {
    Excluder excluder = new Excluder();
    int originalModifiers = getModifiersField(excluder);
    int modifier = Modifier.FINAL;
    Excluder result = excluder.withModifiers(modifier);
    assertNotSame(excluder, result);
    int newModifiers = getModifiersField(result);
    assertEquals(modifier, newModifiers);
    // original should remain unchanged
    assertEquals(originalModifiers, getModifiersField(excluder));
  }

  private int getModifiersField(Excluder excluder) {
    try {
      Field modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      return modifiersField.getInt(excluder);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}