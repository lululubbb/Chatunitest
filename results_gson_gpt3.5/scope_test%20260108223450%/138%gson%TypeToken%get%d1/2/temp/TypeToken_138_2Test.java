package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.util.Map;

class TypeToken_138_2Test {

  @Test
    @Timeout(8000)
  void testGet_withClassType() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withParameterizedType() {
    Type parameterizedType = new TypeToken<Map<String, Integer>>() {}.getType();
    TypeToken<?> token = TypeToken.get(parameterizedType);
    assertNotNull(token);
    assertEquals(Map.class, token.getRawType(), "Raw type should be Map");
    assertEquals(parameterizedType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withGenericArrayType() {
    Type arrayType = new TypeToken<String[]>() {}.getType();
    TypeToken<?> token = TypeToken.get(arrayType);
    assertNotNull(token);
    assertEquals(String[].class, token.getRawType());
    assertEquals(arrayType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withTypeVariable() throws Exception {
    TypeToken<?> token = TypeToken.get(TypeToken.class.getTypeParameters()[0]);
    assertNotNull(token);
    assertEquals(Object.class, token.getRawType());
    assertEquals(TypeToken.class.getTypeParameters()[0], token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withNullType() {
    assertThrows(NullPointerException.class, () -> {
      TypeToken.get(null);
    });
  }
}