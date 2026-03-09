package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Primitives_267_6Test {

  @Test
    @Timeout(8000)
  void testWrap_int() {
    assertEquals(Integer.class, Primitives.wrap(int.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_float() {
    assertEquals(Float.class, Primitives.wrap(float.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_byte() {
    assertEquals(Byte.class, Primitives.wrap(byte.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_double() {
    assertEquals(Double.class, Primitives.wrap(double.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_long() {
    assertEquals(Long.class, Primitives.wrap(long.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_char() {
    assertEquals(Character.class, Primitives.wrap(char.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_boolean() {
    assertEquals(Boolean.class, Primitives.wrap(boolean.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_short() {
    assertEquals(Short.class, Primitives.wrap(short.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_void() {
    assertEquals(Void.class, Primitives.wrap(void.class));
  }

  @Test
    @Timeout(8000)
  void testWrap_nonPrimitive() {
    assertEquals(String.class, Primitives.wrap(String.class));
  }
}