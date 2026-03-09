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

public class Excluder_443_1Test {

  @Test
    @Timeout(8000)
  public void testWithVersion() {
    Excluder original = new Excluder();
    double versionValue = 1.5;

    Excluder result = original.withVersion(versionValue);

    assertNotNull(result);
    assertNotSame(original, result);

    // Use reflection to verify private field 'version'
    double versionInResult = getVersionField(result);
    assertEquals(versionValue, versionInResult);

    // Verify original instance version remains unchanged (default -1.0)
    double versionInOriginal = getVersionField(original);
    assertEquals(-1.0d, versionInOriginal);
  }

  private double getVersionField(Excluder excluder) {
    try {
      Field versionField = Excluder.class.getDeclaredField("version");
      versionField.setAccessible(true);
      return versionField.getDouble(excluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}