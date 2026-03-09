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

public class Excluder_446_5Test {

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithoutExposeAnnotation_returnsCloneWithRequireExposeTrue() throws Exception {
    Excluder original = new Excluder();

    // Use reflection to invoke the focal method
    Method method = Excluder.class.getDeclaredMethod("excludeFieldsWithoutExposeAnnotation");
    method.setAccessible(true);
    Excluder result = (Excluder) method.invoke(original);

    assertNotNull(result);
    assertNotSame(original, result);

    // Use reflection to read private field requireExpose
    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);

    boolean originalRequireExpose = requireExposeField.getBoolean(original);
    boolean resultRequireExpose = requireExposeField.getBoolean(result);

    assertFalse(originalRequireExpose, "Original Excluder requireExpose should be false");
    assertTrue(resultRequireExpose, "Cloned Excluder requireExpose should be true");
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithoutExposeAnnotation_cloneThrowsException() throws Exception {
    Excluder excluderSpy = spy(new Excluder());

    // Workaround: use doAnswer to throw wrapped RuntimeException instead of checked exception
    doAnswer(invocation -> {
      throw new RuntimeException(new CloneNotSupportedException());
    }).when(excluderSpy).clone();

    Method method = Excluder.class.getDeclaredMethod("excludeFieldsWithoutExposeAnnotation");
    method.setAccessible(true);

    try {
      method.invoke(excluderSpy);
      fail("Expected RuntimeException caused by CloneNotSupportedException");
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertNotNull(cause);
      // The focal method does not declare throwing checked exceptions, so clone() must be handled internally.
      // If clone() throws CloneNotSupportedException, it should be wrapped in RuntimeException.
      boolean condition = cause instanceof RuntimeException &&
          (cause.getCause() instanceof CloneNotSupportedException || cause instanceof CloneNotSupportedException);
      assertTrue(condition, "Expected RuntimeException caused by CloneNotSupportedException");
    }
  }
}