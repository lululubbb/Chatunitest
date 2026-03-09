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
import com.google.gson.internal.$Gson$Types;

class GsonTypesArrayOfTest {

  @Test
    @Timeout(8000)
  void testArrayOf_withClassType() {
    Type componentType = String.class;
    GenericArrayType result = $Gson$Types.arrayOf(componentType);
    assertNotNull(result);
    assertEquals(componentType, result.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_withGenericArrayType() {
    Type innerComponentType = Integer.class;
    GenericArrayType innerArray = $Gson$Types.arrayOf(innerComponentType);
    GenericArrayType result = $Gson$Types.arrayOf(innerArray);
    assertNotNull(result);
    assertEquals(innerArray, result.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_withParameterizedType() {
    Type rawType = java.util.List.class;
    Type[] typeArgs = new Type[] {String.class};
    var parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArgs);
    GenericArrayType result = $Gson$Types.arrayOf(parameterizedType);
    assertNotNull(result);
    assertEquals(parameterizedType, result.getGenericComponentType());
  }
}