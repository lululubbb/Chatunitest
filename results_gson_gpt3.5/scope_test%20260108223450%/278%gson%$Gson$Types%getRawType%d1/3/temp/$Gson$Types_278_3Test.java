package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Map;

class GsonTypesGetRawTypeTest {

  @Test
    @Timeout(8000)
  void testGetRawType_withClass() {
    Class<String> clazz = String.class;
    Class<?> result = $Gson$Types.getRawType(clazz);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() throws NoSuchMethodException {
    // Create a ParameterizedType for java.util.Map<String, Integer>
    Method method = Sample.class.getDeclaredMethod("mapMethod");
    Type returnType = method.getGenericReturnType();
    assertTrue(returnType instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) returnType;

    Class<?> result = $Gson$Types.getRawType(pt);
    assertSame(Map.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withGenericArrayType() throws NoSuchFieldException {
    // Field genericArrayField is of type T[]
    Field field = Sample.class.getDeclaredField("genericArrayField");
    Type fieldType = field.getGenericType();
    assertTrue(fieldType instanceof GenericArrayType);

    Class<?> result = $Gson$Types.getRawType(fieldType);
    assertTrue(result.isArray());
    // Since T extends String, raw type is Object[] because getRawType returns Object.class for TypeVariable
    assertEquals(Object[].class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withTypeVariable() throws NoSuchFieldException {
    TypeVariable<?>[] typeParameters = Sample.class.getTypeParameters();
    assertTrue(typeParameters.length > 0);

    Class<?> result = $Gson$Types.getRawType(typeParameters[0]);
    // getRawType returns Object.class for TypeVariable
    assertSame(Object.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withWildcardType() throws NoSuchFieldException {
    Field field = Sample.class.getDeclaredField("wildcardField");
    Type fieldType = field.getGenericType();
    assertTrue(fieldType instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) fieldType;
    Type[] args = pt.getActualTypeArguments();
    assertEquals(1, args.length);
    Type wildcardType = args[0];
    assertTrue(wildcardType instanceof WildcardType);

    Class<?> result = $Gson$Types.getRawType(wildcardType);
    assertSame(Number.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withNull_throws() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(null);
    });
    assertTrue(thrown.getMessage().contains("null"));
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withUnknownType_throws() {
    Type unknownType = new Type() {};
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(unknownType);
    });
    assertTrue(thrown.getMessage().contains(unknownType.getClass().getName()));
  }

  // Helper class with generic members to create types for testing
  static class Sample<T extends String> {
    T[] genericArrayField;
    Map<String, Integer> mapMethod() { return null; }
    java.util.List<? extends Number> wildcardField;

    <U> void genericMethod(U param) {}
  }
}