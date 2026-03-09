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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

class GsonBuilder_17_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void addDeserializationExclusionStrategy_nullStrategy_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.addDeserializationExclusionStrategy(null);
    });
    // The actual message from Objects.requireNonNull is null by default, so check that message is null
    assertNull(exception.getMessage(), "Exception message should be null");
  }

  @Test
    @Timeout(8000)
  void addDeserializationExclusionStrategy_addsStrategyToExcluderAndReturnsThis() throws Exception {
    ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
    // Use reflection to get the private excluder field before invocation
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder originalExcluder = (Excluder) excluderField.get(gsonBuilder);

    // Mock Excluder and its withExclusionStrategy method
    Excluder mockExcluder = mock(Excluder.class);
    when(mockExcluder.withExclusionStrategy(mockStrategy, false, true)).thenReturn(mockExcluder);

    // Use Mockito's MockedStatic to mock Excluder.DEFAULT to return originalExcluder as usual
    try (MockedStatic<Excluder> mockedExcluderStatic = Mockito.mockStatic(Excluder.class, Mockito.CALLS_REAL_METHODS)) {
      // Replace the excluder field with mockExcluder before calling method to simulate withExclusionStrategy call
      excluderField.set(gsonBuilder, mockExcluder);

      GsonBuilder returned = gsonBuilder.addDeserializationExclusionStrategy(mockStrategy);

      // Verify withExclusionStrategy called with correct args
      verify(mockExcluder).withExclusionStrategy(mockStrategy, false, true);

      // Verify that the excluder field is updated to mockExcluder returned by withExclusionStrategy
      Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
      assertSame(mockExcluder, updatedExcluder);

      // Verify method returns this instance
      assertSame(gsonBuilder, returned);
    }
  }
}