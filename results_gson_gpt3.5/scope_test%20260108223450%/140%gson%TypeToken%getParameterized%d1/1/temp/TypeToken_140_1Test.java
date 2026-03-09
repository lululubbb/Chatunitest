package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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
    // Make sure rawType is not Class instance
    assertFalse(rawType instanceof Class);
    Type[] typeArguments = new Type[0];
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArguments));
    assertTrue(ex.getMessage().contains("rawType must be of type Class"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentsCountMismatch_throwsIllegalArgumentException() {
    Class<?> rawType = HashMap.class; // HashMap<K,V> has 2 type parameters
    Type[] typeArguments = new Type[] { String.class };
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArguments));
    assertTrue(ex.getMessage().contains("requires 2 type arguments, but got 1"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentDoesNotSatisfyBounds_throwsIllegalArgumentException() {
    // Create a rawType with a type variable that has bounds
    class Bounded<T extends Number> {}
    Class<?> rawType = Bounded.class;
    Type[] typeArguments = new Type[] { String.class }; // String does not extend Number

    try (MockedStatic<$Gson$Types> gsonTypesMock = Mockito.mockStatic($Gson$Types.class)) {
      // Mock getRawType for the type argument and bound
      gsonTypesMock.when(() -> $Gson$Types.getRawType(typeArguments[0])).thenReturn(String.class);

      TypeVariable<?>[] typeVariables = rawType.getTypeParameters();
      TypeVariable<?> tv = typeVariables[0];
      Type bound = tv.getBounds()[0];
      gsonTypesMock.when(() -> $Gson$Types.getRawType(bound)).thenReturn(Number.class);

      // For newParameterizedTypeWithOwner, call real method to avoid NullPointerException
      gsonTypesMock.when(() -> $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments))
          .thenCallRealMethod();

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> TypeToken.getParameterized(rawType, typeArguments));
      assertTrue(ex.getMessage().contains("does not satisfy bounds"));
    }
  }

  @Test
    @Timeout(8000)
  void getParameterized_validParameters_returnsTypeToken() {
    Class<?> rawType = HashMap.class;
    Type keyType = String.class;
    Type valueType = Integer.class;
    Type[] typeArguments = new Type[] { keyType, valueType };

    try (MockedStatic<$Gson$Types> gsonTypesMock = Mockito.mockStatic($Gson$Types.class)) {
      gsonTypesMock.when(() -> $Gson$Types.getRawType(keyType)).thenReturn(String.class);
      gsonTypesMock.when(() -> $Gson$Types.getRawType(valueType)).thenReturn(Integer.class);

      TypeVariable<?>[] typeVariables = rawType.getTypeParameters();
      for (TypeVariable<?> tv : typeVariables) {
        for (Type bound : tv.getBounds()) {
          gsonTypesMock.when(() -> $Gson$Types.getRawType(bound)).thenAnswer(invocation -> {
            Type b = invocation.getArgument(0);
            if (b instanceof Class) return b;
            return Object.class;
          });
        }
      }

      ParameterizedType parameterizedType = mock(ParameterizedType.class);
      gsonTypesMock.when(() -> $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments))
          .thenReturn(parameterizedType);

      TypeToken<?> result = TypeToken.getParameterized(rawType, typeArguments);

      assertNotNull(result);
      assertEquals(parameterizedType, result.getType());
    }
  }
}