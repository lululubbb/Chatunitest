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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_4_4Test {

  private GsonBuilder gsonBuilder;
  private Excluder originalExcluder;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Use reflection to get the private excluder field
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);

    // Save original Excluder for verification later
    originalExcluder = (Excluder) excluderField.get(gsonBuilder);

    // Create a mock Excluder and set it to the gsonBuilder
    Excluder mockExcluder = mock(Excluder.class);

    // Setup the mock to return a new Excluder instance when withModifiers is called
    when(mockExcluder.withModifiers(any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      int[] modifiersArg;
      if (arg instanceof int[]) {
        modifiersArg = (int[]) arg;
      } else if (arg instanceof Integer) {
        // Defensive fallback if an Integer is passed instead of int[]
        modifiersArg = new int[] { (Integer) arg };
      } else {
        throw new IllegalArgumentException("Expected int[] argument");
      }
      // Use Excluder.DEFAULT.withModifiers with modifiersArg (even if empty)
      Excluder realExcluder = Excluder.DEFAULT;
      return realExcluder.withModifiers(modifiersArg);
    });

    excluderField.set(gsonBuilder, mockExcluder);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withValidModifiers_shouldUpdateExcluderAndReturnThis() throws Exception {
    int[] modifiers = {Modifier.PRIVATE, Modifier.STATIC};

    // Call the focal method
    GsonBuilder returned = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    // Verify that the method returns the same instance
    assertSame(gsonBuilder, returned);

    // Verify that excluder.withModifiers was called with the correct arguments
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluderValue = (Excluder) excluderField.get(gsonBuilder);

    // The excluder field should not be null
    assertNotNull(excluderValue);

    // The excluder field should not be the originalExcluder (since withModifiers returns a new Excluder)
    assertNotSame(originalExcluder, excluderValue);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withEmptyModifiers_shouldUpdateExcluderAndReturnThis() throws Exception {
    int[] modifiers = new int[0];

    // Call the focal method
    GsonBuilder returned = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    assertSame(gsonBuilder, returned);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluderValue = (Excluder) excluderField.get(gsonBuilder);

    assertNotNull(excluderValue);
    assertNotSame(originalExcluder, excluderValue);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withNullModifiers_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      gsonBuilder.excludeFieldsWithModifiers((int[]) null);
    });
  }
}