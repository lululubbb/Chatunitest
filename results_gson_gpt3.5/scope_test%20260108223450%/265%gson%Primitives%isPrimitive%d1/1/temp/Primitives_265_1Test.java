package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_265_1Test {

  @Test
    @Timeout(8000)
  void isPrimitive_withPrimitiveClass_shouldReturnTrue() {
    assertTrue(Primitives.isPrimitive(int.class));
    assertTrue(Primitives.isPrimitive(boolean.class));
    assertTrue(Primitives.isPrimitive(void.class));
  }

  @Test
    @Timeout(8000)
  void isPrimitive_withNonPrimitiveClass_shouldReturnFalse() {
    assertFalse(Primitives.isPrimitive(String.class));
    assertFalse(Primitives.isPrimitive(Object.class));
    assertFalse(Primitives.isPrimitive(Integer.class));
  }

  @Test
    @Timeout(8000)
  void isPrimitive_withNonClassType_shouldReturnFalse() {
    Type anonymousType = new Type() {};
    assertFalse(Primitives.isPrimitive(anonymousType));
    assertFalse(Primitives.isPrimitive(null));
  }
}