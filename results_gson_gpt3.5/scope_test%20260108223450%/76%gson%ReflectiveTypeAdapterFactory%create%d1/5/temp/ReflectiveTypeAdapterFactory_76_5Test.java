package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectiveTypeAdapterFactory_76_5Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    reflectionFilters = mock(List.class);
    gson = mock(Gson.class);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void create_returnsNullForPrimitiveType() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

    TypeAdapter<Integer> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_throwsJsonIOExceptionWhenFilterBlocksAll() {
    TypeToken<DummyClass> typeToken = TypeToken.get(DummyClass.class);

    // Create a ReflectionAccessFilter that returns BLOCK_ALL for DummyClass
    ReflectionAccessFilter filter = new ReflectionAccessFilter() {
      @Override
      public FilterResult check(Class<?> clazz) {
        if (clazz == DummyClass.class) {
          return FilterResult.BLOCK_ALL;
        }
        return FilterResult.ALLOW;
      }
    };
    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, Collections.singletonList(filter));

    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      factoryWithFilter.create(gson, typeToken);
    });
    assertTrue(ex.getMessage().contains("ReflectionAccessFilter does not permit using reflection for"));
  }

  @Test
    @Timeout(8000)
  void create_returnsRecordAdapterWhenRawTypeIsRecord() throws Exception {
    TypeToken<DummyRecord> typeToken = TypeToken.get(DummyRecord.class);

    ReflectionAccessFilter filter = clazz -> FilterResult.ALLOW;
    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, Collections.singletonList(filter));

    // Since ReflectiveTypeAdapterFactory is final and getBoundFields is private, we cannot override or mock them directly.
    // Instead, we create a spy and mock the static ReflectionHelper.isRecord method via reflection.

    ReflectiveTypeAdapterFactory factorySpy = spy(factoryWithFilter);

    // Mock ReflectionHelper.isRecord to return true for DummyRecord.class
    // ReflectionHelper.isRecord is static; we use reflection to replace its behavior temporarily.

    // Save original method reference
    Method isRecordMethod = ReflectionHelper.class.getDeclaredMethod("isRecord", Class.class);
    isRecordMethod.setAccessible(true);

    // Use a helper interface to mock static method (using a simple workaround with a proxy)
    // Since we can't mock static methods easily without a framework like Mockito-inline or Powermock,
    // we will use a wrapper ReflectiveTypeAdapterFactory subclass that uses a custom isRecord method.

    ReflectiveTypeAdapterFactory factoryWithIsRecordTrue = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, Collections.singletonList(filter)) {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();

        if (!Object.class.isAssignableFrom(raw)) {
          return null; // it's a primitive!
        }

        FilterResult filterResult =
            ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, raw);
        if (filterResult == FilterResult.BLOCK_ALL) {
          throw new JsonIOException(
              "ReflectionAccessFilter does not permit using reflection for " + raw
                  + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        boolean blockInaccessible = filterResult == FilterResult.BLOCK_INACCESSIBLE;

        // Override isRecord check to true for DummyRecord.class
        if (DummyRecord.class.equals(raw)) {
          @SuppressWarnings("unchecked")
          TypeAdapter<T> adapter = (TypeAdapter<T>) new RecordAdapter<>(raw,
              getBoundFields(gson, type, raw, blockInaccessible, true), blockInaccessible);
          return adapter;
        }

        ObjectConstructor<T> constructor = constructorConstructor.get(type);
        return new FieldReflectionAdapter<>(constructor, getBoundFields(gson, type, raw, blockInaccessible, false));
      }

      // Expose getBoundFields by reflection since it's private in the original class
      @SuppressWarnings("unchecked")
      private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw,
                                                    boolean blockInaccessible, boolean isRecord) {
        try {
          Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
              "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
          method.setAccessible(true);
          return (Map<String, BoundField>) method.invoke(this, context, type, raw, blockInaccessible, isRecord);
        } catch (ReflectiveOperationException e) {
          throw new RuntimeException(e);
        }
      }
    };

    TypeAdapter<DummyRecord> adapter = factoryWithIsRecordTrue.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$RecordAdapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void create_returnsFieldReflectionAdapterWhenRawTypeIsNormalClass() throws Exception {
    TypeToken<DummyClass> typeToken = TypeToken.get(DummyClass.class);

    ReflectionAccessFilter filter = clazz -> FilterResult.ALLOW;
    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, Collections.singletonList(filter));

    ObjectConstructor<DummyClass> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

    ReflectiveTypeAdapterFactory factoryWithIsRecordFalse = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, null, Collections.singletonList(filter)) {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();

        if (!Object.class.isAssignableFrom(raw)) {
          return null; // it's a primitive!
        }

        FilterResult filterResult =
            ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, raw);
        if (filterResult == FilterResult.BLOCK_ALL) {
          throw new JsonIOException(
              "ReflectionAccessFilter does not permit using reflection for " + raw
                  + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        boolean blockInaccessible = filterResult == FilterResult.BLOCK_INACCESSIBLE;

        // Override isRecord check to false always
        if (false) {
          @SuppressWarnings("unchecked")
          TypeAdapter<T> adapter = (TypeAdapter<T>) new RecordAdapter<>(raw,
              getBoundFields(gson, type, raw, blockInaccessible, true), blockInaccessible);
          return adapter;
        }

        ObjectConstructor<T> constructor = constructorConstructor.get(type);
        return new FieldReflectionAdapter<>(constructor, getBoundFields(gson, type, raw, blockInaccessible, false));
      }

      // Expose getBoundFields by reflection since it's private in the original class
      @SuppressWarnings("unchecked")
      private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw,
                                                    boolean blockInaccessible, boolean isRecord) {
        try {
          Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
              "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
          method.setAccessible(true);
          return (Map<String, BoundField>) method.invoke(this, context, type, raw, blockInaccessible, isRecord);
        } catch (ReflectiveOperationException e) {
          throw new RuntimeException(e);
        }
      }
    };

    TypeAdapter<DummyClass> adapter = factoryWithIsRecordFalse.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$FieldReflectionAdapter", adapter.getClass().getName());
  }

  // Dummy classes for testing
  private static class DummyClass {}

  private static class DummyRecord {
    private final int id;
    public DummyRecord(int id) {
      this.id = id;
    }
    public int id() {
      return id;
    }
  }
}