package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_123_4Test {

  private Type mockType;
  private Class<?> mockRawType;

  @BeforeEach
  void setUp() {
    mockType = mock(Type.class);
    mockRawType = Object.class;
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_withValidType() throws Exception {
    try (MockedStatic<$Gson$Types> mockedGsonTypes = Mockito.mockStatic($Gson$Types.class)) {
      mockedGsonTypes.when(() -> $Gson$Types.canonicalize(mockType)).thenReturn(mockType);
      mockedGsonTypes.when(() -> $Gson$Types.getRawType(mockType)).thenReturn(mockRawType);

      // Use reflection to access private constructor
      var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);

      TypeToken<?> typeToken = (TypeToken<?>) constructor.newInstance(mockType);

      // Access private fields via reflection
      Field typeField = TypeToken.class.getDeclaredField("type");
      Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
      Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
      typeField.setAccessible(true);
      rawTypeField.setAccessible(true);
      hashCodeField.setAccessible(true);

      assertSame(mockType, typeField.get(typeToken));
      assertEquals(mockRawType, rawTypeField.get(typeToken));
      assertEquals(mockType.hashCode(), hashCodeField.get(typeToken));
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_nullTypeThrowsNPE() throws Exception {
    try (MockedStatic<$Gson$Types> mockedGsonTypes = Mockito.mockStatic($Gson$Types.class)) {
      // $Gson$Types.canonicalize should not be called because type is null
      var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);

      Throwable thrown = assertThrows(Throwable.class, () -> {
        constructor.newInstance((Type) null);
      });

      // Unwrap InvocationTargetException to check cause
      Throwable cause = thrown.getCause();
      assertNotNull(cause);
      assertTrue(cause instanceof NullPointerException);
    }
  }
}