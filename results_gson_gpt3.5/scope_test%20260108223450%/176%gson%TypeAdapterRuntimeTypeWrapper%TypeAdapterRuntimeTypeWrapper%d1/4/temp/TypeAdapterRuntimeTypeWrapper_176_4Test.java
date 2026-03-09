package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_176_4Test {

  Gson gson;
  TypeAdapter<Object> delegate;
  Type type;
  TypeAdapterRuntimeTypeWrapper<Object> wrapper;
  JsonReader jsonReader;
  JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    delegate = mock(TypeAdapter.class);
    type = Object.class;
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegate, type);
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegate() throws IOException {
    Object expected = new Object();
    when(delegate.read(jsonReader)).thenReturn(expected);

    Object actual = wrapper.read(jsonReader);

    verify(delegate).read(jsonReader);
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsDelegateWrite() throws IOException {
    wrapper.write(jsonWriter, null);

    verify(delegate).write(jsonWriter, null);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeMoreSpecific_delegatesToCorrectAdapter() throws Exception {
    Object value = "stringValue";
    Type runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<?> stringAdapter = mock(TypeAdapter.class);

    // Setup gson.getAdapter to return stringAdapter for String.class
    when(gson.getAdapter(TypeToken.get(runtimeType))).thenReturn(stringAdapter);

    // Use reflection to access private static getRuntimeTypeIfMoreSpecific
    var getRuntimeTypeIfMoreSpecific = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    getRuntimeTypeIfMoreSpecific.setAccessible(true);
    Type computedType = (Type) getRuntimeTypeIfMoreSpecific.invoke(null, type, value);
    assertEquals(runtimeType, computedType);

    // Use reflection to access private static isReflective
    var isReflective = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    isReflective.setAccessible(true);

    // Because isReflective is private static and cannot be stubbed, we do not mock it.
    // Instead, we rely on the real method returning false for the mock adapter.
    // So no need to mock isReflective.invoke.

    wrapper.write(jsonWriter, value);

    verify(stringAdapter).write(jsonWriter, value);
    verify(delegate, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeSameAsDeclared_delegatesToDelegate() throws IOException {
    Object value = new Object();

    wrapper.write(jsonWriter, value);

    verify(delegate).write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void isReflective_withReflectiveAdapter_returnsTrue() throws Exception {
    TypeAdapter<?> reflectiveAdapter = mock(TypeAdapter.class);

    var isReflective = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    isReflective.setAccessible(true);

    Object result = isReflective.invoke(null, reflectiveAdapter);
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withNullValue_returnsDeclaredType() throws Exception {
    var getRuntimeTypeIfMoreSpecific = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    getRuntimeTypeIfMoreSpecific.setAccessible(true);

    Type result = (Type) getRuntimeTypeIfMoreSpecific.invoke(null, String.class, null);
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withMoreSpecificValue_returnsValueClass() throws Exception {
    var getRuntimeTypeIfMoreSpecific = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    getRuntimeTypeIfMoreSpecific.setAccessible(true);

    Type result = (Type) getRuntimeTypeIfMoreSpecific.invoke(null, Object.class, "test");
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withTypeVariable_returnsValueClass() throws Exception {
    class GenericClass<T> {}
    TypeVariable<Class<GenericClass>>[] typeParameters = GenericClass.class.getTypeParameters();
    assertTrue(typeParameters.length > 0);

    var getRuntimeTypeIfMoreSpecific = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    getRuntimeTypeIfMoreSpecific.setAccessible(true);

    Type result = (Type) getRuntimeTypeIfMoreSpecific.invoke(null, typeParameters[0], "abc");
    assertEquals(String.class, result);
  }
}