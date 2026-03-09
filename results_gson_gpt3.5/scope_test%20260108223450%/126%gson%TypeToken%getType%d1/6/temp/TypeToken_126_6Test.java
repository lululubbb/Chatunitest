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

class TypeToken_126_6Test {

  @Test
    @Timeout(8000)
  void testGetType_withAnonymousSubclass() {
    TypeToken<String> token = new TypeToken<String>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String.class, token.getRawType());
    assertEquals(type, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withClassTypeToken() {
    TypeToken<String> token = TypeToken.get(String.class);
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String.class, token.getRawType());
    assertEquals(type, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withParameterizedTypeToken() {
    TypeToken<java.util.List<String>> token = new TypeToken<java.util.List<String>>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(java.util.List.class, token.getRawType());
    assertEquals(type, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withArrayTypeToken() {
    TypeToken<String[]> token = new TypeToken<String[]>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String[].class, token.getRawType());
    assertEquals(type, token.getType());
  }
}