package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

class ReflectiveTypeAdapterFactory_78_6Test {

  ReflectiveTypeAdapterFactory factory;
  ConstructorConstructor constructorConstructor;
  FieldNamingStrategy fieldNamingStrategy;
  Excluder excluder;
  JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  java.util.List<com.google.gson.ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = java.util.Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void createBoundField_withJsonAdapterAnnotation_andAccessor_writeRead() throws Exception {
    Gson context = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("fieldWithJsonAdapter");
    Method accessor = SampleClass.class.getDeclaredMethod("getFieldWithJsonAdapter");
    String name = "fieldWithJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(String.class);
    boolean serialize = true;
    boolean deserialize = true;
    boolean blockInaccessible = true;

    JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> annotatedAdapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), eq(context), eq(fieldType), eq(annotation))).thenReturn(annotatedAdapter);

    BoundField boundField = invokeCreateBoundField(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    assertNotNull(boundField);

    SampleClass sample = new SampleClass();
    sample.setFieldWithJsonAdapter("value");

    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, sample);
    verify(writer).name(name);
    verify(annotatedAdapter).write(eq(writer), eq("value"));

    JsonReader reader = mock(JsonReader.class);
    when(annotatedAdapter.read(reader)).thenReturn("newValue");
    boundField.readIntoField(reader, sample);
    assertEquals("newValue", sample.getFieldWithJsonAdapter());
  }

  @Test
    @Timeout(8000)
  void createBoundField_withoutJsonAdapterAnnotation_andFieldAccess_writeRead() throws Exception {
    Gson context = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("fieldWithoutJsonAdapter");
    Method accessor = null;
    String name = "fieldWithoutJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(int.class);
    boolean serialize = true;
    boolean deserialize = true;
    boolean blockInaccessible = false;

    TypeAdapter<?> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(context.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    assertNotNull(boundField);

    SampleClass sample = new SampleClass();
    field.setAccessible(true);
    field.setInt(sample, 42);

    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, sample);
    verify(writer).name(name);
    verify(adapter).write(eq(writer), eq(42));

    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn(24);
    boundField.readIntoField(reader, sample);
    assertEquals(24, field.getInt(sample));
  }

  @Test
    @Timeout(8000)
  void createBoundField_serializeFalse_doesNotWrite() throws Exception {
    Gson context = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("fieldWithoutJsonAdapter");
    Method accessor = null;
    String name = "fieldWithoutJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(int.class);
    boolean serialize = false;
    boolean deserialize = true;
    boolean blockInaccessible = false;

    TypeAdapter<?> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(context.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    assertNotNull(boundField);

    SampleClass sample = new SampleClass();
    field.setAccessible(true);
    field.setInt(sample, 42);

    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, sample);
    verifyNoInteractions(writer);
    verifyNoInteractions(adapter);
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoArray_nullForPrimitive_throws() throws Exception {
    Gson context = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("fieldWithoutJsonAdapter");
    Method accessor = null;
    String name = "fieldWithoutJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(int.class);
    boolean serialize = true;
    boolean deserialize = true;
    boolean blockInaccessible = false;

    TypeAdapter<?> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(context.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    assertNotNull(boundField);

    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn(null);

    Object[] target = new Object[1];
    JsonParseException ex = assertThrows(JsonParseException.class, () -> boundField.readIntoArray(reader, 0, target));
    assertTrue(ex.getMessage().contains("null is not allowed"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoField_staticFinalField_throws() throws Exception {
    Gson context = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("STATIC_FINAL_FIELD");
    Method accessor = null;
    String name = "STATIC_FINAL_FIELD";
    TypeToken<?> fieldType = TypeToken.get(String.class);
    boolean serialize = true;
    boolean deserialize = true;
    boolean blockInaccessible = false;

    TypeAdapter<?> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(context.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    assertNotNull(boundField);

    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn("newValue");

    JsonIOException ex = assertThrows(com.google.gson.JsonIOException.class, () -> boundField.readIntoField(reader, new SampleClass()));
    assertTrue(ex.getMessage().contains("Cannot set value of 'static final'"));
  }

  private BoundField invokeCreateBoundField(
      ReflectiveTypeAdapterFactory factory, Gson context, Field field, Method accessor,
      String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize,
      boolean blockInaccessible) throws Exception {
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("createBoundField", Gson.class, Field.class, Method.class, String.class, TypeToken.class, boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (BoundField) method.invoke(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
  }

  private static class SampleClass {
    @JsonAdapter(MockAdapter.class)
    private String fieldWithJsonAdapter;

    private int fieldWithoutJsonAdapter;

    static final String STATIC_FINAL_FIELD = "constant";

    public String getFieldWithJsonAdapter() {
      return fieldWithJsonAdapter;
    }

    public void setFieldWithJsonAdapter(String fieldWithJsonAdapter) {
      this.fieldWithJsonAdapter = fieldWithJsonAdapter;
    }
  }

  private static class MockAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  }
}