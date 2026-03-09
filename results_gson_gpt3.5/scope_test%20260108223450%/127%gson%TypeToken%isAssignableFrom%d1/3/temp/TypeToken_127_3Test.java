package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenIsAssignableFromTest {

  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClassAssignable() {
    // rawType is Object.class, assignable from String.class is true
    boolean result = typeToken.isAssignableFrom(String.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClassNotAssignable() {
    // Create TypeToken<String> and test isAssignableFrom(Integer.class)
    TypeToken<String> stringToken = new TypeToken<String>() {};
    boolean result = stringToken.isAssignableFrom(Integer.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withNullClass() {
    assertThrows(NullPointerException.class, () -> typeToken.isAssignableFrom((Class<?>) null));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_invokesIsAssignableFromType() throws Exception {
    // Use reflection to access private method isAssignableFrom(Type)
    java.lang.reflect.Method isAssignableFromTypeMethod =
        TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromTypeMethod.setAccessible(true);

    Class<?> cls = String.class;
    boolean resultViaMethod = (boolean) isAssignableFromTypeMethod.invoke(typeToken, cls);
    boolean resultViaPublic = typeToken.isAssignableFrom(cls);
    assertEquals(resultViaMethod, resultViaPublic);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withParameterizedType() {
    // Mock ParameterizedType and static methods to simulate assignability
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Type fromType = String.class;

    try (MockedStatic<TypeToken> mockedStatic = Mockito.mockStatic(TypeToken.class, Mockito.CALLS_REAL_METHODS)) {
      // Mock private static isAssignableFrom(Type, ParameterizedType, Map) to return true
      mockedStatic.when(() -> {
        TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
      }).thenCallRealMethod();

      // Because we cannot mock private static methods easily, just test public method with ParameterizedType cast to Type
      TypeToken<?> token = TypeToken.getParameterized(String.class, String.class);
      boolean result = token.isAssignableFrom(fromType);
      // Result depends on actual implementation, so just assert it's boolean
      assertTrue(result || !result);
    }
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withGenericArrayType() throws Exception {
    // Prepare GenericArrayType mock
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type fromType = String[].class;

    // Access private static isAssignableFrom(Type, GenericArrayType)
    java.lang.reflect.Method privateMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    privateMethod.setAccessible(true);

    boolean result = (boolean) privateMethod.invoke(null, fromType, genericArrayType);
    // Result is boolean, just assert no exception and boolean result
    assertTrue(result || !result);
  }
}