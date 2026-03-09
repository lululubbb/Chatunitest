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

class Excluder_445_1Test {

  @Test
    @Timeout(8000)
  void disableInnerClassSerialization_shouldReturnNewExcluderWithSerializeInnerClassesFalse() throws Exception {
    Excluder original = new Excluder();

    // use reflection to get original serializeInnerClasses value
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    boolean originalValue = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValue, "Original Excluder should have serializeInnerClasses = true");

    Excluder result = original.disableInnerClassSerialization();

    assertNotNull(result, "Result Excluder should not be null");
    assertNotSame(original, result, "disableInnerClassSerialization should return a new Excluder instance");

    // verify that the serializeInnerClasses field in result is false
    boolean resultValue = serializeInnerClassesField.getBoolean(result);
    assertFalse(resultValue, "Result Excluder should have serializeInnerClasses = false");

    // verify original is unchanged
    boolean originalValueAfter = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValueAfter, "Original Excluder serializeInnerClasses should remain true");

    // verify other fields remain equal - version, modifiers, requireExpose, strategies
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    assertEquals(versionField.getDouble(original), versionField.getDouble(result), 0.0);

    Field modifiersField = Excluder.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    assertEquals(modifiersField.getInt(original), modifiersField.getInt(result));

    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);
    assertEquals(requireExposeField.getBoolean(original), requireExposeField.getBoolean(result));

    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    assertEquals(serializationStrategiesField.get(original), serializationStrategiesField.get(result));

    Field deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
    deserializationStrategiesField.setAccessible(true);
    assertEquals(deserializationStrategiesField.get(original), deserializationStrategiesField.get(result));
  }
}