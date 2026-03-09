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
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class ReflectiveTypeAdapterFactory_76_4Test {

  private ConstructorConstructor mockConstructorConstructor;
  private ReflectiveTypeAdapterFactory factory;
  private Gson mockGson;
  private Object mockJsonAdapterFactory; // use Object to avoid direct reference
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() throws Exception {
    mockConstructorConstructor = mock(ConstructorConstructor.class);
    mockGson = mock(Gson.class);
    mockJsonAdapterFactory = mock(Object.class); // mock as Object to avoid missing class error
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    reflectionFilters = Collections.emptyList();

    // Use reflection to get the JsonAdapterAnnotationTypeAdapterFactory class and cast mock accordingly
    Class<?> jsonAdapterFactoryClass = null;
    for (Class<?> innerClass : ReflectiveTypeAdapterFactory.class.getDeclaredClasses()) {
      if ("JsonAdapterAnnotationTypeAdapterFactory".equals(innerClass.getSimpleName())) {
        jsonAdapterFactoryClass = innerClass;
        break;
      }
    }
    Object jsonAdapterFactoryMock = mockJsonAdapterFactory;
    if (jsonAdapterFactoryClass != null) {
      jsonAdapterFactoryMock = jsonAdapterFactoryClass.cast(mockJsonAdapterFactory);
    }

    // Use reflection to get the constructor and create factory instance
    factory = createFactoryInstance(
        mockConstructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactoryMock,
        reflectionFilters);
  }

  private ReflectiveTypeAdapterFactory createFactoryInstance(
      ConstructorConstructor constructorConstructor,
      FieldNamingStrategy fieldNamingStrategy,
      Excluder excluder,
      Object jsonAdapterFactory,
      List<ReflectionAccessFilter> reflectionFilters) throws Exception {
    // Find constructor with matching parameter types
    for (var ctor : ReflectiveTypeAdapterFactory.class.getDeclaredConstructors()) {
      Class<?>[] params = ctor.getParameterTypes();
      if (params.length == 5
          && params[0] == ConstructorConstructor.class
          && params[1] == FieldNamingStrategy.class
          && params[2] == Excluder.class
          && params[4] == List.class) {
        ctor.setAccessible(true);
        return (ReflectiveTypeAdapterFactory) ctor.newInstance(
            constructorConstructor,
            fieldNamingStrategy,
            excluder,
            jsonAdapterFactory,
            reflectionFilters);
      }
    }
    throw new IllegalStateException("Could not find suitable ReflectiveTypeAdapterFactory constructor");
  }

  @Test
    @Timeout(8000)
  void create_returnsNull_whenPrimitiveType() {
    TypeToken<Integer> intType = TypeToken.get(int.class);
    TypeAdapter<Integer> adapter = factory.create(mockGson, intType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_throwsJsonIOException_whenFilterBlocksAll() throws Exception {
    // Create a mock ReflectionAccessFilter using a dynamic proxy to mock the interface method
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);

    // Use reflection to find shouldSkipClass method
    Method shouldSkipClassMethod = null;
    for (Method m : ReflectionAccessFilter.class.getMethods()) {
      if ("shouldSkipClass".equals(m.getName()) && m.getParameterCount() == 1 && m.getParameterTypes()[0] == Class.class) {
        shouldSkipClassMethod = m;
        break;
      }
    }
    assertNotNull(shouldSkipClassMethod, "shouldSkipClass method not found in ReflectionAccessFilter");

    // Use Mockito to stub the method via reflection
    when(filter.shouldSkipClass(ArgumentMatchers.any(Class.class))).thenReturn(FilterResult.BLOCK_ALL);

    // Prepare new factory with filter list
    Class<?> jsonAdapterFactoryClass = null;
    for (Class<?> innerClass : ReflectiveTypeAdapterFactory.class.getDeclaredClasses()) {
      if ("JsonAdapterAnnotationTypeAdapterFactory".equals(innerClass.getSimpleName())) {
        jsonAdapterFactoryClass = innerClass;
        break;
      }
    }
    Object jsonAdapterFactoryMock = mockJsonAdapterFactory;
    if (jsonAdapterFactoryClass != null) {
      jsonAdapterFactoryMock = jsonAdapterFactoryClass.cast(mockJsonAdapterFactory);
    }

    factory = createFactoryInstance(
        mockConstructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactoryMock,
        Collections.singletonList(filter));

    TypeToken<String> stringType = TypeToken.get(String.class);

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      factory.create(mockGson, stringType);
    });
    assertTrue(exception.getMessage().contains("ReflectionAccessFilter does not permit using reflection"));
  }

  @Test
    @Timeout(8000)
  void create_returnsRecordAdapter_whenClassIsRecord() throws Exception {
    Class<?> recordClass = getRecordTestClass();

    // Spy the factory to mock private methods isRecord and getBoundFields via reflection
    ReflectiveTypeAdapterFactory factorySpy = spy(factory);

    // Mock private isRecord method via reflection
    Method isRecordMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("isRecord", Class.class);
    isRecordMethod.setAccessible(true);
    doReturn(true).when(factorySpy).create(any(), any()); // prevent actual create call recursion

    // Instead of doReturn(true).when(factorySpy).isRecord(recordClass);
    // we use reflection to override isRecord method behavior
    doAnswer(invocation -> {
      Class<?> arg = invocation.getArgument(0);
      if (arg == recordClass) {
        return true;
      }
      // fallback to real method
      return isRecordMethod.invoke(factorySpy, arg);
    }).when(factorySpy).isRecord(any());

    // Mock getBoundFields (private) via reflection
    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    doAnswer(invocation -> Collections.emptyMap())
        .when(factorySpy)
        .getBoundFields(any(), any(), eq(recordClass), anyBoolean(), eq(true));

    // Mock constructorConstructor.get to return a dummy ObjectConstructor
    ObjectConstructor<?> mockConstructor = mock(ObjectConstructor.class);
    when(mockConstructorConstructor.get(TypeToken.get(recordClass))).thenReturn(mockConstructor);

    TypeToken<?> recordTypeToken = TypeToken.get(recordClass);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = (TypeAdapter<Object>) factorySpy.create(mockGson, (TypeToken<Object>) recordTypeToken);

    assertNotNull(adapter);
    assertEquals(ReflectiveTypeAdapterFactory.RecordAdapter.class, adapter.getClass());
  }

  @Test
    @Timeout(8000)
  void create_returnsFieldReflectionAdapter_whenNormalClass() throws Exception {
    Class<String> clazz = String.class;
    TypeToken<String> stringType = TypeToken.get(clazz);

    ReflectiveTypeAdapterFactory spyFactory = spy(factory);
    // Mock private isRecord method via reflection
    Method isRecordMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("isRecord", Class.class);
    isRecordMethod.setAccessible(true);
    doReturn(false).when(spyFactory).create(any(), any()); // prevent recursion

    doAnswer(invocation -> false).when(spyFactory).isRecord(any());

    // Mock getBoundFields (private) via reflection
    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    doAnswer(invocation -> Collections.emptyMap())
        .when(spyFactory)
        .getBoundFields(any(), any(), eq(clazz), anyBoolean(), eq(false));

    ObjectConstructor<String> mockObjectConstructor = mock(ObjectConstructor.class);
    when(mockConstructorConstructor.get(stringType)).thenReturn(mockObjectConstructor);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = spyFactory.create(mockGson, stringType);

    assertNotNull(adapter);
    assertEquals(ReflectiveTypeAdapterFactory.FieldReflectionAdapter.class, adapter.getClass());
  }

  // Helper method to get a record class via reflection to avoid 'record' keyword usage
  private static Class<?> getRecordTestClass() throws Exception {
    class RecordTest {
      private final String s;

      public RecordTest(String s) {
        this.s = s;
      }

      public String s() {
        return s;
      }
    }
    return RecordTest.class;
  }
}