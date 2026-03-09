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

class TypeToken_125_3Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClassType() {
    TypeToken<String> token = new TypeToken<String>() {};
    Class<? super String> rawType = token.getRawType();
    assertEquals(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() {
    TypeToken<java.util.List<String>> token = new TypeToken<java.util.List<String>>() {};
    Class<? super java.util.List<String>> rawType = token.getRawType();
    assertEquals(java.util.List.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withArrayType() {
    TypeToken<String[]> token = new TypeToken<String[]>() {};
    Class<? super String[]> rawType = token.getRawType();
    assertEquals(String[].class, rawType);
  }
}