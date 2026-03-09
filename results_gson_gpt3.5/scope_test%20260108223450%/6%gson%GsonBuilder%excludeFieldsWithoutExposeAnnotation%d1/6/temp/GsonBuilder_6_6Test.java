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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class GsonBuilder_6_6Test {

  private GsonBuilder gsonBuilder;
  private Excluder excluderSpy;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();
    // Use reflection to replace the private final excluder field with a spy
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder originalExcluder = (Excluder) excluderField.get(gsonBuilder);
    excluderSpy = spy(originalExcluder);
    excluderField.set(gsonBuilder, excluderSpy);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithoutExposeAnnotation_returnsThisAndUpdatesExcluder() throws Exception {
    Excluder newExcluder = mock(Excluder.class);
    // when excludeFieldsWithoutExposeAnnotation is called on excluderSpy, return newExcluder
    when(excluderSpy.excludeFieldsWithoutExposeAnnotation()).thenReturn(newExcluder);

    // Call method under test
    GsonBuilder returned = gsonBuilder.excludeFieldsWithoutExposeAnnotation();

    // Verify excluder was updated to the new one returned by excludeFieldsWithoutExposeAnnotation()
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(newExcluder, updatedExcluder);

    // Verify the method returns the same GsonBuilder instance
    assertSame(gsonBuilder, returned);

    // Verify that excludeFieldsWithoutExposeAnnotation was called exactly once on the excluder spy
    verify(excluderSpy, times(1)).excludeFieldsWithoutExposeAnnotation();

    // Verify that excludeFieldsWithoutExposeAnnotation was NOT called on newExcluder spy (to avoid extra calls)
    verify(newExcluder, times(0)).excludeFieldsWithoutExposeAnnotation();
  }
}