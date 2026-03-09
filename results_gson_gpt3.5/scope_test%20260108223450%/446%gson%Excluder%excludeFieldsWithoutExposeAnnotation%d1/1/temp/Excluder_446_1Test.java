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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class Excluder_446_1Test {

  @Test
    @Timeout(8000)
  void excludeFieldsWithoutExposeAnnotation_shouldReturnCloneWithRequireExposeTrue() {
    Excluder original = new Excluder();
    Excluder clone = original.excludeFieldsWithoutExposeAnnotation();

    assertNotNull(clone);
    assertNotSame(original, clone);

    // Access private field 'requireExpose' via reflection
    boolean originalRequireExpose = getRequireExpose(original);
    boolean cloneRequireExpose = getRequireExpose(clone);

    assertFalse(originalRequireExpose, "Original requireExpose should be false");
    assertTrue(cloneRequireExpose, "Clone requireExpose should be true");
  }

  private boolean getRequireExpose(Excluder excluder) {
    try {
      var field = Excluder.class.getDeclaredField("requireExpose");
      field.setAccessible(true);
      return field.getBoolean(excluder);
    } catch (ReflectiveOperationException e) {
      fail("Failed to get requireExpose field value via reflection");
      return false;
    }
  }
}