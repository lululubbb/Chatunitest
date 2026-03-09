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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_16_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_shouldAddStrategyAndReturnThis() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    GsonBuilder returned = gsonBuilder.addSerializationExclusionStrategy(strategy);

    // returned instance should be same as gsonBuilder (this)
    assertSame(gsonBuilder, returned);

    // verify excluder field updated with the new strategy
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);

    // The excluder field should not be null and should have the serialization strategy added
    assertSame(Excluder.class, excluder.getClass());

    // Using reflection to get serializationStrategies list from Excluder
    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    List<?> serializationStrategies = (List<?>) serializationStrategiesField.get(excluder);

    // The list should contain the passed strategy
    boolean containsStrategy = serializationStrategies.stream().anyMatch(s -> s == strategy);
    assertSame(true, containsStrategy);
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_shouldThrowNullPointerException_whenStrategyIsNull() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.addSerializationExclusionStrategy(null));
  }
}