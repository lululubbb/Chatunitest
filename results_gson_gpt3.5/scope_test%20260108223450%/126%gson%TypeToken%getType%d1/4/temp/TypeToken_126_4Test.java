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

class TypeToken_126_4Test {

  @Test
    @Timeout(8000)
  void testGetType_withDefaultConstructor() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Type type = typeToken.getType();
    assertNotNull(type);
    assertEquals(String.class, typeToken.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withParameterizedConstructor() throws Exception {
    // Use reflection to invoke private constructor TypeToken(Type)
    Type stringType = String.class;
    java.lang.reflect.Constructor<TypeToken> constructor =
      TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> typeToken = constructor.newInstance(stringType);
    Type type = typeToken.getType();
    assertNotNull(type);
    assertEquals(stringType, type);
  }

  @Test
    @Timeout(8000)
  void testGetType_consistency() {
    TypeToken<Integer> typeToken1 = new TypeToken<Integer>() {};
    TypeToken<Integer> typeToken2 = new TypeToken<Integer>() {};
    assertEquals(typeToken1.getType(), typeToken2.getType());
  }
}