package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

class $Gson$Types_284_3Test {

  private static class TestGenericArrayType implements GenericArrayType {
    private final Type componentType;

    TestGenericArrayType(Type componentType) {
      this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
      return componentType;
    }
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_withGenericArrayType_returnsComponentType() {
    Type componentType = String.class;
    GenericArrayType genericArrayType = new TestGenericArrayType(componentType);

    Type result = $Gson$Types.getArrayComponentType(genericArrayType);

    assertSame(componentType, result);
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_withClassArray_returnsComponentType() {
    Type arrayType = String[].class;

    Type result = $Gson$Types.getArrayComponentType(arrayType);

    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_withNonArrayClass_returnsNull() {
    Type nonArrayType = String.class;

    Type result = $Gson$Types.getArrayComponentType(nonArrayType);

    assertNull(result);
  }
}