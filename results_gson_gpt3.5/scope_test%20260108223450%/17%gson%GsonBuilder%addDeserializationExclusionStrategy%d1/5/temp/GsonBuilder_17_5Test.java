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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

public class GsonBuilder_17_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testAddDeserializationExclusionStrategy_NullStrategy_ThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.addDeserializationExclusionStrategy(null));
  }

  @Test
    @Timeout(8000)
  public void testAddDeserializationExclusionStrategy_ValidStrategy_UpdatesExcluderAndReturnsThis() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    // Access the private excluder field before invocation
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder originalExcluder = (Excluder) excluderField.get(gsonBuilder);

    // Mock Excluder.withExclusionStrategy to return a new Excluder instance
    Excluder newExcluder = mock(Excluder.class);
    try (MockedStatic<Objects> objectsMockedStatic = Mockito.mockStatic(Objects.class)) {
      // We don't need to mock Objects.requireNonNull because it is static final method in JDK, 
      // but if needed, this is how to mock static methods.
    }

    // Spy the original Excluder to mock withExclusionStrategy
    Excluder spyExcluder = spy(originalExcluder);
    doReturn(newExcluder).when(spyExcluder).withExclusionStrategy(strategy, false, true);

    // Replace excluder field with the spy
    excluderField.set(gsonBuilder, spyExcluder);

    GsonBuilder returned = gsonBuilder.addDeserializationExclusionStrategy(strategy);

    // Verify withExclusionStrategy called with correct parameters
    verify(spyExcluder).withExclusionStrategy(strategy, false, true);

    // Verify excluder field updated to newExcluder
    Excluder updatedExcluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(newExcluder, updatedExcluder);

    // Verify method returns this instance
    assertSame(gsonBuilder, returned);
  }
}