package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_140_5Test {

  @Test
    @Timeout(8000)
  void getParameterized_validInput_returnsTypeToken() {
    Type rawType = List.class;
    Type typeArg = String.class;
    TypeVariable<?>[] typeVariables = List.class.getTypeParameters();

    try (MockedStatic<$Gson$Types> gsonTypesMock = Mockito.mockStatic($Gson$Types.class)) {
      // Mock getRawType to return the class itself for typeArg and bounds
      gsonTypesMock.when(() -> $Gson$Types.getRawType(typeArg)).thenReturn(String.class);
      for (TypeVariable<?> tv : typeVariables) {
        for (Type bound : tv.getBounds()) {
          gsonTypesMock.when(() -> $Gson$Types.getRawType(bound)).thenAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            return arg instanceof Class ? arg : Object.class;
          });
        }
      }
      // Mock newParameterizedTypeWithOwner to return a mocked ParameterizedType
      ParameterizedType mockParamType = mock(ParameterizedType.class);
      gsonTypesMock.when(() -> $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArg))
          .thenReturn(mockParamType);

      // Fix: also mock getRawType for rawType (List.class) itself, as TypeToken constructor calls it
      gsonTypesMock.when(() -> $Gson$Types.getRawType(rawType)).thenReturn(List.class);

      // Fix: also mock getRawType for the mocked ParameterizedType returned by newParameterizedTypeWithOwner
      gsonTypesMock.when(() -> $Gson$Types.getRawType(mockParamType)).thenReturn(List.class);

      // Fix: mock $Gson$Types.getRawType for the array of type arguments, because TypeToken constructor may call it
      // Use thenAnswer once to cover all cases
      gsonTypesMock.when(() -> $Gson$Types.getRawType(any())).thenAnswer(invocation -> {
        Object arg = invocation.getArgument(0);
        if (arg == rawType) {
          return List.class;
        }
        if (arg == typeArg) {
          return String.class;
        }
        if (arg == mockParamType) {
          return List.class;
        }
        if (arg instanceof Class) {
          return arg;
        }
        return Object.class;
      });

      // Fix: mock $Gson$Types.newParameterizedTypeWithOwner to accept varargs and return mockParamType
      gsonTypesMock.when(() -> $Gson$Types.newParameterizedTypeWithOwner(
          eq(null), eq(rawType), any(Type[].class))).thenReturn(mockParamType);

      TypeToken<?> result = TypeToken.getParameterized(rawType, typeArg);

      assertNotNull(result);
      assertEquals(mockParamType, result.getType());
    }
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullRawType_throwsNullPointerException() {
    Type[] typeArgs = new Type[] {String.class};
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(null, typeArgs));
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullTypeArguments_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(List.class, (Type[]) null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_rawTypeNotClass_throwsIllegalArgumentException() {
    Type rawType = mock(Type.class);
    Type[] typeArgs = new Type[] {String.class};
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains("rawType must be of type Class"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_wrongNumberOfTypeArguments_throwsIllegalArgumentException() {
    Type rawType = List.class;
    Type[] typeArgs = new Type[] {};
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains(List.class.getName() + " requires"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentDoesNotSatisfyBounds_throwsIllegalArgumentException() {
    class Dummy {}
    Type rawType = Map.class; // Map<K,V> has 2 type params
    Type[] typeArgs = new Type[] {Dummy.class, Dummy.class};

    try (MockedStatic<$Gson$Types> gsonTypesMock = Mockito.mockStatic($Gson$Types.class)) {
      // getRawType returns Dummy.class for all typeArgs
      gsonTypesMock.when(() -> $Gson$Types.getRawType(any())).thenReturn(Dummy.class);

      // For bounds of Map's type variables, return Object.class for getRawType(bound)
      TypeVariable<?>[] typeVariables = Map.class.getTypeParameters();
      for (TypeVariable<?> tv : typeVariables) {
        for (Type bound : tv.getBounds()) {
          gsonTypesMock.when(() -> $Gson$Types.getRawType(bound)).thenReturn(Object.class);
        }
      }

      // But override for one bound to simulate failure: Map's K extends Object, so no failure here,
      // simulate by returning String.class as bound and Dummy.class not assignable from String.class
      // Instead, mock getRawType(bound) to return String.class and getRawType(typeArg) Dummy.class,
      // so String.class.isAssignableFrom(Dummy.class) is false, which triggers exception.
      gsonTypesMock.when(() -> $Gson$Types.getRawType(typeArgs[0])).thenReturn(Dummy.class);
      gsonTypesMock.when(() -> $Gson$Types.getRawType(typeVariables[0].getBounds()[0])).thenReturn(String.class);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> TypeToken.getParameterized(rawType, typeArgs));

      assertTrue(ex.getMessage().contains("does not satisfy bounds"));
    }
  }
}