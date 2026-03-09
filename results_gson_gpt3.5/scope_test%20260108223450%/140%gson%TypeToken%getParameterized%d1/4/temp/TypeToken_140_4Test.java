package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

class TypeTokenGetParameterizedTest {

  @Test
    @Timeout(8000)
  void getParameterized_nullRawType_throwsNullPointerException() {
    Type rawType = null;
    Type[] typeArguments = new Type[0];
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(rawType, typeArguments));
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullTypeArguments_throwsNullPointerException() {
    Type rawType = String.class;
    Type[] typeArguments = null;
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(rawType, typeArguments));
  }

  @Test
    @Timeout(8000)
  void getParameterized_rawTypeNotClass_throwsIllegalArgumentException() {
    Type rawType = mock(Type.class);
    // rawType is not an instance of Class
    assertFalse(rawType instanceof Class);
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
        TypeToken.getParameterized(rawType));
    assertTrue(thrown.getMessage().contains("rawType must be of type Class"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentsCountMismatch_throwsIllegalArgumentException() {
    Class<?> rawType = java.util.Map.class; // Map<K,V> has 2 type parameters
    Type[] typeArguments = new Type[] { String.class }; // only 1 argument
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
        TypeToken.getParameterized(rawType, typeArguments));
    assertTrue(thrown.getMessage().contains(rawType.getName()));
    assertTrue(thrown.getMessage().contains("requires 2 type arguments"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentDoesNotSatisfyBounds_throwsIllegalArgumentException() throws Exception {
    // Create a mock Class with a TypeVariable to avoid compile error.

    @SuppressWarnings("unchecked")
    Class<?> rawType = mock(Class.class);

    // Create a TypeVariable[] array with a raw type matching Class's getTypeParameters() signature
    @SuppressWarnings("unchecked")
    TypeVariable<Class<?>> typeVariable = mock(TypeVariable.class);
    TypeVariable<Class<?>>[] typeVariables = (TypeVariable<Class<?>>[]) java.lang.reflect.Array.newInstance(TypeVariable.class, 1);
    typeVariables[0] = typeVariable;

    when(rawType.getTypeParameters()).thenReturn(typeVariables);
    when(rawType.getName()).thenReturn("DummyClass");

    Type bound = Number.class;
    when(typeVariable.getBounds()).thenReturn(new Type[] { bound });

    Type typeArgument = String.class; // String is not a subclass of Number

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
        TypeToken.getParameterized(rawType, typeArgument));
    assertTrue(thrown.getMessage().contains("does not satisfy bounds"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_validParameters_returnsTypeToken() {
    // Use Map<String, Integer>
    Type rawType = java.util.Map.class;
    Type[] typeArguments = new Type[] { String.class, Integer.class };

    // We expect no exception and a non-null TypeToken returned
    TypeToken<?> token = TypeToken.getParameterized(rawType, typeArguments);
    assertNotNull(token);
    // The token's rawType should be Map.class
    assertEquals(rawType, token.getRawType());
  }

  // Helper method to mock $Gson$Types.getRawType(Type) static method
  // Since $Gson$Types is internal and static, we cannot mock directly without a framework like PowerMockito.
  // Instead, we rely on actual behavior for known classes.
  private void mockStaticGsonTypesGetRawType(Type input, Class<?> output) {
    // No-op: we rely on actual $Gson$Types.getRawType for these known classes.
    // If needed, PowerMockito or similar could be used to mock static methods.
  }
}