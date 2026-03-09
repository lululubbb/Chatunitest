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

class Excluder_443_2Test {

  @Test
    @Timeout(8000)
  void withVersion_shouldReturnNewExcluderWithGivenVersion() {
    Excluder original = Excluder.DEFAULT;
    double newVersion = 1.23;

    Excluder result = original.withVersion(newVersion);

    assertNotSame(original, result, "withVersion should return a new Excluder instance");
    assertEquals(newVersion, getVersion(result), 0.00001, "Version should be updated in new Excluder");
    assertEquals(getVersion(original), -1.0d, 0.00001, "Original Excluder version should remain unchanged");
  }

  private double getVersion(Excluder excluder) {
    try {
      java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      return versionField.getDouble(excluder);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}