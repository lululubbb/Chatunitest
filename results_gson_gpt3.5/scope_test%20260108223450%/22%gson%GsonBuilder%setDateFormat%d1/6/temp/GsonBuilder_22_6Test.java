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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_22_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_withStyle_setsDateStyleAndNullifiesPattern() throws Exception {
    int style = DateFormat.SHORT;

    GsonBuilder returned = gsonBuilder.setDateFormat(style);

    assertSame(gsonBuilder, returned, "setDateFormat should return the same GsonBuilder instance");
    // Access private fields using reflection
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyleValue = dateStyleField.getInt(gsonBuilder);
    assertEquals(style, dateStyleValue, "dateStyle field should be set to the given style");

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    Object datePatternValue = datePatternField.get(gsonBuilder);
    assertNull(datePatternValue, "datePattern field should be null after calling setDateFormat(int)");
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_reflectsInAddTypeAdaptersForDateInvocation() throws Exception {
    // Use reflection to invoke private method addTypeAdaptersForDate to verify internal consistency
    // First set dateStyle and datePattern fields manually
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);

    // Set dateStyle and datePattern to test values
    int testStyle = DateFormat.MEDIUM;
    String testPattern = "yyyy-MM-dd";
    dateStyleField.setInt(gsonBuilder, testStyle);
    datePatternField.set(gsonBuilder, testPattern);

    // Prepare factories list field to pass to addTypeAdaptersForDate
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.List<TypeAdapterFactory> factories = (java.util.List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

    // Clear factories list to isolate test
    factories.clear();

    Method addTypeAdaptersForDate = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, java.util.List.class);
    addTypeAdaptersForDate.setAccessible(true);

    // Invoke with null pattern and style 0 to cover branch
    addTypeAdaptersForDate.invoke(gsonBuilder, null, 0, 0, factories);
    // After invocation, factories list should have some entries (cannot assert exact content without internal class visibility)
    assertFalse(factories.isEmpty(), "factories list should not be empty after addTypeAdaptersForDate invocation");

    // Invoke with non-null pattern and styles to cover other branch
    factories.clear();
    addTypeAdaptersForDate.invoke(gsonBuilder, testPattern, testStyle, DateFormat.LONG, factories);
    assertFalse(factories.isEmpty(), "factories list should not be empty after addTypeAdaptersForDate invocation with pattern");
  }
}