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
import java.lang.reflect.Field;

class TypeAdapterRuntimeTypeWrapper_180_2Test {

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withValueNull_returnsOriginalType() throws Exception {
    Type originalType = String.class;
    Object value = null;

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, originalType, value);

    assertSame(originalType, result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withTypeClassAndValueNotNull_returnsValueClass() throws Exception {
    Type originalType = CharSequence.class; // Class<?>
    String value = "test";

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, originalType, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withTypeTypeVariableAndValueNotNull_returnsValueClass() throws Exception {
    // Create a dummy TypeVariable instance by reflection from a generic class
    class GenericClass<T> {
      T field;
    }
    Field field = GenericClass.class.getDeclaredField("field");
    Type genericType = field.getGenericType();
    assertTrue(genericType instanceof TypeVariable<?>);
    TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;

    Integer value = 123;

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, typeVariable, value);

    assertEquals(value.getClass(), result);
  }

  @Test
    @Timeout(8000)
  void testGetRuntimeTypeIfMoreSpecific_withTypeNotClassOrTypeVariableAndValueNotNull_returnsOriginalType() throws Exception {
    // Use a generic array type to simulate a Type that is not Class or TypeVariable
    Type originalType = new Type() {
      @Override
      public String getTypeName() {
        return "dummy";
      }
    };
    Object value = "value";

    Method method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, originalType, value);

    assertSame(originalType, result);
  }
}