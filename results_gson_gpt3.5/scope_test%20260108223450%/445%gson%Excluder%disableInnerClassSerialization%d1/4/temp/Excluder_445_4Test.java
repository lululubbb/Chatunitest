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

public class Excluder_445_4Test {

  @Test
    @Timeout(8000)
  public void testDisableInnerClassSerialization() throws Exception {
    Excluder excluder = new Excluder();

    // Verify initial serializeInnerClasses is true using reflection
    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    assertTrue((Boolean) serializeInnerClassesField.get(excluder));

    // Invoke disableInnerClassSerialization
    Excluder result = excluder.disableInnerClassSerialization();

    // Check that result is not the same instance (clone)
    assertNotSame(excluder, result);

    // Check that result.serializeInnerClasses is false
    assertFalse((Boolean) serializeInnerClassesField.get(result));

    // Check that original excluder serializeInnerClasses remains true
    assertTrue((Boolean) serializeInnerClassesField.get(excluder));

    // Verify clone() method was called by reflection (optional)
    Method cloneMethod = Excluder.class.getDeclaredMethod("clone");
    cloneMethod.setAccessible(true);
    Excluder cloned = (Excluder) cloneMethod.invoke(excluder);
    assertNotNull(cloned);
    assertNotSame(excluder, cloned);
  }
}