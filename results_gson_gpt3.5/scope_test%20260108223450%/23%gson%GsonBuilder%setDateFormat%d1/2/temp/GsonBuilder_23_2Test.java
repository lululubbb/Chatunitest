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
import java.text.DateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonBuilder_23_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testSetDateFormat_setsDateStyleAndTimeStyleAndClearsDatePattern() throws Exception {
    // Arrange initial values
    // Use reflection to set initial datePattern to non-null to verify it is cleared
    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    datePatternField.set(gsonBuilder, "somePattern");

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    dateStyleField.set(gsonBuilder, DateFormat.SHORT);

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    timeStyleField.set(gsonBuilder, DateFormat.MEDIUM);

    int newDateStyle = DateFormat.LONG;
    int newTimeStyle = DateFormat.FULL;

    // Act
    GsonBuilder returned = gsonBuilder.setDateFormat(newDateStyle, newTimeStyle);

    // Assert returned instance is same (for chaining)
    assertSame(gsonBuilder, returned);

    // Assert dateStyle is set correctly
    int actualDateStyle = (int) dateStyleField.get(gsonBuilder);
    assertEquals(newDateStyle, actualDateStyle);

    // Assert timeStyle is set correctly
    int actualTimeStyle = (int) timeStyleField.get(gsonBuilder);
    assertEquals(newTimeStyle, actualTimeStyle);

    // Assert datePattern is cleared (set to null)
    String actualDatePattern = (String) datePatternField.get(gsonBuilder);
    assertNull(actualDatePattern);
  }
}