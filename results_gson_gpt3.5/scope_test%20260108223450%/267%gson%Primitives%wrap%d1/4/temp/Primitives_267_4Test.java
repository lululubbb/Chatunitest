package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Primitives_267_4Test {

  @Test
    @Timeout(8000)
  void wrap_shouldReturnWrapperClass_whenGivenPrimitive() {
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
  void wrap_shouldReturnSameClass_whenGivenNonPrimitive() {
    assertEquals(String.class, Primitives.wrap(String.class));
    assertEquals(Integer.class, Primitives.wrap(Integer.class));
    assertEquals(Object.class, Primitives.wrap(Object.class));
  }
}