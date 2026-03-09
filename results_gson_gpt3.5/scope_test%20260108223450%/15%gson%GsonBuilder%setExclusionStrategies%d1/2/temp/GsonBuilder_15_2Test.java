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
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

class GsonBuilder_15_2Test {

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
  void testSetExclusionStrategies_EmptyArray_ExcluderUnchanged() throws Exception {
    Excluder originalExcluder = getExcluderField(gsonBuilder);
    GsonBuilder returned = gsonBuilder.setExclusionStrategies();
    Excluder afterExcluder = getExcluderField(gsonBuilder);
    assertSame(gsonBuilder, returned);
    assertSame(originalExcluder, afterExcluder);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_SingleStrategy_ExcluderUpdated() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    Excluder originalExcluder = getExcluderField(gsonBuilder);
    Excluder newExcluder = mock(Excluder.class);

    // Spy on original excluder to verify withExclusionStrategy call
    GsonBuilder spyBuilder = spy(gsonBuilder);
    setExcluderField(spyBuilder, originalExcluder);

    // Mock excluder.withExclusionStrategy to return newExcluder
    Excluder excluderSpy = spy(originalExcluder);
    doReturn(newExcluder).when(excluderSpy).withExclusionStrategy(strategy, true, true);
    setExcluderField(spyBuilder, excluderSpy);

    GsonBuilder returned = spyBuilder.setExclusionStrategies(strategy);

    Excluder afterExcluder = getExcluderField(spyBuilder);

    assertSame(spyBuilder, returned);
    assertSame(newExcluder, afterExcluder);
    verify(excluderSpy).withExclusionStrategy(strategy, true, true);
  }

  @Test
    @Timeout(8000)
  void testSetExclusionStrategies_MultipleStrategies_ExcluderUpdatedSequentially() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy3 = mock(ExclusionStrategy.class);

    Excluder originalExcluder = getExcluderField(gsonBuilder);

    Excluder excluder1 = mock(Excluder.class);
    Excluder excluder2 = mock(Excluder.class);
    Excluder excluder3 = mock(Excluder.class);

    // Spy on original excluder to verify withExclusionStrategy calls
    GsonBuilder spyBuilder = spy(gsonBuilder);
    setExcluderField(spyBuilder, originalExcluder);

    Excluder excluderSpy = spy(originalExcluder);

    // Setup chaining of withExclusionStrategy calls
    doReturn(excluder1).when(excluderSpy).withExclusionStrategy(strategy1, true, true);
    doReturn(excluder2).when(excluder1).withExclusionStrategy(strategy2, true, true);
    doReturn(excluder3).when(excluder2).withExclusionStrategy(strategy3, true, true);

    setExcluderField(spyBuilder, excluderSpy);

    // We need to simulate the chaining in the method:
    // Each iteration sets excluder = excluder.withExclusionStrategy(...)
    // So after first call, excluder field should be excluder1,
    // after second call excluder2, after third excluder3.

    // But since the field is private and only set after loop,
    // we can just verify the final excluder is excluder3 after method call.

    GsonBuilder returned = spyBuilder.setExclusionStrategies(strategy1, strategy2, strategy3);

    Excluder afterExcluder = getExcluderField(spyBuilder);

    assertSame(spyBuilder, returned);
    assertSame(excluder3, afterExcluder);

    verify(excluderSpy).withExclusionStrategy(strategy1, true, true);
    verify(excluder1).withExclusionStrategy(strategy2, true, true);
    verify(excluder2).withExclusionStrategy(strategy3, true, true);
  }

  private Excluder getExcluderField(GsonBuilder builder) throws Exception {
    Field f = GsonBuilder.class.getDeclaredField("excluder");
    f.setAccessible(true);
    return (Excluder) f.get(builder);
  }

  private void setExcluderField(GsonBuilder builder, Excluder excluder) throws Exception {
    Field f = GsonBuilder.class.getDeclaredField("excluder");
    f.setAccessible(true);
    f.set(builder, excluder);
  }
}