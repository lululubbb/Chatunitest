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

class Excluder_445_3Test {

  @Test
    @Timeout(8000)
  void testDisableInnerClassSerialization() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to invoke the private clone() method to verify cloning behavior
    Method cloneMethod = Excluder.class.getDeclaredMethod("clone");
    cloneMethod.setAccessible(true);
    Excluder clonedViaReflection = (Excluder) cloneMethod.invoke(original);

    // Call disableInnerClassSerialization and get the result
    Excluder disabled = original.disableInnerClassSerialization();

    // Verify that disableInnerClassSerialization returns a new instance (clone)
    assertNotSame(original, disabled);

    // Verify that the clone method is called and returns equivalent object
    assertEquals(clonedViaReflection.getClass(), disabled.getClass());

    // Use reflection to get the private field serializeInnerClasses from both objects
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);

    boolean originalValue = (boolean) serializeInnerClassesField.get(original);
    boolean disabledValue = (boolean) serializeInnerClassesField.get(disabled);

    // Original should be true (default)
    assertTrue(originalValue);

    // Disabled should be false after disableInnerClassSerialization
    assertFalse(disabledValue);
  }
}