package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_560_2Test {

  @Test
    @Timeout(8000)
  void testEquals_withSameInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(JsonNull.INSTANCE));
  }

  @Test
    @Timeout(8000)
  void testEquals_withNewInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(new JsonNull()));
  }

  @Test
    @Timeout(8000)
  void testEquals_withNull() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_withDifferentType() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.equals("some string"));
    assertFalse(jsonNull.equals(123));
    assertFalse(jsonNull.equals(new Object()));
  }
}