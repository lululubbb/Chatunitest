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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class GsonBuilder_3_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_validVersion_shouldSetExcluderVersionAndReturnThis() throws Exception {
    double version = 1.5;

    // Mock Excluder and its withVersion method
    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder mockedExcluder = mock(Excluder.class);

    when(mockedExcluder.withVersion(version)).thenReturn(mockedExcluder);

    setExcluderField(gsonBuilder, originalExcluder); // ensure original before mocking

    // Spy on gsonBuilder to replace excluder field with mockedExcluder after withVersion call
    // But since excluder is private final, we use reflection to set it after withVersion call

    // To simulate the withVersion call replacing the excluder field, we do the following:
    // 1. Replace excluder with mockedExcluder before call
    setExcluderField(gsonBuilder, mockedExcluder);

    GsonBuilder returned = gsonBuilder.setVersion(version);

    // Verify withVersion was called on mockedExcluder with correct argument
    verify(mockedExcluder).withVersion(version);

    // The excluder field in gsonBuilder should be set to mockedExcluder (simulate withVersion returning mockedExcluder)
    Excluder excluderAfter = getExcluderField(gsonBuilder);
    assertSame(mockedExcluder, excluderAfter);

    // The method should return the same GsonBuilder instance
    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_zeroVersion_shouldSetExcluderVersionAndReturnThis() throws Exception {
    double version = 0.0;

    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder mockedExcluder = mock(Excluder.class);
    when(mockedExcluder.withVersion(version)).thenReturn(mockedExcluder);
    setExcluderField(gsonBuilder, mockedExcluder);

    GsonBuilder returned = gsonBuilder.setVersion(version);

    verify(mockedExcluder).withVersion(version);
    assertSame(mockedExcluder, getExcluderField(gsonBuilder));
    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_invalidVersion_negative_shouldThrowIllegalArgumentException() {
    double invalidVersion = -0.1;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.setVersion(invalidVersion);
    });

    assertEquals("Invalid version: " + invalidVersion, thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testSetVersion_invalidVersion_NaN_shouldThrowIllegalArgumentException() {
    double invalidVersion = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.setVersion(invalidVersion);
    });

    assertEquals("Invalid version: " + invalidVersion, thrown.getMessage());
  }

  private Excluder getExcluderField(GsonBuilder builder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    return (Excluder) excluderField.get(builder);
  }

  private void setExcluderField(GsonBuilder builder, Excluder excluder) {
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      excluderField.set(builder, excluder);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}