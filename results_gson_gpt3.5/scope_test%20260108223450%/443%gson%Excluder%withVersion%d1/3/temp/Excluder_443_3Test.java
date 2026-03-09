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

class Excluder_443_3Test {

  @Test
    @Timeout(8000)
  void testWithVersion() {
    Excluder original = new Excluder();
    double newVersion = 1.23d;

    Excluder result = original.withVersion(newVersion);

    assertNotSame(original, result, "withVersion should return a new Excluder instance");
    assertEquals(newVersion, getVersion(result), 0.0, "Version should be set to new value");
    assertEquals(getVersion(original), -1.0d, 0.0, "Original Excluder version should remain unchanged");
  }

  private double getVersion(Excluder excluder) {
    try {
      java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      return versionField.getDouble(excluder);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}