package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class TypeAdapterRuntimeTypeWrapper_180_6Test {

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withNullValue_returnsOriginalType() throws Exception {
    Type originalType = String.class;
    Object value = null;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertSame(originalType, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withNonClassOrTypeVariableType_returnsOriginalType() throws Exception {
    // Create a Type implementation that is neither Class nor TypeVariable
    Type customType = new Type() {};
    Object value = "string";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(customType, value);

    assertSame(customType, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withClassTypeAndNonNullValue_returnsValueClass() throws Exception {
    Type originalType = CharSequence.class;
    Object value = "string";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withTypeVariableAndNonNullValue_returnsValueClass() throws Exception {
    // Create a dummy TypeVariable instance by reflection
    TypeVariable<?> typeVariable = DummyClass.class.getTypeParameters()[0];
    Object value = 123;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(typeVariable, value);

    assertSame(Integer.class, result);
  }

  // Helper method to invoke private static getRuntimeTypeIfMoreSpecific via reflection
  private static Type invokeGetRuntimeTypeIfMoreSpecific(Type type, Object value) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, type, value);
  }

  // Dummy generic class to obtain a TypeVariable instance
  private static class DummyClass<T> {}
}