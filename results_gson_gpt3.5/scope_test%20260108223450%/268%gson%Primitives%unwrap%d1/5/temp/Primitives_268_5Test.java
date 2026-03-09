package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class Primitives_268_5Test {

  @Test
    @Timeout(8000)
  void unwrap_IntegerClass_returnsIntClass() {
    Class<Integer> input = Integer.class;
    Class<Integer> expected = int.class;
    Class<Integer> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_FloatClass_returnsFloatClass() {
    Class<Float> input = Float.class;
    Class<Float> expected = float.class;
    Class<Float> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_ByteClass_returnsByteClass() {
    Class<Byte> input = Byte.class;
    Class<Byte> expected = byte.class;
    Class<Byte> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_DoubleClass_returnsDoubleClass() {
    Class<Double> input = Double.class;
    Class<Double> expected = double.class;
    Class<Double> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_LongClass_returnsLongClass() {
    Class<Long> input = Long.class;
    Class<Long> expected = long.class;
    Class<Long> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_CharacterClass_returnsCharClass() {
    Class<Character> input = Character.class;
    Class<Character> expected = char.class;
    Class<Character> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_BooleanClass_returnsBooleanClass() {
    Class<Boolean> input = Boolean.class;
    Class<Boolean> expected = boolean.class;
    Class<Boolean> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_ShortClass_returnsShortClass() {
    Class<Short> input = Short.class;
    Class<Short> expected = short.class;
    Class<Short> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_VoidClass_returnsVoidClass() {
    Class<Void> input = Void.class;
    Class<Void> expected = void.class;
    Class<Void> actual = Primitives.unwrap(input);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_PrimitiveClass_returnsSameClass() {
    Class<Integer> input = int.class;
    Class<Integer> actual = Primitives.unwrap(input);
    assertSame(input, actual);
  }

  @Test
    @Timeout(8000)
  void unwrap_NonPrimitiveNonWrapperClass_returnsSameClass() {
    Class<String> input = String.class;
    Class<String> actual = Primitives.unwrap(input);
    assertSame(input, actual);
  }
}