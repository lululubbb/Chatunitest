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
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class GsonBuilder_16_4Test {

  private GsonBuilder gsonBuilder;
  private ExclusionStrategy mockStrategy;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  public void testAddSerializationExclusionStrategy_NullStrategy_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.addSerializationExclusionStrategy(null));
  }

  @Test
    @Timeout(8000)
  public void testAddSerializationExclusionStrategy_AddsStrategyToExcluder() throws Exception {
    GsonBuilder builder = new GsonBuilder();

    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    GsonBuilder returnedBuilder = builder.addSerializationExclusionStrategy(strategy);

    assertSame(builder, returnedBuilder);

    // Use reflection to access private excluder field
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(builder);

    // Use reflection to access serializationStrategies field in Excluder
    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<ExclusionStrategy> serializationStrategies = (List<ExclusionStrategy>) serializationStrategiesField.get(excluder);

    // The new strategy should be contained in the serializationStrategies list
    assertTrue(serializationStrategies.contains(strategy));
  }

}