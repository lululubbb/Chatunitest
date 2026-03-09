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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_4_1Test {

  private GsonBuilder gsonBuilder;
  private Excluder mockExcluder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockExcluder = mock(Excluder.class);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_nullModifiers_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.excludeFieldsWithModifiers((int[]) null);
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_modifiersApplied() throws Exception {
    // Arrange
    int[] modifiers = {Modifier.TRANSIENT, Modifier.STATIC};
    Excluder excluderBefore = getPrivateExcluder(gsonBuilder);

    Excluder excluderAfter = mock(Excluder.class);
    when(excluderAfter.withModifiers(modifiers)).thenReturn(excluderAfter);

    setPrivateExcluder(gsonBuilder, mockExcluder);
    when(mockExcluder.withModifiers(modifiers)).thenReturn(excluderAfter);

    // Act
    GsonBuilder returned = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    // Assert
    assertSame(gsonBuilder, returned, "Method should return the same GsonBuilder instance");
    Excluder excluderAfterCall = getPrivateExcluder(gsonBuilder);
    assertSame(excluderAfter, excluderAfterCall, "Excluder field should be updated with the new Excluder returned by withModifiers");
    verify(mockExcluder).withModifiers(modifiers);
  }

  private Excluder getPrivateExcluder(GsonBuilder builder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    return (Excluder) excluderField.get(builder);
  }

  private void setPrivateExcluder(GsonBuilder builder, Excluder excluder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(builder, excluder);
  }
}