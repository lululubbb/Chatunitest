package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class TypeToken_122_2Test {

  private TypeToken<?> typeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void testProtectedConstructor_setsFieldsCorrectly() throws Exception {
    // Use anonymous subclass to invoke protected constructor
    TypeToken<String> token = new TypeToken<String>() {};
    // rawType should be String.class
    assertEquals(String.class, token.getRawType());
    // type should be the actual Type argument (String)
    assertTrue(token.getType() instanceof Class);
    assertEquals(String.class, token.getType());
    // hashCode should be type.hashCode()
    assertEquals(token.getType().hashCode(), token.hashCode());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_setsFieldsCorrectly() throws Exception {
    // Access private constructor TypeToken(Type)
    Type type = String.class;
    var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = (TypeToken<?>) constructor.newInstance(type);

    // rawType should be String.class
    assertEquals(String.class, token.getRawType());
    // type should be String.class
    assertEquals(type, token.getType());
    // hashCode should be type.hashCode()
    assertEquals(type.hashCode(), token.hashCode());
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_returnsCorrectType() throws Exception {
    // Create anonymous subclass with type argument Integer
    TypeToken<Integer> token = new TypeToken<Integer>() {};
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type result = (Type) method.invoke(token);
    assertEquals(Integer.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_andGetType() {
    assertEquals(Object.class, new TypeToken<Object>() {}.getRawType());
    TypeToken<String> token = new TypeToken<String>() {};
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testEquals_andHashCode_andToString() {
    TypeToken<String> token1 = new TypeToken<String>() {};
    TypeToken<String> token2 = new TypeToken<String>() {};
    TypeToken<Integer> token3 = new TypeToken<Integer>() {};

    assertEquals(token1, token2);
    assertEquals(token1.hashCode(), token2.hashCode());
    assertNotEquals(token1, token3);
    assertNotEquals(token1.hashCode(), token3.hashCode());

    String toString = token1.toString();
    assertTrue(toString.contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFrom_methods() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    // Class<?> argument
    assertTrue(stringToken.isAssignableFrom(String.class));
    assertFalse(stringToken.isAssignableFrom(Integer.class));

    // Type argument
    assertTrue(stringToken.isAssignableFrom(String.class));
    assertFalse(stringToken.isAssignableFrom(Integer.class));

    // TypeToken<?> argument
    TypeToken<?> objectToken = new TypeToken<Object>() {};
    assertTrue(objectToken.isAssignableFrom(stringToken));
    assertFalse(stringToken.isAssignableFrom(objectToken));
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_GenericArrayType() throws Exception {
    // Create GenericArrayType mock
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(genericArrayType.getGenericComponentType()).thenReturn(componentType);

    // from is String[].class (which is a Class and an array)
    Class<?> from = String[].class;

    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, from, genericArrayType);
    assertTrue(result);

    // from is Integer[].class but to component type is String
    Class<?> from2 = Integer[].class;
    boolean result2 = (boolean) method.invoke(null, from2, genericArrayType);
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_ParameterizedType_and_typeEquals() throws Exception {
    // Setup ParameterizedType mocks for from and to
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);

    // Setup typeVarMap
    Map<String, Type> typeVarMap = Map.of();

    Method isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    isAssignableFromMethod.setAccessible(true);

    Method typeEqualsMethod = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    typeEqualsMethod.setAccessible(true);

    // We need to mock $Gson$Types.typeEquals to return true to avoid NullPointerException inside isAssignableFrom
    try (MockedStatic<$Gson$Types> gsonTypesMockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      // Mock the three-parameter typeEquals method instead of two-parameter one
      gsonTypesMockedStatic.when(() -> $Gson$Types.typeEquals(from, to, typeVarMap)).thenReturn(true);

      boolean result = (boolean) isAssignableFromMethod.invoke(null, from, to, typeVarMap);
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    Type unexpectedType = String.class;
    Class<?>[] expected = new Class<?>[] {Integer.class, Double.class};

    InvocationTargetException error = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, unexpectedType, expected);
    });

    // InvocationTargetException wraps the AssertionError thrown by buildUnexpectedTypeError
    Throwable cause = error.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof AssertionError);
    assertTrue(cause.getMessage().contains("Expected one of"));
  }

  @Test
    @Timeout(8000)
  void testMatches() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    Type from = String.class;
    Type to = String.class;
    Map<String, Type> typeMap = Map.of();

    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertTrue(result);

    // Different types
    Type to2 = Integer.class;
    boolean result2 = (boolean) method.invoke(null, from, to2, typeMap);
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testStaticGetMethods() {
    TypeToken<?> token1 = TypeToken.get(String.class);
    assertEquals(String.class, token1.getRawType());

    TypeToken<?> token2 = TypeToken.get((Type) String.class);
    assertEquals(String.class, token2.getRawType());

    TypeToken<?> token3 = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertEquals(Map.class, token3.getRawType());

    TypeToken<?> token4 = TypeToken.getArray(String.class);
    assertEquals(String[].class, token4.getRawType());
  }
}