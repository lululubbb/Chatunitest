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

class Excluder_442_2Test {

  @Test
    @Timeout(8000)
  void clone_shouldReturnDistinctButEqualInstance() {
    Excluder original = new Excluder();
    Excluder clone = original.clone();

    assertNotSame(original, clone);
    // Check that fields are equal (using reflection since fields are private)
    try {
      var versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      assertEquals(versionField.get(original), versionField.get(clone));

      var modifiersField = Excluder.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      assertEquals(modifiersField.get(original), modifiersField.get(clone));

      var serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
      serializeInnerClassesField.setAccessible(true);
      assertEquals(serializeInnerClassesField.get(original), serializeInnerClassesField.get(clone));

      var requireExposeField = Excluder.class.getDeclaredField("requireExpose");
      requireExposeField.setAccessible(true);
      assertEquals(requireExposeField.get(original), requireExposeField.get(clone));

      var serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
      serializationStrategiesField.setAccessible(true);
      assertEquals(serializationStrategiesField.get(original), serializationStrategiesField.get(clone));

      var deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
      deserializationStrategiesField.setAccessible(true);
      assertEquals(deserializationStrategiesField.get(original), deserializationStrategiesField.get(clone));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}