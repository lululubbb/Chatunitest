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

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_3_3Test {

  private GsonBuilder gsonBuilder;
  private Excluder excluderMock;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Create a mock Excluder
    excluderMock = mock(Excluder.class);

    // Use reflection to set the private excluder field to the mock
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, excluderMock);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_withValidVersion_callsExcluderWithVersionAndReturnsThis() {
    double version = 1.5;

    // Mock the behavior of withVersion to return a new Excluder (can be the same mock or a new one)
    Excluder newExcluder = mock(Excluder.class);
    when(excluderMock.withVersion(version)).thenReturn(newExcluder);

    GsonBuilder returned = gsonBuilder.setVersion(version);

    // Verify that withVersion was called with the correct version
    verify(excluderMock).withVersion(version);

    // Verify that the excluder field was updated to newExcluder
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Object excluderValue = excluderField.get(gsonBuilder);
      assertSame(newExcluder, excluderValue);
    } catch (Exception e) {
      fail("Reflection failed to access excluder field");
    }

    // Verify that setVersion returns the same GsonBuilder instance
    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_withZeroVersion() {
    double version = 0.0;

    Excluder newExcluder = mock(Excluder.class);
    when(excluderMock.withVersion(version)).thenReturn(newExcluder);

    GsonBuilder returned = gsonBuilder.setVersion(version);

    verify(excluderMock).withVersion(version);

    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Object excluderValue = excluderField.get(gsonBuilder);
      assertSame(newExcluder, excluderValue);
    } catch (Exception e) {
      fail("Reflection failed to access excluder field");
    }

    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_withNegativeVersion_throwsIllegalArgumentException() {
    double version = -0.1;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.setVersion(version);
    });
    assertEquals("Invalid version: " + version, thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_withNaNVersion_throwsIllegalArgumentException() {
    double version = Double.NaN;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.setVersion(version);
    });
    assertEquals("Invalid version: " + version, thrown.getMessage());
  }
}