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

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeToken_139_1Test {

  @Test
    @Timeout(8000)
  void testGet_withClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class.getName(), token.getType().getTypeName());
    assertEquals(token.hashCode(), token.hashCode());
    assertTrue(token.equals(token));
    assertFalse(token.equals(null));
    assertFalse(token.equals("some string"));
    assertEquals("class java.lang.String", token.toString());
  }

  @Test
    @Timeout(8000)
  void testGet_withPrimitiveClass() {
    TypeToken<Integer> token = TypeToken.get(int.class);
    assertNotNull(token);
    // Fix: getRawType() returns primitive type int.class for int.class input
    assertEquals(int.class, token.getRawType());
    assertEquals(int.class.getTypeName(), token.getType().getTypeName());
  }

  @Test
    @Timeout(8000)
  void testGet_withVoidClass() {
    TypeToken<Void> token = TypeToken.get(void.class);
    assertNotNull(token);
    assertEquals(void.class, token.getRawType());
    assertEquals(void.class.getTypeName(), token.getType().getTypeName());
  }
}