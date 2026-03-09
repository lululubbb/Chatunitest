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
import org.mockito.ArgumentCaptor;
import java.lang.reflect.Field;
import java.util.List;

public class GsonBuilder_16_3Test {

  private GsonBuilder gsonBuilder;
  private ExclusionStrategy mockStrategy;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_shouldAddStrategyToExcluder() throws Exception {
    // Arrange
    Excluder initialExcluder = getExcluderFromGsonBuilder(gsonBuilder);
    Excluder spyExcluder = spy(initialExcluder);
    setExcluderToGsonBuilder(gsonBuilder, spyExcluder);

    Excluder returnedExcluder = new Excluder();
    doReturn(returnedExcluder).when(spyExcluder).withExclusionStrategy(eq(mockStrategy), eq(true), eq(false));

    // Act
    GsonBuilder returned = gsonBuilder.addSerializationExclusionStrategy(mockStrategy);

    // Assert
    assertSame(gsonBuilder, returned, "addSerializationExclusionStrategy should return this");
    verify(spyExcluder).withExclusionStrategy(mockStrategy, true, false);

    Excluder excluderAfter = getExcluderFromGsonBuilder(gsonBuilder);
    assertSame(returnedExcluder, excluderAfter, "Excluder field should be updated with returned Excluder");
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_nullStrategy_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.addSerializationExclusionStrategy(null));
  }

  private static Excluder getExcluderFromGsonBuilder(GsonBuilder builder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    return (Excluder) excluderField.get(builder);
  }

  private static void setExcluderToGsonBuilder(GsonBuilder builder, Excluder excluder) throws Exception {
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(builder, excluder);
  }
}