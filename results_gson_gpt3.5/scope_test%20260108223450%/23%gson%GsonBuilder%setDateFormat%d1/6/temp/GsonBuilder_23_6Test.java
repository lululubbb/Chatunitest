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

public class GsonBuilder_23_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_SetsDateStyleAndTimeStyleAndNullifiesDatePattern() throws Exception {
    // Arrange
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    // Act
    GsonBuilder returned = gsonBuilder.setDateFormat(dateStyle, timeStyle);

    // Assert fluent API returns same instance
    assertSame(gsonBuilder, returned);

    // Use reflection to verify private fields
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int actualDateStyle = (int) dateStyleField.get(gsonBuilder);
    assertEquals(dateStyle, actualDateStyle);

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int actualTimeStyle = (int) timeStyleField.get(gsonBuilder);
    assertEquals(timeStyle, actualTimeStyle);

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String actualDatePattern = (String) datePatternField.get(gsonBuilder);
    assertNull(actualDatePattern);
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_ThenCreate_AddsDateTypeAdapters() throws Exception {
    // Arrange
    int dateStyle = DateFormat.FULL;
    int timeStyle = DateFormat.LONG;

    // Set date format
    gsonBuilder.setDateFormat(dateStyle, timeStyle);

    // Use reflection to access private method addTypeAdaptersForDate
    Method addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, java.util.List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);

    // Prepare empty factories list to pass
    java.util.List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Act
    addTypeAdaptersForDateMethod.invoke(gsonBuilder, null, dateStyle, timeStyle, factories);

    // Assert that factories list is not empty after adding date adapters
    assertFalse(factories.isEmpty());
  }
}