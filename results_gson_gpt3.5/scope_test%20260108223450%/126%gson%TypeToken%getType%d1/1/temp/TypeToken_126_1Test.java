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
import java.lang.reflect.Constructor;

class TypeToken_126_1Test {

  @Test
    @Timeout(8000)
  void testGetType_withDefaultConstructor() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Type type = typeToken.getType();
    assertNotNull(type);
    assertEquals(String.class, type);
  }

  @Test
    @Timeout(8000)
  void testGetType_withParameterizedConstructor() throws Exception {
    Type stringType = String.class;
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> typeTokenInstance = constructor.newInstance(stringType);
    Type type = typeTokenInstance.getType();
    assertNotNull(type);
    assertEquals(stringType, type);
  }

  @Test
    @Timeout(8000)
  void testGetType_consistency() {
    TypeToken<Integer> token = new TypeToken<Integer>() {};
    Type type1 = token.getType();
    Type type2 = token.getType();
    assertSame(type1, type2);
  }
}