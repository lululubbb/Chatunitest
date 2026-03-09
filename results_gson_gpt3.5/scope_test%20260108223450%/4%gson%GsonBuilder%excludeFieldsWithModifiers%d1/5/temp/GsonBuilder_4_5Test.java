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

class GsonBuilder_4_5Test {

  private GsonBuilder gsonBuilder;
  private Excluder originalExcluder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
    // Save original excluder for later verification
    originalExcluder = getExcluder(gsonBuilder);
  }

  @Test
    @Timeout(8000)
  void excludeFieldsWithModifiers_nullModifiers_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.excludeFieldsWithModifiers((int[]) null);
    });
    assertNull(thrown.getMessage(), "Expected NullPointerException with null message");
  }

  @Test
    @Timeout(8000)
  void excludeFieldsWithModifiers_validModifiers_updatesExcluderAndReturnsThis() {
    // Prepare a spy of the original Excluder to verify interaction
    Excluder spyExcluder = spy(originalExcluder);
    setExcluder(gsonBuilder, spyExcluder);

    int[] modifiers = {Modifier.TRANSIENT, Modifier.STATIC};
    GsonBuilder returned = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    // Verify withModifiers called with correct arguments
    verify(spyExcluder).withModifiers(modifiers);

    // Verify the excluder field is updated with the returned Excluder from withModifiers
    Excluder updatedExcluder = getExcluder(gsonBuilder);
    assertNotNull(updatedExcluder);
    assertNotSame(spyExcluder, updatedExcluder, "Excluder should be updated");

    // Verify method returns the same GsonBuilder instance
    assertSame(gsonBuilder, returned, "Method should return this");
  }

  // Helper method to get private excluder field via reflection
  private Excluder getExcluder(GsonBuilder builder) {
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      return (Excluder) excluderField.get(builder);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  // Helper method to set private excluder field via reflection
  private void setExcluder(GsonBuilder builder, Excluder excluder) {
    try {
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      excluderField.set(builder, excluder);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}