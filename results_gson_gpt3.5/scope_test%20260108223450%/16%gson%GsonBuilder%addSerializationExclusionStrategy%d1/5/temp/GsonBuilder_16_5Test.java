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

public class GsonBuilder_16_5Test {

  private GsonBuilder gsonBuilder;
  private ExclusionStrategy mockStrategy;
  private Excluder originalExcluder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_shouldThrowNullPointerException_whenStrategyIsNull() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.addSerializationExclusionStrategy(null);
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void addSerializationExclusionStrategy_shouldAddStrategyAndReturnThis() throws Exception {
    // Access the original excluder field value for later comparison
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    originalExcluder = (Excluder) excluderField.get(gsonBuilder);

    // Spy on the original excluder to verify withExclusionStrategy call
    Excluder spyExcluder = spy(originalExcluder);
    excluderField.set(gsonBuilder, spyExcluder);

    // Stub withExclusionStrategy to return a new Excluder instance
    Excluder newExcluder = new Excluder();
    doReturn(newExcluder).when(spyExcluder).withExclusionStrategy(eq(mockStrategy), eq(true), eq(false));

    // Call the focal method
    GsonBuilder returned = gsonBuilder.addSerializationExclusionStrategy(mockStrategy);

    // Verify the returned object is the same instance
    assertSame(gsonBuilder, returned);

    // Verify withExclusionStrategy was called with correct arguments
    verify(spyExcluder).withExclusionStrategy(mockStrategy, true, false);

    // Verify the excluder field was updated to newExcluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(newExcluder, updatedExcluder);
  }
}