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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class Excluder_445_5Test {

  @Test
    @Timeout(8000)
  void disableInnerClassSerialization_shouldReturnCloneWithSerializeInnerClassesFalse() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to invoke private clone() method to verify clone behavior
    Method cloneMethod = Excluder.class.getDeclaredMethod("clone");
    cloneMethod.setAccessible(true);
    Excluder clonedViaReflection = (Excluder) cloneMethod.invoke(original);
    assertNotNull(clonedViaReflection);
    // By default, serializeInnerClasses is true
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    assertTrue(serializeInnerClassesField.getBoolean(original));
    assertTrue(serializeInnerClassesField.getBoolean(clonedViaReflection));

    // Call the focal method
    Excluder result = original.disableInnerClassSerialization();

    // Check that result is a different instance (clone)
    assertNotSame(original, result);

    // Check that serializeInnerClasses is false in the result
    assertFalse(serializeInnerClassesField.getBoolean(result));

    // Original's serializeInnerClasses remains true
    assertTrue(serializeInnerClassesField.getBoolean(original));
  }
}