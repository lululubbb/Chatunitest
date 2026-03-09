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
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
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
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_78_5Test {

  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
  private ConstructorConstructor constructorConstructor;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private Gson gson;
  private List reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicyMock(), excluder,
        jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  private com.google.gson.FieldNamingStrategy fieldNamingPolicyMock() {
    return mock(com.google.gson.FieldNamingStrategy.class);
  }

  private Field getTestField(String name, Class<?> type, int modifiers) throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField(name);
    // Remove final modifier for testing static final field case
    setFieldModifiers(field, modifiers);
    return field;
  }

  private void setFieldModifiers(Field field, int modifiers) {
    try {
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, modifiers);
    } catch (Exception ignored) {
    }
  }

  @Test
    @Timeout(8000)
  void createBoundField_withJsonAdapterAnnotation_andAccessor_invokesAccessorAndWrites() throws Exception {
    Field field = getTestField("fieldWithJsonAdapter", String.class, Modifier.PUBLIC);
    Method accessor = TestClass.class.getDeclaredMethod("getFieldWithJsonAdapter");
    String name = "fieldWithJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<?> adapterFromJsonAdapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, field.getAnnotation(JsonAdapter.class)))
        .thenReturn(adapterFromJsonAdapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, true);

    // write method calls accessor.invoke and adapter's write
    TestClass instance = new TestClass();
    instance.fieldWithJsonAdapter = "value";
    JsonWriterMock writer = new JsonWriterMock();

    boundField.write(writer, instance);

    assertEquals(name, writer.nameCalled);
    assertTrue(writer.writeCalled);
  }

  @Test
    @Timeout(8000)
  void createBoundField_withoutJsonAdapterAnnotation_andNoAccessor_writesFieldValue() throws Exception {
    Field field = getTestField("normalField", String.class, Modifier.PUBLIC);
    Method accessor = null;
    String name = "normalField";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<?> adapterFromContext = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapterFromContext);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, false);

    TestClass instance = new TestClass();
    instance.normalField = "hello";

    JsonWriterMock writer = new JsonWriterMock();

    boundField.write(writer, instance);

    assertEquals(name, writer.nameCalled);
    assertTrue(writer.writeCalled);
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoField_setsFieldValue() throws Exception {
    Field field = getTestField("normalField", String.class, Modifier.PUBLIC);
    Method accessor = null;
    String name = "normalField";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, false);

    JsonReaderMock reader = new JsonReaderMock();
    String value = "newValue";
    when(adapter.read(reader)).thenReturn(value);

    TestClass instance = new TestClass();
    boundField.readIntoField(reader, instance);

    assertEquals(value, instance.normalField);
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoField_throwsWhenStaticFinalField() throws Exception {
    Field field = getTestField("STATIC_FINAL_FIELD", String.class, Modifier.STATIC | Modifier.FINAL);
    Method accessor = null;
    String name = "STATIC_FINAL_FIELD";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, false);

    JsonReaderMock reader = new JsonReaderMock();
    when(adapter.read(reader)).thenReturn("value");

    TestClass instance = new TestClass();

    JsonIOException ex = assertThrows(JsonIOException.class, () -> boundField.readIntoField(reader, instance));
    assertTrue(ex.getMessage().contains("Cannot set value of 'static final'"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_readIntoArray_throwsWhenNullForPrimitive() throws Exception {
    Field field = getTestField("primitiveInt", int.class, Modifier.PUBLIC);
    Method accessor = null;
    String name = "primitiveInt";
    TypeToken<?> fieldType = TypeToken.get(int.class);

    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, false);

    JsonReaderMock reader = new JsonReaderMock();
    when(adapter.read(reader)).thenReturn(null);

    Object[] target = new Object[1];
    JsonParseException ex = assertThrows(JsonParseException.class, () -> boundField.readIntoArray(reader, 0, target));
    assertTrue(ex.getMessage().contains("null is not allowed as value for record component"));
  }

  @Test
    @Timeout(8000)
  void createBoundField_write_returnsEarlyWhenSerializedFalse() throws Exception {
    Field field = getTestField("normalField", String.class, Modifier.PUBLIC);
    Method accessor = null;
    String name = "normalField";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, false, true, false);

    JsonWriterMock writer = new JsonWriterMock();
    TestClass instance = new TestClass();
    instance.normalField = "hello";

    boundField.write(writer, instance);

    assertFalse(writer.writeCalled);
  }

  @Test
    @Timeout(8000)
  void createBoundField_write_throwsJsonIOExceptionOnAccessorInvocationTargetException() throws Exception {
    Field field = getTestField("fieldWithJsonAdapter", String.class, Modifier.PUBLIC);
    Method accessor = mock(Method.class);
    when(accessor.invoke(any())).thenThrow(new InvocationTargetException(new RuntimeException("cause")));
    String name = "fieldWithJsonAdapter";
    TypeToken<?> fieldType = TypeToken.get(String.class);

    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(jsonAdapterFactory.getTypeAdapter(any(), any(), any(), any())).thenReturn(null);
    when(gson.getAdapter(fieldType)).thenReturn(adapter);

    BoundField boundField = invokeCreateBoundField(gson, field, accessor, name, fieldType, true, true, true);

    JsonWriterMock writer = new JsonWriterMock();
    TestClass instance = new TestClass();

    assertThrows(com.google.gson.JsonIOException.class, () -> boundField.write(writer, instance));
  }

  private BoundField invokeCreateBoundField(Gson context, Field field, Method accessor, String name,
      TypeToken<?> fieldType, boolean serialize, boolean deserialize, boolean blockInaccessible) throws Exception {
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("createBoundField",
        Gson.class, Field.class, Method.class, String.class, TypeToken.class,
        boolean.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (BoundField) method.invoke(reflectiveTypeAdapterFactory, context, field, accessor, name,
        fieldType, serialize, deserialize, blockInaccessible);
  }

  private static class TestClass {
    public String normalField;
    @JsonAdapter(TestJsonAdapter.class)
    public String fieldWithJsonAdapter;
    public static final String STATIC_FINAL_FIELD = "staticFinal";
    public int primitiveInt;

    public String getFieldWithJsonAdapter() {
      return fieldWithJsonAdapter;
    }
  }

  private static class TestJsonAdapter extends TypeAdapter<String> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, String value) throws IOException {
    }

    @Override
    public String read(com.google.gson.stream.JsonReader in) throws IOException {
      return null;
    }
  }

  private static class JsonWriterMock extends com.google.gson.stream.JsonWriter {
    String nameCalled = null;
    boolean writeCalled = false;

    JsonWriterMock() {
      super(new java.io.OutputStreamWriter(new java.io.ByteArrayOutputStream()));
    }

    @Override
    public com.google.gson.stream.JsonWriter name(String name) throws IOException {
      this.nameCalled = name;
      return this;
    }

    @Override
    public void write(String value) throws IOException {
      writeCalled = true;
    }
  }

  private static class JsonReaderMock extends com.google.gson.stream.JsonReader {
    JsonReaderMock() {
      super(new java.io.StringReader(""));
    }
  }
}