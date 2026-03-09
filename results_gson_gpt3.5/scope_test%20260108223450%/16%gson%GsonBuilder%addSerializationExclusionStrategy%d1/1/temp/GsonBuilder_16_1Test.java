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

class GsonBuilder_16_1Test {

  private GsonBuilder gsonBuilder;
  private ExclusionStrategy mockStrategy;
  private Excluder originalExcluder;

  @BeforeEach
  void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();
    mockStrategy = mock(ExclusionStrategy.class);

    // Use reflection to get the private excluder field value for later verification
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    originalExcluder = (Excluder) excluderField.get(gsonBuilder);
  }

  @Test
    @Timeout(8000)
  void addSerializationExclusionStrategy_shouldThrowNPE_whenStrategyIsNull() {
    NullPointerException thrown = assertThrows(NullPointerException.class,
        () -> gsonBuilder.addSerializationExclusionStrategy(null));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void addSerializationExclusionStrategy_shouldUpdateExcluder_withGivenStrategy() throws Exception {
    // Spy the original excluder to verify withExclusionStrategy call
    Excluder spyExcluder = spy(originalExcluder);

    // Replace private excluder field with spy
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, spyExcluder);

    // Prepare a new Excluder to be returned by withExclusionStrategy()
    Excluder returnedExcluder = mock(Excluder.class);
    doReturn(returnedExcluder).when(spyExcluder).withExclusionStrategy(mockStrategy, true, false);

    GsonBuilder returnedBuilder = gsonBuilder.addSerializationExclusionStrategy(mockStrategy);

    // Verify that withExclusionStrategy was called exactly once with correct arguments
    verify(spyExcluder, times(1)).withExclusionStrategy(mockStrategy, true, false);

    // Verify the excluder field was updated to the returned Excluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(returnedExcluder, updatedExcluder);

    // Verify the returned GsonBuilder is the same instance
    assertSame(gsonBuilder, returnedBuilder);
  }
}