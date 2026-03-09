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

class TypeToken_125_2Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClass() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Class<? super String> rawType = typeToken.getRawType();
    assertEquals(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() {
    TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};
    Class<? super java.util.List<String>> rawType = typeToken.getRawType();
    assertEquals(java.util.List.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withArrayType() {
    TypeToken<String[]> typeToken = new TypeToken<String[]>() {};
    Class<? super String[]> rawType = typeToken.getRawType();
    assertEquals(String[].class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withPrimitive() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Class<? super Integer> rawType = typeToken.getRawType();
    assertEquals(Integer.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_staticGet_method() {
    TypeToken<?> typeToken = TypeToken.get(String.class);
    Class<?> rawType = typeToken.getRawType();
    assertEquals(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_staticGetParameterized() {
    TypeToken<?> typeToken = TypeToken.getParameterized(java.util.Map.class, String.class, Integer.class);
    Class<?> rawType = typeToken.getRawType();
    assertEquals(java.util.Map.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_staticGetArray() {
    TypeToken<?> typeToken = TypeToken.getArray(String.class);
    Class<?> rawType = typeToken.getRawType();
    assertEquals(String[].class, rawType);
  }
}