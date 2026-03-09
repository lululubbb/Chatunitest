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

public class Excluder_445_6Test {

  @Test
    @Timeout(8000)
  public void testDisableInnerClassSerialization_returnsCloneWithSerializeInnerClassesFalse() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to get the original serializeInnerClasses field value
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    boolean originalValue = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValue, "Precondition: original serializeInnerClasses must be true");

    Excluder result = original.disableInnerClassSerialization();

    // The result should not be the same instance
    assertNotSame(original, result);

    // The result's serializeInnerClasses field should be false
    boolean resultValue = serializeInnerClassesField.getBoolean(result);
    assertFalse(resultValue);

    // The original's serializeInnerClasses field should remain unchanged
    boolean originalValueAfter = serializeInnerClassesField.getBoolean(original);
    assertTrue(originalValueAfter);
  }
}