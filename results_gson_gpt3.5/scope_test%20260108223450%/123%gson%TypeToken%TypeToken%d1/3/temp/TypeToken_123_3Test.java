package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_123_3Test {

  private Type mockType;
  private Class<?> mockRawType;

  @BeforeEach
  void setup() {
    mockType = mock(Type.class);
    mockRawType = Object.class;
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_andFields() throws Exception {
    try (MockedStatic<$Gson$Types> gsonTypesMockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      gsonTypesMockedStatic.when(() -> $Gson$Types.canonicalize(mockType)).thenReturn(mockType);
      gsonTypesMockedStatic.when(() -> $Gson$Types.getRawType(mockType)).thenReturn(mockRawType);

      Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);
      TypeToken<?> typeToken = constructor.newInstance(mockType);

      // Check fields rawType, type, hashCode
      Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
      rawTypeField.setAccessible(true);
      Object rawTypeValue = rawTypeField.get(typeToken);
      assertEquals(mockRawType, rawTypeValue);

      Field typeField = TypeToken.class.getDeclaredField("type");
      typeField.setAccessible(true);
      Object typeValue = typeField.get(typeToken);
      assertEquals(mockType, typeValue);

      Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
      hashCodeField.setAccessible(true);
      int expectedHash = mockType.hashCode();
      int hashCodeValue = (int) hashCodeField.get(typeToken);
      assertEquals(expectedHash, hashCodeValue);
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_nullType_throwsNPE() throws Exception {
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> constructor.newInstance((Type) null));
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_callsCanonicalizeAndGetRawType() throws Exception {
    Type sampleType = String.class;
    try (MockedStatic<$Gson$Types> gsonTypesMockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      gsonTypesMockedStatic.when(() -> $Gson$Types.canonicalize(sampleType)).thenReturn(sampleType);
      gsonTypesMockedStatic.when(() -> $Gson$Types.getRawType(sampleType)).thenReturn(String.class);

      Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);
      TypeToken<?> token = constructor.newInstance(sampleType);

      gsonTypesMockedStatic.verify(() -> $Gson$Types.canonicalize(sampleType), times(1));
      gsonTypesMockedStatic.verify(() -> $Gson$Types.getRawType(sampleType), times(1));

      Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
      rawTypeField.setAccessible(true);
      assertEquals(String.class, rawTypeField.get(token));
    }
  }
}