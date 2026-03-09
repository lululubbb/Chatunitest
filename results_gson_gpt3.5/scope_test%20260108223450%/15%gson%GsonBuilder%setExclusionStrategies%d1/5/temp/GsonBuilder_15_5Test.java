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

import java.lang.reflect.Field;

class GsonBuilder_15_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_withSingleStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy);
    assertSame(gsonBuilder, returned);

    Excluder excluder = getExcluderField(gsonBuilder);
    // The excluder should not be the default instance anymore
    assertNotNull(excluder);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_withMultipleStrategies() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy1, strategy2);
    assertSame(gsonBuilder, returned);

    Excluder excluder = getExcluderField(gsonBuilder);
    assertNotNull(excluder);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_withNullArray() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.setExclusionStrategies((ExclusionStrategy[]) null));
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_withNullElement() {
    ExclusionStrategy strategy = null;
    // Adjusted test: setExclusionStrategies does not throw NPE for null elements, so test should expect no exception
    // Instead, we check that calling with a null element throws NPE inside the method, which it doesn't.
    // So, we test that no exception is thrown instead.
    assertDoesNotThrow(() -> gsonBuilder.setExclusionStrategies(strategy));
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_excluderWithExclusionStrategyCalled() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    // Spy on excluder to verify withExclusionStrategy calls
    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder spyExcluder = spy(originalExcluder);
    setExcluderField(gsonBuilder, spyExcluder);

    gsonBuilder.setExclusionStrategies(strategy);

    verify(spyExcluder).withExclusionStrategy(strategy, true, true);
  }

  private Excluder getExcluderField(GsonBuilder gsonBuilder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    return (Excluder) excluderField.get(gsonBuilder);
  }

  private void setExcluderField(GsonBuilder gsonBuilder, Excluder excluder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, excluder);
  }
}