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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonBuilder_11_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void setFieldNamingPolicy_shouldDelegateToSetFieldNamingStrategy_andReturnThis() throws Exception {
    // Arrange
    FieldNamingPolicy namingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

    // Spy on gsonBuilder to verify internal call
    GsonBuilder spyBuilder = spy(gsonBuilder);

    // Act
    GsonBuilder returned = spyBuilder.setFieldNamingPolicy(namingPolicy);

    // Assert returned this
    assertSame(spyBuilder, returned);

    // Using reflection to access private field fieldNamingPolicy
    Field field = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    field.setAccessible(true);
    FieldNamingStrategy actualStrategy = (FieldNamingStrategy) field.get(spyBuilder);
    assertSame(namingPolicy, actualStrategy);

    // Verify that setFieldNamingStrategy was called with namingPolicy
    verify(spyBuilder).setFieldNamingStrategy(namingPolicy);
  }

  @Test
    @Timeout(8000)
  void setFieldNamingStrategy_shouldSetFieldNamingPolicy_andReturnThis() throws Exception {
    // Arrange
    FieldNamingStrategy customStrategy = mock(FieldNamingStrategy.class);

    // Act
    GsonBuilder returned = gsonBuilder.setFieldNamingStrategy(customStrategy);

    // Assert returned this
    assertSame(gsonBuilder, returned);

    // Using reflection to access private field fieldNamingPolicy
    Field field = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    field.setAccessible(true);
    FieldNamingStrategy actualStrategy = (FieldNamingStrategy) field.get(gsonBuilder);
    assertSame(customStrategy, actualStrategy);
  }
}