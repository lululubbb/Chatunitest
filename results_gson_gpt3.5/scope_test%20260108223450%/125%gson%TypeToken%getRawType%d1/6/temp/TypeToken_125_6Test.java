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

class TypeToken_125_6Test {

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
  void testGetRawType_withPrimitiveType() {
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Class<? super Integer> rawType = typeToken.getRawType();
    assertEquals(Integer.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_consistency() {
    TypeToken<Double> typeToken = new TypeToken<Double>() {};
    Class<? super Double> rawType1 = typeToken.getRawType();
    Class<? super Double> rawType2 = typeToken.getRawType();
    assertSame(rawType1, rawType2);
  }
}