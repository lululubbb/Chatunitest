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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

public class ReflectiveTypeAdapterFactory_76_2Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  public void create_returnsNullForPrimitiveType() {
    TypeToken<Integer> typeToken = TypeToken.get(int.class);
    TypeAdapter<Integer> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_throwsJsonIOException_whenFilterBlocksAll() {
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    reflectionFilters = Collections.singletonList(filter);
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);

    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    try (MockedStatic<com.google.gson.internal.ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(com.google.gson.internal.ReflectionAccessFilterHelper.class)) {
      mockedHelper.when(() -> com.google.gson.internal.ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, MyClass.class))
          .thenReturn(FilterResult.BLOCK_ALL);

      JsonIOException exception = assertThrows(JsonIOException.class, () -> factory.create(gson, typeToken));
      assertTrue(exception.getMessage().contains("ReflectionAccessFilter does not permit using reflection for"));
    }
  }

  @Test
    @Timeout(8000)
  public void create_returnsRecordAdapter_whenTypeIsRecord() throws Exception {
    // Use a normal class and mock ReflectionHelper.isRecord to true to simulate a record
    Class<?> recordLikeClass = MyRecordLike.class;
    TypeToken<?> typeToken = TypeToken.get(recordLikeClass);

    // Prepare a constructor for MyRecordLike to avoid NPE in getBoundFields
    Constructor<?> ctor = recordLikeClass.getDeclaredConstructor(int.class);
    if (!Modifier.isPublic(ctor.getModifiers())) {
      ctor.setAccessible(true);
    }

    // Prepare a Field to be returned by getDeclaredFields to avoid NPE in getBoundFields
    Field idField = recordLikeClass.getDeclaredField("id");
    if (!Modifier.isPublic(idField.getModifiers())) {
      idField.setAccessible(true);
    }

    try (MockedStatic<com.google.gson.internal.ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(com.google.gson.internal.ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedHelper.when(() -> com.google.gson.internal.ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, recordLikeClass))
          .thenReturn(FilterResult.ALLOW);
      mockedReflectionHelper.when(() -> ReflectionHelper.isRecord(recordLikeClass))
          .thenReturn(true);

      // Mock constructorConstructor.get to return a dummy ObjectConstructor that returns a new instance
      @SuppressWarnings("unchecked")
      ObjectConstructor<Object> objectConstructor = mock(ObjectConstructor.class);
      when(constructorConstructor.get((TypeToken<Object>) typeToken)).thenReturn(objectConstructor);
      when(objectConstructor.construct()).thenAnswer(invocation -> ctor.newInstance(0));

      @SuppressWarnings("unchecked")
      TypeAdapter<Object> adapter = (TypeAdapter<Object>) factory.create(gson, (TypeToken<Object>) typeToken);
      assertNotNull(adapter);
      // The returned adapter should be a RecordAdapter (cannot access class directly, so check class name)
      assertTrue(adapter.getClass().getSimpleName().contains("RecordAdapter"));
    }
  }

  @Test
    @Timeout(8000)
  public void create_returnsFieldReflectionAdapter_whenTypeIsNormalClass() {
    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    try (MockedStatic<com.google.gson.internal.ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(com.google.gson.internal.ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedHelper.when(() -> com.google.gson.internal.ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, MyClass.class))
          .thenReturn(FilterResult.ALLOW);
      mockedReflectionHelper.when(() -> ReflectionHelper.isRecord(MyClass.class))
          .thenReturn(false);

      @SuppressWarnings("unchecked")
      ObjectConstructor<MyClass> objectConstructor = mock(ObjectConstructor.class);
      when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

      TypeAdapter<MyClass> adapter = factory.create(gson, typeToken);
      assertNotNull(adapter);
      // The returned adapter should be a FieldReflectionAdapter (check class name contains it)
      assertTrue(adapter.getClass().getSimpleName().contains("FieldReflectionAdapter"));
    }
  }

  // Dummy classes for testing
  static class MyClass {}

  // Use a normal class to simulate a record-like class for testing
  static class MyRecordLike {
    private final int id;

    public MyRecordLike(int id) {
      this.id = id;
    }

    public int id() {
      return id;
    }
  }
}