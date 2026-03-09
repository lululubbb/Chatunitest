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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Excluder_442_5Test {

  @Test
    @Timeout(8000)
  void clone_returnsDistinctButEqualInstance() {
    Excluder original = new Excluder();
    Excluder cloned = original.clone();

    assertNotSame(original, cloned);
    // The default Excluder has IGNORE_VERSIONS = -1.0d, modifiers, flags etc.
    // Since fields are private and no equals(), check fields via reflection:

    try {
      var versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      double originalVersion = versionField.getDouble(original);
      double clonedVersion = versionField.getDouble(cloned);
      assertEquals(originalVersion, clonedVersion);

      var modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      int originalModifiers = modifiersField.getInt(original);
      int clonedModifiers = modifiersField.getInt(cloned);
      assertEquals(originalModifiers, clonedModifiers);

      var serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
      serializeInnerClassesField.setAccessible(true);
      boolean originalSerializeInnerClasses = serializeInnerClassesField.getBoolean(original);
      boolean clonedSerializeInnerClasses = serializeInnerClassesField.getBoolean(cloned);
      assertEquals(originalSerializeInnerClasses, clonedSerializeInnerClasses);

      var requireExposeField = Excluder.class.getDeclaredField("requireExpose");
      requireExposeField.setAccessible(true);
      boolean originalRequireExpose = requireExposeField.getBoolean(original);
      boolean clonedRequireExpose = requireExposeField.getBoolean(cloned);
      assertEquals(originalRequireExpose, clonedRequireExpose);

      var serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
      serializationStrategiesField.setAccessible(true);
      Object originalSerializationStrategies = serializationStrategiesField.get(original);
      Object clonedSerializationStrategies = serializationStrategiesField.get(cloned);
      assertEquals(originalSerializationStrategies, clonedSerializationStrategies);

      var deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
      deserializationStrategiesField.setAccessible(true);
      Object originalDeserializationStrategies = deserializationStrategiesField.get(original);
      Object clonedDeserializationStrategies = deserializationStrategiesField.get(cloned);
      assertEquals(originalDeserializationStrategies, clonedDeserializationStrategies);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}