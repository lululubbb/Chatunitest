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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class GsonBuilder_3_6Test {

  private GsonBuilder gsonBuilder;
  private Excluder mockExcluder;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();
    mockExcluder = mock(Excluder.class);
    // Use reflection to set the private excluder field to the mock
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, mockExcluder);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_validVersion_callsWithVersionAndReturnsThis() {
    double version = 1.5;
    Excluder returnedExcluder = mock(Excluder.class);
    when(mockExcluder.withVersion(version)).thenReturn(returnedExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.setVersion(version);

    assertSame(gsonBuilder, returnedBuilder);
    // Verify the excluder field is updated to returnedExcluder
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder currentExcluder = (Excluder) excluderField.get(gsonBuilder);
      assertSame(returnedExcluder, currentExcluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
    verify(mockExcluder).withVersion(version);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_zeroVersion() {
    double version = 0.0;
    Excluder returnedExcluder = mock(Excluder.class);
    when(mockExcluder.withVersion(version)).thenReturn(returnedExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.setVersion(version);

    assertSame(gsonBuilder, returnedBuilder);
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder currentExcluder = (Excluder) excluderField.get(gsonBuilder);
      assertSame(returnedExcluder, currentExcluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
    verify(mockExcluder).withVersion(version);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_invalidVersion_negative_throwsException() {
    double invalidVersion = -0.1;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> gsonBuilder.setVersion(invalidVersion));
    assertEquals("Invalid version: " + invalidVersion, thrown.getMessage());
    verify(mockExcluder, never()).withVersion(anyDouble());
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_invalidVersion_NaN_throwsException() {
    double invalidVersion = Double.NaN;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> gsonBuilder.setVersion(invalidVersion));
    assertEquals("Invalid version: " + invalidVersion, thrown.getMessage());
    verify(mockExcluder, never()).withVersion(anyDouble());
  }

}