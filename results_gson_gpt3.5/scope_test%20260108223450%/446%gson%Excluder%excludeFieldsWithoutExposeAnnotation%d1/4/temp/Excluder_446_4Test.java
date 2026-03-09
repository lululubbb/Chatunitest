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

public class Excluder_446_4Test {

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithoutExposeAnnotation_returnsCloneWithRequireExposeTrue() throws Exception {
    Excluder original = new Excluder();

    Excluder result = original.excludeFieldsWithoutExposeAnnotation();

    assertNotNull(result);
    assertNotSame(original, result);

    // Use reflection to check private field requireExpose
    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);

    boolean originalRequireExpose = requireExposeField.getBoolean(original);
    boolean resultRequireExpose = requireExposeField.getBoolean(result);

    assertFalse(originalRequireExpose, "Original Excluder requireExpose should be false");
    assertTrue(resultRequireExpose, "Result Excluder requireExpose should be true");
  }

  @Test
    @Timeout(8000)
  public void testCloneMethodCreatesDistinctObject() throws Exception {
    Excluder original = new Excluder();

    Method cloneMethod = Excluder.class.getDeclaredMethod("clone");
    cloneMethod.setAccessible(true);
    Excluder clone = (Excluder) cloneMethod.invoke(original);

    assertNotNull(clone);
    assertNotSame(original, clone);

    // Check that the clone has same initial requireExpose value as original (both false)
    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);
    assertEquals(requireExposeField.getBoolean(original), requireExposeField.getBoolean(clone));
  }
}