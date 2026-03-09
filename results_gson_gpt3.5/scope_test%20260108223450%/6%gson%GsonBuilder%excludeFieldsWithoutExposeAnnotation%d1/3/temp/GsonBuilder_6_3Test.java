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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_6_3Test {

  private GsonBuilder gsonBuilder;
  private Excluder excluderMock;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Use reflection to set the private excluder field to a spy
    excluderMock = spy(Excluder.DEFAULT);
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, excluderMock);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithoutExposeAnnotation() throws Exception {
    // Mock the behavior of excluder.excludeFieldsWithoutExposeAnnotation() to return a specific Excluder instance
    Excluder newExcluder = mock(Excluder.class);
    when(excluderMock.excludeFieldsWithoutExposeAnnotation()).thenReturn(newExcluder);

    // Call the method under test once and capture the result
    GsonBuilder returnedBuilder = gsonBuilder.excludeFieldsWithoutExposeAnnotation();

    // Verify that excluder.excludeFieldsWithoutExposeAnnotation() was called exactly once
    verify(excluderMock, times(1)).excludeFieldsWithoutExposeAnnotation();

    // Verify that the excluder field was updated to the new Excluder instance
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Object excluderFieldValue = excluderField.get(gsonBuilder);
    assertSame(newExcluder, excluderFieldValue);

    // Verify that the method returns the same GsonBuilder instance (this)
    assertSame(gsonBuilder, returnedBuilder);
  }
}