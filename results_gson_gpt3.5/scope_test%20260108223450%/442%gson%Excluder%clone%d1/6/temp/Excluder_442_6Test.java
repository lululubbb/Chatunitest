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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

class ExcluderCloneTest {

  @Test
    @Timeout(8000)
  void clone_shouldReturnDistinctButEqualInstance() {
    Excluder original = new Excluder();
    Excluder cloned = original.clone();

    assertNotSame(original, cloned, "Clone should create a distinct instance");
    // Since Excluder has no equals override, check fields by reflection

    try {
      Field versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      assertEquals(versionField.getDouble(original), versionField.getDouble(cloned), "version should be equal");

      Field modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      assertEquals(modifiersField.getInt(original), modifiersField.getInt(cloned), "modifiers should be equal");

      Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
      serializeInnerClassesField.setAccessible(true);
      assertEquals(serializeInnerClassesField.getBoolean(original), serializeInnerClassesField.getBoolean(cloned), "serializeInnerClasses should be equal");

      Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
      requireExposeField.setAccessible(true);
      assertEquals(requireExposeField.getBoolean(original), requireExposeField.getBoolean(cloned), "requireExpose should be equal");

      Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
      serializationStrategiesField.setAccessible(true);
      Object originalSerializationStrategies = serializationStrategiesField.get(original);
      Object clonedSerializationStrategies = serializationStrategiesField.get(cloned);
      assertNotNull(originalSerializationStrategies, "serializationStrategies should not be null");
      assertNotNull(clonedSerializationStrategies, "serializationStrategies should not be null");
      assertEquals(originalSerializationStrategies, clonedSerializationStrategies, "serializationStrategies should be equal");
      assertNotSame(originalSerializationStrategies, clonedSerializationStrategies, "serializationStrategies should not be the same instance");

      Field deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
      deserializationStrategiesField.setAccessible(true);
      Object originalDeserializationStrategies = deserializationStrategiesField.get(original);
      Object clonedDeserializationStrategies = deserializationStrategiesField.get(cloned);
      assertNotNull(originalDeserializationStrategies, "deserializationStrategies should not be null");
      assertNotNull(clonedDeserializationStrategies, "deserializationStrategies should not be null");
      assertEquals(originalDeserializationStrategies, clonedDeserializationStrategies, "deserializationStrategies should be equal");
      assertNotSame(originalDeserializationStrategies, clonedDeserializationStrategies, "deserializationStrategies should not be the same instance");

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }
}