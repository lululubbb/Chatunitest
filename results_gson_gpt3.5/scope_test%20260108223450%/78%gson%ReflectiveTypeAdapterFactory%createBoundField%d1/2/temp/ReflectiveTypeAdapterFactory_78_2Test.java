package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
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
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_78_2Test {

  ReflectiveTypeAdapterFactory factory;
  ConstructorConstructor constructorConstructor;
  FieldNamingStrategy fieldNamingStrategy;
  Excluder excluder;
  JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  List reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
  }

  private BoundField invokeCreateBoundField(
      Gson context, Field field, Method accessor, String name,
      TypeToken<?> fieldType, boolean serialize, boolean deserialize,
      boolean blockInaccessible) throws Exception {
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (BoundField) method.invoke(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
  }

  static class Dummy {
    public int intField = 42;
    public String stringField = "test";

    public int getIntField() {
      return intField;
    }

    public String getStringField() {
      return stringField;
    }
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_withJsonAdapterAnnotation() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("intField");
    Method accessor = Dummy.class.getMethod("getIntField");
    TypeToken<Integer> typeToken = TypeToken.get(int.class);
    JsonAdapter annotation = mock(JsonAdapter.class);

    // Setup jsonAdapterFactory to return a mocked adapter when annotation is present
    TypeAdapter<?> mockedAdapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(constructorConstructor, gson, typeToken, annotation)).thenReturn(mockedAdapter);

    // Spy field to return annotation
    Field spyField = spy(field);
    doReturn(annotation).when(spyField).getAnnotation(JsonAdapter.class);

    BoundField boundField = invokeCreateBoundField(gson, spyField, accessor, "intField", typeToken, true, true, true);
    assertNotNull(boundField);

    Dummy dummy = new Dummy();
    JsonWriter writer = mock(JsonWriter.class);

    // Write should call adapter.write with field value
    doNothing().when((TypeAdapter<Object>) mockedAdapter).write(any(JsonWriter.class), any());
    boundField.write(writer, dummy);
    verify((TypeAdapter<Object>) mockedAdapter).write(eq(writer), eq(dummy.getIntField()));

    // readIntoField should set field value
    JsonReader reader = mock(JsonReader.class);
    when(((TypeAdapter<Object>) mockedAdapter).read(reader)).thenReturn(123);
    boundField.readIntoField(reader, dummy);
    assertEquals(123, dummy.intField);
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_withoutJsonAdapterAnnotation() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("stringField");
    Method accessor = Dummy.class.getMethod("getStringField");
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Setup jsonAdapterFactory to return null when no annotation
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);

    // Setup gson to return a mocked adapter
    TypeAdapter<String> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "stringField", typeToken, true, true, false);
    assertNotNull(boundField);

    Dummy dummy = new Dummy();
    JsonWriter writer = mock(JsonWriter.class);

    doNothing().when(adapter).write(any(JsonWriter.class), any());
    boundField.write(writer, dummy);
    verify(adapter).write(eq(writer), eq(dummy.stringField));

    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn("changed");
    boundField.readIntoField(reader, dummy);
    assertEquals("changed", dummy.stringField);
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_write_skipsWhenNotSerialized() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("intField");
    Method accessor = Dummy.class.getMethod("getIntField");
    TypeToken<Integer> typeToken = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "intField", typeToken, false, true, false);
    Dummy dummy = new Dummy();
    JsonWriter writer = mock(JsonWriter.class);

    boundField.write(writer, dummy);
    verify(adapter, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_readIntoArray_primitiveNullThrows() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("intField");
    Method accessor = Dummy.class.getMethod("getIntField");
    TypeToken<Integer> typeToken = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "intField", typeToken, true, true, false);
    JsonReader reader = mock(JsonReader.class);

    when(adapter.read(reader)).thenReturn(null);
    Object[] target = new Object[1];

    JsonParseException ex = assertThrows(JsonParseException.class, () -> boundField.readIntoArray(reader, 0, target));
    assertTrue(ex.getMessage().contains("null is not allowed as value for record component"));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_readIntoField_staticFinal_throws() throws Exception {
    Gson gson = mock(Gson.class);
    // Create a static final field dynamically via a helper class
    class StaticFinal {
      static final int CONST = 10;
    }
    Field field = StaticFinal.class.getDeclaredField("CONST");
    Method accessor = null;
    TypeToken<Integer> typeToken = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "CONST", typeToken, true, true, false);
    JsonReader reader = mock(JsonReader.class);
    when(adapter.read(reader)).thenReturn(5);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> boundField.readIntoField(reader, new StaticFinal()));
    assertTrue(ex.getMessage().contains("Cannot set value of 'static final'"));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_write_accessorThrowsJsonIOException() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("intField");
    Method accessor = Dummy.class.getMethod("getIntField");
    TypeToken<Integer> typeToken = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "intField", typeToken, true, true, true);
    Dummy dummy = new Dummy();
    JsonWriter writer = mock(JsonWriter.class);

    // Make accessor.invoke throw InvocationTargetException with cause
    InvocationTargetException ite = new InvocationTargetException(new RuntimeException("cause"));
    Method spyAccessor = spy(accessor);
    doThrow(ite).when(spyAccessor).invoke(dummy);

    // Replace boundField with one created with spyAccessor via reflection
    BoundField boundFieldWithSpyAccessor = invokeCreateBoundField(gson, field, spyAccessor, "intField", typeToken, true, true, true);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> boundFieldWithSpyAccessor.write(writer, dummy));
    assertTrue(ex.getMessage().contains("Accessor"));
    assertEquals("cause", ex.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_write_fieldValueEqualsSource_skipsWriting() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = Dummy.class.getField("intField");
    Method accessor = null;
    TypeToken<Integer> typeToken = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    TypeAdapter<Integer> adapter = mock(TypeAdapter.class);
    when(gson.getAdapter(typeToken)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "intField", typeToken, true, true, false);
    Dummy dummy = new Dummy();

    // Spy field.get to return the source object itself to simulate recursion avoidance
    Field spyField = spy(field);
    doReturn(dummy).when(spyField).get(dummy);

    BoundField boundFieldWithSpyField = invokeCreateBoundField(gson, dummy.getClass().getDeclaredField("intField"), null, "intField", typeToken, true, true, false);
    JsonWriter writer = mock(JsonWriter.class);

    boundFieldWithSpyField.write(writer, dummy);
    verify(adapter, never()).write(any(), any());
  }
}