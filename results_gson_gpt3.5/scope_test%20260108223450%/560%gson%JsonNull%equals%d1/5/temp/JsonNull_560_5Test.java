package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_560_5Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(jsonNull));
  }

  @Test
    @Timeout(8000)
  void testEquals_otherInstance() {
    JsonNull jsonNull1 = JsonNull.INSTANCE;
    JsonNull jsonNull2 = new JsonNull();
    assertTrue(jsonNull1.equals(jsonNull2));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    Object other = new Object();
    assertFalse(jsonNull.equals(other));
  }
}