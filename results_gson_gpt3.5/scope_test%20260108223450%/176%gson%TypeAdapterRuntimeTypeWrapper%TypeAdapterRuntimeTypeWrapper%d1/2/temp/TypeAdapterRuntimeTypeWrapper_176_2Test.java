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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_176_2Test {

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
    JsonReader reader = mock(JsonReader.class);
    Object expected = new Object();
    when(mockDelegate.read(reader)).thenReturn(expected);

    Object actual = wrapper.read(reader);

    verify(mockDelegate).read(reader);
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  void write_valueNull_delegatesToDelegate() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    wrapper.write(writer, null);

    verify(mockDelegate).write(writer, null);
  }

  @Test
    @Timeout(8000)
  void write_valueWithMoreSpecificRuntimeType_usesRuntimeTypeAdapter() throws Exception {
    JsonWriter writer = mock(JsonWriter.class);
    Object value = "stringValue"; // runtime type is String.class, more specific than Object.class

    // Prepare a mock TypeAdapter for String.class
    @SuppressWarnings("unchecked")
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);

    // Setup Gson to return the stringAdapter when getAdapter(String.class) is called
    when(mockGson.getAdapter(String.class)).thenReturn(stringAdapter);
    // Also setup Gson to return the delegate adapter for Object.class to avoid NPE if called
    when(mockGson.getAdapter(Object.class)).thenReturn(mockDelegate);

    // The delegate's type is Object.class, value is String, so runtime type is more specific
    // So the write method should call stringAdapter.write(writer, value)
    wrapper.write(writer, value);

    // Verify delegate.write is never called
    verify(mockDelegate, never()).write(any(), any());
    // Verify stringAdapter.write is called with correct parameters
    // Cast value to String to match method signature
    verify(stringAdapter).write(eq(writer), eq((String) value));
  }

  @Test
    @Timeout(8000)
  void write_valueWithSameRuntimeType_usesDelegate() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Object value = new Object();

    // Setup Gson to return the delegate adapter for Object.class
    when(mockGson.getAdapter(Object.class)).thenReturn(mockDelegate);

    wrapper.write(writer, value);

    verify(mockDelegate).write(writer, value);
  }

  @Test
    @Timeout(8000)
  void isReflective_returnsTrueForReflectiveTypeAdapter() throws Exception {
    // Use reflection to get private static method isReflective
    Method isReflectiveMethod = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    isReflectiveMethod.setAccessible(true);

    // Create a mock TypeAdapter and override toString to simulate reflective
    TypeAdapter<?> reflectiveAdapter = mock(TypeAdapter.class);
    when(reflectiveAdapter.toString()).thenReturn("com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter");

    boolean result = (boolean) isReflectiveMethod.invoke(null, reflectiveAdapter);
    assertTrue(result);

    // Non-reflective adapter
    TypeAdapter<?> nonReflectiveAdapter = mock(TypeAdapter.class);
    when(nonReflectiveAdapter.toString()).thenReturn("SomeOtherAdapter");

    boolean result2 = (boolean) isReflectiveMethod.invoke(null, nonReflectiveAdapter);
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsValueTypeIfMoreSpecific() throws Exception {
    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type baseType = Object.class;
    String value = "test";

    Type result = (Type) method.invoke(null, baseType, value);
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsOriginalTypeIfNullValue() throws Exception {
    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type baseType = Object.class;
    Object value = null;

    Type result = (Type) method.invoke(null, baseType, value);
    assertEquals(baseType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_returnsOriginalTypeIfTypeVariable() throws Exception {
    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    final TypeVariable<?> typeVariable;
    if (Object.class.getTypeParameters().length > 0) {
      typeVariable = Object.class.getTypeParameters()[0];
    } else {
      // fallback: create dummy TypeVariable via proxy to avoid abstract method errors
      typeVariable = java.lang.reflect.Proxy.newProxyInstance(
          TypeVariable.class.getClassLoader(),
          new Class<?>[] { TypeVariable.class },
          (proxy, method1, args) -> {
            switch (method1.getName()) {
              case "getGenericDeclaration": return Object.class;
              case "getName": return "T";
              case "getBounds": return new Type[] { Object.class };
              case "getAnnotatedBounds": return new java.lang.reflect.AnnotatedType[0];
              case "getAnnotations": return new java.lang.annotation.Annotation[0];
              case "getAnnotation": return null;
              case "equals": return proxy == args[0];
              case "hashCode": return System.identityHashCode(proxy);
              case "toString": return "T";
              default: throw new UnsupportedOperationException("Method not implemented: " + method1.getName());
            }
          });
    }

    Object value = "value";

    Type result = (Type) method.invoke(null, typeVariable, value);
    assertEquals(typeVariable, result);
  }
}