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

class Excluder_443_6Test {

  @Test
    @Timeout(8000)
  void withVersion_shouldReturnNewExcluderWithGivenVersion() {
    Excluder original = new Excluder();

    double newVersion = 1.5;
    Excluder result = original.withVersion(newVersion);

    assertNotNull(result);
    assertNotSame(original, result);

    // Use reflection to verify private field 'version' in result
    try {
      var versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      double versionValue = (double) versionField.get(result);
      assertEquals(newVersion, versionValue, 0.0);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error accessing version field: " + e.getMessage());
    }

    // Also verify original's version remains unchanged (IGNORE_VERSIONS = -1.0)
    try {
      var versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      double originalVersion = (double) versionField.get(original);
      assertEquals(-1.0d, originalVersion, 0.0);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error accessing version field: " + e.getMessage());
    }
  }
}