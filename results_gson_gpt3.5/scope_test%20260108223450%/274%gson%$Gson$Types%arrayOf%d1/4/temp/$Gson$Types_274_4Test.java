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
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class $Gson$Types_274_4Test {

  @Test
    @Timeout(8000)
  void arrayOf_shouldReturnGenericArrayTypeWithComponentType() {
    Type componentType = String.class;

    GenericArrayType result = $Gson$Types.arrayOf(componentType);

    assertNotNull(result);
    assertEquals(componentType, result.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void arrayOf_shouldReturnDifferentInstancesForDifferentComponentTypes() {
    Type componentType1 = Integer.class;
    Type componentType2 = Double.class;

    GenericArrayType result1 = $Gson$Types.arrayOf(componentType1);
    GenericArrayType result2 = $Gson$Types.arrayOf(componentType2);

    assertNotSame(result1, result2);
    assertEquals(componentType1, result1.getGenericComponentType());
    assertEquals(componentType2, result2.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void arrayOf_shouldReturnSameComponentTypeWhenCalledMultipleTimes() {
    Type componentType = Long.class;

    GenericArrayType result1 = $Gson$Types.arrayOf(componentType);
    GenericArrayType result2 = $Gson$Types.arrayOf(componentType);

    assertEquals(result1.getGenericComponentType(), result2.getGenericComponentType());
  }
}