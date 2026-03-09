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

class GsonBuilder_17_6Test {

  private GsonBuilder gsonBuilder;
  private ExclusionStrategy mockStrategy;
  private Excluder initialExcluder;

  @BeforeEach
  void setUp() throws Exception {
    gsonBuilder = new GsonBuilder();

    // Access private field 'excluder' to get initial value
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    initialExcluder = (Excluder) excluderField.get(gsonBuilder);

    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  void addDeserializationExclusionStrategy_nullStrategy_throwsNullPointerException() {
    NullPointerException npe = assertThrows(NullPointerException.class,
        () -> gsonBuilder.addDeserializationExclusionStrategy(null));
    assertNotNull(npe);
  }

  @Test
    @Timeout(8000)
  void addDeserializationExclusionStrategy_callsWithExclusionStrategyAndReturnsThis() throws Exception {
    Excluder newExcluder = mock(Excluder.class);

    // Spy the initial excluder to mock withExclusionStrategy call
    Excluder spyExcluder = spy(initialExcluder);

    // Replace private field 'excluder' with spyExcluder
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    excluderField.set(gsonBuilder, spyExcluder);

    // Use doReturn().when() to avoid calling real method twice
    doReturn(newExcluder).when(spyExcluder).withExclusionStrategy(mockStrategy, false, true);

    GsonBuilder returned = gsonBuilder.addDeserializationExclusionStrategy(mockStrategy);

    assertSame(gsonBuilder, returned);

    // Verify withExclusionStrategy called exactly once with correct arguments
    verify(spyExcluder, times(1)).withExclusionStrategy(mockStrategy, false, true);

    // Verify private field 'excluder' updated to newExcluder
    Excluder currentExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(newExcluder, currentExcluder);
  }
}