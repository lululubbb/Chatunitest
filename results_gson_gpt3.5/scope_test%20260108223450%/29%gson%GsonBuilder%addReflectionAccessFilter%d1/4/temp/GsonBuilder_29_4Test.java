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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GsonBuilder_29_4Test {

  @Mock
  private ReflectionAccessFilter mockFilter;

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void addReflectionAccessFilter_NullFilter_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.addReflectionAccessFilter(null));
  }

  @Test
    @Timeout(8000)
  void addReflectionAccessFilter_ValidFilter_AddsFilterAndReturnsThis() throws Exception {
    GsonBuilder returned = gsonBuilder.addReflectionAccessFilter(mockFilter);
    assertSame(gsonBuilder, returned);

    // Use reflection to access private field 'reflectionFilters'
    Field filtersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    filtersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedList<ReflectionAccessFilter> filters = (LinkedList<ReflectionAccessFilter>) filtersField.get(gsonBuilder);

    assertNotNull(filters);
    assertFalse(filters.isEmpty());
    assertSame(mockFilter, filters.getFirst());
  }
}