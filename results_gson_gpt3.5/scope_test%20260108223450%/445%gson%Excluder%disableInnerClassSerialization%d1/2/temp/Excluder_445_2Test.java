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

class Excluder_445_2Test {

  @Test
    @Timeout(8000)
  void disableInnerClassSerialization_shouldReturnNewExcluderWithSerializeInnerClassesFalse() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to get the original value of serializeInnerClasses
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    boolean originalValue = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValue, "Original serializeInnerClasses should be true");

    // Call the focal method
    Excluder result = original.disableInnerClassSerialization();

    // The result should not be the same instance as original
    assertNotSame(original, result);

    // The result's serializeInnerClasses field should be false
    boolean resultValue = serializeInnerClassesField.getBoolean(result);
    assertFalse(resultValue, "serializeInnerClasses should be false after disableInnerClassSerialization");

    // The original's serializeInnerClasses field should remain true
    boolean originalValueAfter = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValueAfter, "Original serializeInnerClasses should remain true");

    // Other fields should be equal (version, modifiers, requireExpose, strategies)
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    assertEquals(versionField.getDouble(original), versionField.getDouble(result));

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

  @Test
    @Timeout(8000)
  void disableInnerClassSerialization_cloneThrowsRuntimeException_shouldPropagate() throws Exception {
    // Create a spy of Excluder to mock clone method throwing exception
    Excluder excluderSpy = spy(new Excluder());

    doThrow(new RuntimeException("clone failure")).when(excluderSpy).clone();

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      excluderSpy.disableInnerClassSerialization();
    });
    assertEquals("clone failure", thrown.getMessage());
  }
}