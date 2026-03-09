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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactory_76_6Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  static class PrimitiveType {}

  static class NormalType {}

  static class RecordLike {
    public final int x = 0;
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullForPrimitiveType() {
    TypeToken<Integer> primitiveType = TypeToken.get(int.class);
    TypeAdapter<Integer> adapter = factory.create(gson, primitiveType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldThrowJsonIOException_whenFilterBlocksAll() {
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    reflectionFilters = Collections.singletonList(filter);
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);

    TypeToken<NormalType> typeToken = TypeToken.get(NormalType.class);

    when(filter.filterType(any(Class.class))).thenReturn(FilterResult.BLOCK_ALL);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> factory.create(gson, typeToken));
    assertTrue(thrown.getMessage().contains("ReflectionAccessFilter does not permit using reflection"));
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnRecordAdapter_whenTypeIsRecord() throws Exception {
    TypeToken<RecordLike> recordType = TypeToken.get(RecordLike.class);

    ObjectConstructor<RecordLike> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(recordType)).thenReturn(objectConstructor);

    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    ReflectiveTypeAdapterFactory factoryWithRecord = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters) {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
          return null;
        }
        FilterResult filterResult = FilterResult.ALLOW;
        boolean blockInaccessible = filterResult == FilterResult.BLOCK_INACCESSIBLE;
        if (raw == RecordLike.class) {
          @SuppressWarnings("unchecked")
          TypeAdapter<T> adapter = (TypeAdapter<T>) newRecordAdapter(raw,
              invokeGetBoundFields(gson, type, raw, blockInaccessible, true), blockInaccessible);
          return adapter;
        }
        ObjectConstructor<T> constructor = constructorConstructor.get(type);
        return newFieldReflectionAdapter(constructor, invokeGetBoundFields(gson, type, raw, blockInaccessible, false));
      }

      @SuppressWarnings("unchecked")
      private <T> Map<String, ReflectiveTypeAdapterFactory.BoundField> invokeGetBoundFields(Gson gson, TypeToken<T> type, Class<? super T> raw, boolean blockInaccessible, boolean isRecord) {
        try {
          return (Map<String, ReflectiveTypeAdapterFactory.BoundField>) getBoundFieldsMethod.invoke(this, gson, type, raw, blockInaccessible, isRecord);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @SuppressWarnings("unchecked")
      private <T> TypeAdapter<T> newRecordAdapter(Class<? super T> raw, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields, boolean blockInaccessible) {
        try {
          Class<?> recordAdapterClass = Class.forName("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$RecordAdapter");
          return (TypeAdapter<T>) recordAdapterClass.getConstructor(Class.class, Map.class, boolean.class)
              .newInstance(raw, boundFields, blockInaccessible);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @SuppressWarnings("unchecked")
      private <T> TypeAdapter<T> newFieldReflectionAdapter(ObjectConstructor<T> constructor, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
        try {
          Class<?> fieldReflectionAdapterClass = Class.forName("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$FieldReflectionAdapter");
          return (TypeAdapter<T>) fieldReflectionAdapterClass.getConstructor(ObjectConstructor.class, Map.class)
              .newInstance(constructor, boundFields);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };

    TypeAdapter<RecordLike> adapter = factoryWithRecord.create(gson, recordType);
    assertNotNull(adapter);
    assertEquals(
        Class.forName("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$RecordAdapter"),
        adapter.getClass());
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnFieldReflectionAdapter_whenNormalType() throws Exception {
    TypeToken<NormalType> normalType = TypeToken.get(NormalType.class);

    ObjectConstructor<NormalType> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(normalType)).thenReturn(objectConstructor);

    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    // Use a helper class instead of subclassing final class
    class FactoryWithEmptyBoundFields implements ReflectiveTypeAdapterFactoryInterface {
      private final ConstructorConstructor constructorConstructor;
      private final FieldNamingStrategy fieldNamingStrategy;
      private final Excluder excluder;
      private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
      private final List<ReflectionAccessFilter> reflectionFilters;

      FactoryWithEmptyBoundFields(ConstructorConstructor constructorConstructor,
                                 FieldNamingStrategy fieldNamingStrategy,
                                 Excluder excluder,
                                 JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory,
                                 List<ReflectionAccessFilter> reflectionFilters) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
        this.reflectionFilters = reflectionFilters;
      }

      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
          return null;
        }
        FilterResult filterResult = FilterResult.ALLOW;
        boolean blockInaccessible = filterResult == FilterResult.BLOCK_INACCESSIBLE;
        ObjectConstructor<T> constructor = constructorConstructor.get(type);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;
        try {
          boundFields = (Map<String, ReflectiveTypeAdapterFactory.BoundField>) getBoundFieldsMethod.invoke(
              factory, gson, type, raw, blockInaccessible, false);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        try {
          Class<?> fieldReflectionAdapterClass = Class.forName("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$FieldReflectionAdapter");
          @SuppressWarnings("unchecked")
          TypeAdapter<T> adapter = (TypeAdapter<T>) fieldReflectionAdapterClass.getConstructor(ObjectConstructor.class, Map.class)
              .newInstance(constructor, boundFields);
          return adapter;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    FactoryWithEmptyBoundFields factoryWithEmptyBoundFields = new FactoryWithEmptyBoundFields(constructorConstructor,
        fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);

    TypeAdapter<NormalType> adapter = factoryWithEmptyBoundFields.create(gson, normalType);
    assertNotNull(adapter);
    assertEquals(
        Class.forName("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$FieldReflectionAdapter"),
        adapter.getClass());
  }

  // Interface to allow the helper class to implement create()
  interface ReflectiveTypeAdapterFactoryInterface {
    <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
  }
}