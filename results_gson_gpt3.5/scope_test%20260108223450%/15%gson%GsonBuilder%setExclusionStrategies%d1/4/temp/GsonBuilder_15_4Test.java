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

class GsonBuilder_15_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_NullArray_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.setExclusionStrategies((ExclusionStrategy[]) null));
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_EmptyArray_ExcluderNotChanged() throws Exception {
    Excluder originalExcluder = getExcluderField(gsonBuilder);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies();

    assertSame(gsonBuilder, returned);
    Excluder afterExcluder = getExcluderField(gsonBuilder);
    // Since no strategies, excluder should remain the same instance (no withExclusionStrategy calls)
    assertSame(originalExcluder, afterExcluder);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_SingleStrategy_ExcluderUpdated() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder newExcluder = mock(Excluder.class);

    // Spy original excluder to stub withExclusionStrategy
    Excluder spyExcluder = spy(originalExcluder);
    setExcluderField(gsonBuilder, spyExcluder);
    // Use doReturn to avoid calling real method and incrementing invocation count
    doReturn(newExcluder).when(spyExcluder).withExclusionStrategy(strategy, true, true);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy);

    assertSame(gsonBuilder, returned);
    Excluder afterExcluder = getExcluderField(gsonBuilder);
    assertSame(newExcluder, afterExcluder);

    // Verify withExclusionStrategy called exactly once on spyExcluder
    verify(spyExcluder, times(1)).withExclusionStrategy(strategy, true, true);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_MultipleStrategies_ExcluderUpdatedSequentially() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);

    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder excluderAfterFirst = mock(Excluder.class);
    Excluder excluderAfterSecond = mock(Excluder.class);

    Excluder spyExcluder = spy(originalExcluder);
    setExcluderField(gsonBuilder, spyExcluder);

    doReturn(excluderAfterFirst).when(spyExcluder).withExclusionStrategy(strategy1, true, true);
    doReturn(excluderAfterSecond).when(excluderAfterFirst).withExclusionStrategy(strategy2, true, true);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy1, strategy2);

    assertSame(gsonBuilder, returned);
    Excluder afterExcluder = getExcluderField(gsonBuilder);
    assertSame(excluderAfterSecond, afterExcluder);

    verify(spyExcluder, times(1)).withExclusionStrategy(strategy1, true, true);
    verify(excluderAfterFirst, times(1)).withExclusionStrategy(strategy2, true, true);
  }

  private Excluder getExcluderField(GsonBuilder builder) throws Exception {
    Field field = GsonBuilder.class.getDeclaredField("excluder");
    field.setAccessible(true);
    return (Excluder) field.get(builder);
  }

  private void setExcluderField(GsonBuilder builder, Excluder excluder) throws Exception {
    Field field = GsonBuilder.class.getDeclaredField("excluder");
    field.setAccessible(true);
    field.set(builder, excluder);
  }
}