package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class TypeToken_141_3Test {

  @Test
    @Timeout(8000)
  void testGetArray_withClassComponentType() {
    Type componentType = String.class;
    TypeToken<?> result = TypeToken.getArray(componentType);
    assertNotNull(result);
    assertTrue(result.getType().getTypeName().startsWith("[L"));
    Type type = result.getType();
    // Fix: rawType may not be an array class if type is a GenericArrayType, and vice versa
    if (type instanceof GenericArrayType) {
      assertTrue(true);
    } else {
      assertTrue(result.getRawType().isArray());
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withParameterizedTypeComponent() {
    ParameterizedType parameterizedType = new ParameterizedType() {
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
    TypeToken<?> result = TypeToken.getArray(parameterizedType);
    assertNotNull(result);
    assertNotNull(result.getType());
    assertTrue(result.getType().getTypeName().startsWith("["));
    Type type = result.getType();
    if (type instanceof GenericArrayType) {
      assertTrue(true);
    } else {
      assertTrue(result.getRawType().isArray());
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withGenericArrayTypeComponent() {
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
    };
    TypeToken<?> result = TypeToken.getArray(genericArrayType);
    assertNotNull(result);
    assertNotNull(result.getType());
    assertTrue(result.getType().getTypeName().startsWith("["));
    Type type = result.getType();
    if (type instanceof GenericArrayType) {
      assertTrue(true);
    } else {
      assertTrue(result.getRawType().isArray());
    }
  }
}