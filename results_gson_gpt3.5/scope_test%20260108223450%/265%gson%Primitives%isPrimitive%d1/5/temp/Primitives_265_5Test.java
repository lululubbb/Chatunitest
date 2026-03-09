package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_265_5Test {

  @Test
    @Timeout(8000)
  void testIsPrimitive_withPrimitiveClass() {
    assertTrue(Primitives.isPrimitive(int.class));
    assertTrue(Primitives.isPrimitive(boolean.class));
    assertTrue(Primitives.isPrimitive(double.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonPrimitiveClass() {
    assertFalse(Primitives.isPrimitive(String.class));
    assertFalse(Primitives.isPrimitive(Object.class));
    assertTrue(Primitives.isPrimitive(void.class)); // void is considered primitive by isPrimitive()
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonClassType() {
    Type type = new Type() {};
    assertFalse(Primitives.isPrimitive(type));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNull() {
    assertFalse(Primitives.isPrimitive(null));
  }
}