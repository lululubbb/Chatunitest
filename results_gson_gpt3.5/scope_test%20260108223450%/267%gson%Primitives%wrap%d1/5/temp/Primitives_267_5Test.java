package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Primitives_267_5Test {

  @Test
    @Timeout(8000)
  void wrap_PrimitiveTypes_ReturnsWrapperClass() {
    assertEquals(Integer.class, Primitives.wrap(int.class));
    assertEquals(Float.class, Primitives.wrap(float.class));
    assertEquals(Byte.class, Primitives.wrap(byte.class));
    assertEquals(Double.class, Primitives.wrap(double.class));
    assertEquals(Long.class, Primitives.wrap(long.class));
    assertEquals(Character.class, Primitives.wrap(char.class));
    assertEquals(Boolean.class, Primitives.wrap(boolean.class));
    assertEquals(Short.class, Primitives.wrap(short.class));
    assertEquals(Void.class, Primitives.wrap(void.class));
  }

  @Test
    @Timeout(8000)
  void wrap_NonPrimitiveType_ReturnsSameClass() {
    assertEquals(String.class, Primitives.wrap(String.class));
    assertEquals(Object.class, Primitives.wrap(Object.class));
    assertEquals(Primitives.class, Primitives.wrap(Primitives.class));
  }
}