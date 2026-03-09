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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

final class TypeAdapterRuntimeTypeWrapper_180_1Test {

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withNullValue_returnsOriginalType() throws Exception {
    Type originalType = String.class;
    Object value = null;

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, originalType, value);

    assertSame(originalType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withNonClassOrTypeVariableType_returnsOriginalType() throws Exception {
    Type customType = mock(Type.class);
    Object value = "string";

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, customType, value);

    assertSame(customType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withClassTypeAndNonNullValue_returnsValueClass() throws Exception {
    Type originalType = Object.class;
    Object value = "string";

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, originalType, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_withTypeVariableTypeAndNonNullValue_returnsValueClass() throws Exception {
    @SuppressWarnings("rawtypes")
    TypeVariable<Class<TypeAdapterRuntimeTypeWrapper>> typeVariable = TypeAdapterRuntimeTypeWrapper.class.getTypeParameters()[0];
    Object value = "string";

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, typeVariable, value);

    assertEquals(value.getClass(), result);
  }
}