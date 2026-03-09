package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class Primitives_268_6Test {

  @Test
    @Timeout(8000)
  void unwrap_Integer_returnsIntClass() {
    Class<Integer> input = Integer.class;
    Class<Integer> result = Primitives.unwrap(input);
    assertEquals(int.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Float_returnsFloatClass() {
    Class<Float> input = Float.class;
    Class<Float> result = Primitives.unwrap(input);
    assertEquals(float.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Byte_returnsByteClass() {
    Class<Byte> input = Byte.class;
    Class<Byte> result = Primitives.unwrap(input);
    assertEquals(byte.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Double_returnsDoubleClass() {
    Class<Double> input = Double.class;
    Class<Double> result = Primitives.unwrap(input);
    assertEquals(double.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Long_returnsLongClass() {
    Class<Long> input = Long.class;
    Class<Long> result = Primitives.unwrap(input);
    assertEquals(long.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Character_returnsCharClass() {
    Class<Character> input = Character.class;
    Class<Character> result = Primitives.unwrap(input);
    assertEquals(char.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Boolean_returnsBooleanClass() {
    Class<Boolean> input = Boolean.class;
    Class<Boolean> result = Primitives.unwrap(input);
    assertEquals(boolean.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Short_returnsShortClass() {
    Class<Short> input = Short.class;
    Class<Short> result = Primitives.unwrap(input);
    assertEquals(short.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Void_returnsVoidClass() {
    Class<Void> input = Void.class;
    Class<Void> result = Primitives.unwrap(input);
    assertEquals(void.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_NonWrapperClass_returnsSameClass() {
    Class<String> input = String.class;
    Class<String> result = Primitives.unwrap(input);
    assertSame(input, result);
  }
}