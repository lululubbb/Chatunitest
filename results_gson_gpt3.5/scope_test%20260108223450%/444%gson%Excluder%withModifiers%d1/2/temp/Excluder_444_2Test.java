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

public class Excluder_444_2Test {

  @Test
    @Timeout(8000)
  public void testWithModifiers_noModifiers_returnsCloneWithZeroModifiers() {
    Excluder original = new Excluder();
    Excluder result = original.withModifiers();

    assertNotSame(original, result);
    // modifiers field is private, use reflection to verify
    int modifiers = getModifiers(result);
    assertEquals(0, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_singleModifier_setsThatModifier() {
    Excluder original = new Excluder();
    int modifier = Modifier.FINAL;
    Excluder result = original.withModifiers(modifier);

    assertNotSame(original, result);
    int modifiers = getModifiers(result);
    assertEquals(modifier, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_multipleModifiers_combinesModifiers() {
    Excluder original = new Excluder();
    int modifier1 = Modifier.FINAL;
    int modifier2 = Modifier.PUBLIC;
    int modifier3 = Modifier.STATIC;

    Excluder result = original.withModifiers(modifier1, modifier2, modifier3);

    assertNotSame(original, result);
    int modifiers = getModifiers(result);
    int expected = modifier1 | modifier2 | modifier3;
    assertEquals(expected, modifiers);
  }

  @Test
    @Timeout(8000)
  public void testWithModifiers_originalModifiersUnchanged() {
    Excluder original = new Excluder();
    int originalModifiers = getModifiers(original);

    Excluder result = original.withModifiers(Modifier.FINAL);

    int afterOriginalModifiers = getModifiers(original);
    assertEquals(originalModifiers, afterOriginalModifiers);
    assertNotEquals(afterOriginalModifiers, getModifiers(result));
  }

  private int getModifiers(Excluder excluder) {
    try {
      Field modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      return modifiersField.getInt(excluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}