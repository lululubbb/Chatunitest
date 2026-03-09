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

class GsonBuilder_23_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testSetDateFormat_SetsDateStyleAndTimeStyleAndClearsDatePattern() throws Exception {
    // Given initial values different from defaults
    setPrivateField(gsonBuilder, "dateStyle", DateFormat.SHORT);
    setPrivateField(gsonBuilder, "timeStyle", DateFormat.SHORT);
    setPrivateField(gsonBuilder, "datePattern", "yyyy-MM-dd");

    // When
    GsonBuilder returned = gsonBuilder.setDateFormat(DateFormat.MEDIUM, DateFormat.LONG);

    // Then
    assertSame(gsonBuilder, returned, "setDateFormat should return this");
    assertEquals(Integer.valueOf(DateFormat.MEDIUM), getPrivateField(gsonBuilder, "dateStyle"));
    assertEquals(Integer.valueOf(DateFormat.LONG), getPrivateField(gsonBuilder, "timeStyle"));
    assertNull(getPrivateField(gsonBuilder, "datePattern"));
  }

  // Helper to set private fields via reflection
  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = GsonBuilder.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object target, String fieldName) throws Exception {
    Field field = GsonBuilder.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}