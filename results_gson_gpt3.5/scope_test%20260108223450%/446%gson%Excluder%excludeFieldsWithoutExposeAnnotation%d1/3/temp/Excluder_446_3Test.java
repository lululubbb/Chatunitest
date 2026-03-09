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
import org.junit.jupiter.api.Test;
import com.google.gson.internal.Excluder;

class Excluder_446_3Test {

  @Test
    @Timeout(8000)
  void testExcludeFieldsWithoutExposeAnnotation() {
    Excluder original = new Excluder();
    assertFalse(getRequireExpose(original));
    Excluder result = original.excludeFieldsWithoutExposeAnnotation();
    assertNotNull(result);
    assertNotSame(original, result);
    assertTrue(getRequireExpose(result));
    assertFalse(getRequireExpose(original));
  }

  private boolean getRequireExpose(Excluder excluder) {
    try {
      java.lang.reflect.Field field = Excluder.class.getDeclaredField("requireExpose");
      field.setAccessible(true);
      return field.getBoolean(excluder);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}