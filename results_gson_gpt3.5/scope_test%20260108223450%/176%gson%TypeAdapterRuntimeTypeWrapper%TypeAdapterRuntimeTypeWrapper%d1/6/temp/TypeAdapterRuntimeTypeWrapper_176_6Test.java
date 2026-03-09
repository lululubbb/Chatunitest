package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeAdapterRuntimeTypeWrapper_176_6Test {

  Gson mockGson;
  TypeAdapter<Object> mockDelegate;
  Type mockType;
  TypeAdapterRuntimeTypeWrapper<Object> wrapper;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockDelegate = mock(TypeAdapter.class);
    mockType = Object.class;
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, mockType);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegate() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    Object expected = new Object();
    when(mockDelegate.read(mockReader)).thenReturn(expected);

    Object actual = wrapper.read(mockReader);

    verify(mockDelegate).read(mockReader);
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  void write_withNullValue_delegatesToDelegate() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);

    wrapper.write(mockWriter, null);

    verify(mockDelegate).write(mockWriter, null);
  }

  @Test
    @Timeout(8000)
  void write_withValue_runtimeTypeMoreSpecificAndReflectiveUsesDelegate() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    Object value = new String("test");

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegate = mock(TypeAdapter.class);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, delegate, Object.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> runtimeTypeAdapter = mock(TypeAdapter.class);

    when(mockGson.<String>getAdapter(String.class)).thenReturn(runtimeTypeAdapter);

    // To simulate isReflective(delegate) == true, we override toString of delegate to contain "ReflectiveTypeAdapter"
    when(delegate.toString()).thenReturn("ReflectiveTypeAdapter");

    // runtimeTypeAdapter toString returns non-reflective to ensure delegate path
    when(runtimeTypeAdapter.toString()).thenReturn("NonReflectiveAdapter");

    spyWrapper.write(mockWriter, value);

    verify(mockGson).getAdapter(String.class);

    verify(delegate).write(mockWriter, value);
    verify(runtimeTypeAdapter, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void write_withValue_runtimeTypeMoreSpecificAndNotReflectiveUsesRuntimeTypeAdapter() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    Object value = new String("test");

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegate = mock(TypeAdapter.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> runtimeTypeAdapter = mock(TypeAdapter.class);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, delegate, Object.class);

    // Use a cast to TypeAdapter<?> to avoid generic mismatch in when()
    when(mockGson.<String>getAdapter(String.class)).thenReturn((TypeAdapter) runtimeTypeAdapter);

    // To simulate isReflective(delegate) == false and isReflective(runtimeTypeAdapter) == false,
    // override toString accordingly
    when(delegate.toString()).thenReturn("NonReflectiveAdapter");
    when(runtimeTypeAdapter.toString()).thenReturn("NonReflectiveAdapter");

    spyWrapper.write(mockWriter, value);

    verify(runtimeTypeAdapter).write(mockWriter, value);
    verify(delegate, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void isReflective_returnsTrueForReflectiveAdapter() throws Exception {
    // Create a dummy TypeAdapter subclass to simulate reflective adapter
    TypeAdapter<Object> reflectiveAdapter = new TypeAdapter<Object>() {
      @Override
      public Object read(JsonReader in) { return null; }
      @Override
      public void write(JsonWriter out, Object value) {}
      @Override
      public String toString() { return "ReflectiveTypeAdapter"; }
    };

    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    Boolean result = (Boolean) method.invoke(null, reflectiveAdapter);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_returnsFalseForNonReflectiveAdapter() throws Exception {
    TypeAdapter<Object> nonReflectiveAdapter = mock(TypeAdapter.class);
    when(nonReflectiveAdapter.toString()).thenReturn("NonReflectiveAdapter");

    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    Boolean result = (Boolean) method.invoke(null, nonReflectiveAdapter);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsMoreSpecificType() throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type baseType = Object.class;
    String value = "string";

    Type result = (Type) method.invoke(null, baseType, value);
    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsOriginalTypeWhenValueNull() throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type baseType = Object.class;
    Object value = null;

    Type result = (Type) method.invoke(null, baseType, value);
    assertEquals(baseType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsOriginalTypeWhenTypeVariable() throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    TypeVariable<Class<TypeAdapterRuntimeTypeWrapper>>[] typeParameters = TypeAdapterRuntimeTypeWrapper.class.getTypeParameters();
    TypeVariable<?> typeVariable = typeParameters.length > 0 ? typeParameters[0] : null;

    if (typeVariable != null) {
      Type result = (Type) method.invoke(null, typeVariable, "string");
      assertEquals(typeVariable, result);
    }
  }
}