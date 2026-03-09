package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_265_6Test {

  @Test
    @Timeout(8000)
  void testIsPrimitive_withPrimitiveClass_returnsTrue() {
    assertTrue(Primitives.isPrimitive(int.class));
    assertTrue(Primitives.isPrimitive(boolean.class));
    assertTrue(Primitives.isPrimitive(void.class));
    assertTrue(Primitives.isPrimitive(double.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonPrimitiveClass_returnsFalse() {
    assertFalse(Primitives.isPrimitive(String.class));
    assertFalse(Primitives.isPrimitive(Object.class));
    assertFalse(Primitives.isPrimitive(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonClassType_returnsFalse() {
    Type anonType = new Type() {};
    assertFalse(Primitives.isPrimitive(anonType));
    assertFalse(Primitives.isPrimitive(null));
  }
}