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

class TypeToken_139_5Test {

  @Test
    @Timeout(8000)
  void testGet_withClass() {
    // Test with a concrete class
    TypeToken<String> stringToken = TypeToken.get(String.class);
    assertNotNull(stringToken);
    assertEquals(String.class, stringToken.getRawType());
    assertEquals(String.class, stringToken.getType());
    assertEquals(stringToken.hashCode(), stringToken.hashCode());
    assertTrue(stringToken.equals(stringToken));
    assertFalse(stringToken.equals(null));
    assertFalse(stringToken.equals("not a TypeToken"));
    assertTrue(stringToken.toString().contains("java.lang.String"));

    // Test with an interface
    TypeToken<Runnable> runnableToken = TypeToken.get(Runnable.class);
    assertNotNull(runnableToken);
    assertEquals(Runnable.class, runnableToken.getRawType());
    assertEquals(Runnable.class, runnableToken.getType());

    // Test with primitive type
    TypeToken<Integer> intToken = TypeToken.get(int.class);
    assertNotNull(intToken);
    assertEquals(int.class, intToken.getRawType());
    assertEquals(int.class, intToken.getType());
  }
}