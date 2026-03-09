package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_265_3Test {

  @Test
    @Timeout(8000)
  void testIsPrimitive_withPrimitiveClass() {
    assertTrue(Primitives.isPrimitive(int.class));
    assertTrue(Primitives.isPrimitive(boolean.class));
    assertTrue(Primitives.isPrimitive(void.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonPrimitiveClass() {
    assertFalse(Primitives.isPrimitive(String.class));
    assertFalse(Primitives.isPrimitive(Integer.class));
    assertFalse(Primitives.isPrimitive(Object.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonClassType() {
    Type type = new Type() {}; // anonymous Type implementation, not a Class
    assertFalse(Primitives.isPrimitive(type));
    assertFalse(Primitives.isPrimitive(null));
  }
}