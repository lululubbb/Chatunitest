package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TypeToken_123_1Test {

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_withValidType() throws Exception {
    Type mockType = Mockito.mock(Type.class);
    try (MockedStatic<$Gson$Types> gsonTypesStatic = Mockito.mockStatic($Gson$Types.class)) {
      Type canonicalizedType = Mockito.mock(Type.class);
      Class<?> rawTypeClass = String.class;

      gsonTypesStatic.when(() -> $Gson$Types.canonicalize(mockType)).thenReturn(canonicalizedType);
      gsonTypesStatic.when(() -> $Gson$Types.getRawType(canonicalizedType)).thenReturn(rawTypeClass);

      // Use reflection to access private constructor
      Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);
      TypeToken<?> typeToken = constructor.newInstance(mockType);

      // Verify fields via public getters
      assertEquals(rawTypeClass, typeToken.getRawType());
      assertEquals(canonicalizedType, typeToken.getType());
      assertEquals(canonicalizedType.hashCode(), typeToken.hashCode());
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_nullType_throwsNPE() throws Exception {
    // Use reflection to access private constructor
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance((Type) null);
    });
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_canonicalizeReturnsNull_throwsNPE() throws Exception {
    Type mockType = Mockito.mock(Type.class);
    try (MockedStatic<$Gson$Types> gsonTypesStatic = Mockito.mockStatic($Gson$Types.class)) {
      gsonTypesStatic.when(() -> $Gson$Types.canonicalize(mockType)).thenReturn(null);

      Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);

      InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
        constructor.newInstance(mockType);
      });
      assertNotNull(thrown);
      assertTrue(thrown.getCause() instanceof NullPointerException);
    }
  }
}