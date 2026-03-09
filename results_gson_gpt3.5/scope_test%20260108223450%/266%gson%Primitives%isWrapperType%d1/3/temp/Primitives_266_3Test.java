package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_266_3Test {

  @Test
    @Timeout(8000)
  void testIsWrapperType_withWrapperTypes() {
    assertTrue(Primitives.isWrapperType(Integer.class));
    assertTrue(Primitives.isWrapperType(Float.class));
    assertTrue(Primitives.isWrapperType(Byte.class));
    assertTrue(Primitives.isWrapperType(Double.class));
    assertTrue(Primitives.isWrapperType(Long.class));
    assertTrue(Primitives.isWrapperType(Character.class));
    assertTrue(Primitives.isWrapperType(Boolean.class));
    assertTrue(Primitives.isWrapperType(Short.class));
    assertTrue(Primitives.isWrapperType(Void.class));
  }

  @Test
    @Timeout(8000)
  void testIsWrapperType_withNonWrapperTypes() {
    assertFalse(Primitives.isWrapperType(String.class));
    assertFalse(Primitives.isWrapperType(Object.class));
    assertFalse(Primitives.isWrapperType(int.class));
    assertFalse(Primitives.isWrapperType(null));
    assertFalse(Primitives.isWrapperType(Void.TYPE));
  }
}