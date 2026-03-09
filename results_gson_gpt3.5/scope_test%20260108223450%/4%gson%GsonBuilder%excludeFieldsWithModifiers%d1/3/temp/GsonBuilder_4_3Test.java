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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class GsonBuilder_4_3Test {

  private GsonBuilder gsonBuilder;
  private Excluder mockExcluder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockExcluder = mock(Excluder.class);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withValidModifiers_shouldUpdateExcluderAndReturnThis() throws Exception {
    int[] modifiers = {Modifier.PRIVATE, Modifier.PROTECTED};

    try (MockedStatic<Objects> objectsMockedStatic = Mockito.mockStatic(Objects.class)) {
      objectsMockedStatic.when(() -> Objects.requireNonNull(modifiers)).thenCallRealMethod();

      // Inject mock Excluder into gsonBuilder via reflection
      Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      excluderField.set(gsonBuilder, mockExcluder);

      Excluder returnedExcluder = mock(Excluder.class);
      when(mockExcluder.withModifiers(modifiers)).thenReturn(returnedExcluder);

      GsonBuilder result = gsonBuilder.excludeFieldsWithModifiers(modifiers);

      // Verify requireNonNull called
      objectsMockedStatic.verify(() -> Objects.requireNonNull(modifiers));

      // Verify withModifiers called on mock excluder
      verify(mockExcluder).withModifiers(modifiers);

      // Verify the excluder field updated to returnedExcluder
      Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
      assertSame(returnedExcluder, updatedExcluder);

      // Verify method returns this
      assertSame(gsonBuilder, result);
    }
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withNullModifiers_shouldThrowNullPointerException() {
    int[] modifiers = null;
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> gsonBuilder.excludeFieldsWithModifiers(modifiers));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void testExcludeFieldsWithModifiers_withEmptyModifiers_shouldUpdateExcluderAndReturnThis() throws Exception {
    int[] modifiers = new int[0];

    // Inject mock Excluder into gsonBuilder via reflection
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, mockExcluder);

    Excluder returnedExcluder = mock(Excluder.class);
    when(mockExcluder.withModifiers(modifiers)).thenReturn(returnedExcluder);

    GsonBuilder result = gsonBuilder.excludeFieldsWithModifiers(modifiers);

    // Verify withModifiers called on mock excluder
    verify(mockExcluder).withModifiers(modifiers);

    // Verify the excluder field updated to returnedExcluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(returnedExcluder, updatedExcluder);

    // Verify method returns this
    assertSame(gsonBuilder, result);
  }
}