package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_180_3Test {

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_valueIsNull_returnsOriginalType() throws Exception {
    Type originalType = String.class;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, null);

    assertSame(originalType, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_typeIsClass_valueNotNull_returnsValueClass() throws Exception {
    Type originalType = Object.class;
    Object value = "a string";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_typeIsTypeVariable_valueNotNull_returnsValueClass() throws Exception {
    // Create a mock TypeVariable
    @SuppressWarnings("unchecked")
    TypeVariable<Class<String>> typeVariable = mock(TypeVariable.class);

    Object value = 123;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(typeVariable, value);

    assertEquals(Integer.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_typeIsOther_returnsOriginalType() throws Exception {
    // Create a mock Type that is neither Class nor TypeVariable
    Type otherType = mock(Type.class);

    Object value = "value";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(otherType, value);

    assertSame(otherType, result);
  }

  private Type invokeGetRuntimeTypeIfMoreSpecific(Type type, Object value) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, type, value);
  }
}