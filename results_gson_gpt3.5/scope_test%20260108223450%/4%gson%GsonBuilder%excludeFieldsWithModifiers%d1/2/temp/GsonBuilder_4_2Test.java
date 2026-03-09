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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GsonBuilder_4_2Test {

  private GsonBuilder gsonBuilder;
  private Excluder mockExcluder;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Use reflection to replace the private final excluder field with a mock
    mockExcluder = Mockito.mock(Excluder.class);
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, mockExcluder);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_NullModifiers_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      gsonBuilder.excludeFieldsWithModifiers((int[]) null);
    });
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_EmptyModifiers_ReturnsSameBuilder() throws Exception {
    // Setup mock to return itself when withModifiers is called with empty array
    when(mockExcluder.withModifiers()).thenReturn(mockExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.excludeFieldsWithModifiers();

    assertSame(gsonBuilder, returnedBuilder);

    verify(mockExcluder, times(1)).withModifiers();
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_SingleModifier() throws Exception {
    int modifier = Modifier.PRIVATE;

    when(mockExcluder.withModifiers(modifier)).thenReturn(mockExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.excludeFieldsWithModifiers(modifier);

    assertSame(gsonBuilder, returnedBuilder);

    verify(mockExcluder, times(1)).withModifiers(modifier);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_MultipleModifiers() throws Exception {
    int[] modifiers = new int[]{Modifier.PUBLIC, Modifier.STATIC, Modifier.TRANSIENT};

    when(mockExcluder.withModifiers(modifiers)).thenReturn(mockExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    assertSame(gsonBuilder, returnedBuilder);

    verify(mockExcluder, times(1)).withModifiers(modifiers);
  }
}