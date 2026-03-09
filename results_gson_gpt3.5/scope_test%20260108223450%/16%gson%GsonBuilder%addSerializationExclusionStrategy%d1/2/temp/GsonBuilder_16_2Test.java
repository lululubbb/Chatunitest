package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.Gson.DEFAULT_COMPLEX_MAP_KEYS;
import static com.google.gson.Gson.DEFAULT_DATE_PATTERN;
import static com.google.gson.Gson.DEFAULT_ESCAPE_HTML;
import static com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE;
import static com.google.gson.Gson.DEFAULT_LENIENT;
import static com.google.gson.Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_PRETTY_PRINT;
import static com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS;
import static com.google.gson.Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES;
import static com.google.gson.Gson.DEFAULT_USE_JDK_UNSAFE;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_16_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testAddSerializationExclusionStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    // Access excluder field before invocation
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder originalExcluder = (Excluder) excluderField.get(gsonBuilder);

    // Mock Excluder to return a new Excluder instance upon withExclusionStrategy call
    Excluder mockExcluder = mock(Excluder.class);
    when(mockExcluder.withExclusionStrategy(strategy, true, false)).thenReturn(mockExcluder);

    // Replace excluder field with mockExcluder for testing
    excluderField.set(gsonBuilder, mockExcluder);

    // Call focal method
    GsonBuilder returnedBuilder = gsonBuilder.addSerializationExclusionStrategy(strategy);

    // Verify that the method returns the same GsonBuilder instance
    assertSame(gsonBuilder, returnedBuilder);

    // Verify excluder field has been updated to mockExcluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(mockExcluder, updatedExcluder);
  }

  @Test
    @Timeout(8000)
  public void testAddSerializationExclusionStrategy_nullStrategy_throwsNullPointerException() {
    try {
      gsonBuilder.addSerializationExclusionStrategy(null);
    } catch (NullPointerException e) {
      // expected
      return;
    }
    throw new AssertionError("Expected NullPointerException to be thrown");
  }
}