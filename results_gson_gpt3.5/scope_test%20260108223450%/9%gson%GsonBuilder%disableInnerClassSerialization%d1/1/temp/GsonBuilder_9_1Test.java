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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_9_1Test {

  private GsonBuilder gsonBuilder;
  private Excluder mockExcluder;
  private Excluder returnedExcluder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockExcluder = mock(Excluder.class);
    returnedExcluder = mock(Excluder.class);
  }

  @Test
    @Timeout(8000)
  public void testDisableInnerClassSerialization() throws Exception {
    // Use reflection to set private excluder field to mockExcluder
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, mockExcluder);

    // Mock excluder.disableInnerClassSerialization() to return returnedExcluder
    when(mockExcluder.disableInnerClassSerialization()).thenReturn(returnedExcluder);

    // Call the focal method
    GsonBuilder returnedBuilder = gsonBuilder.disableInnerClassSerialization();

    // Assert the method returns the same GsonBuilder instance
    assertSame(gsonBuilder, returnedBuilder);

    // Assert that excluder field is updated to returnedExcluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(returnedExcluder, updatedExcluder);
  }
}