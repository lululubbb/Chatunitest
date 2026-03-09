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

class Excluder_443_4Test {

  @Test
    @Timeout(8000)
  void testWithVersionCreatesNewExcluderWithGivenVersion() {
    Excluder original = new Excluder();
    double version = 1.23;

    Excluder result = original.withVersion(version);

    assertNotNull(result);
    assertNotSame(original, result);

    // Use reflection to get the private field 'version'
    double originalVersion = getVersion(original);
    double resultVersion = getVersion(result);

    assertEquals(-1.0d, originalVersion, 0.0d, "Original Excluder version must be default IGNORE_VERSIONS");
    assertEquals(version, resultVersion, 0.0d, "Result Excluder version must be set to given version");
  }

  private double getVersion(Excluder excluder) {
    try {
      java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      return versionField.getDouble(excluder);
    } catch (ReflectiveOperationException e) {
      fail("Reflection failed to access version field: " + e.getMessage());
      return Double.NaN; // unreachable
    }
  }
}