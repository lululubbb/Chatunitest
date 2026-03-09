package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Primitives_268_2Test {

  @Test
    @Timeout(8000)
  void unwrap_Integer() {
    Class<Integer> result = Primitives.unwrap(Integer.class);
    assertEquals(int.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Float() {
    Class<Float> result = Primitives.unwrap(Float.class);
    assertEquals(float.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Byte() {
    Class<Byte> result = Primitives.unwrap(Byte.class);
    assertEquals(byte.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Double() {
    Class<Double> result = Primitives.unwrap(Double.class);
    assertEquals(double.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Long() {
    Class<Long> result = Primitives.unwrap(Long.class);
    assertEquals(long.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Character() {
    Class<Character> result = Primitives.unwrap(Character.class);
    assertEquals(char.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Boolean() {
    Class<Boolean> result = Primitives.unwrap(Boolean.class);
    assertEquals(boolean.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Short() {
    Class<Short> result = Primitives.unwrap(Short.class);
    assertEquals(short.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_Void() {
    Class<Void> result = Primitives.unwrap(Void.class);
    assertEquals(void.class, result);
  }

  @Test
    @Timeout(8000)
  void unwrap_OtherClass() {
    Class<String> result = Primitives.unwrap(String.class);
    assertEquals(String.class, result);
  }
}