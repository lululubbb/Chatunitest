package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class TypeToken_141_2Test {

  @Test
    @Timeout(8000)
  void testGetArray_withConcreteType() {
    Type componentType = String.class;
    Type arrayType = $Gson$Types.arrayOf(componentType);
    TypeToken<?> result = TypeToken.getArray(componentType);
    assertNotNull(result);
    assertEquals(arrayType, result.getType());
  }

  @Test
    @Timeout(8000)
  void testGetArray_withParameterizedType() {
    // Create a real ParameterizedType instance instead of mock
    ParameterizedType componentType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return java.util.List.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Type arrayType = $Gson$Types.arrayOf(componentType);
    TypeToken<?> result = TypeToken.getArray(componentType);
    assertNotNull(result);
    assertEquals(arrayType, result.getType());
  }

  @Test
    @Timeout(8000)
  void testGetArray_withGenericArrayType() {
    // Create a real GenericArrayType instance instead of mock
    GenericArrayType componentType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
    };
    Type arrayType = $Gson$Types.arrayOf(componentType);
    TypeToken<?> result = TypeToken.getArray(componentType);
    assertNotNull(result);
    assertEquals(arrayType, result.getType());
  }
}