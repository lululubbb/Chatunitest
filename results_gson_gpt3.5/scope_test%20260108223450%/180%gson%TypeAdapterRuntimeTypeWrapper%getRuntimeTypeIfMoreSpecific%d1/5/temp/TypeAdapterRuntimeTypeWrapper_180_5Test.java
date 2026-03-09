package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_180_5Test {

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_valueNull_returnsOriginalType() throws Exception {
    Type originalType = String.class;
    Object value = null;

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertEquals(originalType, result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeIsClass_valueNotNull_returnsValueClass() throws Exception {
    Type originalType = Object.class;
    Object value = "test string";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeIsTypeVariable_valueNotNull_returnsValueClass() throws Exception {
    TypeVariable<Class<TypeAdapterRuntimeTypeWrapper>>[] typeParams = TypeAdapterRuntimeTypeWrapper.class.getTypeParameters();
    TypeVariable<?> typeVariable = typeParams.length > 0 ? typeParams[0] : null;
    Object value = "another string";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(typeVariable, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void getRuntimeTypeIfMoreSpecific_typeNotClassOrTypeVariable_valueNotNull_returnsOriginalType() throws Exception {
    Type originalType = new Type() {
      @Override
      public String getTypeName() {
        return "anonymous";
      }
    };
    Object value = "value";

    Type result = invokeGetRuntimeTypeIfMoreSpecific(originalType, value);

    assertEquals(originalType, result);
  }

  private static Type invokeGetRuntimeTypeIfMoreSpecific(Type type, Object value) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, type, value);
  }
}