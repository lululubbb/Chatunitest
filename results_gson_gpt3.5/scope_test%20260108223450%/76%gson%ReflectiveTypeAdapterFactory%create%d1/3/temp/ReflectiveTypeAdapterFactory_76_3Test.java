package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.FieldReflectionAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.RecordAdapter;
import com.google.gson.TypeAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class ReflectiveTypeAdapterFactory_76_3Test {

  private ConstructorConstructor constructorConstructor;
  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;
  private TypeToken<Object> typeToken;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        mock(com.google.gson.FieldNamingStrategy.class),
        mock(com.google.gson.internal.Excluder.class),
        mock(JsonAdapterAnnotationTypeAdapterFactory.class),
        reflectionFilters
    );
    typeToken = TypeToken.get(Object.class);
  }

  @Test
    @Timeout(8000)
  void create_returnsNullForPrimitiveType() {
    TypeToken<Integer> intType = TypeToken.get(Integer.class);
    TypeAdapter<Integer> adapter = factory.create(gson, intType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_throwsJsonIOException_whenFilterBlocksAll() {
    // Setup a filter that returns BLOCK_ALL for Object.class
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.checkAccess(Object.class)).thenReturn(FilterResult.BLOCK_ALL);
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        mock(com.google.gson.FieldNamingStrategy.class),
        mock(com.google.gson.internal.Excluder.class),
        mock(JsonAdapterAnnotationTypeAdapterFactory.class),
        List.of(filter)
    );

    JsonIOException ex = assertThrows(JsonIOException.class, () -> factory.create(gson, typeToken));
    assertTrue(ex.getMessage().contains("ReflectionAccessFilter does not permit using reflection for"));
  }

  @Test
    @Timeout(8000)
  void create_returnsRecordAdapter_whenRawTypeIsRecord() throws Exception {
    // Use a record type for testing
    // We create a dummy record class dynamically because Java 16+ is required for records.
    // If running on lower Java, this test will be skipped.
    Class<?> recordClass;
    try {
      recordClass = Class.forName("java.lang.Record");
    } catch (ClassNotFoundException e) {
      // JVM does not support records, skip this test
      return;
    }

    // Create a dummy record class for test
    // We simulate ReflectionHelper.isRecord to return true for a custom class
    Class<?> dummyRecord = DummyRecord.class;

    // Spy the factory to mock ReflectionHelper.isRecord
    ReflectiveTypeAdapterFactory spyFactory = spy(factory);
    doReturn(true).when(spyFactory).isRecord(dummyRecord);

    TypeToken<?> recordTypeToken = TypeToken.get(dummyRecord);
    ObjectConstructor<?> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(recordTypeToken)).thenReturn((ObjectConstructor) objectConstructor);

    // We also need to spy getBoundFields to avoid complex setup, just return empty map
    doReturn(Collections.emptyMap()).when(spyFactory)
        .getBoundFields(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), anyBoolean(), anyBoolean());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = (TypeAdapter<Object>) spyFactory.create(gson, (TypeToken<Object>) recordTypeToken);
    assertNotNull(adapter);
    assertTrue(adapter instanceof RecordAdapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsFieldReflectionAdapter_forNormalClass() {
    TypeToken<Object> type = TypeToken.get(Object.class);
    ObjectConstructor<Object> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(type)).thenReturn(objectConstructor);

    ReflectiveTypeAdapterFactory spyFactory = spy(factory);
    doReturn(Collections.emptyMap()).when(spyFactory)
        .getBoundFields(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), anyBoolean(), anyBoolean());
    doReturn(false).when(spyFactory).isRecord(Object.class);

    TypeAdapter<Object> adapter = spyFactory.create(gson, type);
    assertNotNull(adapter);
    assertTrue(adapter instanceof FieldReflectionAdapter);
  }

  // Dummy record class for testing record path
  public record DummyRecord(String name) {}
}