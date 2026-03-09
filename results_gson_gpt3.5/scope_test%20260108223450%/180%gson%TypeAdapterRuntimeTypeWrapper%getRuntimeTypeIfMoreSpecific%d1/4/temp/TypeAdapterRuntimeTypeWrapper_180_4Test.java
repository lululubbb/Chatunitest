package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

class TypeAdapterRuntimeTypeWrapper_180_4Test<T> {

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_valueNull_returnsOriginalType() throws Exception {
    Type originalType = String.class;
    Object value = null;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertSame(originalType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeIsClass_valueNonNull_returnsValueClass() throws Exception {
    Type originalType = Object.class;
    Object value = "testString";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeIsTypeVariable_valueNonNull_returnsValueClass() throws Exception {
    TypeVariable<?> typeVariable = TypeAdapterRuntimeTypeWrapperTest.class.getTypeParameters()[0];
    Object value = 123;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(typeVariable, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeIsOtherType_valueNonNull_returnsOriginalType() throws Exception {
    Type originalType = new Type() {};
    Object value = 456;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertSame(originalType, result);
  }

  private Type invokeGetRuntimeTypeIfMoreSpecific(Type type, Object value) throws Exception {
    Class<?> clazz = Class.forName("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper");
    var method = clazz.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    // getRuntimeTypeIfMoreSpecific is private static, so invoke with null instance
    return (Type) method.invoke(null, type, value);
  }
}