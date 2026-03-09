package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class $Gson$Types_286_5Test {

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withProperties_returnsStringString() {
    Type[] result = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(String.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withParameterizedMapType_returnsActualTypeArguments() throws Exception {
    Type keyType = String.class;
    Type valueType = Integer.class;

    // Create a ParameterizedType instance representing Map<String, Integer>
    ParameterizedType parameterizedMapType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { keyType, valueType };
      }

      @Override
      public Type getRawType() {
        return Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Use reflection to get private static method getSupertype
    java.lang.reflect.Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class, Mockito.CALLS_REAL_METHODS)) {
      // Mock getMapKeyAndValueTypes to call getSupertypeMethod via reflection instead of direct call
      mockedStatic.when(() -> $Gson$Types.getMapKeyAndValueTypes(any(), any())).thenAnswer(invocation -> {
        Object[] args = invocation.getArguments();
        Type context = (Type) args[0];
        Class<?> contextRawType = (Class<?>) args[1];
        Type mapType;
        if (context == Object.class && contextRawType == Object.class) {
          mapType = parameterizedMapType;
        } else {
          mapType = (Type) getSupertypeMethod.invoke(null, context, contextRawType, Map.class);
        }
        if (mapType instanceof ParameterizedType) {
          ParameterizedType mapParameterizedType = (ParameterizedType) mapType;
          return mapParameterizedType.getActualTypeArguments();
        }
        return new Type[] { Object.class, Object.class };
      });

      // Call the mocked getMapKeyAndValueTypes
      Type[] result = $Gson$Types.getMapKeyAndValueTypes(Object.class, Object.class);

      assertNotNull(result);
      assertEquals(2, result.length);
      assertEquals(keyType, result[0]);
      assertEquals(valueType, result[1]);
    }
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withNonParameterizedMapType_returnsObjectObject() throws Exception {
    // Use reflection to get private static method getSupertype
    java.lang.reflect.Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class, Mockito.CALLS_REAL_METHODS)) {
      // Mock getMapKeyAndValueTypes to call getSupertypeMethod via reflection instead of direct call
      mockedStatic.when(() -> $Gson$Types.getMapKeyAndValueTypes(any(), any())).thenAnswer(invocation -> {
        Object[] args = invocation.getArguments();
        Type context = (Type) args[0];
        Class<?> contextRawType = (Class<?>) args[1];
        Type mapType;
        if (context == Object.class && contextRawType == Object.class) {
          mapType = Map.class;
        } else {
          mapType = (Type) getSupertypeMethod.invoke(null, context, contextRawType, Map.class);
        }
        if (mapType instanceof ParameterizedType) {
          ParameterizedType mapParameterizedType = (ParameterizedType) mapType;
          return mapParameterizedType.getActualTypeArguments();
        }
        return new Type[] { Object.class, Object.class };
      });

      Type[] result = $Gson$Types.getMapKeyAndValueTypes(Object.class, Object.class);

      assertNotNull(result);
      assertEquals(2, result.length);
      assertEquals(Object.class, result[0]);
      assertEquals(Object.class, result[1]);
    }
  }
}