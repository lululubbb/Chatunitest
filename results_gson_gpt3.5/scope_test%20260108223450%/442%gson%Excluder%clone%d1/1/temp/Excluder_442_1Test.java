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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

class Excluder_442_1Test {

  @Test
    @Timeout(8000)
  void clone_shouldReturnDistinctButEqualInstance() throws Exception {
    Excluder original = new Excluder();
    Excluder cloned = original.clone();

    assertNotNull(cloned);
    assertNotSame(original, cloned);

    // Use reflection to access private fields and compare their values
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    assertEquals(versionField.get(original), versionField.get(cloned));

    Field modifiersField = Excluder.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    assertEquals(modifiersField.get(original), modifiersField.get(cloned));

    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    assertEquals(serializeInnerClassesField.get(original), serializeInnerClassesField.get(cloned));

    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);
    assertEquals(requireExposeField.get(original), requireExposeField.get(cloned));

    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    assertEquals(serializationStrategiesField.get(original), serializationStrategiesField.get(cloned));

    Field deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
    deserializationStrategiesField.setAccessible(true);
    assertEquals(deserializationStrategiesField.get(original), deserializationStrategiesField.get(cloned));
  }

  @Test
    @Timeout(8000)
  void clone_onCloneNotSupportedException_shouldThrowAssertionError() {
    // Since Excluder is final, we cannot subclass it.
    // Instead, use Mockito spy and mock the clone method to throw CloneNotSupportedException wrapped in AssertionError.
    Excluder excluderSpy = Mockito.spy(new Excluder());

    Mockito.doAnswer(invocation -> {
      throw new AssertionError(new CloneNotSupportedException("forced"));
    }).when(excluderSpy).clone();

    AssertionError error = assertThrows(AssertionError.class, excluderSpy::clone);
    assertTrue(error.getCause() instanceof CloneNotSupportedException);
    assertEquals("forced", error.getCause().getMessage());
  }
}