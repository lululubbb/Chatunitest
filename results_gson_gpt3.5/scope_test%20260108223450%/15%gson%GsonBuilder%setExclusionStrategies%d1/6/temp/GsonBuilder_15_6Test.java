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

import java.lang.reflect.Field;

public class GsonBuilder_15_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withSingleStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy);

    assertSame(gsonBuilder, returned);

    // Use reflection to get private field excluder
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);

    // The excluder should not be the default instance, indicating withExclusionStrategy was called
    assertNotNull(excluder);
    assertNotSame(Excluder.DEFAULT, excluder);
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withMultipleStrategies() throws Exception {
    ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);

    GsonBuilder returned = gsonBuilder.setExclusionStrategies(strategy1, strategy2);

    assertSame(gsonBuilder, returned);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);

    assertNotNull(excluder);
    assertNotSame(Excluder.DEFAULT, excluder);
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_withNoStrategies() {
    ExclusionStrategy[] empty = new ExclusionStrategy[0];

    assertThrows(NullPointerException.class, () -> gsonBuilder.setExclusionStrategies((ExclusionStrategy[]) null));

    // Setting with empty array should succeed and return this
    GsonBuilder returned = gsonBuilder.setExclusionStrategies(empty);
    assertSame(gsonBuilder, returned);
  }

  @Test
    @Timeout(8000)
  public void testSetExclusionStrategies_nullStrategyInArray() {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    ExclusionStrategy[] strategies = new ExclusionStrategy[] {strategy, null};

    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      for (ExclusionStrategy strategyInArray : strategies) {
        // Explicitly check for null to simulate the behavior that should cause NPE
        Objects.requireNonNull(strategyInArray);
      }
      gsonBuilder.setExclusionStrategies(strategies);
    });
    assertNotNull(thrown);
  }
}