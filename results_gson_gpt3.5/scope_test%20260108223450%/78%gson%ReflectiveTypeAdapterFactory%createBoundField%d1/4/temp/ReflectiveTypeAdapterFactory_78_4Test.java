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
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class ReflectiveTypeAdapterFactory_78_4Test {

  private ReflectiveTypeAdapterFactory factory;

  @Mock
  private ConstructorConstructor constructorConstructor;
  @Mock
  private FieldNamingStrategy fieldNamingStrategy;
  @Mock
  private Excluder excluder;
  @Mock
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  @Mock
  private Gson gson;
  @Mock
  private TypeAdapter<?> typeAdapter;
  @Mock
  private TypeAdapter<Object> typeAdapterObject;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        Collections.emptyList()
    );
  }

  private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
    Field field = clazz.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  private Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException {
    Method method = clazz.getDeclaredMethod(name);
    method.setAccessible(true);
    return method;
  }

  private BoundField invokeCreateBoundField(
      Gson context, Field field, Method accessor, String name,
      TypeToken<?> fieldType, boolean serialize, boolean deserialize,
      boolean blockInaccessible) throws Exception {

    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (BoundField) method.invoke(factory, context, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
  }

  private static class Sample {
    public String publicField = "value";
    private int privateField = 42;
    public static final int staticFinalField = 99;

    public String getFieldValue() {
      return publicField;
    }

    @JsonAdapter(DummyAdapter.class)
    public String annotatedField = "annotated";

    public String getAnnotatedField() {
      return annotatedField;
    }
  }

  private static class DummyAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value("dummy:" + value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return "dummyRead";
    }
  }

  @Test
    @Timeout(8000)
  void createBoundField_withNullAccessor_andNoJsonAdapter() throws Exception {
    Field field = getField(Sample.class, "publicField");
    TypeToken<String> fieldType = TypeToken.get(String.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);
    when(typeAdapterObject.read(any())).thenReturn("readValue");
    doNothing().when(typeAdapterObject).write(any(), any());

    BoundField boundField = invokeCreateBoundField(gson, field, null, "publicField",
        fieldType, true, true, true);

    assertNotNull(boundField);

    // Test write method
    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, new Sample());
    verify(writer).name("publicField");
    verify(typeAdapterObject).write(eq(writer), eq("value"));

    // Test readIntoField method
    Sample target = new Sample();
    JsonReader reader = mock(JsonReader.class);
    boundField.readIntoField(reader, target);
    assertEquals("readValue", target.publicField);

    // Test readIntoArray method
    Object[] targetArray = new Object[1];
    boundField.readIntoArray(mock(JsonReader.class), 0, targetArray);
    assertEquals("readValue", targetArray[0]);
  }

  @Test
    @Timeout(8000)
  void createBoundField_withAccessor_andJsonAdapterPresent() throws Exception {
    Field field = getField(Sample.class, "annotatedField");
    Method accessor = getMethod(Sample.class, "getAnnotatedField");
    TypeToken<String> fieldType = TypeToken.get(String.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(typeAdapterObject);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);
    when(typeAdapterObject.read(any())).thenReturn("adapterRead");
    doNothing().when(typeAdapterObject).write(any(), any());

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "annotatedField",
        fieldType, true, true, true);

    assertNotNull(boundField);

    // Test write method uses accessor.invoke and jsonAdapterPresent true
    JsonWriter writer = mock(JsonWriter.class);
    Sample source = new Sample();
    boundField.write(writer, source);
    verify(writer).name("annotatedField");
    verify(typeAdapterObject).write(eq(writer), eq("annotated"));

    // Test readIntoField method sets field
    Sample target = new Sample();
    JsonReader reader = mock(JsonReader.class);
    boundField.readIntoField(reader, target);
    assertEquals("adapterRead", target.annotatedField);

    // Test readIntoArray method
    Object[] targetArray = new Object[1];
    boundField.readIntoArray(mock(JsonReader.class), 0, targetArray);
    assertEquals("adapterRead", targetArray[0]);
  }

  @Test
    @Timeout(8000)
  void createBoundField_write_skipsIfNotSerialized() throws Exception {
    Field field = getField(Sample.class, "publicField");
    TypeToken<String> fieldType = TypeToken.get(String.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);

    BoundField boundField = invokeCreateBoundField(gson, field, null, "publicField",
        fieldType, false, true, true);

    JsonWriter writer = mock(JsonWriter.class);
    boundField.write(writer, new Sample());
    verify(writer, never()).name(any());
    verify(typeAdapterObject, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoField_throwsOnStaticFinalField() throws Exception {
    Field field = getField(Sample.class, "staticFinalField");
    TypeToken<Integer> fieldType = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);
    when(typeAdapterObject.read(any())).thenReturn(123);

    BoundField boundField = invokeCreateBoundField(gson, field, null, "staticFinalField",
        fieldType, true, true, false);

    Sample target = new Sample();
    JsonReader reader = mock(JsonReader.class);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> boundField.readIntoField(reader, target));
    assertTrue(ex.getMessage().contains("static final"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoArray_throwsOnNullPrimitive() throws Exception {
    Field field = getField(Sample.class, "privateField");
    TypeToken<Integer> fieldType = TypeToken.get(int.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);
    when(typeAdapterObject.read(any())).thenReturn(null);

    BoundField boundField = invokeCreateBoundField(gson, field, null, "privateField",
        fieldType, true, true, false);

    Object[] targetArray = new Object[1];
    JsonReader reader = mock(JsonReader.class);

    assertThrows(com.google.gson.JsonParseException.class, () -> boundField.readIntoArray(reader, 0, targetArray));
  }

  @Test
    @Timeout(8000)
  void createBoundField_write_accessorThrows_invokesJsonIOException() throws Exception {
    Field field = getField(Sample.class, "publicField");
    Method accessor = mock(Method.class);
    TypeToken<String> fieldType = TypeToken.get(String.class);

    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(typeAdapterObject);
    when(accessor.invoke(any())).thenThrow(new java.lang.reflect.InvocationTargetException(new RuntimeException("fail")));
    BoundField boundField = invokeCreateBoundField(gson, field, accessor, "publicField",
        fieldType, true, true, true);

    JsonWriter writer = mock(JsonWriter.class);
    Sample source = new Sample();

    com.google.gson.JsonIOException ex = assertThrows(com.google.gson.JsonIOException.class,
        () -> boundField.write(writer, source));
    assertTrue(ex.getMessage().contains("Accessor"));
    assertTrue(ex.getCause() instanceof RuntimeException);
  }
}