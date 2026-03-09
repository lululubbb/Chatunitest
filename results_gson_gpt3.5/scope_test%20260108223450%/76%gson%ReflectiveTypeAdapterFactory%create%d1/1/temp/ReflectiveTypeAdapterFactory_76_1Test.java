package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ReflectiveTypeAdapterFactory_76_1Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        reflectionFilters);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullForPrimitiveType() {
    Gson gson = mock(Gson.class);
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

    TypeAdapter<Integer> adapter = factory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldThrowJsonIOExceptionWhenFilterBlocksAll() {
    Gson gson = mock(Gson.class);
    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    List<ReflectionAccessFilter> filters = Collections.singletonList(
        (ReflectionAccessFilter) raw -> FilterResult.BLOCK_ALL);

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        filters);

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      factory.create(gson, typeToken);
    });
    assertTrue(exception.getMessage().contains("ReflectionAccessFilter does not permit"));
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnRecordAdapterWhenTypeIsRecord() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<MyRecord> typeToken = TypeToken.get(MyRecord.class);

    List<ReflectionAccessFilter> filters = Collections.singletonList(
        (ReflectionAccessFilter) raw -> FilterResult.ALLOW);

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        filters);

    try (MockedStatic<ReflectionHelper> reflectionHelperMockedStatic = Mockito.mockStatic(ReflectionHelper.class)) {

      reflectionHelperMockedStatic.when(() -> ReflectionHelper.isRecord(MyRecord.class)).thenReturn(true);

      ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);

      // Mock getBoundFields via reflection to return empty map
      Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
          "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
      getBoundFieldsMethod.setAccessible(true);

      doAnswer(invocation -> Collections.emptyMap())
          .when(spyFactory)
          .createBoundFields(any(), any(), any(), anyBoolean(), anyBoolean());

      // Instead of mocking a non-existent method createBoundFieldsProxy,
      // we mock getBoundFields via reflection using doAnswer on spy.

      // Since getBoundFields is private, we use reflection to mock it:
      // But Mockito cannot mock private methods directly.
      // So we use a workaround: create a subclass that overrides getBoundFields,
      // or invoke getBoundFields via reflection in the test.
      // Here, we'll invoke create normally without mocking getBoundFields,
      // because mocking private methods with Mockito is not possible.

      when(constructorConstructor.get(typeToken)).thenReturn(() -> new MyRecord(""));

      TypeAdapter<MyRecord> adapter = spyFactory.create(gson, typeToken);

      assertNotNull(adapter);
      assertEquals("com.google.gson.internal.bind.RecordAdapter", adapter.getClass().getName());
    }
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnFieldReflectionAdapterForNormalClass() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    List<ReflectionAccessFilter> filters = Collections.singletonList(
        (ReflectionAccessFilter) raw -> FilterResult.ALLOW);

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        filters);

    try (MockedStatic<ReflectionHelper> reflectionHelperMockedStatic = Mockito.mockStatic(ReflectionHelper.class)) {
      reflectionHelperMockedStatic.when(() -> ReflectionHelper.isRecord(MyClass.class)).thenReturn(false);

      ObjectConstructor<MyClass> objectConstructor = () -> new MyClass();
      when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

      ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);

      // Remove mocking of getBoundFields (private method), just let it run normally

      TypeAdapter<MyClass> adapter = spyFactory.create(gson, typeToken);

      assertNotNull(adapter);
      assertEquals("com.google.gson.internal.bind.FieldReflectionAdapter", adapter.getClass().getName());
    }
  }

  // Dummy classes for testing
  static class MyClass {
    public int value;
  }

  // Use a static nested class instead of record for compatibility
  static class MyRecord {
    private final String name;

    public MyRecord(String name) {
      this.name = name;
    }

    public String name() {
      return name;
    }
  }
}