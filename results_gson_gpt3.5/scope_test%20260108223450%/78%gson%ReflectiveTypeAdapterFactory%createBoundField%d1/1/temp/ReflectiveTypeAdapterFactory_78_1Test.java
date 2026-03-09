package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class ReflectiveTypeAdapterFactory_CreateBoundFieldTest {

  private ReflectiveTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private Gson gson;
  private FieldNamingStrategy fieldNamingStrategy;
  private java.util.List<com.google.gson.ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    reflectionFilters = java.util.Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  private BoundField invokeCreateBoundField(Field field, Method accessor, String name,
                                            TypeToken<?> fieldType, boolean serialize,
                                            boolean deserialize, boolean blockInaccessible) throws Exception {
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (BoundField) method.invoke(factory, gson, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
  }

  @Test
    @Timeout(8000)
  void createBoundField_basicField_noJsonAdapter() throws Exception {
    class TestClass {
      public int intField = 5;
    }
    Field field = TestClass.class.getField("intField");
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(field, null, "intField", typeToken, true, true, false);

    assertNotNull(boundField);
    assertEquals("intField", boundField.name);
    // Test write method calls adapter.write with field value
    JsonWriter writer = mock(JsonWriter.class);
    TestClass instance = new TestClass();
    boundField.write(writer, instance);
    verify(adapter).write(eq(writer), eq(instance.intField));
    // Test readIntoField sets the field on target object
    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn(10);
    boundField.readIntoField(reader, instance);
    assertEquals(10, instance.intField);
  }

  @Test
    @Timeout(8000)
  void createBoundField_withJsonAdapterAnnotation_usesJsonAdapter() throws Exception {
    class TestClass {
      @JsonAdapter(TestAdapter.class)
      public String strField = "hello";
    }
    Field field = TestClass.class.getField("strField");
    JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> jsonAdapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), eq(typeToken), eq(annotation))).thenReturn(jsonAdapter);

    BoundField boundField = invokeCreateBoundField(field, null, "strField", typeToken, true, true, false);

    assertNotNull(boundField);
    assertEquals("strField", boundField.name);

    JsonWriter writer = mock(JsonWriter.class);
    TestClass instance = new TestClass();
    boundField.write(writer, instance);
    verify(jsonAdapter).write(eq(writer), eq(instance.strField));
  }

  @Test
    @Timeout(8000)
  void createBoundField_withAccessor_invokesAccessor() throws Exception {
    class TestClass {
      private String value = "accessorValue";
      public String getValue() { return value; }
    }
    Field field = TestClass.class.getDeclaredField("value");
    Method accessor = TestClass.class.getMethod("getValue");
    TypeToken<String> typeToken = TypeToken.get(String.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<String> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(field, accessor, "value", typeToken, true, true, false);

    assertNotNull(boundField);
    TestClass instance = new TestClass();
    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, instance);
    verify(accessor).invoke(instance);
    verify(adapter).write(eq(writer), eq("accessorValue"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_blockInaccessible_checksAccessible() throws Exception {
    class TestClass {
      private int privateField = 42;
    }
    Field field = TestClass.class.getDeclaredField("privateField");
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(field, null, "privateField", typeToken, true, true, true);

    assertNotNull(boundField);
    TestClass instance = new TestClass();
    JsonWriter writer = mock(JsonWriter.class);
    // We spy on the factory to verify checkAccessible is called
    ReflectiveTypeAdapterFactory spyFactory = spy(factory);
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    BoundField bf = (BoundField) method.invoke(spyFactory, gson, field, null, "privateField", typeToken, true, true, true);

    bf.write(writer, instance);
    verify(spyFactory).checkAccessible(instance, field);
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoField_staticFinal_throws() throws Exception {
    class TestClass {
      public static final int CONST = 1;
    }
    Field field = TestClass.class.getField("CONST");
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);
    when(adapter.read(any())).thenReturn(5);

    BoundField boundField = invokeCreateBoundField(field, null, "CONST", typeToken, true, true, false);

    JsonReader reader = mock(JsonReader.class);
    TestClass instance = new TestClass();

    JsonIOException ex = assertThrows(JsonIOException.class, () -> boundField.readIntoField(reader, instance));
    assertTrue(ex.getMessage().contains("Cannot set value of 'static final'"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoArray_nullPrimitive_throws() throws Exception {
    class TestClass {
      public int intField;
    }
    Field field = TestClass.class.getField("intField");
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);
    BoundField boundField = invokeCreateBoundField(field, null, "intField", typeToken, true, true, false);
    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn(null);

    Object[] target = new Object[1];
    JsonParseException ex = assertThrows(JsonParseException.class, () -> boundField.readIntoArray(reader, 0, target));
    assertTrue(ex.getMessage().contains("null is not allowed"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_write_fieldValueEqualsSource_returnsWithoutWriting() throws Exception {
    class TestClass {
      public TestClass self = this;
    }
    Field field = TestClass.class.getField("self");
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<TestClass> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);
    BoundField boundField = invokeCreateBoundField(field, null, "self", typeToken, true, true, false);

    JsonWriter writer = mock(JsonWriter.class);
    TestClass instance = new TestClass();

    boundField.write(writer, instance);
    verify(writer, never()).name(any());
    verify(adapter, never()).write(any(), any());
  }

  // Helper JsonAdapter for test
  public static class TestAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
    }
    @Override
    public String read(JsonReader in) throws IOException {
      return null;
    }
  }
}