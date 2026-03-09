package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class TypeToken_139_2Test {

  @Test
    @Timeout(8000)
  void testGet_withClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withPrimitiveClass() {
    TypeToken<Integer> token = TypeToken.get(int.class);
    assertNotNull(token);
    assertEquals(int.class, token.getRawType());
    assertEquals(int.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withVoidClass() {
    TypeToken<Void> token = TypeToken.get(void.class);
    assertNotNull(token);
    assertEquals(void.class, token.getRawType());
    assertEquals(void.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withObjectClass() {
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertNotNull(token);
    assertEquals(Object.class, token.getRawType());
    assertEquals(Object.class, token.getType());
  }
}