package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class Primitives_268_1Test {

  @Test
    @Timeout(8000)
  void unwrap_IntegerClass_returnsIntClass() {
    assertEquals(int.class, Primitives.unwrap(Integer.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_FloatClass_returnsFloatClass() {
    assertEquals(float.class, Primitives.unwrap(Float.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_ByteClass_returnsByteClass() {
    assertEquals(byte.class, Primitives.unwrap(Byte.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_DoubleClass_returnsDoubleClass() {
    assertEquals(double.class, Primitives.unwrap(Double.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_LongClass_returnsLongClass() {
    assertEquals(long.class, Primitives.unwrap(Long.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_CharacterClass_returnsCharClass() {
    assertEquals(char.class, Primitives.unwrap(Character.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_BooleanClass_returnsBooleanClass() {
    assertEquals(boolean.class, Primitives.unwrap(Boolean.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_ShortClass_returnsShortClass() {
    assertEquals(short.class, Primitives.unwrap(Short.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_VoidClass_returnsVoidClass() {
    assertEquals(void.class, Primitives.unwrap(Void.class));
  }

  @Test
    @Timeout(8000)
  void unwrap_OtherClass_returnsSameClass() {
    Class<String> stringClass = String.class;
    assertSame(stringClass, Primitives.unwrap(stringClass));
  }
}