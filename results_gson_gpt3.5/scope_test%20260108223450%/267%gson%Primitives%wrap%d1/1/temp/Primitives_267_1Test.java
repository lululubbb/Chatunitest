package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Primitives_267_1Test {

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveInt_returnsIntegerClass() {
    Class<Integer> wrapped = Primitives.wrap(int.class);
    assertEquals(Integer.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveFloat_returnsFloatClass() {
    Class<Float> wrapped = Primitives.wrap(float.class);
    assertEquals(Float.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveByte_returnsByteClass() {
    Class<Byte> wrapped = Primitives.wrap(byte.class);
    assertEquals(Byte.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveDouble_returnsDoubleClass() {
    Class<Double> wrapped = Primitives.wrap(double.class);
    assertEquals(Double.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveLong_returnsLongClass() {
    Class<Long> wrapped = Primitives.wrap(long.class);
    assertEquals(Long.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveChar_returnsCharacterClass() {
    Class<Character> wrapped = Primitives.wrap(char.class);
    assertEquals(Character.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveBoolean_returnsBooleanClass() {
    Class<Boolean> wrapped = Primitives.wrap(boolean.class);
    assertEquals(Boolean.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveShort_returnsShortClass() {
    Class<Short> wrapped = Primitives.wrap(short.class);
    assertEquals(Short.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withPrimitiveVoid_returnsVoidClass() {
    Class<Void> wrapped = Primitives.wrap(void.class);
    assertEquals(Void.class, wrapped);
  }

  @Test
    @Timeout(8000)
  void wrap_withNonPrimitive_returnsSameClass() {
    Class<String> wrapped = Primitives.wrap(String.class);
    assertEquals(String.class, wrapped);
  }
}