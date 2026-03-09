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

class $Gson$Types_274_6Test {

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
    Type innerComponent = Integer.class;
    GenericArrayType innerArray = $Gson$Types.arrayOf(innerComponent);
    GenericArrayType result = $Gson$Types.arrayOf(innerArray);
    assertNotNull(result);
    assertEquals(innerArray, result.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_withParameterizedType() throws Exception {
    // Create a ParameterizedType via reflection to test
    Type rawType = java.util.Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};
    Type parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArgs);
    GenericArrayType result = $Gson$Types.arrayOf(parameterizedType);
    assertNotNull(result);
    assertEquals(parameterizedType, result.getGenericComponentType());
  }
}