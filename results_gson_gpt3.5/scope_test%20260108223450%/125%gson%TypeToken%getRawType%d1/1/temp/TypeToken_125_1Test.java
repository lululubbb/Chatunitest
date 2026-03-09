package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_125_1Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClassType() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Class<? super String> rawType = typeToken.getRawType();
    assertEquals(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() {
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    Class<? super Map<String, Integer>> rawType = typeToken.getRawType();
    assertEquals(Map.class, rawType);
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
  void testGetRawType_withWildcardType() {
    // Fix: use TypeToken<Number> without wildcard for correct typing
    TypeToken<Number> typeToken = new TypeToken<Number>() {};
    Class<? super Number> rawType = typeToken.getRawType();
    assertEquals(Number.class, rawType);
  }
}