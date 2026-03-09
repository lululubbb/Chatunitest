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

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class GsonBuilder_15_1Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withNull_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.setExclusionStrategies((ExclusionStrategy[]) null));
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withEmptyArray_shouldNotChangeExcluder() throws Exception {
    Excluder originalExcluder = getExcluderField(gsonBuilder);
    GsonBuilder returned = gsonBuilder.setExclusionStrategies();
    Excluder afterExcluder = getExcluderField(gsonBuilder);
    assertSame(gsonBuilder, returned);
    assertSame(originalExcluder, afterExcluder);
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withSingleStrategy_shouldUpdateExcluder() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    Excluder originalExcluder = getExcluderField(gsonBuilder);

    // Mock Excluder.withExclusionStrategy to return a new Excluder instance
    Excluder newExcluder = mock(Excluder.class);
    try (MockedStatic<Excluder> excluderStaticMock = Mockito.mockStatic(Excluder.class, Mockito.CALLS_REAL_METHODS)) {
      // We cannot mock static DEFAULT field, so we mock the withExclusionStrategy method on originalExcluder
      Excluder spyExcluder = spy(originalExcluder);
      doReturn(newExcluder).when(spyExcluder).withExclusionStrategy(strategy, true, true);
      setExcluderField(gsonBuilder, spyExcluder);

      GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy);
      Excluder afterExcluder = getExcluderField(gsonBuilder);

      assertSame(gsonBuilder, returned);
      assertSame(newExcluder, afterExcluder);
      verify(spyExcluder).withExclusionStrategy(strategy, true, true);
    }
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withMultipleStrategies_shouldUpdateExcluderStepwise() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);

    Excluder originalExcluder = getExcluderField(gsonBuilder);

    Excluder firstExcluder = mock(Excluder.class);
    Excluder secondExcluder = mock(Excluder.class);

    // Spy originalExcluder to mock withExclusionStrategy calls
    Excluder spyExcluder = spy(originalExcluder);
    doReturn(firstExcluder).when(spyExcluder).withExclusionStrategy(strategy1, true, true);
    doReturn(secondExcluder).when(firstExcluder).withExclusionStrategy(strategy2, true, true);

    setExcluderField(gsonBuilder, spyExcluder);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy1, strategy2);
    Excluder afterExcluder = getExcluderField(gsonBuilder);

    assertSame(gsonBuilder, returned);
    assertSame(secondExcluder, afterExcluder);

    verify(spyExcluder).withExclusionStrategy(strategy1, true, true);
    verify(firstExcluder).withExclusionStrategy(strategy2, true, true);
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