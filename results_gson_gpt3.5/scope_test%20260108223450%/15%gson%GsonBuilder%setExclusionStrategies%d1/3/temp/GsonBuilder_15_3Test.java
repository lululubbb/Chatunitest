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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class GsonBuilder_15_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void setExclusionStrategies_nullArray_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.setExclusionStrategies((ExclusionStrategy[]) null));
  }

  @Test
    @Timeout(8000)
  void setExclusionStrategies_emptyArray_returnsSameInstanceAndDoesNotChangeExcluder() throws Exception {
    Excluder originalExcluder = getExcluder(gsonBuilder);
    GsonBuilder returned = gsonBuilder.setExclusionStrategies();
    Excluder afterExcluder = getExcluder(gsonBuilder);

    assertSame(gsonBuilder, returned);
    assertSame(originalExcluder, afterExcluder);
  }

  @Test
    @Timeout(8000)
  void setExclusionStrategies_singleStrategy_callsWithExclusionStrategyOnce() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    // Spy on Excluder to verify withExclusionStrategy is called
    Excluder originalExcluder = getExcluder(gsonBuilder);
    Excluder spyExcluder = spy(originalExcluder);
    setExcluder(gsonBuilder, spyExcluder);

    // To handle chaining, stub withExclusionStrategy to return spyExcluder itself
    when(spyExcluder.withExclusionStrategy(eq(strategy), eq(true), eq(true))).thenReturn(spyExcluder);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy);

    verify(spyExcluder, times(1)).withExclusionStrategy(strategy, true, true);
    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  void setExclusionStrategies_multipleStrategies_callsWithExclusionStrategyMultipleTimes() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);

    Excluder originalExcluder = getExcluder(gsonBuilder);

    Excluder spyExcluder = spy(originalExcluder);
    setExcluder(gsonBuilder, spyExcluder);

    // Stub withExclusionStrategy to return spyExcluder for both strategies to handle chaining
    when(spyExcluder.withExclusionStrategy(eq(strategy1), eq(true), eq(true))).thenReturn(spyExcluder);
    when(spyExcluder.withExclusionStrategy(eq(strategy2), eq(true), eq(true))).thenReturn(spyExcluder);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy1, strategy2);

    verify(spyExcluder, times(1)).withExclusionStrategy(strategy1, true, true);
    verify(spyExcluder, times(1)).withExclusionStrategy(strategy2, true, true);
    assertSame(gsonBuilder, returned);
  }

  // Helper methods to access private excluder field using reflection
  private Excluder getExcluder(GsonBuilder builder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    return (Excluder) excluderField.get(builder);
  }

  private void setExcluder(GsonBuilder builder, Excluder excluder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(builder, excluder);
  }
}