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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_4_6Test {

  private GsonBuilder gsonBuilder;
  private Excluder excluderMock;

  @BeforeEach
  public void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Use reflection to replace the private final Excluder field with a mock
    excluderMock = mock(Excluder.class);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, excluderMock);
  }

  @Test
    @Timeout(8000)
  public void excludeFieldsWithModifiers_nullModifiers_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.excludeFieldsWithModifiers((int[]) null);
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void excludeFieldsWithModifiers_validModifiers_callsExcluderWithModifiersAndReturnsThis() {
    int[] modifiers = {Modifier.TRANSIENT, Modifier.STATIC};
    Excluder returnedExcluder = mock(Excluder.class);

    // When excluder.withModifiers is called with the modifiers, return returnedExcluder
    when(excluderMock.withModifiers(modifiers)).thenReturn(returnedExcluder);

    GsonBuilder returnedBuilder = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    // Verify that excluder.withModifiers was called once with the correct argument
    verify(excluderMock, times(1)).withModifiers(modifiers);

    // Verify that the excluder field was updated to returnedExcluder
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder currentExcluder = (Excluder) excluderField.get(gsonBuilder);
      assertSame(returnedExcluder, currentExcluder);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }

    // The method should return the same GsonBuilder instance
    assertSame(gsonBuilder, returnedBuilder);
  }
}