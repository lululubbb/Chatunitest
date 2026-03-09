package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TypeTokenToStringTest {

  @Test
    @Timeout(8000)
  void testToString_returnsTypeToString() throws Exception {
    TypeToken<Object> typeToken = new TypeToken<Object>() {};
    Type type = typeToken.getType();

    try (MockedStatic<$Gson$Types> mocked = mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.typeToString(type)).thenReturn("mockedTypeString");

      // Use reflection to call toString() to avoid issues with final methods or proxies
      String result = (String) TypeToken.class.getMethod("toString").invoke(typeToken);

      assertEquals("mockedTypeString", result);
      mocked.verify(() -> $Gson$Types.typeToString(type));
    }
  }
}