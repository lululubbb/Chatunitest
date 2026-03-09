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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Excluder_443_5Test {

  @Test
    @Timeout(8000)
  public void testWithVersion() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to get the 'version' field before calling withVersion
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    double originalVersion = versionField.getDouble(original);
    assertEquals(-1.0d, originalVersion);

    double newVersionValue = 1.5d;
    Excluder cloned = original.withVersion(newVersionValue);

    // Verify the returned object is not the same instance
    assertNotSame(original, cloned);

    // Verify the version field is set correctly on the cloned instance
    double clonedVersion = versionField.getDouble(cloned);
    assertEquals(newVersionValue, clonedVersion);

    // Verify the original instance's version field remains unchanged
    double originalVersionAfter = versionField.getDouble(original);
    assertEquals(originalVersion, originalVersionAfter);

    // Verify other fields are copied (modifiers, serializeInnerClasses, requireExpose)
    Field modifiersField = Excluder.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    assertEquals(modifiersField.getInt(original), modifiersField.getInt(cloned));

    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    assertEquals(serializeInnerClassesField.getBoolean(original), serializeInnerClassesField.getBoolean(cloned));

    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);
    assertEquals(requireExposeField.getBoolean(original), requireExposeField.getBoolean(cloned));

    // Verify serializationStrategies and deserializationStrategies are copied (same references)
    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    assertSame(serializationStrategiesField.get(original), serializationStrategiesField.get(cloned));

    Field deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
    deserializationStrategiesField.setAccessible(true);
    assertSame(deserializationStrategiesField.get(original), deserializationStrategiesField.get(cloned));
  }

  @Test
    @Timeout(8000)
  public void testCloneMethodCreatesDistinctObject() throws Exception {
    Excluder original = new Excluder();

    Method cloneMethod = Excluder.class.getDeclaredMethod("clone");
    cloneMethod.setAccessible(true);
    Excluder cloned = (Excluder) cloneMethod.invoke(original);

    assertNotSame(original, cloned);

    // Verify the version field is the same in clone
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    assertEquals(versionField.getDouble(original), versionField.getDouble(cloned));
  }
}